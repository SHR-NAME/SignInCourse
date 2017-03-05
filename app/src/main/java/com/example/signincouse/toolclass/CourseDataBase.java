package com.example.signincouse.toolclass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/7/7.
 */
public class CourseDataBase extends SQLiteOpenHelper {
    public CourseDataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "courseDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       db.execSQL("CREATE TABLE courseTable(" +
                  "course_id INTEGER DEFAULT 0," +
                  "course_name TEXT DEFAULT \"\"," +
                  "course_teacher TEXT DEFAULT \"\"," +
                  "course_location TEXT DEFAULT \"\"," +
                  "course_weekday INTEGER DEFAULT 0," +
                  "begin_time TEXT DEFAULT \"\"," +
                  "end_time TEXT DEFAULT \"\"," +
                  "start_week INTEGER DEFAULT 1," +
                  "end_week INTEGER DEFAULT 20)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
