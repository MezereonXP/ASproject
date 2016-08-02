package com.checkfile.checkfile.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.checkfile.checkfile.MyApp;
import com.checkfile.checkfile.R;
import com.checkfile.checkfile.SysApplication;
import com.checkfile.checkfile.Adapter.MyAdapter;

public class MainActivity extends Activity {

	private EditText phone;
	private EditText code;
	private ListView login_listview;
	private Button login_button;
	private Button sendm_button;
	private Intent intent;
	private String result;
	private Context ctx=MainActivity.this;
	private MyApp myapp;
	private SharedPreferences hp;
	private Editor editor;
//	private TimeCount time = new TimeCount(60000, 1000);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		SysApplication.getInstance().addActivity(this);
		init();
		
		 hp = ctx.getSharedPreferences("HandPassword", MODE_PRIVATE); 
	     editor = hp.edit();
		if(!(hp.getString("STRING_KEY", "none").equals("none"))){
			intent=new Intent();
			intent.setClass(MainActivity.this, NineActivity2.class);
			startActivity(intent);
		}
		
		
        List<Map<String, Object>> list=getData();  
        login_listview.setAdapter(new MyAdapter(MainActivity.this, list));  
        
        
        
        login_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//			   if(phone==null){
//				   Log.d("tag", "phone is null");
//			   }
				Log.d("tag", myapp.myState);
				try {
					result=getResultForHttpGet(myapp.myState, "9");
					if(result.equals("0")){
						Log.d("tag", "wrong phonenumber");
						Toast.makeText(getApplicationContext(),"Wrong PhoneNumber",Toast.LENGTH_SHORT).show();
					}else{
						skipToNine();
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}

		});
        
 }
        
		
	
    public List<Map<String, Object>> getData(){  
        List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map=new HashMap<String, Object>();  
             if(i==0){
            	map.put("image", R.mipmap.phonenumber);
            	map.put("text", "请输入手机号码");
            	map.put("buttontext", "获取验证码");
             }else{
             	map.put("image", R.mipmap.password);
             	map.put("text", "请输入验证码");
             }
               list.add(map);  
        }  
        return list;  
    }  

     public void init(){
 		login_listview=(ListView) findViewById(R.id.login_listView);
 		login_button=(Button) findViewById(R.id.login_button);
 		phone=(EditText) findViewById(R.id.phone);
 		code=(EditText) findViewById(R.id.code);
     }
     

	public void skipToNine() {
			// TODO Auto-generated method stub
			intent=new Intent();
			intent.putExtra("userName", result);
			intent.setClass(MainActivity.this, NineActivity.class);
			startActivity(intent);
			
		}
	public void onBackPressed() {  
		SysApplication.getInstance().exit();  
	}
	
	
	 @TargetApi(VERSION_CODES.GINGERBREAD)

    @SuppressLint("NewApi")
	
	@SuppressWarnings("deprecation")
	public String getResultForHttpGet(String mobile,String pwd) throws ClientProtocolException, IOException{
       
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

       StrictMode.setThreadPolicy(policy);
		

       String path="http://mountainfile.applinzi.com/login.php";
       String uri=path+"?mobile="+mobile;

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
