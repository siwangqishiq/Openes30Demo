package com.bn.Sample3_7;
import java.io.IOException;
import java.io.InputStream;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class MySurfaceView extends GLSurfaceView 
{
    private SceneRenderer mRenderer;//������Ⱦ��  	
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		//��ά����
		Object pmBase[]=new Object[2];
		ObjectToon pmJJ[]=new ObjectToon[2];
		int ObjectTexId[]=new int[2];//����ͼƬid
        public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);            
            MatrixState.pushMatrix(); 
            MatrixState.translate(-6, 4.5f, 0); 
            pmBase[0].drawSelf(ObjectTexId[0]);//ԭ����ˮ��
            MatrixState.popMatrix();    
            
            MatrixState.pushMatrix();  
            MatrixState.translate(6,4.5f, 0); 
            pmJJ[0].drawSelf(ObjectTexId[0]);//�����ֻ������ˮ��
            MatrixState.popMatrix(); 
            
            
            MatrixState.pushMatrix();  
            MatrixState.translate(-6,-5f, 0); 
            pmBase[1].drawSelf(ObjectTexId[1]);//ԭ���廨
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();  
            MatrixState.translate(6,-5f, 0); 
            pmJJ[1].drawSelf(ObjectTexId[1]);//�����ֻ�����廨
            MatrixState.popMatrix();
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //����cameraλ��
            MatrixState.setCamera
            (
            		0,	//����λ�õ�X
            		0, //����λ�õ�Y
            		20, //����λ�õ�Z
            		0, 	//�����򿴵ĵ�X
            		0,  //�����򿴵ĵ�Y
            		0,  //�����򿴵ĵ�Z
            		0, 	//up����
            		1, 
            		0
            );
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
        }
        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
        	GLES30.glClearColor(1.0f,1.0f,1.0f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();  
            for(int i=0;i<2;i++)
            {
            	pmBase[i] = new Object(MySurfaceView.this,9, 9, 1, 1);
            	pmJJ[i] = new ObjectToon(MySurfaceView.this,9, 9, 1, 1);
            }
    		ObjectTexId[0]=initTexture(R.drawable.object1);//ˮ��
    		ObjectTexId[1]=initTexture(R.drawable.object);//��
        }
    }

	//��ʼ������
	public int initTexture(int drawableId)
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
		//��Mipmap����������˲���	
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		
		//ST�����������췽ʽ
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
        //ʵ�ʼ�������,����������������ͼƬ��ʽ�����⣬���׳�ͼƬ��ʽ�쳣�����ٻ�����ʾ�����쳣
	   	GLUtils.texImage2D
	    (
	    		GLES30.GL_TEXTURE_2D, //��������
	     		0, 
	     		GLUtils.getInternalFormat(bitmapTmp), 
	     		bitmapTmp, //����ͼ��
	     		GLUtils.getType(bitmapTmp), 
	     		0 //����߿�ߴ�
	     );   
        //�Զ�����Mipmap����
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D);
        //�ͷ�����ͼ
        bitmapTmp.recycle();
        //��������ID
        return textureId;
	}	
}
