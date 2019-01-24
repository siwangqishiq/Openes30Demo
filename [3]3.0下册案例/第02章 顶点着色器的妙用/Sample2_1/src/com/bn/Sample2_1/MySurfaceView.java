package com.bn.Sample2_1;
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
class MySurfaceView extends GLSurfaceView 
{
    SceneRenderer mRenderer;//������Ⱦ��    
    int textureFlagId;//ϵͳ����Ĺ�������id
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
	float xAngle;//���峡����X����ת�ĽǶ�
	float yAngle;//���峡����Y����ת�ĽǶ�
	private float mPreviousX;//�ϴεĴ���λ��X����  
	private  float mPrevmiousY;//�ϴεĴ���λ��X����  
	
	//�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {  
        case MotionEvent.ACTION_MOVE:
            float dx = x - mPreviousX;//���㴥�ر�Xλ��
            float dy = y - mPrevmiousY;//���㴥�ر�Yλ��
            yAngle += dx * TOUCH_SCALE_FACTOR;//�������峡����Y����ת�Ƕ�     
            xAngle += dy * TOUCH_SCALE_FACTOR;//���������峡��X����ת�Ƕ�   
            requestRender();//�ػ滭��
        }
        mPreviousX = x;//��¼���ر�λ��
        mPrevmiousY = y;//��¼���ر�λ��
        return true;
    }
    
    @SuppressLint("NewApi")
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
        /**
         * 
         */
        setKeepScreenOn(true);//ͨ������View.setKeepScreenOn(boolean)�ķ���������������
    }

	class SceneRenderer implements GLSurfaceView.Renderer 
    {   
    	TextureRect texRect;//��ʾ������������
    	
        public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -1);
            MatrixState.rotate(yAngle,0,1,0);
            MatrixState.rotate(xAngle,1,0,0);
            //�����������
            texRect.drawSelf(textureFlagId);
            MatrixState.popMatrix();            
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 4, 100);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,0,5,0f,0f,0f,0f,1.0f,0.0f);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA--��ɫ
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);  
            //����������ζԶ��� 
            texRect=new TextureRect(MySurfaceView.this); 
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //��ʼ������
            textureFlagId=initTexture(R.drawable.android_flag);
            //�رձ������   
            GLES30.glDisable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
        }
    }
	
	public int initTexture(int drawableId)//textureId
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
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
        
        //ͨ������������ͼƬ===============begin===================
        InputStream is = this.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        return textureId;
	}
}
