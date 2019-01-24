package com.bn.bnggdh;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.Matrix;

//�洢ϵͳ����״̬����
public class MatrixState {
	private static float[] mProjMatrix = new float[16];//����ͶӰ�� 4*4����
	private static float[] mVMatrix = new float[16];//�����λ�ó���9��������
	private static float[] currMatrix;//��ǰ�任����
	public static float[] lightLocation = new float[]{0,0,0};//��Դλ��
	public static FloatBuffer cameraFB;
	public static FloatBuffer lightPositionFB;
	
	//�����任�����ջ
	static float[][] mStack = new float[10][16];
	static int stackTop = -1;
	
	//��ȡ���任�ĳ�ʼ����
	public static void setInitStack()
	{
		currMatrix = new float[16];
		Matrix.setRotateM(currMatrix, 0, 0, 1, 0, 0);
	}
	
	//�����任����
	public static void pushMatrix()
	{
		stackTop++;
		for(int i = 0; i < 16; i++)
		{
			mStack[stackTop][i] = currMatrix[i];
		}
	}
	
	//�ָ��任����
	public static void popMatrix()
	{
		for(int i = 0; i < 16; i++)
		{
			currMatrix[i] = mStack[stackTop][i];
		}
		stackTop--;
	}
	
	//������xyz���ƶ�
	public static void translate(float x, float y, float z)
	{
		Matrix.translateM(currMatrix, 0, x, y, z);
	}
	
	//������ת
	public static void rotate(float angle, float x, float y, float z)
	{
		Matrix.rotateM(currMatrix, 0, angle, x, y, z);
	}
	
	//��������
	public static void scale(float x, float y, float z)
	{
		Matrix.scaleM(currMatrix, 0, x, y, z);
	}
	
	//�����Դ�����
	public static void matrix(float[] self)
	{
		float[] result = new float[16];
		Matrix.multiplyMM(result, 0, currMatrix, 0, self, 0);//self�������currMatrix����
		currMatrix = result;
	}
	
	//���������
	static ByteBuffer llbb = ByteBuffer.allocateDirect(3*4);//�ֽڻ�����
	static float[] cameraLocation = new float[3];//�����λ��
	public static void setCamera
	(
		float cx,
		float cy,
		float cz,
		float tx,
		float ty,
		float tz,
		float upx,
		float upy,
		float upz
	)
	{
		Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
		
		cameraLocation[0] = cx;
		cameraLocation[1] = cy;
		cameraLocation[2] = cz;
		
		llbb.clear();
		llbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
		cameraFB = llbb.asFloatBuffer();//ͨ���ֽڻ����� �� ����float���ͻ�����
		cameraFB.put(cameraLocation);//�������������뻺������
		cameraFB.position(0);//���û�������ʼλ��
	}
	
	//����͸��ͶӰ�Ĳ���
	public static void setProjectFrustum
	(
		float left,//near���left
		float right,//near���right
		float bottom,//near���bottom
		float top,//near���top
		float near,//near��ľ���
		float far//far�����
	)
	{
		Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);
	}
	
	//��������ͶӰ����
	public static void setProjectOrtho
	(
		float left,//near���left
		float right,//near���right
		float bottom,//near���bottom
		float top,//near���top
		float near,//near��ľ���
		float far//far�����	
	)
	{
		Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
	}
	
	//����һ֡�ڵ����������
	private static float[] mVMatrixForSpecFrame = new float[16];
	public static float[] copyMVMatrix()
	{
		for(int i = 0; i < 16; i++)
		{
			mVMatrixForSpecFrame[i] = mVMatrix[i];
		}
		return mVMatrixForSpecFrame;
	}
	
	//��ȡ����������ܱ任����
	static float[] mMVPMatrix = new float[16];
	public static float[] getFinalMatrix()
	{
		//��ǰ���������mVMatrix  ���� ��ǰ�任����currMatrix   ���� ��ǰͶӰ����mProjMatrix , ���߽�Ͻ������   ����������ܱ任����  
		//��ǰ��������� �� ��ǰ����  ����  multiplyMM����  ��ֵ��  mMVPMatrix����
		//Matrix.multiplyMM(mMVPMatrix, 0, mVMatrixForSpecFrame, 0, currMatrix, 0);
		Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, currMatrix, 0);
		//mMVPMatrix���� �� ��ǰͶӰ����  ���� multiplyMM����  ��ֵ�� mMVPMatrix����
		Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
		return mMVPMatrix;
	}
	
	//��ȡ��������ı任����
	public static float[] getMMatrix()
	{
		return currMatrix;
	}
	
	//���õƹ�λ�õķ���
	static ByteBuffer llbbL = ByteBuffer.allocateDirect(3*4);
	public static void setLightLocation(float x, float y, float z)
	{
		llbbL.clear();
		
		lightLocation[0] = x;
		lightLocation[1] = y;
		lightLocation[2] = z;
		
		llbbL.order(ByteOrder.nativeOrder());//�����ֽڵ�˳��
		lightPositionFB = llbbL.asFloatBuffer();//ͨ���ֽڻ����� ���� float���͵Ļ�����
		lightPositionFB.put(lightLocation);//��������
		lightPositionFB.position(0);//������ʼλ��
	}
}
