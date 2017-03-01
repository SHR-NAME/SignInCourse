package com.shr.filehelper.service;

import com.shr.filehelper.bean.MainBean;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 文件列表
 */

public interface FileService {

    /**
     * 文件列表
     *
     * @return 列表数据
     */
    @GET("files")
    Call<MainBean> getFileListData();

    /**
     * 下载文件
     *
     * @param fileUrl 下载url
     * @return 返回信息
     */
    @Streaming
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlAsync(@Url String fileUrl);
}
