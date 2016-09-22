package com.ly.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class DownloadUtil {

	private HttpURLConnection conn;
	private static final String TAG = "DownLoadUtil";

	public String downloadAsString(String url, Handler handler){
		StringBuilder sb = new StringBuilder();
		String temp = null;
		Message msg = handler.obtainMessage();	
		try {
			InputStream is = getConnection(url).getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"GBK"));
			while((temp = br.readLine()) != null){
				sb.append(temp);
				msg.obj = sb.toString();
				msg.what = 2;
				handler.sendMessage(msg);
				Thread.sleep(1000);
				msg = handler.obtainMessage();
			}
			br.close();
			is.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.i(TAG, "获取IO出错:"+e.getMessage());
		}

		return sb.toString();
	}

	protected HttpURLConnection getConnection(String urlStr){
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
		} catch (Exception e) {
			e.printStackTrace();
			Log.i(TAG, "获取服务器连接出错:"+e.getMessage());
		}
		return conn;
	}

}
