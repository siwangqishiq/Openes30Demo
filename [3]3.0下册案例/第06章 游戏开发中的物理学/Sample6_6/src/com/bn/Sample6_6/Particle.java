package com.bn.Sample6_6;

import com.bn.util.Vector3f;

public class Particle {
	float pfMass;								//�ʵ�����
	float pfInvMass;							//�ʵ������ĵ�����Ϊ�������
	Vector3f pvPosition;							//�ʵ�λ��
	Vector3f pvVelocity;							//�ʵ��ٶ�
	Vector3f pvAcceleration;						//�ʵ���ٶ�
	Vector3f pvForces;							//�ʵ��ܺ���
	boolean bLocked;							//�Ƿ�������־
	public Particle(){								//������
		this.pvPosition = new Vector3f(0,0,0);			//��ʼ��λ��
		this.pvVelocity = new Vector3f(0,0,0); 			//��ʼ���ٶ�
		this.pvAcceleration = new Vector3f(0,0,0); 		//��ʼ�����ٶ�
		this.pvForces = new Vector3f(0,0,0); 			//��ʼ������
	}
}
