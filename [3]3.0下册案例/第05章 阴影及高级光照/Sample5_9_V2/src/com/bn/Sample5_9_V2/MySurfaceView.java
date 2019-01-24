package com.bn.Sample5_9_V2;
import java.io.IOException;
import java.io.InputStream;

import static com.bn.Sample5_9_V2.Constant.*;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.Sample5_9_V2.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��        
    int skyBoxTextureId;//ϵͳ���������id
    int teaPotTextureId;
    static final int GEN_TEX_WIDTH=1024;
    static final int GEN_TEX_HEIGHT=1024;
    private float mPreviousX;//�ϴεĴ���λ��X����
    float xAngle=0;
	//����������Ĺ۲���ͶӰ��Ͼ���
    float[] mMVPMatrixMirror;
	
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	@SuppressLint("ClickableViewAccessibility")
	@Override 
  public boolean onTouchEvent(MotionEvent e) 
  {
      float x = e.getX();
      switch (e.getAction()) 
      {
      case MotionEvent.ACTION_MOVE:
			float dx = x - mPreviousX;//���㴥�ص�Xλ��
			if(Math.abs(dx)<=0.02f){
				break;
			}
			xAngle += dx*TOUCH_SCALE_FACTOR;
			if(xAngle<ANGLE_MIN)
			{
				xAngle=ANGLE_MIN;
			}
			else if(xAngle>ANGLE_MAX)
			{
				xAngle=ANGLE_MAX;
			}		
			calculateMainAndMirrorCamera(xAngle);
		}
		mPreviousX = x;//��¼���ص�λ��
      return true;
  }
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		int mirrorId;// ��̬�����ľ�������Id
		int frameBufferId;//֡����id
		int renderDepthBufferId;//��Ⱦ��Ȼ���id
    	//��ָ����obj�ļ��м��ض���
		LoadedObjectVertexNormalTexture lovo1;//������ƶ���
		LoadedObjectVertexNormalTexture bed;//�����ƶ���
		LoadedObjectVertexNormalTexture deng;//�ƻ��ƶ���
		LoadedObjectVertexNormalTexture book0;//����ƶ���
		LoadedObjectVertexNormalTexture chair;//���ӻ��ƶ���
		LoadedObjectVertexNormalTexture book1;//����ƶ���
		LoadedObjectVertexNormalTexture border;//���ӱ߿���ƶ���
		TextureRect mirror;//�������ӵ�������ζ���
		int bedId;
		int dengId;
		int bookId0;
		int bookId1;
		int chairId;
		int borderId;
		
    	
        public void onDrawFrame(GL10 gl) 
        {
        	generateTextImage();
        	drawMirrorNegativeTexture();
        }
        
        public void initFRBuffers()
        {
				int tia[]=new int[1];//���ڴ�Ų�����֡����id������
				GLES30.glGenFramebuffers(tia.length, tia, 0);//����һ��֡����id
				frameBufferId=tia[0];//��֡����id��¼����Ա������
	    		//��֡����id
				GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);

				GLES30.glGenRenderbuffers(tia.length, tia, 0);//����һ����Ⱦ����id
				renderDepthBufferId=tia[0];//����Ⱦ����id��¼����Ա������
				//��ָ��id����Ⱦ����
				GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderDepthBufferId);
				//Ϊ��Ⱦ�����ʼ���洢
				GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, 
						GLES30.GL_DEPTH_COMPONENT16,GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
				
				int[] tempIds = new int[1];//���ڴ�Ų�������id������
				GLES30.glGenTextures//����һ������id
	    		(
	    				tempIds.length,         //����������id������
	    				tempIds,   //����id������
	    				0           //ƫ����
	    		);
				mirrorId=tempIds[0];//������id��¼����Ա����
				GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,mirrorId);//������id
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//����MIN������ʽ
						GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR);
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//����MAG������ʽ
						GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//����S�����췽ʽ
						GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//����T�����췽ʽ
						GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE); 
				GLES30.glTexImage2D//������ɫ��������ͼ�ĸ�ʽ
	        	(
	        		GLES30.GL_TEXTURE_2D,
	        		0,						//���
	        		GLES30.GL_RGBA, 		//�ڲ���ʽ
	        		GEN_TEX_WIDTH,			//���
	        		GEN_TEX_HEIGHT,			//�߶�
	        		0,						//�߽���
	        		GLES30.GL_RGBA,			//��ʽ 
	        		GLES30.GL_UNSIGNED_BYTE,//ÿ���������ݸ�ʽ
	        		null
	        	);
				GLES30.glFramebufferTexture2D		//�����Զ���֡�������ɫ���帽��
	            (
	            	GLES30.GL_FRAMEBUFFER,
	            	GLES30.GL_COLOR_ATTACHMENT0,	//��ɫ���帽��
	            	GLES30.GL_TEXTURE_2D,
	            	mirrorId, 						//����id
	            	0								//���
	            );   		
				GLES30.glFramebufferRenderbuffer	//�����Զ���֡�������Ȼ��帽��
	        	(
	        		GLES30.GL_FRAMEBUFFER,
	        		GLES30.GL_DEPTH_ATTACHMENT,		//��Ȼ��帽��
	        		GLES30.GL_RENDERBUFFER,			//��Ⱦ����
	        		renderDepthBufferId				//��Ⱦ��Ȼ���id
	        	);
        }
        
        //��һ�λ��ƣ����Ƴ����������ƾ���
        public void generateTextImage()
        {
        	//�����ӿڴ�С
			GLES30.glViewport(0,0,GEN_TEX_WIDTH,GEN_TEX_HEIGHT);	
			//�����ɵ�֡����id  
			GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);
			//���þ����������9����
			MatrixState.setCamera(mirrorCameraX,mirrorCameraY,mirrorCameraZ,  targetX,targetY,targetZ,  upX,upY,upZ);
        	//���ô˷����������͸��ͶӰ����
			MatrixState.setProjectFrustum(left, right, bottom, top, near, far);
			//��ȡ����������Ĺ۲���ͶӰ��Ͼ���
			mMVPMatrixMirror=MatrixState.getViewProjMatrix();
			//�����Ȼ������ɫ����
			GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
			drawThings();			//���Ƴ���
        }
        
        //�ڶ��λ��ƣ����Ƴ�������������
        public void drawMirrorNegativeTexture()
        {     	
        	//�����ӿڴ�С
        	GLES30.glViewport(0, 0,SCREEN_WIDTH,SCREEN_HEIGHT);
        	//��ϵͳĬ�ϵ�֡����id  
        	GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0); 	
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //�������������9����
            MatrixState.setCamera(mainCameraX,mainCameraY,mainCameraZ,  targetX,targetY,targetZ,  upX,upY,upZ);
        	//���ô˷����������͸��ͶӰ����
        	MatrixState.setProjectFrustum(left, right, bottom, top, near, far);

        	drawThings();
            
            //���ƾ��ӱ߿�
            MatrixState.pushMatrix();
            MatrixState.translate(2.2f,12,targetZ-2);     
            border.drawSelf(borderId);
            MatrixState.popMatrix();
            
            //���ƾ���
            MatrixState.pushMatrix();
            MatrixState.translate(2.0f,12,targetZ-1.7f);            
            mirror.drawSelf(mirrorId,mMVPMatrixMirror);
            MatrixState.popMatrix();
        }
        
        //���Ƴ���������������
        public void drawThings()
        {
            //���Ʒ���
            MatrixState.pushMatrix();
            MatrixState.translate(0, -10, 7); 
            lovo1.drawSelf(skyBoxTextureId);
            MatrixState.popMatrix(); 
            
            MatrixState.pushMatrix();
            MatrixState.translate(40, 0,-12); 
            MatrixState.rotate(180, 0, 1, 0);
            deng.drawSelf(dengId);
            MatrixState.popMatrix(); 
            
            MatrixState.pushMatrix();
            MatrixState.translate(14, -7,-18); 
            book1.drawSelf(bookId1);
            MatrixState.popMatrix(); 
            
            MatrixState.pushMatrix();
            MatrixState.translate(22, 0,8);    
            MatrixState.rotate(180, 0, 1, 0);
            bed.drawSelf(bedId);
            MatrixState.popMatrix(); 
            
            MatrixState.pushMatrix();
            MatrixState.translate(-40, 30,10);
            book0.drawSelf(bookId0);
            MatrixState.popMatrix(); 
                        
            MatrixState.pushMatrix();
            MatrixState.translate(-40, 20,20); 
            book0.drawSelf(bookId0);
            MatrixState.popMatrix(); 
            
            MatrixState.pushMatrix();
            MatrixState.translate(-15, 0,-32); 
            chair.drawSelf(chairId);
            MatrixState.popMatrix(); 
                        
            MatrixState.pushMatrix();
            MatrixState.translate(40,0,34); 
            MatrixState.rotate(180, 0, 1, 0);
            deng.drawSelf(dengId);
            MatrixState.popMatrix(); 
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) 
        {
        	SCREEN_WIDTH=width;
        	SCREEN_HEIGHT=height;
        	//�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
        	ratio = (float) SCREEN_WIDTH / SCREEN_HEIGHT;
        	//��ʼ����Դλ��
        	MatrixState.setLightLocation(0, 10, 30);
        	initFRBuffers();        	       
        	initProject(1f); 	//����ͶӰ����
        }
        
        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE); 
            //��ʼ���任����
            MatrixState.setInitStack();
            //����Ҫ���Ƶ�����
            lovo1=LoadUtil.loadFromFile("skybox.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            bed=LoadUtil.loadFromFile("chuang.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            deng=LoadUtil.loadFromFile("deng.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            book0=LoadUtil.loadFromFile("shu00.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            chair=LoadUtil.loadFromFile("yizi0.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            book1=LoadUtil.loadFromFile("shu1.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            border=LoadUtil.loadFromFile("mirror.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            mirror=new TextureRect(MySurfaceView.this);
            //��������
            skyBoxTextureId=initTexture(R.drawable.skybox1);
            bedId=initTexture(R.drawable.chuang3);
            dengId=initTexture(R.drawable.deng2);
            bookId0=initTexture(R.drawable.shu0);
            bookId1=initTexture(R.drawable.shu1);
            chairId=initTexture(R.drawable.yizi);
            borderId=initTexture(R.drawable.mirror);
             //��ʼ����������;���������Ĳ���
            calculateMainAndMirrorCamera(xAngle);
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
