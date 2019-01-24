package com.bn.Sample6_4;
import java.util.ArrayList;

//��������ϵͳ����
public class GrainGroup {
	static GrainForDraw gfd;						//���ڻ������ӵĻ�����
	ArrayList<SingleGrain> al=new ArrayList<SingleGrain>();			//�������ӵ��б�
 	static final float SPEED_SPAN= (float) (1.5f+1.5f*Math.random());	//���ӳ��ٶ�
	static final float SPEED=0.02f;		//�����ƶ�ÿһ����ģ��ʱ�ӣ�Ҳ����ʱ����
	public GrainGroup(MySurfaceView mv){
    		gfd=new GrainForDraw(2,1,1,1,mv);		//�������ӵĻ�����
    		for(int i=0;i<400;i++){			//������б�����Ӳ�ͬ���ٶȵ�����
    			double elevation=0.35f*Math.random()*Math.PI+Math.PI*0.15f;	//����
    			double direction=Math.random()*Math.PI*2;		//��λ��
    			float vy=(float)(SPEED_SPAN*Math.sin(elevation));//�ֽ��3����ĳ��ٶ�
    			float vx=(float)(SPEED_SPAN*Math.cos(elevation)*Math.cos(direction));
    			float vz=(float)(SPEED_SPAN*Math.cos(elevation)*Math.sin(direction));
    			al.add(new SingleGrain(vx,vy,vz));	//�������Ӷ�����ӽ������б�
    		} 
	}
	
	long timeStamp=0;							//���ڼ����ʱ���
	public void drawSelf(){
		long currTimeStamp=System.nanoTime()/1000000;	//��ȡ��ǰϵͳʱ��
		if(currTimeStamp-timeStamp>10){	//��ʱ��������10ms�����������ǰ��һ��
			for(SingleGrain sp:al){		//ɨ�������б����޸����ӵ��ۼ��˶�ʱ��
				sp.timeSpan=sp.timeSpan+SPEED;
				if(sp.timeSpan>10){		//�ж����ӵ��ۼ��˶�ʱ���Ƿ����10
					sp.timeSpan=0;		//�����ӵ��ۼ��˶�ʱ�����
				} 
			}
			timeStamp=currTimeStamp;		//�������ڼ����ʱ���
		}
		int size=al.size();
		for(int i=0;i<size;i++){			//ѭ��ɨ���������ӵ��б����Ƹ�������
			try{ al.get(i).drawSelf();}catch(Exception e){} //��������
		}
	}
}
