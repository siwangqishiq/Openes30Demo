package com.bn.Sample5_2;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("NewApi") class MySurfaceView extends GLSurfaceView 
{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��    
    
    private float mPreviousY;//�ϴεĴ���λ��Y����
    private float mPreviousX;//�ϴεĴ���λ��X����
	//����������ı���
	float cx=0;//�����xλ��
	float cy=0;//�����yλ��
	float cz=60;//�����zλ��
	
	float tx=0;//Ŀ���xλ��
	float ty=0;//Ŀ���yλ��
	float tz=0;//Ŀ���zλ��
	public float currSightDis=60;//�������Ŀ��ľ���
	float angdegElevation=30;//����
	public float angdegAzimuth=180;//��λ��
	
	//���ڵƹ�ı���
	float lx=0;//xλ��
	float ly=0;//yλ��
	float lz=0;//zλ��
	float lightDis=100;
	float lightElevation=40;//�ƹ�����
	public float lightAzimuth=180;//�ƹ�ķ�λ��	
	public MySurfaceView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	
	//�����¼��ص�����
    @SuppressLint("ClickableViewAccessibility") @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
        case MotionEvent.ACTION_MOVE:
            float dy = y - mPreviousY;//���㴥�ر�Yλ��
            float dx = x - mPreviousX;//���㴥�ر�Xλ��
            angdegAzimuth += dx * TOUCH_SCALE_FACTOR;//������x����ת�Ƕ�
            angdegElevation += dy * TOUCH_SCALE_FACTOR;//������z����ת�Ƕ�
            //������������5��90�ȷ�Χ��
            angdegElevation = Math.max(angdegElevation, 5);
            angdegElevation = Math.min(angdegElevation, 90);
            //�����������λ��
            setCameraPostion();
        }
        mPreviousY = y;//��¼���ر�λ��
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }
    // ���������λ�õķ���
	public void setCameraPostion() {
		//�����������λ��
		double angradElevation = Math.toRadians(angdegElevation);// ���ǣ����ȣ�
		double angradAzimuth = Math.toRadians(angdegAzimuth);// ��λ��
		cx = (float) (tx - currSightDis * Math.cos(angradElevation)	* Math.sin(angradAzimuth));
		cy = (float) (ty + currSightDis * Math.sin(angradElevation));
		cz = (float) (tz - currSightDis * Math.cos(angradElevation) * Math.cos(angradAzimuth));
	}
	// λ�õƹ�λ�õķ���
	public void setLightPostion() {
		//����ƹ��λ��
		double angradElevation = Math.toRadians(lightElevation);// ���ǣ����ȣ�
		double angradAzimuth = Math.toRadians(lightAzimuth);// ��λ��
		lx = (float) (- lightDis * Math.cos(angradElevation)	* Math.sin(angradAzimuth));
		ly = (float) (+ lightDis * Math.sin(angradElevation));
		lz = (float) (- lightDis * Math.cos(angradElevation) * Math.cos(angradAzimuth));
	}
	@SuppressLint("NewApi") private class SceneRenderer implements GLSurfaceView.Renderer 
    {
    	//��ָ����obj�ļ��м��ض���
		LoadedObjectVertexNormalFace pm;
		LoadedObjectVertexNormalFace cft;
		LoadedObjectVertexNormalAverage qt;
		LoadedObjectVertexNormalAverage yh;
		LoadedObjectVertexNormalAverage ch;
    	
        public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //����cameraλ��
            MatrixState.setCamera
            (
            		cx,   //����λ�õ�X
            		cy, 	//����λ�õ�Y
            		cz,   //����λ�õ�Z
            		tx, 	//�����򿴵ĵ�X
            		ty,   //�����򿴵ĵ�Y
            		tz,   //�����򿴵ĵ�Z
            		0, 	//upλ��
            		1, 
            		0
            );
            //��ʼ����Դλ��
            MatrixState.setLightLocation(lx, ly, lz);                    
            //�����ص����岿λ�����������            
            pm.drawSelf(0);//����ƽ��
            drawObject(1);//����ƽ���ϸ����������Ӱ
            
            drawObject(0);//�������屾��
           
        } 
        
        public void drawObject(int situ)//����ƽ��������ķ���   //���ݲ����Ĳ�ͬ����������Ӱ�������屾��
        {
            //���Ƴ�����
            MatrixState.pushMatrix();
            MatrixState.scale(1.5f, 1.5f, 1.5f);//�������ű任
            MatrixState.translate(-10f, 0f, 0);//����ƽ�Ʊ任
            cft.drawSelf(situ);//���Ƴ�����
            MatrixState.popMatrix();   //�ָ��ֳ�
            //��������
            MatrixState.pushMatrix();
            MatrixState.scale(1.5f, 1.5f, 1.5f);
            MatrixState.translate(10f, 0f, 0);
            qt.drawSelf(situ);
            MatrixState.popMatrix();  
            //����Բ��
            MatrixState.pushMatrix();
            MatrixState.scale(1.5f, 1.5f, 1.5f);
            MatrixState.translate(0, 0, -10f);
            yh.drawSelf(situ);
            MatrixState.popMatrix();  
            //���Ʋ��
            MatrixState.pushMatrix();
            MatrixState.scale(1.5f, 1.5f, 1.5f);
            MatrixState.translate(0, 0, 10f);
            ch.drawSelf(situ);
            MatrixState.popMatrix(); 
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            //�����������λ��
            setCameraPostion();
            //����ƹ��λ��
            setLightPostion();
            new Thread(){
            	@Override
            	public void run(){
            		while(true){
                		lightAzimuth +=1;
                		lightAzimuth %= 360;
                		//����ƹ��λ��
                        setLightPostion();
                		try {
    						Thread.sleep(50);
    					} catch (InterruptedException e) {
    						e.printStackTrace();
    					}
            		}
            	}
            }.start();
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.3f,0.3f,0.3f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);  
            //�رձ������
            GLES30.glDisable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();            
            //����Ҫ���Ƶ�����
            ch=LoadUtil.loadFromFileVertexOnlyAverage("ch.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            pm=LoadUtil.loadFromFileVertexOnlyFace("pm.obj", MySurfaceView.this.getResources(),MySurfaceView.this);;
    		cft=LoadUtil.loadFromFileVertexOnlyFace("cft.obj", MySurfaceView.this.getResources(),MySurfaceView.this);;
    		qt=LoadUtil.loadFromFileVertexOnlyAverage("qt.obj", MySurfaceView.this.getResources(),MySurfaceView.this);;
    		yh=LoadUtil.loadFromFileVertexOnlyAverage("yh.obj", MySurfaceView.this.getResources(),MySurfaceView.this);;
        }
    }
}
