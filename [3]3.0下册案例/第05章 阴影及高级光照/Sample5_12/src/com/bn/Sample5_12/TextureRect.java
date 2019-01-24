package com.bn.Sample5_12;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.annotation.SuppressLint;
import android.opengl.GLES30;

//����������
@SuppressLint("NewApi") 
public class TextureRect 
{
	int mProgram;//�Զ�����Ⱦ���߳���id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������
    int maTexCoorHandle; //��������������������
    String mVertexShader;//������ɫ������ű�
    String mFragmentShader;//ƬԪ��ɫ������ű�
	
    int sceneTextureHandle;//������������
    int depthTextureHandle;//�����������
    
    int mViewProjectionInverseMatrixHandle;//��ǰ�۲�-ͶӰ�������������
    int mPreviousProjectionMatrixHandle;//ǰһ֡�۲�-ͶӰ���������
    int mSampleNumberHandle;//��������������
    
	FloatBuffer mVertexBuffer;//�����������ݻ���
	FloatBuffer mTexCoorBuffer;//���������������ݻ���
    int vCount=0;
    
    public TextureRect(MySurfaceView mv,float ratio)
    {    	
    	//��ʼ���������ݷ���
    	initVertexData(ratio);
    	//��ʼ����ɫ������
    	initShader(mv);
    }
    
    //��ʼ���������ݷ���
    public void initVertexData(float ratio)
    {
    	//�����������ݵĳ�ʼ��================begin============================
        vCount=6;
        final float UNIT_SIZE=1.0f;
        float vertices[]=new float[]
        {
        		-ratio*UNIT_SIZE,UNIT_SIZE,0,
        		-ratio*UNIT_SIZE,-UNIT_SIZE,0,
        		ratio*UNIT_SIZE,-UNIT_SIZE,0,
        		
        		ratio*UNIT_SIZE,-UNIT_SIZE,0,
        		ratio*UNIT_SIZE,UNIT_SIZE,0,
        		-ratio*UNIT_SIZE,UNIT_SIZE,0
        };
		
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
        
        //���������������ݵĳ�ʼ��================begin============================
        float texCoor[]=new float[]//������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
        {    
        		0,1, 0,0, 1.0f,0,
        		1.0f,0, 1.0f,1.0f, 0,1.0f  
        };        
        //�������������������ݻ���
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���������������ݵĳ�ʼ��================end============================
    }

    //��ʼ����ɫ������
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_tex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_tex.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        
        sceneTextureHandle=GLES30.glGetUniformLocation(mProgram,"sTexture");
        depthTextureHandle=GLES30.glGetUniformLocation(mProgram,"depthTexture");
        
        mViewProjectionInverseMatrixHandle=GLES30.glGetUniformLocation(mProgram, "uViewProjectionInverseMatrix"); 
        mPreviousProjectionMatrixHandle=GLES30.glGetUniformLocation(mProgram, "uPreviousProjectionMatrix"); 
        mSampleNumberHandle=GLES30.glGetUniformLocation(mProgram, "g_numSamples"); 
    }
    
    public void drawSelf(int sceneTexId,int depthTexId,float[] mPreviousProjectionMatrix,float[] mViewProjectionMatrix,int sampleNumber)
    {        
    	 //ָ��ʹ��ĳ����ɫ����
    	 GLES30.glUseProgram(mProgram);
    	 MatrixState.pushMatrix();
         //������Z������λ��1
         MatrixState.translate(0, 0, 1);
         //�����ձ任��������Ⱦ����
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);
         
         GLES30.glUniformMatrix4fv(mViewProjectionInverseMatrixHandle, 1, false, mViewProjectionMatrix, 0);
         GLES30.glUniformMatrix4fv(mPreviousProjectionMatrixHandle, 1, false, mPreviousProjectionMatrix, 0);
         GLES30.glUniform1i(mSampleNumberHandle, sampleNumber);
         
         //������λ�����ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //�����������������ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES30.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );   
         //���ö���λ����������
         GLES30.glEnableVertexAttribArray(maPositionHandle);
         //���ö���������������
         GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
         
         //������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, sceneTexId);
         GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, depthTexId);
         
         GLES30.glUniform1i(sceneTextureHandle, 0);//ͨ������ָ����������
         GLES30.glUniform1i(depthTextureHandle, 1);  //ͨ������ָ����ҹ����        
         //�����������
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
         MatrixState.popMatrix();
    }
}
