package com.bn.Sample11_6a;

import android.opengl.Matrix;

public class Constant 
{
	public static final int WATER_WIDTH=127;//ˮ����������
	public static final int WATER_HEIGHT=127;//ˮ�����������
	public static final float WATER_UNIT_SIZE=1.5f;//ˮ����ӳߴ�
	
	//������۲�Ŀ��㣬����Ϊˮ�����ε����ĵ�
	public static final float tx=WATER_WIDTH*WATER_UNIT_SIZE/2.0f;
	public static final float ty=0;
	public static final float tz=WATER_HEIGHT*WATER_UNIT_SIZE/2.0f;
	//�����λ��
	public static float cx,cy,cz;	
	//�����up����
	public static float upx,upy,upz;
	//���������
	public static float direction=0;
	//���������
	public static float yj=45;
	//�������Ŀ���ľ���
	public static float fsjl=(float)Math.sqrt((WATER_WIDTH*WATER_UNIT_SIZE)*(WATER_WIDTH*WATER_UNIT_SIZE)/4+
			                    (WATER_HEIGHT*WATER_UNIT_SIZE)*(WATER_HEIGHT*WATER_UNIT_SIZE)/4)*0.7f;
	
	//����������������ǵȼ��������9����
	public static void calCamera()
	{
		//���������λ�õ�
		float cameraV[]={0,0,fsjl,1};
		//����������仯����
		float[] m=new float[16];
		Matrix.setIdentityM(m,0);
		//��λ����ת
		Matrix.rotateM(m,0,direction,0,1,0);
		//������ת
		Matrix.rotateM(m,0,-yj,1,0,0);
		//���������������۲���������λ��
		float[] cameraVResult=new float[4];
		Matrix.multiplyMV(cameraVResult,0,m,0,cameraV,0);
	    //�����up����
		float up[]={0,1,0,1};
		float[] upResult=new float[4];
		Matrix.multiplyMV(upResult,0,m,0,up,0);
		//���ý�������
		cx=tx+cameraVResult[0];
		cy=ty+cameraVResult[1];
		cz=tz+cameraVResult[2];
		upx=upResult[0];
		upy=upResult[1];
		upz=upResult[2];		
	}
	
	//����һ������ָ����ĸ߶��Ŷ�
	public static float calHdr(
		float[] bx,	//����
		float bc,	//����
		float zf,	//���
		float qsj,	//��ʼ��
		float[] ddxz	//���Ŷ��Ķ���xz����
	){
	    //�����벨�ĵľ���
	    float dis=distance(ddxz,bx);
	    //����Ƕȿ��
	    float angleSpan=dis*2*3.1415926f/bc;
	    //����˲��Դ˶��������Ŷ�
	    float hrd=(float) (Math.sin(angleSpan+qsj)*zf);
	    //���ظ߶��Ŷ�ֵ
	    return hrd;
	}
	
	public static float distance(float[] ddxz,float[] bx)
	{
		float result=(float) Math.sqrt((ddxz[0]-bx[0])*(ddxz[0]-bx[0])+(ddxz[1]-bx[1])*(ddxz[1]-bx[1]));
		return result;
	}
	
	//�����������Ĳ��
	public static float[] getCrossProduct(float x1,float y1,float z1,float x2,float y2,float z2)
	{		
		//�������ʸ�����ʸ����XYZ��ķ���ABC
	    float A=y1*z2-y2*z1;
	    float B=z1*x2-z2*x1;
	    float C=x1*y2-x2*y1;
			
	    return new float[]{A,B,C};
	}
		
	//�������
	public static float[] vectorNormal(float[] vector)
	{
		//��������ģ
		float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
	}
}
