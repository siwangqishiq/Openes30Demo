package com.bn;
import java.awt.*;
import javax.swing.*;

public class TwoDDisplay extends JPanel
{
	private static final long serialVersionUID = -2109681415253211585L;

	public Image[] data;
	
	
	public TwoDDisplay()
	{
		this.setPreferredSize(new Dimension(550,8000));
	}
	
	public void paint(Graphics g)
	{
		//�հ״�С
		final int span=5;
		//����
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 1000,8000);
		
		
		if(data==null) return;
		
		//��ȡ��ͼ������
		int count=data.length;
		
		//ѭ������ÿһ��ͼ��
		for(int i=0;i<count;i++)
		{
			g.drawImage(data[i],span,512*i+span*(i+1),this);
		}
	}
	
	public void refresh(Image[] data)
	{
		this.data=data;
		this.repaint();
	}
}
