package com.checkfile.checkfile.View;



import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.checkfile.checkfile.R;

public class CustomTitle2 {

	private static Activity mActivity;
	private static TextView textView;

	public static void getCustomTitle(Activity activity, String title) {
		mActivity = activity;
		mActivity.requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		mActivity.setContentView(R.layout.custom_title2);
		mActivity.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.custom_title2);
		
	    textView = (TextView) activity.findViewById(R.id.head_center_text);  
        textView.setText(title);  

	}
	
	public static void setText(String text){
		textView.setText(text);
	}
}
