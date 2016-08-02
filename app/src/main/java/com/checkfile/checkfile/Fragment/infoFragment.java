package com.checkfile.checkfile.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.checkfile.checkfile.Activity.SettingActivity;
import com.checkfile.checkfile.R;
import com.checkfile.checkfile.View.HeaderListView;
import com.checkfile.checkfile.View.PullToZoomListView;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


public class infoFragment extends Fragment {


	 private Context ctx;
	 private ImageView bgiv;
	 private ListView info_list;
	 private View v;
	 private PullToZoomListView listView;
	 private String[] adapterData; 
	 private String name;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


	}	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		 v = inflater.inflate(R.layout.fragment_info, container, false);
			listView = (PullToZoomListView)v.findViewById(R.id.listview);
			 SharedPreferences hp = getActivity().getSharedPreferences("HandPassword", 0); 
		     Editor editor = hp.edit();
		     name=hp.getString("USERNAME", "none");
			
			
	        adapterData = new String[] { name,"设置"};
	        listView.setAdapter(new ArrayAdapter<String>(getActivity(), 
	                android.R.layout.simple_list_item_1, adapterData));
	        listView.getHeaderView().setImageResource(R.mipmap.bgpicture);
	        listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
	        listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					// TODO Auto-generated method stub
					switch(arg2){
					case 1:
						
						break;
					case 2:
						Intent intent;
						intent =new Intent(getActivity(), SettingActivity.class);
						startActivity(intent);
						break;
					}
				}
			});
		return v; 
	}
	

	
//		public void onClick(View v) {
//			// TODO Auto-generated method stub
//			Log.e("tag", "click!");
//			Intent intent;
//			ctx=getActivity();
//			 SharedPreferences hp = ctx.getSharedPreferences("HandPassword", 0); 
//		     Editor editor = hp.edit();
//		     hp.edit().clear().commit();
//			 intent=new Intent(getActivity(), MainActivity.class);
//			startActivity(intent);
//
//		}
	
	

	
}