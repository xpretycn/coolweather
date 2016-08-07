package com.coolweather.activity;

import com.coolweather.app.R;
import com.coolweather.util.HttpCallBackListener;
import com.coolweather.util.HttpUtil;
import com.coolweather.util.ParseUtility;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WeatherActivity extends Activity implements OnClickListener{

	private LinearLayout weatherInfoLayout; 
	private TextView cityNameText;   //������ʾ��������
	private TextView publishText;  //������ʾ����ʱ��
	/**
	* ������ʾ����������Ϣ
	*/
	private TextView weatherDespText;
	/**
	* ������ʾ����1
	*/
	private TextView temp1Text;
	/**
	* ������ʾ����2
	*/
	private TextView temp2Text;
	/**
	* ������ʾ��ǰ����
	*/
	private TextView currentDateText;
	/**
	* �л����а�ť
	*/
	private Button switchCity;
	/**
	* ����������ť
	*/
	private Button refreshWeather;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		
		// ��ʼ�����ؼ�
		weatherInfoLayout = (LinearLayout) findViewById(R.id.weather_info_layout);
		cityNameText = (TextView) findViewById(R.id.city_name);
		publishText = (TextView) findViewById(R.id.publish_text);
		weatherDespText = (TextView) findViewById(R.id.weather_desp);
		temp1Text = (TextView) findViewById(R.id.temp1);
		temp2Text = (TextView) findViewById(R.id.temp2);
		currentDateText = (TextView) findViewById(R.id.current_date);
		switchCity = (Button) findViewById(R.id.switch_city);
		refreshWeather = (Button) findViewById(R.id.refresh_weather);
		
		switchCity.setOnClickListener(this);
		refreshWeather.setOnClickListener(this);
		
		String countyIndex = getIntent().getStringExtra("county_index");
		
		if (!TextUtils.isEmpty(countyIndex)) {
		// ���ؼ�����ʱ��ȥ��ѯ����
			publishText.setText("ͬ����...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherInfo(countyIndex);
		} else {
		// û���ؼ�����ʱ��ֱ����ʾ��������
			showWeather();
		}
	}
	@Override
	public void onClick(View v) {
		
//		Log.v("in onClick......", v.toString());
		switch (v.getId()){
			case R.id.switch_city:
			Intent intent = new Intent(this, ChooseAreaActivity.class);
			intent.putExtra("from_weather_activity", true);
			startActivity(intent);
			finish();
			break;
		
			case R.id.refresh_weather:
			publishText.setText(" ͬ����...");
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			String countryIndex = prefs.getString("country_index", "");
			Log.v("In onClick......", "weatherCode = "+ countryIndex);
			
			if (!TextUtils.isEmpty(countryIndex)) {
				queryWeatherInfo(countryIndex);
			}  else {
				// û���ؼ�����ʱ��ֱ����ʾ��������
				showWeather();
			}
			break;
			
			default:
			break;
		}
	}

	
	/*//��ѯ�ؼ���������Ӧ����������
	private void queryWeatherCode(String countyCode) {
		// TODO Auto-generated method stub
		String address = "http://www.weather.com.cn/data/list3/city" +
				countyCode + ".xml";
				queryFromServer(address, "countyCode");
	}*/
	//��ѯ������������Ӧ������
	private void queryWeatherInfo(String weatherCode) {
		String address = "http://www.weather.com.cn/data/cityinfo/" + "10119040" +
		weatherCode + ".html";
		queryFromServer(address, "weatherCode");
	}
	
	//���ݴ���ĵ�ַ������ȥ���������ѯ�������Ż���������Ϣ
	private void queryFromServer(final String address, final String type) {
		
		HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
	
		@Override
		public void onFinish(final String response) {
		if ("countyCode".equals(type)) {
			if (!TextUtils.isEmpty(response)) {
			// �ӷ��������ص������н�������������
				String[] array = response.split("\\|");
				if (array != null && array.length == 2) {
					String weatherCode = array[1];
					queryWeatherInfo(weatherCode);
				}
			}
		} else if ("weatherCode".equals(type)) {
			// �������������ص�������Ϣ
			ParseUtility.handleWeatherReport(WeatherActivity.this,response);
				runOnUiThread(new Runnable() {
				@Override
					public void run() {
						showWeather();
					}
				});
			}
		}
		@Override
		public void onError(Exception e) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						publishText.setText("ͬ��ʧ��");
					}
				});
			}
		});
	}	
	
	// ��SharedPreferences�ļ��ж�ȡ�洢��������Ϣ������ʾ��������
	private void showWeather() {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		cityNameText.setText( prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("����" + prefs.getString("publish_time", "") + "����");
		currentDateText.setText(prefs.getString("current_date", ""));
		
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
	
	}
		
}