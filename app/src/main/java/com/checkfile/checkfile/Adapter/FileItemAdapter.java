package com.checkfile.checkfile.Adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.CountDownTimer;
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

import com.checkfile.checkfile.R;

public class FileItemAdapter extends BaseAdapter{

    private List<Map<String, Object>> data;  
    private LayoutInflater layoutInflater;  
    private Context context;  
	
    public FileItemAdapter(Context context,List<Map<String, Object>> data){  
        this.context=context;  
        this.data=data;  
        this.layoutInflater=LayoutInflater.from(context);  
    }  
    
    

    public final class Zujian{  
        public ImageView image;  
        public TextView  text;  
        public TextView  timetext;
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

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		Zujian zujian=new Zujian(); 
	     if(convertView==null){  
	    	 Log.e("convertView = ", " NULL");
	            convertView=layoutInflater.inflate(R.layout.item_file, null);  
	            zujian.image=(ImageView)convertView.findViewById(R.id.file_imageView);
	            zujian.text=(TextView) convertView.findViewById(R.id.file_text);
	            zujian.timetext=(TextView) convertView.findViewById(R.id.timetext);
	            convertView.setTag(zujian);
	        }else{
              zujian=(Zujian)convertView.getTag();    	
	        }
	     
	     zujian.image.setBackgroundResource((Integer) data.get(position).get("file_imageView"));
		 zujian.text.setText((String)data.get(position).get("file_text"));
		 zujian.timetext.setText((String)data.get(position).get("time"));
		return convertView;
	}

}
