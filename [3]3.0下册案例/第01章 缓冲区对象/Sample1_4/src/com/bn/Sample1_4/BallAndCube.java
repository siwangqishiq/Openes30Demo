package com.bn.Sample1_4;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import android.opengl.GLES30;

//��ʾΪ��ͨ������
public class BallAndCube
{	
	int mProgram;//�Զ�����Ⱦ���߳���id
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������
    int maTexCoorHandle; //��������������������
    String mVertexShader;//������ɫ������ű�
    String mFragmentShader;//ƬԪ��ɫ������ű�
    
    int vCount=0; //��������
    int iCount=0;
	float yAngle = 0;// ��y����ת�ĽǶ�
	float xAngle = 0;// ��x����ת�ĽǶ�
	float zAngle = 0;// ��z����ת�ĽǶ�
	
	FloatBuffer mVertexMappedBuffer;//��������ӳ�仺���Ӧ�Ķ����������ݻ���
	FloatBuffer mTexCoorMappedBuffer;//��������ӳ�仺���Ӧ�Ķ����������ݻ���
    IntBuffer mIndicesBuffer;
	
	int mVertexBufferId;//�����������ݻ��� id
	int mTexCoorBufferId;//�����������ݻ���id
	int mIndicesBufferId;
	
	ByteBuffer vbb1;//�����������ݵ�ӳ�仺��
	float[]vertices;//ԭʼ��Ķ�����������
	float[] verticesCube;//ԭʼ������Ķ�����������
	int[] indices;
	float[] texCoors;//��������
    public float[] curBallForDraw;
    public float[] curBallForCal;
    float span=30;//�з�Ϊ30��
    
	ArrayList<Float> alVertix=new ArrayList<Float>();//��Ŷ��������ArrayList
	ArrayList<Float> alVertix1=new ArrayList<Float>();//��Ŷ�Ӧ������Ķ��������ArrayList
	ArrayList<Float> alVertixTexCoor=new ArrayList<Float>();//������������ArrayList
	ArrayList<Integer> alVertixIndice=new ArrayList<Integer>();//��Ŷ��������ArrayList
	
	ArrayList<Float> alVertix2=new ArrayList<Float>();//��ʱ��Ŷ��������ArrayList
	ArrayList<Float> alVertixCube2=new ArrayList<Float>();//��ʱ��Ŷ�Ӧ������Ķ��������ArrayList
	ArrayList<Float> alVertixTexCoor2=new ArrayList<Float>();//��ʱ������������ArrayList
	
	Object lock=new Object();//������
    public BallAndCube(MySurfaceView mv,float r)
    {
    	//���㶥������
    	initYSData(r);
    	//���ó�ʼ���������ݵķ���
    	initVertexData();
    	//���ó�ʼ����ɫ���ķ���
    	initShader(mv);
    }
    
