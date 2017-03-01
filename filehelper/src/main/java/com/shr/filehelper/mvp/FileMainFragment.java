package com.shr.filehelper.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.shr.filehelper.R;
import com.shr.filehelper.bean.MainBean;

import java.util.List;

/**
 * 主页面
 */

public class FileMainFragment extends Fragment implements MainView, MainAdapter.OnItemClickListener {


    private IPresenter mPresenter;
    private MainAdapter adapter;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.file_fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.file_recycler_view);
        progressBar = (ProgressBar) view.findViewById(R.id.file_progressBar);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MainAdapter(getActivity());

        recyclerView.setAdapter(adapter);

        mPresenter.initData();

        adapter.setOnItemClickListener(this);
    }

    @Override
    public void setPresenter(IPresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showFileList(List<MainBean> list) {
        adapter.addList(list);
    }

    @Override
    public void onItemClick(int position, String downLoadUrl, int type, String name) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("url", downLoadUrl);
        intent.putExtra("type", type);
        intent.putExtra("name", name);
        startActivity(intent);
    }

}
