package com.checkfile.checkfile;

import java.util.LinkedList;   
import java.util.List;   
import android.app.Activity;   
import android.app.Application;   
/** 
 * һ���� �����������к�̨activity 
 * @author Administrator 
 * 
 */  
public class SysApplication extends Application {  
    //����list��������ÿһ��activity�ǹؼ�  
    private List<Activity> mList = new LinkedList<Activity>();  
    //Ϊ��ʵ��ÿ��ʹ�ø���ʱ�������µĶ���������ľ�̬����  
    private static SysApplication instance;   
    //���췽��  
    private SysApplication(){}  
    //ʵ����һ��  
    public synchronized static SysApplication getInstance(){   
        if (null == instance) {   
            instance = new SysApplication();   
        }   
        return instance;   
    }   
    // add Activity    
    public void addActivity(Activity activity) {   
        mList.add(activity);   
    }   
    //�ر�ÿһ��list�ڵ�activity  
    public void exit() {   
        try {   
            for (Activity activity:mList) {   
                if (activity != null)   
                    activity.finish();   
            }   
        } catch (Exception e) {   
            e.printStackTrace();   
        } finally {   
            System.exit(0);   
        }   
    }   
    //ɱ����  
    public void onLowMemory() {   
        super.onLowMemory();       
        System.gc();   
    }    
}  
