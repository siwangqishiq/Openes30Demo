package com.bn.Sample5_10;
public class UpdateThread extends Thread{
	
	MySurfaceView mv;
	public UpdateThread(MySurfaceView mv)
	{
		this.mv=mv;
	}
	
	public void run()
	{
		while(true)
		{
			//��ȡ������������
			mv.waterReflect.calVerticesNormalAndTangent();
			try{
				mv.waterReflect.mytime++;
	            Thread.sleep(80);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

}
