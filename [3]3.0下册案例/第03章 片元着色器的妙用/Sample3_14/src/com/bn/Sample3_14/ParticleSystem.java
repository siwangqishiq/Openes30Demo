package com.bn.Sample3_14;//������

import static com.bn.Sample3_14.MySurfaceView.cx;
import static com.bn.Sample3_14.MySurfaceView.cz;
import static com.bn.Sample3_14.ParticleDataConstant.*;
import android.opengl.GLES30;

public class ParticleSystem implements Comparable<ParticleSystem> 
{
	public float[] startColor;//������ʼ��ɫ����
	public float[] endColor;//������ֹ��ɫ����
	public int srcBlend;//Դ�������
	public int dstBlend;//Ŀ��������
	public int blendFunc;//��Ϸ�ʽ
	public float maxLifeSpan;//�������������
	public float lifeSpanStep;//���������ڲ���
	public int sleepSpan;//���Ӹ����߳�����ʱ����
	public int groupCount;//ÿ���緢����������
	public float sx;//���������x����
	public float sy;//���������y����
	float positionX;//����λ��x����
	float positionZ;//����λ��z����
	public float xRange;//�����x����ı仯��Χ
	public float yRange;//�����y����ı仯��Χ
	public float vx;//���ӷ����x�����ٶ�
	public float vy;//���ӷ����y�����ٶ�
	float yAngle=0;//������ϵͳ����ת�Ƕ�
	ParticleForDraw fpfd;//����Ⱥ�Ļ�����
	boolean flag=true;//�̹߳����ı�־λ
	float halfSize;//���Ӱ뾶
	
	public float[] points;//���Ӷ�Ӧ�����ж�����������
	
    public ParticleSystem(float positionx,float positionz,ParticleForDraw fpfd,int count)
    {//������
    	this.positionX=positionx;//��ʼ��������ϵͳ�Ļ���λ��x����
    	this.positionZ=positionz;//��ʼ��������ϵͳ�Ļ���λ��y����
    	this.startColor=START_COLOR[CURR_INDEX];//��ʼ��������ʼ��ɫ
    	this.endColor=END_COLOR[CURR_INDEX];//��ʼ��������ֹ��ɫ
    	this.srcBlend=SRC_BLEND[CURR_INDEX];//��ʼ��Դ������� 
    	this.dstBlend=DST_BLEND[CURR_INDEX];//��ʼ��Ŀ��������
    	this.blendFunc=BLEND_FUNC[CURR_INDEX];//��ʼ����Ϸ�ʽ
    	this.maxLifeSpan=MAX_LIFE_SPAN[CURR_INDEX];//��ʼ��ÿ�����ӵ����������
    	this.lifeSpanStep=LIFE_SPAN_STEP[CURR_INDEX];//��ʼ��ÿ�����ӵ���������
    	this.groupCount=GROUP_COUNT[CURR_INDEX];//��ʼ��ÿ���緢��������
    	this.sleepSpan=THREAD_SLEEP[CURR_INDEX];//��ʼ���̵߳�����ʱ��
    	this.sx=0;//��ʼ��������ϵͳ�����ĵ�x����
    	this.sy=0;//��ʼ��������ϵͳ�����ĵ�y����
    	this.xRange=X_RANGE[CURR_INDEX];//��ʼ���Ӿ������ĵ�x�����������
    	this.yRange=Y_RANGE[CURR_INDEX];//��ʼ���Ӿ������ĵ�y�����������
    	this.vx=0;//��ʼ�����ӵ�x�����˶��ٶ�
    	this.vy=VY[CURR_INDEX];//��ʼ�����ӵ�y�����˶��ٶ�
    	this.halfSize=RADIS[CURR_INDEX];//��ʼ��������ϵͳ�����Ӱ뾶
    	
    	this.fpfd=fpfd;//��ʼ������Ⱥ�Ļ�����
    	
    	this.points=initPoints(count);//��ʼ����������Ӧ�����ж�����������
    	fpfd.initVertexData(points);//���ó�ʼ�����������������������ݵķ���
    	new Thread()
    	{//�������ӵĸ����߳�
    		public void run()//��дrun����
    		{
    			while(flag)
    			{
    				update();//����update������������״̬
    				try 
    				{
						Thread.sleep(sleepSpan);//����һ����ʱ��
					} catch (InterruptedException e) 
					{
						e.printStackTrace();//��ӡ�쳣��Ϣ
					}
    			}
    		}
    	}.start();//�����߳�
    }
    
