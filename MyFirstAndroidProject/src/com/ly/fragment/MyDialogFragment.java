package com.ly.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.ly.R;

public class MyDialogFragment extends DialogFragment{

	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE); 
		view = inflater.inflate(R.layout.dialog_fragment, container);
		return view;
	}
}
