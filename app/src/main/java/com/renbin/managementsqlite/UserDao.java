package com.renbin.managementsqlite;

import android.util.Log;

import com.renbin.managementsqlite.sql.BaseDao;

import java.util.List;

/**
 * data:2021-07-30
 * Author:renbin
 * 十、User表扩展类，增删改查,数据处理
 */
public class UserDao extends BaseDao<User> {


    @Override
    public Long insert(User entity) {
        //二十一、
        List<User> query = query(new User());
        User where = null;
        for (User user : query){
            where = new User();
            where.setId(user.getId());
            user.setStatus(0);
            Log.e("---->" , "用户 "+user.getName() + "更改为未登录状态");
            update(user,where);
        }
        Log.e("---->" , "用户 "+entity.getName() + "更改为登录状态");
        entity.setStatus(1);

        return super.insert(entity);
    }

    @Override
    public List<User> query(User where) {
        return super.query(where);
    }

    /**
     * //二十二、当前登录的用户
     */
    public User getCurrectUser(){
        User user = new User();
        user.setStatus(1);
        List<User> query = query(user);
        if (query.size()>0){
            return query.get(0);
        }
        return null;
    }
}
