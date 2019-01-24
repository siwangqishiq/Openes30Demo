package com.bn.bnggdh;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.Activity;
import android.content.pm.ActivityInfo;

public class MyActivity extends Activity {
	private MySurfaceView mGLSurfaceView;
	static boolean threadFlag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//����Ϊ�ޱ����
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        		WindowManager.LayoutParams.FLAG_FULLSCREEN);//����Ϊ����״̬   ��д��仰�Ļ�������ʾ���ֻ�������״̬��
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//���ú���
        
        mGLSurfaceView = new MySurfaceView(this);
        setContentView(mGLSurfaceView);
        mGLSurfaceView.requestFocus();//��ȡ����
        mGLSurfaceView.setFocusableInTouchMode(true);//����Ϊ�ɴ���
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	threadFlag = true;
    	mGLSurfaceView.onResume();
    }
    
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	threadFlag = false;
    	mGLSurfaceView.onPause();
    }
    
}
