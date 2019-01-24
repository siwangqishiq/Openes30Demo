package com.bn;
import static com.bn.Constant.*;
import java.util.*;

public class Noise1DUtil 
{
	//��˷�����ķ���
	public static int pow(int a,int b)
	{
		int result=1;
		for(int i=0;i<b;i++)
		{
			result=result*a;
		}
		return result;
	}
	
	//�����������Ƶ�ķ���
	public static double noise(int x,double amp)
	{
		x = (x<<13) ^ x;
		double result=(( 1.0 - ( (x * (x * x * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0)+1)/2.0; 
		return result*amp;
	}
	
	 //���Ҳ�ֵ����
	public static double cosIn(double a,double b,double x)
	{
		double ft = x * Math.PI;
		double f = (1 - Math.cos(ft)) * 0.5;
		return a*(1-f) + b*f;
	}
	
	 //���ĳһ����Ƶ������
    public static double[][] calYJBP(int level)
    {
    	double[][] result=null;
    	 
    	//�������һ����Ƶ���ܶ���
    	int tc=COUNT*pow(BP,level-1);
    	//��һ����Ƶ�Ŀ��Ƶ�ֵ
    	double[] cp=new double[tc+1];
    	//��һ����ƵX����
    	double xSpan=X_SPAN/pow(BP,level-1);
    	//��һ����Ƶ�����
    	double amp=1.0/pow(BP,level-1);
    	//�����һ����Ƶ�����п��Ƶ�		
    	for(int i=0;i<=tc;i++)
    	{
    		cp[i]=noise(++X_CURR,amp);
    	}
    	
    	//��һ����Ƶ�з���
    	int qfs=pow(BP,8-level);
    	//��һ����Ƶ�зֺ��X����
    	double xSpanQf=xSpan/qfs;    	
    	//����зֲ�ֵ���������б�
    	List<double[]> list=new ArrayList<double[]>();
    	
    	for(int i=0;i<tc;i++)
    	{
    		//ȡ����һ�ε��������Ƶ��ֵ
    		double a=cp[i];
    		double b=cp[i+1];
    		//����ʼ����ӽ��б�
    		list.add(new double[]{i*xSpan,a});
    		//ѭ����ֵ���������м��
    		for(int j=1;j<qfs;j++)
    		{
    			//��ֵ��X
    			double xIn=(1.0/qfs)*j;
    			//����˵��ֵ�����б�
    			list.add(new double[]{i*xSpan+xSpanQf*j,cosIn(a,b,xIn)});
    		}
    	}
    	//�����һ��������б�
    	list.add(new double[]{COUNT,cp[tc]});
    	
    	result=new double[list.size()][];
    	for(int i=0;i<list.size();i++)
    	{
    		result[i]=list.get(i);
    	}    	    	
    	return result;
    }

    //������б�Ƶ������Լ������
    public static double[][][] calSYBP()
    {
    	double[][][] result=new double[PLS+1][][];
    	
    	for(int i=1;i<=PLS;i++)
    	{
    		result[i-1]=calYJBP(i);
    	}
    	
    	result[PLS]=new double[result[0].length][2];
    	
    	for(int i=0;i<result[0].length;i++)
    	{
    		result[PLS][i][0]=result[0][i][0];
    		double temp=0;
    		for(int j=0;j<PLS;j++)
    		{
    			temp+=result[j][i][1];
    		}
    		result[PLS][i][1]=temp;
    	}
    	return result;
    }
}
