package com.shr.filehelper.mvp;

/**
 * Created by shi hao on 2017/2/8.
 */

public interface IView {

    void setPresenter(IPresenter presenter);

    void showProgress();

    void hideProgress();
}
