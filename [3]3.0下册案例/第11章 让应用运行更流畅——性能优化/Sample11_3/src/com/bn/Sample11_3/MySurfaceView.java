package com.bn.Sample11_3;//������
import java.io.IOException;
import java.io.InputStream;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.util.Log;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��    
    private float mPreviousX;//�ϴεĴ���λ��X����
    
    int textureId;//ϵͳ���������id
    int textureRDId;//�Ŷ�����id
    int textureJBId;//С����ɫ��������id
    float cAngle=0;//�����ת���ĽǶ�
  	
    float num=40000;//С�ݵ��ܿ���
	public  float r=(float) Math.sqrt(num)/4-1;	//�������Ŀ���ľ��룬���������ת�İ뾶
  	//�����Ŀ��������
  	public  float targetX=r;
  	public  float targetY=0;
  	public  float targetZ=r;
  	
  	//�������up����
  	public  float upX=0;
  	public  float upY=1;
  	public  float upZ=0;
  	public  float ANGLE_MIN=-360;//�������ת�ĽǶȷ�Χ����Сֵ
  	public  float ANGLE_MAX=360;//�������ת�ĽǶȷ�Χ�����ֵ
  	
  	//������Ĺ۲�������
  	public  float CameraX=r;
  	public  float CameraY=6;
  	public  float CameraZ=r;
  	
  	int uTexHandle;//С��������������id
  	int uJBTexHandle;//��ɫ������������id
  	public MySurfaceView(Context context) {
  		super(context);
  		this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
  		mRenderer = new SceneRenderer();	//����������Ⱦ��
  		setRenderer(mRenderer);				//������Ⱦ��		        
  		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
  	}
  	
  	//�����¼��ص�����
  	@Override 
  	public boolean onTouchEvent(MotionEvent e) 
    {
    	float x = e.getX();
    	switch (e.getAction()) 
    	{
    	case MotionEvent.ACTION_MOVE:
    		float dx = x - mPreviousX;//���㴥�ص�Xλ��
    		cAngle += dx*TOUCH_SCALE_FACTOR;	
    	}
    	mPreviousX = x;//��¼���ص�λ��
    	calculateCamera(cAngle);//����������Ĺ۲��
    	
    	return true;
    }
    
    public  void calculateCamera(float angle)
	{		
		//����������۲��ߵ�����
    	CameraX = (float) (r*Math.sin(Math.toRadians(angle)))+targetX;
    	CameraZ = (float) (r*Math.cos(Math.toRadians(angle)))+targetZ;
    	//�����������9����
    	MatrixState.setCamera(CameraX,CameraY,CameraZ,  targetX,targetY,targetZ,  upX,upY,upZ);
	}

	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
    	//��ָ����obj�ļ��м��صĶ���
		GrassObject lovo;
		
		//����ˢ֡Ƶ�ʵĴ���
		long olds;
		long currs;
		
		public void onDrawFrame(GL10 gl) 
		{
			//ע�͵���Ϊ����ˢ֡Ƶ�ʵĴ���
			currs=System.nanoTime();
			Log.d("FPS", (1000000000.0/(currs-olds)+"FPS"));
			olds=currs;
			//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
          
            //�����ص����岻Ϊ�����������
            if(lovo!=null)
            {
            	lovo.drawSelf(textureId,textureRDId,textureJBId,(int)num);
            }                
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100000);
            calculateCamera(cAngle);//����������Ĺ۲�㲢�����������λ��
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) 
        {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.5f,0.5f,0.5f, 1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //����Ҫ���Ƶ�����
            lovo=LoadUtil.loadFromFile("grass.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            //С������ͼ
            textureId=initTexture(R.drawable.grass);
            //�Ŷ�����ͼ
            textureRDId=initTexture(R.drawable.noise2);
            //С����ɫ��������ͼ
            textureJBId=initTexture(R.drawable.jb);
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
