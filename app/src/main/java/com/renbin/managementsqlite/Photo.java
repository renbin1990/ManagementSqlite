package com.renbin.managementsqlite;

import com.renbin.managementsqlite.sql.annotion.DbField;
import com.renbin.managementsqlite.sql.annotion.DbTable;

@DbTable("tb_table")
public class Photo {
    @DbField("tb_time")
    public String time;
    @DbField("tb_path")
    public String path;
    @DbField("tb_descript")
    public String descript;


    public Photo(){

    }

    public Photo(String time, String path, String descript) {
        this.time = time;
        this.path = path;
        this.descript = descript;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "time='" + time + '\'' +
                ", path='" + path + '\'' +
                ", descript='" + descript + '\'' +
                '}';
    }
}