	public float[] initPoints(int zcount)//��ʼ����������Ӧ�����ж������ݵķ���
	{
		float[] points=new float[zcount*4*6];//��ʱ��Ŷ������ݵ�����-ÿ�����Ӷ�Ӧ6�����㣬ÿ���������4��ֵ
		for(int i=0;i<zcount;i++)
		{//ѭ��������������
			//�����ĸ��������������ӵ�λ��------**/
			float px=(float) (sx+xRange*(Math.random()*2-1.0f));//��������λ�õ�x����
	        float py=(float) (sy+yRange*(Math.random()*2-1.0f));//��������λ�õ�y����
	        float vx=(sx-px)/150;//�������ӵ�x�����˶��ٶ�
	        
	        points[i*4*6]=px-halfSize/2;//���Ӷ�Ӧ�ĵ�һ�����x����
	        points[i*4*6+1]=py+halfSize/2;//���Ӷ�Ӧ�ĵ�һ�����y����
	        points[i*4*6+2]=vx;//���Ӷ�Ӧ�ĵ�һ�����x�����˶��ٶ�
	        points[i*4*6+3]=10.0f;//���Ӷ�Ӧ�ĵ�һ����ĵ�ǰ������--10�������Ӵ���δ����״̬
			
	        points[i*4*6+4]=px-halfSize/2;
	        points[i*4*6+5]=py-halfSize/2;
	        points[i*4*6+6]=vx;
	        points[i*4*6+7]=10.0f;
			
	        points[i*4*6+8]=px+halfSize/2;
	        points[i*4*6+9]=py+halfSize/2;
	        points[i*4*6+10]=vx;
	        points[i*4*6+11]=10.0f;
			
	        points[i*4*6+12]=px+halfSize/2;
	        points[i*4*6+13]=py+halfSize/2;
	        points[i*4*6+14]=vx;
	        points[i*4*6+15]=10.0f;
			
	        points[i*4*6+16]=px-halfSize/2;
	        points[i*4*6+17]=py-halfSize/2;
	        points[i*4*6+18]=vx;
	        points[i*4*6+19]=10.0f;
			
	        points[i*4*6+20]=px+halfSize/2;
	        points[i*4*6+21]=py-halfSize/2;
	        points[i*4*6+22]=vx;
	        points[i*4*6+23]=10.0f;
		}
		for(int j=0;j<groupCount;j++)
	    {//ѭ��������һ��������
			points[4*j*6+3]=lifeSpanStep;//�������������ڣ���Ϊ10ʱ����ʾ���Ӵ��ڻ�Ծ״̬
			points[4*j*6+7]=lifeSpanStep;//�������������ڣ���Ϊ10ʱ����ʾ���Ӵ��ڻ�Ծ״̬
			points[4*j*6+11]=lifeSpanStep;//�������������ڣ���Ϊ10ʱ����ʾ���Ӵ��ڻ�Ծ״̬
			points[4*j*6+15]=lifeSpanStep;//�������������ڣ���Ϊ10ʱ����ʾ���Ӵ��ڻ�Ծ״̬
			points[4*j*6+19]=lifeSpanStep;//�������������ڣ���Ϊ10ʱ����ʾ���Ӵ��ڻ�Ծ״̬
			points[4*j*6+23]=lifeSpanStep;//�������������ڣ���Ϊ10ʱ����ʾ���Ӵ��ڻ�Ծ״̬
			
	    }
		return points;//�����������Ӷ���������������
	}
    
//  int countt=0;//����֡���ʵ�ʱ��������--������
//	long timeStart=System.nanoTime();//��ʼʱ��	
	
