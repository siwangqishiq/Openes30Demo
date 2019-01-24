package com.bn.bnggdh;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import com.bn.jar.bnggdh.Bnggdh;

import android.opengl.GLES30;

public class BnggdhDraw {
	public float maxKeytime;
	private int texId;
	Bnggdh bnggdh;

	public BnggdhDraw(InputStream is, MySurfaceView mv, String path) {
		bnggdh = new Bnggdh(is);
		try {
			bnggdh.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
		maxKeytime = bnggdh.getMaxKeytime();

		this.texId = LoadTextrueUtil.initTextureRepeat(mv, path);
		initShader(mv);
		initBuffer();
	}

	int mProgram;// �Զ�����Ⱦ���߳���id
	int maPositionHandle;// ����λ����������id
	int maTexCoorHandle; // ��������������������id
	int muMVPMatrixHandle;// �ܱ任��������id
	int muTexHandle;// ��������id

	int muMMatrixHandle;
	int muCameraHandle;
	int muLightHandle;

	int maNormalHandle;

	private FloatBuffer mVertexMappedBuffer;// ����λ�����ݻ��� mVertexBuffer
	private FloatBuffer mTextureBuffer;// �����������ݻ���
	private ShortBuffer mIndexBuffer;// �����������ݻ���
	private int mTextureBufferId;
	private int mIndexBufferId;
	private int mVertexBufferId;// �����������ݻ��� id
	private int mNormalBufferId;// �����������ݻ��� id

	ByteBuffer vbb1;// �����������ݵ�ӳ�仺��
	ByteBuffer vbb2;// �������������ݵ�ӳ�仺��

	private void initBuffer() {
		int bufferIds[] = new int[4];
		GLES30.glGenBuffers(4, bufferIds, 0);
		mTextureBufferId = bufferIds[0];
		mIndexBufferId = bufferIds[1];
		mVertexBufferId = bufferIds[2];
		mNormalBufferId = bufferIds[3];

		ByteBuffer mTex_bf = ByteBuffer.allocateDirect(this.bnggdh
				.getTextures().length * 4);// ���������������ݻ���
		mTex_bf.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mTextureBuffer = mTex_bf.asFloatBuffer();// ת����FloatBuffer
		mTextureBuffer.put(this.bnggdh.getTextures());// �� �����������ݻ����� �з� ������������
		mTextureBuffer.position(0);// ���û���������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBufferת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// �󶨵������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mTextureBufferId);
		// �������������ݻ�����������
		GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,
				this.bnggdh.getTextures().length * 4, mTextureBuffer,
				GLES30.GL_STATIC_DRAW);

		ByteBuffer mInd_bf = ByteBuffer
				.allocateDirect(this.bnggdh.getIndices().length * 2);// ���������������ݻ���
		mInd_bf.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mIndexBuffer = mInd_bf.asShortBuffer();// ת����ShortBuffer
		mIndexBuffer.put(this.bnggdh.getIndices());// �� �����������ݻ��� �з� ������������
		mIndexBuffer.position(0);// ���û���������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBufferת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// �󶨵������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mIndexBufferId);
		// �������������ݻ�����������
		GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,
				this.bnggdh.getIndices().length * 2, mIndexBuffer,
				GLES30.GL_STATIC_DRAW);

