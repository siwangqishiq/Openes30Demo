package com.bn.Sample6_7;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import com.bn.util.Vector3f;

public class CalThread extends Thread
{
    boolean flag=true;
    ParticleControl pc;
    Vector3f ballP;
    FloatBuffer mVertexBuffer;
    float[] vdata;
    
    public CalThread(ParticleControl pc)
    {
    	this.pc=pc;
    }
    
    public void run()
    {
    	while(flag)
    	{
    		synchronized(Constant.lockB)
    		{
    		pc.stepSimulation(0.010f);//�����������
    		}
    		vdata=pc.getVerties();//���һ֡����
    		ballP = pc.getBall();
            ByteBuffer vbb = ByteBuffer.allocateDirect(vdata.length*4);
            vbb.order(ByteOrder.nativeOrder());//�����ֽ�˳��
            mVertexBuffer = vbb.asFloatBuffer();//ת��Ϊint�ͻ���
            mVertexBuffer.put(vdata);//�򻺳����з��붥����������
            mVertexBuffer.position(0);//���û�������ʼλ��
    		synchronized(Constant.lockA)
    		{
    			Constant.mVertexBufferForFlag=mVertexBuffer;//�����㻺������ָ���µ�����
    			Constant.ballP = ballP;
    		}
    	}
    }
}

