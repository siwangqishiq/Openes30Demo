package com.bn.Sample4_7;

import static com.bn.Sample4_7.Constant.*;

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

public class MySurfaceView extends GLSurfaceView
{
	static float direction=0;//���߷���
    static float cx=0;//�����x���� 
    static float cz=30;//�����z����
    
    static float tx=0;//�۲�Ŀ���x����
    static float tz=0;//�۲�Ŀ���z����
    static final float DEGREE_SPAN=(float)(3.0/180.0f*Math.PI);//�����ÿ��ת���ĽǶ�
    //�߳�ѭ���ı�־λ  
    boolean flag=true;
    float Offset=30;
	SceneRenderer mRender;
	float preX;
	float preY;

	public MySurfaceView(Context context)
	{
		super(context);
		this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRender = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRender);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ 
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				preX=event.getX();
				preY=event.getY();
			break;
			case MotionEvent.ACTION_MOVE:
				float x=event.getX();
				float y=event.getY();
				float disX=x-preX;
				float disY=y-preY;
				cx-=disX*0.1f;
				cz-=disY*0.1f;	
				preX=x;
				preY=y;
			break;
		}
		tx=cx;//�۲�Ŀ���x���� 
        tz=cz-Offset;//�۲�Ŀ���z����
        //�����µ������λ��
        MatrixState.setCamera(cx,105,cz,tx,75,tz,0,1,0);
		return true;
	}
	
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		Mountion mountion;
		int ssTexId;//ɰʯ
		int lcpTexId;//�̲�Ƥ
		int dlTexId;//��·		
		int hcpTexId;//�Ʋ�Ƥ
		int rgbTexId;
		@Override
		public void onDrawFrame(GL10 gl)
		{
			//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
        
            MatrixState.pushMatrix();
            mountion.drawSelf(ssTexId,lcpTexId,dlTexId,hcpTexId,rgbTexId);
            MatrixState.popMatrix(); 
		}
		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height)
		{
			//�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ���� 
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 2000);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(cx,105,cz,tx,75,tz,0,1,0);
            //��ʼ����Դλ��
            MatrixState.setLightLocation(-400, 1000, -400);
		}
		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config)
		{
			//������Ļ����ɫRGBA
			GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            MatrixState.setInitStack();
            //�ӻҶ�ͼƬ�м���½����ÿ������ĸ߶�
    		yArray=loadLandforms(MySurfaceView.this.getResources(), R.drawable.heightmap16);
            normols = caleNormal(yArray);
            mountion=new Mountion(MySurfaceView.this,yArray,normols,yArray.length-1,yArray[0].length-1);
            //��ʼ������
            ssTexId=initTexture(R.drawable.ss);
            lcpTexId=initTexture(R.drawable.lcp);
            dlTexId=initTexture(R.drawable.dl);
            hcpTexId=initTexture(R.drawable.hcp);
            rgbTexId=initTextureSingle(R.drawable.rgb);
		}
    }
	//��������Id�ķ���
	public int initTextureSingle(int drawableId)
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
		  GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);	//������
		  GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,
					GLES30.GL_LINEAR_MIPMAP_LINEAR);   		//ʹ��MipMap�����������
		  GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MIN_FILTER,
			          GLES30.GL_LINEAR_MIPMAP_NEAREST);		//ʹ��MipMap������������
		//ST�����������췽ʽ
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);		
        
        //ͨ������������ͼƬ
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
        //�Զ�����Mipmap����
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        //�ͷ�����ͼ
        bitmapTmp.recycle();
        //��������ID
        return textureId;
	}
	//��������Id�ķ���
	public int initTexture(int drawableId)
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
		  GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);	//������
		  GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,
					GLES30.GL_LINEAR_MIPMAP_LINEAR);   		//ʹ��MipMap�����������
		  GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MIN_FILTER,
			          GLES30.GL_LINEAR_MIPMAP_NEAREST);		//ʹ��MipMap������������
		//ST�����������췽ʽ
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);		
        
        //ͨ������������ͼƬ
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
        //�Զ�����Mipmap����
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        //�ͷ�����ͼ
        bitmapTmp.recycle();
        //��������ID
        return textureId;
	}
}