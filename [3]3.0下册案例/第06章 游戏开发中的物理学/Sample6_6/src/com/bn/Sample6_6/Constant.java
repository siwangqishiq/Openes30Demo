package com.bn.Sample6_6;

import java.nio.FloatBuffer;

public class Constant 
{
	static FloatBuffer mVertexBufferForFlag;//���㻺������
	static Object lockA=new Object();//������A
	static Object lockB=new Object();//������B
	final static int NUMROWS = 7;//������������
	final static int NUMCOLS = 10;//������������
	final static int NUMVERTICES = (NUMROWS+1)*(NUMCOLS+1);//������Ŀ
	final static int NUMSPTINGS = (NUMROWS*(NUMCOLS+1)+(NUMROWS+1)*NUMCOLS+2*NUMROWS*NUMCOLS);//������Ŀ
	final static float RSTER = 0.75f/NUMROWS;//�ʵ��м��
	final static float CSTER = 1.0f/NUMCOLS;//�ʵ��м��
	final static float 	KRESTITUTION=0.3f; //����ϵ��
	final static float COLLISIONTOLERANCE = -6.6f;//����λ��
	final static float 	FRICTIONFACTOR=0.9f; //Ħ��ϵ��
	final static float 	FLAGPOLERADIUS=0.04f;//��˰뾶
	final static float GRAVITY = -0.7f;//�������ٶ�
	final static float SPRING_TENSION_CONSTANT = 500.f;//���쵯�ɲ���
	final static float SPRING_SHEAR_CONSTANT = 300.f;//�������ɲ���
	final static float SPRING_DAMPING_CONSTANT = 2.f;//��������
	static boolean isC = false;//�Ƿ�����ײ����־
	static float WindForce = 2.0f;//����
	final static float DRAGCOEFFICIENT = 0.01f;//��������
}
