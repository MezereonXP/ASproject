package com.checkfile.checkfile.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.checkfile.checkfile.Adapter.LogisticsAdapter;
import com.checkfile.checkfile.R;
import com.checkfile.checkfile.SysApplication;
import com.checkfile.checkfile.View.CustomNodeListView;
import com.checkfile.checkfile.Adapter.LogisticsInfo;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class RoutineActivity extends Activity {
	
	
	
	private String filename,routineResult;
	private JSONObject jsonobject;
	private JSONArray jsonarray;
	private int  count=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);  
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.file_routine);
		Intent intent=getIntent();
		filename=intent.getStringExtra("filename");
		CustomNodeListView listview = (CustomNodeListView) this
				.findViewById(R.id.routine_listview);
		View headView = getLayoutInflater().inflate(R.layout.logistics_head_layout, null);
		headView.setFocusable(true);
		headView.setFocusableInTouchMode(true);
		headView.requestFocus();
		listview.addHeaderView(headView);
		try {
			routineResult=getResultForHttpGet(filename, "");
			jsonobject=new JSONObject(routineResult);
			try {
				jsonarray=jsonobject.getJSONArray("data");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		List<LogisticsInfo> datas = new ArrayList<LogisticsInfo>();
		for (int i = jsonarray.length()-1; i >=0; i--) {
			LogisticsInfo data = new LogisticsInfo();
			try {
				data.setDateTime(jsonarray.getJSONObject(i).getString("date"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				data.setInfo(jsonarray.getJSONObject(i).getString("name"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				if(jsonarray.getJSONObject(i).getString("state").equals("0")){
					data.setBrc(R.mipmap.no);
					count++;
				}else{
					data.setBrc(R.mipmap.yes);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			datas.add(data);
		}
		
		listview.getNodeLineView().setBignum(count-1);
		LogisticsAdapter adapter = new LogisticsAdapter(datas, this);
		listview.setAdapter(adapter);
	}
	
	
	 @TargetApi(VERSION_CODES.GINGERBREAD)

	    @SuppressLint("NewApi")
		
		@SuppressWarnings("deprecation")
		public String getResultForHttpGet(String mobile,String pwd) throws ClientProtocolException, IOException{
	       
			StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

	       StrictMode.setThreadPolicy(policy);
			

	       String path="http://mountainfile.applinzi.com/returnfileinfo.php";
	       String uri=path+"?filename="+mobile+"&request="+"flow";

	      
	       String result="";
	      
	       HttpGet httpGet=new HttpGet(uri);
	       HttpClient httpclient = new DefaultHttpClient();
	       HttpResponse httpResponse = httpclient.execute(httpGet);
	       Log.d("tag", "succes");
	       if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
	       {
	        result = EntityUtils.toString(httpResponse.getEntity());
	       }
	       else
	       {
	        result="error";
	       }
					
			
	       return result;
	}
	
	
	
}
