package com.checkfile.checkfile.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.checkfile.checkfile.MyApp;
import com.checkfile.checkfile.R;
import com.checkfile.checkfile.SysApplication;
import com.checkfile.checkfile.View.CustomTitle;

public class SettingActivity extends Activity {
	
	private ListView settingList;
	private String[] adapterData;
	private Button  lgoutButton;
	private MyApp myapp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //͸��״̬��
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //͸��������
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
		SysApplication.getInstance().addActivity(this);
		CustomTitle.getCustomTitle(this, "设置");
		setContentView(R.layout.activity_setting);

		settingList=(ListView) findViewById(R.id.setting_list);
		lgoutButton=(Button) findViewById(R.id.lgout_button);
		adapterData = new String[] { "清除缓存","重新绘制密码"};
		settingList.setAdapter(new ArrayAdapter<String>(this, 
                android.R.layout.simple_list_item_1, adapterData));
		settingList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Integer num=new Integer(arg2);
				Log.e("tag", num.toString());
				switch(arg2){
				case 0:
					 Intent intent;
					 SharedPreferences hp = SettingActivity.this.getSharedPreferences("HandPassword",0); 
				     Editor editor = hp.edit();
				     hp.edit().clear().commit();
				     myapp.myState="";
					 intent=new Intent(SettingActivity.this, MainActivity.class);
					 startActivity(intent);
					break;
				case 1:
					 Intent intent2;
					 intent2=new Intent(SettingActivity.this, NineActivity.class);
					 hp = SettingActivity.this.getSharedPreferences("HandPassword",0); 
				     editor = hp.edit();
				     hp.edit().clear().commit();
					 startActivity(intent2);
					break;
				}
			}
		});
		lgoutButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent intent;
				 SharedPreferences hp = SettingActivity.this.getSharedPreferences("HandPassword",0); 
			     Editor editor = hp.edit();
			     hp.edit().clear().commit();
				  hp = SettingActivity.this.getSharedPreferences("FILE",0); 
			     editor = hp.edit();
			     hp.edit().clear().commit();
			     myapp.myState="";
				 intent=new Intent(SettingActivity.this, MainActivity.class);
				 startActivity(intent);
			}
		});
		
	}
}
