package com.checkfile.checkfile.View;



import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.checkfile.checkfile.R;

public class CustomTitle3 {

	private static Activity mActivity;
	private static TextView textView;

	public static void getCustomTitle(Activity activity, String title) {
		mActivity = activity;
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mActivity.setContentView(R.layout.title_nine_activity);
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.title_nine_activity);
		
	    textView = (TextView) activity.findViewById(R.id.head_center_text);  
        textView.setText(title);  
        Button titleBackBtn = (Button) activity.findViewById(R.id.TitleBackBtn);  
        titleBackBtn.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {  
            	Log.d("Title back","key down");
            	mActivity.onBackPressed();
            }  
        });  

	}
	
	public static void setText(String text){
		textView.setText(text);
	}
}
