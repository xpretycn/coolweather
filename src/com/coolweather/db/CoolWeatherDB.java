package com.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coolweather.model.City;
import com.coolweather.model.Country;
import com.coolweather.model.Province;

public class CoolWeatherDB {

	public static final String DB_NAME = "cool_weather";
	public static final int VERSION = 1;
	private SQLiteDatabase db;
	private static CoolWeatherDB coolWeatherDB;
	
	//本类设计为单例模式，全项目唯一，构造方法私有化
	
	private CoolWeatherDB(Context context){
		
		CoolWeatherOpenHelper dbHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}
	
	//提供获取本类实例的方法
	
	public static synchronized CoolWeatherDB getInstance(Context context){
		if (coolWeatherDB == null){
			coolWeatherDB = new CoolWeatherDB(context);
		}
		return coolWeatherDB;
	}
	
	//提供存储省份的功能
	public void saveProvince(Province province){
		if (province != null){
			ContentValues values = new ContentValues();
			values.put("Province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("province", null, values);
		}
	}
	//提供访问省份的功能
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		
		while(cursor.moveToNext()){
			Province province = new Province();
			province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
			province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
			province.setProvinceId(cursor.getInt(cursor.getColumnIndex("id")));
			list.add(province);
		}
		
		return list;
	}
	
	//提供存储市区的功能
		public void saveCity(City city){
			if (city != null){
				ContentValues values = new ContentValues();
				values.put("city_name", city.getCityName());
				values.put("city_code", city.getCityCode());
				values.put("province_id", city.getProvinceId() );
				db.insert("city", null, values);
			}
		}
		//提供访问市区的功能
		public List<City> loadCity(int provinceId){
			List<City> list = new ArrayList<City>();
			Cursor cursor = db.query("City", null, "province_id = ?", 
					new String[]{String.valueOf(provinceId)}, null, null, null);
			
			while(cursor.moveToNext()){
				City city = new City();
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
				list.add(city);
			}
			
			return list;
		}
	
		//提供存储country的功能
		public void saveCountry(Country country){
			if (country != null){
				ContentValues values = new ContentValues();
				values.put("country_name", country.getCountryName());
				values.put("country_code", country.getCountryCode());
				values.put("city_id", country.getCityId() );
				db.insert("country", null, values);
			}
		}
		//提供访问country的功能
		public List<Country> loadCountry(int cityId){
			List<Country> list = new ArrayList<Country>();
			Cursor cursor = db.query("Country", null, "city_id = ?", 
					new String[]{String.valueOf(cityId)}, null, null, null);
			
			while(cursor.moveToNext()){
				Country country  = new Country();
				country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
				country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
				country.setCountryId(cursor.getInt(cursor.getColumnIndex("id")));
				country.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
				list.add(country);
			}
			
			return list;
		}		
}
