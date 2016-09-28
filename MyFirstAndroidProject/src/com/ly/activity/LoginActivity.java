package com.ly.activity;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ly.R;

public class LoginActivity extends Activity implements OnClickListener{

	private EditText username, password;
	private Button login;
	private CheckBox checkBox;
	private SharedPreferences sp;
	private String sysToken = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		initUI();
		initData();
	}

	private void initUI(){
		username = (EditText) this.findViewById(R.id.username);
		password = (EditText) this.findViewById(R.id.password);
		login = (Button) this.findViewById(R.id.loginBtn);
		checkBox = (CheckBox) this.findViewById(R.id.checkBtn);
		
		login.setOnClickListener(this);
	}

	private void initData(){
		sp = getSharedPreferences("loginSetting", MODE_PRIVATE);
		sysToken = sp.getString("systoken", "noData");
		Log.i("WeChat", sysToken);
		if (sysToken == "noData") {
			getSystemToken();
		}
	}


	private void getSystemToken(){
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL("http://192.168.1.77:81/index.php/user/logIn");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setRequestMethod("POST");
					conn.connect();

					OutputStream out = conn.getOutputStream();
					String params = "name=test&password=111111";
					out.write(params.getBytes());
					out.flush();
					if (conn.getResponseCode() == 200) {
						InputStream in = conn.getInputStream();
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						byte[] data = new byte[1024];
						int len = 0;
						while((len = in.read(data)) != -1){
							os.write(data, 0, len);
						}
						in.close();
						os.close();
						String result = new String(os.toByteArray());
						Log.i("WeChat", result);

						JSONObject object = new JSONObject(result);
						if (object.getString("message").equals("µÇÂ¼³É¹¦")) {
							Editor editor = sp.edit();
							editor.putString("systoken", object.getJSONObject("data").getString("token"));
							editor.commit();
						}
					}else {
						Log.i("WeChat", "·µ»ØÊ§°Ü");
					}

				} catch (Exception e) {
				}



			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginBtn:

			String name = username.getText().toString();
			String pwd = password.getText().toString();
			if (name.trim().length() < 1) {
				Toast.makeText(LoginActivity.this, "ÇëÊäÈëÓÃ»§Ãû£¡", Toast.LENGTH_SHORT).show();
				return;
			}
			if (pwd.trim().length() < 1) {
				Toast.makeText(LoginActivity.this, "ÇëÊäÈëÃÜÂë£¡", Toast.LENGTH_SHORT).show();
				return;
			}
			login(name, pwd);
			break;

		default:
			break;
		}
	}


	public void login(final String username, final String password){
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL("http://192.168.1.77:81/index.php/information/" + sysToken + "/user/login");
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setRequestMethod("POST");
					conn.connect();

					OutputStream out = conn.getOutputStream();
					
					JSONObject params = new JSONObject();
					JSONArray arr = new JSONArray();
					JSONObject obj = new JSONObject();
					obj.put("username", username);
					obj.put("password", password);
					obj.put("flag", 0);
					
					arr.put(obj);
					params.put("obj", arr.toString());
					params.put("page", "1");
					params.put("pageSize", "10");
					
					
					out.write(params.toString().replace("\\", "").getBytes());
					Log.i("WeChat", params.toString());
					out.flush();
					if (conn.getResponseCode() == 200) {
						InputStream in = conn.getInputStream();
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						byte[] data = new byte[1024];
						int len = 0;
						while((len = in.read(data)) != -1){
							os.write(data, 0, len);
						}
						in.close();
						os.close();
						String result = new String(os.toByteArray());
						Log.i("WeChat", result);

						JSONObject object = new JSONObject(result);
						
					}else {
						Log.i("WeChat", "·µ»ØÊ§°Ü");
					}

				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		}).start();
	}

}
