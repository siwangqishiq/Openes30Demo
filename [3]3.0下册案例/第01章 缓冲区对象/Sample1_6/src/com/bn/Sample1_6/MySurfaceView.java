package com.bn.Sample1_6;

import java.io.IOException;
import java.io.InputStream;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.Sample1_6.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

@SuppressLint("NewApi")
class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��
    
    private float mPreviousY;//�ϴεĴ���λ��Y����
    private float mPreviousX;//�ϴεĴ���λ��X����
    float ratio;
    
    static final int GEN_TEX_WIDTH=1024;
    static final int GEN_TEX_HEIGHT=512;
    
    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OpenGL ES 3.0
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
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;//���㴥�ص�Yλ��
            float dx = x - mPreviousX;//���㴥�ص�Xλ��
            mRenderer.yAngle += dx * TOUCH_SCALE_FACTOR;//������y����ת�Ƕ�
            mRenderer.xAngle+= dy * TOUCH_SCALE_FACTOR;//������x����ת�Ƕ�
            requestRender();//�ػ滭��
        }
        mPreviousY = y;//��¼���ر�λ��
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		float yAngle;//��Y����ת�ĽǶ�
    	float xAngle; //��X����ת�ĽǶ�

		LoadedObjectVertexNormalTexture lovo;    	//��ָ����obj�ļ��м��ض���
		
		int[] textureIds = new int[4];//���ڴ�Ų�������id������
		int frameBufferId;//֡����id
		int textureIdGHXP;//����СƷ������id
		TextureRect tr;//���λ��ƶ���

		int renderDepthBufferId;//��Ⱦ��Ȼ���id
		
		@SuppressLint("NewApi")
		public boolean initFBO()
		{
			int[] attachments=new int[]{
					GLES30.GL_COLOR_ATTACHMENT0,
					GLES30.GL_COLOR_ATTACHMENT1,					
					GLES30.GL_COLOR_ATTACHMENT2,
					GLES30.GL_COLOR_ATTACHMENT3
			};
			int tia[]=new int[1];//���ڴ�Ų�����֡����id������
			
			//֡����========start==========
			GLES30.glGenFramebuffers(1, tia, 0);//����һ��֡����id
			frameBufferId=tia[0];//��֡����id��¼����Ա������
			//��֡����id
			GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);
			//֡����========end==========
			
			//��Ⱦ����=========start============
			GLES30.glGenRenderbuffers(1, tia, 0);//����һ����Ⱦ����id
			renderDepthBufferId=tia[0];//����Ⱦ����id��¼����Ա������
			//��ָ��id����Ⱦ����
			GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderDepthBufferId);
			//Ϊ��Ⱦ�����ʼ���洢
			GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER,
					GLES30.GL_DEPTH_COMPONENT16,GEN_TEX_WIDTH, GEN_TEX_HEIGHT);			
			//�����Զ���֡�������Ȼ��帽��
			GLES30.glFramebufferRenderbuffer	
        	(
        		GLES30.GL_FRAMEBUFFER,
        		GLES30.GL_DEPTH_ATTACHMENT,		//��Ȼ��帽��
        		GLES30.GL_RENDERBUFFER,			//��Ⱦ����
        		renderDepthBufferId				//��Ⱦ��Ȼ���id
        	);
			//��Ⱦ����=========end============
			
			
			GLES30.glGenTextures//����4������id
    		(
    				textureIds.length,			//����������id������
    				textureIds,	//����id������
    				0			//ƫ����
    		);
			for(int i=0;i<attachments.length;i++)
			{
				GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,textureIds[i]);//������id
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
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//����MIN������ʽ
						GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//����MAG������ʽ
						GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//����S�����췽ʽ
						GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
				GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,//����T�����췽ʽ
						GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE);
				
				GLES30.glFramebufferTexture2D		//��ָ������󶨵�֡����
	            (
	            	GLES30.GL_DRAW_FRAMEBUFFER,
	            	attachments[i],					//��ɫ����
	            	GLES30.GL_TEXTURE_2D,
	            	textureIds[i], 					//����id
	            	0								//���
	            );
			}
			
			GLES30.glDrawBuffers(attachments.length, attachments,0);
			if(GLES30.GL_FRAMEBUFFER_COMPLETE != 
					GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER))
			{
				return false;
			}
			return true;
		}
		
		@SuppressLint("NewApi") 
		public void generateTextImage()//ͨ�����Ʋ�������
		{
			//������Ļ����ɫRGBA
        	GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);  
			//�����Ӵ���С��λ�� 
			GLES30.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
			//��֡����id
			GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,frameBufferId );//frameBufferId
    		//�����Ȼ�������ɫ����
			GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //����͸��ͶӰ
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,0,3,0f,0f,-1f,0f,1.0f,0.0f);
            
            MatrixState.pushMatrix();//�����ֳ�
            MatrixState.translate(0f, -15f, -70f);//����ϵ��Զ
            //��Y�ᡢX����ת
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.rotate(xAngle, 1, 0, 0);
            if(lovo!=null)//�����ص����岿λ�����������
            {
            	lovo.drawSelf(textureIdGHXP);
            }
            MatrixState.popMatrix();//�ָ��ֳ�
		}
		
		@SuppressLint("NewApi")
		public void drawShadowTexture()//�������ɵľ�������
		{
			//������Ļ����ɫRGBA
        	GLES30.glClearColor(0.5f,0.5f,0.5f,1.0f);  
			//�����Ӵ���С��λ�� 
			GLES30.glViewport(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
			GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);//��֡����id
        	//�����Ȼ�������ɫ����
			GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT |GLES30.GL_COLOR_BUFFER_BIT);
            //��������ͶӰ
            MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 2, 100);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,0,3,0f,0f,0f,0f,1.0f,0.0f);
            
            MatrixState.pushMatrix();
            MatrixState.translate(-ratio/2, 0.5f, 0);
            tr.drawSelf(textureIds[0]);//�����������
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            MatrixState.translate(ratio/2, 0.5f, 1);
            tr.drawSelf(textureIds[1]);//�����������
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            MatrixState.translate(-ratio/2, -0.5f, 0);
            tr.drawSelf(textureIds[2]);//�����������
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            MatrixState.translate(ratio/2, -0.5f, 1);
            tr.drawSelf(textureIds[3]);//�����������
            MatrixState.popMatrix();
		}

		public void onDrawFrame(GL10 gl) 
        {
        	generateTextImage();//ͨ�����Ʋ�����������
        	drawShadowTexture();//���ƾ�������
        }
       
		public void onSurfaceChanged(GL10 gl, int width, int height)
        {
        	SCREEN_WIDTH=width;
        	SCREEN_HEIGHT=height;
            ratio = (float) width / height;//����GLSurfaceView�Ŀ�߱�
            initFBO();
            textureIdGHXP=initTexture(R.drawable.ghxp);//���ع���СƷ����ͼ
            tr=new TextureRect(MySurfaceView.this,ratio);//�������λ��ƶ���
        }
        
		public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {
            //����ȼ��
        	GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
        	GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            //��ʼ����Դλ��
            MatrixState.setLightLocation(40, 100, 20);
            //����Ҫ���Ƶ�����
            lovo=LoadUtil.loadFromFile("ch_t.obj", MySurfaceView.this.getResources(),
            		MySurfaceView.this);
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
        try{bitmapTmp = BitmapFactory.decodeStream(is);}
        finally{try{is.close();}catch(IOException e){e.printStackTrace();}
        }
        //ͨ������������ͼƬ===============end===================== 
	   	GLUtils.texImage2D(
	    		GLES30.GL_TEXTURE_2D, //��������
	     		0,						//���
	     		GLUtils.getInternalFormat(bitmapTmp),//�ڲ���ʽ
	     		bitmapTmp, //����ͼ��
	     		GLUtils.getType(bitmapTmp),//��������
	     		0 //����߿�ߴ�
	     );
	    bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        return textureId;//��������id
	}
}
