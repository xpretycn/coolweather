package com.coolweather.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.util.Log;

public class HttpUtil {

	/**
	 * ���b�l��Ո��ķ���
	 * @param address �YԴ��ַ 
	 * @param listener ���{�����ӿ�
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
						listener.onFinish(response.toString());   //���{onFinish ���Y������
					}
				}catch (Exception e){
					if (listener != null){
						Log.v("In HttpUtil......", "e =" + e.toString());
						listener.onError(e);  //���{onError ̎����
						
					}
				}finally{
					
					if (connection != null){
							connection.disconnect();  //ጷ��YԴ
					}
				}
			}
			
		}).start();
	}
}
