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
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.checkfile.checkfile.Activity.FileCheckingActivity;
import com.checkfile.checkfile.MyApp;
import com.checkfile.checkfile.R;
import com.checkfile.checkfile.Adapter.FileItemAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshAdapterViewBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class noFragment extends Fragment {

	private ListView noListView;
	private String[] noFileData;
	private String  result;
	private MyApp myapp;
	private JSONObject jsonobject;
	private JSONArray jsonarray;
	private List<Map<String, Object>> list;
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
		View v = inflater.inflate(R.layout.fragment_no, container, false);
		swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.noFragmentLayout);
		swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color_1, R.color.swipe_color_2, R.color.swipe_color_3, R.color.swipe_color_4);
		noListView=(ListView) v.findViewById(R.id.noListView);
		 SharedPreferences hp = getActivity().getSharedPreferences("FILE", 0); 
	     Editor editor = hp.edit();
	     if(hp.getString("no", "none").equals("none")){
			 try {
			result=getResultForHttpGet(myapp.myState, "");
			editor.putString("no", result);
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

			 list = getData();
		 }else{
	    	result=hp.getString("no", "none");
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
			 list = getData();


		 }
        noListView.setAdapter(new FileItemAdapter(getActivity(), list));  
		noListView.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				try {
					if(!(jsonarray.getJSONObject(0).getString("date").equals("null"))){
					Intent intent=new Intent();
					Map<String,String> map=(Map<String, String>)noListView.getAdapter().getItem(arg2);
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
				SharedPreferences hp = getActivity().getSharedPreferences("FILE", 0);
				Editor editor = hp.edit();
				List<Map<String, Object>> list1;
				try {
					result=getResultForHttpGet(myapp.myState, "");
					editor.putString("no", result);
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


				list1 = getData();
				noListView.setAdapter(new FileItemAdapter(getActivity(), list1));
				Log.d("tag", "refreshing");
				swipeRefreshLayout.setRefreshing(false);

//				new FinishRefresh().execute();

			}
		});




		return  v;

	}



	private List<Map<String, Object>>getData(){
		// TODO Auto-generated method stub
		List<Map<String, Object>> list=new ArrayList<Map<String,Object>>();
		try {
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
		} catch (JSONException e) {
			e.printStackTrace();
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
	       String uri=path+"?mobile="+mobile+"&request="+"0";
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
	
	
	


//
//private class FinishRefresh extends AsyncTask<Void, Void, Void> {
//    @Override
//    protected Void doInBackground(Void... params) {
//      try {
//        Thread.sleep(1000);
//      } catch (InterruptedException e) {
//
//      }
//      return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void aVoid) {
//     swipeRefreshLayout.onr.onRefreshComplete();
////      ((BaseAdapter) noListView.getAdapter()).notifyDataSetChanged();
//    }
//  }


}