		// �����������ݵĳ�ʼ��================start============================
		// �󶨵������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
		// �򶥵��������ݻ�����������,����vertices.length*4���洢��λ��ͨ�����ֽڣ���
		// �ڴ棬���ڴ洢�������ݻ���������ǰ�����뵱ǰ�󶨶�������������ݶ���ɾ����
		GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,
				bnggdh.getPosition().length * 4, null, GLES30.GL_STATIC_DRAW);
		vbb1 = (ByteBuffer) GLES30.glMapBufferRange(GLES30.GL_ARRAY_BUFFER, // ��ʾ��������
				0, // ƫ����
				bnggdh.getPosition().length * 4, // ����
				GLES30.GL_MAP_WRITE_BIT | GLES30.GL_MAP_INVALIDATE_BUFFER_BIT// ���ʱ�־
		);
		if (vbb1 == null) {
			return;
		}
		vbb1.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexMappedBuffer = vbb1.asFloatBuffer();// ת��ΪFloat�ͻ���
		mVertexMappedBuffer.put(bnggdh.getPosition());// ��ӳ��Ļ������з��붥����������
														// verticesCube
		mVertexMappedBuffer.position(0);// ���û�������ʼλ��
		if (GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER) == false) {
			return;
		}
		// �����������ݵĳ�ʼ��================end============================

		// �������������ݵĳ�ʼ��================start============================
		float[] normals = bnggdh.getCurrentNormal();
		// �󶨵������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mNormalBufferId);
		// �򶥵��������ݻ�����������,����vertices.length*4���洢��λ��ͨ�����ֽڣ���
		// �ڴ棬���ڴ洢�������ݻ���������ǰ�����뵱ǰ�󶨶�������������ݶ���ɾ����
		GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, normals.length * 4, null,
				GLES30.GL_STATIC_DRAW);
		vbb2 = (ByteBuffer) GLES30.glMapBufferRange(GLES30.GL_ARRAY_BUFFER, // ��ʾ��������
				0, // ƫ����
				normals.length * 4, // ����
				GLES30.GL_MAP_WRITE_BIT | GLES30.GL_MAP_INVALIDATE_BUFFER_BIT// ���ʱ�־
		);
		if (vbb2 == null) {
			return;
		}
		vbb2.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexMappedBuffer = vbb2.asFloatBuffer();// ת��ΪFloat�ͻ���
		mVertexMappedBuffer.put(normals);// ��ӳ��Ļ������з��붥���������� verticesCube
		mVertexMappedBuffer.position(0);// ���û�������ʼλ��
		if (GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER) == false) {
			return;
		}
		// �������������ݵĳ�ʼ��================end============================
		// �󶨵�ϵͳĬ�ϻ��� ϵͳ����0 Ҫ��Ȼ���������ľͻ���������
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
	}

	private void refreshBuffer() {
		// �󶨵������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
		vbb1 = (ByteBuffer) GLES30.glMapBufferRange(GLES30.GL_ARRAY_BUFFER, 0, // ƫ����
				bnggdh.getPosition().length * 4, // ����
				GLES30.GL_MAP_WRITE_BIT | GLES30.GL_MAP_INVALIDATE_BUFFER_BIT// ���ʱ�־
		);
		if (vbb1 == null) {
			return;
		}
		vbb1.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexMappedBuffer = vbb1.asFloatBuffer();// ת��ΪFloat�ͻ���
		mVertexMappedBuffer.put(bnggdh.getPosition());// ��ӳ��Ļ������з��붥����������
		mVertexMappedBuffer.position(0);// ���û�������ʼλ��
		if (GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER) == false) {
			return;
		}

		// �󶨵������������ݻ���
		float[] normals = bnggdh.getCurrentNormal();
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mNormalBufferId);
		vbb2 = (ByteBuffer) GLES30.glMapBufferRange(GLES30.GL_ARRAY_BUFFER, 0, // ƫ����
				normals.length * 4, // ����
				GLES30.GL_MAP_WRITE_BIT | GLES30.GL_MAP_INVALIDATE_BUFFER_BIT// ���ʱ�־
		);
		if (vbb2 == null) {
			return;
		}
		vbb2.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mVertexMappedBuffer = vbb2.asFloatBuffer();// ת��ΪFloat�ͻ���
		mVertexMappedBuffer.put(normals);// ��ӳ��Ļ������з��붥����������
		mVertexMappedBuffer.position(0);// ���û�������ʼλ��
		if (GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER) == false) {
			return;
		}
	}

	public void initShader(MySurfaceView mv) {
		// ���ض�����ɫ���Ľű�
		String mVertexShader = ShaderUtil.loadFromAssetsFile("vertex_light.sh",
				mv.getResources());
		// ����ƬԪ��ɫ���Ľű�
		String mFragmentShader = ShaderUtil.loadFromAssetsFile("frag_light.sh",
				mv.getResources());
		// ����shader����
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// ��ȡshader�����ж���λ����������id
		maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
		// ��ȡ�����ж�������������������id
		maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");

		maNormalHandle = GLES30.glGetAttribLocation(mProgram, "aNormal");

		// ��ȡ�������ܱ任��������id
		muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
		// ��ȡ��������id
		muTexHandle = GLES30.glGetUniformLocation(mProgram, "sTexture");

		muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");
		muLightHandle = GLES30.glGetUniformLocation(mProgram, "uLightLocation");

		muCameraHandle = GLES30.glGetUniformLocation(mProgram, "uCamera");
	}

	public void draw(float time) {
		bnggdh.updata(time);
		refreshBuffer();// ���»�����

		GLES30.glUseProgram(mProgram);// ʹ��ĳ��shader����
		// glUniformMatrix4fv (int location, int count, boolean transpose,
		// float[] value, int offset)
		GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);// �����ձ任������shader����

		// ��λ�á���ת�任������shader����
		GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false,
				MatrixState.getMMatrix(), 0);
		// �������λ�ô���shader����
		GLES30.glUniform3fv(muCameraHandle, 1, MatrixState.cameraFB);
		// �������λ�ô���shader����
		GLES30.glUniform3fv(muLightHandle, 1, MatrixState.lightPositionFB);

		GLES30.glEnableVertexAttribArray(maPositionHandle);// ������λ������
		GLES30.glEnableVertexAttribArray(maTexCoorHandle);// ��������������
		GLES30.glEnableVertexAttribArray(maNormalHandle);// ��������������

		// ��������=======first============
		// GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,
		// false, 3 * 4, mVertexBuffer);// Ϊ����ָ������λ������
		// �󶨵������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
		// ������λ������������Ⱦ����
		GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,
				false, 3 * 4, 0);
		// ��������=======end============

		// ����������=======first============
		// GLES30.glVertexAttribPointer(maNormalHandle, 3, GLES30.GL_FLOAT,
		// false, 3 * 4, mNormalBuffer);//Ϊ����ָ������λ������
		// �󶨵������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mNormalBufferId);
		// ������λ������������Ⱦ����
		GLES30.glVertexAttribPointer(maNormalHandle, 3, GLES30.GL_FLOAT, false,
				3 * 4, 0);
		// ����������=======end============

		// ����====first
		// GLES30.glVertexAttribPointer(maTexCoorHandle, 2, GLES30.GL_FLOAT,
		// false, 2 * 4, mTextureBuffer);//Ϊ����ָ��������������
		// �󶨵����������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mTextureBufferId);
		// ָ������������������
		GLES30.glVertexAttribPointer(maTexCoorHandle, 2, GLES30.GL_FLOAT,
				false, 2 * 4, 0);
		// ����====end

		// �󶨵�ϵͳĬ�ϻ��� ϵͳ����0 Ҫ��Ȼ���������ľͻ���������
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

		// ������
		GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, this.texId);// �ڶ�����������úü�������ͼ�Ļ�
																// Ӧ��д
																// this.vdfd.texId[i]
		GLES30.glUniform1i(muTexHandle, 0);

		// ��������������������
		GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferId);
		// �������η�ʽִ�л���
		GLES30.glDrawElements(GLES30.GL_TRIANGLES, bnggdh.getIndices().length,
				GLES30.GL_UNSIGNED_SHORT, 0);
		GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);

		GLES30.glDisableVertexAttribArray(maPositionHandle);
		GLES30.glDisableVertexAttribArray(maTexCoorHandle);
		GLES30.glDisableVertexAttribArray(maNormalHandle);

	}
}
