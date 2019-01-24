package com.bn.Sample4_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;

public class Mountion
{
	//��λ����
	float UNIT_SIZE=3.0f;
	
	//�Զ�����Ⱦ���ߵ�id
	int mProgram;
	//�ܱ仯�������õ�id
	int muMVPMatrixHandle;
	//����λ����������id
	int maPositionHandle;
	//��������������������id
	int maTexCoorHandle;
	
    int muMMatrixHandle;//λ�á���ת�任��������
    int maNormalHandle; //���㷨������������
    int maLightLocationHandle;//��Դλ����������
    int maCameraHandle; //�����λ���������� 
	
	//ɰʯ
	int ssTextureHandle;
	//�̲�Ƥ
	int lcpTextureHandle;
	//��·
	int dlTextureHandle;
	//�Ʋ�Ƥ
	int hcpTextureHandle;
	//RGB
	int rgbTextureHandle;
	//��ʼxֵ
	int landStartYYHandle;
	//����
	int landYSpanHandle;
	int repeatHandle;
	
	//�������ݻ���������������ݻ���
	FloatBuffer mVertexBuffer;
	FloatBuffer mTexCoorBuffer; 
	FloatBuffer mNormalBuffer;//���㷨�������ݻ���
	//��������
	int vCount=0;
	
	public Mountion(MySurfaceView mv,float[][] yArray,float [][][] normols,int rows,int cols)
	{
		initVertexData(yArray,normols,rows,cols);
		initShader(mv);
	}
	//��ʼ��������������ɫ���ݵķ���
    public void initVertexData(float[][] yArray,float [][][] normols,int rows,int cols)
    {
    	//�����������ݵĳ�ʼ��
    	vCount=cols*rows*2*3;//ÿ���������������Σ�ÿ��������3������   
        float vertices[]=new float[vCount*3];//ÿ������xyz��������
        float vnormols[]=new float[vCount*3];//ÿ������xyz��������
        int count=0;//���������
        int count2=0;//���������
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
        		
        		vnormols[count2++]=-normols[j][i][0];
        		vnormols[count2++]=-normols[j][i][1];
        		vnormols[count2++]=-normols[j][i][2];
        		
        		vertices[count++]=zsx;
        		vertices[count++]=yArray[j+1][i];
        		vertices[count++]=zsz+UNIT_SIZE;
        		
        		vnormols[count2++]=-normols[j+1][i][0];
        		vnormols[count2++]=-normols[j+1][i][1];
        		vnormols[count2++]=-normols[j+1][i][2];
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=yArray[j][i+1];
        		vertices[count++]=zsz;
        		
        		vnormols[count2++]=-normols[j][i+1][0];
        		vnormols[count2++]=-normols[j][i+1][1];
        		vnormols[count2++]=-normols[j][i+1][2];
        		
        		
        		
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=yArray[j][i+1];
        		vertices[count++]=zsz;
        		
        		vnormols[count2++]=-normols[j][i+1][0];
        		vnormols[count2++]=-normols[j][i+1][1];
        		vnormols[count2++]=-normols[j][i+1][2];
        		
        		vertices[count++]=zsx;
        		vertices[count++]=yArray[j+1][i];
        		vertices[count++]=zsz+UNIT_SIZE;
        		
        		vnormols[count2++]=-normols[j+1][i][0];
        		vnormols[count2++]=-normols[j+1][i][1];
        		vnormols[count2++]=-normols[j+1][i][2];
        		
        		vertices[count++]=zsx+UNIT_SIZE;
        		vertices[count++]=yArray[j+1][i+1];
        		vertices[count++]=zsz+UNIT_SIZE;
        		
        		vnormols[count2++]=-normols[j+1][i+1][0];
        		vnormols[count2++]=-normols[j+1][i+1][1];
        		vnormols[count2++]=-normols[j+1][i+1][2];
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
        
		//�������ƶ��㷨��������
        ByteBuffer nbb = ByteBuffer.allocateDirect(vnormols.length*4);
        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = nbb.asFloatBuffer();//ת��Ϊfloat�ͻ���
        mNormalBuffer.put(vnormols);//�򻺳����з��붥����������
        mNormalBuffer.position(0);//���û�������ʼλ�� 
    }
	
