package com.bn.Sample4_8;//������

import static com.bn.Sample4_8.Constant.DIS_MAX;
import static com.bn.Sample4_8.Constant.SCALE_MAX;
import static com.bn.Sample4_8.Constant.SCALE_MIN;

import java.util.ArrayList;//�����б�

public class Flare{
	
	public int[] textures;//��������
	
	public ArrayList<SingleFlare> sFl=new ArrayList<SingleFlare>();//��Ź���Ԫ�ص��б�
	public Flare(int[] textures)
	{
		this.textures=textures;//��ʼ����������
		initFlare();//��ʼ������Ԫ��
	}
	public void initFlare()
	{//��ʼ������Ԫ�ض���ķ���
		//���´��������б��������13����ͷ����Ԫ�ض���
		sFl.add(new SingleFlare(textures[1],5.4f,-1.0f,new float[]{1.0f,1.0f,1.0f,1.0f}));
		sFl.add(new SingleFlare(textures[1],0.4f,-0.8f,new float[]{0.7f,0.5f,0.0f,0.02f}));
		sFl.add(new SingleFlare(textures[1],0.04f,-0.7f,new float[]{1.0f, 0.0f, 0.0f, 0.07f}));
		sFl.add(new SingleFlare(textures[0],0.4f,-0.5f,new float[]{1.0f, 1.0f, 0.0f, 0.05f}));
		sFl.add(new SingleFlare(textures[2],1.22f,-0.4f,new float[]{1.0f, 1.0f, 0.0f, 0.05f}));
		sFl.add(new SingleFlare(textures[0],0.4f,-0.3f,new float[]{1.0f, 0.5f, 0.0f, 1.0f}));
		sFl.add(new SingleFlare(textures[1],0.4f,-0.1f,new float[]{1.0f, 1.0f, 0.5f, 0.05f}));
		sFl.add(new SingleFlare(textures[0],0.4f,0.2f,new float[]{1.0f, 0.0f, 0.0f, 1.0f}));
		sFl.add(new SingleFlare(textures[1],0.8f,0.3f,new float[]{1.0f, 1.0f, 0.6f, 1.0f}));
		sFl.add(new SingleFlare(textures[0],0.6f,0.4f,new float[]{1.0f, 0.7f, 0.0f, 0.03f}));
		sFl.add(new SingleFlare(textures[2],0.6f,0.7f,new float[]{1.0f, 0.5f, 0.0f, 0.02f}));
		sFl.add(new SingleFlare(textures[2],1.28f,1.0f,new float[]{1.0f, 0.7f, 0.0f, 0.02f}));
		sFl.add(new SingleFlare(textures[2],3.20f,1.3f,new float[]{1.0f, 0.0f, 0.0f, 0.05f}));
	}
	
	public void update(float lx,float ly)
	{//���¹���λ�õķ���
		
		float currDis=(float)Math.sqrt(lx*lx+ly*ly);//̫����ԭ��ľ���
		float currScale=SCALE_MIN+(SCALE_MAX-SCALE_MIN)*(1-currDis/DIS_MAX);//�������ֵ-���ڻ��Ƴߴ�ļ���
		
		for(SingleFlare ss:sFl)
		{//ѭ���������й���Ԫ�ض���
			ss.px=-ss.distance*lx;//����ù���Ԫ�صĻ���λ��x����
			ss.py=-ss.distance*ly;//����ù���Ԫ�صĻ���λ��y����
			ss.bSize=ss.size*currScale;//����任��ĳߴ�
		}
	}
}
