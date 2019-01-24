package com.bn.Sample9_2;

public class DoActionThread extends Thread
{
    int currActionIndex=0;
    int currStep=0;
    Action currAction;
    Robot robot;
    MySurfaceView msv;
    
    public DoActionThread(Robot robot,MySurfaceView msv)
    {
    	this.robot=robot;
    	this.msv=msv;
    }
    
    public void run()
    {
    	try 
		{
			Thread.sleep(500);
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
    	//�õ���ǰ�Ķ������
    	currAction=ActionGenerator.acArray[currActionIndex];
    	while(true)
    	{
    		//�����ԭʼ�ĳ�ʼ�仯����
    		 robot.backToInit();
    		//����˴ζ����������ˣ��������һ�鶯���Ĳ���
    		if(currStep>=currAction.totalStep)
    		{
    			//ȡ����ƶ����ı����һ����Χ��
    			currActionIndex=(currActionIndex+1)%ActionGenerator.acArray.length;
    			//���»�ȡ��ǰ�Ķ������
    			currAction=ActionGenerator.acArray[currActionIndex];
    			//��ǰ�Ķ������ϸ�ڱ�ţ���Ϊ0;
    			currStep=0;
    		}    		
    		//�޸�����
    		for(float[] ad:currAction.data)
    		{
    			//��������
    			int partIndex=(int) ad[0];
    			//��������
    			int aType=(int)ad[1]; 
    			//��ǰ����ֵ
    			
    			if(aType==0)
    			{//ƽ��
    				float xStart=ad[2];
    				float yStart=ad[3];
    				float zStart=ad[4];
    				
    				float xEnd=ad[5];
    				float yEnd=ad[6];
    				float zEnd=ad[7];
    				
    				float currX=xStart+(xEnd-xStart)*currStep/currAction.totalStep;
    				float currY=yStart+(yEnd-yStart)*currStep/currAction.totalStep;
    				float currZ=zStart+(zEnd-zStart)*currStep/currAction.totalStep;
    				
    				robot.bpArray[partIndex].transtate(currX, currY, currZ);
    			}
    			else if(aType==1)
    			{//��ת
    				float startAngle=ad[2];
    				float endAngle=ad[3];
    				float currAngle=startAngle+(endAngle-startAngle)*currStep/currAction.totalStep;
    				float x=ad[4];
    				float y=ad[5];
    				float z=ad[6];
    				robot.bpArray[partIndex].rotate(currAngle, x, y, z);
    			}
    		}    
    		//��μ������¹�������ķ���
    		robot.updateState();
    		//����������Ϳ��Ƶ�
    		robot.calLowest();
    		//�����վ������ݿ����������õ����վ�����lowestForDraw���¸�ֵ����ֵ������������� ֵ
    		robot.flushDrawData();
    		//һ֡������ɻ���һ֡�Ķ���
    		currStep++;    		
    		try 
    		{
				Thread.sleep(30);
			} catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
    	}
    }
}
