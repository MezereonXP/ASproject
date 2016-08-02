package com.checkfile.checkfile.Fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.checkfile.checkfile.Activity.FileCheckingActivity;
import com.checkfile.checkfile.Adapter.FileItemAdapter;
import com.checkfile.checkfile.MyApp;
import com.checkfile.checkfile.R;


public class yesFragment extends Fragment {

	private ListView yesListView;
	private MyApp myapp;
	private JSONObject jsonobject;
	private JSONArray jsonarray;
	private List<Map<String, Object>> list;
	private String result;
	private SwipeRefreshLayout swipeRefreshLayout;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragement_yes, container, false);
		swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.yesFragmentLayout);
		swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
		yesListView=(ListView) v.findViewById(R.id.yesListView);
		 SharedPreferences hp = getActivity().getSharedPreferences("FILE", 0); 
	     Editor editor = hp.edit();
//	     yesListView.setMode(Mode.PULL_FROM_START);
	  if(hp.getString("yes", "none").equals("none")){
		try {
			result=getResultForHttpGet(myapp.myState, "");
			editor.putString("yes", result);
			editor.commit();
			Log.d("result", result);
			try {
				jsonobject=new JSONObject(result);
				jsonarray=jsonobject.getJSONArray("data");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	
		try {
			list = getData();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	  }else{
		  result=hp.getString("yes", "none");
		  try {
			jsonobject=new JSONObject(result);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			try {
				jsonarray=jsonobject.getJSONArray("data");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				list = getData();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	  }
        yesListView.setAdapter(new FileItemAdapter(getActivity(), list));
        yesListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				try {
					if(!(jsonarray.getJSONObject(0).getString("date").equals("null"))){
					Intent intent=new Intent();
					Log.d("list", ""+list.size());
					Map<String,String> map=(Map<String, String>)yesListView.getAdapter().getItem(arg2);
					intent.putExtra("filename", map.get("file_text"));
					intent.setClass(getActivity(), FileCheckingActivity.class);
					startActivity(intent);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
        
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				SharedPreferences hp = getActivity().getSharedPreferences("YESFILE", 0);
				Editor editor = hp.edit();
				List<Map<String, Object>> list1;
				try {
					result=getResultForHttpGet(myapp.myState, "");
					editor.putString("yes", result);
					editor.commit();
					Log.d("result", result);
					try {
						jsonobject=new JSONObject(result);

						jsonarray=jsonobject.getJSONArray("data");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}


				try {

					list1 = getData();
					yesListView.setAdapter(new FileItemAdapter(getActivity(), list1));
					Log.d("tag", "refreshing");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

//				new FinishRefresh().execute();
				swipeRefreshLayout.setRefreshing(false);
			}
		});
        
        
		return v;
		
		
	}


	private List<Map<String, Object>> getData() throws JSONException {
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();  
		if(!(jsonarray.getJSONObject(0).getString("date").equals("null"))){
			for (int i = 0; i < jsonarray.length(); i++) {  
	            Map<String, Object> map=new HashMap<String, Object>();  
	            	
	            	map.put("file_imageView", R.mipmap.paperdocument);
	            	map.put("file_text", jsonarray.getJSONObject(i).getString("fileName"));  
	            	map.put("time", jsonarray.getJSONObject(i).getString("date"));
	               list.add(map);  
	        }  
		}else{
			for (int i = 0; i < 1; i++) {  
	            Map<String, Object> map=new HashMap<String, Object>();  
	            	
	            	map.put("file_imageView", R.mipmap.ic_launcher);
	            	map.put("file_text", "暂无文件");
	            	map.put("time", "0000-00-00");
	               list.add(map);  
	        }  
		}
        return list;  
	}
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)

    @SuppressLint("NewApi")
	
	@SuppressWarnings("deprecation")
	public String getResultForHttpGet(String mobile,String pwd) throws ClientProtocolException, IOException{
       
		StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();

       StrictMode.setThreadPolicy(policy);
		
		//������  ����������Ŀ  ��servlet����
       String path="http://mountainfile.applinzi.com/returnfile.php";
       String uri=path+"?mobile="+mobile+"&request="+"1";
       //name:�������˵��û�����pwd:�������˵�����
       //ע���ַ�������ʱ���ܴ��ո�
      
       String result="";
      
       HttpGet httpGet=new HttpGet(uri);//���߰�����HttpPost�������ڣ������ǽ������ڵ�ַ�д���
       HttpClient httpclient = new DefaultHttpClient();
       HttpResponse httpResponse = httpclient.execute(httpGet);
       Log.d("tag", "succes");
       if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK)
       {
        //ȡ�÷��ص��ַ���
        result = EntityUtils.toString(httpResponse.getEntity());
       }
       else
       {
        result="error";
       }
				
		
       return result;
}
//	private class FinishRefresh extends AsyncTask<Void, Void, Void> {
//	    @Override
//	    protected Void doInBackground(Void... params) {
//	      try {
//	        Thread.sleep(1000);
//	      } catch (InterruptedException e) {
//
//	      }
//	      return null;
//	    }
//
//	    @Override
//	    protected void onPostExecute(Void aVoid) {
//	      yesListView.onRefreshComplete();
////	      ((BaseAdapter) yesListView.getAdapter()).notifyDataSetChanged();
//	    }
//	  }
	
}