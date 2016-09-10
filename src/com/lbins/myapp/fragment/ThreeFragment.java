package com.lbins.myapp.fragment;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lbins.myapp.R;
import com.lbins.myapp.base.BaseFragment;

/**
 * Created by zhl on 2016/7/1.
 */
public class ThreeFragment extends BaseFragment {
    private View view;
    private Resources res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.one_fragment, null);
        res = getActivity().getResources();
        return view;
    }
}
