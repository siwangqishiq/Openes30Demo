package com.bn.Sample11_4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;
import android.opengl.Matrix;

//������
public class Triangle
{
	public static float[] mProjMatrix = new float[16];//4x4���� ͶӰ��
    public static float[] mVMatrix = new float[16];//�����λ�ó���9��������
    public static float[] mMVPMatrix;//��������õ��ܱ任����
	
	int mProgram;//�Զ�����Ⱦ���߳���id
    int muMVPMatrixHandle;//�ܱ任��������id
    int maPositionHandle; //����λ����������id  
    int maColorHandle; //������ɫ��������id  
    static float[] mMMatrix = new float[16];//����������ƶ���ת������ת��ƽ��
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mColorBuffer;//������ɫ���ݻ���
    int vCount=0;	
    float xAngle=0;//��x����ת�ĽǶ�
    public Triangle(MyTDView mv)
    {    	
    	//��ʼ��������������ɫ����
    	initVertexData();
    	//��ʼ��shader
    	initShader(mv);
    }
   
    public void initVertexData()
    {
    	//�����������ݵĳ�ʼ��
        vCount=3;  
        final float UNIT_SIZE=0.2f;
        float vertices[]=new float[]
        {
        	-4*UNIT_SIZE,0,
        	0,0,-4*UNIT_SIZE,
        	0,4*UNIT_SIZE,0,0
        };
		
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertices);
        mVertexBuffer.position(0);
        
        float colors[]=new float[]
        {
        		1,1,0,0,
        		0,1,1,0,
        		1,0,1,0
        };
        
        ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length*4);
        cbb.order(ByteOrder.nativeOrder());
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colors);
        mColorBuffer.position(0);
    }

    //��ʼ��shader
    public void initShader(MyTDView mv)
    {
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram("shader.bin",mv.getResources());
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�����ɫ��������id  
        maColorHandle= GLES30.glGetAttribLocation(mProgram, "aColor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
    
    public void drawSelf()
    {        
    	 //�ƶ�ʹ��ĳ��shader����
    	 GLES30.glUseProgram(mProgram);        
    	 //��ʼ���任����
         Matrix.setRotateM(mMMatrix,0,0,0,1,0);
         //������Z������λ��1
         Matrix.translateM(mMMatrix,0,0,0,1);
         //������x����ת
         Matrix.rotateM(mMMatrix,0,xAngle,1,0,0);
         //
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, Triangle.getFianlMatrix(mMMatrix), 0); 
         //Ϊ����ָ������λ������
         GLES30.glVertexAttribPointer(
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );
         GLES30.glVertexAttribPointer  
         (
        		maColorHandle,
         		4,
         		GLES30.GL_FLOAT,
         		false,
                4*4,
                mColorBuffer
         );
         //������λ����������
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         GLES30.glEnableVertexAttribArray(maColorHandle);  
         //����������
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
    public static float[] getFianlMatrix(float[] spec)
    {
    	mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);        
        return mMVPMatrix;
    }
}