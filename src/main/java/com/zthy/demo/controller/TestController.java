package com.zthy.demo.controller;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mongodb.gridfs.GridFSDBFile;
import com.zthy.demo.core.entity.UserInfo;
import com.zthy.demo.service.UserInfoService;

@Controller
public class TestController {
	
	@Resource
	private UserInfoService userInfoService;
	
	@RequestMapping(value="test",method=RequestMethod.GET)
	public String test(){
		UserInfo userInfo = new UserInfo();
		userInfo.setNickname("ÀîÐ¡Áú");
		userInfo.setUsername("Âõ¿Ë¶û");
		userInfo.setSex("ÄÐ");
		userInfo.setAge("33");
		userInfoService.add(userInfo);
		return "test";
	}
	
	@RequestMapping(value="testUploadFile",method=RequestMethod.GET)
	public String testUploadFile(){
		File file = new File("F:/Í¼Æ¬/2015-05-16_22-38-25-004.jpg");
		userInfoService.SaveFile("fs", file, "12123123.jpg");
		return "test2";
	}
	
	@RequestMapping(value="testRmFile",method=RequestMethod.GET)
	public String testRmFile(){
		userInfoService.RemoveFile("fs", new ObjectId("566a80907fe11b10b0c46d1a"));
		return "test3";
	}
}
