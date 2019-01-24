package com.bn.bullet;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

@SuppressLint("NewApi")
public class GL2JNIView extends GLSurfaceView
{
    private SceneRenderer mRenderer;//������Ⱦ��	
	private float mPreviousY;//�ϴεĴ���λ��Y����
    private float mPreviousX;//�ϴεĴ���λ��X����
    
	public GL2JNIView(Context context)
	{
		super(context);
		this.setEGLContextClientVersion(3);
		mRenderer=new SceneRenderer();
		this.setRenderer(mRenderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
		JNIPort.setCamera(0,0);
	}
	
	//�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
    	float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) 
        {
           case MotionEvent.ACTION_MOVE:
	            float dy = y - mPreviousY;//���㴥�ر�Yλ��
	            float dx = x - mPreviousX;//���㴥�ر�Yλ��
	            
	            JNIPort.setCamera(dx,dy);
           break;
        }
        mPreviousY = y;//��¼���ر�λ��
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }
    
	private class SceneRenderer implements GLSurfaceView.Renderer
	{
		public void onDrawFrame(GL10 gl)
		{
			JNIPort.step();
		}
		public void onSurfaceChanged(GL10 gl, int width, int height)
		{
			JNIPort.onSurfaceChanged(width, height);
		}
		public void onSurfaceCreated(GL10 gl, EGLConfig config)
		{
			JNIPort.onSurfaceCreated(GL2JNIView.this);
		}
	}
	
	//��������ķ���
	public static int initTextureRepeat(GLSurfaceView gsv,String pname)
	{
		//��������ID
		int[] textures = new int[1];
		GLES30.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);    
		int textureId=textures[0];    
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
					
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);
		
           
        //ͨ������������ͼƬ===============begin===================
		InputStream is = null;
		try {
			is = gsv.getResources().getAssets().open(pname);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        Bitmap bitmapTmp;
        try {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally {
            try {
                is.close();
            } 
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //��������
        		0, 					  	//����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  	//����ͼ��
        		0					  	//����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  	//������سɹ����ͷ�ͼƬ 
        return textureId;
	}
}