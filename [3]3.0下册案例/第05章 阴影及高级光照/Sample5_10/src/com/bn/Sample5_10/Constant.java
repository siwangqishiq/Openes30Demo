package com.bn.Sample5_10;


public class Constant  
{ 
	public static float WIDTH_SPAN=2f*63;//���򳤶��ܿ��
	public static boolean threadFlag=true;//ˮ�滻֡�̹߳�����־λ	  
	public static float r=60;	//�������Ŀ���ľ��룬���������ת�İ뾶
	
	
	public static float XANGLE_MIN=-55;//�����������ת�ĽǶȷ�Χ����Сֵ
	public static float XANGLE_MAX=55;//�����������ת�ĽǶȷ�Χ�����ֵ
	public static float YANGLE_MIN=15;//�����������ת�ĽǶȷ�Χ����Сֵ
	public static float YANGLE_MAX=90;//�����������ת�ĽǶȷ�Χ�����ֵ
	
	public static final float UNIT_SIZE=0.6f;	
	public static int SCREEN_WIDTH;//��Ļ���
	public static  int SCREEN_HEIGHT;//��Ļ�߶�   

	//����ͶӰ����Ĳ���
	public static  float left=0;
	public static float right=0;
	public static float bottom=0;
	public static float top=0;
	public static float near=0;
	public static float far=0;
	public static float ratio =0;
	
	//ע��start======�������뾵���������Ŀ����up������һ�µ�
	//�����Ŀ��������
	public static float targetX=0;
	public static float targetY=0;
	public static float targetZ=0;
	
	//�������up����
	public static float upX=0;
	public static float upY=1;
	public static float upZ=0;	
	//ע��end======�������뾵���������Ŀ����up������һ�µ�
	
	//��������Ĺ۲�������
	public static float mainCameraX=0;
	public static float mainCameraY=0;
	public static float mainCameraZ=0;
	
	//����������Ĺ۲�������
	public static float mirrorCameraX=0;
	public static float mirrorCameraY=0;
	public static float mirrorCameraZ=0;	
	
	public static void calculateMainAndMirrorCamera(float xAngle,float yAngle)
	{
		mainCameraX = (float) (r*Math.cos(Math.toRadians(yAngle))*Math.sin(Math.toRadians(xAngle)));
		mainCameraY = (float) (r*Math.sin(Math.toRadians(yAngle)));
		mainCameraZ = (float) (r*Math.cos(Math.toRadians(yAngle))*Math.cos(Math.toRadians(xAngle)));
		
		//���㾵��������۲��ߵ�����
		mirrorCameraX=mainCameraX;//�����������x�������������z����һ��
		mirrorCameraY=2*targetY-mainCameraY;
		mirrorCameraZ=mainCameraZ;//���ݶԳƹ�ϵ���㾵���������z����
	}
	//��ʼ��ͶӰ����Ĳ���
	public static void initProject(float factor)
	{
		left=-ratio*factor*0.5f;
		right=ratio*factor*0.5f;
		bottom=-1*factor*0.5f;
		top=1*factor*0.5f;
		near=1*factor;
	    far=500;
	}
	
	public static  float waveFrequency1 = 0.19f;							//1�Ų���Ƶ
	public static float waveFrequency2 = 0.09f;								//2�Ų���Ƶ
	public static float waveFrequency3 = 0.01f;								//3�Ų���Ƶ
	
	
	public  static float waveAmplitude1=0.126f;							//1�Ų����
	public  static float waveAmplitude2=0.21f;							//2�Ų����
	public  static float waveAmplitude3=0.35f;							//3�Ų����
	
	
	public static float wave1PositionX=0;								//1�Ų���λ��
	public static float wave1PositionY=0;
	public static float wave1PositionZ=0;
	
	public static float wave2PositionX=-200;								//2�Ų���λ��
	public static float wave2PositionY=0;
	public static float wave2PositionZ=-200;
	
	public static float wave3PositionX=300;								//3�Ų���λ��
	public static float wave3PositionY=0;
	public static float wave3PositionZ=300;
	
	
	public static Object lock=new Object();//����Դ
}