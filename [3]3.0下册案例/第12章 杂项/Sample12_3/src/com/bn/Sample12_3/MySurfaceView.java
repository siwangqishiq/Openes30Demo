package com.bn.Sample12_3;//������

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.opengl.GLUtils;
import android.os.Environment;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.Sample12_3.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.Config;
import android.graphics.Matrix;

class MySurfaceView extends GLSurfaceView 
{
    private SceneRenderer mRenderer;//������Ⱦ��    
    public static boolean threadFlag=false;
    
    int screenWidth;
    int screenHeight;
    boolean saveFlag=false;
    int textureId;//ϵͳ���������id
	
    int BUTTON_LEFT=1580;
    int BUTTON_RIGHT=1780;
    int BUTTON_UP=450;
    int BUTTON_DOWN=620;
    float ratio;
    float xAngle=0;//��ת�Ƕ�
    float yAngle=0;//��ת�Ƕ�
    
    Matrix matrix = new Matrix();
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
		float x=Constant.fromRealScreenXToStandardScreenX(e.getX());//����ǰ��Ļ����ת��Ϊ��׼��Ļ����
		float y=Constant.fromRealScreenYToStandardScreenY(e.getY());
        switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	if(x>BUTTON_LEFT&&x<BUTTON_RIGHT&&y>BUTTON_UP&&y<BUTTON_DOWN)
        	{
        		setFlag(true);
        	}
        }
        return true;
    }
    public synchronized void setFlag(boolean flag)
    {
    	saveFlag=flag;
    }
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {  
    	//��ָ����obj�ļ��м��صĶ���
		LoadedObjectVertexNormalTexture lovo;
		Triangle texRect;//�������
		int texTriId;
        public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);

            //����ϵ��Զ
            MatrixState.pushMatrix();
            MatrixState.translate(-8f, -8f, -50f);
            //������ת
            MatrixState.rotate(yAngle, 0, 1, 0);
            MatrixState.rotate(xAngle, 1, 0, 0);
            
            //�����ص����岻Ϊ�����������
            if(lovo!=null)
            {
            	lovo.drawSelf(textureId);
            }   
            MatrixState.popMatrix(); 
            
            texRect.drawSelf(texTriId);
            
            if(saveFlag)
            {
            	saveScreen();
            	setFlag(false);
            }
        }  
        public void saveScreen()
        {
            matrix.reset();  
            matrix.setRotate(180); //��ת180��
            matrix.postScale(-1, 1);
        	
        	ByteBuffer cbbTemp = ByteBuffer.allocateDirect(screenWidth*screenHeight*4);
        	GLES30.glReadPixels(0, 0, screenWidth, screenHeight, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, cbbTemp);
        	
        	Bitmap bm =Bitmap.createBitmap(screenWidth, screenHeight, Config.ARGB_8888);
        	bm.copyPixelsFromBuffer(cbbTemp);
        	bm=Bitmap.createBitmap(bm, 0, 0, screenWidth, screenHeight, matrix, true);
        	try
			{
	    		File sd=Environment.getExternalStorageDirectory();
	    		String path=sd.getPath()+"/HappyScreenShot";
	    		File file=new File(path);
	    		if(!file.exists())
	    		{
	    			file.mkdirs();
	    		}
        		File myFile = File.createTempFile
        				(
        						"ScreenShot",  //�����ļ���
        						".png",     //��׺
        						file //Ŀ¼·��
        				);
        		
				 FileOutputStream fout=new FileOutputStream(myFile);
				 BufferedOutputStream bos = new BufferedOutputStream(fout);  
				 bm.compress
				 (
						 Bitmap.CompressFormat.PNG,   //ͼƬ��ʽ
						 100, 						   //Ʒ��0-100
						 bos						   //ʹ�õ������
				  );   
				 bos.flush();   
				 bos.close(); 
				 System.out.println("����ɹ����ļ�����"+myFile.getName());
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("����ʧ�ܣ�"); 
			}
        }
        public void onSurfaceChanged(GL10 gl, int width, int height) {
        	screenWidth=width;
        	screenHeight=height;
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 70);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,0,3,0f,0f,-1f,0f,1.0f,0.0f);
            //����һ���̶߳�ʱ��ת��������
            new Thread()
            {
            	public void run()
            	{
            		while(threadFlag)
            		{
            			//������ת�Ƕ�
            			xAngle=(xAngle+2)%360;
            			yAngle=(yAngle+3)%360;
            			try {
							Thread.sleep(100);
						} catch (InterruptedException e) {				  			
							e.printStackTrace();
						}
            		}
            	}
            }.start();   
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
            MatrixState.setLightLocation(40, 10, 20);
            //����Ҫ���Ƶ�����
            lovo=LoadUtil.loadFromFile("ch_t.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            //��������ͼ
            textureId=initTexture(R.drawable.ghxp);
            //���������ζԶ��� 
            texRect=new Triangle(MySurfaceView.this);
            texTriId=initTexture(R.drawable.screenshot1);
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
