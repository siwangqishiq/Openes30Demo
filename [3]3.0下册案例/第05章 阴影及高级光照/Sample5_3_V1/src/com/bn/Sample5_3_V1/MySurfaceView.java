package com.bn.Sample5_3_V1;
import static com.bn.Sample5_3_V1.Constant.SCREEN_HEIGHT;
import static com.bn.Sample5_3_V1.Constant.SCREEN_WIDTH;
import static com.bn.Sample5_3_V1.Constant.SHADOW_TEX_HEIGHT;
import static com.bn.Sample5_3_V1.Constant.SHADOW_TEX_WIDTH;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
 
class MySurfaceView extends GLSurfaceView   
{
    private SceneRenderer mRenderer;//������Ⱦ��         
    //�ƹ�λ��
	float lx=0;
	final float ly=10;
	float lz=45;   
	float lAngle=0;
	final float lR=45;	  
    final float cDis=15;    
    float[] mMVPMatrixGY;//��ԴͶӰ���۲���Ͼ���
	
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES 3.0        
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
        
        
        new Thread()
        {
        	public void run()
        	{
        		while(true) 
        		{
        			lAngle += 0.5;//������x����ת�Ƕ�                    
                    lx=(float) Math.sin(Math.toRadians(lAngle))*lR;
                    lz=(float) Math.cos(Math.toRadians(lAngle))*lR;
                    try {
   					Thread.sleep(40);
	   				} catch (InterruptedException e) {
	   					e.printStackTrace();
	   				}
        		}
        	}
        }.start();
    }

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {  
    	//��ָ����obj�ļ��м��ض���
		LoadedObjectVertexNormal lovo_pm;//ƽ��
		LoadedObjectVertexNormal lovo_ch;//���
		LoadedObjectVertexNormal lovo_cft;//������
		LoadedObjectVertexNormal lovo_qt;//����
		LoadedObjectVertexNormal lovo_yh;//Բ��
		
		TextureRect tr;		
		int frameBufferId;
		int shadowId;//�������������Id
		int renderDepthBufferId;//������Ȼ������Ⱦ�������
		
		//����Ļ�ϻ��ƻ���
        public void onDrawFrame(GL10 gl)
        {    
        	//���ù�Դλ��
        	MatrixState.setLightLocation(lx, ly, lz);        	
        	//ͨ�����Ʋ�����������
            generateShadowImage();
            //���ƾ���������Ļ
            drawShadowTexture();
        }
				
        //ͨ�����Ʋ�����������        
        public void generateShadowImage() 
        {
        	//�����ӿ�
        	GLES30.glViewport(0, 0, SHADOW_TEX_WIDTH, SHADOW_TEX_HEIGHT);  
        	//��֡����
        	GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId); 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            
            //���������
            MatrixState.setCamera(lx,ly,lz,0f,0f,0f,0f,1,0);
            //����͸�Ӿ���
            MatrixState.setProjectFrustum(-1, 1, -1.0f, 1.0f, 1.5f, 400); 
            //��ȡ�����ͶӰ��Ͼ���
            mMVPMatrixGY=MatrixState.getViewProjMatrix();
            
            //�����������ƽ��
            lovo_pm.drawSelfForShadow();  
            
            //��������
            MatrixState.pushMatrix(); 
            MatrixState.translate(-cDis, 0, 0);
            //�����ص����岿λ�����������
            lovo_qt.drawSelfForShadow();
            MatrixState.popMatrix();    
            
            //����Բ��
            MatrixState.pushMatrix();            
            MatrixState.translate(cDis, 0, 0);
            MatrixState.rotate(30, 0, 1, 0);
            //�����ص����岿λ�����������
            lovo_yh.drawSelfForShadow();
            MatrixState.popMatrix();  
            
            //���Ƴ�����
            MatrixState.pushMatrix(); 
            MatrixState.translate(0, 0, -cDis);
            //�����ص����岿λ�����������
            lovo_cft.drawSelfForShadow();
            MatrixState.popMatrix();
            
            //���Ʋ��
            MatrixState.pushMatrix(); 
            MatrixState.translate(0, 0, cDis);
            //�����ص����岿λ�����������
            lovo_ch.drawSelfForShadow();
            MatrixState.popMatrix();     
        }
        
        public void drawShadowTexture(){//������������ֵ���Ļ�ϵķ���
        	//�����ӿڴ�С��λ�� 
        	GLES30.glViewport(0, 0, (int)SCREEN_WIDTH, (int)SCREEN_HEIGHT); 
        	GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, 0);//��֡����
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            GLES30.glClearColor(1,1,1,1);//���ñ���ɫ  
            //���������
            MatrixState.setCamera(0,0,1.5f,0f,0f,-1f,0f,1.0f,0.0f);
			//����ͶӰ
            MatrixState.setProjectOrtho(-0.6f,0.6f, -0.6f, 0.6f, 1, 10);  
            tr.drawSelf(shadowId);//����������ʾ����������������
        }
        
        public void onSurfaceChanged(GL10 gl, int width, int height) 
        {
            //�����ӿڴ�С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	Constant.SCREEN_HEIGHT=height;
        	Constant.SCREEN_WIDTH=width;
        	
        	//��ʼ��֡����
        	initFRBuffers();
        }
       
        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {     	
        	//������Ļ����ɫRGBA
            GLES30.glClearColor(0,0,0,1);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            //��ʼ����Դλ��
            MatrixState.setLightLocation(lx, ly, lz);
            //����Ҫ���Ƶ�����
            lovo_ch=LoadUtil.loadFromFileVertexOnly("ch.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            lovo_pm=LoadUtil.loadFromFileVertexOnly("pm.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            lovo_cft=LoadUtil.loadFromFileVertexOnly("cft.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            lovo_qt=LoadUtil.loadFromFileVertexOnly("qt.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            lovo_yh=LoadUtil.loadFromFileVertexOnly("yh.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            //��ʾ��Ӱ��ͼ���������
            tr=new TextureRect(MySurfaceView.this);
        }
		//��ʼ��֡�������Ⱦ����ķ���
		public void initFRBuffers()
		{
			int[] tia=new int[1];//���ڴ�Ų�����֡����id������
			GLES30.glGenFramebuffers(1, tia, 0);//����һ��֡����id
			frameBufferId=tia[0];//��֡����id��¼����Ա������
        	 
			
			GLES30.glGenRenderbuffers(1, tia, 0);//����һ��֡����id
			renderDepthBufferId=tia[0];//����Ⱦ����id��¼����Ա������
			//��ָ��id����Ⱦ����
			GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, renderDepthBufferId);
			//Ϊ��Ⱦ�����ʼ���洢
        	GLES30.glRenderbufferStorage
        	(
        			GLES30.GL_RENDERBUFFER, 
        			GLES30.GL_DEPTH_COMPONENT16, 
        			SHADOW_TEX_WIDTH, 
        			SHADOW_TEX_HEIGHT
        	);
			
        	
			int[] tempIds = new int[1];//���ڴ�Ų�������id������
    		GLES30.glGenTextures//����һ������id
    		(
    				1,          //����������id������
    				tempIds,   //����id������
    				0           //ƫ����
    		);   
    		
    		shadowId=tempIds[0];//������id��¼����������id��Ա����
    		
    		//��ʼ����ɫ��������
        	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, shadowId);//������id 
        	//����min��mag�Ĳ�����ʽ
        	GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_LINEAR);
    		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
    		//��������s��t������췽ʽ
    		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_CLAMP_TO_EDGE);
    		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_CLAMP_TO_EDGE); 
    		GLES30.glTexImage2D//������ɫ��������ͼ�ĸ�ʽ
        	(
        		GLES30.GL_TEXTURE_2D, 
        		0, //���
        		GLES30.GL_R16F, //�ڲ���ʽ
        		SHADOW_TEX_WIDTH, //���
        		SHADOW_TEX_HEIGHT, //�߶�
        		0, //�߽���
        		GLES30.GL_RED,//��ʽ 
        		GLES30.GL_FLOAT, //ÿ�������ݸ�ʽ
        		null
        	);        	
        	
    		GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, frameBufferId);
            GLES30.glFramebufferTexture2D//�����Զ���֡�������ɫ����
            (
            	GLES30.GL_FRAMEBUFFER, 
            	GLES30.GL_COLOR_ATTACHMENT0,//��ɫ����
            	GLES30.GL_TEXTURE_2D,//����Ϊ2D���� 
            	shadowId, //����id
            	0	//���
            );       
        	GLES30.glFramebufferRenderbuffer//�����Զ���֡�������Ȼ��帽��
        	(
        		GLES30.GL_FRAMEBUFFER, 
        		GLES30.GL_DEPTH_ATTACHMENT,//��Ȼ��帽��
        		GLES30.GL_RENDERBUFFER, //��Ⱦ����
        		renderDepthBufferId//��Ⱦ����id
        	);
		}
    }
}
