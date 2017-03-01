package com.shr.filehelper.mvp;

import android.app.Activity;

import com.shr.filehelper.bean.MainBean;

/**
 * 业务处理
 */

public class MainPresenter implements IPresenter,MainModel.CallBack<MainBean>{

    private Activity mActivity;
    private MainView mMainView;
    private MainModel mMainModel;

    public MainPresenter(Activity activity, MainView mainView) {
        this.mActivity = activity;
        this.mMainView = mainView;

        mainView.setPresenter(this);

        mMainModel = new MainModel();
    }

    @Override
    public void initData() {
        mMainView.showProgress();
        mMainModel.executeFileListTask(this);
    }

    @Override
    public void onSuccess(MainBean bean) {
        mMainView.hideProgress();
        if (bean != null) {
            mMainView.showFileList(bean.data);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        mMainView.hideProgress();
    }
}
