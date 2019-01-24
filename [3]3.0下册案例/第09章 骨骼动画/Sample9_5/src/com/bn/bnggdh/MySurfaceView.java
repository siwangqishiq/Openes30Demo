package com.bn.bnggdh;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.bn.obj.LoadedObjectVertexNormalTexture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

@SuppressLint("ClickableViewAccessibility")
public class MySurfaceView extends GLSurfaceView {
	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// �Ƕ����ű���
	private SceneRenderer mRenderer;// ������Ⱦ��
	private float mPreviousY;// �ϴδ�����λ��Y����
	private float mPreviousX;// �ϴδ�����λ��X����

	float angle = 45;// ���������
	float direction = 0;// �������λ��
	final float distance = 120;// ���������Ŀ���ľ���

	// y���� 1
	float cx = 0, cy = distance, cz = 0;// �����λ��
	float tx = 0, ty = 0, tz = 0;// Ŀ���
	float ux = 0, uy = 0, uz = 1;// up����

	public MySurfaceView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setEGLContextClientVersion(3);// ����ʹ��OpenGL ES3.0
		mRenderer = new SceneRenderer();// ����������Ⱦ��
		setRenderer(mRenderer);// ������Ⱦ��
		setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// ������ȾģʽΪ�������ϵ�
		// calCamera(0,0);
	}

	// �����������λ������ת�Ƕ�
	public void calCamera(float dx, float dy) {
		direction += dx * TOUCH_SCALE_FACTOR;// ��λ��
		angle += dy * TOUCH_SCALE_FACTOR;// �����
		if (angle <= 0) {
			angle = 0;
		} else if (angle >= 90) {
			angle = 90;
		}
		// ��������
		double angleHD = Math.toRadians(direction);
		float[] cup = { -(float) Math.sin(angleHD), 0,
				(float) Math.cos(angleHD), 1 };
		float[] cLocation = { 0, distance, 0, 1 };

		// ������ת����
		float[] zhou = { -cup[2], 0, cup[0] };

		// �������������
		float[] helpM = new float[16];
		Matrix.setIdentityM(helpM, 0);
		Matrix.rotateM(helpM, 0, angle, zhou[0], zhou[1], zhou[2]);// ���� ���ݽǶ� ��
																	// ������ ������ת
		float[] cupr = new float[4];
		float[] cLocationr = new float[4];
		Matrix.multiplyMV(cupr, 0, helpM, 0, cup, 0);// ��ת���� �� xz���ϵ������� ���up����
		Matrix.multiplyMV(cLocationr, 0, helpM, 0, cLocation, 0);// ��ת���� ��
																	// y���ϵ�һ������
																	// ���
																	// �����λ������

		// �����λ��
		cx = cLocationr[0];
		cy = cLocationr[1];
		cz = cLocationr[2];

		// �۲��λ��
		tx = 0f;
		ty = 50;
		tz = 0f;

		// up����
		ux = cupr[0];
		uy = cupr[1];
		uz = cupr[2];
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		float y = e.getY();
		float x = e.getX();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dy = y - mPreviousY;
			float dx = x - mPreviousX;
			calCamera(dx, dy);// ���������λ�ü���ز���
			break;
		}
		mPreviousY = y;
		mPreviousX = x;
		return true;
	}

	private class SceneRenderer implements GLSurfaceView.Renderer {

		BNModel bnm;
		LoadedObjectVertexNormalTexture lo;

		public void onDrawFrame(GL10 gl) {
			// TODO Auto-generated method stub
			// �����Ȼ�������ɫ����
			GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT
					| GLES30.GL_COLOR_BUFFER_BIT);

			MatrixState.setLightLocation(cx, cy, cz);
			// MatrixState.setLightLocation(300, 0, 0);
			// ���������
			MatrixState.setCamera(cx, cy, cz, tx, ty, tz, ux, uy, uz);

			bnm.draw();
			lo.drawSelfWithBone();
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			// TODO Auto-generated method stub
			// �����Ӵ���С��λ��
			GLES30.glViewport(0, 0, width, height);
			// ����GLSurfaceView�Ŀ�߱�
			float ratio = (float) width / height;
			// ���ô˷����������͸��ͶӰ����
			MatrixState.setProjectFrustum(-ratio, ratio, -1, 1, 1, 1000);
			// MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 1000);
			// ��ʼ�������任����
			MatrixState.setInitStack();
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// TODO Auto-generated method stub
			// ������Ļ����ɫRGBA
			GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
			// ����ȼ��
			GLES30.glEnable(GLES30.GL_DEPTH_TEST);
			// �رձ������
			GLES30.glDisable(GLES30.GL_CULL_FACE);
			// GLES30.glEnable(GLES30.GL_CULL_FACE);

			bnm = new BNModel("a.bnggdh", "hero1.png", false, 0.005f, MySurfaceView.this);
			bnm.setDtFactor(0.001f);

			lo = new LoadedObjectVertexNormalTexture("box1.obj", "b.jpg",
					getResources(), MySurfaceView.this);
			lo.initWithBone(bnm, "Bip01 R Finger0", 4, 0, -2, 90, 0, 0, 1,1,1);//Bone03
		}
	}

}
