package com.ly.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.ly.R;
import com.ly.listener.MyLocationListener;

public class PhoneFragment extends Fragment implements BDLocationListener{

	private View view;
	private MapView mapView;
	private BaiduMap baiduMap;
	private LocationClient locationClient;
	private BDLocationListener listener = new MyLocationListener();
	private boolean isFirst = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//在使用SDK各组件之前初始化context信息，传入ApplicationContext  
		//注意该方法要再setContentView方法之前实现  
		SDKInitializer.initialize(getActivity().getApplicationContext()); 
		view = inflater.inflate(R.layout.phone_fragment, container, false);
		initUI();
		return view;
	}

	public void initUI(){

		//获取地图控件引用  
		mapView = (MapView) view.findViewById(R.id.bmapView); 
		baiduMap = mapView.getMap();
		baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		baiduMap.setMyLocationEnabled(true);
		baiduMap.setIndoorEnable(true);

		locationClient = new LocationClient(getActivity().getApplicationContext());
		locationClient.registerLocationListener(this);
		initLocationOptions();
		Toast.makeText(getActivity(), "set option after", Toast.LENGTH_SHORT).show();
		Intent intent=new Intent("com.baidu.location.f");  
		getActivity().startService(intent);
		locationClient.start();
	}

	public void initLocationOptions(){
		Toast.makeText(getActivity(), "set option", Toast.LENGTH_SHORT).show();
		LocationClientOption option = new LocationClientOption();
		//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
		option.setLocationMode(LocationMode.Hight_Accuracy);
		//可选，默认gcj02，设置返回的定位结果坐标系
		option.setCoorType("bd09ll");
		//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
		int span = 1000;
		option.setScanSpan(span);
		//可选，设置是否需要地址信息，默认不需要
		option.setIsNeedAddress(true);
		//可选，默认false,设置是否使用gps
		option.setOpenGps(true);
		//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
		option.setLocationNotify(true);
		//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
		option.setIsNeedLocationDescribe(true);
		//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
		option.setIsNeedLocationPoiList(true);
		//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死  
		option.setIgnoreKillProcess(false);
		//可选，默认false，设置是否收集CRASH信息，默认收集
		option.SetIgnoreCacheException(false);
		//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
		option.setEnableSimulateGps(false);
		locationClient.setLocOption(option);
	}

	@Override
	public void onReceiveLocation(BDLocation location) {
		//Receive Location
		StringBuffer sb = new StringBuffer(256);
		sb.append("time : ");
		sb.append(location.getTime());
		sb.append("\nerror code : ");
		sb.append(location.getLocType());
		sb.append("\nlatitude : ");
		sb.append(location.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(location.getLongitude());
		sb.append("\nradius : ");
		sb.append(location.getRadius());
		if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());// 单位：公里每小时
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			sb.append("\nheight : ");
			sb.append(location.getAltitude());// 单位：米
			sb.append("\ndirection : ");
			sb.append(location.getDirection());// 单位度
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			sb.append("\ndescribe : ");
			sb.append("gps定位成功");

		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			//运营商信息
			sb.append("\noperationers : ");
			sb.append(location.getOperators());
			sb.append("\ndescribe : ");
			sb.append("网络定位成功");
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
			sb.append("\ndescribe : ");
			sb.append("离线定位成功，离线定位结果也是有效的");
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			sb.append("\ndescribe : ");
			sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			sb.append("\ndescribe : ");
			sb.append("网络不同导致定位失败，请检查网络是否通畅");
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			sb.append("\ndescribe : ");
			sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
		}
		sb.append("\nlocationdescribe : ");
		sb.append(location.getLocationDescribe());// 位置语义化信息
		List<Poi> list = location.getPoiList();// POI数据
		if (list != null) {
			sb.append("\npoilist size = : ");
			sb.append(list.size());
			for (Poi p : list) {
				sb.append("\npoi= : ");
				sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
			}
		}

		if (isFirst) {
			MyLocationData mLocationData = new MyLocationData.Builder().accuracy(location.getRadius())
					.direction(100).latitude(location.getLatitude())  
					.longitude(location.getLongitude()).build();
			baiduMap.setMyLocationData(mLocationData);
			Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_SHORT).show();
			isFirst = false;
			LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatus.Builder builder = new MapStatus.Builder();
            builder.target(ll).zoom(18.0f);
            baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
		}
		Log.i("BaiduLocationApiDem", sb.toString());

	}



	@Override
	public void onPause() {
		mapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		// 退出时销毁定位
		locationClient.stop();
		// 关闭定位图层
		baiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		super.onDestroy();
	}
}
