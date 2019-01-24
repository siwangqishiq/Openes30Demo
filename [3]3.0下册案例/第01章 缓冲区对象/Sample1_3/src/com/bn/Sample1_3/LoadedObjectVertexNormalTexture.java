package com.bn.Sample1_3;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;

//UBO
public class LoadedObjectVertexNormalTexture
{	
	static final int BYTES_PER_FLOAT=4;//ÿ�����������ֽ���
	
	int mProgram;//�Զ�����Ⱦ������ɫ������id  
    int muMVPMatrixHandle;//�ܱ任��������
    int muMMatrixHandle;//�����任����
    int maPositionHandle; //����λ����������  
    int maNormalHandle; //���㷨������������  
    int maTexCoorHandle; //��������������������  
      
    int uboHandle;//һ�¿黺�����id    
    int blockIndex;//һ�¿������
    
    String mVertexShader;//������ɫ������ű�    	 
    String mFragmentShader;//ƬԪ��ɫ������ű�    
    
    FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mNormalBuffer;//���㷨�������ݻ���
	FloatBuffer  mTexCoorBuffer;//�����������ݻ���
    
    int vCount=0;  
    
    public LoadedObjectVertexNormalTexture(MySurfaceView mv,float[] vertices,float[] normals,float texCoors[])
    {  
    	//��ʼ���������ݵķ���
    	initVertexData(vertices,normals,texCoors); 
    	//��ʼ����ɫ���ķ���        
    	initShader(mv);    	
    }
    
    //��ʼ���������ݵķ���
    public void initVertexData(float[] vertices,float[] normals,float texCoors[])
    {
    	vCount=vertices.length/3;   
        //���������������ݻ���
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer.put(vertices);//�򻺳����з��붥����������
        mVertexBuffer.position(0);//���û�������ʼλ��
     
        ByteBuffer cbb = ByteBuffer.allocateDirect(normals.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mNormalBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mNormalBuffer.put(normals);//�򻺳����з��붥�㷨��������
        mNormalBuffer.position(0);//���û�������ʼλ��

        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoors);//�򻺳����з��붥��������������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
    }
    
    //��ʼ��һ�»���
    public void initUBO()
    {
        //��ȡһ�¿������
        blockIndex=GLES30.glGetUniformBlockIndex(mProgram, "MyDataBlock"); 
        //��ȡһ�¿�ĳߴ�
        int[] blockSizes=new int[1];        
        GLES30.glGetActiveUniformBlockiv(mProgram, blockIndex, GLES30.GL_UNIFORM_BLOCK_DATA_SIZE, blockSizes, 0);
        int blockSize=blockSizes[0];           
        //����һ�¿��ڵĳ�Ա��������
        String[] names={"MyDataBlock.uLightLocation","MyDataBlock.uCamera"};
        //������Ӧ�ĳ�Ա��������
        int[] uIndices=new int[names.length];
        //��ȡһ�¿��ڵĳ�Ա����
        GLES30.glGetUniformIndices(mProgram, names, uIndices, 0);
        //��ȡһ�¿��ڵĳ�Աƫ����        
        int[] offset=new int[names.length];
        GLES30.glGetActiveUniformsiv(mProgram, 2,uIndices,0, GLES30.GL_UNIFORM_OFFSET, offset,0);
        //���ڴ洢һ�»�������ŵ�����
        int[] uboHandles=new int[1];  
        //����һ�»������
        GLES30.glGenBuffers(1, uboHandles, 0);
        //��ȡһ�»��������
        uboHandle=uboHandles[0];        
        //��һ�»������󶨵�һ�¿�  
        GLES30.glBindBufferBase(GLES30.GL_UNIFORM_BUFFER,blockIndex,uboHandle);
        //���ٴ��һ�»����������ݵ��ڴ滺��
        ByteBuffer ubb = ByteBuffer.allocateDirect(blockSize);
        ubb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        FloatBuffer  uBlockBuffer = ubb.asFloatBuffer();     //ת��ΪFloat�ͻ���
        //����Դλ�����������ڴ滺��
   	    float[] data=MatrixState.lightLocation;
   	    uBlockBuffer.position(offset[0]/BYTES_PER_FLOAT);
        uBlockBuffer.put(data);
        //�������λ�����������ڴ滺��
        float[] data1=MatrixState.cameraLocation;
        uBlockBuffer.position(offset[1]/BYTES_PER_FLOAT);
        uBlockBuffer.put(data1);
        //���û�����ʼƫ����
        uBlockBuffer.position(0);   
        //����Դλ�á������λ���������ڴ滺���е���������һ�»���               
        GLES30.glBufferData(GLES30.GL_UNIFORM_BUFFER,blockSize,uBlockBuffer,GLES30.GL_DYNAMIC_DRAW);  
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
        //��ȡ�����ж�����ɫ��������  
        maNormalHandle= GLES30.glGetAttribLocation(mProgram, "aNormal");
        //��ȡ�����ж�������������������  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor"); 
        //��ȡ�ܱ任��������
        muMVPMatrixHandle=GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
        //��ȡ�����任��������
        muMMatrixHandle=GLES30.glGetUniformLocation(mProgram, "uMMatrix");
        //��ʼ��һ�»���
        initUBO();
    }
    
    public void drawSelf(int texId)
    {        
    	 //ָ��ʹ��ĳ����ɫ������
    	 GLES30.glUseProgram(mProgram);

         //���ܱ任��������Ⱦ���� 
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0);  
         //��λ�á���ת�任��������Ⱦ����
         GLES30.glUniformMatrix4fv(muMMatrixHandle, 1, false,MatrixState.getMMatrix(),0);
         //Ϊһ�¿��һ�»���
         GLES30.glBindBufferBase(GLES30.GL_UNIFORM_BUFFER,blockIndex,uboHandle);         
         GLES30.glEnableVertexAttribArray(maPositionHandle);   //���ö���λ����������
         GLES30.glEnableVertexAttribArray(maNormalHandle);      //���ö��㷨������������
         GLES30.glEnableVertexAttribArray(maTexCoorHandle);     //���ö�����������������
         //������λ������������Ⱦ����
         GLES30.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );  
         //�����㷨����������Ⱦ����
         GLES30.glVertexAttribPointer  
         (
        		maNormalHandle, 
         		3,   
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mNormalBuffer
         );   
         //��������������������Ⱦ����
         GLES30.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES30.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );        
    
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);//��������
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);     //������
         //���Ƽ��ص�����
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