	//��ʼ��Shader�ķ���
	public void initShader(MySurfaceView mv) 
	{
		String mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
		String mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
		//���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������id  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������id  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
 
        //��ȡλ�á���ת�任��������
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix");  
        //��ȡ�����ж��㷨������������  
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�����й�Դλ������
        maLightLocationHandle=GLES30.glGetUniformLocation(mProgram, "uLightLocation");
        //��ȡ�����������λ������
        maCameraHandle=GLES30.glGetUniformLocation(mProgram, "uCamera"); 

        //����
		//ɰʯ
        ssTextureHandle=GLES30.glGetUniformLocation(mProgram, "ssTexture");
		//�̲�Ƥ
        lcpTextureHandle=GLES30.glGetUniformLocation(mProgram, "lcpTexture");
		//��·
        dlTextureHandle=GLES30.glGetUniformLocation(mProgram, "dlTexture");
        //�Ʋ�Ƥ
        hcpTextureHandle=GLES30.glGetUniformLocation(mProgram, "hcpTexture");
        //RGB
        rgbTextureHandle=GLES30.glGetUniformLocation(mProgram, "rgbTexture");
        
		repeatHandle=GLES30.glGetUniformLocation(mProgram, "repeatVaule");
	}
	
	//�Զ���Ļ��Ʒ���drawSelf
	public void drawSelf(int ssTexId,int lcpTexId,int dlTexId,int hcpTexId,int rgbTexId)
	{
		//ָ��ʹ��ĳ��shader����
   	 	GLES30.glUseProgram(mProgram); 
        //�����ձ任������shader����
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 

        //��λ�á���ת�任��������ɫ������
        GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
        //����Դλ�ô�����ɫ������   
        GLES30.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB);
        //�������λ�ô�����ɫ������   
        GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);

        
        //���Ͷ���λ������
		GLES30.glVertexAttribPointer
		(
			maPositionHandle, 
			3, 
			GLES30.GL_FLOAT, 
			false, 
			3*4, 
			mVertexBuffer
		);
		
		//���Ͷ���������������
		GLES30.glVertexAttribPointer
		(
			maTexCoorHandle, 
			2, 
			GLES30.GL_FLOAT, 
			false, 
			2*4, 
			mTexCoorBuffer
		);
		
        //�����㷨�������ݴ�����Ⱦ����
		GLES30.glVertexAttribPointer(maNormalHandle, 3, GLES30.GL_FLOAT, false,
				3 * 4, mNormalBuffer);
		//������λ����������
        GLES30.glEnableVertexAttribArray(maPositionHandle);  
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
        GLES30.glEnableVertexAttribArray(maNormalHandle);// ���ö��㷨��������

        //������
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, ssTexId);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, lcpTexId);
		GLES30.glActiveTexture(GLES30.GL_TEXTURE2);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, dlTexId);
		GLES30.glActiveTexture(GLES30.GL_TEXTURE3);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, hcpTexId);
		GLES30.glActiveTexture(GLES30.GL_TEXTURE4);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, rgbTexId);
		GLES30.glUniform1i(ssTextureHandle, 0);//ʹ��0������
        GLES30.glUniform1i(lcpTextureHandle, 1); //ʹ��1������
        GLES30.glUniform1i(dlTextureHandle, 2); //ʹ��2������
        GLES30.glUniform1i(hcpTextureHandle, 3); //ʹ��3������
        GLES30.glUniform1i(rgbTextureHandle, 4); //ʹ��4������
        
        GLES30.glUniform1f(repeatHandle,16);
        
        //�����������
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