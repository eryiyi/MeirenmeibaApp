package com.lbins.myapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.camera.createqr.CreateQRImageTest;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.lang.ref.WeakReference;

/**
 * Created by zhl on 2016/8/30.
 * 我的推广二维码 别人通过我的二维码注册  我就是他们的上级
 */
public class TuiguangActivity extends BaseActivity implements View.OnClickListener {
    private TextView title;
    private TextView right_btn;
    private String urlErweima= "";

    private ImageView share_pic;//二维码
    Bitmap bitmap ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuiguang_activity);
        mShareListener = new CustomShareListener(this);
        urlErweima = InternetURL.APP_SHARE_REG_URL+"?emp_id=" +getGson().fromJson(getSp().getString("empId", ""), String.class);
        bitmap = CreateQRImageTest.createQRImage(urlErweima);
        initView();
    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        right_btn = (TextView) this.findViewById(R.id.right_btn);
        right_btn.setVisibility(View.VISIBLE);
        right_btn.setText("分享");
        right_btn.setOnClickListener(this);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("我的推广");
        share_pic = (ImageView) this.findViewById(R.id.share_pic);
        if(bitmap != null){
            share_pic.setImageBitmap(bitmap);
        }
    }

    private UMShareListener mShareListener;
    private ShareAction mShareAction;


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.right_btn:
            {
                //分享
                String title =  getGson().fromJson(getSp().getString("empName", ""), String.class)+"邀请您注册美人美吧";
                String content = " 美人美吧，源于一个美好的愿望，我们要将美送到千家万户，我们希望所有的女性都美丽，我们能让所有女性都变美丽，我们能让所有女性都美丽，美人！美吧！";
                UMImage image = new UMImage(TuiguangActivity.this, getGson().fromJson(getSp().getString("empCover", ""), String.class));
                     /*无自定按钮的分享面板*/
                mShareAction = new ShareAction(TuiguangActivity.this).setDisplayList(
                        SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                        SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
                        SHARE_MEDIA.ALIPAY,
                        SHARE_MEDIA.SMS, SHARE_MEDIA.EMAIL,
                        SHARE_MEDIA.MORE)
                        .withText(content)
                        .withTitle(title)
                        .withTargetUrl(urlErweima)
                        .withMedia(image)
                        .setCallback(mShareListener);

                ShareBoardConfig config = new ShareBoardConfig();
                config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);
                config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR); // 圆角背景
                mShareAction.open(config);
            }
                break;
        }
    }


    private static class CustomShareListener implements UMShareListener {

        private WeakReference<DetailPaopaoGoodsActivity> mActivity;

        private CustomShareListener(TuiguangActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            if (platform.name().equals("WEIXIN_FAVORITE")) {
                Toast.makeText(mActivity.get(), platform + " 收藏成功啦", Toast.LENGTH_SHORT).show();
            } else {
                if (platform!= SHARE_MEDIA.MORE&&platform!=SHARE_MEDIA.SMS
                        &&platform!=SHARE_MEDIA.EMAIL
                        &&platform!=SHARE_MEDIA.FLICKR
                        &&platform!=SHARE_MEDIA.FOURSQUARE
                        &&platform!=SHARE_MEDIA.TUMBLR
                        &&platform!=SHARE_MEDIA.POCKET
                        &&platform!=SHARE_MEDIA.PINTEREST
                        &&platform!=SHARE_MEDIA.LINKEDIN
                        &&platform!=SHARE_MEDIA.INSTAGRAM
                        &&platform!=SHARE_MEDIA.GOOGLEPLUS
                        &&platform!=SHARE_MEDIA.YNOTE
                        &&platform!=SHARE_MEDIA.EVERNOTE){
                    Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
                }

            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if (platform!= SHARE_MEDIA.MORE&&platform!=SHARE_MEDIA.SMS
                    &&platform!=SHARE_MEDIA.EMAIL
                    &&platform!=SHARE_MEDIA.FLICKR
                    &&platform!=SHARE_MEDIA.FOURSQUARE
                    &&platform!=SHARE_MEDIA.TUMBLR
                    &&platform!=SHARE_MEDIA.POCKET
                    &&platform!=SHARE_MEDIA.PINTEREST
                    &&platform!=SHARE_MEDIA.LINKEDIN
                    &&platform!=SHARE_MEDIA.INSTAGRAM
                    &&platform!=SHARE_MEDIA.GOOGLEPLUS
                    &&platform!=SHARE_MEDIA.YNOTE
                    &&platform!=SHARE_MEDIA.EVERNOTE){
                Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
                if (t != null) {
                    Log.d("throw", "throw:" + t.getMessage());
                }
            }

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

//    void share() {
//        new ShareAction(TuiguangActivity.this).setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                .setShareboardclickCallback(shareBoardlistener)
//                .open();
//    }
//
//    private ShareBoardlistener shareBoardlistener = new ShareBoardlistener() {
//
//        @Override
//        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
//            UMImage image = new UMImage(TuiguangActivity.this, getGson().fromJson(getSp().getString("empCover", ""), String.class));
//            String title =  getGson().fromJson(getSp().getString("empName", ""), String.class)+"邀请您注册美人美吧";
//            String content = " 美人美吧，源于一个美好的愿望，我们要将美送到千家万户，我们希望所有的女性都美丽，我们能让所有女性都变美丽，我们能让所有女性都美丽，美人！美吧！";
//            new ShareAction(TuiguangActivity.this).setPlatform(share_media).setCallback(umShareListener)
//                    .withText(content)
//                    .withTitle(title)
//                    .withTargetUrl(urlErweima)
//                    .withMedia(image)
//                    .share();
//        }
//    };


}
