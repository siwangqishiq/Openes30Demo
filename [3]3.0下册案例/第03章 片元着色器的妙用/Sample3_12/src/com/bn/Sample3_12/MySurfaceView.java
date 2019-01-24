package com.bn.Sample3_12;
import java.nio.ByteBuffer;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.Sample3_12.tex3d.Load3DTexUtil;
import com.bn.Sample3_12.tex3d.Tex3D;

import android.annotation.SuppressLint;
import android.content.Context;

class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��    
    
    private float mPreviousY;//�ϴεĴ���λ��Y���� 
    private float mPreviousX;//�ϴεĴ���λ��X����
    
    int textureId;//ϵͳ���������id
	
	public MySurfaceView(Context context) { 
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES 3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    } 
	
	//�����¼��ص�����
	@SuppressLint("ClickableViewAccessibility")
	@Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float y = e.getY(); 
        float x = e.getX();
        switch (e.getAction()) 
        {
          case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;//���㴥�ر�Yλ��
            float dx = x - mPreviousX;//���㴥�ر�Xλ��
            mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;//������x����ת�Ƕ�
            mRenderer.xAngle+= dy * TOUCH_SCALE_FACTOR;//������z����ת�Ƕ�
        }
        mPreviousY = y;//��¼���ر�λ��
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {  
		float yAngle;//��Y����ת�ĽǶ�
    	float xAngle; //��Z����ת�ĽǶ�
    	//��ָ����obj�ļ��м��ض���
		LoadedObjectVertexNormal lovo;
    	
        public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

            //����ϵ��Զ
            MatrixState.pushMatrix();
            MatrixState.translate(0, -0.2f, -3f);   
            //��Y�ᡢZ����ת
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.rotate(xAngle, 1, 0, 0);
            
            //�����ص����岿λ�����������
            if(lovo!=null)
            {
            	lovo.drawSelf(textureId);
            }   
            MatrixState.popMatrix();                  
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) 
        {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio*0.3f, ratio*0.3f, -1*0.3f, 1*0.3f, 2, 100);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,0,0,0f,0f,-1f,0f,1.0f,0.0f);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(1.0f,1.0f,1.0f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            
            //��ʼ���任����
            MatrixState.setInitStack();
            //��ʼ����Դλ��
            MatrixState.setLightLocation(400, 100, 200);
            //����Ҫ���Ƶ�����
            lovo=LoadUtil.loadFromFile("ch.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            //��������
            //��������
    		Tex3D tex3D=Load3DTexUtil.load(MySurfaceView.this.getResources(), "3dNoise.bn3dtex");
            textureId=init3DTexture(tex3D.data,tex3D.width,tex3D.height,tex3D.depth);
        }
    }
  	public int init3DTexture(byte[] texData,int width,int height,int depth)//textureId
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
		GLES30.glBindTexture(GLES30.GL_TEXTURE_3D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_3D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_3D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_3D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_3D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_3D, GLES30.GL_TEXTURE_WRAP_R,GLES30.GL_REPEAT);
        
		
		System.out.println(texData.length+"texData.length");		
		ByteBuffer texels = ByteBuffer.allocateDirect(texData.length); 
		texels.put(texData);
		texels.position(0);//���û�������ʼλ��
		
		GLES30.glTexImage3D
		(
				GLES30.GL_TEXTURE_3D,  
				0, 
				GLES30.GL_RGBA8, 
				width, 
				height, 
				depth, 
				0, 
				GLES30.GL_RGBA, 
				GLES30.GL_UNSIGNED_BYTE, 
	            texels
	    );
	    
        return textureId;
	}
}
