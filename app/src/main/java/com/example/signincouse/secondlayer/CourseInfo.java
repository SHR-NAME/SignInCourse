package com.example.signincouse.secondlayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.administrator.signincouse.R;

public class CourseInfo extends AppCompatActivity {
    private TextView name,teacher,location,weeks,credit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_info);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        name= (TextView) findViewById(R.id.course_name_instance);
        teacher= (TextView) findViewById(R.id.course_teacher_instance);
        location= (TextView) findViewById(R.id.course_location_instance);
        weeks= (TextView) findViewById(R.id.course_weeks_instance);
        credit= (TextView) findViewById(R.id.course_credit_instance);
        name.setText(bundle.getString("name"));
        teacher.setText(bundle.getString("teacher"));
        location.setText(bundle.getString("location"));
        weeks.setText(bundle.getString("weeks"));
        credit.setText(bundle.getString("credit"));
    }
}
