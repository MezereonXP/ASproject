package com.checkfile.checkfile.View;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.checkfile.checkfile.R;

public class CustomTitle {

	private static Activity mActivity;
	private static TextView textView;

	public static void getCustomTitle(Activity activity, String title) {
		mActivity = activity;
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mActivity.setContentView(R.layout.custom_title);
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title);
		
	    textView = (TextView) activity.findViewById(R.id.head_center_text);  
        textView.setText(title);  
        Button titleBackBtn = (Button) activity.findViewById(R.id.TitleBackBtn);  
        titleBackBtn.setOnClickListener(new OnClickListener() {  
            public void onClick(View v) {  
            	Log.d("Title back","key down");
            	
            	mActivity.finish();
            }  
        });  
	}
	
	public static void setText(String text){
		textView.setText(text);
	}
}
