package com.example.push;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库操作类
 * <p>
 * Created by shi hao on 2017/3/5.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "people.db";
    private static final int DB_VERSION = 1;
    public static final String TABLE_STUDENT = "student";
    public static final String TABLE_TEACHER = "teacher";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists "+TABLE_STUDENT+" (id integer primary key autoincrement,"
        +"name varchar(20),title varchar(20),content varchar,time varchar,extra varchar)");
        db.execSQL("create table if not exists "+TABLE_TEACHER+" (id integer primary key autoincrement,"
                +"name varchar(20),title varchar(20),content varchar,time varchar,extra varchar)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_TEACHER);
        db.execSQL("drop table if exists "+TABLE_STUDENT);
        onCreate(db);
    }
}
