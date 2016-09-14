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
    //更改性别
    public static final String UPDATE_INFO_SEX_URL = INTERNAL + "modifyMemberSex.do";
    //更新生日
    public static final String UPDATE_INFO_BIRTH_URL = INTERNAL + "modifyMemberBirth.do";
    // 跟新手机号
    public static final String UPDATE_INFO_MOBILE_URL = INTERNAL + "resetMobile.do";
    //更新支付密码
    public static final String UPDATE_INFO_PAY_PASS_URL = INTERNAL + "modifyMemberPayPass.do";

    //我的收货地址列表
    public static final String MINE_ADDRSS =   INTERNAL +"listShoppingAddress.do";
    //添加收货地址
    public static final String ADD_MINE_ADDRSS =  INTERNAL + "saveShoppingAddress.do";
    //更新收货地址
    public static final String UPDATE_MINE_ADDRSS =   INTERNAL +"updateShoppingAddress.do";
    //删除收货地址
    public static final String DELETE_MINE_ADDRSS =   INTERNAL +"deleteShoppingAddress.do";
    //获得默认收货地址
    public static final String GET_MOREN_ADDRESS =   INTERNAL +"getSingleAddressByEmpId.do";
    //添加收货地址--选择省份--城市--地区
    public static final String SELECT_PROVINCE_ADDRESS =   INTERNAL +"appGetProvince.do";
    public static final String SELECT_CITY_ADDRESS =   INTERNAL +"appGetCity.do";
    public static final String SELECT_AREA_ADDRESS =   INTERNAL +"appGetArea.do";
    //上传经纬度
    public static final String SEND_LOCATION_BYID_URL = INTERNAL + "sendLocation.do";


    //获得商品分类
    public static final String GET_GOODS_TYPE_URL = INTERNAL + "appGetGoodsType.do";
    //
    public static final String GET_GOODS_URL = INTERNAL + "paopaogoods/listGoods.do";
}
