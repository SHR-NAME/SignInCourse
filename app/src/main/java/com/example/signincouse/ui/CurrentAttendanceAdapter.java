package com.example.signincouse.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.signincouse.R;
import com.example.signincouse.model.Course;

import java.util.List;

/**
 * Created by bangzhu on 2016/9/28.
 */
public class CurrentAttendanceAdapter extends BaseAdapter {
    private List<Course> mCourseList;
    private Context mContext;

    public CurrentAttendanceAdapter(Context context, List<Course> list) {
        mCourseList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mCourseList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCourseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.current_attendance_item, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tv_current_attendance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Course course = mCourseList.get(position);
        viewHolder.textView.setText(course.getCourseName());
        return convertView;
    }

    public final class ViewHolder {
        public TextView textView;
    }
}
