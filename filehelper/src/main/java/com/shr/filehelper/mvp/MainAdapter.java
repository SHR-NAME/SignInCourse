package com.shr.filehelper.mvp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.shr.filehelper.R;
import com.shr.filehelper.bean.MainBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据填充
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.FileViewHolder> {

    private List<MainBean> mainBeanList;
    private Context context;

    public MainAdapter(Context context) {
        this.context = context;
        mainBeanList = new ArrayList<>();
    }

    /**
     * 添加数据
     *
     * @param list 要添加的数据
     */
    public void addList(List<MainBean> list) {
        if (list != null) {
            mainBeanList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
        FileViewHolder holder = new FileViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(FileViewHolder holder, int position) {
        MainBean bean = mainBeanList.get(position);
        holder.name.setText(bean.getName());
        holder.time.setText(bean.getAtime());
        final String downLoadUrl = bean.getPath();
        final int type = getType(downLoadUrl);
        final String d_name = bean.getName();
        final int curPos = position;
        switch (type) {
            case DetailActivity.PIC_TYPE:
                holder.icon.setImageResource(R.mipmap.photo);
                break;
            case DetailActivity.DOC_TYPE:
                holder.icon.setImageResource(R.mipmap.word);
                break;
            case DetailActivity.EXCEL_TYPE:
                holder.icon.setImageResource(R.mipmap.excel);
                break;
            case DetailActivity.PPT_TYPE:
                holder.icon.setImageResource(R.mipmap.power_point);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(curPos, downLoadUrl, type, d_name);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mainBeanList.size();
    }

    class FileViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView name, time;

        public FileViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            name = (TextView) itemView.findViewById(R.id.name);
            time = (TextView) itemView.findViewById(R.id.time);
        }
    }

    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    interface OnItemClickListener {
        void onItemClick(int position, String downLoadUrl, int type, String name);
    }

    private int getType(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.endsWith(".png") || url.endsWith(".jpg")) {
                return DetailActivity.PIC_TYPE;
            } else if (url.endsWith(".doc")) {
                return DetailActivity.DOC_TYPE;
            } else if (url.endsWith(".exl")) {
                return DetailActivity.EXCEL_TYPE;
            } else if (url.endsWith(".ppt")) {
                return DetailActivity.PPT_TYPE;
            } else if (url.endsWith(".pdf")) {
                return DetailActivity.PDF_TYPE;
            } else if (url.endsWith(".docx")) {
                return DetailActivity.DOCX_TYPE;
            }else if (url.endsWith(".txt")) {
                return DetailActivity.TXT_TYPE;
            }
        }

        return -1;
    }
}
