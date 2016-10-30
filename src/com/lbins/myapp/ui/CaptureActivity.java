package com.lbins.myapp.ui;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.camera.CameraManager;
import com.lbins.myapp.camera.decoding.CaptureActivityHandler;
import com.lbins.myapp.camera.decoding.InactivityTimer;
import com.lbins.myapp.camera.view.ViewfinderView;
import com.lbins.myapp.data.SuccessData;
import com.lbins.myapp.entity.PayScanObj;
import com.lbins.myapp.entity.ShoppingAddress;
import com.lbins.myapp.util.StringUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
//扫描
public class CaptureActivity extends BaseActivity implements Callback
{
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.capture_activity);
        // 初始化 CameraManager
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface)
        {
            initCamera(surfaceHolder);
        }
        else
        {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL)
        {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (handler != null)
        {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy()
    {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder)
    {
        try
        {
            CameraManager.get().openDriver(surfaceHolder);
        }
        catch (IOException ioe)
        {
            return;
        }
        catch (RuntimeException e)
        {
            return;
        }
        if (handler == null)
        {
            handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        if (!hasSurface)
        {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView()
    {
        return viewfinderView;
    }

    public Handler getHandler()
    {
        return handler;
    }

    public void drawViewfinder()
    {
        viewfinderView.drawViewfinder();

    }
    PayScanObj payScanObj = new PayScanObj();//扫一扫付款对象
    public void handleDecode(final Result obj, Bitmap barcode)
    {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        if(barcode == null){
            //
        }else{
            //扫描结果
            String url = obj.getText();
            if(url.contains(InternetURL.APP_SHARE_REG_URL)){
                //推广注册
                //用默认浏览器打开扫描得到的地址
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
                finish();
            }
            if (StringUtil.isNullOrEmpty(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ||
                    "0".equals(getGson().fromJson(getSp().getString("isLogin", ""), String.class)) ) {
                //未登录
            } else {
                if(url.contains(InternetURL.SAVE_FAVOUR_URL)){
                    //收藏店铺，加粉丝
                    url = url+"&emp_id_favour="+ getGson().fromJson(getSp().getString("empId", ""), String.class);
                    saveFavour(url);
                }
                if(url.contains(InternetURL.GET_GET_GOODS_URN)){
                    String emp_id = url.substring(url.indexOf("=")).replace("=","");
                    //有偿消费
                    payScanObj.setEmp_id(emp_id);
                    payScanObj.setTitle("扫码消费_有偿消费");
                    //弹框提示输入金额
                    Intent intent = new Intent(CaptureActivity.this, PaySelectTwoActivity.class);
                    intent.putExtra("payScanObj", payScanObj);
                    startActivity(intent);
                    finish();
                }
                if(url.contains(InternetURL.GET_GET_DXK_GOODS_URN)){
                    if("1".equals(getGson().fromJson(getSp().getString("is_card_emp", ""), String.class))){
                        //是定向卡会员
                        //无偿消费
                        String emp_id = url.substring(url.indexOf("=")).replace("=", "");
                        //插入一个订单-定向卡订单
                        saveDxkOrder(emp_id);
                    }else {
                        //不是定向卡会员
                       showMsg(CaptureActivity.this, "您不是定向卡会员，不能扫描！");
                    }
                }
            }

        }
    }


    private void showPayCount() {
        final Dialog picAddDialog = new Dialog(CaptureActivity.this, R.style.dialog);
        View picAddInflate = View.inflate(this, R.layout.msg_pay_dialog, null);
        TextView btn_sure = (TextView) picAddInflate.findViewById(R.id.btn_sure);
        final EditText cont = (EditText) picAddInflate.findViewById(R.id.cont);
        cont.setText("请输入支付金额？");
        btn_sure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(StringUtil.isNullOrEmpty(cont.getText().toString())){
                    showMsg(CaptureActivity.this, "请输入支付金额！");
                    return;
                }else {
                    payScanObj.setPay_count(cont.getText().toString());
                    Intent intent = new Intent(CaptureActivity.this, PaySelectTwoActivity.class);
                    intent.putExtra("payScanObj", payScanObj);
                    ShoppingAddress shoppingAddress=null;
                    startActivity(intent);
                    picAddDialog.dismiss();
                }
            }
        });

        //取消
        TextView btn_cancel = (TextView) picAddInflate.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picAddDialog.dismiss();
            }
        });
        picAddDialog.setContentView(picAddInflate);
        picAddDialog.show();
    }

    //收藏店铺
    void saveFavour(String url){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                showMsg(CaptureActivity.this, "收藏店铺成功！");
                                finish();
                            } else if(data.getCode() == 2){
                                showMsg(CaptureActivity.this, "已经收藏了！");
                                finish();
                            }
                            else {
                                Toast.makeText(CaptureActivity.this, "收藏店铺失败,请重新扫描！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(CaptureActivity.this, "收藏店铺失败,请重新扫描！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(CaptureActivity.this, "收藏店铺失败,请重新扫描！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }

    //保存订单 定向卡的
    void saveDxkOrder(final String emp_id){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.SAVE_DXK_ORDER_URN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            SuccessData data = getGson().fromJson(s, SuccessData.class);
                            if (data.getCode() == 200) {
                                showMsg(CaptureActivity.this, "订单已生成，等待管理员审核！");
                                finish();
                            }
                            else {
                                Toast.makeText(CaptureActivity.this, "订单生成失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Toast.makeText(CaptureActivity.this, "订单生成失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(CaptureActivity.this, "订单生成失败，请稍后重试！", Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", getGson().fromJson(getSp().getString("empId", ""), String.class));//这个是买家的
                params.put("seller_emp_id", emp_id);//这个是卖家的
                params.put("payable_amount","0");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        getRequestQueue().add(request);
    }


    private void initBeepSound()
    {
        if (playBeep && mediaPlayer == null)
        {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try
            {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            }
            catch (IOException e)
            {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate()
    {
        if (playBeep && mediaPlayer != null)
        {
            mediaPlayer.start();
        }
        if (vibrate)
        {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener()
    {
        public void onCompletion(MediaPlayer mediaPlayer)
        {
            mediaPlayer.seekTo(0);
        }
    };

}