package com.bn.Sample4_8;//������

public class SingleFlare {
	public int texture;//��������
	public float distance;//���루�ع�Դ����Ļ����ֱ�ߵĳɱ����ľ��룩
	public float size;//ԭʼ�ߴ�
	public float bSize;//�任��ĳߴ�
	public float[] color;//��ɫ����
	public float px;//����λ��x����
	public float py;//����λ��y����
	public SingleFlare(int texture,float size,float distance,float[] color)
	{
		this.texture=texture;//��ʼ������
		this.distance=distance;//��ʼ������ֵ
		this.size=size;//��ʼ��ԭʼ�ߴ�ֵ
		this.bSize=size;//��ʼ���任��ĳߴ�ֵ
		this.color=color;//��ʼ����ɫ����
		this.px=0;//��ʼ������λ��x����
		this.py=0;//��ʼ������λ��y����
	}
}
