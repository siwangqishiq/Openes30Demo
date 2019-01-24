package com.bn.qkj.util;

public class QKJUtil 
{
	//�����пռ�,����ֵΪ������
	public static double[] calQKJ
	(
		double p0x,//���������һ�����X����
		double p0y,//���������һ�����Y����
		double p0z,//���������һ�����Z����
		double p1x,//��������ڶ������X����
		double p1y,//��������ڶ������Y����
		double p1z,//��������ڶ������Z����			
		double p2x,//����������������X����
		double p2y,//����������������Y����
		double p2z,//����������������Z����	
		double p0s,//���������һ�����S��������
		double p0t,//���������һ�����T��������
		double p1s,//��������ڶ������S��������
		double p1t,//��������ڶ������T��������	
		double p2s,//����������������S��������
		double p2t//����������������T��������			
	)
	{
		//ÿ���������ǻ������������е�s���
		//���X���� 
		double a0=p1s-p0s;//����1�붥��0��������s��ֵ
		double b0=p1t-p0t;//����1�붥��0��������t��ֵ
		double c0=p0x-p1x;//����1�붥��0λ������x��ֵ
		
		double a1=p2s-p0s;//����2�붥��0��������s��ֵ
		double b1=p2t-p0t;//����2�붥��0��������t��ֵ
		double c1=p0x-p2x;	//����2�붥��0λ������x��ֵ
		
		double[] TBX=EYYCFCUtil.solveEquation(a0,b0,c0,a1,b1,c1);//���з���任���x����
		
		//���Y���� 
		a0=p1s-p0s;//����1�붥��0��������s��ֵ
		b0=p1t-p0t;//����1�붥��0��������t��ֵ
		c0=p0y-p1y;//����1�붥��0λ������y��ֵ
		
		a1=p2s-p0s;//����2�붥��0��������s��ֵ
		b1=p2t-p0t;//����2�붥��0��������t��ֵ
		c1=p0y-p2y;	//����2�붥��0λ������y��ֵ
		
		double[] TBY=EYYCFCUtil.solveEquation(a0,b0,c0,a1,b1,c1);//���з���任���y����
		
		//���Z���� 
		a0=p1s-p0s;//����1�붥��0��������s��ֵ
		b0=p1t-p0t;//����1�붥��0��������t��ֵ
		c0=p0z-p1z;//����1�붥��0λ������z��ֵ
		
		a1=p2s-p0s;//����2�붥��0��������s��ֵ
		b1=p2t-p0t;//����2�붥��0��������t��ֵ
		c1=p0z-p2z;	//����2�붥��0λ������z��ֵ
		
		double[] TBZ=EYYCFCUtil.solveEquation(a0,b0,c0,a1,b1,c1);//���з���任���z����
		
		return new double[]{TBX[0],TBY[0],TBZ[0]};//���ظ����������������
	}
	
	public static void main(String args[])
	{
		double[] r=calQKJ(5,5,-5, 0,0,0, 10,0,0, 0.5,0, 0,1, 1,1);
		System.out.println(r[0]+","+r[1]+","+r[2]);
	}
}
