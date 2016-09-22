package com.ly.adapter;

import com.ly.fragment.HomeTabFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class HomeFragmentPagerAdapter extends FragmentPagerAdapter{

	public HomeFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int arg0) {
		Fragment fragment = new HomeTabFragment();
		Bundle bundle = new Bundle();
		switch (arg0) {
		case 0:
			bundle.putString("content", "ȫ��");
			break;
		case 1:
			bundle.putString("content", "δǩ��");
			break;
		case 2:
			bundle.putString("content", "��ǩ��");
			break;
		case 3:
			bundle.putString("content", "����վ");
			break;

		default:
			break;
		}
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public int getCount() {
		return 4;
	}

}
