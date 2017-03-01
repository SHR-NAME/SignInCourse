package com.shr.filehelper.bean;

import java.util.List;

/**
 *
 * Created by shi hao on 2017/2/9.
 */

public class BaseResponse<T> {
//    "status": 1,
//    "msg": "文件上传成功",

    public int status;
    public String msg;
    public List<T> data;

}
