package com.example.signincouse.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.signincouse.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/6/7.
 */
public class ListtodayCourseAdapter extends BaseAdapter {
    private Context context;
    private TextView textViewTime,textViewName,textViewAdd;
    private ArrayList<HashMap<String,String>>listItem;
    public ListtodayCourseAdapter(Context context,ArrayList<HashMap<String,String>> listItem){
        this.context=context;
        this.listItem=listItem;
    }
    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.listtodaycourse_item,null);
            textViewTime= (TextView) convertView.findViewById(R.id.courseTime);
            textViewName= (TextView) convertView.findViewById(R.id.courseNane);
            textViewAdd= (TextView) convertView.findViewById(R.id.courseAddress);
            textViewName.setText(listItem.get(position).get("textName"));
            textViewTime.setText(listItem.get(position).get("textTime"));
            textViewAdd.setText(listItem.get(position).get("textAdd"));
        }
        return convertView;
    }
}
