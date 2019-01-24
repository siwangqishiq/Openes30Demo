package com.bn.Sample7_4;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES30;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.TriangleIndexVertexArray;
import com.bulletphysics.extras.gimpact.GImpactMeshShape;


public class LoadedObjectVertexNormal 
{
	int mProgram;//�Զ�����Ⱦ���߳���id 
    int muMVPMatrixHandle;//�ܱ任��������id   
    int muMMatrixHandle;//λ�á���ת�任����
    int maCameraHandle; //�����λ����������id  
    int maPositionHandle; //����λ����������id 
    int maNormalHandle; //���㷨������������  
    int maLightLocationHandle;//��Դλ����������  
    
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	
	private FloatBuffer mVertexBuffer;//�����������ݻ���
    private FloatBuffer mNormalBuffer;//���㷨�������ݻ���
    int vCount=0;//��������
    CollisionShape loadShape;//��ײ��״����
    float[] vertices;//����������������
    float[] normals;//���㷨������������
    
    public LoadedObjectVertexNormal(float[] vertices,float[] normals) 
    {
    	this.vertices=vertices;
    	this.normals=normals;
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
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = nbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥�㷨��������
        mNormalBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //������ɫ���ݵĳ�ʼ��================end============================
        
        //����Ϊ������ײ��״��GImpact������״��״������Ķ������ݻ���
    	ByteBuffer gVertices=ByteBuffer.allocateDirect(vCount*3*4).order(ByteOrder.nativeOrder()); 
    	for(int i=0;i<vertices.length;i++)//������������ӵ�������
    	{
    		gVertices.putFloat(i*4,vertices[i]);//������������ӵ�������
    	} 
    	gVertices.position(0);//���û�������ʼλ��
    	 //����Ϊ������ײ��״��GImpact������״��״������Ķ�����������
    	ByteBuffer gIndices=ByteBuffer.allocateDirect(vCount*4).order(ByteOrder.nativeOrder());
    	for(int i=0;i<vCount;i++)
    	{
    		gIndices.putInt(i);//������������ӵ�������
    	}
    	gIndices.position(0);//���û�������ʼλ��
    	int vertStride = 4*3;//�������ݼ��
		int indexStride = 4*3;//�������ݼ��
    	TriangleIndexVertexArray indexVertexArrays=  //�����������鶥����������
		new TriangleIndexVertexArray
		(
			vCount/3,//�����εĸ���
			gIndices, //��������
			indexStride,//�������ݼ��
			vCount, //�������
			gVertices, //���㻺��
			vertStride//�������ݼ��
		);
    	//���ص�������״�������Ӧ����ײ��״  	
    	GImpactMeshShape trimesh = new GImpactMeshShape(indexVertexArrays);   
    	trimesh.updateBound();//������ײ��״�ı߽�
    	loadShape =trimesh;//������ײ��״����
    }
    
  //��ʼ��shader
    public void initShader(int mProgram)
    {
    	this.mProgram=mProgram;
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�������ܱ任��������id 
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        //��ȡ�����ж��㷨������������  
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal");
        //��ȡλ�á���ת�任��������
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix"); 
        //��ȡ�����й�Դλ������
        maLightLocationHandle=GLES30.glGetUniformLocation(mProgram, "uLightLocation");
        //��ȡ�����������λ������
        maCameraHandle=GLES30.glGetUniformLocation(mProgram, "uCamera");
    }

    public void drawSelf() 
    {        
    	 //�ƶ�ʹ��ĳ��shader����
    	 GLES30.glUseProgram(mProgram);
         //�����ձ任������shader����
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
       //��λ�á���ת�任��������ɫ������
         GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);   
         //����Դλ�ô�����ɫ������   
         GLES30.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFBRed);
         //�������λ�ô�����ɫ������   
         GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
         
         //Ϊ����ָ������λ������    
         GLES30.glVertexAttribPointer        
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4, 
                mVertexBuffer   
         );       
         //�����㷨�������ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer 
         (
        		maNormalHandle, 
         		3,   
         		GLES30.GL_FLOAT,
         		false,
                3*4,   
                mNormalBuffer
         );
         //������λ����������
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         GLES30.glEnableVertexAttribArray(maNormalHandle);
         //����������
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
