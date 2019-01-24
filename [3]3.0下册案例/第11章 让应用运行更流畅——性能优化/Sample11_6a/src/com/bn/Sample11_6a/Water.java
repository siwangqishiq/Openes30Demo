package com.bn.Sample11_6a;

import static com.bn.Sample11_6a.ShaderUtil.createProgram;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import android.opengl.GLES31;

//����������
public class Water 
{	
	int mProgram;//�Զ�����Ⱦ���߳���id
    int muMVPMatrixHandle;//�ܱ任��������id
    int muMMatrixHandle;//λ�á���ת�任��������
    int muLightLocationHandle;//��Դλ����������
    int muCameraHandle; //�����λ���������� 
    int mutexCoorOffsetHandle;//ÿ֡����������ƫ������id
    
    int maPositionHandle; //����λ����������id  
    int maNormalHandle; //���㷨������������id  
    int maTexCoorHandle; //��������������������id  
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
	
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mNormalBuffer;//�����������ݻ���
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���
	IntBuffer   mIndexBuffer;//��װ��������
	
    int vCount=0;   
    int iCount=0;

    //�������Ҳ��ε���ʼ��
    float qsj1=0;
    float qsj2=90;
    float qsj3=45;
    
    float[] bx1={50,150};
    float bc1=32;
    float zf1=0.8f;
    
    float[] bx2={10,40};
    float bc2=24;
    float zf2=1;
    
    float[] bx3={200,200};
    float bc3=60;
    float zf3=2;
    
    //ÿ֡����������ƫ����
    float texCoorOffset=0.0f;
    
    public Water(MySurfaceView mv)
    {    	
    	//��ʼ��������������ɫ����
    	initVertexData();
    	//��ʼ����ɫ��        
    	initShader(mv);
    }
    float vertices0[];
    //���¶������ݵķ���
    public void updateVertexData()
    {
    	 //��1��������ʼ��
  	     qsj1=(qsj1+9)%360;
  	     float temp1=(float)Math.toRadians(qsj1);
  	     //��2��������ʼ��
   	     qsj2=(qsj2+9)%360;
   	     float temp2=(float)Math.toRadians(qsj2);
   	     //��3��������ʼ��
   	     qsj3=(qsj3+4.0f)%360;
   	     float temp3=(float)Math.toRadians(qsj3);
   	     int tempCount=0;
   	     for(int j=0;j<=Constant.WATER_HEIGHT;j++)
   	     {
   	    	 for(int i=0;i<=Constant.WATER_WIDTH;i++)
   	    	 {
   	    		float[] ddxz={vertices0[tempCount*3],vertices0[tempCount*3+2]};
   	    		vertices0[tempCount*3+1]=Constant.calHdr(bx1,bc1,zf1,temp1,ddxz)+
   	    				Constant.calHdr(bx2,bc2,zf2,temp2,ddxz)+
                           Constant.calHdr(bx3,bc3,zf3,temp3,ddxz);
   	    		
   	    		normals0[tempCount*3]=0;
   	    		normals0[tempCount*3+1]=0;
   	    		normals0[tempCount*3+2]=0;
   	    		
   	    		tempCount++;
   	    	 }
   	     }
    }
    
