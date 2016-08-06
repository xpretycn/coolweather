package com.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

	/*province 建表语句
	 */
	public static final String CREATE_PORVINCE = "create table Province("
			+ "id integer primary key autoincrement,"
			+ "province_name text,"
			+ "province_code text)";
	/*city 建表语句*/
	public static final String CREATE_CITY = "create table City("
			+ "id integer primary key autoincrement,"
			+ "city_name text,"
			+ "city_code text,"
			+ "province_id integer)";
	
	/* country 创建语句*/
	public static final String CREATE_COUNTRY = "create table Country("
			+"id integer primary key autoincrement,"
			+"country_name text,"
			+"country_code text,"
			+"city_id integer)";
	
	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_PORVINCE);
		db.execSQL(CREATE_CITY);
		db.execSQL(CREATE_COUNTRY);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
