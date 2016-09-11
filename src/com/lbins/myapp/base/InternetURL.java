package com.lbins.myapp.base;

/**
 * Created by zhanghl on 2015/1/12.
 */
public class InternetURL {
    public static final String INTERNAL = "http://smilekyle.xicp.net:42765/";
    //多媒体文件上传接口
    public static final String UPLOAD_FILE = INTERNAL + "uploadFileController.do?uploadImage";

    public static final String LOGIN__URL = INTERNAL + "memberLogin.do";

    //获得商品分类
    public static final String GET_GOODS_TYPE_URL = INTERNAL + "appGetGoodsType.do";


}
