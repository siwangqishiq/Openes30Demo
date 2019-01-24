package com.bn.Sample5_12;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.annotation.SuppressLint;
import android.opengl.GLES30;
@SuppressLint("NewApi") 
public class Mountain
{
	float Size;
	//�Զ�����Ⱦ���ߵ�id
	int mProgram;
	//�ܱ仯�������õ�
	int muMVPMatrixHandle;
	//����λ����������
	int maPositionHandle;
	//��������������������
	int maTexCoorHandle;
	
	//�������ݻ���������������ݻ���
	FloatBuffer mVertexBuffer;
	FloatBuffer mTexCoorBuffer; 
	//��������
	int vCount=0;
	
	public Mountain(MySurfaceView mv,float[][] yArray,int rows,int cols)
	{
		if(mv.SCREEN_HEIGHT>960&&mv.SCREEN_WIDTH>512)
		{
			Size=0.95f;
		}else{
			Size=1.9f;
		}
		initVertexData(yArray,rows,cols,Size);
		initShader(mv);
	}
	//��ʼ���������ݵķ���
    public void initVertexData(float[][] yArray,int rows,int cols,float UNIT_SIZE)
    {
    	//�����������ݵĳ�ʼ��
    	vCount=cols*rows*2*3;//ÿ���������������Σ�ÿ��������3������   
        float vertices[]=new float[vCount*3];//ÿ������xyz��������
        int count=0;//���������
        for(int j=0;j<rows;j++)
        {
        	for(int i=0;i<cols;i++) 
        	{        		
        		//���㵱ǰ�������ϲ������ 
        		float zsx=-UNIT_SIZE*cols/2+i*UNIT_SIZE;
        		float zsz=-UNIT_SIZE*rows/2+j*UNIT_SIZE;
        		
        		vertices[count++]=zsx;
        		vertices[count++]=yArray[j][i];
        		vertices[count++]=zsz;
        		
        		vertices[count++]=zsx;
        		vertices[count++]=yArray[j+1][i];
        		vertices[count++]=zsz+UNIT_SIZE;
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=yArray[j][i+1];
        		vertices[count++]=zsz;
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=yArray[j][i+1];
        		vertices[count++]=zsz;
        		
        		vertices[count++]=zsx;
        		vertices[count++]=yArray[j+1][i];
        		vertices[count++]=zsz+UNIT_SIZE;
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=yArray[j+1][i+1];
        		vertices[count++]=zsz+UNIT_SIZE;
        	}
        }
		
        //���������������ݻ���
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��

        //���������������ݵĳ�ʼ��
        float[] texCoor=generateTexCoor(cols,rows);
        //�������������������ݻ���
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
        mTexCoorBuffer.position(0);//���û�������ʼλ��
    }
	
	//��ʼ����ɫ���ķ���
	public void initShader(MySurfaceView mv) 
	{
		String mVertexShader=ShaderUtil.loadFromAssetsFile("vertex_mountain.sh", mv.getResources());
		String mFragmentShader=ShaderUtil.loadFromAssetsFile("frag_mountain.sh", mv.getResources());
		//���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
	}
	
	//�Զ���Ļ��Ʒ���drawSelf
	public void drawSelf(int texId)
	{
		//ָ��ʹ��ĳ����ɫ������
   	 	GLES30.glUseProgram(mProgram); 
        //�����ձ任��������Ⱦ����
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
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
		//������λ����������
        GLES30.glEnableVertexAttribArray(maPositionHandle);  
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
        //������
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
        //���Ƶ���
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
	}
	//�Զ��з����������������ķ���
    public float[] generateTexCoor(int bw,int bh)
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=16.0f/bw;//����
    	float sizeh=16.0f/bh;//����
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