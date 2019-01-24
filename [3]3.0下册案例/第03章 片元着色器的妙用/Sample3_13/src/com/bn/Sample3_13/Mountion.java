package com.bn.Sample3_13;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;

public class Mountion
{
	//��λ����
	float UNIT_SIZE=3.0f;
	//����������ƽ��ĸ߶�
	float TJ_GOG_SLAB_Y=8f;
	//�Ŷ���ʼ��
	float startAngle=0;
	
	//�Զ�����Ⱦ���ߵ�id
	int mProgram;
	//�ܱ仯�������õ�id
	int muMVPMatrixHandle;
	//�����任�����õ�id
	int muMMatrixHandle;
	//�����λ�����õ�id
	int muCamaraLocationHandle;
	//����������ƽ��߶����õ�id
	int slabYHandle;
	//�����߶��Ŷ���ʼ�����õ�id
	int startAngleHandle;
	//����λ����������id
	int maPositionHandle;
	//��������������������id
	int maTexCoorHandle;	
	
	//�ݵص�id
	int sTextureGrassHandle;
	//ʯͷ��id
	int sTextureRockHandle;
	//��ʼxֵ
	int landStartYYHandle;
	//����
	int landYSpanHandle;
	
	//�������ݻ���������������ݻ���
	FloatBuffer mVertexBuffer;
	FloatBuffer mTexCoorBuffer; 
	//��������
	int vCount=0;
	
	public Mountion(MySurfaceView mv,float[][] yArray,int rows,int cols)
	{
		initVertexData(yArray,rows,cols);
		initShader(mv);
	}
	//��ʼ��������������ɫ���ݵķ���
    public void initVertexData(float[][] yArray,int rows,int cols)
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
        //��ȡ�������ܱ任�������õ�id
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix"); 
        //��ȡ�����л����任�������õ�id
        muMMatrixHandle=GLES30.glGetUniformLocation(mProgram, "uMMatrix"); 
        //��ȡ�����������λ�����õ�id
        muCamaraLocationHandle=GLES30.glGetUniformLocation(mProgram, "uCamaraLocation"); 
        //��ȡ����������������ƽ��߶����õ�id
        slabYHandle=GLES30.glGetUniformLocation(mProgram, "slabY"); 
        //��ȡ�����������߶��Ŷ���ʼ�����õ�id
        startAngleHandle=GLES30.glGetUniformLocation(mProgram, "startAngle"); 
        
        //����
		//�ݵ�
		sTextureGrassHandle=GLES30.glGetUniformLocation(mProgram, "sTextureGrass");
		//ʯͷ
		sTextureRockHandle=GLES30.glGetUniformLocation(mProgram, "sTextureRock");
		//xλ��
		landStartYYHandle=GLES30.glGetUniformLocation(mProgram, "landStartY");
		//x���
		landYSpanHandle=GLES30.glGetUniformLocation(mProgram, "landYSpan");
	}
	
	//�Զ���Ļ��Ʒ���drawSelf
	public void drawSelf(int texId,int rock_textId)
	{
		//ָ��ʹ��ĳ����ɫ������
   	 	GLES30.glUseProgram(mProgram); 
        //�����ձ任��������Ⱦ����
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        //�������任��������Ⱦ����
        GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0); 
        //�������λ�ô�����Ⱦ����
        GLES30.glUniform3fv(muCamaraLocationHandle, 1,MatrixState.cameraFB);
        
        
        //����������ƽ��߶ȴ�����Ⱦ����
        GLES30.glUniform1f(slabYHandle, TJ_GOG_SLAB_Y);  
        //��������Ŷ���ʼ�Ǵ�����Ⱦ����
        GLES30.glUniform1f(startAngleHandle, (float) Math.toRadians(startAngle));  
        //�޸��Ŷ��ǵ�ֵ��ÿ�μ�3��ȡֵ��Χ��Զ��0~360�ķ�Χ��
        startAngle=(startAngle+3f)%360.0f;
        
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
		//������λ����������
        GLES30.glEnableVertexAttribArray(maPositionHandle);  
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
        
        //������
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
        GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, rock_textId);
		GLES30.glUniform1i(sTextureGrassHandle, 0);//ʹ��0������
        GLES30.glUniform1i(sTextureRockHandle, 1); //ʹ��1������
        
        //������Ӧ��x����
        GLES30.glUniform1f(landStartYYHandle, 0);
        GLES30.glUniform1f(landYSpanHandle, 50);
        
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