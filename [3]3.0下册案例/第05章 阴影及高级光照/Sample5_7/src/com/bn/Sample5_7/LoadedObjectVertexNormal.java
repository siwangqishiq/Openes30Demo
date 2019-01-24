package com.bn.Sample5_7;//������
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES30;

//���غ�����塪��Я��������Ϣ��ƽ��������
public class LoadedObjectVertexNormal
{	
	int mProgram1;//�Զ�����Ⱦ������ɫ������id  

    int muMVPMatrixHandle1;//�ܱ任��������
    int muMMatrixHandle1;//λ�á���ת�任����
    int maPositionHandle1; //����λ���������� 
    int maNormalHandle1; //���㷨������������ 
    int maLightLocationHandle1;//��Դλ���������� 
    int muSkyColorHandle1;
    int muGroundColorHandle1;
    
    String mVertexShader1;//������ɫ��    	 
    String mFragmentShader1;//ƬԪ��ɫ��    
	
	FloatBuffer mVertexBuffer;//�����������ݻ���  
	FloatBuffer mNormalBuffer;//���㷨�������ݻ���

    int vCount=0;     
    
    public LoadedObjectVertexNormal(MySurfaceView mv,float[] vertices,float[] normals)
    {    	
    	//��ʼ��������������ɫ����
    	initVertexData(vertices,normals);
    	//��ʼ��shader        
    	intShader(mv);
    }
    
    //��ʼ��������������ɫ���ݵķ���
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

    //��ʼ��shader
    public void intShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader1=ShaderUtil.loadFromAssetsFile("vertex_ball.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader1=ShaderUtil.loadFromAssetsFile("frag_ball.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram1 = ShaderUtil.createProgram(mVertexShader1, mFragmentShader1);
       
        //��ȡ�����ж���λ���������� 
        maPositionHandle1 = GLES30.glGetAttribLocation(mProgram1, "aPosition");
        //��ȡ�����ж�����ɫ�������� 
        maNormalHandle1= GLES30.glGetAttribLocation(mProgram1, "aNormal");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle1 = GLES30.glGetUniformLocation(mProgram1, "uMVPMatrix");  
        //��ȡλ�á���ת�任��������
        muMMatrixHandle1 = GLES30.glGetUniformLocation(mProgram1, "uMMatrix"); 
        //��ȡ�����й�Դλ������
        maLightLocationHandle1=GLES30.glGetUniformLocation(mProgram1, "uLightLocation");
        
        muSkyColorHandle1=GLES30.glGetUniformLocation(mProgram1, "uSkyColor");
        muGroundColorHandle1=GLES30.glGetUniformLocation(mProgram1, "uGroundColor");
    } 
    
    public void drawSelf()
    {   
		//�ƶ�ʹ��ĳ����ɫ������
		GLES30.glUseProgram(mProgram1);
		//�����ձ任��������ɫ������
		GLES30.glUniformMatrix4fv(muMVPMatrixHandle1, 1, false, MatrixState.getFinalMatrix(), 0);        
		//��λ�á���ת�任��������ɫ������
		GLES30.glUniformMatrix4fv(muMMatrixHandle1, 1, false, MatrixState.getMMatrix(), 0);   
		//����Դλ�ô�����Ⱦ����  
		GLES30.glUniform3fv(maLightLocationHandle1, 1, MatrixState.lightPositionFB);
		GLES30.glUniform3f(muSkyColorHandle1,1f,1f,1f);//�������ɫ������Ⱦ����
		GLES30.glUniform3f(muGroundColorHandle1,0.1f,0.1f,0.1f);//�������ɫ������Ⱦ����
	   
		//������λ�����ݴ�����Ⱦ����
		GLES30.glVertexAttribPointer  
		(
	 		maPositionHandle1,   
	 		3, 
	 		GLES30.GL_FLOAT, 
	 		false,
	        3*4,   
	        mVertexBuffer
		);       
		//�����㷨�������ݴ�����Ⱦ����
		GLES30.glVertexAttribPointer  
		(
    		maNormalHandle1, 
     		3,   
     		GLES30.GL_FLOAT, 
     		false,
            3*4,   
            mNormalBuffer
		);
		//���ö���λ�á���������������
		GLES30.glEnableVertexAttribArray(maPositionHandle1);  
		GLES30.glEnableVertexAttribArray(maNormalHandle1);  
		//���Ƽ��ص�����
		GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}
