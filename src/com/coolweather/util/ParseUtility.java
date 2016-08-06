package com.coolweather.util;

import android.text.TextUtils;

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
	
}
