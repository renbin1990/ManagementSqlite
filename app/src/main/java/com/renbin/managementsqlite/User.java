package com.renbin.managementsqlite;

import com.renbin.managementsqlite.sql.annotion.DbField;
import com.renbin.managementsqlite.sql.annotion.DbTable;

/**
 * data:2021-07-30
 * Author:renbin
 * 八、添加注解
 */
@DbTable("tb_user")
public class User {

    @DbField("_id")
    private String id;

    @DbField("name")
    private String name;

    @DbField("password")
    private String password;

    @DbField("status")
    private Integer status;    // 1 登录 0 未登录

    public User() {

    }

    public User(String id, String name, String password, Integer status) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

