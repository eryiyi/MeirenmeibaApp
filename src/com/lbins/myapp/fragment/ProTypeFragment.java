package com.lbins.myapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.lbins.myapp.MeirenmeibaAppApplication;
import com.lbins.myapp.R;
import com.lbins.myapp.adapter.AnimateFirstDisplayListener;
import com.lbins.myapp.adapter.GridViewAdapter;
import com.lbins.myapp.base.BaseFragment;
import com.lbins.myapp.base.InternetURL;
import com.lbins.myapp.data.GoodsTypeData;
import com.lbins.myapp.entity.GoodsType;
import com.lbins.myapp.ui.SearchGoodsByTypeActivity;
import com.lbins.myapp.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProTypeFragment extends BaseFragment {

	private ArrayList<GoodsType> list = new ArrayList<GoodsType>();
	private GridView gridView;
	private GridViewAdapter adapter;
	private GoodsType goodsType;
	private TextView toptype;
	private ImageView icon;

	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
	ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_pro_type, null);
		gridView = (GridView) view.findViewById(R.id.listView);
		int index = getArguments().getInt("index");
		goodsType = TuijianFragment.listGoodsType.get(index);
		toptype = (TextView) view.findViewById(R.id.toptype);
		icon = (ImageView) view.findViewById(R.id.icon);
		if(goodsType != null){
			toptype.setText(goodsType.getTypeName());
			imageLoader.displayImage(goodsType.getTypeCover(), icon, MeirenmeibaAppApplication.txOptions, animateFirstListener);
		}
		GetTypeList();
		adapter = new GridViewAdapter(getActivity(), list);
		gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				if(list !=null){
					if(list.size() > i){
						GoodsType goodsType1 = list.get(i);
						if(goodsType1 != null){
							Intent intent = new Intent(getActivity(), SearchGoodsByTypeActivity.class);
							intent.putExtra("typeId", goodsType1.getTypeId());
							intent.putExtra("typeName", goodsType1.getTypeName());
							intent.putExtra("keyContent", "");
							startActivity(intent);
						}
					}
				}

			}
		});

		return view;
	}

	private void GetTypeList() {
			StringRequest request = new StringRequest(
					Request.Method.POST,
					InternetURL.GET_GOODS_SMALL_TYPE_URL,
					new Response.Listener<String>() {
						@Override
						public void onResponse(String s) {
							if (StringUtil.isJson(s)) {
								try {
									JSONObject jo = new JSONObject(s);
									String code = jo.getString("code");
									if (Integer.parseInt(code) == 200) {
										list.clear();
										Gson gson = getGson();
										if(gson != null){
											GoodsTypeData data = gson.fromJson(s, GoodsTypeData.class);
											list.addAll(data.getData());
											adapter.notifyDataSetChanged();
										}
									}else {
										Toast.makeText(getActivity(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}


							}
						}
					},
					new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError volleyError) {
							Toast.makeText(getActivity(), getResources().getString(R.string.get_data_error), Toast.LENGTH_SHORT).show();
						}
					}
			) {
				@Override
				protected Map<String, String> getParams() throws AuthFailureError {
					Map<String, String> params = new HashMap<String, String>();
					params.put("is_hot", "");
					params.put("type_isuse", "0");
					params.put("type_id", goodsType.getTypeId());
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

	@Override
	public void onStop() {
		releaseImageViews();
		super.onStop();
	}

	private void releaseImageViews() {
		if(icon != null){
			releaseImageView(icon);
		}
	}

	private void releaseImageView(ImageView imageView) {
		Drawable d = imageView.getDrawable();
		if (d != null)
			d.setCallback(null);
		imageView.setImageDrawable(null);
		imageView.setBackgroundDrawable(null);
	}

}
