package com.zthy.demo.core.dao.impl;

import java.io.File;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import com.zthy.demo.core.dao.BaseDao;
import com.zthy.demo.core.util.Page;

/**
 * mongodb 数据泛型basedao实现类
 * @author 梁冲
 *
 */
@Repository(value="baseDao")
public class MongoDBBaseDaoImpl implements BaseDao{
	
	@Autowired
	@Qualifier("mongoTemplate")
	protected MongoTemplate mongoTemplate;

	
	/*
	 * 根据主键返回对象
	 * @see com.zthy.demo.core.dao.BaseDao#findById(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T> T findById(Class<T> entityClass, String id) {
		return this.mongoTemplate.findById(id, entityClass);
	}

	/*
	 * 根据类获取全部的对象列表
	 * @see com.zthy.demo.core.dao.BaseDao#findAll(java.lang.Class)
	 */
	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		return this.mongoTemplate.findAll(entityClass);
	}

	/*
	 * 删除一个对象
	 * @see com.zthy.demo.core.dao.BaseDao#remove(java.lang.Object)
	 */
	@Override
	public void remove(Object obj) {
		this.mongoTemplate.remove(obj);
	}

	/*
	 * 添加一个对象
	 * 会自动为集合信息加入 实体类 位置
	 * @see com.zthy.demo.core.dao.BaseDao#add(java.lang.Object)
	 */
	@Override
	public void add(Object obj) {
		this.mongoTemplate.insert(obj);
	}
	
	/*
	 * 添加或修改一个对象
	 * @see com.zthy.demo.core.dao.BaseDao#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public void saveOrUpdate(Object obj) {
		this.mongoTemplate.save(obj);
	}

	/*
	 * 分页查询
	 * @see com.zthy.demo.core.dao.BaseDao#findByQuery(java.lang.Class, org.springframework.data.mongodb.core.query.Query, com.zthy.demo.core.util.Page)
	 */
	@Override
	public <T> List<T> findByQuery(Class<T> entityClass, Query query, Page page) {
		Long count = this.count(entityClass, query);
		page.setCount(count);
		int pageNumber = page.getCurrent();
		int pageSize = page.getPageCount();
		query.skip((pageNumber - 1) * pageSize).limit(pageSize);
		return this.mongoTemplate.find(query, entityClass);
	}
	
	/**
	 * 
	 * @param entityClass
	 *            查询对象
	 * @param query
	 *            查询条件
	 * @return
	 */
	public <T> Long count(Class<T> entityClass, Query query) {
		return this.mongoTemplate.count(query, entityClass);
	}

	/*
	 * query查询对象
	 * @see com.zthy.demo.core.dao.BaseDao#find(java.lang.Class, org.springframework.data.mongodb.core.query.Query)
	 */
	@Override
	public <T> T find(Class<T> entityClass, Query query) {
		return this.mongoTemplate.findOne(query, entityClass);
	}
	
	/**
	 * 存储文件
	 * @param collectionName 集合名
	 * @param file 文件
	 * @param filename 文件名称
	 */
	@Override
	public void SaveFile(String collectionName, File file, String filename) {
		try {
			DB db = this.mongoTemplate.getDb();
			// 存储fs的根节点
            GridFS gridFS = new GridFS(db, collectionName);
            GridFSInputFile gfs = gridFS.createFile(file);
            gfs.setFilename(filename);
            gfs.setContentType(filename.substring(filename.lastIndexOf(".")+1));
            gfs.save();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("存储文件时发生错误");
		}
	}

	/**
	 * 根据文件名读取文件
	 * @param collectionName 集合名
	 * @param filename 文件名
	 */
	@Override
	public GridFSDBFile retrieveFileOne(String collectionName, String filename) {
		try {
			DB db = this.mongoTemplate.getDb();
			// 获取fs的根节点
            GridFS gridFS = new GridFS(db, collectionName);
            GridFSDBFile dbfile = gridFS.findOne(filename);
            if (dbfile != null) {
                return dbfile;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据文件名删除文件
	 * @param collectionName 集合名
	 * @param filename 文件名
	 */
	@Override
	public void RemoveFile(String collectionName, String filename) {
		try {
			DB db = this.mongoTemplate.getDb();
			// 获取fs的根节点
            GridFS gridFS = new GridFS(db, collectionName);
            gridFS.remove(filename);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("删除图片失败");
		}
	}

	/**
	 * 根据文件id删除文件
	 * @param collectionName 集合名
	 * @param id 文件id
	 */
	@Override
	public void RemoveFile(String collectionName, ObjectId id) {
		try {
			DB db = this.mongoTemplate.getDb();
			// 获取fs的根节点
            GridFS gridFS = new GridFS(db, collectionName);
            gridFS.remove(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("删除图片失败");
		}
	}

	/**
	 * 根据 DBObject 删除文件
	 * @param query 
	 */
	@Override
	public void RemoveFile(DBObject query) {
		try {
			DB db = this.mongoTemplate.getDb();
			// 获取fs的根节点
            GridFS gridFS = new GridFS(db);
            gridFS.remove(query);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("删除图片失败");
		}
	}

	/**
	 * 文件id获取文件信息
	 * @param collectionName 集合名
	 * @param id 文件id
	 */
	@Override
	public GridFSDBFile retrieveFileOne(String collectionName, ObjectId id) {
		try {
			DB db = this.mongoTemplate.getDb();
			// 获取fs的根节点
            GridFS gridFS = new GridFS(db, collectionName);
            GridFSDBFile dbfile = gridFS.findOne(id);
            if (dbfile != null) {
                return dbfile;
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 添加文档
	 * @see com.zthy.demo.core.dao.BaseDao#add(java.lang.Object, java.lang.String)
	 */
	@Override
	public void add(Object obj, String collection) {
		this.mongoTemplate.insert(obj,collection);
	}

	/**
	 * 修改文档
	 */
	@Override
	public void saveOrUpdate(Object obj, String conllection) {
		this.mongoTemplate.save(obj, conllection);
	}

	/**
	 * 根据条件修改文档
	 */
	@Override
	public <T> WriteResult updateObject(Class<T> entityClass, Query query, Update update) {
		return this.mongoTemplate.updateFirst(query, update, entityClass);
	}

	@Override
	public <T> void remove(String id, Class<T> entityClass) {
		this.mongoTemplate.remove(findById(entityClass, id));
	}

}
