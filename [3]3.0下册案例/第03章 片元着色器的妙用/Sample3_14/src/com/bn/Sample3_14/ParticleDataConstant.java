package com.bn.Sample3_14;//������

import android.opengl.GLES30;//����������
     
public class ParticleDataConstant 
{       
	//��Դ������
	public static Object lock=new Object();
	//ǽ��ĳ������ű�
	public static float wallsLength=30;
	//��ǰ����  
    public static int CURR_INDEX=3;
    //����ĳ�ʼ��λ��  
    public static float distancesFireXZ=6;
    //����ĳ�ʼ��λ��  
    public static float distancesBrazierXZ=6;
    public static float[][] positionFireXZ={{distancesFireXZ,distancesFireXZ},{distancesFireXZ,-distancesFireXZ},{-distancesFireXZ,distancesFireXZ},{-distancesFireXZ,-distancesFireXZ}};
    public static float[][] positionBrazierXZ={{distancesBrazierXZ,distancesBrazierXZ},{distancesBrazierXZ,-distancesBrazierXZ},{-distancesBrazierXZ,distancesBrazierXZ},{-distancesBrazierXZ,-distancesBrazierXZ}};
    public static int walls[]=new int[6];
    
    public static final float[][] START_COLOR=
	{//������ʼ��ɫ
    	{0.7569f,0.2471f,0.1176f,1.0f},	//0-��ͨ����
    	{0.7569f,0.2471f,0.1176f,1.0f},	//1-��������
    	{0.6f,0.6f,0.6f,1.0f},			//2-��ͨ��
    	{0.6f,0.6f,0.6f,1.0f},			//3-������
	};
    
    public static final float[][] END_COLOR=
	{//������ֹ��ɫ
    	{0.0f,0.0f,0.0f,0.0f},	//0-��ͨ����
    	{0.0f,0.0f,0.0f,0.0f},	//1-��������
    	{0.0f,0.0f,0.0f,0.0f},	//2-��ͨ��
    	{0.0f,0.0f,0.0f,0.0f},	//3-������
	};
    
    public static final int[] SRC_BLEND=
	{//Դ�������
    	GLES30.GL_SRC_ALPHA,   				//0-��ͨ����
    	GLES30.GL_ONE,   					//1-��������
    	GLES30.GL_SRC_ALPHA,				//2-��ͨ��
    	GLES30.GL_ONE,						//3-������
	};
   
    public static final int[] DST_BLEND=
	{//Ŀ��������
    	GLES30.GL_ONE,      				//0-��ͨ����
    	GLES30.GL_ONE,      				//1-��������
    	GLES30.GL_ONE_MINUS_SRC_ALPHA,		//2-��ͨ��
    	GLES30.GL_ONE,						//3-������
	};
    
    public static final int[] BLEND_FUNC=
	{//��Ϸ�ʽ
    	GLES30.GL_FUNC_ADD,    				//0-��ͨ����
    	GLES30.GL_FUNC_ADD,    				//1-��������
    	GLES30.GL_FUNC_ADD,    				//2-��ͨ��
    	GLES30.GL_FUNC_REVERSE_SUBTRACT,	//3-������
	};
    
    public static final int[] COUNT=
	{//��������
    	340,   					//0-��ͨ����
    	340,   					//1-��������
    	99,						//2-��ͨ��
    	99,						//3-������
	};
   
    public static final float[] RADIS=
    {//�������Ӱ뾶
    	0.5f,		//0-��ͨ����
    	0.5f,		//1-��������
    	0.8f,		//2-��ͨ��
    	0.8f,		//3-������
    };
    
    public static final float[] MAX_LIFE_SPAN=
    {//�������������
    	5.0f,		//0-��ͨ����
    	5.0f,		//1-��������
    	6.0f,		//2-��ͨ��
    	6.0f,		//3-������
    };
   
    public static final float[] LIFE_SPAN_STEP=
    {//���������ڲ���
    	0.07f,		//0-��ͨ����
    	0.07f,		//1-��������
    	0.07f,		//2-��ͨ��
    	0.07f,		//3-������
    };
    
    public static final float[] X_RANGE=
	{//���ӷ����x���ҷ�Χ
	    0.5f,		//0-��ͨ����
	    0.5f,		//1-��������
	    0.5f,		//2-��ͨ��
	    0.5f,		//3-������
	};
    
    
    public static final float[] Y_RANGE=
	{//���ӷ����y���·�Χ
	    0.3f,		//0-��ͨ����
	    0.3f,		//1-��������
	    0.15f,		//2-��ͨ��
	    0.15f,		//3-������
	};
    
    public static final int[] GROUP_COUNT=
	{//ÿ���������������
    	4,			//0-��ͨ����
    	4,			//1-��������
    	1,			//2-��ͨ��
    	1,			//3-������
	};
    
    
    public static final float[] VY=
	{//����y�������ڵ��ٶ�
    	0.05f,		//0-��ͨ����
    	0.05f,		//1-��������
    	0.04f,		//2-��ͨ��
    	0.04f,		//3-������
	};
    
    public static final int[] THREAD_SLEEP=
    {//���Ӹ��������߳�����ʱ�䣨ms��
    	60,		//0-��ͨ����
    	60,		//1-��������
    	30,		//2-��ͨ��
    	30,		//3-������
    };
}
