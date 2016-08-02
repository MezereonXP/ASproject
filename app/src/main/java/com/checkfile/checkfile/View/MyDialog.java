package com.checkfile.checkfile.View;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class  MyDialog extends Dialog {
	 private int FLAG_DISMISS = 1;
	 private boolean flag = true;
	 
	 public MyDialog(Context context) {
	     super(context);
	     setTitle("绘制错误");
	 }

	 public void setText(String text){
		 setTitle(text);
	 }
	 
	@Override
	 public void show() {
	     super.show();
	     mThread.start();
	 }

	@Override
	 public void dismiss() {
	     super.dismiss();
	     flag = false;
	 }

	private Thread mThread = new Thread(){
	     @Override
	     public void run() {
	  super.run();
	  while(flag){
	      try {
	   Thread.sleep(2000);
	   Message msg = mHandler.obtainMessage();
	   msg.what = FLAG_DISMISS;
	   mHandler.sendMessage(msg);
	      } catch (InterruptedException e) {
	   e.printStackTrace();
	      }
	  }
	     }   
	 };
	 
	 private Handler mHandler = new Handler(){
	     @Override
	     public void handleMessage(Message msg) {
	  super.handleMessage(msg);
	  if(msg.what == FLAG_DISMISS)
	      dismiss();
	     }
	     
	 };
	 
	    }