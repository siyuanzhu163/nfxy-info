package com.nfxy.manager.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.nfxy.manager.AJAXResponse;
import com.nfxy.manager.exception.IllegalImgException;
import com.nfxy.manager.upload.IMGAttr;
import com.nfxy.manager.upload.PictureTypeEnum;
import com.nfxy.service.PictureService;
import com.nfxy.util.UUIDUtils;

/**
 * 图片上传
 * @author SIYUAN
 */
@Controller
@RequestMapping("/manager/picture")
public class PictureUploadController extends BaseController {
	
	private static final Logger LOGGER = 
			LoggerFactory.getLogger(PictureUploadController.class);
	
	@Autowired
	private PictureService pictureService;
	
	private ExecutorService resizePicturePool = Executors.newFixedThreadPool(4);
	
	private static final String SIZE_PLACEHOLDER = "{size}";
	
	private static final String ORIGINAL_VALUE = "original";
	
	@ResponseBody
	@RequestMapping(value = "/{pictureType}", method = RequestMethod.POST)
	public AJAXResponse<String> upload(
			@PathVariable("pictureType") PictureTypeEnum pictureType,
			@RequestParam("file") MultipartFile file) {
		AJAXResponse<String> ajaxResp = new AJAXResponse<String>();
		InputStream inputValid = null;
		try {
			inputValid = file.getInputStream();
			BufferedImage image = ImageIO.read(inputValid);
			if (image == null) {
				throw new IllegalImgException("上传的图片格式错误");
			}
			
			validate(pictureType, image);
			
			ajaxResp.setContent(save(pictureType, file));
		} catch (Exception e) {
			LOGGER.error("fail to save the picture", e);
			ajaxResp.setStatus(AJAXResponse.FAIL);
			ajaxResp.setMsg(e.getMessage());
		} finally {
			IOUtils.closeQuietly(inputValid);
		}
		return ajaxResp;
	}
	
	/**
	 * 上传图片规格校验
	 * @param image
	 * @param pictureType
	 * @throws IllegalImgException
	 */
	protected void validate(PictureTypeEnum pictureType, BufferedImage image) 
			throws IllegalImgException {
		if (image == null)
			throw new IllegalArgumentException("图片不能为空");
		IMGAttr imgAttr = pictureType.getImgAttr();
		//该类型的图片对规格无限制
		if (imgAttr == null) {
			return;
		}
		//图片符合规格
		if (image.getWidth() == imgAttr.getWidth()
				&& image.getHeight() == imgAttr.getHeight()) {
			return;
		}
		StringBuffer result = new StringBuffer();
		result.append("上传图片规格不正确，请上传")
			.append(imgAttr.getWidth())
			.append("x")
			.append(imgAttr.getHeight())
			.append("格式的图片");
		throw new IllegalImgException(result.toString());
	}
	
	/**
	 * 保存图片
	 * @param pictureType
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private String save(PictureTypeEnum pictureType, final MultipartFile file) throws IOException {
		InputStream inputSave = null;
		try {
			// original
			inputSave = file.getInputStream();
			String fileName = file.getOriginalFilename();
			String suffix = fileName.substring(fileName.indexOf("."));
			String originalPath = getPath(pictureType, ORIGINAL_VALUE) + suffix;
			String fileURL = pictureService.save(inputSave, originalPath);
			
			// resized
			if (pictureType.getImgResizeAttrs() != null) {
				for (final IMGAttr imgAttr : pictureType.getImgResizeAttrs()) {
					resizePicturePool.submit(new ResizePictureSaver(
							imgAttr.getWidth(), imgAttr.getHeight(), file,
							originalPath));
				}
			}
			
			return fileURL.replace(ORIGINAL_VALUE, SIZE_PLACEHOLDER);
		} finally {
			IOUtils.closeQuietly(inputSave);
		}
	}
	
	/**
	 * 获取图片的保存路径
	 * @return
	 */
	protected String getPath(PictureTypeEnum pictureType, String size) {
		StringBuilder path = new StringBuilder();
		return path.append(pictureType.getPath())
				.append("/").append(size)
				.append("/").append(UUIDUtils.getUUID())
				.toString();
	}
	
	private static final Logger LOGGER_RESIZE_PICTURE = 
			LoggerFactory.getLogger(ResizePictureSaver.class);
	
	/**
	 * 线程保存缩略版图片
	 */
	private class ResizePictureSaver implements Runnable {
		
		// 缩略图片的宽
		private int width;
		
		// 缩略图片的高
		private int height;
		
		// 原始图片
		private MultipartFile file;
		
		// 原始图片的保存路径
		private String originalPath;
		
		public ResizePictureSaver(int width, int height, MultipartFile file,
				String originalPath) {
			this.width = width;
			this.height = height;
			this.file = file;
			this.originalPath = originalPath;
		}

		@Override
		public void run() {
			InputStream input = null;
			try {
				input = file.getInputStream();
				// 图片的缩略
				ByteArrayOutputStream bufferOut = new ByteArrayOutputStream();
				Thumbnails.of(input).size(this.width, this.height).toOutputStream(bufferOut);
				// 图片的保存路径
				String path = originalPath.replace(ORIGINAL_VALUE, 
						this.width + "x" + this.height);
				// 保存
				pictureService.save(bufferOut.toByteArray(), path);
			} catch (IOException e) {
				LOGGER_RESIZE_PICTURE.error("fail to save the resized picture", e);
			} finally {
				IOUtils.closeQuietly(input);
			}
		}
		
	}
	
}
