package com.bn.Sample12_1;
import java.io.IOException;
import java.io.InputStream;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Build;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class MySurfaceView extends GLSurfaceView
{
    private SceneRenderer mRenderer;//������Ⱦ��
	
    public static final int drawSample=3;//�ڵ���ѯ������
    
	//���ε�λ��
	static float rectX;
	static float rectY;
	static final float moveSpan = 0.1f;
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
    	//��ָ����obj�ļ��м��ض���
		LoadedObjectVertexNormalFace cft;//��������ƶ���
		LoadedObjectVertexNormalAverage qt;//������ƶ���
		LoadedObjectVertexNormalAverage ch;//������ƶ���
		
        @SuppressLint("InlinedApi")
		@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
		public void onDrawFrame(GL10 gl)
        {
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            
            int[] tia=new int[drawSample];//�������ָ����ѯid������
            GLES30.glGenQueries(drawSample, tia, 0);//����ָ�������Ĳ�ѯ����
            
            MatrixState.pushMatrix();//�����ֳ�
            MatrixState.scale(1.5f, 1.5f, 1.5f);//��������
            
            MatrixState.pushMatrix();//�����ֳ�
            MatrixState.translate(0, 0, 10f);//ƽ��
            ch.drawSelf();//���Ʋ��
            MatrixState.popMatrix();//�ָ��ֳ�
            
            GLES30.glBeginQuery(GLES30.GL_ANY_SAMPLES_PASSED, tia[0]);//�����ڵ���ѯ
            MatrixState.pushMatrix();//�����ֳ�
            MatrixState.translate(-10f, 0f, 0);//ƽ��
            cft.drawSelf();//���Ƴ�����
            MatrixState.popMatrix(); //�ָ��ֳ�
            GLES30.glEndQuery(GLES30.GL_ANY_SAMPLES_PASSED);//�����ڵ���ѯ
            
            GLES30.glBeginQuery(GLES30.GL_ANY_SAMPLES_PASSED, tia[1]);//�����ڵ���ѯ
            MatrixState.pushMatrix();//�����ֳ�
            MatrixState.translate(0f, 0f, 0);//ƽ��
            qt.drawSelf();//��������
            MatrixState.popMatrix();//�ָ��ֳ�
            GLES30.glEndQuery(GLES30.GL_ANY_SAMPLES_PASSED);//�����ڵ���ѯ

            GLES30.glBeginQuery(GLES30.GL_ANY_SAMPLES_PASSED, tia[2]);//�����ڵ���ѯ
            MatrixState.pushMatrix();//�����ֳ�
            MatrixState.translate(15f, 0f, 0);//ƽ��
            qt.drawSelf();//��������
            MatrixState.popMatrix();//�ָ��ֳ�
            GLES30.glEndQuery(GLES30.GL_ANY_SAMPLES_PASSED); //�����ڵ���ѯ
            MatrixState.popMatrix();//�ָ��ֳ�

        	for(int i=0;i<tia.length;i++)
        	{//ѭ����ȡ�ڵ���ѯ���
        		int[] result=new int[1];
        		//��ѯ�����Ƿ��ڵ� 0-��ȫ���ڵ� 1-����ȫ���ڵ�
        		////��ȡ������鿴�����Ƿ��ڵ�
        		GLES30.glGetQueryObjectuiv(tia[i],GLES30.GL_QUERY_RESULT,result,0);
        		System.out.println("i:"+i+" result:"+result[0]);
        	}

            GLES30.glDeleteQueries(drawSample, tia, 0);//ɾ����ѯ����
        }
        @SuppressLint("NewApi")
		public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            //����cameraλ��
            MatrixState.setCamera
            (
            		0,//����λ�õ�X
            		0,//����λ�õ�Y
            		50,//����λ�õ�Z
            		0,//�����򿴵ĵ�X
            		0,//�����򿴵ĵ�Y
            		0,//�����򿴵ĵ�Z
            		0,//upλ��
            		1, 
            		0
            );
            //��ʼ����Դλ��
            MatrixState.setLightLocation(100, 100, 100);
        }
        @SuppressLint("NewApi")
		@Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.3f,0.3f,0.3f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            //����Ҫ���Ƶ�����
            ch=LoadUtil.loadFromFileVertexOnlyAverage("ch.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
    		cft=LoadUtil.loadFromFileVertexOnlyFace("cft.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
    		qt=LoadUtil.loadFromFileVertexOnlyAverage("qt.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
        }
    }

	
	@SuppressLint("NewApi")
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
        		GLES30.GL_TEXTURE_2D,	//�������ͣ���OpenGL ES�б���ΪGLES30.GL_TEXTURE_2D
        		0,						//����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp,				//����ͼ��
        		0						//����߿�ߴ�
        );
        bitmapTmp.recycle();			//������سɹ����ͷ�ͼƬ
        
        return textureId;
	}
}
