package com.example.administrator.signincouse.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.signincouse.R;
import com.example.administrator.signincouse.toolclass.AsyncHttpUtil;
import com.example.administrator.signincouse.toolclass.Contants;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shr.push.PushService;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class Login extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
    private EditText userEdit, passwordEdit;
    private Button loginBtn;
    private Boolean isLogin;
    private SharedPreferences preferences;
    private TextView changeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        userEdit = (EditText) findViewById(R.id.user_TextView);
        passwordEdit = (EditText) findViewById(R.id.password_TextView);
        loginBtn = (Button) findViewById(R.id.login_btn);
        changeTextView = (TextView) findViewById(R.id.change_password);
        userEdit.setHint(preferences.getString("user", "请输入用户名"));
        passwordEdit.setHint(preferences.getString("password", "请输入密码"));
        passwordEdit.setOnFocusChangeListener(this);
        userEdit.setOnFocusChangeListener(this);
        loginBtn.setOnClickListener(this);
        changeTextView.setOnClickListener(this);
    }

    private void init() {
        preferences = getSharedPreferences("login", MODE_PRIVATE);
        isLogin = preferences.getBoolean("isLogin", false);
        if (isLogin) {
            startActivity(new Intent(Login.this, MainActivity.class));
            finish();
        } else {

        }
    }

    public void asyncHttpPostUserLogin() {
        RequestParams params = new RequestParams();
        params.put("username", userEdit.getText().toString());
        params.put("password", passwordEdit.getText().toString());
        AsyncHttpUtil.post(Contants.POST_USER_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int code = response.getInt("code");
                    if (code != 0) {
                        //账号密码有误时
                        Toast.makeText(Login.this, "请正确输入用户名和密码", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        //登录成功
                        JSONObject userInformation = response.getJSONObject("msg");
                        int userID = userInformation.getInt("userID");
                        String permission = userInformation.getString("permission");
                        //String name = userInformation.getString("name");
                        preferences.edit().putBoolean("isLogin", true).commit();
                        preferences.edit().putInt("userID", userID).commit();
                        preferences.edit().putString("permission", permission).commit();
                        preferences.edit().putString("user", userEdit.getText().toString()).commit();
                        preferences.edit().putString("password", passwordEdit.getText().toString()).commit();
                        startActivity(new Intent(Login.this, MainActivity.class));
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(Login.this, "加载失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_btn:
                asyncHttpPostUserLogin();
                break;
            case R.id.change_password:
                Toast.makeText(Login.this, "改密码", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.user_TextView:
                if (!hasFocus) {
                    ((EditText) v).setHint("请输入用户名");
                } else {
                    ((EditText) v).setHint("");
                }
                break;
            case R.id.password_TextView:
                if (!hasFocus) {
                    ((EditText) v).setHint("请输入密码");
                } else {
                    ((EditText) v).setHint("");
                }
                break;
        }
    }
}
