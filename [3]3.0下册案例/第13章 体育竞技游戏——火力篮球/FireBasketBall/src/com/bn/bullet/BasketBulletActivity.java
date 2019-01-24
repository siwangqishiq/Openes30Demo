package com.bn.bullet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import com.bn.bullet.GL2JNIView;
import com.bn.bullet7111.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class BasketBulletActivity extends Activity {
	GL2JNIView mView;
	// ��Ļ��Ӧ�Ŀ�Ⱥ͸߶�
	static float WIDTH;
	static float HEIGHT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ������
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// ȥ����ͷ
		// this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//ǿ�ƺ���
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// ǿ������
		// ���ϵͳ�Ŀ���Լ��߶�
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (dm.widthPixels > dm.heightPixels) {
			WIDTH = dm.heightPixels;
			HEIGHT = dm.widthPixels;
		} else {
			WIDTH = dm.widthPixels;
			HEIGHT = dm.heightPixels;
		}
		mView = new GL2JNIView(this);
		GL2JNIView.hba = this;
		mView.requestFocus(); // ��ȡ����
		mView.setFocusableInTouchMode(true); // ����Ϊ�ɴ���
		setContentView(mView);

		JNIPort.sendWidthAndHeight(WIDTH, HEIGHT);
		JNIPort.nativeSetAssetManager(this.getAssets());// ��ʼ��

		chushihuaSounds();
		JNIPort.hba = this;
		
		//1�����ж�sdcard�Ƿ���أ�Ȼ���ڿ��Ͻ�����Ҫ���ļ��м��ļ�
		File sdcardDir = Environment.getExternalStorageDirectory();
		//��ȡSDCard·��
		String SDCardPath = sdcardDir.getPath();
		//����SDcard·��
		JNIPort.sendSDCardPath(SDCardPath);
		
		CopyAssetFileToSDCard cztsd = new CopyAssetFileToSDCard(this);
		try {
			cztsd.copy("obj/wang.obj", Constant.SDCardPath,"wang.obj");
			cztsd.copy("obj/lankuang.obj", Constant.SDCardPath,"lankuang.obj");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		JNIPort.setJNIEnv();
	}

	MediaPlayer beijingyinyue;// ��Ϸ�������ֲ�����
	SoundPool shengyinChi;// ������
	HashMap<Integer, Integer> soundIdMap;// ������������ID���Զ�������ID��Map

	// ���������ķ���
	@SuppressLint("UseSparseArrays")
	public void chushihuaSounds() {
		beijingyinyue = MediaPlayer.create(this, R.raw.beijingyingyu);
		beijingyinyue.setLooping(true);// �Ƿ�ѭ��
		beijingyinyue.setVolume(0.2f, 0.2f);// ������С
		shengyinChi = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		soundIdMap = new HashMap<Integer, Integer>();
		soundIdMap = new HashMap<Integer, Integer>();
		soundIdMap.put(1, shengyinChi.load(this, R.raw.pengzhuang, 1));// ��ײ����
		soundIdMap.put(2, shengyinChi.load(this, R.raw.levelend, 1));// ��Ϸʱ���������
		soundIdMap.put(3, shengyinChi.load(this, R.raw.shoot, 1));// ��������
	}

	// ���������ķ���
	public int shengyinBoFang(int sound, int loop) {
		AudioManager mgr = (AudioManager) this
				.getSystemService(Context.AUDIO_SERVICE);
		float streamVolumeCurrent = mgr
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		float streamVolumeMax = mgr
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		float volume = streamVolumeCurrent / streamVolumeMax;
		shengyinChi.play(soundIdMap.get(sound), volume, volume, 1, loop, 0.5f);

		return 0;
	}
    static void recordCurrViewIndex(int currViewIn)
    {
    	//��¼��ǰ��view���
    	Constant.VIEW_CUR_GL_INDEX = currViewIn;
    	System.out.println("currView = "+Constant.VIEW_CUR_GL_INDEX);
    }
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent e)
	{ 
		if(keyCode!= 4)
		{
			return false;
		}
		if(Constant.VIEW_CUR_GL_INDEX == Constant.VIEW_ABOUT_INDEX | Constant.VIEW_CUR_GL_INDEX == Constant.VIEW_GAME_INDEX
				| Constant.VIEW_CUR_GL_INDEX == Constant.VIEW_RECORD_INDEX | Constant.VIEW_CUR_GL_INDEX == Constant.VIEW_SET_INDEX 
				| Constant.MENU_CURR_MENUINDEX == Constant.MENU_GAMEVIEW_END
				){
			//��������ý���,���ڽ��棬��¼����,��������,��Ϸ��������
			//���ص��˵�����
			System.out.println("�ص��˵�����------");
			beijingyinyue.pause();
			JNIPort.toMenuView();
			return true;
		}
		if(Constant.VIEW_CUR_GL_INDEX == Constant.VIEW_MENU_INDEX){//�˵�����
			System.out.println("�˳�------");
			System.exit(0);
			return true;
		}
		return true;
	}	
	@Override
	protected void onPause() {
		super.onPause();
		mView.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mView.onResume();
	}
}