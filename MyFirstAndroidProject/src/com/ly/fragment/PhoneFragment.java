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
		//��ʹ��SDK�����֮ǰ��ʼ��context��Ϣ������ApplicationContext  
		//ע��÷���Ҫ��setContentView����֮ǰʵ��  
		SDKInitializer.initialize(getActivity().getApplicationContext()); 
		view = inflater.inflate(R.layout.phone_fragment, container, false);
		initUI();
		return view;
	}

	public void initUI(){

		//��ȡ��ͼ�ؼ�����  
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
		//��ѡ��Ĭ�ϸ߾��ȣ����ö�λģʽ���߾��ȣ��͹��ģ����豸
		option.setLocationMode(LocationMode.Hight_Accuracy);
		//��ѡ��Ĭ��gcj02�����÷��صĶ�λ�������ϵ
		option.setCoorType("bd09ll");
		//��ѡ��Ĭ��0��������λһ�Σ����÷���λ����ļ����Ҫ���ڵ���1000ms������Ч��
		int span = 1000;
		option.setScanSpan(span);
		//��ѡ�������Ƿ���Ҫ��ַ��Ϣ��Ĭ�ϲ���Ҫ
		option.setIsNeedAddress(true);
		//��ѡ��Ĭ��false,�����Ƿ�ʹ��gps
		option.setOpenGps(true);
		//��ѡ��Ĭ��false�������Ƿ�gps��Чʱ����1S1��Ƶ�����GPS���
		option.setLocationNotify(true);
		//��ѡ��Ĭ��false�������Ƿ���Ҫλ�����廯�����������BDLocation.getLocationDescribe��õ�����������ڡ��ڱ����찲�Ÿ�����
		option.setIsNeedLocationDescribe(true);
		//��ѡ��Ĭ��false�������Ƿ���ҪPOI�����������BDLocation.getPoiList��õ�
		option.setIsNeedLocationPoiList(true);
		//��ѡ��Ĭ��true����λSDK�ڲ���һ��SERVICE�����ŵ��˶������̣������Ƿ���stop��ʱ��ɱ��������̣�Ĭ�ϲ�ɱ��  
		option.setIgnoreKillProcess(false);
		//��ѡ��Ĭ��false�������Ƿ��ռ�CRASH��Ϣ��Ĭ���ռ�
		option.SetIgnoreCacheException(false);
		//��ѡ��Ĭ��false�������Ƿ���Ҫ����gps��������Ĭ����Ҫ
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
		if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS��λ���
			sb.append("\nspeed : ");
			sb.append(location.getSpeed());// ��λ������ÿСʱ
			sb.append("\nsatellite : ");
			sb.append(location.getSatelliteNumber());
			sb.append("\nheight : ");
			sb.append(location.getAltitude());// ��λ����
			sb.append("\ndirection : ");
			sb.append(location.getDirection());// ��λ��
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			sb.append("\ndescribe : ");
			sb.append("gps��λ�ɹ�");

		} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// ���綨λ���
			sb.append("\naddr : ");
			sb.append(location.getAddrStr());
			//��Ӫ����Ϣ
			sb.append("\noperationers : ");
			sb.append(location.getOperators());
			sb.append("\ndescribe : ");
			sb.append("���綨λ�ɹ�");
		} else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// ���߶�λ���
			sb.append("\ndescribe : ");
			sb.append("���߶�λ�ɹ������߶�λ���Ҳ����Ч��");
		} else if (location.getLocType() == BDLocation.TypeServerError) {
			sb.append("\ndescribe : ");
			sb.append("��������綨λʧ�ܣ����Է���IMEI�źʹ��嶨λʱ�䵽loc-bugs@baidu.com��������׷��ԭ��");
		} else if (location.getLocType() == BDLocation.TypeNetWorkException) {
			sb.append("\ndescribe : ");
			sb.append("���粻ͬ���¶�λʧ�ܣ����������Ƿ�ͨ��");
		} else if (location.getLocType() == BDLocation.TypeCriteriaException) {
			sb.append("\ndescribe : ");
			sb.append("�޷���ȡ��Ч��λ���ݵ��¶�λʧ�ܣ�һ���������ֻ���ԭ�򣬴��ڷ���ģʽ��һ���������ֽ�����������������ֻ�");
		}
		sb.append("\nlocationdescribe : ");
		sb.append(location.getLocationDescribe());// λ�����廯��Ϣ
		List<Poi> list = location.getPoiList();// POI����
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
		// �˳�ʱ���ٶ�λ
		locationClient.stop();
		// �رն�λͼ��
		baiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		super.onDestroy();
	}
}
