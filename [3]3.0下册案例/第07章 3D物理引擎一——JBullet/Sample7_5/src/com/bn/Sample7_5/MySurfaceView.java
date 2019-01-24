package com.bn.Sample7_5;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.vecmath.Vector3f;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.view.MotionEvent;
import com.bulletphysics.BulletGlobals;
import com.bulletphysics.collision.broadphase.AxisSweep3;
import com.bulletphysics.collision.dispatch.CollisionConfiguration;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.Clock;
import com.bulletphysics.linearmath.MatrixUtil;
import com.bulletphysics.linearmath.Transform;

import static com.bn.Sample7_5.Constant.*;

public class MySurfaceView extends GLSurfaceView {
	
	DiscreteDynamicsWorld dynamicsWorld;//��������
	CollisionShape boxShape;//��������ײ��״
	HingeConstraint constraint;//�����ؽ�Լ��
	private	float time;//��ǰʱ��
	private float cyclePeriod; //����ʱ��
	private float muscleStrength;//�����ؽڵ�����
	Clock clock = new Clock();//ʱ�ӣ�JBullet�ṩ��	
	private float mPreviousY;//�ϴεĴ���λ��Y����
    private float mPreviousX;//�ϴεĴ���λ��X����
    boolean flag=true;//ѭ�����Ʊ�־
    MyRenderer renderer; //��Ⱦ��
    
