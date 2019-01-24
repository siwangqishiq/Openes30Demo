package com.bn.Sample5_10;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static com.bn.Sample5_10.Constant.*;
import android.opengl.GLES30;

//�в���Ч�����������
public class TextureRect 
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id  
    int muMVPMatrixHandle;//�ܱ任��������    
    int muMMatrixHandle;//λ�á���ת�任����
    int maPositionHandle; //����λ���������� 
    int maTexCoorHandle; //�������������������� 
    int muMVPMatrixMirrorHandle;//����������Ĺ۲���ͶӰ��Ͼ�������
    
    int maNormalHandle; //���㷨������������  
    int maLightLocationHandle;//��Դ��������
    int maCameraHandle; //�����λ���������� 
    
    int uDYTexHandle;//��Ӱ������������
    int uWaterTexHandle;//ˮ����������������
    int uNormalTexHandle;//����������������
    
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���
	FloatBuffer   mNormalBuffer;//���㷨�������ݻ���
    IntBuffer mIndicesBuffer;
    
    public  float mytime;//��ʱ��	
	float[] zero1;//1�Ų���Դ
	float[] zero2;//2�Ų���Դ
	float[] zero3;//3�Ų���Դ
	
    int[] indices;//��������
    float vertices[]; //��������
    float normals[];//������
    float texCoor[];//����
    
    float[] verticesForCal;//���ڼ���Ķ���λ����������
    float[] normalsForCal;//���ڼ���Ķ��㷨��������
    
   	int iCount;
    int vCount=0;   //��������
    
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
        	int cols=64;//����
        	int rows=64;//����	       
	       	float UNIT_SIZE=WIDTH_SPAN/(cols-1);//ÿ��ĵ�λ����
	        ArrayList<Integer> alVertixIndice=new ArrayList<Integer>();//��Ŷ��������ArrayList
	        ArrayList<Float> alVertixV=new ArrayList<Float>();//��Ŷ���λ�������ArrayList
	        ArrayList<Float> alVertixN=new ArrayList<Float>();//��Ŷ��㷨������ArrayList
	        ArrayList<Float> alVertixT=new ArrayList<Float>();//��Ŷ������������ArrayList
           
        
	        for(int j = 0; j <rows;j++){//��
	       		for(int i = 0; i< cols; i++){//��
	       		//���㵱ǰ�������ϲ������ 
	           		float zsx=-WIDTH_SPAN/2+i*UNIT_SIZE;
	           		float zsz=-WIDTH_SPAN/2+j*UNIT_SIZE;
	           		float zsy=0;
	           		
	           		alVertixV.add(zsx);alVertixV.add(zsy);alVertixV.add(zsz);
	           		alVertixN.add(0.0f);alVertixN.add(1.0f);alVertixN.add(0.0f);
	           		float s=zsx/WIDTH_SPAN+0.5f;
	           		float t=zsz/WIDTH_SPAN+0.5f;
	           		alVertixT.add(s);alVertixT.add(t);	           		
	       		}
	        }
	    
	       for(int i = 0; i <(rows-1); i++){//��
	       		for(int j = 0; j < (cols-1); j++){//��
	       			
	       			int x = i * rows + j;
	       			alVertixIndice.add(x);
	       			alVertixIndice.add(x + cols);
	       			alVertixIndice.add(x + 1);
	       			
	       			alVertixIndice.add(x + 1);
	       			alVertixIndice.add(x + cols);
	       			alVertixIndice.add(x + cols + 1);
	       		}
	       	}
	    	vCount=alVertixV.size()/3;//ÿ���������������Σ�ÿ��������3������
	    	
	        vertices=new float[vCount*3];//ÿ������xyz��������
	        texCoor=new float[vCount*2];
	        normals=new float[vCount*3];
	        verticesForCal=new float[vCount*3];
	        normalsForCal=new float[vCount*3];
	        iCount=alVertixIndice.size();
	        indices=new int[alVertixIndice.size()];
	        for(int i=0;i<alVertixIndice.size();i++)
	        {
        	   indices[i]=alVertixIndice.get(i);
	        }
           
	        for(int i=0;i<alVertixV.size();i++)
	        {
        	   vertices[i]=alVertixV.get(i);
	        }
           
	        for(int i=0;i<alVertixN.size();i++)
	        {
        	   normals[i]=alVertixN.get(i);
	        }
        
	        for(int i=0;i<alVertixT.size();i++)
	        {
        	   texCoor[i]= alVertixT.get(i);
	        }     
           
           
	       //��ʼ��4��������Դλ��
	       zero1=new float[]{wave1PositionX,wave1PositionY,wave1PositionZ};
	       zero2=new float[]{wave2PositionX,wave2PositionY,wave2PositionZ};
	       zero3=new float[]{wave3PositionX,wave3PositionY,wave3PositionZ};
	       
	        //���㶥���������ݵĳ�ʼ��================start============================
	       //���������������ݻ���
	       //vertices.length*4����Ϊһ�������ĸ��ֽ�
	       ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
	       vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	       mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
	       mVertexBuffer.put(vertices);//�򻺳����з��붥����������
	       mVertexBuffer.position(0);//���û�������ʼλ��
	        //���㶥���������ݵĳ�ʼ��================end============================
	       
	       //���������������ݵĳ�ʼ��================begin============================   
	       //�������������������ݻ���
	       ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
	       cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	       mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
	       mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
	       mTexCoorBuffer.position(0);//���û�������ʼλ��
	        //���㶥�������������ݵĳ�ʼ��================end============================
	       
	       //���㷨�������ݵĳ�ʼ��================begin============================  
	       	ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
	        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mNormalBuffer = nbb.asFloatBuffer();//ת��ΪFloat�ͻ���
	        mNormalBuffer.put(normals);//�򻺳����з��붥�㷨��������
	        mNormalBuffer.position(0);//���û�������ʼλ��
	        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
	        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
	        //���㷨�������ݵĳ�ʼ��================end============================
	        
	        
	        //�����������ݵĳ�ʼ��================begin============================  
	        ByteBuffer  ibb = ByteBuffer.allocateDirect(indices.length*4);
	        ibb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
	        mIndicesBuffer = ibb.asIntBuffer();//ת��ΪFloat�ͻ���
	        mIndicesBuffer.put(indices);//�򻺳����з��붥�㷨��������
	        mIndicesBuffer.position(0);//���û�������ʼλ��
	      //�����������ݵĳ�ʼ��================end============================
    }
    
   
    //��ʼ����ɫ���ķ���
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        String mVertexShader=ShaderUtil.loadFromAssetsFile("water_vertex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        String mFragmentShader=ShaderUtil.loadFromAssetsFile("water_frag.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram= ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡλ�á���ת�任��������
        muMMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMMatrix"); 
        //��ȡ�����ж�������������������  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor"); 
        //��ȡ����������Ĺ۲���ͶӰ��Ͼ�������
        muMVPMatrixMirrorHandle=GLES30.glGetUniformLocation(mProgram, "uMVPMatrixMirror");        
        //��ȡ��Ӱ��������
        uDYTexHandle=GLES30.glGetUniformLocation(mProgram, "sTextureDY");  
        //��ȡˮ���������������
        uWaterTexHandle=GLES30.glGetUniformLocation(mProgram, "sTextureWater");
        //��ȡ��������������
        uNormalTexHandle=GLES30.glGetUniformLocation(mProgram, "sTextureNormal");          
        //��ȡ�����ж��㷨������������  
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�����й�Դλ������
        maLightLocationHandle=GLES30.glGetUniformLocation(mProgram, "uLightLocation"); 
        //��ȡ�����������λ������
        maCameraHandle=GLES30.glGetUniformLocation(mProgram, "uCamera"); 
    }
    
    
    public void drawSelf(int texId,int waterId,int textureIdNormal,float[] mMVPMatrixMirror)
    {
		synchronized(lock){
    		updateData();
    	}
    	//ָ��ʹ��ĳ��shader����
    	GLES30.glUseProgram(mProgram); 
    	//�����ձ任��������Ⱦ����
    	GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
    	//������������Ĺ۲���ͶӰ��Ͼ�������Ⱦ����
        GLES30.glUniformMatrix4fv(muMVPMatrixMirrorHandle, 1, false, mMVPMatrixMirror, 0);
    	//��λ�á���ת�任��������Ⱦ����
    	GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);   
		//����Դλ�ô�����Ⱦ����
    	GLES30.glUniform3fv(maLightLocationHandle, 1, MatrixState.lightPositionFB); 
        //�������λ�ô�����Ⱦ����
    	GLES30.glUniform3fv(maCameraHandle, 1, MatrixState.cameraFB);
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
    	
    	GLES30.glEnableVertexAttribArray(maPositionHandle);  //���ö���λ����������
    	GLES30.glEnableVertexAttribArray(maTexCoorHandle);  //���ö�������������������
        GLES30.glEnableVertexAttribArray(maNormalHandle);  //���ö��㷨������������
    	//������
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);//����0������
    	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);//ˮ�浹Ӱ����
    	GLES30.glActiveTexture(GLES30.GL_TEXTURE1);//����1������
    	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, waterId); //ˮ����������
    	GLES30.glActiveTexture(GLES30.GL_TEXTURE2);//����2������
    	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureIdNormal);//����������
    	GLES30.glUniform1i(uDYTexHandle, 0);//ʹ��0������
    	GLES30.glUniform1i(uWaterTexHandle, 1); //ʹ��1������
    	GLES30.glUniform1i(uNormalTexHandle, 2);  //ʹ��2������  
    	//�������η�ʽִ�л���
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, iCount, GLES30.GL_UNSIGNED_INT, mIndicesBuffer);
    	
    }
    //���¶������ݺͷ��������ݵĻ�������
    public void updateData()
    {
    	//�����������ݵĳ�ʼ��================begin============================  
    	//���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
    	//�����������ݵĳ�ʼ��================end============================  
        
        //���㷨�������ݵĳ�ʼ��================begin============================  
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = nbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥�㷨��������
        mNormalBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���㷨�������ݵĳ�ʼ��================end============================
    }
    //���㶥�����ꡢ������
    public void calVerticesNormalAndTangent()
    {
    	//���㶥������
    	for(int i=0;i<vCount;i++)
    	{
    		verticesForCal[i*3]=vertices[i*3];
    		verticesForCal[i*3+1]=findHeight(vertices[i*3],vertices[i*3+2]);
    		verticesForCal[i*3+2]=vertices[i*3+2];
    	}
    	//���㷨����
    	normalsForCal=CalNormal.calNormal(verticesForCal, indices);
    	
        synchronized (lock) {
        	vertices=Arrays.copyOf(verticesForCal,verticesForCal.length);   
        	normals=Arrays.copyOf(normalsForCal,normalsForCal.length);    	 
		}
    }    
  
    //����3�����Զ����Ӱ��֮��ĸ߶�ֵ
    public float findHeight(float x,float z)
    {
    	float result=0;
    	//��ȡ�㵽���ĵľ���
    	float distance1=(float) Math.sqrt((x-zero1[0])*(x-zero1[0])+(z-zero1[2])*(z-zero1[2]));
    	//�������2�Ų���ʼλ�õľ���
    	float distance2=(float) Math.sqrt((x-zero2[0])*(x-zero2[0])+(z-zero2[2])*(z-zero2[2]));
    	//�������3�Ų���ʼλ�õľ���
    	float distance3=(float) Math.sqrt((x-zero3[0])*(x-zero3[0])+(z-zero3[2])*(z-zero3[2]));
    	
    	result= (float) (Math.sin((distance1) * waveFrequency1 * Math.PI + mytime) *waveAmplitude1);		//���ö���߶�
    	result=(float) (result+Math.sin((distance2) * waveFrequency2 * Math.PI + mytime)*waveAmplitude2);//���ö���߶�
    	result=(float) (result+Math.sin((distance3) * waveFrequency3 * Math.PI + mytime) *waveAmplitude3);//���ö���߶�
    	return result;
    }
}
