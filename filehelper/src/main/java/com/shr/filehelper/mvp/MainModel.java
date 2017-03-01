package com.shr.filehelper.mvp;

import com.shr.filehelper.bean.MainBean;
import com.shr.filehelper.constant.Constants;
import com.shr.filehelper.service.FileService;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 数据处理
 */

public class MainModel {

    Retrofit retrofit;
    private final FileService fileListService;

    public MainModel() {
        retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        fileListService = retrofit.create(FileService.class);
    }

    /**
     * 获取文件列表
     *
     * @param callBack 回调
     */
    public void executeFileListTask(final CallBack<MainBean> callBack) {


        Call<MainBean> call = fileListService.getFileListData();
        call.enqueue(new Callback<MainBean>() {
            @Override
            public void onResponse(Call<MainBean> call, Response<MainBean> response) {
                callBack.onSuccess(response.body());

            }

            @Override
            public void onFailure(Call<MainBean> call, Throwable t) {
                callBack.onFailure(t);
            }
        });
    }

    public void executeDownLoadTask(String url, final CallBack<ResponseBody> callBack) {
        Call<ResponseBody> call = fileListService.downloadFileWithDynamicUrlAsync(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() != null) {
                    callBack.onSuccess(response.body());
                } else {
                    callBack.onFailure(new NullPointerException());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                callBack.onFailure(t);
            }
        });

    }

    interface CallBack<Object> {
        void onSuccess(Object t);

        void onFailure(Throwable throwable);
    }

}
