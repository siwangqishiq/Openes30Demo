package com.bn.Sample6_6;
import static com.bn.Sample6_6.Constant.*;
//����
public class Spring {
	ParticleRet p1;								//���������ӵ�1���ʵ�λ��
	ParticleRet p2;								//���������ӵ�2���ʵ�λ��
	float k;									//���ɾ���ϵ��������ϵ����
	float d;									//����ϵ��
	float L;									//���ɾ�ֹʱ�ĳ���
	public Spring(){								//������
		this.p1 = new ParticleRet();			//��ʼ���������ӵ�1���ʵ�λ��
		this.p2 = new ParticleRet();			//��ʼ���������ӵ�2���ʵ�λ��
		this.k = SPRING_TENSION_CONSTANT;	//��ʼ�����ɾ���ϵ��
		this.d = SPRING_DAMPING_CONSTANT;//��ʼ����������ϵ��
	}
}
