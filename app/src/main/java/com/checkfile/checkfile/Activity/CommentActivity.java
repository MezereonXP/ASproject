package com.checkfile.checkfile.Activity;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.checkfile.checkfile.*;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CommentActivity extends Activity {

	private Button commitButton;
	private EditText comment;
	private LinearLayout commentLayout;
	private String filename;
	private MyApp myapp;
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
		setContentView(R.layout.activity_comment);
		Intent intent=getIntent();
		filename=intent.getStringExtra("filename");
		commitButton=(Button) findViewById(R.id.commitButton);
		comment=(EditText) findViewById(R.id.comment);
		commentLayout=(LinearLayout) findViewById(R.id.commentLayout);
		controlKeyboardLayout(commentLayout, commitButton);
		comment.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
				if(s.length()==0){
					commitButton.setEnabled(false);
					commitButton.setTextColor(0xffaaa);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				if(s==null){
					commitButton.setEnabled(false);
					commitButton.setTextColor(0xffaaa);
				}
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				Log.d("tag",comment.getText().toString() );
				if(comment.getText().toString()!=null){
					commitButton.setEnabled(true);
					commitButton.setTextColor(0xff000000);
				}if(s.length()==0){
					commitButton.setEnabled(false);
					commitButton.setTextColor(0xffADADAD);
				}
			}
		});
		
		commitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					Intent intent1=new Intent();
					intent1.setClass(CommentActivity.this, FileActivity.class);
					Log.d("tag",filename+"+"+myapp.myState );
					Log.d("result", getResultForHttpGet(filename+"!"+myapp.myState,""));
					startActivity(intent1);
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
	
	 private void controlKeyboardLayout(final View root, final View scrollToView) {
	        	        root.getViewTreeObserver().addOnGlobalLayoutListener(
	                new OnGlobalLayoutListener() {
	                    @Override
	                    public void onGlobalLayout() {
	                        Rect rect = new Rect();
	                        root.getWindowVisibleDisplayFrame(rect);
	                        int rootInvisibleHeight = root.getRootView()
	                                .getHeight() - rect.bottom;
	                        Log.i("tag", "" + root.getRootView().getHeight());
	                        if (rootInvisibleHeight > 100) {
	                            //����̵�������ʱ��
	                        	comment.setBackgroundResource(R.drawable.shape3);
	                            int[] location = new int[2];
	                            scrollToView.getLocationInWindow(location);
	                            int srollHeight = (location[1] + scrollToView
	                                    .getHeight()) - rect.bottom;
	                            root.scrollTo(0, srollHeight);
	                        } else {
	                        	comment.setBackgroundResource(R.drawable.shape4);
	                            root.scrollTo(0, 0);
	                        }
	                    }
	                });
	    }
	 
	 
	 @TargetApi(VERSION_CODES.GINGERBREAD)

	    @SuppressLint("NewApi")
		
		@SuppressWarnings("deprecation")
		public String getResultForHttpGet(String mobile,String pwd) throws ClientProtocolException, IOException{
	       
			StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

	       StrictMode.setThreadPolicy(policy);
			

	       String path="http://mountainfile.applinzi.com/getresult.php";
	       String uri=path+"?result="+mobile;

	      
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
