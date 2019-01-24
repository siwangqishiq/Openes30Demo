package com.bn;
import java.awt.*;
import javax.swing.*;
import static com.bn.Constant.*;

public class OneDDisplay extends JPanel
{
	private static final long serialVersionUID = -2109681415253211585L;

	public double[][][] lineData;
	
	
	public OneDDisplay()
	{
		this.setPreferredSize(new Dimension(550,900));
	}
	
	public void paint(Graphics g)
	{
		//�հ״�С
		final int span=5;
		//����
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, 1000,1000);
		
		
		if(lineData==null) return;
		
		//��ȡ��ͼ������
		int count=lineData.length;
		//���������X���
		double xSpan=X_ARRANGE/lineData[0].length;
		
		//ѭ������ÿһ��ͼ��
		for(int i=0;i<count;i++)
		{
			//�����һ����ͼ��
			g.setColor(Color.WHITE);
			if(i==count-1)
			{				
				g.fillRect(span, span*(i+1)+(int)BASE_AMPLITUDE*i, (int)X_ARRANGE, (int)BASE_AMPLITUDE+100);
			}
			else
			{
				g.fillRect(span, span*(i+1)+(int)BASE_AMPLITUDE*i, (int)X_ARRANGE, (int)BASE_AMPLITUDE);
			}
			
			
			//ȡ��һ����ͼ�ĵ�
			double[][] points=lineData[i];
			//�������ͼ��Y��ʼֵ
			int yStart=span*(i+1)+(int)BASE_AMPLITUDE*i;
			
			for(int j=0;j<points.length-1;j++)
			{				
				int x1=(int)(xSpan*j)+span;
				int y1=(int)(points[j][1]*BASE_AMPLITUDE*(i!=(count-1)?i+1:1))+yStart;
				int x2=(int)(xSpan*j+xSpan)+span;
				int y2=(int)(points[j+1][1]*BASE_AMPLITUDE*(i!=(count-1)?i+1:1))+yStart;
				
				g.setColor(Color.BLACK); 
				g.drawLine(x1,y1, x2, y2);
			}
		}
	}
	
	public void refresh(double[][][] lineData)
	{
		this.lineData=lineData;
		this.repaint();
	}
}
