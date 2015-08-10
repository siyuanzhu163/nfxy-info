package com.nfxy.manager.upload;

/**
 * 图片类型
 * @author SIYUAN
 */
public enum PictureTypeEnum {
	
	INFO_CONTENT("资讯正文", null, null, "/info/content"),
	INFO_COVER("资讯封面", null, 
			new IMGAttr[]{
				new IMGAttr(1080, 677), new IMGAttr(720, 451), 
				new IMGAttr(206, 206), new IMGAttr(137, 137)
			},
			"/info/cover"),
	BANNER("轮播图", null, null, "/banner");
	
	/**
	 * 类型描述
	 */
	private String desc;
	
	/**
	 * 该类型图片的规格，null表示无限制
	 */
	private IMGAttr imgAttr;
	
	/**
	 * 图片需要使用的缩放尺寸
	 */
	private IMGAttr[] imgResizeAttrs;
	
	/**
	 * 在图片服务器中的路径
	 */
	private String path;
	
	private PictureTypeEnum(String desc, IMGAttr imgAttr, IMGAttr[] imgResizeAttrs, String path) {
		this.desc = desc;
		this.imgAttr = imgAttr;
		this.imgResizeAttrs = imgResizeAttrs;
		this.path = path;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public IMGAttr getImgAttr() {
		return imgAttr;
	}

	public void setImgAttr(IMGAttr imgAttr) {
		this.imgAttr = imgAttr;
	}
	
	public IMGAttr[] getImgResizeAttrs() {
		return imgResizeAttrs;
	}

	public void setImgResizeAttrs(IMGAttr[] imgResizeAttrs) {
		this.imgResizeAttrs = imgResizeAttrs;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
}
