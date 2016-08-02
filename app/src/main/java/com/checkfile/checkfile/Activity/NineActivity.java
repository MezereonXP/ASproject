package com.checkfile.checkfile.Activity;



import com.checkfile.checkfile.MyApp;
import com.checkfile.checkfile.R;
import com.checkfile.checkfile.SysApplication;
import com.checkfile.checkfile.View.CustomTitle3;
import com.checkfile.checkfile.View.LocusPassWordView;
 import com.checkfile.checkfile.View.MyDialog;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.TextView;

public class NineActivity extends Activity {

	
	private LocusPassWordView lv;
	private Intent intent;
	private String name;
	private View[] mPreviewViews =  new View[9];
	private TextView tipsTV;
	private Context ctx=NineActivity.this;
	private MyApp myapp;
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        CustomTitle3.getCustomTitle(this, "绘制手势密码");
        setContentView(R.layout.activity_nine);
        Intent intent1=getIntent();
        name=intent1.getStringExtra("userName");
        SysApplication.getInstance().addActivity(this);
        initPreviewViews();
        tipsTV=(TextView) findViewById(R.id.tipstext);
        lv=(LocusPassWordView) findViewById(R.id.lpwv);
        lv.setPasswordMinLength(2);
        lv.setOnCompleteListener(new LocusPassWordView.OnCompleteListener() {
			
			@Override
			public void onComplete(String password) {
				Log.e("tag", password);
				lv.resetPassWord(password);
				lv.clearPassword();
				 SharedPreferences hp = ctx.getSharedPreferences("HandPassword", MODE_PRIVATE); 
			     Editor editor = hp.edit();
		        if(!(hp.getString("STRING_KEY", "none").equals("none"))){
                if(password.equals(hp.getString("STRING_KEY", "none"))){
                	intent=new Intent(NineActivity.this, FileActivity.class);
                	if(hp.getString("PHONENUM", "none").equals("none")){
                    	editor.putString("PHONENUM",myapp.myState);
                    	editor.putString("USERNAME", name);
                    	editor.commit();
                    	Log.d("phone", hp.getString("PHONENUM", "none"));
                	}

                	startActivity(intent);
                }else{
                   dialog();
                	 
                	 
                }
                
		        }
		        
		        
		        if(hp.getString("STRING_KEY", "none").equals("none")&&password.length()>3){
				updatePreviewViews(password);
		        editor.putString("STRING_KEY", password);
		        editor.putInt("INT_KEY", 0);
		        editor.commit();
		        tipsTV.setText("请绘制手势密码");
		        lv.setPasswordMinLength(0);
		        } 
		        if(password.length()<=3&&hp.getString("STRING_KEY", "none").equals("none")){
		        	MyDialog dialog = new MyDialog(NineActivity.this);
		        	dialog.setText("密码长度应大于2");
		        	dialog.show();

		        }


			}
		});
        
}

protected void dialog() {
  MyDialog dialog = new MyDialog(NineActivity.this);
  dialog.show();
  

 }


public void onBackPressed() {  
    super.onBackPressed(); 
//    hp.edit().clear().commit();
	 SharedPreferences hp = ctx.getSharedPreferences("HandPassword", MODE_PRIVATE); 
     Editor editor = hp.edit();
     hp.edit().clear().commit();
	intent=new Intent(NineActivity.this, MainActivity.class);
	myapp.myState="";
	startActivity(intent);
}  

private void initPreviewViews() {
	mPreviewViews =  new View[9];
	mPreviewViews[0] = (ImageView) findViewById(R.id.gesturepwd_setting_preview_0);
	mPreviewViews[1] = (ImageView) findViewById(R.id.gesturepwd_setting_preview_1);
	mPreviewViews[2] = (ImageView) findViewById(R.id.gesturepwd_setting_preview_2);
	mPreviewViews[3] = (ImageView) findViewById(R.id.gesturepwd_setting_preview_3);
	mPreviewViews[4] = (ImageView) findViewById(R.id.gesturepwd_setting_preview_4);
	mPreviewViews[5] = (ImageView) findViewById(R.id.gesturepwd_setting_preview_5);
	mPreviewViews[6] = (ImageView) findViewById(R.id.gesturepwd_setting_preview_6);
	mPreviewViews[7] = (ImageView) findViewById(R.id.gesturepwd_setting_preview_7);
	mPreviewViews[8] = (ImageView) findViewById(R.id.gesturepwd_setting_preview_8);
}
private void updatePreviewViews(String password) {

	if (password == null)
		return;
	char []temp=password.toCharArray();
	for(int i=0;i<temp.length;i++){
		if(temp[i]!=','){
			int tempNum=temp[i]-'0';
			Log.d("tag", "here");
			mPreviewViews[tempNum].setBackgroundResource(R.mipmap.touch);
		}
	}

	

}

}