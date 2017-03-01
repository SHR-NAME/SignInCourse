package com.example.administrator.signincouse.toolclass;

import android.content.Context;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Administrator on 2016/7/7.
 */
public class CourseAsynchttp {
    private Context context;
    public CourseAsynchttp(Context context){
      this.context=context;
    }
    public void asynchttpPost(){
        AsyncHttpClient client=new AsyncHttpClient();
        String myUrl="http://penderie.cn/penderie/test/getJson?url=getAllCourse";
        RequestParams params=new RequestParams("url","getAllCourse");
        client.get(myUrl, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                Toast.makeText(context,responseString,Toast.LENGTH_SHORT).show();
            }

        });
    }
    public void asyncHttpPostJson(){
        AsyncHttpClient client=new AsyncHttpClient();
        String myUrl="http://penderie.cn/penderie/test/getJson?url=getAllCourse";
        RequestParams params=new RequestParams("url","getAllCourse");
        client.get(myUrl,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(context,"成功",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context,"加载失败",Toast.LENGTH_SHORT).show();
            }
        });
    }
}