    public void initYSData(float r)
    {
    	//�����������ݵĳ�ʼ��================begin============================    	
    	final float UNIT_SIZE=0.5f;
    	final float angleSpan=5;//������е�λ�зֵĽǶ�
		float length=(float) (r*UNIT_SIZE*Math.sin(Math.toRadians(45)));//������߳���һ��
		float length2=length*2;//������߳�
    	for(float vAngle=90;vAngle>=-90;vAngle=vAngle-angleSpan)//��ֱ����angleSpan��һ��
        {
        	for(float hAngle=360;hAngle>0;hAngle=hAngle-angleSpan)//ˮƽ����angleSpan��һ��
        	{//����������һ���ǶȺ�����Ӧ�Ĵ˵��������ϵ�����
        		//��ǰ�� ====================start====================
        		double xozLength=r*UNIT_SIZE*Math.cos(Math.toRadians(vAngle));
        		//�����ϵ�һ������
        		float x1=(float)(xozLength*Math.cos(Math.toRadians(hAngle)));
        		float z1=(float)(xozLength*Math.sin(Math.toRadians(hAngle)));
        		float y1=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(vAngle)));
        		
        		//��Ӧ�������ϵĵ�һ������
        		float x10=x1;
        		float z10=z1;
        		float y10=y1;
        		        		
        		//��һ���������������
        		float s1=0;
        		float t1=0;
        		
        		//������������ٽ��Ե
        		if(vAngle==50||vAngle==-45)
        		{
        			float x1Temp=0;float z1Temp=0;float y1Temp=0;//���ϵĶ���        			
        			float x10Temp=0;float z10Temp=0;	float y10Temp=0;     //��Ӧ�������ϵĶ���   			
        			float s2=0;float t2=0;//��������
        			float xozLengthTemp=0;
        			
        			if(vAngle==50)
        			{
        				//��vAngle����50ʱ����������45ʱ��һȦ���㣬���Լ���45��ʱ��Ķ��㡢��Ӧ�������ϵĶ��㡢��������
        				xozLengthTemp=(float) (r*UNIT_SIZE*Math.cos(Math.toRadians(45)));
            			x1Temp=(float)(xozLengthTemp*Math.cos(Math.toRadians(hAngle)));
            			z1Temp=(float)(xozLengthTemp*Math.sin(Math.toRadians(hAngle)));
            			y1Temp=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(45)));            			
            			y10Temp=length;        
        			}
        			else if(vAngle==-45)
        			 { 
        				//��vAngle����-45ʱ����������-45ʱ��һȦ���㣬���Լ���-45��ʱ��Ķ��㡢��Ӧ�������ϵĶ��㡢��������
        				xozLengthTemp=(float) (r*UNIT_SIZE*Math.cos(Math.toRadians(-45)));
            			x1Temp=(float)(xozLengthTemp*Math.cos(Math.toRadians(hAngle)));
            			z1Temp=(float)(xozLengthTemp*Math.sin(Math.toRadians(hAngle)));
            			y1Temp=(float)(r*UNIT_SIZE*Math.sin(Math.toRadians(-45)));            			
            			y10Temp=-length;        	
        			}      
        			 
         			if(Math.abs(x1Temp)>Math.abs(z1Temp)){
        				if(x1Temp>0){
        					x10Temp=(float) xozLengthTemp;
        				}else{
        					x10Temp=(float) -xozLengthTemp;
        				}
        				z10Temp=(float) (x10Temp*Math.tan(Math.toRadians(hAngle)));
        			}else{
        				if(z1Temp>0){
        					z10Temp=(float) xozLengthTemp;
        				}else{
        					z10Temp=(float) -xozLengthTemp;
        				}
        				x10Temp=(float) (z10Temp/Math.tan(Math.toRadians(hAngle)));
        			}
        			
