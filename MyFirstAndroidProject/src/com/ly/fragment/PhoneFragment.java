package com.ly.fragment;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.map.v;
import com.ly.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PhoneFragment extends Fragment{

	private View view;
	private MapView mapView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
        //注意该方法要再setContentView方法之前实现  
        SDKInitializer.initialize(getActivity().getApplicationContext());  
        view = inflater.inflate(R.layout.phone_fragment, container, false);
      //获取地图控件引用  
        mapView = (MapView) view.findViewById(R.id.bmapView);  
		return view;
	}
}
