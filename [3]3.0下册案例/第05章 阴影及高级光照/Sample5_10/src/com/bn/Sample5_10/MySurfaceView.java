package com.bn.Sample5_10;
import java.io.IOException;
import java.io.InputStream;

import static com.bn.Sample5_10.Constant.*;
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

public class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��        
    static final int GEN_TEX_WIDTH=1024;
    static final int GEN_TEX_HEIGHT=1024;
    private float mPreviousX;//�ϴεĴ���λ��X����
    private float mPreviousY;//�ϴεĴ���λ��X����
    float xAngle=0;
    float yAngle=0;
	//����������Ĺ۲���ͶӰ��Ͼ���
    float[] mMVPMatrixMirror;
    int waterId;//ˮ�����������id
    int textureIdNormal;//ϵͳ���������id
	TextureRect waterReflect;//����ˮ����������
	
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
      float y = e.getY();
      switch (e.getAction()) 
      {
      case MotionEvent.ACTION_MOVE:
			float dx = x - mPreviousX;//���㴥�ص�Xλ��
	    	float dy = y - mPreviousY;//���㴥�ص�Yλ��
			if(Math.abs(dx)<=0.02f||Math.abs(dy)<=0.02f){
				break;
			}
			xAngle += dx*TOUCH_SCALE_FACTOR;
	    	yAngle += dy*TOUCH_SCALE_FACTOR;
			if(xAngle<XANGLE_MIN)
			{//����x����ת�Ƕȵ���Сֵ
				xAngle=XANGLE_MIN;
			}
			else if(xAngle>XANGLE_MAX)
			{//����x����ת�Ƕȵ����ֵ
				xAngle=XANGLE_MAX;
			}		
			
			if(yAngle<YANGLE_MIN)
			{//����y����ת�Ƕȵ���Сֵ
    		  yAngle=YANGLE_MIN;
			}
			else if(yAngle>YANGLE_MAX)
			{//����y����ת�Ƕȵ����ֵ
				yAngle=YANGLE_MAX;
			}	
			//������������;���������Ĳ���
			calculateMainAndMirrorCamera(xAngle,yAngle);
		}
		mPreviousX=x;//��¼���ص�X����
		mPreviousY=y;//��¼���ص�Y����
      return true;
  }
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		int waterReflectId;// ��̬������ˮ�浹Ӱ����Id
		int frameBufferId;//֡����id
		int renderDepthBufferId;//��Ⱦ��Ȼ���id
    	//��ָ����obj�ļ��м��ض���
		LoadedObjectVertexNormalTexture house1;//������ƶ���
		LoadedObjectVertexNormalTexture qiao;//������ƶ���
		LoadedObjectVertexNormalTexture tong;//������ƶ���
		LoadedObjectVertexNormalTexture tree0;//������ƶ���
		LoadedObjectVertexNormalTexture skyBox;//��պ�
		LoadedObjectVertexNormalTexture table;//����
		LoadedObjectVertexNormalTexture woodPile;//ľͷ
		LoadedObjectVertexNormalTexture mushRoom5;//Ģ��
		LoadedObjectVertexNormalTexture fb;//Ģ��
		LoadedObjectVertexNormalTexture flower;

		int house1Id;
		int qiaoId;
		int tongId;
		int tree0Id;
		int skyBoxId;
		int tableId;
		int woodPileId;
		int mushRoom5Id;
		int fbId;
		int flowerId;
		
    	
        public void onDrawFrame(GL10 gl) 
        {
        	generateTextImage();
        	DrawMirrorNegativeTexture();
        }
        
        public void initFRBuffers()
        {
				int tia[]=new int[1];//���ڴ�Ų�����֡����id������
				GLES30.glGenFramebuffers(tia.length, tia, 0);//����һ��֡����id
				frameBufferId=tia[0];//��֡����id��¼����Ա������

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
				waterReflectId=tempIds[0];//������id��¼����Ա����
				GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,waterReflectId);//������id
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
	    		//��֡����id
				GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);
				GLES30.glBindTexture(GLES30.GL_TEXTURE_2D,waterReflectId);//������id
				GLES30.glFramebufferTexture2D		//�����Զ���֡�������ɫ���帽��
	            (
	            	GLES30.GL_FRAMEBUFFER,
	            	GLES30.GL_COLOR_ATTACHMENT0,	//��ɫ���帽��
	            	GLES30.GL_TEXTURE_2D,
	            	waterReflectId, //����id
	            	0//���
	            );   		
				GLES30.glFramebufferRenderbuffer//�����Զ���֡�������Ȼ��帽��
	        	(
	        		GLES30.GL_FRAMEBUFFER,
	        		GLES30.GL_DEPTH_ATTACHMENT,//��Ȼ��帽��
	        		GLES30.GL_RENDERBUFFER,//��Ⱦ����
	        		renderDepthBufferId//��Ⱦ��Ȼ���id
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
        public void DrawMirrorNegativeTexture()
        {     	
        	//�����ӿڴ�С
        	GLES30.glViewport(0, 0,SCREEN_WIDTH,SCREEN_HEIGHT);
        	GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);//��ϵͳĬ�ϵ�֡����id   	
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //�������������9����
            MatrixState.setCamera(mainCameraX,mainCameraY,mainCameraZ,  targetX,targetY,targetZ,  upX,upY,upZ);
        	//���ô˷����������͸��ͶӰ����
        	MatrixState.setProjectFrustum(left, right, bottom, top, near, far);

        	drawThings();           
            
            //���ƾ���
            MatrixState.pushMatrix();
            MatrixState.translate(0,0,22);
            waterReflect.drawSelf(waterReflectId,waterId,textureIdNormal,mMVPMatrixMirror);
            MatrixState.popMatrix();
        }
        
        //���Ƴ���������������
        public void drawThings()
        {
        	//������պ�
            MatrixState.pushMatrix();
            skyBox.drawSelf( skyBoxId);
            MatrixState.popMatrix();
        	
        	//���Ʒ���
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -66.5f);
            house1.drawSelf( house1Id);
            MatrixState.popMatrix();
            
        	//������
            MatrixState.pushMatrix();
            MatrixState.translate(-25, 5, -15);
            qiao.drawSelf( qiaoId);
            MatrixState.popMatrix();
            
            
        	//����Ͱ
            MatrixState.pushMatrix();
            MatrixState.translate(15, -0.5f, 16);
            tong.drawSelf( tongId);
            MatrixState.popMatrix();
            
        	//������
            MatrixState.pushMatrix();
            MatrixState.translate(40, 0, -15);
            tree0.drawSelf( tree0Id);
            MatrixState.popMatrix();
            
            
        	//��������
            MatrixState.pushMatrix();
            MatrixState.translate(-25, -0.5f, 5);
            table.drawSelf( tableId);
            MatrixState.popMatrix();
            
        	//����ľͷ
            MatrixState.pushMatrix();
            MatrixState.translate(23, -0.5f, 10);
            woodPile.drawSelf( woodPileId);
            MatrixState.popMatrix();
            
        	//����Ģ��
            MatrixState.pushMatrix();
            MatrixState.translate(46, 0, -43);
            mushRoom5.drawSelf( mushRoom5Id);
            MatrixState.popMatrix();
            
            //���Ʒ���
            MatrixState.pushMatrix();
            MatrixState.translate(-50, 0, -50);
            fb.drawSelf( fbId);
            MatrixState.popMatrix();
            
            
            //���ƻ���
            MatrixState.pushMatrix();
            MatrixState.translate(30, 0, -43);
            flower.drawSelf(flowerId);
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            MatrixState.translate(35, 0, -43);
            flower.drawSelf(flowerId);
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
        	//��ʼ������ⷽ��
        	MatrixState.setLightLocation(10,350,250);
        	initFRBuffers();   //��ʼ��֡����     	       
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
            house1=LoadUtil.loadFromFile("house1.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            qiao=LoadUtil.loadFromFile("qiao.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            tong=LoadUtil.loadFromFile("tong.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            tree0=LoadUtil.loadFromFile("tree0.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            skyBox=LoadUtil.loadFromFile("skybox.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            table=LoadUtil.loadFromFile("table.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            woodPile=LoadUtil.loadFromFile("woodpile.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            mushRoom5=LoadUtil.loadFromFile("mushroom5.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            fb=LoadUtil.loadFromFile("fb.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            flower=LoadUtil.loadFromFile("flower1.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            waterReflect=new TextureRect(MySurfaceView.this);
            //��������
            house1Id=initTexture(R.drawable.house1);
            qiaoId=initTexture(R.drawable.qiao);
            tongId=initTexture(R.drawable.stuff02);
            tree0Id=initTexture(R.drawable.tree0);
            skyBoxId=initTexture(R.drawable.sky);
            tableId=initTexture(R.drawable.tree0);
            woodPileId=initTexture(R.drawable.stuff01);
            mushRoom5Id=initTexture(R.drawable.vegetation01);
            waterId=initTexture(R.drawable.water);
            textureIdNormal=initTexture(R.drawable.resultnt);    
            fbId=initTexture(R.drawable.stuff01);    
            flowerId=initTexture(R.drawable.vegetation01);    
             //��ʼ����������;���������Ĳ���
            calculateMainAndMirrorCamera(0,15);
            
            UpdateThread ut=new UpdateThread(MySurfaceView.this);
            ut.start();
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
