package com.bn.Sample5_4;
import java.io.IOException;
import java.io.InputStream;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
 
@SuppressLint("NewApi") class MySurfaceView extends GLSurfaceView 
{
    private SceneRenderer mRenderer;//������Ⱦ��    
     //�����λ�����
    float cx=0;
    float cy=30;
    float cz=60;
    float cAngle=0;
    final float cR=60;
    
    //�ƹ�λ��
	float lx=40;
	float ly=60;
	float lz=0;   
	float lAngle=0;
	final float lR=1;
	//�ƹ�ͶӰUp����   
	float ux=-3;
	float uy=2;
	float uz=0;
	
	float tx=0;
	float ty=0;
	float tz=0;
	int angle=0;  
    //��Դ�ܱ任����
    float[] mMVPMatrixGY;
	static int width=0;//��Ļ�Ŀ��
	static int height=0;//��Ļ�ĸ߶�
	int move_x=0;//����ģ���ƶ���־λ
	int move_z=0;//����ģ���ƶ���־λ
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(2); //����ʹ��OPENGL ES2.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	
	//�����¼��ص�����
    @SuppressLint("ClickableViewAccessibility") @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
        {
        	if(x<MySurfaceView.width*0.5f&&y<MySurfaceView.height*0.5f)
        	{
        		move_x=1;
        	}else if(x<MySurfaceView.width*0.5f&&y>MySurfaceView.height*0.5f)
        	{
        		move_x=-1;
        	}else if(x>MySurfaceView.width*0.5f&&y<MySurfaceView.height*0.5f)
        	{
        		move_z=1;
        	}else if(x>MySurfaceView.width*0.5f&&y>MySurfaceView.height*0.5f)
        	{
        		move_z=-1;
        	}
        }
        break;
        case MotionEvent.ACTION_UP:
        {
        	move_x=0;
        	move_z=0;
        } 
        break;
       }
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {  
    	//��ָ����obj�ļ��м��ض���
		LoadeObjectVertexLand lovo_pm;//ƽ��
		LoadedObjectVertexNormal lovo_ch;//����ģ��	
		//����Id
		int tyTexId;
        
        public void onDrawFrame(GL10 gl)
        {        	   
        	//�����ƹ�λ�õ�ͶӰ�����������
            MatrixState.setCamera(lx,ly,lz,0,0f,0f,ux,uy,uz);
            MatrixState.setProjectFrustum(-0.5f, 0.5f, -0.5f, 0.5f, 0.14f, 400);  
            mMVPMatrixGY=MatrixState.getViewProjMatrix();
            
            
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(cx,cy,cz,0f,0f,0f,0f,1f,0f);
            MatrixState.setProjectFrustum(-ratio, ratio, -1.0f, 1.0f, 2, 1000);  
            MatrixState.setLightLocation(lx, ly, lz); 
            
            //�����������ƽ��
            MatrixState.pushMatrix();
            MatrixState.scale(1f,1f,1f);
            lovo_pm.drawSelf(tyTexId,mMVPMatrixGY,0);  
            MatrixState.popMatrix(); 
            
            //��������ģ��
            MatrixState.pushMatrix(); 
            MatrixState.translate(tx, 0,tz);
            MatrixState.rotate(180, 0, 1,0);
            MatrixState.scale(2f,2f,2f);
            //�����ص����岿λ�����������
            lovo_ch.drawSelf(tyTexId,mMVPMatrixGY,0);
            lovo_ch.drawSelf(tyTexId,mMVPMatrixGY,1);
            MatrixState.popMatrix();     

        }

        float ratio;
        public void onSurfaceChanged(GL10 gl, int width, int height) 
        {
        	MySurfaceView.width=width;
        	MySurfaceView.height=height;
        	//�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            ratio = (float) width / height; 
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
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
            lovo_ch=LoadUtil.loadFromFileVertexOnly("ver.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            lovo_pm=LoadUtil.loadFromFileVertexLand("pm.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            //��ʾ��Ӱ��ͼ���������
            tyTexId=initTexture(R.raw.w);
            Move_Thread mt=new Move_Thread();
            mt.start();
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
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR);
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
    class Move_Thread extends Thread
    {
    	public void run()
    	{
    		while(true)
    		{
    			if(move_x==1)
    			{
    				tx+=1;
    			}else if(move_x==-1)
    			{
    				tx-=1;
    			}else if(move_z==1)
    			{
    				tz-=1;
    			}else if(move_z==-1)
    			{
    				tz+=1;
    			}
    			try {
					sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}
    }
}

