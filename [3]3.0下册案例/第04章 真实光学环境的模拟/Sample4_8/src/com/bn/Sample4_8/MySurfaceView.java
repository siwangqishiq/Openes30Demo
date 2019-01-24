package com.bn.Sample4_8;//������

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import static com.bn.Sample4_8.Constant.*;

public class MySurfaceView extends GLSurfaceView
{	
	private SceneRenderer mRenderer;//������Ⱦ��    
	public Flare flare;//���ζ���
	
	public float lpx;//̫��λ��x����
	public float lpy;//̫��λ��y����
	float preX;//��¼���ص�x����
    float preY;//��¼���ص�y����
	public MySurfaceView(Context context){
		super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
	}
    
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {//�����¼��ص�����         
    	float x=e.getX();//��ȡ���ص�x����
    	float y=e.getY();//��ȡ���ص�y����
    	
    	int action=e.getAction()&MotionEvent.ACTION_MASK;		
		switch(action)
		{
			case MotionEvent.ACTION_DOWN: //����down
				preX=x;//��¼��ǰ���ص�x����
				preY=y;//��¼��ǰ���ص�y����
			break;	
			case MotionEvent.ACTION_MOVE://�ƶ�
				float dx=x-preX;//����x���ƶ�λ��
				float dy=y-preY;//����y���ƶ�λ��				
				CameraUtil.changeDirection(dx*0.1f);//�ı��������λ��
				CameraUtil.changeYj(dy*0.1f);//�ı����������
				preX=x;//��¼��ǰ���ص�x����
				preY=y;//��¼��ǰ���ص�y����
			break;
		}
		return true;//����true
    }
    
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		int[] textureIds=new int[3];
		DrawFlare df;
		TextureRect texRect;//�������
		int[] textureIdA=new int[6];//��պ����������
		@Override
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
          
			textureIds[0]=initTexture(R.drawable.flare1);
			textureIds[1]=initTexture(R.drawable.flare2);
			textureIds[2]=initTexture(R.drawable.flare3);
            
            textureIdA[0]=initTexture(R.raw.skycubemap_back);
            textureIdA[1]=initTexture(R.raw.skycubemap_left);
            textureIdA[2]=initTexture(R.raw.skycubemap_right);
            textureIdA[3]=initTexture(R.raw.skycubemap_down);
            textureIdA[4]=initTexture(R.raw.skycubemap_up);
            textureIdA[5]=initTexture(R.raw.skycubemap_front); 
            
            flare=new Flare(textureIds);
			df=new DrawFlare(MySurfaceView.this);
			//����������ζԶ��� 
            texRect=new TextureRect(MySurfaceView.this);  
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height){
			//�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
        	RATIO = (float) width / height;
        	CameraUtil.init3DCamera();        
        	DIS_MAX=(int)Math.sqrt(RATIO*RATIO+1);        	
		}

		@Override
		public void onDrawFrame(GL10 gl)
		{
			//�����Ȼ�������ɫ����
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            
            //3D���Ʋ���
            MatrixState.setProjectFrustum(-RATIO, RATIO, -1.0f, 1.0f, 2, 1000);//����͸��ͶӰ
            CameraUtil.flush3DCamera();//����3D�����λ��           
            
            //��պ��������ֵ
            final float tzz=0.4f;
            //������պк���
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -UNIT_SIZE+tzz);
            texRect.drawSelf(textureIdA[0]);
            MatrixState.popMatrix();              
            //������պ�ǰ��
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, UNIT_SIZE-tzz);
            MatrixState.rotate(180, 0, 1, 0);
            texRect.drawSelf(textureIdA[5]);
            MatrixState.popMatrix(); 
            //������ǽ
            MatrixState.pushMatrix();
            MatrixState.translate(-UNIT_SIZE+tzz, 0, 0);
            MatrixState.rotate(90, 0, 1, 0);
            texRect.drawSelf(textureIdA[1]);
            MatrixState.popMatrix(); 
            //������ǽ
            MatrixState.pushMatrix();
            MatrixState.translate(UNIT_SIZE-tzz, 0, 0);
            MatrixState.rotate(-90, 0, 1, 0);
            texRect.drawSelf(textureIdA[2]);
            MatrixState.popMatrix();
            //������ǽ
            MatrixState.pushMatrix();
            MatrixState.translate(0, -UNIT_SIZE+tzz, 0);
            MatrixState.rotate(-90, 1, 0, 0);
            texRect.drawSelf(textureIdA[3]);
            MatrixState.popMatrix(); 
            //������ǽ
            MatrixState.pushMatrix();
            MatrixState.translate(0, UNIT_SIZE-tzz, 0);
            MatrixState.rotate(90, 1, 0, 0);
            texRect.drawSelf(textureIdA[4]);
            MatrixState.popMatrix();
            
            //��ȡ��Դ����Ļ�ϵ�����
        	float[] ls=CameraUtil.calLightScreen(RATIO);//�����ڵ�ǰ������۲�����¹�Դ�����Ļ����
			lpx=ls[0];//��ȡ̫��λ��x����
			lpy=ls[1];//��ȡ̫��λ��y����
			         
			if(lpx>RATIO||lpy>1)
			{//̫������Ļ
				return;//����
			}
			flare.update(lpx, lpy);//���¹��λ���λ��
			
			//ƽ��ͶӰ���Ʋ���
            MatrixState.setProjectOrtho(-RATIO, RATIO, -1.0f, 1.0f, 2, 1000);//����ƽ��ͶӰ
            MatrixState.setCamera(0,0,0, 0,0,-1, 0,1,0); //���������
			//���ƹ���
            MatrixState.pushMatrix();//�����ֳ�
            
            GLES30.glEnable(GLES30.GL_BLEND);//�򿪻��
            GLES30.glBlendFunc(GLES30.GL_SRC_COLOR, GLES30.GL_ONE);//���û������
            for(SingleFlare ss:flare.sFl)
            {//ѭ����������Ԫ���б�-���л���
            	MatrixState.pushMatrix();//�����ֳ�
            	MatrixState.translate(ss.px, ss.py, -100+ss.distance);//ƽ�Ƶ�ָ��λ��
            	MatrixState.scale(ss.bSize, ss.bSize, ss.bSize);//����������
            	df.drawSelf(ss.texture,ss.color);//���ƹ���Ԫ��
            	MatrixState.popMatrix();//�ָ��ֳ�
            }
            GLES30.glDisable(GLES30.GL_BLEND);//�رջ��
            
            MatrixState.popMatrix(); //�ָ��ֳ�
     
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
