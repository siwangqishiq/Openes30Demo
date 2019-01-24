package com.bn.Sample10_3;//������

import static com.bn.Sample10_3.MySurfaceView.*;
import static com.bn.Sample10_3.Sample10_3Activity.*;

public class KeyThread extends Thread
{
	//����״̬  1-up 2-down 4-left 8-right
    int keyState=0;//�洢������λ��int�ͱ���
	boolean flag=true;//�߳�ѭ����־
	@Override
	public void run()
	{
		while(flag)//�̲߳���ѭ����ʱ��ȡ������λֵ
		{
			if((keyState&0x1)!=0) 
			{//������ƶ�����Ӧ����
				xOffset-=0.3f;
			}
			else if((keyState&0x2)!=0)
			{//������ƶ�����Ӧ����
				xOffset+=0.3f;
			}
			if((keyState&0x4)!=0)
			{//��ʱ����ת������Ӧ����
				yAngle+=2.5f;
			}
			else if((keyState&0x8)!=0)
			{//˳ʱ����ת������Ӧ����
				yAngle-=2.5f;
			}
			if(yAngle>=360||yAngle<=-360)
			{//�����ת�ǶȾ���ֵ����360��������
				yAngle=0;
			}
			try
			{
				Thread.sleep(30);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}
	//��ⰴ�´��ص����������ö�Ӧ����λֵΪ1
	public void keyPress(float x,float y)
	{
		if(x>=0&&x<screenWidth/2&&y>=0&&y<screenHeight/2)
		{//��ǰ
			keyState=keyState|0x1;//���ص��ڲ����������
		}
		else if(x>=screenWidth/2&&x<screenWidth&&y>=0&&y<screenHeight/2)
		{//���
			keyState=keyState|0x2;//���ص��ڲ����������
		}
		else if(x>=0&&x<screenWidth/2&&y>=screenHeight/2&&y<screenHeight)
		{//����
			keyState=keyState|0x4;//���ص�����ʱ����ת����
		}
		else if(x>=screenWidth/2&&x<=screenWidth&&y>=screenHeight/2&&y<=screenHeight)
		{//����
			keyState=keyState|0x8;//���ص���˳ʱ����ת����
		}
	}
	//���̧�𴥿ص����������ö�Ӧ����λֵΪ0
	public void keyUp(float x,float y)
	{
		if(x>=0&&x<screenWidth/2&&y>=0&&y<screenHeight/2)
		{//��ǰ
			keyState=keyState&0xE;//���ص��ڲ����������
		}
		else if(x>=screenWidth/2&&x<screenWidth&&y>=0&&y<screenHeight/2)
		{//���
			keyState=keyState&0xD;//���ص��ڲ����������
		}
		else if(x>=0&&x<screenWidth/2&&y>=screenHeight/2&&y<screenHeight)
		{//����
			keyState=keyState&0xB;//���ص�����ʱ����ת����
		}
		else if(x>=screenWidth/2&&x<=screenWidth&&y>=screenHeight/2&&y<=screenHeight)
		{//����
			keyState=keyState&0x7;//���ص���˳ʱ����ת����
		}
	}
	//���д��ص��̧��ʱ���õķ���������keyStateֵ��Ϊ0
	public void clearKeyState()
	{
		keyState=keyState&0x0;
	}
}