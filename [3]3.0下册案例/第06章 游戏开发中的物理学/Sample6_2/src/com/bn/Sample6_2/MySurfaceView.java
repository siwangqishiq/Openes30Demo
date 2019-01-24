package com.bn.Sample6_2;
import java.util.ArrayList;
import android.opengl.GLSurfaceView;
import android.opengl.GLES30;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;

class MySurfaceView extends GLSurfaceView 
{
    private SceneRenderer mRenderer;//������Ⱦ��
    LovoGoThread lgt;
    ArrayList<RigidBody> aList=new ArrayList<RigidBody>();
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
		LoadedObjectVertexNormal ch;
		LoadedObjectVertexNormal pm;
    	
        public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            
            //�����ص����岿λ�����������
            if(ch!=null)
            {
            	for(int i=0;i<aList.size();i++)
            	{
            		aList.get(i).drawSelf();
            	}
            }
            if(pm!=null)
            {
            	pm.drawSelf();
            }               
        }  

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 2, 100);
            //���ô˷������������9����λ�þ���
            MatrixState.setCamera(0,35,0,0f,0f,0f,0f,0f,1f);
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            //������Ļ����ɫRGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f,1.0f);    
            //����ȼ��
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            //�򿪱������   
            GLES30.glEnable(GLES30.GL_CULL_FACE);
            //��ʼ���任����
            MatrixState.setInitStack();
            //��ʼ����Դλ��
            MatrixState.setLightLocation(0, 10, -15);
            //����Ҫ���Ƶ�����
            ch=LoadUtil.loadFromFile("gxq.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            pm=LoadUtil.loadFromFile("pm.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
            //��ת����Ĺ���ǹ
            aList.add(new RigidBody(ch,true,new Vector3f(-18f,0f,0),new Vector3f(0,0,0),new Orientation(45,0,1,0)));	//�������ĸ��岢�Ž������б�
            aList.add(new RigidBody(ch,true,new Vector3f(20f,0f,0),new Vector3f(0,0,0),new Orientation(45,0,1,0)));		//�����Ҳ�ĸ��岢�Ž������б�
            aList.add(new RigidBody(ch,false,new Vector3f(0f,0f,0),new Vector3f(0.1f,0,0),new Orientation(0,0,1,0)));//�����м�ĸ��岢�Ž������б�
            //ȫ����ת90��
//            aList.add(new RigidBody(ch,true,new Vector3f(-18f,0f,0),new Vector3f(0,0,0),new Orientation(90,0,1,0)));
//            aList.add(new RigidBody(ch,true,new Vector3f(20f,0f,0),new Vector3f(0,0,0),new Orientation(90,0,1,0)));		
//            aList.add(new RigidBody(ch,false,new Vector3f(0f,0f,0),new Vector3f(0.1f,0,0),new Orientation(90,0,1,0)));
            
            lgt=new LovoGoThread(aList);//������ʱ�˶�������߳�
            lgt.start();//������ʱ�˶�������߳�
        }
    }
}