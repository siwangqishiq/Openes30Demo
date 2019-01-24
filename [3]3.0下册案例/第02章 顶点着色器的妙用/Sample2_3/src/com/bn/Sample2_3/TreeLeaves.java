package com.bn.Sample2_3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES30;
/*
 * ���ڻ�����Ҷ����
 */
public class TreeLeaves 
{	
	int mProgram;//�Զ�����Ⱦ������ɫ������id   
    int muMVPMatrixHandle;//�ܱ任��������
    int maPositionHandle; //����λ����������  
    int maTexCoorHandle; //��������������������  
    String mVertexShader;//������ɫ��    	 
    String mFragmentShader;//ƬԪ��ɫ��
    
	FloatBuffer   mVertexBuffer;//�����������ݻ���
	FloatBuffer   mTexCoorBuffer;//���������������ݻ���
    int vCount=0;   
    float centerX;//��Ҷ���ĵ�X����
    float centerZ;//��Ҷ���ĵ�Z����
    int index;//��ǰ��������
    
    public TreeLeaves(int mProgram,float width,float height,float absolute_height,int index)
    {    	
    	this.mProgram=mProgram;
    	//��ʼ����������
    	initVertexData(width,height,absolute_height,index);
    	//��ʼ����ɫ��        
    	intShader();
    	this.index=index;
    }
    //��ʼ���������ݵķ���
    public void initVertexData(float width,float height,float absolute_height,int index)
    {
        vCount=6;
        float vertices[]=null;//������������
        float texCoor[]=null;//������������
        switch(index)//�������������ɶ�Ӧ�Ƕ���Ҷ������εĶ�������
        {
        case 0://��һ���������Ҷ������εı���x���غϣ���Ӧ��ת�Ƕ�Ϊ0��
            vertices=new float[]
            {
        		0,height+absolute_height,0,
        		0,absolute_height,0,
        		width,height+absolute_height,0,
            	
        		width,height+absolute_height,0,
        		0,absolute_height,0,
        		width,absolute_height,0,
            };
            texCoor=new float[]//��������
            {
            	1,0, 1,1, 0,0,
            	0,0, 1,1, 0,1
            };
            //ȷ�����ĵ�����
            centerX=width/2;
            centerZ=0;
        	break;
        case 1://�ڶ����������x��н�60�ȵ���Ҷ�������
           vertices=new float[]
           {
	       		0,height+absolute_height,0,
	       		0,absolute_height,0,
	       		width/2,height+absolute_height,(float) (-width*Math.sin(Math.PI/3)),
	           	
	       		width/2,height+absolute_height,(float) (-width*Math.sin(Math.PI/3)),
	       		0,absolute_height,0,
	       		width/2,absolute_height,(float) (-width*Math.sin(Math.PI/3))
           };
           texCoor=new float[]
           {
	           	1,0, 1,1, 0,0,
	           	0,0, 1,1, 0,1
           };
           //ȷ�����ĵ�����
           centerX=width/4;
           centerZ=(float) (-width*Math.sin(Math.PI/3))/2;
        	break;
        case 2://��x��н�120�ȵ���Ҷ�������
        	vertices=new float[]
            {
        		-width/2,height+absolute_height,(float) (-width*Math.sin(Math.PI/3)),
        		-width/2,absolute_height,(float) (-width*Math.sin(Math.PI/3)),
        		0,height+absolute_height,0,
            	
        		0,height+absolute_height,0,
        		-width/2,absolute_height,(float) (-width*Math.sin(Math.PI/3)),
        		0,absolute_height,0,
            };
            texCoor=new float[]
            {
        		0,0, 0,1, 1,0,
            	1,0, 0,1, 1,1
            };
            //ȷ�����ĵ�����
            centerX=-width/4;
            centerZ=(float) (-width*Math.sin(Math.PI/3))/2;
        	break;
        case 3://��x��н�180�ȵ���Ҷ�������
           vertices=new float[]
           {
	       		-width,height+absolute_height,0,
	       		-width,absolute_height,0,
	       		0,height+absolute_height,0,
	           	
	       		0,height+absolute_height,0,
	       		-width,absolute_height,0,
	       		0,absolute_height,0,
           };
           texCoor=new float[]
           {
	       		0,0, 0,1, 1,0,
	           	1,0, 0,1, 1,1
           };
           //ȷ�����ĵ�����
           centerX=-width/2;
           centerZ=0;
        	break;
        case 4://��x��н�240�ȵ���Ҷ�������
           vertices=new float[]
           {
	       		-width/2,height+absolute_height,(float) (width*Math.sin(Math.PI/3)),
	       		-width/2,absolute_height,(float) (width*Math.sin(Math.PI/3)),
	       		0,height+absolute_height,0,
	           	
	       		0,height+absolute_height,0,
	       		-width/2,absolute_height,(float) (width*Math.sin(Math.PI/3)),
	       		0,absolute_height,0,
           };
           texCoor=new float[]
           {
	       		0,0, 0,1, 1,0,
	           	1,0, 0,1, 1,1
           };
           //ȷ�����ĵ�����
           centerX=-width/4;
           centerZ=(float) (width*Math.sin(Math.PI/3))/2;
           break;
        case 5://��x��н�300�ȵ���Ҷ�������
           vertices=new float[]
	       {
		   		0,height+absolute_height,0,
		   		0,absolute_height,0,
		   		width/2,height+absolute_height,(float) (width*Math.sin(Math.PI/3)),
		       	
		   		width/2,height+absolute_height,(float) (width*Math.sin(Math.PI/3)),
		   		0,absolute_height,0,
		   		width/2,absolute_height,(float) (width*Math.sin(Math.PI/3))
	       };
	       texCoor=new float[]
	       {
		       	1,0, 1,1, 0,0,
		       	0,0, 1,1, 0,1
	       };
	       //ȷ�����ĵ�����
           centerX=width/4;
           centerZ=(float) (width*Math.sin(Math.PI/3))/2;
        	break;
        }
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
        //�������������������ݻ���
        
        ByteBuffer cbb = ByteBuffer.allocateDirect(texCoor.length*4);
        cbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
        mTexCoorBuffer = cbb.asFloatBuffer();//ת��ΪFloat�ͻ���
        mTexCoorBuffer.put(texCoor);//�򻺳����з��붥����ɫ����
        mTexCoorBuffer.position(0);//���û�������ʼλ��
    }
    //��ʼ����ɫ��
    public void intShader()
    {
        //��ȡ�����ж���λ����������  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
    }
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
         //���ö���λ�á�����������������
         GLES30.glEnableVertexAttribArray(maPositionHandle);  
         GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
         
         //������
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         
         //�����������
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
