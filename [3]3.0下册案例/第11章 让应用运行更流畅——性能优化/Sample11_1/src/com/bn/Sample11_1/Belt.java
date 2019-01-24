package com.bn.Sample11_1;//������

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;

//��ɫ��״��
public class Belt {
	int mProgram;// �Զ�����Ⱦ������ɫ������id
	int muMVPMatrixHandle;// �ܱ任��������
	int maPositionHandle; // ����λ����������
	int maColorHandle; // ������ɫ��������
	String mVertexShader;// ������ɫ������ű�
	String mFragmentShader;// ƬԪ��ɫ������ű�

	FloatBuffer mVertexBuffer;// �����������ݻ���
	FloatBuffer mColorBuffer;// ������ɫ���ݻ���
	private ByteBuffer mIndexBuffer;// �����������ݻ���
	int vCount = 0;
	int iCount = 0;//��������

	public Belt(MySurfaceView mv) {
		// ��ʼ��������������ɫ����
		initVertexData();
		// ��ʼ��shader
		initShader(mv);
	}

	//��ʼ������������ݵķ���
	public void initVertexData() {
		// �����������ݵĳ�ʼ��================begin============================
		vCount =8;
		
		float[] vertices ={
			-0.6f*Constant.UNIT_SIZE,0.1f*Constant.UNIT_SIZE,0,
			-0.6f*Constant.UNIT_SIZE,-0.1f*Constant.UNIT_SIZE,0,
			-0.2f*Constant.UNIT_SIZE,0.1f*Constant.UNIT_SIZE,0,
			-0.2f*Constant.UNIT_SIZE,-0.1f*Constant.UNIT_SIZE,0,
	
			0.2f*Constant.UNIT_SIZE,0.1f*Constant.UNIT_SIZE,0,
			0.2f*Constant.UNIT_SIZE,-0.1f*Constant.UNIT_SIZE,0,
			0.6f*Constant.UNIT_SIZE,0.1f*Constant.UNIT_SIZE,0,
			0.6f*Constant.UNIT_SIZE,-0.1f*Constant.UNIT_SIZE,0
		}; 
		
		// ���������������ݻ���
		//vertices.length*4����Ϊһ�������ĸ��ֽ�
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexBuffer = vbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mVertexBuffer.put(vertices);// �򻺳����з��붥����������
		mVertexBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// �����������ݵĳ�ʼ��================end============================

		//�����ι����������ݳ�ʼ��==========begin==========================
		iCount = vCount;//��ȡ��������
		byte indices[] ={
				0,1,2,3,4,5,6,7
		};//��������������ݵ�����
		//�����������ݻ���
		mIndexBuffer = ByteBuffer.allocateDirect(indices.length);
		mIndexBuffer.put(indices);//�򻺳����з�����������
		mIndexBuffer.position(0);//���û�������ʼλ��
		// �����ι����������ݳ�ʼ��==========end==============================

		// ������ɫ���ݵĳ�ʼ��================begin============================
		// ������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
		int count = 0;
		
		float colors[] = new float[vCount * 4];
		for(int i=0; i<colors.length; i+=8){
        	colors[count++] = 1; 
        	colors[count++] = 1; 
        	colors[count++] = 1; 
        	colors[count++] = 0;
        	
        	colors[count++] = 0; 
        	colors[count++] = 1; 
        	colors[count++] = 1; 
        	colors[count++] = 0;
        }

		// ����������ɫ���ݻ���
		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length * 4);
		cbb.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mColorBuffer = cbb.asFloatBuffer();// ת��ΪFloat�ͻ���
		mColorBuffer.put(colors);// �򻺳����з��붥����ɫ����
		mColorBuffer.position(0);// ���û�������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
		// ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// ������ɫ���ݵĳ�ʼ��================end============================
	}

	// ��ʼ����ɫ��
	public void initShader(MySurfaceView mv) {
		// ���ض�����ɫ���Ľű�����
		mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh",
				mv.getResources());
		// ����ƬԪ��ɫ���Ľű�����
		mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh",
				mv.getResources());
		// ���ڶ�����ɫ����ƬԪ��ɫ����������
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// ��ȡ�����ж���λ����������id
		maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
		// ��ȡ�����ж�����ɫ��������id
		maColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");
		// ��ȡ�������ܱ任��������id
		muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
	}

	public void drawSelf(){
		//ָ��ʹ��ĳ����ɫ������
		GLES30.glUseProgram(mProgram);
		// �����ձ任��������Ⱦ����
		GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);
		
		//������λ������������Ⱦ����
		GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,
				false, 3 * 4, mVertexBuffer);
		//��������ɫ����������Ⱦ����
		GLES30.glVertexAttribPointer(maColorHandle, 4, GLES30.GL_FLOAT, false,
				4 * 4, mColorBuffer);
		//���ö���λ����������
		GLES30.glEnableVertexAttribArray(maPositionHandle);
		//���ö�����ɫ��������
		GLES30.glEnableVertexAttribArray(maColorHandle);
		
		//����ͼԪ����
		GLES30.glEnable(GLES30.GL_PRIMITIVE_RESTART_FIXED_INDEX);
		//����ͼ��
		GLES30.glDrawElements(GLES30.GL_TRIANGLE_STRIP, iCount,
				GLES30.GL_UNSIGNED_BYTE, mIndexBuffer);
		//�ر�ͼԪ����
		GLES30.glDisable(GLES30.GL_PRIMITIVE_RESTART_FIXED_INDEX);
	}
}
