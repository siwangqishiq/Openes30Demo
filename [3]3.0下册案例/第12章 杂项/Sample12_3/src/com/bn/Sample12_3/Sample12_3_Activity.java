package com.bn.Sample12_3;
import com.bn.util.screenscale.ScreenScaleUtil;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

public class Sample12_3_Activity extends Activity {
	private MySurfaceView mGLSurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);         
        //����Ϊȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//����Ϊ����ģʽ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		DisplayMetrics dm=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        Constant.ssr=ScreenScaleUtil.calScale(dm.widthPixels, dm.heightPixels);
		
		//��ʼ��GLSurfaceView
        mGLSurfaceView = new MySurfaceView(this);
        setContentView(mGLSurfaceView);	
        mGLSurfaceView.requestFocus();//��ȡ����
        mGLSurfaceView.setFocusableInTouchMode(true);//����Ϊ�ɴ���  
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
        MySurfaceView.threadFlag=true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
        MySurfaceView.threadFlag=false;
    }    
}



