package com.lbins.myapp.base;

/**
 * Created by zhanghl on 2015/1/12.
 */
public class InternetURL {
    //mob
    public static final String APP_MOB_KEY = "171c439f201e8";
    public static final String APP_MOB_SCRECT = "dc2a8e43e9498e9454f4c13297c8bed2";

    public static final String WEIXIN_APPID = "wx9769250919c81901";
    public static final String WEIXIN_SECRET = "559afbe3ff53400c469f12648a976eff";
    public static final  String WX_API_KEY="559afbe3ff53400c469f12648a976eff";

    public static final String INTERNAL = "http://157j1274e3.iask.in/";
//    public static final String INTERNAL = "http://192.168.0.224:8080/";
    //多媒体文件上传接口
    public static final String UPLOAD_FILE = INTERNAL + "uploadImage.do";

    //登录
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
    //申请入驻
    public static final String EMP_APPLY_DIANPU_URL = INTERNAL + "saveManagerInfo.do";

    //获得商品分类
    public static final String GET_GOODS_TYPE_URL = INTERNAL + "appGetGoodsType.do";

    //获得附近首页商店列表
    public static final String GET_DIANPU_LISTS = INTERNAL + "appGetNearbyDianpu.do";
    //获得首页推荐商品
    public static final String GET_INDEX_TUIJIAN_LISTS = INTERNAL + "getIndexTuijian.do";
    //查询店铺详情
    public static final String GET_DIPU_DETAIL_LISTS = INTERNAL + "appGetDianpuDetailByEmpId.do";
    //查询店铺广告轮播图
    public static final String GET_DIPU_ADS_LISTS = INTERNAL + "appGetAdEmp.do";
    //查询商品详情
    public static final String GET_GODS_DETAIL_LISTS = INTERNAL + "paopaogoods/findById.do";
    //查询商品评论
    public static final String GET_GOODS_COMMENT_LISTS = INTERNAL + "listGoodsComment.do";
    //广告轮播
    public static final String GET_AD_LIST_TYPE_LISTS = INTERNAL + "appGetAdByType.do";
    //获取商品列表
    public static final String GET_GOODS_URL = INTERNAL + "paopaogoods/listGoods.do";


    //传订单给服务端--生成订单
    public static final String SEND_ORDER_TOSERVER = INTERNAL + "orderSave.do";
    //更新订单状态
    public static final String UPDATE_ORDER_TOSERVER =  INTERNAL +"orderUpdate.do";
    //查询订单列表
    public static final String MINE_ORDERS_URL = INTERNAL + "listOrders.do";
    //更新订单
    public static final String UPDATE_ORDER = INTERNAL + "updateOrder.do";
    //去付款--单个订单付款-支付宝
    public static final String SAVE_ORDER_SIGNLE =INTERNAL +  "orderSaveSingle.do";

    //更新订单状态
    public static final String UPDATE_ORDER_TOSERVER_SINGLE = INTERNAL + "orderUpdateSingle.do";
    //根据地址id，查询收货地址、
    public static final String GET_ADDRESS_BYID =  INTERNAL + "getSingleAddressByAddressId.do";


    //收藏商品接口
    public static final String SAVE_FAVOUR = INTERNAL +  "saveGoodsFavour.do";
    //收藏商品列表
    public static final String MINE_FAVOUR = INTERNAL +  "listFavour.do";
    //删除收藏的商品
    public static final String DELETE_FAVOUR = INTERNAL +  "deleteFavour.do";

    //删除店铺收藏
    public static final String DELETE_DIANPU_FAVOUR_URL = INTERNAL +   "deleteDianpuFavour.do";
    //收藏店铺
    public static final String SAVE_FAVOUR_URL = INTERNAL +   "saveDianpuFavour.do";
    //获得我的店铺收藏列表
    public static final String APP_GET_FAVOUR_DIANPU_URL = INTERNAL +   "appGetDianpuFavour.do";

    //查询用户银行卡
    public static final String APP_GET_BANK_CARDS_URL = INTERNAL +   "appGetBankCards.do";
    //保存银行卡信息
    public static final String APP_SAVE_BANK_CARDS_URL = INTERNAL +   "appSaveBankCards.do";
    //删除银行卡 根据银行卡id
    public static final String APP_DELETE_BANK_CARDS_URL = INTERNAL +   "deleteBankCard.do";
    //查询会员钱包信息
    public static final String APP_GET_PACKAGE_URL = INTERNAL +   "appGetPackage.do";
    //版本检查
    public static final String CHECK_VERSION_CODE_URL = INTERNAL +   "getVersionCode.do";


    //百度推送 绑定 get方法
    public static final String UPDATE_PUSH_ID = INTERNAL + "updatePushId.do";

    //查询我的浏览记录
    public static final String GET_BROWSING_ID = INTERNAL + "appGetBrowsing.do";
    //分享商品链接
    public static final String SHARE_GOODS_DETAIL_URL = INTERNAL + "paopaogoods/shareGoodsUrl.do";
    //微信支付
    public static final String SEND_ORDER_TOSERVER_WX = INTERNAL + "orderSaveWx";

    //添加商品评论
    public static final String PUBLISH_GOODS_COMMNENT_URL = INTERNAL+ "saveGoodsComment.do";
    //获得客户电话列表
    public static final String GET_KEFU_TEL_URL = INTERNAL+ "getKefuTel.do";

    //推广注册
    public static final String APP_SHARE_REG_URL = INTERNAL+ "appShareReg.do";

    //去付款--单个订单付款-微信
    public static final String SAVE_ORDER_SIGNLE_WX =INTERNAL +  "orderSaveSingleWx.do";

    //确认收货 生成二维码用，卖家扫一扫确认发货 这个是面对面的，所以直接订单完成
    public static final String APP_SURE_FAHUO_URL = INTERNAL+ "scanOrder.do";

    //查询我的點評
    public static final String GET_MINE_GOODS_COMMENT_LISTS = INTERNAL + "listGoodsComment.do";

    //更新订单--退货
    public static final String UPDATE_ORDER_RETURN = INTERNAL + "updateOrder.do";

    //获得会员的消费记录 ，钱包-》我的金币，点击查看消费详情
    public static final String GET_CONSUMPTION_RETURN = INTERNAL + "appGetConsumption.do";
    //我的积分记录
    public static final String GET_COUNT_RECORD_RETURN = INTERNAL + "appGetCountRecord.do";
    //查询我的积分
    public static final String GET_COUNT_RETURN = INTERNAL + "appGetCount.do";

    //提交提现申请
    public static final String SAVE_BANK_APPLY_RETURN = INTERNAL + "appSaveBankApply.do";
    //猜你喜欢的
    public static final String GET_LIKES_URN = INTERNAL + "appGetLikes.do";
}
