package com.bn.Sample10_1;//������
import java.util.ArrayList;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


import android.content.Context;

class MySurfaceView extends GLSurfaceView 
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
	public float currSightDis=100;//�������Ŀ��ľ���
	float angdegElevation=30;//����
	public float angdegAzimuth=180;//��λ��	
	float left;
    float right;
	float top;
	float bottom;
	float near;
	float far;
	
	//��ʰȡ�����б�
	ArrayList<TouchableObject> lovnList=new ArrayList<TouchableObject>();
	//��ѡ�����������ֵ����id��û�б�ѡ��ʱ����ֵΪ-1
	int checkedIndex=-1;
	public MySurfaceView(Context context) {
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
        float y = e.getY();//��ȡ���ص��x����
        float x = e.getX();//��ȡ���ص��y����
        switch (e.getAction()) {
        case MotionEvent.ACTION_DOWN://���ǰ��¶���
        	//�������任��A��B��������������ϵ�е�����
			float[] AB=IntersectantUtil.calculateABPosition
			(
				x, y, //���ص�x��y����
				Sample10_1_Activity.screenWidth, //��Ļ���
				Sample10_1_Activity.screenHeight, //��Ļ�߶�
				//��ƽ�����
				left, //�ӽ�left��topֵ
				top,
				near, //�ӽ�near��farֵ
				far
			);
			//����AB
			Vector3f start = new Vector3f(AB[0], AB[1], AB[2]);//ʰȡ�����߶����A����
			Vector3f end = new Vector3f(AB[3], AB[4], AB[5]);//ʰȡ�����߶����B����
			
			
			/*
			 * ����AB�߶���ÿ�������Χ�е���ѽ���(��A������Ľ���)��
			 * ����¼����ѽ�����������б��е�����ֵ
			 */
			//��¼�б���ʱ����С������ֵ
    		checkedIndex = -1;//����ѡ����������(-1��ʾû��ѡ������)
    		int tmpIndex=-1;//���ڼ�¼����A��������������ֵ
    		float minTime=1;//���ڼ�¼�б���������ʰȡ�����߶��ཻ�����ʱ��
    		for(int i=0;i<lovnList.size();i++){//�����б��е�����
    			OBB box = lovnList.get(i).getCurrBox(); //�������OBB��Χ��   
    			//������start->end��������յ�任����������ϵ   	   
    			Vector3f rayStart=MatrixState.fromGToO(start,lovnList.get(i).m);
    			Vector3f rayEnd=MatrixState.fromGToO(end,lovnList.get(i).m);
    			//��������߲��������е�rayDir����end-start
    			Vector3f rayDir=rayEnd.minus(rayStart);//ʰȡ�����߶εĳ��Ⱥͷ���
    			
				float t = box.rayIntersect(rayStart, rayEnd,rayDir);//�����ཻʱ��
    			if (t <= minTime) {//��ʱ��С��ԭ���ʱ��
					minTime = t;//�������ʱ��
					tmpIndex = i;//���������������
				}
    		}
    		checkedIndex=tmpIndex;//��¼���(ѡ��)�������� 		
    		changeObj(checkedIndex);//�ı䱻ѡ������	
       	break;
        case MotionEvent.ACTION_MOVE://�����ƶ�����
            float dy = y - mPreviousY;//���㴥�ر�Yλ��
            float dx = x - mPreviousX;//���㴥�ر�Xλ��
            //��������ֵ���ƶ������
            if(Math.abs(dx)<7f && Math.abs(dy)<7f){
            	break;
            }            
            angdegAzimuth += dx * TOUCH_SCALE_FACTOR;//������x����ת�Ƕ�
            angdegElevation += dy * TOUCH_SCALE_FACTOR;//������z����ת�Ƕ�
            //������������5��90�ȷ�Χ��
            angdegElevation = Math.max(angdegElevation, 5);
            angdegElevation = Math.min(angdegElevation, 90);
            //�����������λ��
            setCameraPostion();
        break;
        }
        //��¼���ص�x��y����
        mPreviousY = y;//��¼���ر�λ��
        mPreviousX = x;//��¼���ر�λ��
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
	//���������ı�ѡ������
	public void changeObj(int index){
		if(index != -1){//��������屻ѡ��
    		for(int i=0;i<lovnList.size();i++){//�������п�ʰȡ�����б�
    			if(i==index){//��Ϊѡ������
    				lovnList.get(i).changeOnTouch(true);//�ı�������ɫ���Ŵ��ʵ�ѡ��״̬
    			}
    			else{//������ѡ������
    				lovnList.get(i).changeOnTouch(false);//�ı�������ɫ���Ŵ��ʵ�δѡ��״̬
    			}
    		}
        }
    	else{//���û�����屻ѡ��
    		for(int i=0;i<lovnList.size();i++){//�������п�ʰȡ�����б�			
    			lovnList.get(i).changeOnTouch(false);//�ı�������ɫ���Ŵ��ʵ�δѡ��״̬
    		}
    	}
	}
	private class SceneRenderer implements GLSurfaceView.Renderer 
    {
    	//��ָ����obj�ļ��м��ض���
		LoadedObjectVertexNormalFace pm;//��obj�ļ��м��ص�ƽ��
		LoadedObjectVertexNormalFace cft;
		LoadedObjectVertexNormalAverage qt;
		LoadedObjectVertexNormalAverage yh;
		LoadedObjectVertexNormalAverage ch;
    	
        public void onDrawFrame(GL10 gl) 
        { 
        	//�����Ȼ�������ɫ����
            GLES30.glClear( GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            //���������λ��
			MatrixState.setCamera(cx, cy, cz, tx, ty, tz, 0, 1, 0);
			//���ù�Դλ��
            MatrixState.setLightLocation(100, 100, 100);                    
            //��������            
            pm.drawSelf();//����ƽ��
        	
            //���Ƴ�����
            MatrixState.pushMatrix();//�����ֳ�
            MatrixState.translate(-30f, 0, 0);//ƽ�Ʊ任
            MatrixState.scale(cft.size, cft.size, cft.size);//���ű任
            cft.drawSelf();//���Ƴ�����
            MatrixState.popMatrix();//�ָ��ֳ�   
            //��������
            MatrixState.pushMatrix();
            MatrixState.translate(30f, 0f, 0);
            MatrixState.scale(qt.size, qt.size, qt.size);
            qt.drawSelf();
            MatrixState.popMatrix();  
            //����Բ��
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, -30f);
            MatrixState.scale(yh.size, yh.size, yh.size);
            MatrixState.rotate(45, 0, 1, 0);
            yh.drawSelf();
            MatrixState.popMatrix();  
            //���Ʋ��
            MatrixState.pushMatrix();
            MatrixState.translate(0, 0, 30f);
            MatrixState.scale(ch.size, ch.size, ch.size);
            MatrixState.rotate(30, 0, 1, 0);
            ch.drawSelf();
            MatrixState.popMatrix(); 
        } 

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio = (float) width / height;
            //���ô˷����������͸��ͶӰ����
            left=right=ratio;
            top=bottom=1;
            near=2;
            far=500;
            MatrixState.setProjectFrustum(-left, right, -bottom, top, near, far);
            //�����������λ��
            setCameraPostion();
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
            
            //����ƽ��
            pm=LoadUtil.loadFromFileVertexOnlyFace("pm.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
                        
            ch=LoadUtil.loadFromFileVertexOnlyAverage("ch.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
    		cft=LoadUtil.loadFromFileVertexOnlyFace("cft.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
    		qt=LoadUtil.loadFromFileVertexOnlyAverage("qt.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
    		yh=LoadUtil.loadFromFileVertexOnlyAverage("yh.obj", MySurfaceView.this.getResources(),MySurfaceView.this);
    		//����ʰȡ��������б�
    		lovnList.add(ch);
            lovnList.add(cft);
            lovnList.add(qt);
            lovnList.add(yh);
        }
    }
}
