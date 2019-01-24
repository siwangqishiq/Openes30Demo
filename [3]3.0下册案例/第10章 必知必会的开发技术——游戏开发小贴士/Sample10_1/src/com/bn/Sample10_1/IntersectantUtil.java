package com.bn.Sample10_1;//������
public class IntersectantUtil {	
	/*
	 * 1��ͨ������Ļ�ϵĴ���λ�ã������Ӧ�Ľ�ƽ�������꣬
	 * �Ա����AB���������������ϵ�е�����
	 * 2����AB�����������������ϵ�е�����������������������
	 * �Ա����AB��������������ϵ�е�����
	 */
	public static float[] calculateABPosition(//����AB������ķ���
		float x,float y,//���ص�x��y����
		float w,float h,//��Ļ��ȡ��߶�
		//��ƽ�����
		float left,//�ӽ�leftֵ
		float top,//�ӽ�topֵ
		float near,//�ӽ�nearֵ
		float far//�ӽ�farֵ
	)
	{
		//������Ļ���Ͻ�Ϊԭ��Ĵ��ص����껻��Ϊ����Ļ����Ϊ����ԭ�������
		float x0=x-w/2;
		float y0=h/2-y;		
		//�����Ӧ��near���ϵ�x��y����
		float xNear=2*x0*left/w;
		float yNear=2*y0*top/h;
		//�����Ӧ��far���ϵ�x��y����
		float ratio=far/near;//����far��near�ı�ֵ
		float xFar=ratio*xNear;//�����Ӧ��far���ϵ�x����
		float yFar=ratio*yNear;//�����Ӧ��far���ϵ�y����
		//���������ϵ��A������
        float ax=xNear;//���������ϵ��A���x����
        float ay=yNear;//���������ϵ��A���y����
        float az=-near;//���������ϵ��A���z����
        //���������ϵ��B������
        float bx=xFar;//���������ϵ��B���x����
        float by=yFar;//���������ϵ��B���y����
        float bz=-far; //���������ϵ��B���z����
        //ͨ�����������ϵ��A��B��������꣬����������ϵ��A��B���������
		float[] A = MatrixState.fromPtoPreP(new float[] { ax, ay, az });//����������ϵ��A������
		float[] B = MatrixState.fromPtoPreP(new float[] { bx, by, bz });//����������ϵ��B������
		return new float[] {//����AB��������������ϵ�е�����
			A[0],A[1],A[2],
			B[0],B[1],B[2]
		};
	}
}