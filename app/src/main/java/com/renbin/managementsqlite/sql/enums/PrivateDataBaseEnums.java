package com.renbin.managementsqlite.sql.enums;

import android.os.Environment;

import com.renbin.managementsqlite.User;
import com.renbin.managementsqlite.UserDao;
import com.renbin.managementsqlite.sql.BaseDaoFactory;

import java.io.File;

/**
 * 二十四、
 */
public enum  PrivateDataBaseEnums {

    database("Msg3.0.db");
    private String value;

    PrivateDataBaseEnums(String value) {
        this.value = value;
    }

    public String getValue() {
        UserDao userDao = BaseDaoFactory.getInstance().getUserDao(UserDao.class, User.class);
        User currectUser = userDao.getCurrectUser();
        if (currectUser != null){
            File file = new File(Environment.getExternalStorageDirectory(),"userDate");
            if (!file.exists()){
                file.mkdirs();
            }
            File userFile = new File(file,currectUser.getName());
            if (!userFile.exists()){
                userFile.mkdirs();
            }
            String userDatabasePath = userFile.getAbsolutePath()+"/"+value;
            return userDatabasePath;
        }
        return null;
    }
}
