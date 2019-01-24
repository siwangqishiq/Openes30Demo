package com.bn.Sample5_9_V2;


public class Constant  
{ 
	public static float r=40;	//�������Ŀ���ľ��룬���������ת�İ뾶
	public static float ANGLE_MIN=-55;//�������ת�ĽǶȷ�Χ����Сֵ
	public static float ANGLE_MAX=55;//�������ת�ĽǶȷ�Χ�����ֵ
	
	public static final float UNIT_SIZE=15f;	
	public static int SCREEN_WIDTH;//��Ļ���
	public static  int SCREEN_HEIGHT;//��Ļ�߶�   

	//����ͶӰ����Ĳ���
	public static  float left=0;
	public static float right=0;
	public static float bottom=0;
	public static float top=0;
	public static float near=0;
	public static float far=0;
	public static float ratio =0;
	
	//ע��start======�������뾵���������Ŀ����up������һ�µ�
	//�����Ŀ��������
	public static float targetX=0;
	public static float targetY=18;
	public static float targetZ=-45;
	
	//�������up����
	public static float upX=0;
	public static float upY=1;
	public static float upZ=0;	
	//ע��end======�������뾵���������Ŀ����up������һ�µ�
	
	//��������Ĺ۲�������
	public static float mainCameraX=0;
	public static float mainCameraY=0;
	public static float mainCameraZ=0;
	
	//����������Ĺ۲�������
	public static float mirrorCameraX=0;
	public static float mirrorCameraY=0;
	public static float mirrorCameraZ=0;
	

	
	public static void calculateMainAndMirrorCamera(float angle)
	{		
		//������������۲��ߵ�����
		mainCameraX = (float) (r*Math.sin(Math.toRadians(angle)));
		mainCameraY=targetY;
		mainCameraZ = (float) (r*Math.cos(Math.toRadians(angle)))+targetZ;
		
		//���㾵��������۲��ߵ�����
		mirrorCameraX=mainCameraX;//�����������x�������������z����һ��
		mirrorCameraY=mainCameraY;
		mirrorCameraZ=2*targetZ-mainCameraZ;//���ݶԳƹ�ϵ���㾵���������z����
	}
	
	//��ʼ��ͶӰ����Ĳ���
	public static void initProject(float factor)
	{
		left=-ratio*factor;
		right=ratio*factor;
		bottom=-1*factor;
		top=1*factor;
		near=1*factor;
	    far=500;
	}
}