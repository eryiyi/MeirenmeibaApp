package com.lbins.myapp.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.*;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseActivity;
import com.lbins.myapp.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhl on 2016/8/30.
 * 商家地图位置
 */
public class DianpuLocationMapActivity extends BaseActivity implements View.OnClickListener,AMap.OnMarkerClickListener,AMap.OnInfoWindowClickListener, AMap.OnMarkerDragListener, AMap.OnMapLoadedListener,
        AMap.InfoWindowAdapter {
    private TextView title;
    private String lat ;
    private String lng ;
    private String name ;
    private String address ;

    private MapView mapView;
    private AMap aMap;

    private MarkerOptions markerOption;
    private LatLng latlng = new LatLng(36.061, 103.834);

    private TextView nameText;
    private TextView addressText;
    private TextView btn_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dianpu_location_map_activity);

        lat = getIntent().getExtras().getString("lat");
        lng = getIntent().getExtras().getString("lng");
        name = getIntent().getExtras().getString("name");
        address = getIntent().getExtras().getString("address");

        initView();
        initView(savedInstanceState);

        if(!StringUtil.isNullOrEmpty(name)){
            nameText.setText(name);
        }
        if(!StringUtil.isNullOrEmpty(address)){
            addressText.setText(address);
        }

    }

    private void initView() {
        this.findViewById(R.id.back).setOnClickListener(this);
        this.findViewById(R.id.right_btn).setVisibility(View.GONE);
        title = (TextView) this.findViewById(R.id.title);
        title.setText("商家位置");
        nameText = (TextView) this.findViewById(R.id.name);
        addressText = (TextView) this.findViewById(R.id.address);
        btn_map = (TextView) this.findViewById(R.id.btn_map);
        btn_map.setOnClickListener(this);
    }

    private void initView( Bundle savedInstanceState) {
        if(!StringUtil.isNullOrEmpty(lat) && !StringUtil.isNullOrEmpty(lng)){
            latlng = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        }
        mapView = (MapView) this.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        setUpMap();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void setUpMap() {
        aMap.setOnMarkerDragListener(this);// 设置marker可拖拽事件监听器
        aMap.setOnMapLoadedListener(this);// 设置amap加载成功事件监听器
        aMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
        aMap.setOnInfoWindowClickListener(this);// 设置点击infoWindow事件监听器
        aMap.setInfoWindowAdapter(this);// 设置自定义InfoWindow样式
        addMarkersToMap();// 往地图上添加marker
    }

    /**
     * 在地图上添加marker
     */
    public void addMarkersToMap() {
        // 文字显示标注，可以设置显示内容，位置，字体大小颜色，背景色旋转角度
        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
//        TextView textView= (TextView) view.findViewById(R.id.location_map);

                markerOption = new MarkerOptions();
                if(!StringUtil.isNullOrEmpty(lat) && !StringUtil.isNullOrEmpty(lng)){
                    //用户有会员定位数据
                    final LatLng latLngTmp = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                    markerOption.position(latLngTmp);

                    markerOption.title(name).snippet(name+address);
                    markerOption.draggable(true);

//                    final ImageView imageView = (ImageView) view.findViewById(R.id.location_map);
//                    imageLoader.displayImage(emp.getMm_emp_cover(), imageView, GuirenApplication.txOptions, new AnimateFirstDisplayListener(){
//                        @Override
//                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                            super.onLoadingComplete(imageUri, view, loadedImage);
//
//                            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
//                            markerOption.icon(
//                            BitmapDescriptorFactory.fromBitmap(bitmap));
//
//                        }
//                    });
                    // 将Marker设置为贴地显示，可以双指下拉看效果
//                    textView.setText(emp.getMm_emp_nickname());
//                    textView.setTextSize(8.0f);
//                    textView.setPadding(3,3,3,6);
//                    textView.setTextColor(getResources().getColor(R.color.white));
//                    textView.setDrawingCacheEnabled(false);
//                    textView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//                    textView.layout(0, 0, textView.getMeasuredWidth(), textView.getMeasuredHeight()*2);
//                    Bitmap bitmap = textView.getDrawingCache();
//                    markerOption.icon(
//                            BitmapDescriptorFactory.fromBitmap(bitmap));

                    markerOption.setFlat(true);
                    markerOption.icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
                    markerOptionlst.add(markerOption);
//                    aMap.addMarker(markerOption);
                }
            List<Marker> markerlst = aMap.addMarkers(markerOptionlst, true);
    }

    /**
     * 对marker标注点点击响应事件
     */
    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (aMap != null) {
            jumpPoint(marker);
        }
        return false;
    }

    /**
     * 从地上生长效果，实现思路
     * 在较短的时间内，修改marker的图标大小，从而实现动画<br>
     * 1.保存原始的图片；
     * 2.在原始图片上缩放得到新的图片，并设置给marker；
     * 3.回收上一张缩放后的图片资源；
     * 4.重复2，3步骤到时间结束；
     * 5.回收上一张缩放后的图片资源，设置marker的图标为最原始的图片；
     *
     * 其中时间变化由AccelerateInterpolator控制
     * @param marker
     */
    private void growInto(final Marker marker) {
        marker.setVisible(false);
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final long duration = 250;// 动画总时长
        final Bitmap bitMap = marker.getIcons().get(0).getBitmap();// BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher);
        final int width = bitMap.getWidth();
        final int height = bitMap.getHeight();

        final Interpolator interpolator = new AccelerateInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                if (t > 1) {
                    t = 1;
                }

                // 计算缩放比例
                int scaleWidth = (int) (t * width);
                int scaleHeight = (int) (t * height);
                if (scaleWidth > 0 && scaleHeight > 0) {

                    // 使用最原始的图片进行大小计算
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(Bitmap
                            .createScaledBitmap(bitMap, scaleWidth,
                                    scaleHeight, true)));
                    marker.setVisible(true);

                    // 因为替换了新的图片，所以把旧的图片销毁掉，注意在设置新的图片之后再销毁
                    if (lastMarkerBitMap != null
                            && !lastMarkerBitMap.isRecycled()) {
                        lastMarkerBitMap.recycle();
                    }

                    //第一次得到的缩放图片，在第二次回收，最后一次的缩放图片，在动画结束时回收
                    ArrayList<BitmapDescriptor> list = marker.getIcons();
                    if (list != null && list.size() > 0) {
                        // 保存旧的图片
                        lastMarkerBitMap = marker.getIcons().get(0).getBitmap();
                    }

                }

                if (t < 1.0 && count < 10) {
                    handler.postDelayed(this, 16);
                } else {
                    // 动画结束回收缩放图片，并还原最原始的图片
                    if (lastMarkerBitMap != null
                            && !lastMarkerBitMap.isRecycled()) {
                        lastMarkerBitMap.recycle();
                    }
                    marker.setIcon(BitmapDescriptorFactory.fromBitmap(bitMap));
                    marker.setVisible(true);
                }
            }
        });
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        //todo
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * marker.getPosition().longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * marker.getPosition().latitude + (1 - t)
                        * startLatLng.latitude;
