package com.example.push;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.push.bean.MessageBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shihao on 2017/3/5.
 */

public class DBManager {

    private DBHelper helper;
    private SQLiteDatabase sqLiteDatabase;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        sqLiteDatabase = helper.getWritableDatabase();
    }

    public void insert(String tableName, MessageBody messageBody) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("to",messageBody.getToUser());
        contentValues.put("origin", messageBody.getOrigin());
        contentValues.put("title", messageBody.getTitle());
        contentValues.put("content", messageBody.getContent());
        contentValues.put("time", messageBody.getTime());
        contentValues.put("extra", messageBody.getExtra());
        long id = sqLiteDatabase.insert(tableName, null, contentValues);
        if (id == -1) {
            Log.e("xmpp", "插入数据失败");
        }
    }

    public List<MessageBody> query(String tableName, String[] args) {
        List<MessageBody> msgList = new ArrayList<>();
        helper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(tableName, null, null, args, null, null, "id desc");
        while (cursor.moveToNext()) {
            MessageBody body = new MessageBody();
            body.setToUser(cursor.getString(cursor.getColumnIndex("touser")));
            body.setOrigin(cursor.getString(cursor.getColumnIndex("origin")));
            body.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            body.setContent(cursor.getString(cursor.getColumnIndex("content")));
            body.setTime(cursor.getString(cursor.getColumnIndex("time")));
            body.setExtra(cursor.getString(cursor.getColumnIndex("extra")));
            msgList.add(body);
        }
        cursor.close();
        return msgList;
    }

    public void close() {
        if (sqLiteDatabase != null) {
            sqLiteDatabase.close();
        }
    }
}
