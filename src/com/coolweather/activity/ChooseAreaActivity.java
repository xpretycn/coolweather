package com.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.app.R;
import com.coolweather.db.CoolWeatherDB;
import com.coolweather.model.City;
import com.coolweather.model.Country;
import com.coolweather.model.Province;
import com.coolweather.util.HttpCallBackListener;
import com.coolweather.util.HttpUtil;
import com.coolweather.util.ParseUtility;

public class ChooseAreaActivity extends Activity {

	public final int LEVEL_PROVINCE = 0;
	public final int LEVEL_CITY = 1;
	public final int LEVEL_COUNTRY = 2;
	
	private ProgressDialog progressDialog;
	private ListView listView;
	private TextView textView;
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB db;
	private List<String> dataList = new ArrayList<String>();  //適配器數據列表
	
	private List<Province> provinceList;  //省列表
	private List<City> cityList;   //市列表
	private List<Country> countryList;  //賢列表
	
	private Province selectedProvince;  //當前選中的省
	private City selectedCity;   //當前選中的市
	private int currentLEVEL;   //當前選中的級別

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		
		listView = (ListView) findViewById(R.id.list_view);
		Log.v("In ChooseAreaActivity...", listView.toString());
		textView = (TextView) findViewById(R.id.title_text);
		adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, dataList);
		listView.setAdapter(adapter);
		db = CoolWeatherDB.getInstance(getApplicationContext());
		
		queryProvinces();  //加載省級資源
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (LEVEL_PROVINCE == currentLEVEL){
					selectedProvince = provinceList.get(arg2);
					queryCities();  //加載對應的市列表
				}else if (LEVEL_CITY == currentLEVEL){  
					selectedCity = cityList.get(arg2);
					queryCountries();  //加載對應的賢列表
				}
			}

			
		});
	}
	//查詢全國所有省，第一次從網絡查詢，以後從數據庫查詢（優先從數據庫查詢）
	private void queryProvinces() {
		// TODO Auto-generated method stub
		provinceList = db.loadProvinces();
		
		if (provinceList.size() > 0){
			dataList.clear();
			
			for(Province province : provinceList){
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);   //調整到第一個位置
			textView.setText("中國");
			currentLEVEL = LEVEL_PROVINCE;
		}else{
			queryFromServer(null, "province");
		}
		
	}
	
	private void queryCities() {
		// TODO Auto-generated method stub
		cityList = db.loadCity(selectedProvince.getProvinceId());
		
		if (cityList.size() > 0){
			dataList.clear();
			
			for (City city : cityList){
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedProvince.getProvinceName());
			currentLEVEL = LEVEL_CITY;
		}else{
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
		
	}

	private void queryCountries() {
		// TODO Auto-generated method stub
		countryList = db.loadCountry(selectedCity.getCityId());
		
		if (countryList.size() > 0){
			dataList.clear();
			
			for (Country country : countryList){
				dataList.add(country.getCountryName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			textView.setText(selectedCity.getCityName());
			currentLEVEL = LEVEL_COUNTRY;
		}else{
			queryFromServer(selectedCity.getCityCode(), "country");
		}
	}
	
	
	/**
	 * 根據傳入的地區代碼和類型，從服務器上查詢數據
	 * @param object 查詢條件
	 * @param string  用於區分查詢類型
	 */
	private void queryFromServer(String code, final String string) {
		// TODO Auto-generated method stub
		String addr;
		
		Log.v("queryFromServer......", "code = " + code + "  " + "String = " + string);
		
		if (TextUtils.isEmpty(code)){
			
			addr = "http://www.weather.com.cn/data/list3/city.xml";
		}else{
			addr = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
		}
		showProgressDialog();
		Log.v("queryFromServer......", "addr = " + addr);
		HttpUtil.sendHttpRequest(addr, new HttpCallBackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				boolean result = false;
				
				if ("province".equals(string)){
					result = ParseUtility.handleProvinceResponse(db, response);
				}else if ("city".equals(string)){
					result = ParseUtility.handleCityResponse(db, response, selectedProvince.getProvinceId());					
				}else if ("country".equals(string)){
					result = ParseUtility.handleCountryResponse(db, response, selectedCity.getCityId());
				}
				Log.v("result......", "result = " + result);
				if (result){
					//通過runOnUiThread()方法回到主線程處理邏輯
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							closeProgressDialog();
							
							if ("province".equals(string)){
								queryProvinces();
							}else if ("city".equals(string)){
								queryCities();
							}else if ("country".equals(string)){
								queryCountries();
							}
						}
					});
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				//通過runOnUiThread()方法回到主線程處理邏輯
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						closeProgressDialog();
						Toast.makeText(ChooseAreaActivity.this, "加載失敗啦...", 1).show();	
					}
				});
			}
		});
	}
	//進度對話框的設置提醒功能
	private void showProgressDialog() {
		// TODO Auto-generated method stub
		if (progressDialog == null){
			progressDialog = new ProgressDialog(getApplicationContext());
			progressDialog.setMessage("正在加載...");
			progressDialog.setCanceledOnTouchOutside(false);
			Log.v("In showProgressDialog......", progressDialog.toString());
		}
		progressDialog.show();
	}
	
	private void closeProgressDialog() {
		// TODO Auto-generated method stub
		if (progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
	//補貨back按鍵，根據當前的級別判斷，此時應該返回的是市列表還是省列表或者是退出
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (currentLEVEL == LEVEL_COUNTRY){
			queryCities();
		}else if (currentLEVEL == LEVEL_CITY){
			queryProvinces();
		}else{
			finish();
		}
		
	}
	
	
	
}
