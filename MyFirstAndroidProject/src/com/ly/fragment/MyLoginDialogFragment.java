package com.ly.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.ly.R;

public class MyLoginDialogFragment extends DialogFragment{

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.login_dialog, null);
		builder.setView(view)
				.setPositiveButton("登录", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
					}
				}).setNegativeButton("取消", null);
		Dialog dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);// 设置点击屏幕Dialog不消失
		return dialog;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		/*Window window = getDialog().getWindow();
		WindowManager.LayoutParams windowParams = window.getAttributes();
		windowParams.dimAmount = 1f;
		window.setAttributes(windowParams);*/
	}
}
