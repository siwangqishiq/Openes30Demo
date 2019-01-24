package com.bn.Sample6_6;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.util.MatrixState;
import com.bn.util.ShaderManager;

import android.opengl.GLES30;
import android.opengl.GLSurfaceView;

import static com.bn.Sample6_6.Constant.*;

public class TextureRect {
	FloatBuffer mTextureBuffer;
	
	int mProgram;// �Զ�����Ⱦ���߳���id
	int muMVPMatrixHandle;// �ܱ任��������id
	int maPositionHandle; // ����λ����������id
	int maTexCoorHandle; // ��������������������id
    int maColorHandle; //������ɫ��������id 
	String mVertexShader;// ������ɫ��
	String mFragmentShader;// ƬԪ��ɫ��

	int texId;
	
	public TextureRect(GLSurfaceView gsv)
	{
		// ��ʼ��������������ɫ����
		initVertexData();
		// ��ʼ��shader
		initShader(gsv);
	}
	
	// ��ʼ��������������ɫ���ݵķ���
	private void initVertexData() 
	{
		final int cols=NUMCOLS;
		final int rows = NUMROWS;        
        //�����������ݵĳ�ʼ��================begin============================
    	//�Զ������������飬20��15��
    	float textures[]=generateTexCoor(cols,rows);
        
        //���������������ݻ���
        ByteBuffer tbb = ByteBuffer.allocateDirect(textures.length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTextureBuffer= tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTextureBuffer.put(textures);//�򻺳����з��붥����ɫ����
        mTextureBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
	}
	
	// ��ʼ����ɫ��
	public void initShader(GLSurfaceView gsv) {
		// ���ض�����ɫ���Ľű�����
		mVertexShader = ShaderManager.loadFromAssetsFile("vertex.sh",gsv.getResources());
		// ����ƬԪ��ɫ���Ľű�����
		mFragmentShader = ShaderManager.loadFromAssetsFile("frag.sh",gsv.getResources());
		// ���ڶ�����ɫ����ƬԪ��ɫ����������
		mProgram = ShaderManager.createProgram(mVertexShader, mFragmentShader);
		// ��ȡ�����ж���λ����������id
		maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
		// ��ȡ�����ж�������������������id
		maTexCoorHandle = GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�����ж�����ɫ��������id  
        maColorHandle= GLES30.glGetAttribLocation(mProgram, "aColor");
		// ��ȡ�������ܱ任��������id
		muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
	}
	
	public void drawSelf(FloatBuffer fb,int texId) {
		
		if(fb==null) return;
		
		// �ƶ�ʹ��ĳ��shader����
		GLES30.glUseProgram(mProgram);
		// �����ձ任������shader����
		GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false,MatrixState.getFinalMatrix(), 0);
		// Ϊ����ָ������λ������
		GLES30.glVertexAttribPointer(maPositionHandle, 3, GLES30.GL_FLOAT,false, 3 * 4, fb);
		// Ϊ����ָ������������������
		GLES30.glVertexAttribPointer(maTexCoorHandle, 2, GLES30.GL_FLOAT,false, 2 * 4, mTextureBuffer);		
		// ������λ����������
		GLES30.glEnableVertexAttribArray(maPositionHandle);
		GLES30.glEnableVertexAttribArray(maTexCoorHandle); 
		// ������
		GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
		// �����������
		GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, fb.capacity()/3);
	}

	//�Զ��з����������������ķ���
    public float[] generateTexCoor(int bw,int bh)
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=1.0f/bw;//����
    	float sizeh=1.0f/bh;//����
    	int c=0;
    	for(int i=0;i<bh;i++)
    	{
    		for(int j=0;j<bw;j++)
    		{
    			//ÿ����һ�����Σ������������ι��ɣ��������㣬12����������
    			float s=j*sizew;
    			float t=i*sizeh;
    			
    			result[c++]=s;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			
    			result[c++]=s+sizew;
    			result[c++]=t;
    			
    			result[c++]=s;
    			result[c++]=t+sizeh;
    			
    			result[c++]=s+sizew;
    			result[c++]=t+sizeh;    			
    		}
    	}
    	return result;
    }
}
