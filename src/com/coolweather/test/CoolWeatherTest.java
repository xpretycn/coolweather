package com.coolweather.test;

import java.util.List;

import android.app.AlertDialog;
import android.test.AndroidTestCase;

import com.coolweather.db.CoolWeatherDB;
import com.coolweather.model.Province;
import com.coolweather.util.ParseUtility;

public class CoolWeatherTest extends AndroidTestCase {

	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	public void testDB(){
		
		Province province = new Province();
		province.setProvinceCode("190990");
		province.setProvinceName("beijing");
		
		CoolWeatherDB db = CoolWeatherDB.getInstance(getContext());
		
		db.saveProvince(province);
		boolean flag = ParseUtility.handleProvinceResponse(db, "190990|beijing");
		assertEquals(true, flag);
	}

	@Override
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		super.tearDown();
	}

	
}
