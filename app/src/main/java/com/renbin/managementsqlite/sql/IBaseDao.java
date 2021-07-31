package com.renbin.managementsqlite.sql;

import java.util.List;

/**
 * data:2021-07-30
 * Author:renbin
 * 一、
 */
public interface IBaseDao<T> {
    //增 数据库插入的时候 返回值就是long 统一
    Long insert(T entity);
    // 删
    int delete(T where);
    // 改
    int update(T entity,T where);
    //查
    List<T> query(T where);
    List<T> query(T where,String groupBy, String having,
                  String orderBy, Integer startIndex, Integer limit);
}