    int indexs0[];
    float normals0[];
    //���·���������
    public void updateNormalData()
    {
    	int tempCount=0;
    	for(int i=0;i<Constant.WATER_WIDTH;i++)
    	{
    		for(int j=0;j<Constant.WATER_HEIGHT;j++)
    		{
				//��0�ŵ㵽1�ŵ������
	      		float vxa=vertices0[indexs0[tempCount*6+1]*3]-vertices0[indexs0[tempCount*6]*3];
	      		float vya=vertices0[indexs0[tempCount*6+1]*3+1]-vertices0[indexs0[tempCount*6]*3+1];
	      		float vza=vertices0[indexs0[tempCount*6+1]*3+2]-vertices0[indexs0[tempCount*6]*3+2];
	      	    //��0�ŵ㵽2�ŵ������
	      		float vxb=vertices0[indexs0[tempCount*6+2]*3]-vertices0[indexs0[tempCount*6]*3];
	      		float vyb=vertices0[indexs0[tempCount*6+2]*3+1]-vertices0[indexs0[tempCount*6]*3+1];
	      		float vzb=vertices0[indexs0[tempCount*6+2]*3+2]-vertices0[indexs0[tempCount*6]*3+2];		
    			float[] nor=Constant.vectorNormal(Constant.getCrossProduct(vxa,vya,vza,vxb,vyb,vzb));
    			
    			normals0[indexs0[tempCount*6]*3]+=nor[0];
    			normals0[indexs0[tempCount*6]*3+1]+=nor[1];
    			normals0[indexs0[tempCount*6]*3+2]+=nor[2];
    			
    			normals0[indexs0[tempCount*6+1]*3]+=nor[0];
    			normals0[indexs0[tempCount*6+1]*3+1]+=nor[1];
    			normals0[indexs0[tempCount*6+1]*3+2]+=nor[2];
    			
    			normals0[indexs0[tempCount*6+2]*3]+=nor[0];
    			normals0[indexs0[tempCount*6+2]*3+1]+=nor[1];
    			normals0[indexs0[tempCount*6+2]*3+2]+=nor[2];
	      		
    			//��0�ŵ㵽1�ŵ������
	      		vxa=vertices0[indexs0[tempCount*6+4]*3]-vertices0[indexs0[tempCount*6+3]*3];
	      		vya=vertices0[indexs0[tempCount*6+4]*3+1]-vertices0[indexs0[tempCount*6+3]*3+1];
	      		vza=vertices0[indexs0[tempCount*6+4]*3+2]-vertices0[indexs0[tempCount*6+3]*3+2];
	      	    //��0�ŵ㵽2�ŵ������
	      		vxb=vertices0[indexs0[tempCount*6+5]*3]-vertices0[indexs0[tempCount*6+3]*3];
	      		vyb=vertices0[indexs0[tempCount*6+5]*3+1]-vertices0[indexs0[tempCount*6+3]*3+1];
	      		vzb=vertices0[indexs0[tempCount*6+5]*3+2]-vertices0[indexs0[tempCount*6+3]*3+2];		
    			float[] nor0=Constant.vectorNormal(Constant.getCrossProduct(vxa,vya,vza,vxb,vyb,vzb));
    			
    			normals0[indexs0[tempCount*6+3]*3]+=nor0[0];
    			normals0[indexs0[tempCount*6+3]*3+1]+=nor0[1];
    			normals0[indexs0[tempCount*6+3]*3+2]+=nor0[2];
    			
    			normals0[indexs0[tempCount*6+4]*3]+=nor0[0];
    			normals0[indexs0[tempCount*6+4]*3+1]+=nor0[1];
    			normals0[indexs0[tempCount*6+4]*3+2]+=nor0[2];
    			
    			normals0[indexs0[tempCount*6+5]*3]+=nor[0];
    			normals0[indexs0[tempCount*6+5]*3+1]+=nor[1];
    			normals0[indexs0[tempCount*6+5]*3+2]+=nor[2];
    			
	      		tempCount++;
    		}
    	}
    	
    }
   
