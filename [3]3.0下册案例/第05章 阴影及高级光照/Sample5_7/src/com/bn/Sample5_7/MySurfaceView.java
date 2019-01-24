package com.bn.Sample5_7;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
 
class MySurfaceView extends GLSurfaceView 
{
    private SceneRenderer mRenderer;//������Ⱦ��    
     //�����λ�����
    float cx=0;
    float cy=30;
    float cz=60;
    
    //�ƹ�λ��
	float lx=0;//50;
	float ly=40;
	float lz=0;//30;  

    private float mPreviousY;//�ϴεĴ���λ��Y����
    float xAngle=0;//�������X����ת�ĽǶ�
    private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
   
    float roate=0;
    
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	
	//�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float y = e.getY();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
        	
            //��������λ���������x����ת -90��+90
            float dy = y - mPreviousY;//���㴥�ر�Yλ�� 
            xAngle += dy * TOUCH_SCALE_FACTOR;//��Yλ���������X����ת�ĽǶ�
            if(xAngle>90)
            {
            	xAngle=90;
            }
            else if(xAngle<-90)
            {
            	xAngle=-90;
            }
            cy=(float) (7.2*Math.sin(Math.toRadians(xAngle)));
            cz=(float) (7.2*Math.cos(Math.toRadians(xAngle)));
            float upy=(float) Math.cos(Math.toRadians(xAngle));
            float upz=-(float) Math.sin(Math.toRadians(xAngle));
            MatrixState.setCamera(0, cy, cz, 0, 0, 0, 0, upy, upz);           
        }
        mPreviousY = y;//��¼���ر�λ��
        return true; 
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {  
    	//��ָ����obj�ļ��м��ض���
		LoadedObjectVertexNormal lovo_ch;//����ģ��
		
        public void onDrawFrame(GL10 gl)
        {        	   
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            
            //��������ģ��
            MatrixState.pushMatrix(); 
            MatrixState.rotate(roate, 0, 1,0);
            //�����ص����岿λ�����������
            lovo_ch.drawSelf();
            MatrixState.popMatrix(); 
          
        }

        float ratio;
        public void onSurfaceChanged(GL10 gl, int width, int height) 
        {
        	//�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            ratio = (float) width / height; 
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	
        	MatrixState.setCamera(0,0,7.2f,0f,0f,0f,0f,1.0f,0.0f);
            MatrixState.setProjectFrustum(-ratio, ratio, -1.0f, 1.0f, 2, 100); 
            MatrixState.setLightLocation(lx, ly, lz); 
            
            //����һ���̶߳�ʱ��ת��������
            new Thread()
            {
            	public void run()
            	{
            		while(true)
            		{
            			//������ת�Ƕ�
            			roate=(roate+2)%360;
            			try {
							Thread.sleep(100);
						} catch (InterruptedException e) {				  			
							e.printStackTrace();
						}
            		}
            	}
            }.start();
        }
       
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {     	
        	//������Ļ����ɫRGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            //����Ҫ���Ƶ�����
            lovo_ch=LoadUtil.loadFromFile("rw1.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
        }
    }
}

