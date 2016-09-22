package com.ly.fragment;

import com.ly.R;
import com.ly.adapter.HomeFragmentPagerAdapter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class HomeFragment extends Fragment implements OnPageChangeListener, OnClickListener{

	private ViewPager viewPager;
	private View view;
	private FragmentPagerAdapter adapter;

	private TextView all, not_get_in, get_in, receive;
	private View all_line, not_get_in_line, get_in_line, receive_line; 


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.home_fragment, container, false);
		initUi();
		return view;
	}

	private void initUi() {
		all = (TextView) view.findViewById(R.id.all);
		not_get_in = (TextView) view.findViewById(R.id.not_get_in);
		get_in = (TextView) view.findViewById(R.id.get_in);
		receive = (TextView) view.findViewById(R.id.receive);
		
		all.setOnClickListener(this);
		not_get_in.setOnClickListener(this);
		get_in.setOnClickListener(this);
		receive.setOnClickListener(this);

		all_line = view.findViewById(R.id.tab_all_line);
		not_get_in_line = view.findViewById(R.id.not_get_in_line);
		get_in_line = view.findViewById(R.id.get_in_line);
		receive_line = view.findViewById(R.id.receive_line);



		viewPager = (ViewPager) view.findViewById(R.id.home_fragmentVP);
		adapter = new HomeFragmentPagerAdapter(getFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);

	}

	//viewpager 事件 -----------start
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {

		switch (arg0) {
		case 0:
			all_line.setBackgroundColor(getResources().getColor(R.color.home_tab_selected));
			not_get_in_line.setBackgroundResource(0);
			get_in_line.setBackgroundResource(0);
			receive_line.setBackgroundResource(0);
			break;
		case 1:
			all_line.setBackgroundResource(0);
			//all_line.setBackgroundColor(getResources().getColor(R.color.home_tab_not_selected));
			not_get_in_line.setBackgroundColor(getResources().getColor(R.color.home_tab_selected));
			get_in_line.setBackgroundResource(0);
			receive_line.setBackgroundResource(0);
			break;
		case 2:
			all_line.setBackgroundResource(0);
			not_get_in_line.setBackgroundResource(0);
			get_in_line.setBackgroundColor(getResources().getColor(R.color.home_tab_selected));
			receive_line.setBackgroundResource(0);
			break;
		case 3:
			all_line.setBackgroundResource(0);
			not_get_in_line.setBackgroundResource(0);
			get_in_line.setBackgroundResource(0);
			receive_line.setBackgroundColor(getResources().getColor(R.color.home_tab_selected));
			break;
		default:
			break;
		}
	}

	//viewpager 事件 -----------end

	//文字单击-----start
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.all:
			viewPager.setCurrentItem(0);
			break;
		case R.id.not_get_in:
			viewPager.setCurrentItem(1);
			break;
		case R.id.get_in:
			viewPager.setCurrentItem(2);
			break;
		case R.id.receive:
			viewPager.setCurrentItem(3);
			break;

		default:
			break;
		}
	}

	//文字单击-----end

}
