package com.bn.bullet;
import com.bn.bullet.GL2JNIView;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class HelloBulletActivity extends Activity 
{
//    @Override
//    public void onCreate(Bundle savedInstanceState) 
//    {    
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.main);        
//        System.out.println(450);
//        for(int i=0;i<450;i++)
//        {
//        	System.out.print("##$"+JNIPort.calJNI()+","+(((i+1)%6==0)?"\n":""));  
//        }        
//    }
    GL2JNIView mView;
	//��Ļ��Ӧ�Ŀ�Ⱥ͸߶�
	static float WIDTH;
	static float HEIGHT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {    
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//ȥ������
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);//ȥ����ͷ
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//ǿ�ƺ���
		//���ϵͳ�Ŀ���Լ��߶�
        DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        if(dm.widthPixels>dm.heightPixels)
        {
        	WIDTH=dm.widthPixels;
        	HEIGHT=dm.heightPixels;
        }
        else
        {
        	WIDTH=dm.heightPixels;
        	HEIGHT=dm.widthPixels;
        }
        mView = new GL2JNIView(this);
		mView.requestFocus();					//��ȡ����
		mView.setFocusableInTouchMode(true); 	//����Ϊ�ɴ���
        setContentView(mView);
        
        JNIPort.nativeSetAssetManager(this.getAssets());//��ʼ��
    }
    
    @Override protected void onPause() {
        super.onPause();
        mView.onPause();
    }

    @Override protected void onResume() {
        super.onResume();
        mView.onResume();
    }
}