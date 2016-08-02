package com.checkfile.checkfile.Activity;

import com.checkfile.checkfile.MyApp;
import com.checkfile.checkfile.R;
import com.checkfile.checkfile.SysApplication;
import com.checkfile.checkfile.View.CustomTitle2;

import android.R.color;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;
import android.widget.TextView;
import com.checkfile.checkfile.Fragment.*;
public class FileActivity extends Activity {

	
	private RadioGroup rg ;
    private RadioButton yesRb;
    private RadioButton noRb;
    private RadioButton infoRb;
	private TextView title;
	 private infoFragment infoFragment;
	 private yesFragment yesFragment;
	 private noFragment noFragment;
	 private MyApp myapp;
	 private FragmentTransaction transaction;
	 
	 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
		CustomTitle2.getCustomTitle(this, "个人信息");
		
//		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); 
		setContentView(R.layout.activity_file);
		
		SysApplication.getInstance().addActivity(this);
//		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title);
		
		rg= (RadioGroup) findViewById(R.id.group);
        yesRb = (RadioButton) findViewById(R.id.bottom_tabbar_rb_1);
        noRb = (RadioButton) findViewById(R.id.bottom_tabbar_rb_2);
        infoRb = (RadioButton) findViewById(R.id.bottom_tabbar_rb_3);
//		title=(TextView) findViewById(R.id.title);
		 
		
		SharedPreferences hp = this.getSharedPreferences("HandPassword", MODE_PRIVATE); 
	    Editor editor = hp.edit();
		myapp.myState= hp.getString("PHONENUM", "none");
		Log.d("phone", myapp.myState);
		
		FragmentManager fragmentManager = getFragmentManager(); 
		transaction = fragmentManager.beginTransaction();
		CustomTitle2.setText("个人信息");

		if (null == infoFragment) {
			infoFragment = new infoFragment();
		}
		transaction.add(R.id.mHomeContent,
				infoFragment);
		// Commit the transaction
		transaction.commit();
		
		
		
		
		
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
	            switch (checkedId) {
                case R.id.bottom_tabbar_rb_1:
                	CustomTitle2.setText("已批阅");
//                     yesRb.setBackgroundResource(drawable.readed_selected);
//                     noRb.setBackgroundResource(drawable.unreaded_normal);
//                     infoRb.setBackgroundResource(drawable.information_normal);
                   yesFragment = new yesFragment();  
                   FragmentTransaction transaction = getFragmentManager().beginTransaction();  
                   // Replace whatever is in the fragment_container view with this fragment,  
                   // and add the transaction to the back stack  
                   transaction.replace(R.id.mHomeContent, yesFragment);  
                   transaction.addToBackStack(null);  
                   // Commit the transaction  
                   transaction.commit();
                    break;
                case R.id.bottom_tabbar_rb_2:
                	CustomTitle2.setText("未批阅");
//                    yesRb.setBackgroundResource(drawable.readed_normal);
//                    noRb.setBackgroundResource(drawable.unreaded_selected);
//                    infoRb.setBackgroundResource(drawable.information_normal);
                    noFragment = new noFragment();  
                    FragmentTransaction transaction2 = getFragmentManager().beginTransaction();  
                    // Replace whatever is in the fragment_container view with this fragment,  
                    // and add the transaction to the back stack  
                    transaction2.replace(R.id.mHomeContent, noFragment);  
                    transaction2.addToBackStack(null);  
                    // Commit the transaction  
                    transaction2.commit();
                    break;
                case R.id.bottom_tabbar_rb_3:
                	CustomTitle2.setText("个人信息");
//                    yesRb.setBackgroundResource(drawable.readed_normal);
//                    noRb.setBackgroundResource(drawable.unreaded_normal);
//                    infoRb.setBackgroundResource(drawable.information_selected);
                    infoFragment = new infoFragment();  
                    FragmentTransaction transaction3 = getFragmentManager().beginTransaction();  
                    // Replace whatever is in the fragment_container view with this fragment,  
                    // and add the transaction to the back stack  
                    transaction3.replace(R.id.mHomeContent, infoFragment);  
                    transaction3.addToBackStack(null);  
                    // Commit the transaction  
                    transaction3.commit();
                    break;
                }
	            
	          
	            
			}
		});
	}
	
	public void onBackPressed() {  
		SysApplication.getInstance().exit();  
	}

}