    public void update()
    {
    	mVertexBuffer.clear();//��ն����������ݻ���
        mVertexBuffer.put(vertices0);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
        
        mNormalBuffer.clear();
        mNormalBuffer.put(normals0);//�򻺳����з��붥����������
        mNormalBuffer.position(0);//���û�������ʼλ��        
    }
    //��ʼ��������������ɫ���ݵķ���
    public void initVertexData()
    {
    	//�����������ݵĳ�ʼ��================begin============================
    	vCount=(Constant.WATER_WIDTH+1)*(Constant.WATER_HEIGHT+1);
    	float vertices[]=new float[vCount*3];
    	int tempCount=0;
    	
    	for(int j=0;j<=Constant.WATER_HEIGHT;j++)
    	{
    		for(int i=0;i<=Constant.WATER_WIDTH;i++)
    		{
    			float x=Constant.WATER_UNIT_SIZE*i;
    			float z=Constant.WATER_UNIT_SIZE*j;
    			float y=0;
    			vertices[tempCount*3]=x;
    			vertices[tempCount*3+1]=y;
    			vertices[tempCount*3+2]=z;
    			tempCount++; 
    		}
    	} 	
    	vertices0=vertices;
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
        float normals[]=new float[vCount*3];
    	tempCount=0;
    	
    	for(int j=0;j<=Constant.WATER_HEIGHT;j++)
    	{
    		for(int i=0;i<=Constant.WATER_WIDTH;i++)
    		{    			
    			normals[tempCount*3]=0;
    			normals[tempCount*3+1]=1;
    			normals[tempCount*3+2]=0;
    			tempCount++; 
    		}
    	} 	
    	normals0=normals;
        //���������������ݻ���
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer nbb = ByteBuffer.allocateDirect(normals.length*4);
        nbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = nbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥����������
        mNormalBuffer.position(0);//���û�������ʼλ��        
        //���㷨�������ݵĳ�ʼ��================end============================
        
        
        //���������������ݵĳ�ʼ��================begin============================
        float texCoor[]=new float[vCount*2];
        tempCount=0;
        
        for(int j=0;j<=Constant.WATER_HEIGHT;j++)
    	{
        	for(int i=0;i<=Constant.WATER_WIDTH;i++)
    		{
    			float s=(1.0f/Constant.WATER_WIDTH)*i;
    			float t=(1.0f/Constant.WATER_HEIGHT)*j;

    			texCoor[tempCount*2]=s;
    			texCoor[tempCount*2+1]=t;
    			tempCount++;
    		}
    	}
          
        //�������������������ݻ���
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
        mTexCoorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���������������ݵĳ�ʼ��================end============================
        
        //������װ�������ݵĳ�ʼ��================begin============================
        iCount=Constant.WATER_WIDTH*Constant.WATER_HEIGHT*6;
        int[] indexs=new int[iCount];
        tempCount=0;
        for(int i=0;i<Constant.WATER_WIDTH;i++)
    	{
    		for(int j=0;j<Constant.WATER_HEIGHT;j++)
    		{
    			//0---1
    			//| / |
    			//3---2
    			int widthTemp=Constant.WATER_WIDTH+1;
    			int index0=j*widthTemp+i;   
    			int index1=index0+1; 
    			int index2=index0+1+widthTemp;
    			int index3=index0+widthTemp;
    			
    			//0-3-1
    			indexs[tempCount*6]=index0;
    			indexs[tempCount*6+1]=index3;
    			indexs[tempCount*6+2]=index1;
    			
    			//1-3-2
				indexs[tempCount*6+3]=index1;
    			indexs[tempCount*6+4]=index3;
    			indexs[tempCount*6+5]=index2;
    			
    			tempCount++;
    		}
    	}
        indexs0=indexs;
        ByteBuffer ibb = ByteBuffer.allocateDirect(indexs.length*4);
        ibb.order(ByteOrder.nativeOrder());//�����ֽ�˳��        
        mIndexBuffer=ibb.asIntBuffer();
        mIndexBuffer.put(indexs);
        mIndexBuffer.position(0);
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
        maPositionHandle = GLES31.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж��㷨������������id  
        maNormalHandle = GLES31.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�����ж�������������������id  
        maTexCoorHandle= GLES31.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������id
        muMVPMatrixHandle = GLES31.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡ������λ�á���ת�任��������id
        muMMatrixHandle = GLES31.glGetUniformLocation(mProgram, "uMMatrix");
        //��ȡ�����й�Դλ����������id
        muLightLocationHandle = GLES31.glGetUniformLocation(mProgram, "uLightLocation");
        //��ȡ�����������λ���������� id
        muCameraHandle = GLES31.glGetUniformLocation(mProgram, "uCamera"); 
        //��ȡ��������������ƫ���������� id
        mutexCoorOffsetHandle = GLES31.glGetUniformLocation(mProgram, "utexCoorOffset"); 
    }
    
    public void drawSelf(int texId)
    {     
    	 updateVertexData();
    	 updateNormalData();
    	 update();
    	 //�ƶ�ʹ��ĳ��shader����
    	 GLES31.glUseProgram(mProgram);        
    	 
    	 MatrixState.setInitStack();
    	 
         //������Z������λ��1
         MatrixState.translate(0, 0, 1);

         //�����ձ任������shader����
         GLES31.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //��λ�á���ת�任��������ɫ������
         GLES31.glUniformMatrix4fv(muMMatrixHandle, 1, false, MatrixState.getMMatrix(), 0);
         //����Դλ�ô�����ɫ������   
         GLES31.glUniform3fv(muLightLocationHandle, 1, MatrixState.lightPositionFB);
         //�������λ�ô�����ɫ������   
         GLES31.glUniform3fv(muCameraHandle, 1, MatrixState.cameraFB);   
         //����������ƫ�ƴ�����ɫ������
         texCoorOffset=(texCoorOffset+0.001f)%10.0f;
         GLES31.glUniform1f(mutexCoorOffsetHandle,texCoorOffset);    
         
         //��������������������Ⱦ����
         GLES31.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES31.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );    
         //�����㷨��������������Ⱦ����
         GLES31.glVertexAttribPointer  
         (
        		maNormalHandle,   
         		3, 
         		GLES31.GL_FLOAT, 
         		false,
                3*4,   
                mNormalBuffer
         );             
         //������������������������Ⱦ����
         GLES31.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES31.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );
         //������λ����������
         GLES31.glEnableVertexAttribArray(maPositionHandle);  
         GLES31.glEnableVertexAttribArray(maNormalHandle);  
         GLES31.glEnableVertexAttribArray(maTexCoorHandle);  
         
         //������
         GLES31.glActiveTexture(GLES31.GL_TEXTURE0);
         GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, texId);

         GLES31.glDrawElements(GLES31.GL_TRIANGLES, iCount, GLES31.GL_UNSIGNED_INT, mIndexBuffer);
    }
}
