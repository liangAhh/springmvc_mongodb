package com.zthy.demo.core.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserInfo {
	
	private String id;
	private String username;
	private String nickname;
	private String sex;
	private String age;
	
	public UserInfo(){}
	
	public UserInfo(String username,String nickname,String sex,String age){
		super();
		this.username = username;
		this.nickname = nickname;
		this.sex = sex;
		this.age = age;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}
}
