package com.ly.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ly.R;
import com.ly.view.AutoListView;
import com.ly.view.AutoListView.OnLoadListener;
import com.ly.view.AutoListView.OnRefreshListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SimpleAdapter;

public class ListViewActivity extends Activity implements OnRefreshListener, OnLoadListener{
	
	private AutoListView listView;
	private List<Map<String, String>> itemMaps = new ArrayList<Map<String,String>>();
	private Handler handler;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listview_activity);
		listView = (AutoListView) findViewById(R.id.autolistview);
		initData();
	}
	
	public void initData(){
		Map<String, String> map = new HashMap<String, String>();
		for(int i = 0; i<10; i++){
			map = new HashMap<String, String>();
			map.put("title", "title"+i*100);
			map.put("info", "info1"+i*100);
			itemMaps.add(map);
		}
		
		SimpleAdapter adapter = new SimpleAdapter(this, itemMaps, R.layout.listview_item, new String[]{"title", "info"},
				new int[]{R.id.item_title, R.id.item_info});

		listView.setAdapter(adapter);
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);
		handler = new Handler();
	}

	@Override
	public void onLoad() {
		
	}

	@Override
	public void onRefresh() {
		
	}

}
