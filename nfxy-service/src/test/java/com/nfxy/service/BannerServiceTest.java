package com.nfxy.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BannerServiceTest {

	public static void main(String[] args) {
		ApplicationContext ctxt = new ClassPathXmlApplicationContext("ApplicationContext-*.xml");
		System.out.println(ctxt);
		System.out.println(ctxt.getBeansOfType(BannerService.class));
	}

}
