package com.bn.Sample5_3_V1;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES30;

//���غ�����塪��Я��������Ϣ���Զ�������ƽ��������
public class LoadedObjectVertexNormal//���غ������
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id  
    int muMVPMatrixHandle;//�ܱ任��������
    int muMMatrixHandle;//λ�á���ת�任����
    int maPositionHandle; //����λ����������  
    int maNormalHandle; //���㷨������������  
    int maLightLocationHandle;//��Դλ����������  
    int muMVPMatrixGYHandle;//��Դ�ܱ任�������� 
    int maCameraHandle; //�����λ���������� 
    String mVertexShader;//������ɫ������ű�    	 
    String mFragmentShader;//ƬԪ��ɫ������ű�    
    
    int mProgramForShadow;//�Զ�����ɫ������id�����������ã�  
    int muMVPMatrixHandleForShadow;//�ܱ任�������ã����������ã�
    int muMMatrixHandleForShadow;//λ�á���ת�����ű任�������ã����������ã�
    int maPositionHandleForShadow; //����λ���������ã����������ã�  
    int maLightLocationHandleForShadow;//��Դλ���������ã����������ã�
    String mVertexShaderForShadow;//������ɫ��    	 
    String mFragmentShaderForShadow;//ƬԪ��ɫ��    
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���  
	FloatBuffer   mNormalBuffer;//���㷨�������ݻ���
    int vCount=0;     
    
    public LoadedObjectVertexNormal(MySurfaceView mv,float[] vertices,float[] normals)
    {    	   	
    	initVertexData(vertices,normals);//��ʼ����������	       
    	initShaderForShadow(mv);//��ʼ�����ƾ����������ɫ��
    }
    
    //��ʼ����������  
    public void initVertexData(float[] vertices,float[] normals)
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
        
        //���㷨�������ݵĳ�ʼ��================begin============================  
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥�㷨��������
        mNormalBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //������ɫ���ݵĳ�ʼ��================end============================
    } 
    
    //��ʼ�����ƾ����������ɫ��
    public void initShaderForShadow(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShaderForShadow=ShaderUtil.loadFromAssetsFile("vertex_shadow.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShaderForShadow=ShaderUtil.loadFromAssetsFile("frag_shadow.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgramForShadow = ShaderUtil.createProgram(mVertexShaderForShadow, mFragmentShaderForShadow);
        //��ȡ�����ж���λ����������  
        maPositionHandleForShadow = GLES30.glGetAttribLocation(mProgramForShadow, "aPosition");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandleForShadow = GLES30.glGetUniformLocation(mProgramForShadow, "uMVPMatrix");  
        //��ȡλ�á���ת�任��������
        muMMatrixHandleForShadow = GLES30.glGetUniformLocation(mProgramForShadow, "uMMatrix"); 
        //��ȡ�����й�Դλ������
        maLightLocationHandleForShadow=GLES30.glGetUniformLocation(mProgramForShadow, "uLightLocation");
    }
    
    //��������ķ��������������ã�
    public void drawSelfForShadow()
    {        
   	 	//ָ��ʹ��ĳ����ɫ������
   	 	GLES30.glUseProgram(mProgramForShadow);
        //�����ձ任��������Ⱦ����
        GLES30.glUniformMatrix4fv(muMVPMatrixHandleForShadow, 1, false, MatrixState.getFinalMatrix(), 0);      
        //�������任��������Ⱦ����
        GLES30.glUniformMatrix4fv(muMMatrixHandleForShadow, 1, false, MatrixState.getMMatrix(), 0);   
        //����Դλ�ô�����Ⱦ����   
        GLES30.glUniform3fv(maLightLocationHandleForShadow, 1, MatrixState.lightPositionFB);
        
        //������λ�����ݴ�����Ⱦ����
        GLES30.glVertexAttribPointer  
        (
        		maPositionHandleForShadow,   
        		3, 
        		GLES30.GL_FLOAT, 
        		false,
               3*4,   
               mVertexBuffer
        );       
        //���ö���λ������
        GLES30.glEnableVertexAttribArray(maPositionHandleForShadow);  
        //���Ƽ��ص�����
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
