package com.checkfile.checkfile.Activity;

import com.checkfile.checkfile.MyApp;
import com.checkfile.checkfile.R;
import com.checkfile.checkfile.SysApplication;
import com.checkfile.checkfile.View.*;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

public class NineActivity2 extends Activity {

	
	private LocusPassWordView lv;
	private Context ctx=NineActivity2.this;
	private Intent intent;
	private MyApp myapp;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_nine2);
		SysApplication.getInstance().addActivity(this);
		lv=(LocusPassWordView) findViewById(R.id.lpwv);
		lv.setPasswordMinLength(0);
		lv.setOnCompleteListener(new LocusPassWordView.OnCompleteListener() {
			
			@Override
			public void onComplete(String password) {
				// TODO Auto-generated method stub
				 SharedPreferences hp = ctx.getSharedPreferences("HandPassword", MODE_PRIVATE); 
			     Editor editor = hp.edit();
	                if(password.equals(hp.getString("STRING_KEY", "none"))){
	                	myapp.myState=hp.getString("PHONENUM", "none");
//	                	Log.d("myapp", myapp.myState);
	                	intent=new Intent(NineActivity2.this, FileActivity.class);
	                	startActivity(intent);
	                }else{
	                	lv.clearPassword();
	                	dialog();
	                }
			}
		});
	
	}
	
	protected void dialog() {
		  MyDialog dialog = new MyDialog(NineActivity2.this);
		  dialog.show();
		  

		 }
	
	public void onBackPressed() {  
		SysApplication.getInstance().exit();  
	}

}
