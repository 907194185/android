package com.ly.adapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ly.fragment.HomeFragment;
import com.ly.fragment.ItemFragment;

public class TabAdapter extends FragmentPagerAdapter{

	private List<Fragment> list;
	public static String[] titles = new String[]{ "头条", "房产", "另一面", "女人",  
            "财经", "数码", "情感", "科技"};
	
	public TabAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.list = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = new ItemFragment();
		Bundle bundle = new Bundle();
		bundle.putString("title", titles[arg0]);
		fragment.setArguments(bundle);
		
		return fragment;
	}

	@Override
	public int getCount() {
		return titles.length;
	}
	
	@Override
	public CharSequence getPageTitle(int position) {
		return titles[position % titles.length];
	}

}
