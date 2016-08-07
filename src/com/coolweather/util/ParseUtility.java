package com.coolweather.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.coolweather.db.CoolWeatherDB;
import com.coolweather.model.City;
import com.coolweather.model.Country;
import com.coolweather.model.Province;

//用於解析請求服務器后的返回結果
public class ParseUtility {

	//解析返回的省份數據
	public synchronized static boolean handleProvinceResponse(CoolWeatherDB db, String response){
		
		if ( !TextUtils.isEmpty(response)){
			String allProvinces[] = response.split(",");
			if ( allProvinces != null && allProvinces.length > 0){
				for(String pro : allProvinces){
					String[] array = pro.split("//|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					
					db.saveProvince(province);
				}
				return true;
			}
		}
		
		return false;
	}
	
	//解析返回的市數據
	
	public synchronized static boolean handleCityResponse(CoolWeatherDB db, String response, int provinceId){
		
		if ( !TextUtils.isEmpty(response)){
			String allCities[] = response.split(",");
			if ( allCities != null && allCities.length > 0){
				for(String cit : allCities){
					String[] array = cit.split("//|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					
					db.saveCity(city);
				}
				return true;
			}
		}
		
		return false;
	}
	
	//解析返回的鄉鎮數據
	
	public synchronized static boolean handleCountryResponse(CoolWeatherDB db, String response, int cityId){
		
		if ( !TextUtils.isEmpty(response)){
			String allCountries[] = response.split(",");
			if ( allCountries != null && allCountries.length > 0){
				for(String coun : allCountries){
					String[] array = coun.split("//|");
					Country country = new Country();
					country.setCountryCode(array[0]);
					country.setCountryName(array[1]);
					country.setCityId(cityId);
					
					db.saveCountry(country);
				}
				return true;
			}
		}
		
		return false;
	}
	public static void handleWeatherReport(Context context, String response){
		Log.v("In ParseUtility.....", "response = "+ response);
		try{
			JSONObject jsonObject = new JSONObject(response);
			JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
			String cityName = weatherInfo.getString("city");
			String weatherCode = weatherInfo.getString("cityid");
			String temp1 = weatherInfo.getString("temp1");
			String temp2 = weatherInfo.getString("temp2");
			String weatherDesp = weatherInfo.getString("weather");
			String publishTime = weatherInfo.getString("ptime");
			
			Log.v("In ParseUtility.....", cityName+" "+weatherCode+" "+temp1+" "+temp2
					+" "+weatherDesp+" "+publishTime);
			
			saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
			
		}catch(JSONException e){
			e.printStackTrace();
		}
	}
	//将服务器返回的天气信息存贮到SharedPreferenced文件中
	private static void saveWeatherInfo(Context context, String cityName,
			String weatherCode, String temp1, String temp2, String weatherDesp,
			String publishTime) {
		// TODO Auto-generated method stub
		SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
		SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
		
		editor.putString("city_name", cityName);
		editor.putString("weather_code", weatherCode);
		editor.putString("temp1", temp1);
		editor.putString("temp2", temp2);
		editor.putString("weather_desp", weatherDesp);
		editor.putString("publish_time", publishTime);
		editor.putString("current_date", format.format(new Date()));
		
		editor.commit();
	}
}
