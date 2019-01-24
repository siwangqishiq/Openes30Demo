package com.bn.bnggdh;

import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

public class LoadTextrueUtil {
	// ��������ķ���
	public static int initTextureRepeat(MySurfaceView gsv, String pname) {
		// ��������ID
		int[] textures = new int[1];
		GLES30.glGenTextures(1, // ����������id������
				textures, // ����id������
				0 // ƫ����
		);
		int textureId = textures[0];
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);

		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,
				GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,
				GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,
				GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,
				GLES30.GL_REPEAT);

		// ͨ������������ͼƬ===============begin===================
		InputStream is = null;
		String path = "pic/" + pname;
		try {
			is = gsv.getResources().getAssets().open(path);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Bitmap bitmapTmp;
		try {
			bitmapTmp = BitmapFactory.decodeStream(is);
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// ʵ�ʼ�������
		GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, // �������ͣ���OpenGL
													// ES�б���ΪGL10.GL_TEXTURE_2D
				0, // ����Ĳ�Σ�0��ʾ����ͼ��㣬�������Ϊֱ����ͼ
				bitmapTmp, // ����ͼ��
				0 // ����߿�ߴ�
		);
		bitmapTmp.recycle(); // ������سɹ����ͷ�ͼƬ
		return textureId;
	}
}
