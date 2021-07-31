package com.renbin.managementsqlite.sql;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.renbin.managementsqlite.sql.annotion.DbField;
import com.renbin.managementsqlite.sql.annotion.DbTable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * data:2021-07-30
 * Author:renbin
 * 二、数据库框架层实例化对象，工厂模式
 */
public class BaseDao<T> implements IBaseDao<T> {
    private static final String TAG = "BaseDao";
    /**
     * 五、持有数据库操作类的引用
     */
    private SQLiteDatabase database;
    /**
     * 持有操作数据库表对应的java类型
     */
    private Class<T> entityClass;

    /**
     * 保证实例化一次
     */
    private boolean isInit = false;
    private String tableName;

    /**
     * 十二、检查表
     */
    private HashMap<String, Field> cacheMap;

    protected BaseDao() {

    }

    /**
     * 六、自动建表方法实现
     *
     * @param entity
     * @param sqLiteDatabase
     * @return
     */
    protected synchronized boolean init(Class<T> entity, SQLiteDatabase sqLiteDatabase) {
        if (!isInit) {
            //初始化完成,自动建表
            entityClass = entity;
            database = sqLiteDatabase;

            //sqlite 没有String 只有text
            //自动拼接创建数据库语句
            if (entity.getAnnotation(DbTable.class) == null) {
                //如果没有注解，就获取注解对应类的名字
                tableName = entity.getClass().getSimpleName();
            } else {
                //如果有注解，就有注解的名称
                tableName = entity.getAnnotation(DbTable.class).value();
            }

            if (!database.isOpen()) {
                return false;
            }
            //创建表 create table if not exists user{name TEXT,age INTEGER}
            String sql = createTable();
            try {
                database.execSQL(sql);
            } catch (Exception e) {
                isInit = false;
                return false;
            }
            //简历映射关系
            initCacheMap();
            isInit = true;
        }
        return isInit;
    }

    /**
     * 十三、初始化数据缓冲
     * 将真实表的列名 +成员变量映射
     * 优化 提前构建好 成员变量
     */
    private void initCacheMap() {
        cacheMap = new HashMap<>();
        //   String sql = "select * form "+tableName + " limit 0";
        String sql = "select * from " + tableName + " limit 0";

        Cursor cursor = database.rawQuery(sql, null);
        String[] columnNames = cursor.getColumnNames();
        Field[] columnFields = entityClass.getDeclaredFields();

        for (String columnName : columnNames) {
            Field resultField = null;
            for (Field field : columnFields) {
                String fieldAnnotationName = field.getAnnotation(DbField.class).value();
                if (columnName.equals(fieldAnnotationName)) {
                    resultField = field;
                    break;
                }
            }
            if (resultField != null) {
                cacheMap.put(columnName, resultField);
            }
        }
    }

