package com.bn.Sample1_4;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;


public class Sample1_4_Activity extends Activity {
	MySurfaceView mySurfaceView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //����Ϊȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//����Ϊ����ģʽ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		//��ʼ��GLSurfaceView
		mySurfaceView = new MySurfaceView(this);
		mySurfaceView.requestFocus();//��ȡ����
		mySurfaceView.setFocusableInTouchMode(true);//����Ϊ�ɴ���  
		//�л���������
		setContentView(mySurfaceView);		
    }
    @Override
    protected void onResume() {
        super.onResume();
        mySurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mySurfaceView.onPause();
    } 
    
    
    public boolean onKeyDown(int keyCode,KeyEvent e)
    {
    	switch(keyCode)
        	{
    	case 4:
    		System.exit(0);
    		break;
        	}
    	return true;
    };
}