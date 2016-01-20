package com.zthy.demo.core.dao;

import java.io.File;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFSDBFile;
import com.zthy.demo.core.util.Page;

public interface BaseDao {
	<T> T find(Class<T> entityClass,Query query);
	
	<T> T findById(Class<T> entityClass,String id);
	
	<T> List<T> findAll(Class<T> entityClass);
	
	void remove(Object obj);
	
	<T> void remove(String id,Class<T> entityClass);
	
	void add(Object obj);
	
	void add(Object obj, String collection);
	
	void saveOrUpdate(Object obj);
	
	void saveOrUpdate(Object obj,String conllection);
	
	<T> WriteResult updateObject(Class<T> entityClass,Query query,Update update);
	
	<T> List<T> findByQuery(Class<T> entityClass, Query query, Page page);
	
	void SaveFile(String collectionName,File file,String newfilename);
	
	GridFSDBFile retrieveFileOne(String collectionName, String filename);
	
	GridFSDBFile retrieveFileOne(String collectionName, ObjectId filename);
	
	void RemoveFile(String collectionName, String filename);
	
	void RemoveFile(String collectionName, ObjectId id);
	
	void RemoveFile(DBObject query);
}
