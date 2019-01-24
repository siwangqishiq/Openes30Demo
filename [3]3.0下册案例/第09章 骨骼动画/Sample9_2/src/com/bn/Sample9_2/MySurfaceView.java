package com.bn.Sample9_2;
import java.io.IOException;
import java.io.InputStream;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.GLES30;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class MySurfaceView extends GLSurfaceView 
{
    private SceneRenderer mRenderer;//������Ⱦ��    
    int armTexId;//ϵͳ���������id--�첲������
    int headTexId;//ϵͳ���������id--ͷ
    int floorTexId;//ϵͳ��������� ����
    //�������岿���Ļ���������
    LoadedObjectVertexNormalTexture[] lovntArray=new LoadedObjectVertexNormalTexture[12];
    LoadedObjectVertexNormalTexture floor;
    //������
    Robot robot;
    //ִ�ж������߳�
    DoActionThread dat;
    private final float TOUCH_SCALE_FACTOR = 80.0f/800;//�Ƕ����ű���
    private float mPreviousX;//�ϴεĴ���λ��X����
    
    
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2); //����ʹ��OPENGL ES2.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	@Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dx = x - mPreviousX;//���㴥�ر�Xλ��
            mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;//������x����ת�Ƕ�
        }
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    { 
    	float yAngle;
        public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            MatrixState.setCamera(
            		(float)(2.5 * Math.sin(yAngle)),   //����λ�õ�X
            		0.05f, 	//����λ�õ�Y
            		(float)(2.5 * Math.cos(yAngle)),   //����λ�õ�Z
            		0, 	//�����򿴵ĵ�X
            		0.03f,   //�����򿴵ĵ�Y
            		0f,   //�����򿴵ĵ�Z
            		0, 
            		1, 
            		0);
           
            //���ƻ�����
            robot.drawSelf();
            
            MatrixState.pushMatrix();     	
        	if(floor!=null)floor.drawSelf();
        	MatrixState.popMatrix();  

            
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(2f,   //����λ�õ�X
            		0.05f, 	//����λ�õ�Y
            		2f,   //����λ�õ�Z
            		0, 	//�����򿴵ĵ�X
            		0f,   //�����򿴵ĵ�Y
            		0f,   //�����򿴵ĵ�Z
            		0, 
            		1, 
            		0);
            MatrixState.setInitStack();
            MatrixState.setLightLocation(0,10,10);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(1f,1f,1f, 1.0f);   
            //�رձ������   
            GLES30.glDisable(GLES30.GL_CULL_FACE);  
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //��ʼ������
            armTexId=initTexture(R.drawable.arm);
            headTexId=initTexture(R.drawable.head);//ϵͳ���������id
            floorTexId=initTexture(R.drawable.wenli);
          //��������
            lovntArray[0]=LoadUtil.loadFromFile
            (
                	"body.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            );
            //����ͷ
            lovntArray[1]=LoadUtil.loadFromFile
            (
                	"head.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	headTexId
            );
            //������첲
            lovntArray[2]=LoadUtil.loadFromFile
            (
                	"left_top.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            );
            lovntArray[3]=LoadUtil.loadFromFile
            (
                	"left_bottom.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            );
            
            
            //�����Ҹ첲
            lovntArray[4]=LoadUtil.loadFromFile
            (
                	"right_top.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            );  
            lovntArray[5]=LoadUtil.loadFromFile
            (
                	"right_bottom.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            );  
            
            //��������
            lovntArray[6]=LoadUtil.loadFromFile
            (
                	"right_leg_top.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            );  
            lovntArray[7]=LoadUtil.loadFromFile
            (
                	"right_leg_bottom.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            );
            
            //��������
            lovntArray[8]=LoadUtil.loadFromFile
            (
                	"left_leg_top.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            );  
            lovntArray[9]=LoadUtil.loadFromFile
            (
                	"left_leg_bottom.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            ); 
            
            //�������
            lovntArray[10]=LoadUtil.loadFromFile
            (
                	"left_foot.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            );  
            lovntArray[11]=LoadUtil.loadFromFile
            (
                	"right_foot.obj", 
                	MySurfaceView.this.getResources(),
                	MySurfaceView.this,
                	armTexId
            ); 
            
            floor = LoadUtil.loadFromFile
            (
                  	"floor.obj", 
                  	MySurfaceView.this.getResources(),
                  	MySurfaceView.this,
                  	floorTexId
             ) ;

            
            robot=new Robot(lovntArray,MySurfaceView.this);  
            dat=new DoActionThread(robot,MySurfaceView.this);       
            dat.start();
        }
    }
	
	public int initTexture(int resId)//textureId
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
        InputStream is = this.getResources().openRawResource(resId);
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
        //ͨ������������ͼƬ===============end=====================  
        
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
