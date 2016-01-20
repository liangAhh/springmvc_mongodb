package com.zthy.demo.service.impl;

import org.springframework.stereotype.Service;

import com.zthy.demo.core.dao.impl.MongoDBBaseDaoImpl;
import com.zthy.demo.service.UserInfoService;

@Service(value="userInfoService")
public class UserInfoServiceImpl extends MongoDBBaseDaoImpl implements UserInfoService{

}
