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
 * mongodb ���ݷ���basedaoʵ����
 * @author ����
 *
 */
@Repository(value="baseDao")
public class MongoDBBaseDaoImpl implements BaseDao{
	
	@Autowired
	@Qualifier("mongoTemplate")
	protected MongoTemplate mongoTemplate;

	
	/*
	 * �����������ض���
	 * @see com.zthy.demo.core.dao.BaseDao#findById(java.lang.Class, java.lang.String)
	 */
	@Override
	public <T> T findById(Class<T> entityClass, String id) {
		return this.mongoTemplate.findById(id, entityClass);
	}

	/*
	 * �������ȡȫ���Ķ����б�
	 * @see com.zthy.demo.core.dao.BaseDao#findAll(java.lang.Class)
	 */
	@Override
	public <T> List<T> findAll(Class<T> entityClass) {
		return this.mongoTemplate.findAll(entityClass);
	}

	/*
	 * ɾ��һ������
	 * @see com.zthy.demo.core.dao.BaseDao#remove(java.lang.Object)
	 */
	@Override
	public void remove(Object obj) {
		this.mongoTemplate.remove(obj);
	}

	/*
	 * ���һ������
	 * ���Զ�Ϊ������Ϣ���� ʵ���� λ��
	 * @see com.zthy.demo.core.dao.BaseDao#add(java.lang.Object)
	 */
	@Override
	public void add(Object obj) {
		this.mongoTemplate.insert(obj);
	}
	
	/*
	 * ��ӻ��޸�һ������
	 * @see com.zthy.demo.core.dao.BaseDao#saveOrUpdate(java.lang.Object)
	 */
	@Override
	public void saveOrUpdate(Object obj) {
		this.mongoTemplate.save(obj);
	}

	/*
	 * ��ҳ��ѯ
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
	 *            ��ѯ����
	 * @param query
	 *            ��ѯ����
	 * @return
	 */
	public <T> Long count(Class<T> entityClass, Query query) {
		return this.mongoTemplate.count(query, entityClass);
	}

	/*
	 * query��ѯ����
	 * @see com.zthy.demo.core.dao.BaseDao#find(java.lang.Class, org.springframework.data.mongodb.core.query.Query)
	 */
	@Override
	public <T> T find(Class<T> entityClass, Query query) {
		return this.mongoTemplate.findOne(query, entityClass);
	}
	
	/**
	 * �洢�ļ�
	 * @param collectionName ������
	 * @param file �ļ�
	 * @param filename �ļ�����
	 */
	@Override
	public void SaveFile(String collectionName, File file, String filename) {
		try {
			DB db = this.mongoTemplate.getDb();
			// �洢fs�ĸ��ڵ�
            GridFS gridFS = new GridFS(db, collectionName);
            GridFSInputFile gfs = gridFS.createFile(file);
            gfs.setFilename(filename);
            gfs.setContentType(filename.substring(filename.lastIndexOf(".")+1));
            gfs.save();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("�洢�ļ�ʱ��������");
		}
	}

	/**
	 * �����ļ�����ȡ�ļ�
	 * @param collectionName ������
	 * @param filename �ļ���
	 */
	@Override
	public GridFSDBFile retrieveFileOne(String collectionName, String filename) {
		try {
			DB db = this.mongoTemplate.getDb();
			// ��ȡfs�ĸ��ڵ�
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
	 * �����ļ���ɾ���ļ�
	 * @param collectionName ������
	 * @param filename �ļ���
	 */
	@Override
	public void RemoveFile(String collectionName, String filename) {
		try {
			DB db = this.mongoTemplate.getDb();
			// ��ȡfs�ĸ��ڵ�
            GridFS gridFS = new GridFS(db, collectionName);
            gridFS.remove(filename);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ɾ��ͼƬʧ��");
		}
	}

	/**
	 * �����ļ�idɾ���ļ�
	 * @param collectionName ������
	 * @param id �ļ�id
	 */
	@Override
	public void RemoveFile(String collectionName, ObjectId id) {
		try {
			DB db = this.mongoTemplate.getDb();
			// ��ȡfs�ĸ��ڵ�
            GridFS gridFS = new GridFS(db, collectionName);
            gridFS.remove(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ɾ��ͼƬʧ��");
		}
	}

	/**
	 * ���� DBObject ɾ���ļ�
	 * @param query 
	 */
	@Override
	public void RemoveFile(DBObject query) {
		try {
			DB db = this.mongoTemplate.getDb();
			// ��ȡfs�ĸ��ڵ�
            GridFS gridFS = new GridFS(db);
            gridFS.remove(query);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ɾ��ͼƬʧ��");
		}
	}

	/**
	 * �ļ�id��ȡ�ļ���Ϣ
	 * @param collectionName ������
	 * @param id �ļ�id
	 */
	@Override
	public GridFSDBFile retrieveFileOne(String collectionName, ObjectId id) {
		try {
			DB db = this.mongoTemplate.getDb();
			// ��ȡfs�ĸ��ڵ�
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
	 * ����ĵ�
	 * @see com.zthy.demo.core.dao.BaseDao#add(java.lang.Object, java.lang.String)
	 */
	@Override
	public void add(Object obj, String collection) {
		this.mongoTemplate.insert(obj,collection);
	}

	/**
	 * �޸��ĵ�
	 */
	@Override
	public void saveOrUpdate(Object obj, String conllection) {
		this.mongoTemplate.save(obj, conllection);
	}

	/**
	 * ���������޸��ĵ�
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
