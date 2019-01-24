package com.bn.Sample5_6;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.annotation.SuppressLint;
import android.opengl.GLES30;

//���غ�����塪��Я��������Ϣ���Զ�������ƽ��������
@SuppressLint("NewApi") public class LoadedObjectVertexNormal
{	
	int mProgram1;//�Զ�����Ⱦ������ɫ������id  

    int muMVPMatrixHandle1;//�ܱ任��������
    int muMMatrixHandle1;//λ�á���ת�任����
    int maPositionHandle1; //����λ���������� 
    int maNormalHandle1; //���㷨������������ 
    int maLightLocationHandle1;//��Դλ���������� 
    int maCameraHandle1; //�����λ����������
    int muProjCameraMatrixHandle1;
    int mLight1;//�۹�Ƶķ�������������
    
	int mProgram2;//�Զ�����Ⱦ������ɫ������id 
    int muMMatrixHandle2;//λ�á���ת�任����
    int maPositionHandle2; //����λ���������� 
    int maLightLocationHandle2;//��Դλ���������� 
    int muProjCameraMatrixHandle2;
    
    String mVertexShader1;//������ɫ��    	 
    String mFragmentShader1;//ƬԪ��ɫ��    
    String mVertexShader2;//������ɫ��    	 
    String mFragmentShader2;//ƬԪ��ɫ��
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���  
	FloatBuffer   mNormalBuffer;//���㷨�������ݻ���
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
        mVertexShader1=ShaderUtil.loadFromAssetsFile("vertex_land.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader1=ShaderUtil.loadFromAssetsFile("frag_land.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram1 = ShaderUtil.createProgram(mVertexShader1, mFragmentShader1);
        //��ȡ�۹�Ʒ�������������
        mLight1=GLES30.glGetUniformLocation(mProgram1, "light"); 
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
        //��ȡ�����������λ������
        maCameraHandle1=GLES30.glGetUniformLocation(mProgram1, "uCamera");
 
        //���ض�����ɫ���Ľű�����
        mVertexShader2=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader2=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram2 = ShaderUtil.createProgram(mVertexShader2, mFragmentShader2);
        //��ȡ�����ж���λ���������� 
        maPositionHandle2 = GLES30.glGetAttribLocation(mProgram2, "aPosition");
        //��ȡλ�á���ת�任��������
        muMMatrixHandle2 = GLES30.glGetUniformLocation(mProgram2, "uMMatrix");
        //��ȡ�����й�Դλ������
        maLightLocationHandle2=GLES30.glGetUniformLocation(mProgram2, "uLightLocation");
        muProjCameraMatrixHandle2=GLES30.glGetUniformLocation(mProgram2, "uMProjCameraMatrix"); 
    } 
    
    public void drawSelf(int isShadow)
    {        
    	if(isShadow==0)
    	{
    		//�ƶ�ʹ��ĳ����ɫ������
    		GLES30.glUseProgram(mProgram1);
    		//�����ձ任��������ɫ������
    		GLES30.glUniformMatrix4fv(muMVPMatrixHandle1, 1, false, MatrixState.getFinalMatrix(), 0);        
    		//��λ�á���ת�任��������ɫ������
    		GLES30.glUniformMatrix4fv(muMMatrixHandle1, 1, false, MatrixState.getMMatrix(), 0);   
    		//����Դλ�ô�����ɫ������   
    		GLES30.glUniform3fv(maLightLocationHandle1, 1, MatrixState.lightPositionFB);
    		//�������λ�ô�����ɫ������   
    		GLES30.glUniform3fv(maCameraHandle1, 1, MatrixState.cameraFB);
            //���۹�Ʒ�������������ɫ������   
            GLES30.glUniform3fv(mLight1, 1,MySurfaceView.dis);
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
    	else if(isShadow==1)
    	{
    		//�ƶ�ʹ��ĳ����ɫ������
    		GLES30.glUseProgram(mProgram2);        
    		//��λ�á���ת�任��������ɫ������
    		GLES30.glUniformMatrix4fv(muMMatrixHandle2, 1, false, MatrixState.getMMatrix(), 0);   
    		//����Դλ�ô�����ɫ������   
    		GLES30.glUniform3fv(maLightLocationHandle2, 1, MatrixState.lightPositionFB);
    		//��ͶӰ���������Ͼ�������ɫ������
    		GLES30.glUniformMatrix4fv(muProjCameraMatrixHandle2, 1, false, MatrixState.getViewProjMatrix(), 0); 
    		//������λ�����ݴ�����Ⱦ����
    		GLES30.glVertexAttribPointer  
    		(
         		maPositionHandle2,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
    				);          
    		//���ö���λ�á���������������
    		GLES30.glEnableVertexAttribArray(maPositionHandle2);  
    		//���Ƽ��ص�����
    		GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    	}
    }
}
