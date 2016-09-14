package com.lbins.myapp.base;

/**
 * Created by zhanghl on 2015/1/12.
 */
public class InternetURL {
    //mob
    public static final String APP_MOB_KEY = "171c439f201e8";
    public static final String APP_MOB_SCRECT = "dc2a8e43e9498e9454f4c13297c8bed2";

    public static final String INTERNAL = "http://smilekyle.xicp.net:42765/";
    //多媒体文件上传接口
    public static final String UPLOAD_FILE = INTERNAL + "uploadImage.do";

    public static final String LOGIN__URL = INTERNAL + "memberLogin.do";

    //注册第一步
    public static final String REG_ONE__URL = INTERNAL + "memberRegister.do";
    //根据手机号查询用户是否存在
    public static final String GET_EMP_MOBILE = INTERNAL + "getMemberByMobile.do";

    //更新密码
    public static final String UPDATE_PWR__URL = INTERNAL + "resetPass.do";
    //更新头像
    public static final String UPDATE_INFO_COVER_URL = INTERNAL + "modifyMember.do";

    //获得商品分类
    public static final String GET_GOODS_TYPE_URL = INTERNAL + "appGetGoodsType.do";
    //
    public static final String GET_GOODS_URL = INTERNAL + "paopaogoods/listGoods.do";
}