    /**
     * 九、自动建表 sql语句
     * create table if not exists user( name TEXT,age INTEGER ）
     */
    protected String createTable() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("create table if not exists ");
        stringBuffer.append(tableName + " (");        //表名
        //获取bean里面子元素集合，转换成数据库对应的数据类型
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            Class type = field.getType();
            if (type == String.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value() + " TEXT,");
            } else if (type == Double.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value() + "  DOUBLE,");
            } else if (type == Integer.class) {
                Log.e(TAG, "createTable " + field.toString());
                stringBuffer.append(field.getAnnotation(DbField.class).value() + "  INTEGER,");
            } else if (type == Long.class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value() + "  BIGINT,");
            } else if (type == byte[].class) {
                stringBuffer.append(field.getAnnotation(DbField.class).value() + "  BLOB,");
            } else {
                  /*
                不支持的类型
                 */
                continue;
            }
        }

        //去除最后一个逗号
        if (stringBuffer.charAt(stringBuffer.length() - 1) == ',') {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }

        stringBuffer.append(" )");
        Log.e(TAG, "createTable " + stringBuffer.toString());
        return stringBuffer.toString();
    }

    /**
     * 十一、插入数据
     * 1创建ContentValues contentValues = new ContentValues();
     * 2contentValues添加数据表参数
     * 3提交insert
     *
     * @param entity
     * @return
     */
    @Override
    public Long insert(T entity) {
        //1 entity转hashmap
        Map<String, String> map = getValue(entity);
        //2 hashmap转ContentValues
        ContentValues contentValues = getContentValues(map);
        return database.insert(tableName, null, contentValues);
    }

    /**
     * 十四、
     *
     * @param entity
     * @return
     */
    private Map<String, String> getValue(T entity) {
        HashMap<String, String> map = new HashMap<>();
        Iterator<Field> fieldIterator = cacheMap.values().iterator();
        while (fieldIterator.hasNext()) {
            Field field = fieldIterator.next();
            field.setAccessible(true);
            try {
                Object object = field.get(entity);
                if (object == null) {
                    continue;
                }
                String value = object.toString();
                String key = field.getAnnotation(DbField.class).value();
                if (!TextUtils.isEmpty(value) && !TextUtils.isEmpty(key)) {
                    map.put(key, value);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }


        return map;
    }

    private ContentValues getContentValues(Map<String, String> map) {
        ContentValues contentValues = new ContentValues();
        Set<String> keys = map.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = map.get(key);
            if (value != null) {
                contentValues.put(key, value);
            }
        }
        return contentValues;
    }

    @Override
    public int delete(T where) {
        // database.delete(tableName,"age=?" ,new String[]{"99"});
        Map<String, String> whereMap = getValue(where);
        Condition condition = new Condition(whereMap);
        database.delete(tableName, condition.getWhereClause(), condition.getWhereArgs());
        return 0;
    }

    /**
     * 十五、
     *
     * @param entity
     * @param where
     * @return
     */
    @Override
    public int update(T entity, T where) {
        Map<String, String> value = getValue(entity);
        ContentValues contentValues = getContentValues(value);
        //条件
        Map<String, String> whereMap = getValue(where);
        Condition condition = new Condition(whereMap);
        database.update(tableName, contentValues, condition.getWhereClause(), condition.getWhereArgs());
        return 0;
    }

    /**
     * 十七、封装 String whereClause, String[] whereArgs 这两个参数
     *
     * @return
     */
    class Condition {
        private String whereClause;
        private String[] whereArgs;

        public Condition(Map<String, String> whereClause) {
            List list = new ArrayList();
            StringBuilder stringBuilder = new StringBuilder();
            //承上启下的作用，防止恶意传值 报错
            stringBuilder.append(" 1=1 ");
            Set<String> keys = whereClause.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = whereClause.get(key);
                if (value != null) {
                    stringBuilder.append(" and  " + key + " =? ");
                    list.add(value);
                }
            }
            this.whereClause = stringBuilder.toString();
            this.whereArgs = (String[]) list.toArray(new String[list.size()]);
        }

        public String getWhereClause() {
            return whereClause;
        }

        public String[] getWhereArgs() {
            return whereArgs;
        }
    }

    /**
     * 十八、
     *
     * @param where 查询条件
     * @return
     */
    @Override
    public List<T> query(T where) {
        return query(where, null, null, null, null, null);
    }

    @Override
    public List<T> query(T where, String groupBy, String having,
                         String orderBy, Integer startIndex, Integer limit) {
        String limitString = null;
        if (startIndex != null && limit != null){
            limitString = startIndex+ " , "+limit;
        }
        //条件
        Map<String, String> whereMap = getValue(where);
        Condition condition = new Condition(whereMap);

        Cursor cursor = database.query(tableName, null, condition.getWhereClause(), condition.getWhereArgs(),
                groupBy, having, orderBy, limitString
        );
        //封装  返回
        List<T> result = getResult(cursor,where);

        return null;
    }

    private List<T> getResult(Cursor cursor, T where) {
        ArrayList<T> list = new ArrayList<>();
        while (cursor.moveToFirst()){

        }
        return null;
    }
}
