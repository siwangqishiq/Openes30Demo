package com.bn.Sample5_12;

import java.io.IOException;
import java.io.InputStream;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.bn.Sample5_12.Constant.*;

@SuppressLint("NewApi") 
class MySurfaceView extends GLSurfaceView
{
    private SceneRenderer mRenderer;//������Ⱦ��
    float ratio;
    
    float cz=100f;//�������zλ������
    float targetz=0;//�������zĿ�������
    float preCZ=cz;//ǰһ֡���������zλ������
    float preTargetZ=targetz;//ǰһ֡���������zĿ�������
    
    int SCREEN_WIDTH;//��Ļ���
    int SCREEN_HEIGHT;//��Ļ�߶�
    
    float[] mViewProjectionInverseMatrix;//��ǰ�۲�-ͶӰ���������
    float[] mPreviousProjectionMatrix;//ǰһ֡�Ĺ۲�-ͶӰ����
    
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		LoadedObjectVertexNormalTexture tree1;//��һ�������ƶ���
		LoadedObjectVertexNormalTexture tree2;//�ڶ��������ƶ���
    	Mountain mountain;//ɽ�ػ��ƶ���
    	Sky_cloud cloud;//��ջ��ƶ���
    	
		int frameBufferId;//֡����id
		int renderDepthBufferId;//��Ⱦ��Ȼ���id
		int[] tempIds = new int[2];//���ڴ�Ų�������id������
		
		int mountainTextId;//ɽ������id
		int tree1TextId;//��һ����������id
		int tree2TextId;//�ڶ�����������id
		int cloudTextId;//�������id
		TextureRect tr;//���λ��ƶ���
		
		public boolean initFRBuffers()//��ʼ��֡�������Ⱦ����ķ���
		{
			int attachment[]=new int[]{
					GLES30.GL_COLOR_ATTACHMENT0,
					GLES30.GL_COLOR_ATTACHMENT1
			};
			int tia[]=new int[1];//���ڴ�Ų�����֡����id������
			GLES30.glGenFramebuffers(1, tia, 0);//����һ��֡����id
			frameBufferId=tia[0];//��֡����id��¼����Ա������
			//��֡����id
			GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);
			
