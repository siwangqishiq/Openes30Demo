package com.bn.Sample2_3;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import android.opengl.GLES30;
/*
 * ���ڻ�������
 */
public class TreeTrunk
{
	//----------��������ɵĽڵ����Ť��---------------------
	int fBendRHandle;//���������뾶�ľ��Id
	int fwind_direction_Handle;//����Ƕȵľ��
	//--------------------------------------------
	//�Զ�����Ⱦ������ɫ������id
	int mProgram;
	//�ܱ仯�������õ�id
	int muMVPMatrixHandle;
	//����λ����������
	int maPositionHandle;
	//��������������������
	int maTexCoorHandle;
	
	//�������ݻ���������������ݻ��嶥�㷨�������ݻ���
	FloatBuffer mVertexBuffer;
	FloatBuffer mTexCoorBuffer;
	//��������
	int vCount=0;
	//�����зֵĽǶ�
	float longitude_span=12;
	//������ ���ĵ�һ���ڵ�׶˰뾶����һ���ڵ�Ķ��˰뾶��ÿ���ڵ�ĸ߶ȣ��ڵ������
	public TreeTrunk(int mProgram,float bottom_Radius,float joint_Height,int jointNum,int availableNum)
	{
		this.mProgram=mProgram;
		initVertexData(bottom_Radius,joint_Height,jointNum,availableNum);
		initShader();
	}
	//��ʼ���������ݵķ���
	public void initVertexData(float bottom_radius,float joint_Height,int jointNum,int availableNum)//R����׶˰뾶��r�����˰뾶
	{
		List<Float> vertex_List=new ArrayList<Float>();//���������б�
		List<float[]> texture_List=new ArrayList<float[]>();//�������������б�
		for(int num=0;num<availableNum;num++)//ѭ�������ÿ�������еĸ�������
		{
			//�˽����ɵ׶˰뾶
			float temp_bottom_radius=bottom_radius*(jointNum-num)/(float)jointNum;
			//�˽����ɶ��˰뾶
			float temp_top_radius=bottom_radius*(jointNum-(num+1))/(float)jointNum;
			//�˽����ɵ׶˵�y����
			float temp_bottom_height=num*joint_Height;
			//�˽����ɶ��˵�y����
			float temp_top_height=(num+1)*joint_Height;
			
			//ѭ��һ�ܣ�������ɴ˽����ɸ����ı��εĶ������꣬�����Ƴ�������
			for(float hAngle=0;hAngle<360;hAngle=hAngle+longitude_span)
			{
				//��ǰ�ı������ϵ�X��Y��Z����
				float x0=(float) (temp_top_radius*Math.cos(Math.toRadians(hAngle)));
				float y0=temp_top_height;
				float z0=(float) (temp_top_radius*Math.sin(Math.toRadians(hAngle)));
				//��ǰ�ı������µ�X��Y��Z����
				float x1=(float) (temp_bottom_radius*Math.cos(Math.toRadians(hAngle)));
				float y1=temp_bottom_height;
				float z1=(float) (temp_bottom_radius*Math.sin(Math.toRadians(hAngle)));
				//��ǰ�ı������ϵ�X��Y��Z����
				float x2=(float) (temp_top_radius*Math.cos(Math.toRadians(hAngle+longitude_span)));
				float y2=temp_top_height;
				float z2=(float) (temp_top_radius*Math.sin(Math.toRadians(hAngle+longitude_span)));
				//��ǰ�ı������µ�X��Y��Z����
				float x3=(float) (temp_bottom_radius*Math.cos(Math.toRadians(hAngle+longitude_span)));
				float y3=temp_bottom_height;
				float z3=(float) (temp_bottom_radius*Math.sin(Math.toRadians(hAngle+longitude_span)));
				
				//���������갴�վ��Ƴ����������ε�˳�����η��붥�������б�
				vertex_List.add(x0);vertex_List.add(y0);vertex_List.add(z0);
				vertex_List.add(x1);vertex_List.add(y1);vertex_List.add(z1);
				vertex_List.add(x2);vertex_List.add(y2);vertex_List.add(z2);
				
				vertex_List.add(x2);vertex_List.add(y2);vertex_List.add(z2);
				vertex_List.add(x1);vertex_List.add(y1);vertex_List.add(z1);
				vertex_List.add(x3);vertex_List.add(y3);vertex_List.add(z3);
			}
			//������������
			//�����������껺��
			float[] texcoor=generateTexCoor//��ȡ�з���ͼ����������                
	        (
	  			 (int)(360/longitude_span), //����ͼ�зֵ�����
	  			  1                    //(int)(180/ANGLE_SPAN)  //����ͼ�зֵ�����
	        );
			texture_List.add(texcoor);
		}
		//�������㻺��
		float[] vertex=new float[vertex_List.size()];
		for(int i=0;i<vertex_List.size();i++)
		{
			vertex[i]=vertex_List.get(i);
		}
		vCount=vertex_List.size()/3;
		
		ByteBuffer vbb=ByteBuffer.allocateDirect(vertex.length*4);
		vbb.order(ByteOrder.nativeOrder());
		mVertexBuffer=vbb.asFloatBuffer();
		mVertexBuffer.put(vertex);
		mVertexBuffer.position(0);
		//�����������껺��
		ArrayList<Float> al_temp=new ArrayList<Float>(); 
		for(float []temp:texture_List)
		{
			for(float tem:temp)
			{
				al_temp.add(tem);
			}
		}
		float[]texcoor=new float[al_temp.size()];	
		int num=0;
		for(float temp:al_temp)
		{
			texcoor[num]=temp;
			num++;
		}
		ByteBuffer tbb=ByteBuffer.allocateDirect(texcoor.length*4);
		tbb.order(ByteOrder.nativeOrder());
		mTexCoorBuffer=tbb.asFloatBuffer();
		mTexCoorBuffer.put(texcoor);
		mTexCoorBuffer.position(0);
	}
	//��ʼ����ɫ���ķ���
	public void initShader() 
	{
		//------------------------------�ڶ�������-----------------------
        //��ȡ�����ж���λ����������  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //��ȡ�����ж�������������������  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor");
        //��ȡ�������ܱ任��������
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //��ȡ���������������뾶������
        fBendRHandle = GLES30.glGetUniformLocation(mProgram,"bend_R");
        //��ȡ�����з���ĽǶ�����
        fwind_direction_Handle = GLES30.glGetUniformLocation(mProgram,"direction_degree");
	}
	//�Զ���Ļ��Ʒ���drawSelf
	public void drawSelf(int texId,float bend_R,float wind_direction)
	{
		//ָ��ʹ��ĳ����ɫ������
   	 	GLES30.glUseProgram(mProgram); 
        //�����ձ任��������Ⱦ����
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
        //�������뾶������Ⱦ����
        GLES30.glUniform1f(fBendRHandle, bend_R);
        //����������Ⱦ����
        GLES30.glUniform1f(fwind_direction_Handle, wind_direction);
		
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
		//�������������ݴ�����Ⱦ����
		GLES30.glVertexAttribPointer
		(
			maTexCoorHandle, 
			2, 
			GLES30.GL_FLOAT, 
			false, 
			2*4, 
			mTexCoorBuffer
		);
		//���ö���λ�����ݡ�����������������
        GLES30.glEnableVertexAttribArray(maPositionHandle);  
        GLES30.glEnableVertexAttribArray(maTexCoorHandle);  
        //������
        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
        //�����������
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
	}
	//�Զ��з����������������ķ���
    public float[] generateTexCoor(int bw,int bh)
    {
    	float[] result=new float[bw*bh*6*2]; 
    	float sizew=1.0f/bw;//����
    	float sizeh=1.0f/bh;//����
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
