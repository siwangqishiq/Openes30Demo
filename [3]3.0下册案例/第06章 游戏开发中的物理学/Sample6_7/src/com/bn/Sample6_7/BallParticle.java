package com.bn.Sample6_7;

import com.bn.util.Vector3f;

public class BallParticle extends Particle{				//�̳�����ͨ�ʵ���
	float ballR;								//����뾶
	Vector3f cn;								//��ײ������
	float rQ;									//����뾶��ƽ��
	public BallParticle(float r){						//������
		super();								//���ø��๹����
		cn = new Vector3f(0, 0, 0);				//��ʼ����ײ������
		ballR = r;								//��ʼ���뾶
		rQ = r*r;								//����뾶��ƽ��
	}
}