			GLES30.glGenRenderbuffers(1, tia, 0);//����һ����Ⱦ����id
			renderDepthBufferId=tia[0];//����Ⱦ����id��¼����Ա������
			//��ָ��id����Ⱦ����
			GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderDepthBufferId);
			//Ϊ��Ⱦ�����ʼ���洢
			GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER,
					GLES30.GL_DEPTH_COMPONENT16,GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
			GLES30.glFramebufferRenderbuffer	//�����Զ���֡�������Ȼ��帽��
        	(
        		GLES30.GL_FRAMEBUFFER,
        		GLES30.GL_DEPTH_ATTACHMENT,		//��Ȼ��帽��
        		GLES30.GL_RENDERBUFFER,			//��Ⱦ����
        		renderDepthBufferId				//��Ⱦ��Ȼ���id
        	);
			
			GLES30.glGenTextures//����һ������id
    		(
    				2,         //����������id������
    				tempIds,   //����id������
    				0           //ƫ����
    		);
			GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,tempIds[0]);//������id
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
					attachment[0],					//��ɫ���帽��
					GLES30.GL_TEXTURE_2D,
					tempIds[0], 					//����id
					0								//���
			);
			
			GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,tempIds[1]);//������id
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
					GLES30.GL_R16F, 		//�ڲ���ʽ
					GEN_TEX_WIDTH,			//���
					GEN_TEX_HEIGHT,			//�߶�
					0,						//�߽���
					GLES30.GL_RED, 
	        		GLES30.GL_FLOAT,
					null
			);
			GLES30.glFramebufferTexture2D		//�����Զ���֡�������ɫ���帽��
			(
					GLES30.GL_FRAMEBUFFER,
					attachment[1],					//��ɫ���帽��
					GLES30.GL_TEXTURE_2D,
					tempIds[1], 					//����id
					0								//���
			);
			GLES30.glDrawBuffers(attachment.length, attachment,0);
			
			if(GLES30.GL_FRAMEBUFFER_COMPLETE != 
					GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER))
			{
				return false;
			}
			return true;
		}
		
		public void generateTextImage()//ͨ�����Ʋ�������
		{
			//�����ӿڴ�С��λ��
			GLES30.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
			//��֡����id
			GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);
    		//�����Ȼ�������ɫ����
			GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //����͸��ͶӰ
			MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 300);
			
			//���ô˷������������9����λ�þ���
			MatrixState.setCamera(0, 0, preCZ, 0, 0, preTargetZ, 0, 1, 0);
			//���ǰһ֡�Ĺ۲�-ͶӰ����
			mPreviousProjectionMatrix=MatrixState.getViewProjMatrix();
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0, 0, cz, 0, 0, targetz, 0, 1, 0);
            //��õ�ǰ�Ĺ۲�-ͶӰ����
            float[] mViewProjectionMatrix=new float[16];
            mViewProjectionMatrix=MatrixState.getViewProjMatrix();
            float[] tempM=new float[16];
            //�Ե�ǰ�Ĺ۲�-ͶӰ�����������
            Matrix.invertM(tempM, 0, mViewProjectionMatrix, 0);
            mViewProjectionInverseMatrix=tempM;
            
            //����ɽ��
            MatrixState.pushMatrix();//�����ֳ�
            MatrixState.scale(1, -1, 1);
            mountain.drawSelf(mountainTextId);
            MatrixState.popMatrix();//�ָ��ֳ�
            //���������
            MatrixState.pushMatrix();
            MatrixState.translate(0,-10.0f,0);
            MatrixState.rotate(-90, 0, 1, 0);
            cloud.drawSelf(cloudTextId);
            MatrixState.popMatrix();
            //������1
            drawTrees(1,28f,-30.0f,-40f);
            drawTrees(1,-28f,-15.0f,-30f);
            drawTrees(1,30f,-18.0f,-10f);
            drawTrees(1,29f,-12.0f,0f);
            drawTrees(1,-28f,-37.5f,5f);
            drawTrees(1,-26f,-13.0f,40f);
            drawTrees(1,-30f,-25.0f,20f);
            drawTrees(1,30f,-30.0f,40f);
            drawTrees(1,-26f,-15.0f,52f);
            drawTrees(1,-30f,-20.0f,-45);
            drawTrees(1,30f,-29.0f,-45);
            drawTrees(1,26f,-15.0f,-55);
            drawTrees(1,2f,-16.0f,-55);
            drawTrees(1,-2f,-34.0f,0);
            
            //������2
            drawTrees(2,26f,-18.0f,-50);
            drawTrees(2,-26f,-14.0f,-40);
            drawTrees(2,28f,-13.0f,10);
            drawTrees(2,-30f,-26.0f,-20);
            drawTrees(2,-26f,-15.0f,30);
            drawTrees(2,26f,-10.0f,15);
            drawTrees(2,-28f,-10.0f,45);
            drawTrees(2,-30f,-10.0f,60);
            drawTrees(2,30f,-32.0f,55);
            drawTrees(2,30f,-20.0f,-55);
            drawTrees(2,-26f,-10.0f,-60);
            drawTrees(2,28f,-30.0f,60f);
            drawTrees(2,2f,-25.0f,35f);
		}
		public void drawShadowTexture()//�������ɵľ�������
		{
			//�����ӿڴ�С��λ�� 
			GLES30.glViewport(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
			GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);//��֡����id   	
        	//�����Ȼ�������ɫ����
			GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT |GLES30.GL_COLOR_BUFFER_BIT);
            //����ƽ��ͶӰ
            MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 300);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,0,3.0f,0f,0f,0f,0f,1.0f,0.0f);
            
            MatrixState.pushMatrix();
            tr.drawSelf(tempIds[0],tempIds[1],mPreviousProjectionMatrix,mViewProjectionInverseMatrix,SAMPLENUMBER);//�����������
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
            initFRBuffers();//��ʼ��֡�������Ⱦ����ķ���
            tr=new TextureRect(MySurfaceView.this,ratio);//�������λ��ƶ���
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
            MatrixState.setLightLocation(0, 80, 100);
            //����ɽ�ػ��ƶ���
            yArray=loadLandforms(MySurfaceView.this.getResources(), R.drawable.hd1);
            mountain=new Mountain(MySurfaceView.this,yArray,yArray.length-1,yArray[0].length-1);
            cloud=new Sky_cloud(MySurfaceView.this);
            //�������Ļ��ƶ���
            tree1=LoadUtil.loadFromFile("tree.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            tree2=LoadUtil.loadFromFile("tree01.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            
            mountainTextId=initTexture(R.drawable.grass);
            tree1TextId=initTexture(R.drawable.tree);
            tree2TextId=initTexture(R.drawable.tree1);
            cloudTextId=initTexture(R.drawable.sky_cloud);
            
            new Thread()
            {
            	public void run()
            	{
            		while(true)
            		{
            			preCZ=cz;
                        preTargetZ=targetz;
            			cz-=SPAN;
            			targetz-=SPAN;
            			if(cz<=-35)
            			{
            				cz=100f;
            				targetz=0f;
            				preCZ=cz;
                            preTargetZ=targetz;
            			}
            			try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
            		}
            	}
            }.start();
        }
        
        public void drawTrees(int treeIndex,float transX,float transY,float transZ)
        {
        	if(treeIndex==1)//������1
        	{
        		MatrixState.pushMatrix();//�����ֳ�
        		MatrixState.translate(transX, transY, transZ);
        		 if(tree1!=null){
                 	tree1.drawSelf(tree1TextId);
                 }
        		MatrixState.popMatrix();
        	}else//������2
        	{
        		MatrixState.pushMatrix();//�����ֳ�
        		MatrixState.translate(transX, transY, transZ);
        		 if(tree2!=null){
                 	tree2.drawSelf(tree2TextId);
                 }
        		MatrixState.popMatrix();
        	}
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
	    bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        return textureId;//��������id
	}
}
