package com.bn.qkj.util;

//���Ԫһ�η��̵Ĺ�����
public class EYYCFCUtil 
{
	public static double[] solveEquation
	(//���Ԫһ�η��̵ķ���
		//���� a0*x+b0*y+c0=0������ϵ��
		double a0,							//����1��xϵ��
		double b0, 							//����1��yϵ��
		double c0, 							//����1�ĳ���
		//���� a1*x+b1*y+c1=0������ϵ��
		double a1, 							//����2��xϵ��
		double b1,		 					//����2��xϵ��
		double c1							//����2�ĳ���
	){
		double x=(c1*b0-c0*b1)/(a0*b1-a1*b0);		//����ó���xֵ
		double y=(-a0*x-c0)/b0; 					//����ó���yֵ
		return new double[]{x,y};					//���ؼ�����
	}
	
	public static void main(String args[])
	{
		double[] r=solveEquation(2,4,7,3,4,5);
		System.out.println(r[0]+"|"+r[1]);
	}
}
