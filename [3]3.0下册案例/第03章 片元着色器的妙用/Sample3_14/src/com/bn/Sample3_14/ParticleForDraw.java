package com.bn.Sample3_14;
import static com.bn.Sample3_14.ParticleDataConstant.lock;
import static com.bn.Sample3_14.ShaderUtil.createProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;
//����������
public class ParticleForDraw 
{	
	int mProgram;//�Զ�����Ⱦ���߳���id
    int muMVPMatrixHandle;//�ܱ任��������id
    int muLifeSpan;//˥����������id
    int muBj;//�뾶����id  
    int muStartColor;//��ʼ��ɫ����id
    int muEndColor;//��ֹ��ɫ����id
    int maPositionHandle; //����λ����������id  
    int maTexCoorHandle; //��������������������id  
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	
	FloatBuffer mVertexBuffer;//�����������ݻ���
	FloatBuffer mTexCoorBuffer;//���������������ݻ���
    int vCount=0;   //�������
    float halfSize;//���Ӱ뾶
    
    public ParticleForDraw(MySurfaceView mv,float halfSize)
    {//������	
    	this.halfSize=halfSize;//��ʼ�����Ӱ뾶
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
    
    //��ʼ�����������������������ݵķ���
    public void initVertexData(float[] points)
    {
    	//�����������ݵĳ�ʼ��================begin============================
    	vCount=points.length/4;//����
        float vertices[]=points;
       
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
       
    	 float texCoor[]=new float[vCount*2];//������ɫֵ���飬ÿ������4��ɫ��ֵRGBA
         for(int i=0;i<vCount/6;i++)
         {
         	texCoor[12*i]=0;
         	texCoor[12*i+1]=0;
         	texCoor[12*i+2]=0;
         	texCoor[12*i+3]=1;
         	texCoor[12*i+4]=1;
         	texCoor[12*i+5]=0;
         	
         	texCoor[12*i+6]=1;
         	texCoor[12*i+7]=0;
         	texCoor[12*i+8]=0;
         	texCoor[12*i+9]=1;
         	texCoor[12*i+10]=1;
         	texCoor[12*i+11]=1;
         }
         
         //�������������������ݻ���
         ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
         cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
         mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
         mTexCoorBuffer.clear();
         mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
         mTexCoorBuffer.position(0);//���û�������ʼλ��
         //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
         //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
         //���������������ݵĳ�ʼ��================end============================  
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
        //��ȡ�����ж�������������������id  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //
        muLifeSpan=GLES30.glGetUniformLocation(mProgram, "maxLifeSpan");
        //��ȡ�����а뾶����id
        muBj=GLES30.glGetUniformLocation(mProgram, "bj");
        //��ȡ��ʼ��ɫ����id
        muStartColor=GLES30.glGetUniformLocation(mProgram, "startColor");
        //��ȡ��ֹ��ɫ����id
        muEndColor=GLES30.glGetUniformLocation(mProgram, "endColor");
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
         GLES30.glUniform1f(muBj, halfSize*60);
         //����ʼ��ɫ������Ⱦ����
         GLES30.glUniform4fv(muStartColor, 1, startColor, 0);
         //����ֹ��ɫ������Ⱦ����
         GLES30.glUniform4fv(muEndColor, 1, endColor, 0);
        
         //������������������������Ⱦ����
         GLES30.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES30.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );   
         //������λ����������
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
         
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
        	//�����������
             GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
     	 } 
    }
}
