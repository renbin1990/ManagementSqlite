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
    @DbField("tb_name")
    private String name;

    @DbField("tb_age")
    private Integer age;

    public User() {

    }
    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
