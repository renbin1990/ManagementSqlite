package com.renbin.managementsqlite.sql;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

/**
 * data:2021-07-30
 * Author:renbin
 * 三、创建数据库工厂
 */
public class BaseDaoFactory {


    private SQLiteDatabase sqLiteDatabase;
    private String sqLiteDatabasePath;
    private static  BaseDaoFactory instance = new BaseDaoFactory();
    public static  BaseDaoFactory getInstance(){
        return instance;
    }
    public BaseDaoFactory(){
        sqLiteDatabasePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/user.db";
        sqLiteDatabase =  SQLiteDatabase.openOrCreateDatabase(sqLiteDatabasePath,null);
    }

    //四、创建数据库，建表
    public  synchronized  <R extends BaseDao<T>,T> R createBaseDao(Class<R> clazz ,Class<T> entityClass){
//        IBaseDao baseDao = null;
//
//         baseDao = new BaseDao();
//
//         return (R) baseDao;
        BaseDao baseDao = null;
        try {
            baseDao = clazz.newInstance();
            baseDao.init(entityClass,sqLiteDatabase);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (R) baseDao;
    }
}
