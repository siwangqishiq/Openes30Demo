package com.bn;
import static com.bn.Constant.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.*;

public class Noise2DUtil 
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
	public static double noise(int x,int y,double amp)
	{
		int n = x + y * 57;
		n = (n<<13) ^ n;
		double result=(( 1.0 - ( (n * (n * n * 15731 + 789221) + 1376312589) & 0x7fffffff) / 1073741824.0)+1)/2.0; 
		return result*amp;
	}

	//����ĳһ����Ƶ��ͼ
	public static BufferedImage calYJBP(int level)
	{
		//����˼���Ƶ��ʵ�ʴ�С
		int size=COUNT*pow(2,level-1);  
		//�����˼���Ƶ��ʵ��ͼ��
		BufferedImage  temp=new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		//����˼������
		double amp=255.0/pow(2,level);		
		if(Constant.ZQDBD_FLAG)
		{
			amp=255;
		}
		
		//ѭ������ÿһ������ֵ
		for(int i=0;i<size;i++)
		{
			for(int j=0;j<size;j++)
			{
				double val=noise(++X_CURR,++Y_CURR,amp);
				int gray=(int)val;
				int cResult=0x00000000;
                cResult+=255<<24;
                cResult+=gray<<16;
                cResult+=gray<<8;
                cResult+=gray;                
                temp.setRGB(j,i,cResult);
			}
		}
		
		//����512x512�Ľ��ͼ��
		BufferedImage  result=new BufferedImage(512,512,BufferedImage.TYPE_INT_ARGB);
		//������ű�
		int scale=512/size;
		//��ȡ����
		Graphics g=result.getGraphics();
		Graphics2D g2d=(Graphics2D)g;
		//���ò�ֵ
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

		//�������Ŷ���
		AffineTransform at=new AffineTransform();
		at.scale(scale, scale);
		g2d.drawImage(temp, at, null);
				
		return result;
	}
	
	
	public static BufferedImage[] calSYBP()
	{
		BufferedImage[] result=new BufferedImage[PLS+1];
		
		for(int i=1;i<=PLS;i++)
    	{
    		result[i-1]=calYJBP(i);
    	}
		
		//����512x512�Ľ��ͼ��
		result[PLS]=new BufferedImage(512,512,BufferedImage.TYPE_INT_ARGB);
		
		for(int i=0;i<512;i++)
		{
			for(int j=0;j<512;j++)
			{
				int gray=0;
				for(int k=1;k<=PLS;k++)
				{
					int color=result[k-1].getRGB(i, j);
					//��ֳ�RGB����ɫ��ͨ����ֵ
					int r=(color >> 16) & 0xff;
					int g=(color >> 8) & 0xff;
					int b=(color) & 0xff;
					int grayTemp=0;
					if(Constant.ZQDBD_FLAG)
					{
						grayTemp=(int)((r+g+b)/3.0/pow(2,k));
					}
					else
					{
						grayTemp=(r+g+b)/3;
					}
					gray+=grayTemp;
				}
				int cResult=0x00000000;
                cResult+=255<<24;
                cResult+=gray<<16;
                cResult+=gray<<8;
                cResult+=gray;                
                result[PLS].setRGB(i,j,cResult);
			}
		}
		
		return result;
	}
}
