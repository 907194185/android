package com.ly.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ly.R;
import com.ly.view.AutoListView;
import com.ly.view.AutoListView.OnLoadListener;
import com.ly.view.AutoListView.OnRefreshListener;

public class HomeTabFragment extends Fragment implements OnLoadListener, OnRefreshListener, OnItemClickListener{

	private AutoListView listView;
	private View view;
	private List<String> items = new ArrayList<String>();
	private List<Map<String,String>> itemMaps = new ArrayList<Map<String,String>>();
	private Handler handler;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.home_main, container, false);

		/*TextView textView = (TextView) view.findViewById(R.id.home_main_text);
		Bundle bundle = getArguments();
		textView.setText(bundle.getString("content"));*/
		initUI();
		//initData();
		initSimpleData();
		return view;
	}

	private void initUI(){
		listView = (AutoListView) view.findViewById(R.id.home_listview);
	}

	private void initData(){
		items = new ArrayList<String>();

		items.add("item1111111");
		items.add("item222222222");
		items.add("item33333333");
		items.add("item44444444444");
		items.add("item1111111");
		items.add("item1111111");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_expandable_list_item_1, items);
		listView.setAdapter(adapter);
	}

	private void initSimpleData(){
		itemMaps = new ArrayList<Map<String,String>>();

		Map<String, String> map = new HashMap<String, String>();


		for(int i = 0; i<10; i++){
			map = new HashMap<String, String>();
			map.put("title", "title"+i*100);
			map.put("info", "info1"+i*100);
			itemMaps.add(map);
		}
		listView.setResultSize(100);
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), itemMaps, R.layout.listview_item, new String[]{"title", "info"},
				new int[]{R.id.item_title, R.id.item_info});

		listView.setAdapter(adapter);
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);
		listView.setOnItemClickListener(this);
		handler = new Handler();



	}

	@Override
	public void onRefresh() {
		Log.i("refresh", "------进入刷新----");
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Map<String, String> map = new HashMap<String, String>();
				itemMaps = new ArrayList<Map<String,String>>();
				for(int i = 0; i<9; i++){
					map = new HashMap<String, String>();
					map.put("title", "title"+i*100+2);
					map.put("info", "info"+i*100+2);
					itemMaps.add(map);
				}

				listView.setResultSize(100);
				SimpleAdapter adapter = new SimpleAdapter(getActivity(), itemMaps, R.layout.listview_item, new String[]{"title", "info"},
						new int[]{R.id.item_title, R.id.item_info});

				listView.setAdapter(adapter);
				listView.onRefreshComplete();
			}
		}, 2000);

	}

	@Override
	public void onLoad() {
		Log.i("refresh", "------进入加载----");
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				Map<String, String> map = new HashMap<String, String>();
				for(int i = 0; i<listView.getPageSize() - itemMaps.size(); i++){
					map = new HashMap<String, String>();
					map.put("title", "title"+i*110+2);
					map.put("info", "info"+i*110+2);
					itemMaps.add(map);
				}

				//listView.setResultSize(itemMaps.size());
				SimpleAdapter adapter = new SimpleAdapter(getActivity(), itemMaps, R.layout.listview_item, new String[]{"title", "info"},
						new int[]{R.id.item_title, R.id.item_info});

				listView.setAdapter(adapter);
				listView.onLoadComplete();
			}
		}, 5000);
		//listView.onLoadComplete();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(getActivity(), position + "---" + id, Toast.LENGTH_SHORT).show();
	}

}
