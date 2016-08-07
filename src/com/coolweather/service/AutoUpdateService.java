package com.coolweather.service;

import com.coolweather.receiver.AutoUpdateReceiver;
import com.coolweather.util.HttpCallBackListener;
import com.coolweather.util.HttpUtil;
import com.coolweather.util.ParseUtility;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;

public class AutoUpdateService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				updateWeather();
			}
		}).start();
		//时钟设置
		AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
		int anHour = 8 * 60 * 60 * 1000;  //8小时的毫秒数
		long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
		Intent pintent = new Intent(this, AutoUpdateReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, pintent, 0);
		manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
		
		return super.onStartCommand(intent, flags, startId);
	}

	protected void updateWeather() {
		// TODO Auto-generated method stub
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String countryIndex = prefs.getString("country_index", "");
		String address = "http://www.weather.com.cn/data/cityinfo/" +  "10119040" +
				countryIndex + ".html";
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
			
			@Override
			public void onFinish(String response) {
				ParseUtility.handleWeatherReport(AutoUpdateService.this,response);
			}
			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
			
		});
	}	
}

