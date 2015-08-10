package com.nfxy.api.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.nfxy.api.AJAXResponse;
import com.nfxy.api.APIPropertyConstant;
import com.nfxy.entity.Banner;
import com.nfxy.entity.Info;
import com.nfxy.entity.enums.PartEnum;
import com.nfxy.entity.enums.StatusEnum;
import com.nfxy.service.BannerService;

/**
 * APP-轮播图接口
 * @author SIYUAN
 */
@Controller("apiBannerController")
@RequestMapping("/api/banner/{part}")
public class APIBannerController extends APIBaseController {
	
	@Autowired
	private BannerService bannerService;
	
	/**
	 * 获取模块下的所有轮播图
	 * @param part 模块
	 * @return
	 */
	@ResponseBody
	@RequestMapping("")
	public AJAXResponse<List<Banner>> query(@PathVariable("part") String part) {
		PartEnum partVal = validatePart(part);
		
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("part", partVal);
		criteria.put("status", StatusEnum.ENABLED);
		PageBounds page = new PageBounds(1, 10);
		
		AJAXResponse<List<Banner>> result = new AJAXResponse<List<Banner>>();
		result.setContent(bannerService.query(criteria, page));
		result.setSerializeFilters(new SerializeFilter[]{
				new SimplePropertyPreFilter(Banner.class, APIPropertyConstant.BANNER_lIST),
				new SimplePropertyPreFilter(Info.class, APIPropertyConstant.BANNER_lIST_INFO)});
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/{id}/view")
	public AJAXResponse<String> view(@PathVariable("part") String part,
			@PathVariable("id") long id) {
		validatePart(part);
		
		AJAXResponse<String> result = new AJAXResponse<String>();
		
		bannerService.viewStatistic(id);
		
		return result;
	}
	
	@ResponseBody
	@RequestMapping("/{id}/share")
	public AJAXResponse<String> share(@PathVariable("part") String part,
			@PathVariable("id") long id) {
		validatePart(part);
		
		AJAXResponse<String> result = new AJAXResponse<String>();
		
		bannerService.shareStatistic(id);
		
		return result;
	}
	
	private PartEnum validatePart(String part) {
		try {
			return PartEnum.valueOf(StringUtils.upperCase(part));
		} catch (Exception e) {
			throw new IllegalArgumentException("无效的模块值");
		}
	}
	
}
