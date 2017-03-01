package com.shr.filehelper.mvp;

import com.shr.filehelper.bean.MainBean;

import java.util.List;

/**
 *
 */

public interface MainView extends IView {

    /**
     * 展示列表
     */
    void showFileList(List<MainBean> list);
}
