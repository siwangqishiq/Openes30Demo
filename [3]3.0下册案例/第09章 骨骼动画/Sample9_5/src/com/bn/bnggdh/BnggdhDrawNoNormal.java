package com.bn.bnggdh;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import android.annotation.SuppressLint;
import android.opengl.GLES30;

import com.bn.jar.bnggdh.Bnggdh;

@SuppressLint("UseSparseArrays")
public class BnggdhDrawNoNormal {

	public float maxKeytime;
	private int texId;
	Bnggdh bnggdh;

	public BnggdhDrawNoNormal(InputStream is, MySurfaceView mv, String path) {
		
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

	public void initShader(MySurfaceView mv) {
		// ���ض�����ɫ���Ľű�
		String mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh",
				mv.getResources());
		// ����ƬԪ��ɫ���Ľű�
		String mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh",
				mv.getResources());
		// ����shader����
		mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
		// ��ȡshader�����ж���λ����������id
		maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
		// ��ȡ�����ж�������������������id
		maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
		// ��ȡ�������ܱ任��������id
		muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
		// ��ȡ��������id
		muTexHandle = GLES30.glGetUniformLocation(mProgram, "sTexture");

	}

	private FloatBuffer mTextureBuffer;// �����������ݻ���
	private ShortBuffer mIndexBuffer;// �����������ݻ���
	private int mTextureBufferId;
	private int mIndexBufferId;
	private int mVertexBufferId;// �����������ݻ��� id

	ByteBuffer vbb1;// �����������ݵ�ӳ�仺��
	FloatBuffer mVertexMappedBuffer;// ��������ӳ�仺���Ӧ�Ķ����������ݻ���

	private void initBuffer() {
		int bufferIds[] = new int[3];
		GLES30.glGenBuffers(3, bufferIds, 0);
		mTextureBufferId = bufferIds[0];
		mIndexBufferId = bufferIds[1];
		mVertexBufferId = bufferIds[2];

		ByteBuffer mTex_bf = ByteBuffer
				.allocateDirect(bnggdh.getTextures().length * 4);// ���������������ݻ���
		mTex_bf.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mTextureBuffer = mTex_bf.asFloatBuffer();// ת����FloatBuffer
		mTextureBuffer.put(bnggdh.getTextures());// �� �����������ݻ����� �з� ������������
		mTextureBuffer.position(0);// ���û���������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBufferת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// �󶨵������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mTextureBufferId);
		// �������������ݻ�����������
		GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,
				bnggdh.getTextures().length * 4, mTextureBuffer,
				GLES30.GL_STATIC_DRAW);

		ByteBuffer mInd_bf = ByteBuffer
				.allocateDirect(bnggdh.getIndices().length * 2);// ���������������ݻ���
		mInd_bf.order(ByteOrder.nativeOrder());// �����ֽ�˳��
		mIndexBuffer = mInd_bf.asShortBuffer();// ת����ShortBuffer
		mIndexBuffer.put(bnggdh.getIndices());// �� �����������ݻ��� �з� ������������
		mIndexBuffer.position(0);// ���û���������ʼλ��
		// �ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBufferת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
		// �󶨵������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mIndexBufferId);
		// �������������ݻ�����������
		GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER,
				bnggdh.getIndices().length * 2, mIndexBuffer,
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
	}

	public void draw(float time) {
		bnggdh.updata(time);
		refreshBuffer();// ���»�����

		GLES30.glUseProgram(mProgram);// ʹ��ĳ��shader����
		GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,
				MatrixState.getFinalMatrix(), 0);// �����ձ任������shader����

		GLES30.glEnableVertexAttribArray(maPositionHandle);// ������λ������
		GLES30.glEnableVertexAttribArray(maTexCoorHandle);// ��������������

		// ��������=======first============
		// �󶨵������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVertexBufferId);
		// ������λ������������Ⱦ����
		GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,
				false, 3 * 4, 0);
		// ��������=======end============

		// ��������=======first============
		// �󶨵����������������ݻ���
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mTextureBufferId);
		// ָ������������������
		GLES30.glVertexAttribPointer(maTexCoorHandle, 2, GLES30.GL_FLOAT,
				false, 2 * 4, 0);
		// �󶨵�ϵͳĬ�ϻ��� ϵͳ����0 Ҫ��Ȼ���������ľͻ���������
		GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
		// ��������=======end============

		// ������
		GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, this.texId);// �ڶ�����������úü�������ͼ�Ļ�
																// Ӧ��д
																// this.vdfd.texId[i]
		GLES30.glUniform1i(muTexHandle, 0);

		// ��������=======first============
		// ��������������������
		GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mIndexBufferId);
		// �������η�ʽִ�л���
		GLES30.glDrawElements(GLES30.GL_TRIANGLES, bnggdh.getIndices().length,
				GLES30.GL_UNSIGNED_SHORT, 0);
		GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, 0);
		// ��������=======end============

		GLES30.glDisableVertexAttribArray(maPositionHandle);
		GLES30.glDisableVertexAttribArray(maTexCoorHandle);
	}
}
