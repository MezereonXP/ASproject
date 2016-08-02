package com.checkfile.checkfile.Adapter;

import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.checkfile.checkfile.*;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyAdapter extends BaseAdapter{

    private final int TYPE_HAVEBUTTON   = 0;
    private final int TYPE_NOBUTTON  = 1;
    private List<Map<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
    private MyApp myapp;
	final Zujian zujian=new Zujian(); 
	Zujian2 zujian2=null;

    public MyAdapter(Context context,List<Map<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }  
    
    

    public final class Zujian{  
        public ImageView image;  
        public EditText  hint;  
        public Button view;  
    }  
    public final class Zujian2{  
        public ImageView image;  
        public EditText  hint;  
    }  
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	

	
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub             
        int p = position%4;
        if(p == 0)
            return TYPE_HAVEBUTTON;
        else if(p == 1)
            return TYPE_NOBUTTON;
        else 
            return 0;
    }
	

    
	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		int type = getItemViewType(position);
     if(convertView==null){  
    	 Log.e("convertView = ", " NULL");
        if(type==0) {
            convertView=layoutInflater.inflate(R.layout.item, null);  
            zujian.image=(ImageView)convertView.findViewById(R.id.image);  
            zujian.hint=(EditText)convertView.findViewById(R.id.phone);  
            zujian.view=(Button)convertView.findViewById(R.id.sendm_button);
            convertView.setTag(zujian);
            
            final CountDownTimer timer = new CountDownTimer(10000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    zujian.view.setText(millisUntilFinished/1000 + "��");
                }

                @Override
                public void onFinish() {
                    zujian.view.setEnabled(true);
                    zujian.view.setText("������֤��");
                }
            };
            
            zujian.view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
		            v.setEnabled(false);
		            timer.start();
				}
			});
            zujian.hint.addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
					myapp.myState=s.toString();
				}
			});
            
            
            Log.e("convertView = ", "NULL TYPE_HAVEBUTTON"); 
        }
		if(type==1){
			zujian2=new Zujian2();
            //��������ʵ�������  
            convertView=layoutInflater.inflate(R.layout.item2, null);  
            zujian2.image=(ImageView)convertView.findViewById(R.id.image1);  
            zujian2.hint=(EditText)convertView.findViewById(R.id.code);   
            convertView.setTag(zujian2); 
            Log.e("convertView = ", "NULL TYPE_NOBUTTON"); 
		}
       


        }else{
//                 if(type==0){
//                	 zujian=(Zujian)convertView.getTag();  
//                	 Log.e("convertView !!!!!!= ", "NULL TYPE_HAVEBUTTON");
//        }
                 if(type==1){
                	 zujian2=(Zujian2)convertView.getTag();  
                	 Log.e("convertView !!!!!!= ", "NULL TYPE_NOBUTTON");
       
       }
        	
        }
   if(type==0){
         zujian.image.setBackgroundResource((Integer)data.get(position).get("image"));  
         zujian.hint.setHint((String)data.get(position).get("text")); 
         zujian.view.setText((String)data.get(position).get("buttontext"));
   }
   if(type==1){
         zujian2.image.setBackgroundResource((Integer)data.get(position).get("image"));  
         zujian2.hint.setHint((String)data.get(position).get("text"));  
     
     }
   
   

        return convertView;  
	}
    }

