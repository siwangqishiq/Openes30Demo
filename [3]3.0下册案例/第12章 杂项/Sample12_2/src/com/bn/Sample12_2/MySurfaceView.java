package com.bn.Sample12_2;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import android.opengl.GLES11Ext;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.Sample12_2.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.hardware.Camera.Size;

class MySurfaceView extends GLSurfaceView 
{
    private SceneRenderer mRenderer;//������Ⱦ��
    int textureIdCamera;//ϵͳ���������id
    int textureId;
    SurfaceTexture st;//����ͷ���������ߣ������Ǵ�һ��ͼ�����в���ͼ��֡��ΪOpenGL ES����
    Object lock=new Object();//ͬ����
    boolean canUpdate=false;//����ͷ�����Ƿ���Ҫ����
    Camera gCamera;
    float angle=0;
	
	public MySurfaceView(Context context) 
	{
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ
    }
	
	private class SceneRenderer implements GLSurfaceView.Renderer
    {
    	CameraBackDrawer backDrawer;//��������ͷʵʱ�������
    	LoadedObjectVertexNormalTexture lovo;//������ƶ���
    	
        public void onDrawFrame(GL10 gl)
        {
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            
            synchronized(lock)
            {
            	if(canUpdate)
            	{
            		/*updateTexImage�������Եõ�֡��Ϣ�Ļص����������κ��̱߳����ã�
            		 * �����û������Ҫ�ı���������£�
            		 * updateTexImage()��Ӧ��ֱ�Ӵӻص������е��á�
            		 */
            		st.updateTexImage(); //��������ͼ��Ϊ��ͼ��������ȡ�����һ֡��
            		canUpdate=false;
            	}
            }
            //��������ͷʵʱ����
            MatrixState.pushMatrix();
            MatrixState.translate(0,0,-20);
            backDrawer.drawSelf(textureIdCamera);  
            MatrixState.popMatrix();
            
            //���Ʋ��
            MatrixState.pushMatrix();
            MatrixState.translate(0, -0.2f, 0);
            MatrixState.scale(0.02f, 0.02f, 0.02f);
            MatrixState.rotate(18, 1, 0, 0);
            MatrixState.rotate(angle, 0, 1, 0);
            lovo.drawSelf(textureId);
            MatrixState.popMatrix();
            angle=angle+0.5f;
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height);
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 20, 1000);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,0,30,0f,0f,0f,0f,1.0f,0.0f);
            //��ʼ���任����
            MatrixState.setInitStack();
            //��ʼ����Դλ��
            MatrixState.setLightLocation(-40, 10, 20);
            //��ʼ������ͷ����ʼԤ��
            initCamera(width,height);
            //������������ݻ����߶Զ��� 
            backDrawer=new CameraBackDrawer(MySurfaceView.this,ratio,1);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config)
        {
        	//�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.5f,0.5f,0.5f, 1.0f); 
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //��ʼ������ͷר������
            textureIdCamera=initSurfaceTexture();
            //��ʼ������ͷ������
            st=genSurfaceTexture(textureIdCamera);
            //��������
            textureId=initTexture(R.drawable.ghxp);
            //����Ҫ���Ƶ�����
            lovo=LoadUtil.loadFromFile("ch_t.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
        }
    }
	
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
	
  	public int initSurfaceTexture()
  	{
  		//��������id
		int[] textures = new int[1];
		GLES30.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);
		int textureId=textures[0];
		GLES30.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, textureId);
		GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);
		
		return textureId;
  	}

  	public SurfaceTexture genSurfaceTexture(int texId)
  	{
  		SurfaceTexture st=new SurfaceTexture(texId);  
  		st.setDefaultBufferSize(512, 512);//����Ĭ�ϵ�ͼ�񻺳�����С
  		st.setOnFrameAvailableListener
  		(//ע��һ���ص�����������һ֡ͼ���SurfaceTexture����ʱ����
  		 new OnFrameAvailableListener()
  		 {
			@Override
			public void onFrameAvailable(SurfaceTexture surfaceTexture) 
			{
				synchronized(lock)
				{
					canUpdate=true;
				}
			}  			 
  		 }
  		);
  		return st;
  	}

  	public void initCamera(int screenWidth,int screenHeight)
  	{
    	if(gCamera!=null)
    	{
    		gCamera.release();//�ͷ����������	
    		gCamera=null;//������������	
    	}
	
    	gCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);		//�������

    	try 
        {
			gCamera.setPreviewTexture(st);
		}
        catch (IOException e)
		{
			e.printStackTrace();
		}
    	if(gCamera != null)
    	{
    		//����Camera��getParameters������ȡ���ղ���
    	   	Camera.Parameters parameters = gCamera.getParameters();
    	   	//��ȡ��֧�ֵ�Ԥ��ͼƬ�Ĵ�С
        	List<Size> preSize = parameters.getSupportedPreviewSizes();
        	int previewWidth = preSize.get(0).width;//��ȡ���
        	int previewHeight = preSize.get(0).height;//��ȡ�߶�
        	for (int i = 1; i < preSize.size(); i++) {
        		double similarity = Math.abs(((double) preSize.get(i).height / screenHeight)-
        				((double) preSize.get(i).width / screenWidth));
        	 	if (similarity < Math.abs(((double) previewHeight / screenHeight)- ((double) previewWidth / screenWidth))) {
        	 			previewWidth = preSize.get(i).width;
        	 			previewHeight = preSize.get(i).height;
        	 	}
        	}
        	parameters.setPreviewSize(previewWidth, previewHeight); //����Ԥ����С
        	parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);//���ý���ģʽΪ��������
        	gCamera.setParameters(parameters);      //������������Camera
            gCamera.startPreview();		//��ʼԤ��
    	}
  	}
}
