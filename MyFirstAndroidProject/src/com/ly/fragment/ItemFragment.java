package com.ly.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ly.R;

public class ItemFragment extends Fragment{
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.home_fragment, container, false);
		
		/*TextView textView = (TextView) view.findViewById(R.id.home_text);
		Bundle bundle = getArguments();
		String title = bundle.getString("title");
		textView.setText(title);*/
		return view; 
	}

}
