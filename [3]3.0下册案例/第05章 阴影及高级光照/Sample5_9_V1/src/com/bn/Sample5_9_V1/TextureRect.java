package com.bn.Sample5_9_V1;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;

//�������
public class TextureRect 
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id  
    int muMVPMatrixHandle;//�ܱ任��������
    
    int muMMatrixHandle;//λ�á���ת�任����
    int maPositionHandle; //����λ���������� 
    int muMVPMatrixMirrorHandle;//����������Ĺ۲���ͶӰ��Ͼ�������
    
	FloatBuffer   mVertexBuffer;//�����������ݻ���
    int vCount=0;   
    
    
    public TextureRect(MySurfaceView mv)
    {    	
    	//��ʼ���������ݵķ���
    	initVertexData();
    	//��ʼ����ɫ���ķ���        
    	initShader(mv);
    }
    //��ʼ���������ݵķ���
    public void initVertexData()
    {	
    	//�����������ݵĳ�ʼ��================begin============================
    	vCount=6;//ÿ���������������Σ�ÿ��������3������    
    	
        float vertices[] = new float[] {
        		-Constant.UNIT_SIZE, -Constant.UNIT_SIZE, 0,
				Constant.UNIT_SIZE, Constant.UNIT_SIZE, 0,
				-Constant.UNIT_SIZE, Constant.UNIT_SIZE, 0,
				
				-Constant.UNIT_SIZE, -Constant.UNIT_SIZE, 0,
				Constant.UNIT_SIZE, -Constant.UNIT_SIZE, 0,
				Constant.UNIT_SIZE, Constant.UNIT_SIZE, 0
		};
       
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
    }
    //��ʼ����ɫ���ķ���
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        String mVertexShader=ShaderUtil.loadFromAssetsFile("mirror_vertex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        String mFragmentShader=ShaderUtil.loadFromAssetsFile("mirror_frag.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram= ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡλ�á���ת�任��������
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix"); 
        //��ȡ����������Ĺ۲���ͶӰ��Ͼ�������
        muMVPMatrixMirrorHandle=GLES30.glGetUniformLocation(mProgram, "uMVPMatrixMirror");
    }
    
  
    public void drawSelf(int texId,float[] mMVPMatrixMirror)
    {
    	//ָ��ʹ��ĳ��shader����
    	GLES30.glUseProgram(mProgram); 
    	//�����ձ任��������Ⱦ����
    	GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
    	//������������Ĺ۲���ͶӰ��Ͼ�������Ⱦ����
        GLES30.glUniformMatrix4fv(muMVPMatrixMirrorHandle, 1, false, mMVPMatrixMirror, 0);        
    	//��λ�á���ת�任��������Ⱦ����
    	GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);   
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

    	GLES30.glEnableVertexAttribArray(maPositionHandle);      	//���ö���λ����������
    	GLES30.glActiveTexture(GLES30.GL_TEXTURE0);//��������
    	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);        	//������
    	GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);     	//�������η�ʽִ�л���
    }
}
