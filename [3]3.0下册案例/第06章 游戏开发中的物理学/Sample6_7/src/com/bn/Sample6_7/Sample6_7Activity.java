package com.bn.Sample6_7;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.Window;
import android.view.WindowManager;

public class Sample6_7Activity extends Activity {
	MysurfaceView mysurfaceView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //����Ϊȫ��
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN ,  
		              WindowManager.LayoutParams.FLAG_FULLSCREEN);
		//����Ϊ����ģʽ
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		mysurfaceView=new MysurfaceView(this);
		mysurfaceView.requestFocus();
		mysurfaceView.setFocusableInTouchMode(true);
		setContentView(mysurfaceView);
    }
	@Override
	protected void onPause() {
		super.onPause();
		mysurfaceView.onPause();
	}
    
}
