package com.bn.Sample10_2;//������
import java.util.HashMap;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.content.Context;

class MySurfaceView extends GLSurfaceView 
{
    private SceneRenderer mRenderer;//������Ⱦ��   
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
	//���ڶ�㴥�ص���
	HashMap<Integer,BNPoint> hm=new HashMap<Integer,BNPoint>();
	float distance=0;//���������
	float currScale=2;//��ʼ���ű���
	float scaleSpeedSpan=100;//���Ų�������
	float angle=0;//��ʼ��ת�Ƕ�
	public MySurfaceView(Context context){
        super(context);
        this.setEGLContextClientVersion(3); //����ʹ��OPENGL ES3.0
        mRenderer = new SceneRenderer();	//����������Ⱦ��
        setRenderer(mRenderer);				//������Ⱦ��		        
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������ȾģʽΪ������Ⱦ   
    }
	//�����¼�������
    @Override 
    public boolean onTouchEvent(MotionEvent e) 
    {
    	//��ȡ���ض������
		int action=e.getAction()&MotionEvent.ACTION_MASK;
		//��ȡ��������id��downʱ������id����ȷ��upʱ����id��ȷ������idҪ��ѯMap��ʣ�µ�һ�����id��
		int index=(e.getAction()&MotionEvent.ACTION_POINTER_INDEX_MASK)
				>>>MotionEvent.ACTION_POINTER_INDEX_SHIFT;	//��ȡ���ص�����
		int id=e.getPointerId(index); //��ȡ���ص�id
		switch(action)
		{
		case MotionEvent.ACTION_DOWN: //���㰴��
		case MotionEvent.ACTION_POINTER_DOWN: //���㰴��
			//���������㻹�Ǹ��㰴�½���Map�з���һ���µ�
			hm.put(id, new BNPoint(e.getX(index), e.getY(index)));
			//���Ѿ����������ص㰴�£���������
			if (hm.size() == 2){//���Ѿ����������ص㰴��
				BNPoint bpTempA = hm.get(e.getPointerId(0)); //��Map��ȡ����һ�����ص�
				BNPoint bpTempB = hm.get(e.getPointerId(1));	//��Map��ȡ���ڶ������ص�
				distance = BNPoint.calDistance(bpTempA, bpTempB);//���������ص�֮��ľ���
			}
		break;
		case MotionEvent.ACTION_MOVE: //��/�����ƶ�
			//������/����Move��������λ��
			int count=e.getPointerCount();//��ȡ���ص�����
			for(int i=0;i<count;i++)//�������д��ص�
			{
				int tempId=e.getPointerId(i);//��ȡ���ص�id
				hm.get(tempId).setLocation(e.getX(i), e.getY(i));//���´��ص�λ��
			}
			
			//����ǰ���������ص㰴������㴥�ص���벢����Ϊ����ϵ��
			//ͬʱ������ת�Ƕ�
			if (hm.size() == 2) {//����ǰ���������ص㰴��
				BNPoint bpTempA = hm.get(e.getPointerId(0)); //��Map��ȡ����һ�����ص�
				BNPoint bpTempB = hm.get(e.getPointerId(1)); //��Map��ȡ���ڶ������ص�
				//���㴥�ص���벢����Ϊ����ϵ��
				float currDis = BNPoint.calDistance(bpTempA, bpTempB);//�����������
				currScale = currScale + (currDis - distance) / scaleSpeedSpan;//��������ϵ��
				if (currScale > 4 || currScale < 1) {//������ϵ��������Χ���лָ�
					currScale = currScale - (currDis - distance) / scaleSpeedSpan;
				}
				distance = currDis;//��¼�������
				//������ת�Ƕ�
				if (bpTempA.hasOld || bpTempB.hasOld) {//���������ص㶼����һλ��
					double alphaOld = Math.atan2((bpTempA.oldY - bpTempB.oldY),
							(bpTempA.oldX - bpTempB.oldX));//������һλ�ö�Ӧ�ĽǶ�
					double alphaNew = Math.atan2((bpTempA.y - bpTempB.y),
							(bpTempA.x - bpTempB.x));//���㵱ǰλ�ö�Ӧ�ĽǶ�
					//�����Ƕ�����õ���ת�仯�Ƕȣ�������ת�仯�Ƕȵ��ӵ�����ת�Ƕ���
					angle = angle - (float) Math.toDegrees(alphaNew - alphaOld);
				}
			}
		break;
		case MotionEvent.ACTION_UP: //����̧��
			//�ڱ�Ӧ��������UP��ֻ��Ҫ���Map���ɣ�������һЩӦ������Ҫ������
			//��ȡ��Map��Ψһʣ�µĵ��������
			hm.clear();//��մ��ص�Map
		break;
		case MotionEvent.ACTION_POINTER_UP: //����̧��
			//��Map��ɾ����Ӧid�ĸ���
			hm.remove(id);//��Map��ɾ����Ӧid�ĸ���
		break;
		} 			
		return true;
	}
    
    //���������λ�õķ���
	public void setCameraPostion() {
		//�����������λ��
		double angradElevation = Math.toRadians(angdegElevation);//���ǣ����ȣ�
		double angradAzimuth = Math.toRadians(angdegAzimuth);//��λ��
		cx = (float) (tx - currSightDis * Math.cos(angradElevation)	* Math.sin(angradAzimuth));
		cy = (float) (ty + currSightDis * Math.sin(angradElevation));
		cz = (float) (tz - currSightDis * Math.cos(angradElevation) * Math.cos(angradAzimuth));
	}
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
		LoadedObjectVertexNormalAverage ch;
    	
        public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //���Ʋ��
            MatrixState.pushMatrix();
            MatrixState.translate(0, -10, 0);
            MatrixState.scale(currScale, currScale, currScale);
            MatrixState.rotate(angle, 0, 0, 1);
            ch.drawSelf();
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

			//����cameraλ��
			MatrixState.setCamera(cx, cy, cz, tx, ty, tz, 0, 1, 0);            
            //��ʼ����Դλ��
            MatrixState.setLightLocation(100, 100, 100);       
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
        }
    }
}