	public MySurfaceView(Context context) {
		super(context);
		this.setEGLContextClientVersion(3);		
		renderer = new MyRenderer();//������Ⱦ��
		this.setRenderer(renderer);//������Ⱦ��
		this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);//������Ⱦģʽ
		initWorld();//��ʼ����������
		time = 0.0f;//����ǰʱ���ʼ��Ϊ0
		cyclePeriod = 3000.0f; //��������ʱ��
		muscleStrength = 5f;//���������ؽڵ�����
	}
	 @Override 
	    public boolean onTouchEvent(MotionEvent e) {
	        float y = e.getY();
	        float x = e.getX();
	        switch (e.getAction()) {
	        case MotionEvent.ACTION_MOVE:
	            float dy = y - mPreviousY;//���㴥�ر�Yλ��
	            float dx = x - mPreviousX;//���㴥�ر�Xλ�� 	            
	            if(Math.abs(dx)>10)
	            {
	            	yAngle += dx /100;//��������Բ����y����ת�ĽǶ�
	 	            cx=(float) (Math.cos(yAngle)*15);
	 	            cz=(float) (Math.sin(yAngle)*15);
	            }
	            if(Math.abs(dy)>10)
	            {
	            	cy=cy+dy/10;
		            if(cy>15)
		            {
		            	cy=15;
		            }
		            else if(cy<-15)
		            {
		            	cy=-15;
		            }
	            }	            
	            break;
	        }
	        mPreviousY = y;//��¼���ر�λ��
	        mPreviousX = x;//��¼���ر�λ��
	        return true; 
	    }
	public void initWorld(){//��ʼ����������
		CollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);
		Vector3f worldAabbMin = new Vector3f(-10000, -10000, -10000);
		Vector3f worldAabbMax = new Vector3f(10000, 10000, 10000);
		int maxProxies = 1024;
		AxisSweep3 overlappingPairCache =new AxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();
		dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, overlappingPairCache, solver,collisionConfiguration);
		dynamicsWorld.setGravity(new Vector3f(0, -10, 0));
		boxShape=new BoxShape(new Vector3f(halfX,halfY,halfZ));
	}
	private class MyRenderer implements GLSurfaceView.Renderer{
		Cuboid door1; //��ʾ�������ŵ���������
		Cuboid door2;	//��ʾ�˶����ŵ���������
		int activeTexId;//�ŵ�����id
		@Override
		public void onDrawFrame(GL10 gl) {//����һ֡����ķ���
		
			GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT|GLES30.GL_COLOR_BUFFER_BIT);//��ջ���
			float ms = getDeltaTimeMicroseconds();//��ȡ��֡������ʱ����
			time+=ms;//��ǰʱ�����
			float preAngle = constraint.getHingeAngle();//��ȡ�ؽ���һʱ�̵ĽǶ�
			//��ù�ʽ1����Ҫ��x
			float x=(((int)(time / 1000) % (int)(cyclePeriod)) / cyclePeriod)*BulletGlobals.SIMD_2_PI;
			//���ݹ�ʽ1���y
			float y= 0.5f * (1.0f + (float)Math.sin(x));
			//�������ǰʱ�̵���ת�Ƕ�
			float currAngle = constraint.getLowerLimit() + y * (constraint.getUpperLimit() - constraint.getLowerLimit());
			float angleDelta = currAngle - preAngle;	//����ǶȲ�
			float desiredAngularVel = 1000000.f * angleDelta/ms;//�����֡�Ľ��ٶ�
			constraint.enableAngularMotor(true, desiredAngularVel, muscleStrength);//������ת���
			//���������
            MatrixState.setCamera(cx,cy,cz,0,0,0,0f,1.0f,0.0f);    
			MatrixState.pushMatrix();//�����ֳ�
			door1.drawSelf(activeTexId);//���Ʋ�������
			door2.drawSelf(activeTexId);//�����˶�����
			MatrixState.popMatrix();//�ָ��ֳ�
		}   

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			 //�����Ӵ���С��λ�� 
        	GLES30.glViewport(0, 0, width, height); 
        	//����GLSurfaceView�Ŀ�߱�
            float ratio= (float) width / height;
            //���ô˷����������͸��ͶӰ����
            MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1.5f, 100);            
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			//������Ļ����ɫRGBA
            GLES30.glClearColor(0.0f,0.0f,0.0f, 1.0f);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            MatrixState.setInitStack();
            activeTexId = initTexture(R.drawable.wood_bin1);
            //�����������Ŷ�Ӧ�ĳ�����
            door1 = new Cuboid(dynamicsWorld, boxShape, 0, 0, 0, 4, MySurfaceView.this,halfX,halfY,halfZ);
            //�����˶����Ŷ�Ӧ�ĳ�����
            door2 = new Cuboid(dynamicsWorld, boxShape, 1, 0, 0, -4, MySurfaceView.this,halfX,halfY,halfZ);
            //�������Ŷ�Ӧ�ĸ�����ӽ���Լ��
            addHingeConstraint(door1.body,door2.body);
            new Thread()
            {
            	public void run()
            	{
            		while(flag)
            		{            			
            			try 
            			{
            				//ģ��
                			dynamicsWorld.stepSimulation(1f/60.f, 5);
							Thread.sleep(20);
						} catch (Exception e) 
						{
							e.printStackTrace();
						}
            		}
            	}
            }.start();
		}
	}
	
	public void addHingeConstraint(RigidBody rbA,RigidBody rbB){//��ӽ���Լ���ķ���
		Transform transA = new Transform();//�����任���󣨴�Լ���������ŵ����ģ�
		transA.setIdentity();//��ʼ���任����
		MatrixUtil.setEulerZYX(transA.basis, BulletGlobals.SIMD_HALF_PI,0,0 );//���ñ任�����е���ת��Ϣ
		transA.origin.set(new Vector3f(0,0,-halfZ));//���ñ任�����е�ƽ����Ϣ
		Transform transB = new Transform();//�����任���󣨴�Լ�����˶��ŵ����ģ�		
		transB.setIdentity();//��ʼ���任����
		MatrixUtil.setEulerZYX(transB.basis, BulletGlobals.SIMD_HALF_PI,0,0 );//���ñ任�����е���ת��Ϣ
		transB.origin.set(new Vector3f(0,0,halfZ));//���ñ任�����е�ƽ����Ϣ
		constraint = new HingeConstraint(rbA,rbB,transA,transB);//��������Լ��
		constraint.setLimit(-BulletGlobals.SIMD_HALF_PI/2, BulletGlobals.SIMD_HALF_PI/2);//���ý���Լ����ת����Χ��������ֵ������ֵ��
		dynamicsWorld.addConstraint(constraint, true);//��Լ����ӵ���������
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
	public float getDeltaTimeMicroseconds() {//��ȡ��֡�������������ʱ���ķ���
		float dt = clock.getTimeMicroseconds();//��ȡ��ʱ�Ӵ������ϴ�ʱ�����õ����ڵĺ�����
		clock.reset();//����ʱ��
		return dt;//���������ʱ���
	}
}