//                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private int count = 1;
    Bitmap lastMarkerBitMap = null;


    /**
     * 监听点击infowindow窗口事件回调
     */
    @Override
    public void onInfoWindowClick(Marker marker) {
//        Toast.makeText(getActivity(), "你点击了infoWindow窗口"+marker.getTitle() ,Toast.LENGTH_SHORT).show();
//        ManagerInfo empT = null;
//        for(ManagerInfo emp:listsDianpu){
//            if(emp.getCompany_name().equals(marker.getTitle())){
//                LatLng latLng = marker.getPosition();
//                if(String.valueOf(latLng.latitude).equals(emp.getLat_company())){
//                    empT = emp;
//                    break;
//                }
//            }
//        }
//        if(empT != null){
//            Intent detail = new Intent(getActivity(), DianpuDetailActivity.class);
//            detail.putExtra("emp_id_dianpu", empT.getEmp_id());
//            startActivity(detail);
//        }

    }

    /**
     * 监听拖动marker时事件回调
     */
    @Override
    public void onMarkerDrag(Marker marker) {
        String curDes = marker.getTitle() + "拖动时当前位置:(lat,lng)\n("
                + marker.getPosition().latitude + ","
                + marker.getPosition().longitude + ")";
//        markerText.setText(curDes);
    }

    /**
     * 监听拖动marker结束事件回调
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
//        markerText.setText(marker.getTitle() + "停止拖动");
    }

    /**
     * 监听开始拖动marker事件回调
     */
    @Override
    public void onMarkerDragStart(Marker marker) {
//        markerText.setText(marker.getTitle() + "开始拖动");
    }

    /**
     * 监听amap地图加载成功事件回调
     */
    @Override
    public void onMapLoaded() {
        // 设置所有maker显示在当前可视区域地图中
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(Constants.XIAN).include(Constants.CHENGDU)
//                .include(Constants.BEIJING).include(latlng).build();
//        LatLngBounds bounds = new LatLngBounds.Builder()
//                .include(latlng)
//                .build();
//        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
        LatLng marker1 = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
        //设置中心点和缩放比例
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(marker1));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }

    /**
     * 监听自定义infowindow窗口的infocontents事件回调
     */
    @Override
    public View getInfoContents(Marker marker) {
//        if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_contents) {
//            return null;
//        }
        View infoContent = getLayoutInflater().inflate(
                R.layout.custom_info_contents, null);
        render(marker, infoContent);
        return infoContent;
    }

    /**
     * 监听自定义infowindow窗口的infowindow事件回调
     */
    @Override
    public View getInfoWindow(Marker marker) {
//        if (radioOption.getCheckedRadioButtonId() != R.id.custom_info_window) {
//            return null;
//        }
        View infoWindow = getLayoutInflater().inflate(
                R.layout.custom_info_window, null);

        render(marker, infoWindow);
        return infoWindow;
    }

    /**
     * 自定义infowinfow窗口
     */
    public void render(Marker marker, View view) {
//        if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_contents) {
//            ((ImageView) view.findViewById(R.id.badge))
//                    .setImageResource(R.drawable.badge_sa);
//        } else if (radioOption.getCheckedRadioButtonId() == R.id.custom_info_window) {
//            ImageView imageView = (ImageView) view.findViewById(R.id.badge);
//            imageView.setImageResource(R.drawable.badge_wa);
//        }
        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.BLACK), 0,
                    titleText.length(), 0);
            titleUi.setTextSize(14);
            titleUi.setText(titleText);

        } else {
            titleUi.setText("");
        }
        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0,
                    snippetText.length(), 0);
            snippetUi.setTextSize(14);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_map:
            {
                if(StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.latStr) || StringUtil.isNullOrEmpty(MeirenmeibaAppApplication.lngStr)){
                    showMsg(DianpuLocationMapActivity.this, "无法定位您的位置，不能规划路线，请先打开你的GPS！");
                }else{
                    //路线规划
                    Intent intent = new Intent(DianpuLocationMapActivity.this, RouteActivity.class);
                    intent.putExtra("lat_company",lat);
                    intent.putExtra("lng_company",lng);
                    startActivity(intent);
                }
            }
                break;
        }
    }
}
