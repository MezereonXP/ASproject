package com.checkfile.checkfile.Activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;

import com.checkfile.checkfile.R;
import com.checkfile.checkfile.SysApplication;

import uk.co.senab.photoview.PhotoView;

public class FileCheckingActivity extends Activity {

	
	private ScrollView fileScorllView;
	private uk.co.senab.photoview.PhotoView  fileview;
	private Button sayyesButton;
	private Button checktheroutineButton;
	private String filename;
	private JSONObject jsonobject;
	private JSONArray jsonarray;
	private String  pictureResult;
	private Bitmap bitmap;
	private Bitmap bitmap2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SysApplication.getInstance().addActivity(this);
		if(VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //͸��״̬��
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //͸��������
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_filechecking);
		Intent intent=getIntent();
		filename=intent.getStringExtra("filename");
//		Log.d("filename", filename);
		fileScorllView=(ScrollView) findViewById(R.id.scrollView1);
		sayyesButton=(Button) findViewById(R.id.sayyes);
		checktheroutineButton=(Button) findViewById(R.id.checktheroutine);
		fileview=(PhotoView) findViewById(R.id.file_picture);
		
		try {
			pictureResult=getResultForHttpGet(filename, "");
			Log.d("result", pictureResult);
			jsonobject=new JSONObject(pictureResult);
			jsonarray=jsonobject.getJSONArray("data");
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			
			if(jsonarray.length()>1){
			for(int i=0;i<jsonarray.length()-1;i++){
				if(i==0){
					bitmap=getHttpBitmap(jsonarray.getJSONObject(i).getString("address"));
				}
				 bitmap2=getHttpBitmap(jsonarray.getJSONObject(i+1).getString("address"));
				bitmap=toConformBitmap(bitmap, bitmap2);
			
			}
			fileview.setImageBitmap(bitmap);
			}else{
				Bitmap bitmap=getHttpBitmap(jsonarray.getJSONObject(0).getString("address"));
				fileview.setImageBitmap(bitmap);
			
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		checktheroutineButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("filename", filename);
				intent.setClass(FileCheckingActivity.this, RoutineActivity.class);
				startActivity(intent);
			}
		});
		sayyesButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				intent.putExtra("filename", filename);
				intent.setClass(FileCheckingActivity.this, CommentActivity.class);
				startActivity(intent);
			}
		});
		
	}
	
	 public static Bitmap getHttpBitmap(String url){
		  URL myFileURL;
		  Bitmap bitmap=null;
		  try{
		   myFileURL = new URL(url);
		   //�������
		   HttpURLConnection conn=(HttpURLConnection)myFileURL.openConnection();
		   conn.setConnectTimeout(6000);
		   conn.setDoInput(true);
		   conn.setUseCaches(false);
		   InputStream is = conn.getInputStream();
		   bitmap = BitmapFactory.decodeStream(is);
		   is.close();
		  }catch(Exception e){
		   e.printStackTrace();
		  }
		  return bitmap;
		 }
	 
	 @TargetApi(VERSION_CODES.GINGERBREAD)

	    @SuppressLint("NewApi")
		
		@SuppressWarnings("deprecation")
		public String getResultForHttpGet(String mobile,String pwd) throws ClientProtocolException, IOException{
	       
			StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

	       StrictMode.setThreadPolicy(policy);

	       String path="http://mountainfile.applinzi.com/returnfileinfo.php";
	       String uri=path+"?filename="+mobile+"&request="+"info";
	      
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
	 
	 public static  Bitmap toConformBitmap(Bitmap background, Bitmap foreground) {
	        if( background == null ) {  
	           return null;  
	        }  
	 
	        int bgWidth = background.getWidth();  
	        int bgHeight = background.getHeight();  
	        int fgWidth = foreground.getWidth();  
	        int fgHeight = foreground.getHeight();  
	        //create the new blank bitmap
	        if(bgWidth>=fgWidth){
	        	 Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight+fgHeight, Config.ARGB_8888); 
	        
	       
	        Canvas cv = new Canvas(newbmp);  
	        //draw bg into  
	        cv.drawBitmap(background, 0, 0, null);
	        //draw fg into  
	        cv.drawBitmap(foreground, 0, bgHeight, null);
	        //save all clip  
	        cv.save(Canvas.ALL_SAVE_FLAG);
	        //store  
	        cv.restore();
	        return newbmp;  
	        }else{
	        	Bitmap newbmp = Bitmap.createBitmap(fgWidth, bgHeight+fgHeight, Config.ARGB_8888); 
		        Canvas cv = new Canvas(newbmp);  
		        //draw bg into  
		        cv.drawBitmap(background, 0, 0, null);
		        //draw fg into  
		        cv.drawBitmap(foreground, 0, bgHeight, null);
		        //save all clip  
		        cv.save(Canvas.ALL_SAVE_FLAG);
		        //store  
		        cv.restore();
		        return newbmp;  
	        }
	   }
	
}
