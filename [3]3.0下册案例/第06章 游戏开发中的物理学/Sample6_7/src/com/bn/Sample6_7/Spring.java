package com.bn.Sample6_7;

import static com.bn.Sample6_7.Constant.*;
//����
public class Spring {
	ParticleRet p1;//������1������
	ParticleRet p2;//������2������
	float k;//��չ�ĵ��ɳ���
	float d;//����ϵ��
	float L;//���ɾ�ֹʱ�ĳ���
	
	public Spring()
	{
		this.p1 = new ParticleRet();
		this.p2 = new ParticleRet();
		this.k = SPRING_TENSION_CONSTANT;
		this.d = SPRING_DAMPING_CONSTANT;
	}
}