        			if(vAngle==50){//������������
        				s2=0.5f+x10Temp/length2;
            			t2=0.5f+z10Temp/length2;
        			}
        			else  if(vAngle==-45){//������������
        			    s2=0.5f+x10Temp/length2;
            			t2=(-0.5f+z10Temp/length2)*-1;
        			}
        			//��
        			alVertix2.add(x1Temp);alVertix2.add(y1Temp);alVertix2.add(z1Temp);
        			//������
        			alVertixCube2.add(x10Temp);alVertixCube2.add(y10Temp);alVertixCube2.add(z10Temp);        			
        			//����
        			alVertixTexCoor2.add(s2); alVertixTexCoor2.add(t2);        			
        		}
        		
        		if(vAngle>45)
        		{//���vAngle����45ʱ����Ӧ�����������
        			if(Math.abs(x1)>Math.abs(z1)){
        				if(x1>0){
        					x10=(float) xozLength;
        				}else{
        					x10=(float) -xozLength;
        				}
        				z10=(float) (x10*Math.tan(Math.toRadians(hAngle)));
        			}else{
        				if(z1>0){
        					z10=(float) xozLength;
        				}else{
        					z10=(float) -xozLength;
        				}
        				x10=(float) (z10/Math.tan(Math.toRadians(hAngle)));
        			}
        			y10=length;
        			s1=0.5f+x10/length2;
        			t1=0.5f+z10/length2;        			
        		}
        		else  if(vAngle<(-45))
        		{//���vAngleС��-45ʱ����Ӧ�����������
        			if(Math.abs(x1)>Math.abs(z1)){
        				if(x1>0){
        					x10=(float) xozLength;
        				}else{
        					x10=(float) -xozLength;
        				}
        				z10=(float) (x10*Math.tan(Math.toRadians(hAngle)));
        			}else{
        				if(z1>0){
        					z10=(float) xozLength;
        				}else{
        					z10=(float) -xozLength;
        				}
        				x10=(float) (z10/Math.tan(Math.toRadians(hAngle)));
        			}
        			y10=-length;
        			s1=0.5f+x10/length2;
        			t1=1-(0.5f+z10/length2);
        			
        		}
        		else{            			
        			if(Math.abs(x1)>Math.abs(z1))
            		{//x>z
            			if(x1>0){
            				x10=length;
            				z10=(float) (x10*Math.tan(Math.toRadians(hAngle)));           
            				s1=0.5f+z10/length2;
            			}else{
            				x10=-length;
            				z10=(float) (x10*Math.tan(Math.toRadians(hAngle)));           
            				s1=(-0.5f+z10/length2)*-1;
            			}    			
            			t1=1-(0.5f+(y10)/length2);
            		}else{
            			if(z1>0)
            			{
            				z10=length;
            				x10=(float)(z10/Math.tan(Math.toRadians(hAngle)));
            				s1=0.5f+x10/length2;
            			}else{
            				z10=-length;
            				x10=(float)(z10/Math.tan(Math.toRadians(hAngle)));
            				s1=-1*(-0.5f+x10/length2);
            			}
            			t1=1-(0.5f+(y10)/length2);
            		}
        		}
        		//��ǰ��====================end====================

    			//����ǰ�Ķ�������б���=============
        		alVertix.add(x1);alVertix.add(y1);alVertix.add(z1);
        		//���������Ӧ�Ķ�������б���=============
        		alVertix1.add(x10);alVertix1.add(y10);alVertix1.add(z10);
        		//��������������б���
        		alVertixTexCoor.add(s1); alVertixTexCoor.add(t1);        		
        	}
        	
        	if(vAngle==50||vAngle==-45)
        	{       		
        		for(int i=0;i<alVertix2.size()/3;i++)
        		{
        			alVertix.add(alVertix2.get(i*3));
        			alVertix.add(alVertix2.get(i*3+1));
        			alVertix.add(alVertix2.get(i*3+2));        			    		
        		}
        		for(int i=0;i<alVertixCube2.size()/3;i++)
        		{
        			alVertix1.add(alVertixCube2.get(i*3));
        			alVertix1.add(alVertixCube2.get(i*3+1));
        			alVertix1.add(alVertixCube2.get(i*3+2));
        		}        		
        		for(int i=0;i<alVertixTexCoor2.size()/3;i++)
        		{
        			alVertixTexCoor.add(alVertixTexCoor2.get(i*3));
        			alVertixTexCoor.add(alVertixTexCoor2.get(i*3+1));
        			alVertixTexCoor.add(alVertixTexCoor2.get(i*3+2));        			
        		}
        		
        		alVertix2.clear();
        		alVertixCube2.clear();
        		alVertixTexCoor2.clear();        
        	}	
        }    	
    	//������ƣ���ֹ���ֶ������==========start=============
    	int w = (int) (360 / angleSpan);
    	for(int i = 0; i <(w+2); i++){
    		for(int j = 0; j < w; j++){
    			int x = i * w + j;
    			alVertixIndice.add(x);
    			alVertixIndice.add(x + w);
    			alVertixIndice.add(x + 1);
    			alVertixIndice.add(x + 1);
    			alVertixIndice.add(x + w);
    			alVertixIndice.add(x + w + 1);
    		}
    	}
    	//������ƣ���ֹ���ֶ������==========end=============
    	
        vCount=alVertix.size()/3;//���������Ϊ����ֵ������1/3����Ϊһ��������3������
        
        iCount=alVertixIndice.size();
        vertices=new float[vCount*3];
        verticesCube=new float[vCount*3];
        texCoors=new float[alVertixTexCoor.size()];
        curBallForDraw=new float[vertices.length/2];
        curBallForCal=new float[vertices.length/2];
        indices=new int[iCount];
    	for(int i=0;i<alVertix.size();i++)
    	{//��������
    		vertices[i]=alVertix.get(i);
    	}
    	
    	for(int i=0;i<iCount;i++)
    	{
    		indices[i]=alVertixIndice.get(i);
    	}
    	
    	for(int i=0;i<alVertix1.size();i++)
    	{//��Ӧ������Ķ�������
    		verticesCube[i]=alVertix1.get(i);
    	}
    	for(int i=0;i<alVertixTexCoor.size();i++)
    	{//������������
    		texCoors[i]=alVertixTexCoor.get(i);
    	}
    	
    }
    
    //��ʼ���������ݵķ���
    public void initVertexData()
    {
    	//����id����
    	int[] buffIds=new int[3];
    	//����2������id
    	GLES30.glGenBuffers(3, buffIds, 0);
    	//�����������ݻ��� id
    	mVertexBufferId=buffIds[0];
    	//���������������ݻ��� id
    	mTexCoorBufferId=buffIds[1];
    	
    	mIndicesBufferId=buffIds[2];
     	//���������������ݵĳ�ʼ��================begin============================  
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        FloatBuffer mTexCoorBuffer = tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoors);//�򻺳����з��붥��������������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //�󶨵����������������ݻ���
    	GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,mTexCoorBufferId);
    	//�򶥵������������ݻ�����������
    	GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, texCoors.length*4, mTexCoorBuffer, GLES30.GL_STATIC_DRAW);
   	 	//���������������ݵĳ�ʼ��================end============================  
		
    	ByteBuffer ibb= ByteBuffer.allocateDirect(indices.length*4);
    	ibb.order(ByteOrder.nativeOrder());
    	mIndicesBuffer=ibb.asIntBuffer();
        mIndicesBuffer.put(indices);//�򻺳����з��붥��������������
        mIndicesBuffer.position(0);//���û�������ʼλ��
        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,mIndicesBufferId);
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indices.length*4, mIndicesBuffer, GLES30.GL_STATIC_DRAW);
        
        
    	//�����������ݵĳ�ʼ��================start============================  
    	//�󶨵������������ݻ��� 
    	GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,mVertexBufferId);
    	//�򶥵��������ݻ�����������,����vertices.length*4���洢��λ��ͨ�����ֽڣ���
    	//�ڴ棬���ڴ洢�������ݻ���������ǰ�����뵱ǰ�󶨶�������������ݶ���ɾ����
    	GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertices.length*4, null, GLES30.GL_STATIC_DRAW);  
    	vbb1=(ByteBuffer)GLES30.glMapBufferRange(
    			GLES30.GL_ARRAY_BUFFER, //��ʾ��������
    			0, //ƫ����
    			vertices.length*4, //����
    			GLES30.GL_MAP_WRITE_BIT|GLES30.GL_MAP_INVALIDATE_BUFFER_BIT//���ʱ�־
    	);
    	if(vbb1==null)
    	{
    		return;
    	}
    	vbb1.order(ByteOrder.nativeOrder());//�����ֽ�˳��
    	mVertexMappedBuffer=vbb1.asFloatBuffer();//ת��ΪFloat�ͻ���
    	
    	mVertexMappedBuffer.put(vertices);//��ӳ��Ļ������з��붥����������   verticesCube
    	mVertexMappedBuffer.position(0);//���û�������ʼλ��
    	if(GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER)==false)
    	{
    		return;
    	}
    	//�����������ݵĳ�ʼ��================end============================  
    	//�󶨵�ϵͳĬ�ϻ���
    	GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0);
    }
  
    //��ʼ����ɫ��
    public void initShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж��㾭γ����������
        maTexCoorHandle=GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }
    //���¶�������
	public void updateMapping(float[] currVertex)
	{
		//�󶨵������������ݻ��� 
    	GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,mVertexBufferId);
    	vbb1=(ByteBuffer)GLES30.glMapBufferRange(
    			GLES30.GL_ARRAY_BUFFER, 
    			0, //ƫ����
    			currVertex.length*4, //����
    			GLES30.GL_MAP_WRITE_BIT|GLES30.GL_MAP_INVALIDATE_BUFFER_BIT//���ʱ�־
    	);
    	if(vbb1==null)
    	{
    		return;
    	}
    	vbb1.order(ByteOrder.nativeOrder());//�����ֽ�˳��
    	mVertexMappedBuffer=vbb1.asFloatBuffer();//ת��ΪFloat�ͻ���
    	mVertexMappedBuffer.put(currVertex)  ;//��ӳ��Ļ������з��붥����������
    	mVertexMappedBuffer.position(0);//���û�������ʼλ��
    	if(GLES30.glUnmapBuffer(GLES30.GL_ARRAY_BUFFER)==false)
    	{
    		return;
    	}    	
	}
    public void drawSelf(int texId)
    {        
    	MatrixState.rotate(xAngle, 1, 0, 0);//��X��ת��
    	MatrixState.rotate(yAngle, 0, 1, 0);//��Y��ת��
    	MatrixState.rotate(zAngle, 0, 0, 1);//��Z��ת��
    	//ָ��ʹ��ĳ����ɫ������
    	GLES30.glUseProgram(mProgram);
    	//�����ձ任��������Ⱦ����
    	GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);      	
  
    	GLES30.glEnableVertexAttribArray(maTexCoorHandle);  	//����������������
    	//�󶨵����������������ݻ���
    	GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,mTexCoorBufferId);
    	//��������������������Ⱦ����
    	GLES30.glVertexAttribPointer  
    	(
    			maTexCoorHandle, 
    			2, 
    			GLES30.GL_FLOAT,
    			false,
    			2*4,
    			0
    			);  
    	
    	//���ö���λ����������
    	GLES30.glEnableVertexAttribArray(maPositionHandle);
    	//�󶨵������������ݻ��� 
    	GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,mVertexBufferId);
    	//������λ������������Ⱦ����
    	GLES30.glVertexAttribPointer
    	(
    			maPositionHandle,   
    			3, 
    			GLES30.GL_FLOAT,  
    			false,
    			3*4,
    			0
    			); 
    	

    	GLES30.glActiveTexture(GLES30.GL_TEXTURE0);								//��������
    	GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);             	//������
    	
    	synchronized(lock)
    	{
    		//���¶�������
        	updateMapping(curBallForDraw);
    	}
    	GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,mIndicesBufferId);
    	//�������η�ʽִ�л���
    	GLES30.glDrawElements(GLES30.GL_TRIANGLES, iCount, GLES30.GL_UNSIGNED_INT, 0);
    	GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER,0);
    	//�󶨵�ϵͳĬ�ϻ���
    	GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER,0);
    }
    
    //���㶥���������ݷ���
    public void calVertices(int count,boolean flag)
    {
    	for(int i=0;i<vertices.length/2;i++)
		{
			curBallForCal[i]=insertValue(vertices[i],verticesCube[i],span,count,flag);
		}
    	synchronized(lock)
    	{
        	curBallForDraw=Arrays.copyOf(curBallForCal, curBallForCal.length);
    	}
    }
    
    //�����ֵ����
    public float insertValue(float start,float end,float span,int count,boolean isBallToCubeY)
	{
		float result=0;
		if(isBallToCubeY)
		{
			result=start+count*(end-start)/span;
		}else{
			result=end-count*(end-start)/span;
		}
		return result;
	}
}
