package com.example.administrator.signincouse.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.signincouse.R;
import com.example.administrator.signincouse.model.Course;

import java.util.List;

/**
 * Created by bangzhu on 2016/8/29.
 */
public class NoticeListAdapter extends BaseAdapter{
    private Context mContext;
    private List<Course> mCourseList;

    public NoticeListAdapter(Context context,  List<Course> list){
        mContext = context;
        mCourseList = list;
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
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.notice_list_item, parent, false);
            viewHolder.sender = (TextView) convertView.findViewById(R.id.tv_sender);
            viewHolder.noticeTitle = (TextView) convertView.findViewById(R.id.tv_notice_title);
            viewHolder.noticeBody = (TextView) convertView.findViewById(R.id.tv_notice_body);
            viewHolder.sendTime = (TextView) convertView.findViewById(R.id.tv_send_time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Course course = mCourseList.get(position);
        viewHolder.sender.setText(course.getCourseTeachter());
        viewHolder.noticeTitle.setText(course.getCourseName());
        viewHolder.noticeBody.setText(course.getCourseLocation());
        viewHolder.sendTime.setText(course.getBeginTime());
        return convertView;
    }

    final class ViewHolder{
        public TextView sender;
        public TextView noticeTitle;
        public TextView noticeBody;
        public TextView sendTime;
    }
}
