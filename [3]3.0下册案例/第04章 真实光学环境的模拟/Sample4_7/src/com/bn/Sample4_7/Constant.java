package com.bn.Sample4_7;

import java.util.HashMap;
import java.util.HashSet;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

public class Constant
{
	public static float[][] yArray;
	public static float [][][] normols;
	public static final float LAND_HIGH_ADJUST=2f;//½�صĸ߶ȵ���ֵ
	public static final float LAND_HIGHEST=60f;//½�����߲� 
	//��λ����
	public static final float UNIT_SIZE=3.0f;
	
	//�ӻҶ�ͼƬ�м���½����ÿ������ĸ߶�
	public static float[][] loadLandforms(Resources resources,int index)
	{
		Bitmap bt=BitmapFactory.decodeResource(resources, index);
		int colsPlusOne=bt.getWidth();
		int rowsPlusOne=bt.getHeight();
		float[][] result=new float[rowsPlusOne][colsPlusOne];
		for(int i=0;i<rowsPlusOne;i++)
		{
			for(int j=0;j<colsPlusOne;j++)
			{
				int color=bt.getPixel(j,i);
				int r=Color.red(color);
				int g=Color.green(color); 
				int b=Color.blue(color);
				int h=(r+g+b)/3;
				result[i][j]=h*LAND_HIGHEST/255-LAND_HIGH_ADJUST;  
			}
		}
		return result;
	}

	public static float[][][]  caleNormal(float yArray[][])//��ڲ���Ϊ�ӻҶ�ͼ���صĸ߶�����
	{
		//���ȼ���ÿ�����������
		float [][][] vertices = new float [yArray.length][yArray[0].length][3];//���ɽ���ж���λ�õ���ά����
		float [][][] normols = new float [yArray.length][yArray[0].length][3];//���ɽ���ж��㷨��������ά����
		//�Ը߶�������������㶥��λ������
		for(int i=0;i<yArray.length;i++)
		{
			for(int j=0;j<yArray[0].length;j++)
			{
				float zsx=-UNIT_SIZE*yArray.length/2+i*UNIT_SIZE;
	    		float zsz=-UNIT_SIZE*yArray[0].length/2+j*UNIT_SIZE;
				vertices[i][j][0]=zsx; //�����x����  	
				vertices[i][j][1]=yArray[i][j];//�����y����
				vertices[i][j][2]=zsz;//�����z����
			}
		}
		//���ڴ�Ŷ��㷨������HashMap�����Ϊ�����������ֵΪ��Ӧ����ķ���������
		HashMap<Integer,HashSet<Normal>> hmn=new HashMap<Integer,HashSet<Normal>>();
		int rows = yArray.length-1; //�������������
		int cols = yArray[0].length-1;//�������������
		
		for(int i=0;i<rows;i++)//�Ե���������б���
        {
        	for(int j=0;j<cols;j++) 
        	{  
	      		//����4�����������
        		//0  1
        		//2  3
        		int []index = new int[4];//�������ڴ�ŵ�ǰ�����ĸ���������������
        		index[0]=i*(cols+1)+j;//������0�ŵ������
        		index[1]=index[0]+1;//������1�ŵ������
        		index[2]=index[0]+cols+1;//������2�ŵ������
        		index[3]=index[1]+cols+1;//������3�ŵ������
        		//���㵱ǰ��������������������ķ�����
	      		float vxa=vertices[i+1][j][0]-vertices[i][j][0];
	      		float vya=vertices[i+1][j][1]-vertices[i][j][1];
	      		float vza=vertices[i+1][j][2]-vertices[i][j][2];
	      		float vxb=vertices[i][j+1][0]-vertices[i][j][0];
	      		float vyb=vertices[i][j+1][1]-vertices[i][j][1];
	      		float vzb=vertices[i][j+1][2]-vertices[i][j][2];
	      		float[] vNormal1=Normal.vectorNormal(Normal.getCrossProduct
		      			(
		      					vxa,vya,vza,vxb,vyb,vzb
		      			));
	      		for(int k=0;k<3;k++){//��������ķ�����������������Ӧ�ķ�����������
	      			HashSet<Normal> hsn=hmn.get(index[k]);
	      			if(hsn==null)
	      			{//�����ϲ������򴴽�
	      				hsn=new HashSet<Normal>();
	      			}
	      			hsn.add(new Normal(vNormal1[0],vNormal1[1],vNormal1[2]));
	      			//�����ϷŽ�HsahMap��
	      			hmn.put(index[k], hsn);
        		} 
	      		
	      		//���㵱ǰ��������������������ķ�����
	      		vxa=vertices[i+1][j][0]-vertices[i+1][j+1][0];
	      		vya=vertices[i+1][j][1]-vertices[i+1][j+1][1];
	      		vza=vertices[i+1][j][2]-vertices[i+1][j+1][2];
	      		vxb=vertices[i][j+1][0]-vertices[i+1][j+1][0];
	      		vyb=vertices[i][j+1][1]-vertices[i+1][j+1][1];
	      		vzb=vertices[i][j+1][2]-vertices[i+1][j+1][2];
	      		float[] vNormal2=Normal.vectorNormal(Normal.getCrossProduct
		      			(
		      					vxb,vyb,vzb,vxa,vya,vza
		      			));
	      		for(int k=1;k<4;k++){//��������ķ�����������������Ӧ�ķ�����������
	      			HashSet<Normal> hsn=hmn.get(index[k]);
	      			if(hsn==null)
	      			{//�����ϲ������򴴽�
	      				hsn=new HashSet<Normal>();
	      			}
	      			hsn.add(new Normal(vNormal2[0],vNormal2[1],vNormal2[2]));
	      			//�����ϷŽ�HsahMap��
	      			hmn.put(index[k], hsn);
        		}
        	}
        }
		
		for(int i=0;i<yArray.length;i++)//�����������飬����ÿ�������ƽ��������
		{
			for(int j=0;j<yArray[0].length;j++)
			{
				int index=i*(cols+1)+j;
				
		    	HashSet<Normal> hsn=hmn.get(index);
		    	//���ƽ��������
		    	float[] tn=Normal.getAverage(hsn);
		    	//���������ƽ����������ŵ�������������
		    	normols[i][j] = tn;//���ط���������
			}
		}

		return normols;
	}
}