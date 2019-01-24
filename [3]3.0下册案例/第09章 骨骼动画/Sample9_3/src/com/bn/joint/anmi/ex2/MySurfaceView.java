package com.bn.joint.anmi.ex2;
import java.io.InputStream;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;
import android.opengl.GLES30;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.bn.ms3d.core.MS3DModel;
import com.bn.ms3d.texutil.TextureManager;
import android.content.Context;
public class MySurfaceView extends GLSurfaceView{
	private final float TOUCH_SCALE_FACTOR = 180.0f/320;//�Ƕ����ű���
    private SceneRenderer mRenderer;//������Ⱦ��	
	private float mPreviousY;//�ϴεĴ���λ��Y����
    private float mPreviousX;//�ϴεĴ���λ��X����
    float cx=0,cy=7,cz=-15;//�����λ��
    float tx=0,ty=2,tz=0;//Ŀ���
    float ux=0,uy=2,uz=0;//UP����
    float angle=45;//���������
    float direction=0;//�������λ��
    final float distance=25;//���������Ŀ���ľ���
	public MySurfaceView(Context context){
        super(context);
        this.setEGLContextClientVersion(2); //����ʹ��OPENGL ES2.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ 
        calCamera(0,0);
    }
	public void calCamera(float dx,float dy) //�����������λ������ת�Ƕ�
	{
		direction += dx * TOUCH_SCALE_FACTOR;	//��λ��
		angle+=dy * TOUCH_SCALE_FACTOR;			//�����
		if(angle<=0)
		{
			angle=0;
		}
		else if(angle>=90)
		{
			angle=90;
		}
        
		//��������
	   	 double angleHD=Math.toRadians(direction);
	   	 float[] cup={-(float) Math.sin(angleHD),0,(float) Math.cos(angleHD),1};
	   	 float[] cLocation={0,distance,0,1};
	   	 
	   	 //����ת������    	 
	   	 float[] zhou={-cup[2],0,cup[0]};   
	   	
	   	 //�������������
	   	 float[] helpM=new float[16];
	   	 Matrix.setIdentityM(helpM, 0);
	   	 Matrix.rotateM(helpM, 0, angle, zhou[0],zhou[1],zhou[2]);
	   	 float[] cupr=new float[4];
	   	 float[] cLocationr=new float[4];
	   	 Matrix.multiplyMV(cupr, 0, helpM, 0, cup, 0);
	   	 Matrix.multiplyMV(cLocationr, 0, helpM, 0, cLocation, 0);
	   	 
	   	 cx=cLocationr[0];//�����λ��
	   	 cy=cLocationr[1];
	   	 cz=cLocationr[2];
	   	 tx=0f;ty=0f;tz=0f;//�۲��λ��
	   	 ux=cupr[0];uy=cupr[1];uz=cupr[2];//up����
	}
	
	//�����¼��ص�����
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
    	float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) 
        {
           case MotionEvent.ACTION_MOVE:
	            float dy = y - mPreviousY;//���㴥�ر�Yλ��
	            float dx = x - mPreviousX;//���㴥�ر�Yλ��
	            calCamera(dx,dy);
           break;
        }
        mPreviousY = y;//��¼���ر�λ��
        mPreviousX = x;//��¼���ر�λ��
        return true;
    }

	private class SceneRenderer implements GLSurfaceView.Renderer{     	
    	TextureManager manager;	//���������
    	MS3DModel ms3d;			//ms3dģ��
    	float time = 0;			//��ǰʱ�䣨���ڶ������ţ�
        public void onDrawFrame(GL10 gl){ 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);        
            //���������
            MatrixState.setCamera(
        		cx,cy,cz, 
            	tx,ty,tz, 
            	ux,uy,uz
            );
            final float span=6.0f;
            final int k=2;
            for(int i=-k;i<=k;i++){
            	for(int j=-k;j<=k;j++){
            		MatrixState.pushMatrix();
            		MatrixState.translate(i*span, 0, j*span);
                    this.ms3d.animate(time); 
                    MatrixState.popMatrix();
            	}}
            //����ģ�Ͷ���ʱ��
            time += 0.015f;
            //����ǰ����ʱ������ܵĶ���ʱ����ʵ�ʲ���ʱ����ڵ�ǰ����ʱ���ȥ�ܵĶ���ʱ��
            if(time > this.ms3d.getTotalTime()) {
            	time = time - this.ms3d.getTotalTime();
            }}  
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1,1000);
            //��ʼ�������任����
            MatrixState.setInitStack();
        }
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�رձ������   
            GLES30.glDisable(GLES30.GL_CULL_FACE);
            //�����������������
            manager = new TextureManager(getResources());
            //��ȡms3d�ļ���������
            InputStream in=null;
            try{
            	in=getResources().getAssets().open("ninja.ms3d");
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            }
            //������������ģ��
    		ms3d = MS3DModel.load(in,manager,MySurfaceView.this); 	
        }
    }
}
