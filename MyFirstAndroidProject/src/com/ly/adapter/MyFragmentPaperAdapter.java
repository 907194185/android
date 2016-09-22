package com.ly.adapter;

import java.util.ArrayList;
import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyFragmentPaperAdapter extends FragmentPagerAdapter{
	
	private List<Fragment> list;

	public MyFragmentPaperAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		this.list = fragments;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
