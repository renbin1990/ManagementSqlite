package com.renbin.managementsqlite;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.renbin.managementsqlite.sql.BaseDao;
import com.renbin.managementsqlite.sql.BaseDaoFactory;
import com.renbin.managementsqlite.sql.IBaseDao;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    IBaseDao<User> mUserIBaseDao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermission(this);
        mUserIBaseDao = BaseDaoFactory.getInstance().getUserDao(UserDao.class,User.class);
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
        mUserIBaseDao.insert(new User("3","秀秀美女","123456",1));
        Toast.makeText(this,"执行成功",Toast.LENGTH_LONG).show();
    }


    public void insertPerson(View view) {
        BaseDao<Photo> appDao = BaseDaoFactory.getInstance().getAppDao(Photo.class);
        appDao.insert(new Photo("2021-7-31","/ssa/asda/asdas","她是一个大美女，大美女"));
    }

    public void update(View view) {
        User user = new User();
        user.setStatus(1);
        mUserIBaseDao.update(new User("4","renbin","123456",18),user);
    }

    public void delete(View view) {
        User user = new User();
        user.setStatus(1);
        mUserIBaseDao.delete(user);
    }

    public void query(View view) {
        List<User> query = mUserIBaseDao.query(new User());
        Log.e("---->","query  "+query.size());
    }

    public void queryPerson(View view) {
        BaseDao<Photo> appDao = BaseDaoFactory.getInstance().getAppDao(Photo.class);
        List<Photo> query = appDao.query(new Photo());
        Log.e("---->",query.size()+"");
    }
}