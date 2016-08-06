package com.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {

	/**
	 * 封裝發送請求的方法
	 * @param address 資源地址 
	 * @param listener 回調函數接口
	 */
	public static void sendHttpRequest(final String address, final HttpCallBackListener listener){
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				HttpURLConnection connection = null;
				try{
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					InputStream in = connection.getInputStream();
					
					BufferedReader bfrd = new BufferedReader(new InputStreamReader(in));
					StringBuilder response = new StringBuilder();
					String line;
					
					while ((line = bfrd.readLine()) != null){
						
							response.append(line);
					}
					Log.v("In HttpUtil......", "response =" +response.toString());
					if (listener != null){
						listener.onFinish(response.toString());   //回調onFinish 將結果傳回
					}
				}catch (Exception e){
					if (listener != null){
						Log.v("In HttpUtil......", "e =" + e.toString());
						listener.onError(e);  //回調onError 處理異常
						
					}
				}finally{
					
					if (connection != null){
							connection.disconnect();  //釋放資源
					}
				}
			}
			
		}).start();
	}
}
