package com.bn.Sample6_6;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.bn.util.LoadUtil;
import com.bn.util.LoadedObjectVertexNormalTexture;
import com.bn.util.MatrixState;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;

public class MySurfaceView extends GLSurfaceView{
	SceneRenderer mRenderer;
	int textureFlagId[] = new int[3];
	int nowId;
	int groundId; 
	float scan = -15;
	float scana = 0;
	float downy;
	LoadedObjectVertexNormalTexture bg;
	public MySurfaceView(Context context)
	{
		super(context);
		this.setEGLContextClientVersion(3);
		mRenderer=new SceneRenderer();
		this.setRenderer(mRenderer);
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				downy = y;
				break;
			case MotionEvent.ACTION_MOVE:
				scana = (y - downy)*0.02f;
				break;
			case MotionEvent.ACTION_UP:
				scan += scana;
				scana = 0;
		}
		return true;
	}
	class SceneRenderer implements GLSurfaceView.Renderer
	{
		TextureRect flag;  
		ParticleControl pc;
		CalThread ct;
		
		long start;
		public void onDrawFrame(GL10 gl)
		{
			FloatBuffer fbt=null;
			synchronized(Constant.lockA)
    		{
				 fbt = Constant.mVertexBufferForFlag;//��ȡ��ǰ����֡��������
    		}	
			//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            MatrixState.setInitStack();
            MatrixState.translate(-0.5f, 0, scan+scana);
            
            MatrixState.pushMatrix();
            flag.drawSelf(fbt,textureFlagId[nowId]); 
            MatrixState.popMatrix();
            
            MatrixState.pushMatrix();
            MatrixState.translate(0, Constant.COLLISIONTOLERANCE-0.05f, 0);//-0.05fΪ�����Ŷ�����ֹ����Ҳ�ڵ�����ʱ˺��
            bg.drawSelf(groundId, 0);
            MatrixState.popMatrix();
		}
		public void onSurfaceChanged(GL10 gl, int width, int height)
		{
			//�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height);
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio*0.4f, ratio*0.4f, -1*0.4f, 1*0.4f, 1, 100);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,1f,3,0f,0f,-1f,0f,1.0f,0.0f);
		}
		public void onSurfaceCreated(GL10 gl, EGLConfig config)
		{
			//������Ļ����ɫRGBA
            GLES30.glClearColor(0.9f,0.9f,1,1.0f);  
            MatrixState.setLightLocation(10, 30, 10);
            //���������ζԶ��� 
            flag = new TextureRect(MySurfaceView.this);   
            pc = new ParticleControl();
            textureFlagId[0]=initTexture(R.drawable.openglflag);
            textureFlagId[1]=initTexture(R.drawable.hzflag);
            textureFlagId[2]=initTexture(R.drawable.android_flag);
            groundId = initTexture(R.drawable.t);
            bg = LoadUtil.loadFromFile("ground.obj",MySurfaceView.this.getResources(), MySurfaceView.this);
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            ct=new CalThread(pc);
            ct.start();
		}			
	}

	
	@Override
	public void onPause() {
		super.onPause();
		mRenderer.ct.flag = false;
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
        //ʵ�ʼ�������
        GLUtils.texImage2D
        (
        		GLES30.GL_TEXTURE_2D,   //�������ͣ���OpenGL ES�б���ΪGL10.GL_TEXTURE_2D
        		0, 					  //����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
        		bitmapTmp, 			  //����ͼ��
        		0					  //����߿�ߴ�
        );
        bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        return textureId;
	}
}
