package com.example.signincouse.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.signincouse.R;
import com.example.push.bean.MessageBody;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shihao on 2017/3/5.
 */

public class MsgAdapter extends RecyclerView.Adapter {

    private List<MessageBody> msgList = new ArrayList<>();

    public void addData(List<MessageBody> msgs) {
        if (msgs != null) {
            msgList.addAll(msgs);
            notifyDataSetChanged();
        }
    }

    public void delData(MessageBody body) {
        msgList.remove(body);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_msg, parent, false);
        return new MsgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MsgViewHolder viewHolder = (MsgViewHolder) holder;
        MessageBody messageBody = msgList.get(position);
        viewHolder.titleTv.setText(messageBody.getTitle()+"    来自" + messageBody.getOrigin());
        viewHolder.contentTv.setText(messageBody.getContent());
        viewHolder.timeTv.setText(messageBody.getTime());
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    private class MsgViewHolder extends RecyclerView.ViewHolder {

        TextView titleTv, contentTv,timeTv;

        public MsgViewHolder(View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.title);
            contentTv = (TextView) itemView.findViewById(R.id.content);
            timeTv = (TextView) itemView.findViewById(R.id.time);
        }
    }
}
