package com.bn.Sample2_4;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import android.opengl.GLES30;

//���غ�����塪����Я��������Ϣ����ɫ���
public class GledeForDraw
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id  
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle1; //����λ����������  
    int maPositionHandle2; //����λ����������  
    int maPositionHandle3; //����λ����������  
    int maTexCoorHandle; //��������������������  
    int muBfbHandle;//�仯�ٷֱ�����
    String mVertexShader;//������ɫ������ű�    	 
    String mFragmentShader;//ƬԪ��ɫ������ű�   
	
	FloatBuffer   mVertexBuffer1;//�����������ݻ���  
	FloatBuffer   mVertexBuffer2;//�����������ݻ���  
	FloatBuffer   mVertexBuffer3;//�����������ݻ���  
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���
	
	float[][] glede_one;
	float[] glede_two;
	float[] glede_three;
	
    int vCount=0;  
    
    int operator=1;
    float span=0.15f;
    float bfbCurr=0f;
    
    public GledeForDraw(MySurfaceView mv)
    {    	
    	//��ʼ����������
    	initVertexData(mv);
    	//��ʼ����ɫ��        
    	intShader(mv);
    	new Thread()
    	{
    		@Override
    		public void run()
    		{
    			while(true)
    			{
    				bfbCurr=bfbCurr+operator*span;
    				if(bfbCurr>2.0f)
    				{
    					bfbCurr=2.0f;
    					operator=-operator;
    				}
    				else if(bfbCurr<0)
    				{
    					bfbCurr=0;
    					operator=-operator;
    				}
    				try
    				{
    					Thread.sleep(50);
    				}
    				catch(Exception e)
    				{
    					e.printStackTrace();
    				}
    			}
    		}
    	}.start();
    }
    
    //��ʼ���������ݵķ���
    public void initVertexData(MySurfaceView mv)
    {
    	//������ӥģ��
    	glede_one=LoadUtil.loadFromFileVertexOnly("laoying01.obj",mv);
    	glede_two=LoadUtil.loadFromFileVertexOnly("laoying02.obj",mv)[0]; 
    	glede_three=LoadUtil.loadFromFileVertexOnly("laoying03.obj",mv)[0]; 
    	
    	//========================1=========================================
    	//������һ�������������ݻ���
    	vCount=glede_one.length/3;
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        ByteBuffer vbb = ByteBuffer.allocateDirect(glede_one[0].length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer1 = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer1.put(glede_one[0]);//�򻺳����з��붥����������
        mVertexBuffer1.position(0);//���û�������ʼλ��
        //====================2==========================
        //�����ڶ��������������ݻ���
        vCount=glede_two.length/3;
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        vbb = ByteBuffer.allocateDirect(glede_two.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer2 = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer2.put(glede_two);//�򻺳����з��붥����������
        mVertexBuffer2.position(0);//���û�������ʼλ��
        //---------------------------------3-----------------------------
        //���������������������ݻ���
        vCount=glede_three.length/3;
        //vertices.length*4����Ϊһ�������ĸ��ֽ�
        vbb = ByteBuffer.allocateDirect(glede_three.length*4);
        vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mVertexBuffer3 = vbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mVertexBuffer3.put(glede_three);//�򻺳����з��붥����������
        mVertexBuffer3.position(0);//���û�������ʼλ��
        
        //------------------����-----------------------------------------
        //���������������ݻ���
        ByteBuffer tbb = ByteBuffer.allocateDirect(glede_one[1].length*4);
        tbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = tbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(glede_one[1]);//�򻺳����з��붥��������������
        mTexCoorBuffer.position(0);//���û�������ʼλ��
        //�ر���ʾ�����ڲ�ͬƽ̨�ֽ�˳��ͬ���ݵ�Ԫ�����ֽڵ�һ��Ҫ����ByteBuffer
        //ת�����ؼ���Ҫͨ��ByteOrder����nativeOrder()�������п��ܻ������
        //���������������ݵĳ�ʼ��================end============================
    }
    //��ʼ����ɫ��
    public void intShader(MySurfaceView mv)
    {
    	//���ض�����ɫ���Ľű�����
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //����ƬԪ��ɫ���Ľű�����
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //���ڶ�����ɫ����ƬԪ��ɫ����������
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //��ȡ�����ж���λ����������  
        maPositionHandle1 = GLES30.glGetAttribLocation(mProgram, "aPosition");
        maPositionHandle2 = GLES30.glGetAttribLocation(mProgram, "bPosition");
        maPositionHandle3 = GLES30.glGetAttribLocation(mProgram, "cPosition");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡ�����ж�������������������  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor"); 
        //�仯�ٷֱ�����
        muBfbHandle= GLES30.glGetUniformLocation(mProgram, "uBfb");
    }
    
    public void drawSelf(int texId)
    { 
    	 //ָ��ʹ��ĳ����ɫ������
    	 GLES30.glUseProgram(mProgram);
         //�����ձ任��������Ⱦ����
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         //���仯�ٷֱȴ�����Ⱦ����
         GLES30.glUniform1f(muBfbHandle, bfbCurr);
         //������λ�����ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer  
         (
         		maPositionHandle1,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer1
         );    
         //������λ�����ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer  
         (
         		maPositionHandle2,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer2
         ); 
         //������λ�����ݴ�����Ⱦ����
         GLES30.glVertexAttribPointer  
         (
         		maPositionHandle3,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer3
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
         //���ö���λ�á�����������������
         GLES30.glEnableVertexAttribArray(maPositionHandle1);  
         GLES30.glEnableVertexAttribArray(maPositionHandle2);
         GLES30.glEnableVertexAttribArray(maPositionHandle3);
         GLES30.glEnableVertexAttribArray(maTexCoorHandle); 
         //������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         //���Ƽ��ص�����
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
