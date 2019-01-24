package com.bn.Sample5_11;

import java.io.IOException;
import java.io.InputStream;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.Sample5_11.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import static com.bn.Sample5_11.Constant.*;

@SuppressLint("NewApi") 
class MySurfaceView extends GLSurfaceView
{
    private SceneRenderer mRenderer;//������Ⱦ��
    float ratio;
    int SCREEN_WIDTH;
    int SCREEN_HEIGHT;
    
    float cz=100f;
    float targetz=0;
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
    	
		int[] frameBufferIds=new int[5];//֡����id
		int[] renderDepthBufferIds=new int[5];//��Ⱦ��Ȼ���id
		int[] textureIds=new int[5];//������ɵ�����id
		
		int mountainTextId;//ɽ������id
		int tree1TextId;//��һ����������id
		int tree2TextId;//�ڶ�����������id
		int cloudTextId;//�������id
		TextureRect tr;//���λ��ƶ���
		
		public void initFRBuffers()//��ʼ��֡�������Ⱦ����ķ���
		{
			int tia[]=new int[5];//���ڴ�Ų�����֡����id������
			GLES30.glGenFramebuffers(tia.length, tia, 0);//����һ��֡����id
			frameBufferIds=tia;
			
			GLES30.glGenRenderbuffers(tia.length, tia, 0);//����һ����Ⱦ����id
			renderDepthBufferIds=tia;//����Ⱦ����id��¼����Ա������
			
			int[] tempIds = new int[5];//���ڴ�Ų�������id������
			GLES30.glGenTextures//����һ������id
    		(
    				5,         //����������id������
    				tempIds,   //����id������
    				0           //ƫ����
    		);
			textureIds=tempIds;//������id��¼����Ա����
			for(int i=0;i<textureIds.length;i++)
			{
				//��֡����id
				GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferIds[i]);
				//��ָ��id����Ⱦ����
				GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderDepthBufferIds[i]);
				//Ϊ��Ⱦ�����ʼ���洢
				GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER, 
						GLES30.GL_DEPTH_COMPONENT16,GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
				
				GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,textureIds[i]);//������id
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
	            	textureIds[i], 					//����id
	            	0								//���
	            );
				GLES30.glFramebufferRenderbuffer	//�����Զ���֡�������Ȼ��帽��
	        	(
	        		GLES30.GL_FRAMEBUFFER,
	        		GLES30.GL_DEPTH_ATTACHMENT,		//��Ȼ��帽��
	        		GLES30.GL_RENDERBUFFER,			//��Ⱦ����
	        		renderDepthBufferIds[i]			//��Ⱦ��Ȼ���id
	        	);
			}
		}
		
		public void generateTextImage()//ͨ�����Ʋ�������
		{
			float czCurrTemp=cz;
			float targetzCurrTemp=targetz;
			for(int i=0;i<frameBufferIds.length;i++)
			{
				//�����ӿڴ�С��λ��
				GLES30.glViewport(0, 0, GEN_TEX_WIDTH, GEN_TEX_HEIGHT);
				//��֡����id
				GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferIds[i]);
	    		//�����Ȼ�������ɫ����
				GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
	            //����͸��ͶӰ
				MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 300);
	            //���ô˷������������9����λ�þ���
	            MatrixState.setCamera(0, 0, czCurrTemp-TIMESPAN*i, 0, 0, targetzCurrTemp-TIMESPAN*i, 0, 1, 0);
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
		}
		
		public void drawSceneTexture()//�������ɵľ�������
		{
			//�����ӿڴ�С��λ�� 
			GLES30.glViewport(0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
			GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);//��֡����id   	
			//�����Ȼ�������ɫ����
			GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT |GLES30.GL_COLOR_BUFFER_BIT);
			//����ƽ��ͶӰ
			MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 2, 100);
			//���ô˷������������9����λ�þ���
			MatrixState.setCamera(0,0,3,0f,0f,0f,0f,1.0f,0.0f);
			MatrixState.pushMatrix();
			tr.drawSelf(textureIds[0],textureIds[1],textureIds[2],textureIds[3],textureIds[4]);//�����������
			MatrixState.popMatrix();
		}
        public void onDrawFrame(GL10 gl) 
        {
        	generateTextImage();//ͨ�����Ʋ�����������
        	drawSceneTexture();//���ƾ�������
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
        	GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);  
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
            
            new Thread(){
            	public void run(){
            		while(true){
            			cz-=SPAN;
            			targetz-=SPAN;
            			if(cz<=-35){
            				cz=95;
            				targetz=0f;
            			}
            			try {Thread.sleep(20);
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
