package com.bn.Sample1_4;
public class UpdateThread extends Thread{
	
	MySurfaceView mv;
    int count=0;
    boolean isBallCube=true;//�����״̬����ʼ״̬Ϊ��    
	public UpdateThread(MySurfaceView mv)
	{
		this.mv=mv;
	}
	
	public void run()
	{
		while(true)
		{
			//��ȡ������������
			mv.mBallAndCube.calVertices(count,isBallCube);			
			try{
	            count++;
	            if(count%mv.mBallAndCube.span==0)
	            {
	            	count=0;
	            	isBallCube=!isBallCube;	          
	            }
	            Thread.sleep(40);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
