package com.renbin.managementsqlite.sql;

import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.renbin.managementsqlite.sql.enums.PrivateDataBaseEnums;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * data:2021-07-30
 * Author:renbin
 * 三、创建数据库工厂
 */
public class BaseDaoFactory {


    private SQLiteDatabase sqLiteDatabase;
    private String sqLiteDatabasePath;

    //总数据  库 只包含用户表  登录状态
    private SQLiteDatabase userDatabase;

    //保存所有的dao层，实现单例
    protected Map<String,BaseDao> map = Collections.synchronizedMap(new HashMap<>());

    private static  BaseDaoFactory instance = new BaseDaoFactory();

    public static  BaseDaoFactory getInstance(){
        return instance;
    }
    public BaseDaoFactory(){
//        sqLiteDatabasePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/user.db";
//        sqLiteDatabase =  SQLiteDatabase.openOrCreateDatabase(sqLiteDatabasePath,null);
        //二十、分库 分表
        //总表
        File file = new File(Environment.getExternalStorageDirectory(),"userDate");
        if (!file.exists()){
            file.mkdirs();
        }

        String userDatabasePath = file.getAbsolutePath()+"/user.db";
        //总数据库
        userDatabase =  SQLiteDatabase.openOrCreateDatabase(userDatabasePath,null);

    }
    //userdao
    public synchronized <T extends BaseDao<M>,M> T getUserDao(Class<T> daoClass,Class<M> entityClass){
        BaseDao baseDao = null;
//        if (map.get(entityClass.getAnnotation(DbTable.class).value()) != null){
//            return (T) map.get(entityClass.getSimpleName());
//        }

        if (map.get(daoClass.getSimpleName()) != null){
            return (T) map.get(daoClass.getSimpleName());
        }
        //没有userdao 实例化  缓存
        try {
            baseDao = daoClass.newInstance();
            baseDao.init(entityClass,userDatabase);
            map.put(daoClass.getSimpleName(),baseDao);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return (T) baseDao;
    }

    //appdao  二十三、 个人数据库
    public synchronized <T> BaseDao<T> getAppDao(Class<T> entityClass){
        BaseDao baseDao = null;
        if (sqLiteDatabase == null){
            //通过枚举 获取个人数据库路径
            sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(PrivateDataBaseEnums.database.getValue(),null);
        }

        try {
            baseDao = BaseDao.class.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        baseDao.init(entityClass,sqLiteDatabase);
        return baseDao;
    }


    //四、创建数据库，建表
    public  synchronized  <R extends BaseDao<T>,T> R createBaseDao(Class<R> clazz ,Class<T> entityClass){
//        IBaseDao baseDao = null;
//         baseDao = new BaseDao();
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
