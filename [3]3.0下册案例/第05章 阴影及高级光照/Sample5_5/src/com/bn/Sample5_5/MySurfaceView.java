package com.bn.Sample5_5;//������
import java.io.IOException;
import java.io.InputStream;

import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��  
    private float mPreviousY;//�ϴεĴ���λ��Y����
    private float mPreviousX;//�ϴεĴ���λ��X����
	//����������ı���
	float cx=0;//�����xλ��
	float cy=0;//�����yλ��
	float cz=60;//�����zλ��
	
	float tx=0;//Ŀ���xλ��
	float ty=0;//Ŀ���yλ��
	float tz=0;//Ŀ���zλ��
	float upX=0;
	float upY=1;
	float upZ=0;

	float tempx=upX+cx;//�м�ֵx
	float tempz=upZ+cy;//�м�ֵz
	float tempLimit=tempz;
	public float currSightDis=100;//�������Ŀ��ľ���
	float angdegElevation=30;//����
	public float angdegAzimuth=180;//��λ��	
	final int size=4;
    int[] TexId=new int[size];//���������ĸ�����

  	float ratio;
  	
	
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
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;//���㴥�ر�Yλ��
            float dx = x - mPreviousX;//���㴥�ر�Xλ��
            //��������ֵ���ƶ������
            if(Math.abs(dx)<7f && Math.abs(dy)<7f){
            	break;
            }            
            angdegAzimuth += dx * TOUCH_SCALE_FACTOR;//������x����ת�Ƕ�
            angdegElevation += dy * TOUCH_SCALE_FACTOR;//������z����ת�Ƕ�
            //������������5��90�ȷ�Χ��
            angdegElevation = Math.max(angdegElevation, 5);
            angdegElevation = Math.min(angdegElevation, 90);
            //�����������λ��
            setCameraPostion();
        break;
        }
        mPreviousY = y;//��¼���ر�λ��
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }
    //���������λ�õķ���
   	public void setCameraPostion() {
   		//�����������λ��
   		double angradElevation = Math.toRadians(angdegElevation);//���ǣ����ȣ�
   		double angradAzimuth = Math.toRadians(angdegAzimuth);//��λ��
   		cx = (float) (tx - currSightDis * Math.cos(angradElevation)	* Math.sin(angradAzimuth));
   		cy = (float) (ty + currSightDis * Math.sin(angradElevation));
   		cz = (float) (tz - currSightDis * Math.cos(angradElevation) * Math.cos(angradAzimuth));
   		
   		tempx=(float) (Math.sin(angradAzimuth)*tempLimit);
		tempz=(float) (Math.cos(angradAzimuth)*tempLimit);
		//����up����ֵ
		upX=tempx-cx;
		upZ=tempz-cz;
		MatrixState.setCamera(cx, cy, cz, tx, ty, tz, upX,upY, upZ);
   	}

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {  
    	//��ָ����obj�ļ��м��صĶ���
		LoadedObjectVertexNormalTexture lovo[]=new LoadedObjectVertexNormalTexture[5];
    	float[] trans={18,20,0};//��ͨ���λ��
    	
        public void onDrawFrame(GL10 gl) 
        {
             
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            
            
            
            //���ƾ��о�̬������ͼ���
            for(int i=0;i<2;i++)
            {
	    		MatrixState.pushMatrix();
	        	lovo[i].drawSelf1(TexId[i]);
	        	MatrixState.popMatrix();  
            }
            
            //����ƽ��
            MatrixState.pushMatrix();
            lovo[2].drawSelf1(TexId[2]);
            MatrixState.popMatrix();
            
            //��ͨ�������--��Ӱ
    		MatrixState.pushMatrix();
    		MatrixState.scale(1.2f, 1.2f,1.2f);
   		 	MatrixState.translate(trans[0],trans[2],trans[1]);
   		 	lovo[3].drawSelf(TexId[3],1);
   		 	MatrixState.popMatrix();
            
            //��ͨ�������
    		MatrixState.pushMatrix();
    		MatrixState.scale(1.2f, 1.2f, 1.2f);
   		 	MatrixState.translate(trans[0],trans[2],trans[1]);
   		 	lovo[3].drawSelf(TexId[3],0);
   		 	MatrixState.popMatrix();
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
        	 ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 1000);
            //�����������λ��
            setCameraPostion();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.5f,0.5f,1.0f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            //��ʼ����Դλ��
            MatrixState.setLightLocation(186,111,104);
            
            lovo[0]=LoadUtil.loadFromFile("ch1.obj", MySurfaceView.this.getResources(),MySurfaceView.this,true);
            lovo[1]=LoadUtil.loadFromFile("ch2.obj", MySurfaceView.this.getResources(),MySurfaceView.this,true);
            lovo[2]=LoadUtil.loadFromFile("pm.obj", MySurfaceView.this.getResources(),MySurfaceView.this,true);
            lovo[3]=LoadUtil.loadFromFile("ptch.obj", MySurfaceView.this.getResources(),MySurfaceView.this,false);
           
            TexId[0]=initTexture(R.drawable.c1);//���1��̬������ͼ
            TexId[1]=initTexture(R.drawable.c2);//���2��̬������ͼ
            TexId[2]=initTexture(R.drawable.pm);//ƽ����ͼ
            TexId[3]=initTexture(R.drawable.ghxp);//��ͨ�����ͼ
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
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);
        
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
	   	GLUtils.texImage2D
	    (
	    		GLES30.GL_TEXTURE_2D, //��������
	     		0, 
	     		GLUtils.getInternalFormat(bitmapTmp), 
	     		bitmapTmp, //����ͼ��
	     		GLUtils.getType(bitmapTmp), 
	     		0 //����߿�ߴ�
	     );
	    bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        return textureId;
	}
}
