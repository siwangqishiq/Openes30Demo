package com.bn.Sample11_3A;//������
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES30;

//���غ�����塪����Я��������Ϣ����ɫ���
public class GrassObject
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id  
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������  
    int maTexCoorHandle; //��������������������  
    String mVertexShader;//������ɫ������ű�    	 
    String mFragmentShader;//ƬԪ��ɫ������ű�
	
    int vCount=0;  
    
	FloatBuffer   mVertexBuffer;//�����������ݻ���  
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���
	
	 int uTexHandle;//С��������������id
     int uJBTexHandle;//��ɫ������������id
     int uJBSHandle;//����ɫ���������в�����Sֵ����id
     int uJBTHandle;//����ɫ���������в�����Tֵ����id
    
    public GrassObject(MySurfaceView mv,float[] vertices,float texCoors[])
    {
    	//��ʼ���������ݵķ���
    	initVertexData(vertices,texCoors);
    	//��ʼ����ɫ��      
    	initShader(mv);
    }
    
	//��ʼ���������ꡢ�������������������ݵķ���
    public void initVertexData(float[] vertices,float texCoors[])
    {
    	//�����������ݵĳ�ʼ��================begin============================
    	vCount=vertices.length/3;   
		
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
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoors);//�򻺳����з��붥��������������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���������������ݵĳ�ʼ��================end============================
    }

    //��ʼ��shader
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��Ӧ����ɫ���������в�����Sֵ����id 
        uJBSHandle= GLES30.glGetUniformLocation(mProgram, "jbS");
        //��Ӧ����ɫ���������в�����Tֵ����id 
        uJBTHandle= GLES30.glGetUniformLocation(mProgram, "jbT");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡС����������id
        uTexHandle=GLES30.glGetUniformLocation(mProgram, "ssTexture");  
        //��ɫ������������id
        uJBTexHandle=GLES30.glGetUniformLocation(mProgram, "jbTexture");
        //��ȡ�����ж�������������������  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor"); 
    }
    //���Ƽ�������ķ���
    public void drawSelf(int texId,int jbTexId,float[] jb)
    {        
    	 //ָ��ʹ��ĳ����ɫ������
    	 GLES30.glUseProgram(mProgram);
         //�����ձ任��������Ⱦ����
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         // ������λ�����ݴ�����Ⱦ����
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
         //���ö���λ�á�����������������
         GLES30.glEnableVertexAttribArray(maPositionHandle);   
         GLES30.glEnableVertexAttribArray(maTexCoorHandle); //��������������������
         //��С������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         //����ɫ��������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, jbTexId);
         
         GLES30.glUniform1i(uTexHandle, 0);
         GLES30.glUniform1i(uJBTexHandle, 1);   
         GLES30.glUniform1f(uJBSHandle, jb[0]);//��Ӧ����ɫ���������в�����Sֵ
         GLES30.glUniform1f(uJBTHandle, jb[1]);//��Ӧ����ɫ���������в�����Sֵ
         //���Ƽ��ص�����
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