    public void drawSelf(int texId)
    {//���ƴ�����ϵͳ���������ӵķ���
//    	if(countt==19)//ÿʮ��һ����֡����
//    	{
//    		long timeEnd=System.nanoTime();//����ʱ��
//    		
//    		//����֡����
//    		float ps=(float)(1000000000.0/((timeEnd-timeStart)/20));
//    		System.out.println("ps="+ps);
//    		countt=0;//��������0
//    		timeStart=timeEnd;//��ʼʱ����Ϊ����ʱ��
//    	}
//    	countt=(countt+1)%20;//���¼�������ֵ
    	
        GLES30.glDisable(GLES30.GL_DEPTH_TEST);//�ر���ȼ��
        GLES30.glEnable(GLES30.GL_BLEND); //������� 
        GLES30.glBlendEquation(blendFunc);//���û�Ϸ�ʽ
        GLES30.glBlendFunc(srcBlend,dstBlend); //���û������
        
    	MatrixState.translate(positionX, 1, positionZ);//ִ��ƽ�Ʊ任
		MatrixState.rotate(yAngle, 0, 1, 0);//ִ����ת�任
		
		MatrixState.pushMatrix();//�����ֳ�
    	fpfd.drawSelf(texId,startColor,endColor,maxLifeSpan);//��������Ⱥ   	
		MatrixState.popMatrix();//�ָ��ֳ�
    	
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);//������ȼ��
        GLES30.glDisable(GLES30.GL_BLEND);//�رջ��  
    }
    
	int count=1;//�������ӵ�λ�ü�����
	public void update()//��������״̬�ķ���
	{	
		if(count>=(points.length/groupCount/4/6))//������������������λ��ʱ
		{
			count=0;//���¼���
		}
		
		//�鿴�������Լ�������һλ�õ���Ӧ����
		for(int i=0;i<points.length/4/6;i++)
		{//ѭ��������������
			if(points[i*4*6+3]!=10.0f)//��ǰΪ��Ծ����ʱ
			{
				points[i*4*6+3]+=lifeSpanStep;//���㵱ǰ������
				points[i*4*6+7]+=lifeSpanStep;//���㵱ǰ������
				points[i*4*6+11]+=lifeSpanStep;//���㵱ǰ������
				points[i*4*6+15]+=lifeSpanStep;//���㵱ǰ������
				points[i*4*6+19]+=lifeSpanStep;//���㵱ǰ������
				points[i*4*6+23]+=lifeSpanStep;//���㵱ǰ������
	    		if(points[i*4*6+3]>this.maxLifeSpan)//��ǰ�����ڴ������������ʱ---�������ø����Ӳ���
	    		{
	    			float px=(float) (sx+xRange*(Math.random()*2-1.0f));//��������λ��x����
	                float py=(float) (sy+yRange*(Math.random()*2-1.0f));//��������λ��y����
	                float vx=(sx-px)/150;//��������x������ٶ�
	                
	                points[i*4*6]=px-halfSize/2;//���Ӷ�Ӧ�ĵ�һ�������x����
	    	        points[i*4*6+1]=py+halfSize/2;//���Ӷ�Ӧ�ĵ�һ�������y����
	    	        points[i*4*6+2]=vx;//���Ӷ�Ӧ�ĵ�һ�������x�����˶��ٶ�
	    	        points[i*4*6+3]=10.0f;//���Ӷ�Ӧ�ĵ�һ������ĵ�ǰ������--10�������Ӵ���δ����״̬
	    			
	    	        points[i*4*6+4]=px-halfSize/2;
	    	        points[i*4*6+5]=py-halfSize/2;
	    	        points[i*4*6+6]=vx;
	    	        points[i*4*6+7]=10.0f;
	    			
	    	        points[i*4*6+8]=px+halfSize/2;
	    	        points[i*4*6+9]=py+halfSize/2;
	    	        points[i*4*6+10]=vx;
	    	        points[i*4*6+11]=10.0f;
	    			
	    	        points[i*4*6+12]=px+halfSize/2;
	    	        points[i*4*6+13]=py+halfSize/2;
	    	        points[i*4*6+14]=vx;
	    	        points[i*4*6+15]=10.0f;
	    			
	    	        points[i*4*6+16]=px-halfSize/2;
	    	        points[i*4*6+17]=py-halfSize/2;
	    	        points[i*4*6+18]=vx;
	    	        points[i*4*6+19]=10.0f;
	    			
	    	        points[i*4*6+20]=px+halfSize/2;
	    	        points[i*4*6+21]=py-halfSize/2;
	    	        points[i*4*6+22]=vx;
	    	        points[i*4*6+23]=10.0f;
	    		}else//������С�����������ʱ----�������ӵ���һλ������
	    		{
	    			 points[i*4*6]+=points[i*4*6+2];//�������Ӷ�Ӧ�ĵ�һ�������x����
	                 points[i*4*6+1]+=vy;//�������Ӷ�Ӧ�ĵ�һ�������y����
	                 
	                 points[i*4*6+4]+=points[i*4*6+6];
	                 points[i*4*6+5]+=vy;
	                 
	                 points[i*4*6+8]+=points[i*4*6+10];
	                 points[i*4*6+9]+=vy;
	                 
	                 points[i*4*6+12]+=points[i*4*6+14];
	                 points[i*4*6+13]+=vy;
	                 
	                 points[i*4*6+16]+=points[i*4*6+18];
	                 points[i*4*6+17]+=vy;
	                 
	                 points[i*4*6+20]+=points[i*4*6+22];
	                 points[i*4*6+21]+=vy;
	    		}
			}
		}
		
		for(int i=0;i<groupCount;i++)
		{//ѭ������һ�������������ָ��λ�õ�����
			if(points[groupCount*count*4*6+4*i*6+3]==10.0f)//������Ӵ���δ����״̬ʱ
			{
				points[groupCount*count*4*6+4*i*6+3]=lifeSpanStep;//��������--�������ӵ�ǰ����������
				points[groupCount*count*4*6+4*i*6+7]=lifeSpanStep;//��������--�������ӵ�ǰ����������
				points[groupCount*count*4*6+4*i*6+11]=lifeSpanStep;//��������--�������ӵ�ǰ����������
				points[groupCount*count*4*6+4*i*6+15]=lifeSpanStep;//��������--�������ӵ�ǰ����������
				points[groupCount*count*4*6+4*i*6+19]=lifeSpanStep;//��������--�������ӵ�ǰ����������
				points[groupCount*count*4*6+4*i*6+23]=lifeSpanStep;//��������--�������ӵ�ǰ����������
			}
		}
		synchronized(lock)
		{//����--��ֹ�ڸ��¶�����������ʱ����������������������Ⱦ����
			fpfd.updatVertexData(points);//���¶����������ݻ���ķ���
		}
		count++;//�´μ������ӵ�λ��
	}
    
	public void calculateBillboardDirection()
	{
		//���������λ�ü�����泯��
		float xspan=positionX-MySurfaceView.cx;
		float zspan=positionZ-MySurfaceView.cz;
		
		if(zspan<=0)
		{
			yAngle=(float)Math.toDegrees(Math.atan(xspan/zspan));	
		}
		else
		{
			yAngle=180+(float)Math.toDegrees(Math.atan(xspan/zspan));
		}
	}
	@Override
	public int compareTo(ParticleSystem another) {
		//��д�ıȽ��������������������ķ���
		float xs=positionX-cx;
		float zs=positionZ-cz;
		
		float xo=another.positionX-cx;
		float zo=another.positionZ-cz;
		
		float disA=(float)(xs*xs+zs*zs);
		float disB=(float)(xo*xo+zo*zo);
		return ((disA-disB)==0)?0:((disA-disB)>0)?-1:1;  
	}

}
