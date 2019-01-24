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

import static com.bn.bullet.HelloBulletActivity.*;

@SuppressLint("NewApi")
public class GL2JNIView extends GLSurfaceView
{
	//private final float TOUCH_SCALE_FACTOR = 180.0f/800;//�Ƕ����ű���
	float cx=0;//�����xλ��
	float cy=150;//�����yλ��
	float cz=400;//�����zλ��

    float mPreviousX;
    float mPreviousY;
    
    float xAngle=45;//�������X����ת�ĽǶ�
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
	@SuppressLint("ClickableViewAccessibility") @Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float x=event.getX();
		float y=event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				isAddBody = true;
				
//				System.out.println(WIDTH/2+" "+HEIGHT/2);
//				System.out.println(x+" "+y);
				
				if(y>HEIGHT/2)
				{
					if(x<WIDTH/3*1)
					{
//						System.out.println("�� ��ת");
						JNIPort.addBody(1);//�� ��ת
					}
					else if(x>WIDTH/3*2)
					{
//						System.out.println("�� ��ת");
						JNIPort.addBody(2);//�� ��ת
					}
					else
					{
//						System.out.println("�� ��");
						JNIPort.addBody(3);//�� ��
					}
				}
				else
				{
					if(x<WIDTH/3*1)
					{
//						System.out.println("ǰ��ת");
						JNIPort.addBody(4);//ǰ��ת
					}
					else if(x>WIDTH/3*2)
					{
//						System.out.println("ǰ ��ת");
						JNIPort.addBody(5);//ǰ ��ת
					}
					else
					{
//						System.out.println("ǰ��");
						JNIPort.addBody(6);//ǰ��
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				if(isAddBody)
				{

					isAddBody = false;
				}
				
				JNIPort.addBody(7);
				break;
			case MotionEvent.ACTION_MOVE:
	            
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