package com.renbin.managementsqlite;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.renbin.managementsqlite.sql.BaseDaoFactory;
import com.renbin.managementsqlite.sql.IBaseDao;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    IBaseDao<User> mUserIBaseDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission(this);
        mUserIBaseDao = BaseDaoFactory.getInstance().createBaseDao(UserDao.class,User.class);
    }

    public static boolean checkPermission(
            Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

        }
        return false;
    }

    public void insert(View view) {
        mUserIBaseDao.insert(new User("renbin",123456));
        mUserIBaseDao.insert(new User("renbin",123));
        mUserIBaseDao.insert(new User("renbin",234));
        mUserIBaseDao.insert(new User("renbin",2342));
        mUserIBaseDao.insert(new User("renbin",675));
    }

    public void update(View view) {
        User user = new User();
        user.setAge(2342);
        mUserIBaseDao.update(new User("xiuxiu",18),user);
    }

    public void delete(View view) {
        User user = new User();
        user.setAge(123456);
        mUserIBaseDao.delete(user);
    }

    public void query(View view) {

    }
}