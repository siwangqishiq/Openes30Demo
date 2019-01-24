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
import android.opengl.Matrix;
import android.view.MotionEvent;

import static com.bn.bullet.HelloBulletActivity.*;

@SuppressLint("NewApi")
public class GL2JNIView extends GLSurfaceView
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/800;//�Ƕ����ű���
	float cx=0;//�����xλ��
	float cy=150;//�����yλ��
	float cz=400;//�����zλ��

    float mPreviousX;
    float mPreviousY;
    
    float xAngle=0;//�������X����ת�ĽǶ�
    float yAngle=0;//̫���ƹ���y����ת�ĽǶ�
    
    boolean isAddBody;
	SceneRenderer mRenderer;
	public GL2JNIView(Context context)
	{
		super(context);
		this.setEGLContextClientVersion(3);
		mRenderer=new SceneRenderer();
		this.setRenderer(mRenderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
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
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x=event.getX();
		float y=event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				isAddBody = true;
				break;
			case MotionEvent.ACTION_UP:
				if(isAddBody)
				{
					if(x>WIDTH/2)
					{
						JNIPort.addBody(1);
					}
					else
					{
						JNIPort.addBody(2);
					}

					isAddBody = false;
				}
				break;
			case MotionEvent.ACTION_MOVE:


				float dx = x - mPreviousX;//���㴥�ر�Xλ�� 
				float dy = y - mPreviousY;//���㴥�ر�Yλ�� 
				if(!(dx<5&&dx>-5&&dy<5&&dy>-5))
				{				
					isAddBody = false;
					yAngle -= dx * TOUCH_SCALE_FACTOR;//����̫����y����ת�ĽǶ�
					xAngle += dy * TOUCH_SCALE_FACTOR;//����̫����y����ת�ĽǶ�
					
					xAngle = Math.max(xAngle, 10);
					xAngle = Math.min(xAngle, 89);
		            
					float []camera = new float[4];//���������λ������
					camera[0] = 0;
					camera[1] = 0;
					camera[2] = 30;
					camera[3] = 1;
					
					float []n = new float[4];//��λ������������ڶ�����ת����ת��
					n[0] = 0;
					n[1] = 0;
					n[2] = 1;
					n[3] = 1;
					
					float []setp1 = new float[16];	//��һ����ת����ת����
					Matrix.setIdentityM(setp1, 0);
					Matrix.rotateM(setp1,0,yAngle,0,1,0);
					
					float []setp2 = new float[16];
					Matrix.setIdentityM(setp2, 0);
					Matrix.rotateM(setp2,0,yAngle-90,0,1,0);//��ת����ת����
					
					Matrix.multiplyMV(camera, 0, setp1, 0, camera, 0);//���һ����ת������������
					Matrix.multiplyMV(n, 0, setp2, 0, n, 0);//��ڶ�����ת����ת��
					
					float []setp3 = new float[16];		//��ڶ�����ת����ת����
					Matrix.setIdentityM(setp3, 0);
					Matrix.rotateM(setp3,0,xAngle,n[0],n[1],n[2]);
					
					Matrix.multiplyMV(camera, 0, setp3, 0, camera, 0);//��ڶ�����ת������������
		            
		            JNIPort.setCamera(camera[0],camera[1],camera[2],0,1,0);
				}
	            
				break;
		}
		mPreviousX = x;
		mPreviousY = y;
		return true;
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
        		GLES30.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  	//����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  	//����ͼ��
        		0					  	//����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  	//������سɹ����ͷ�ͼƬ 
        return textureId;
	}
}