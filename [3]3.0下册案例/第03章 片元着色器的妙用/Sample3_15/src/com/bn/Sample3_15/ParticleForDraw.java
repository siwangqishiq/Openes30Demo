package com.bn.Sample3_15;//������
import static com.bn.Sample3_15.ParticleDataConstant.lock;
import static com.bn.Sample3_15.ShaderUtil.createProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;
//����������
public class ParticleForDraw 
{	
	int mProgram;//�Զ�����Ⱦ���߳���id
    int muMVPMatrixHandle;//�ܱ任��������id
    int muLifeSpan;//
    int muBj;//�������ӵİ뾶����id  
    int muStartColor;//��ʼ��ɫ����id
    int muEndColor;//��ֹ��ɫ����id
    int muCameraPosition;//�����λ��
    int muMMatrix;//�����任�����ܾ���
    int maPositionHandle; //����λ����������id  
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
    int vCount=0;   
    float halfSize;
    
    public ParticleForDraw(MySurfaceView mv,float halfSize)
    {    	
    	this.halfSize=halfSize;
    	//��ʼ����ɫ��        
    	initShader(mv);
    }
    
    //���¶����������ݻ���ķ���
    public void updatVertexData(float[] points)
    {
    	mVertexBuffer.clear();//��ն����������ݻ���
        mVertexBuffer.put(points);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
    }
    
    //��ʼ���������ݵķ���
    public void initVertexData(float[] points)
    {
    	//�����������ݵĳ�ʼ��================begin============================
        vCount=points.length/4;//�������
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(points.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(points);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�����������ݵĳ�ʼ��================end============================
    }

    //��ʼ����ɫ��
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡ������˥����������id
        muLifeSpan=GLES30.glGetUniformLocation(mProgram, "maxLifeSpan");
        //��ȡ�����а뾶����id
        muBj=GLES30.glGetUniformLocation(mProgram, "bj");
        //��ȡ��ʼ��ɫ����id
        muStartColor=GLES30.glGetUniformLocation(mProgram, "startColor");
        //��ȡ��ֹ��ɫ����id
        muEndColor=GLES30.glGetUniformLocation(mProgram, "endColor");
        //��ȡ�����λ������id
        muCameraPosition=GLES30.glGetUniformLocation(mProgram, "cameraPosition");
        //��ȡ�����任�����ܾ�������id
        muMMatrix=GLES30.glGetUniformLocation(mProgram, "uMMatrix");
    }
    
    public void drawSelf(int texId,float[] startColor,float[] endColor,float maxLifeSpan)
    {        
    	 //�ƶ�ʹ��ĳ��shader����
    	 GLES30.glUseProgram(mProgram);  
         //�����ձ任������shader����
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //
         GLES30.glUniform1f(muLifeSpan, maxLifeSpan);
         //���뾶����shader����
         GLES30.glUniform1f(muBj, halfSize);
         //����ʼ��ɫ������Ⱦ����
         GLES30.glUniform4fv(muStartColor, 1, startColor, 0);
         //����ֹ��ɫ������Ⱦ����
         GLES30.glUniform4fv(muEndColor, 1, endColor, 0);
         //�������λ�ô�����Ⱦ����
         GLES30.glUniform3f(muCameraPosition,MatrixState.cx, MatrixState.cy, MatrixState.cz);
         //�������任�����ܾ�������Ⱦ����
         GLES30.glUniformMatrix4fv(muMMatrix, 1, false, MatrixState.getMMatrix(), 0); 
         
         //������λ����������
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         
         //������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         
         synchronized(lock)
     	 {//����--��ֹ�ڽ�������������������Ⱦ����ʱ�����¶�����������
        	//��������������������Ⱦ����
             GLES30.glVertexAttribPointer  
             (
             		maPositionHandle,   
             		4, 
             		GLES30.GL_FLOAT, 
             		false,
                    4*4,   
                    mVertexBuffer
             );                
        	 //���Ƶ�
        	 GLES30.glDrawArrays(GLES30.GL_POINTS, 0, vCount);
     	 }
    }
}
