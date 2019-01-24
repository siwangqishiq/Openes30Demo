package com.bn.Sample3_15;//������

import static com.bn.Sample3_15.MySurfaceView.cx;
import static com.bn.Sample3_15.MySurfaceView.cz;
import static com.bn.Sample3_15.ParticleDataConstant.*;
import android.opengl.GLES30;

public class ParticleSystem implements Comparable<ParticleSystem> 
{
	//��ʼ��ɫ
	public float[] startColor;
	//��ֹ��ɫ
	public float[] endColor;
	//Դ�������
	public int srcBlend;
	//Ŀ��������
	public int dstBlend;
	//��Ϸ�ʽ
	public int blendFunc;
	//�������������
	public float maxLifeSpan;
	//���������ڲ���
	public float lifeSpanStep;
	//���Ӹ����߳�����ʱ����
	public int sleepSpan;
	//ÿ���緢����������
	public int groupCount;
	//���������
	public float sx;
	public float sy;
	//����λ��
	float positionX;
	float positionZ;
	//�����仯��Χ
	public float xRange;
	public float yRange;
	//���ӷ�����ٶ�
	public float vx;
	public float vy;
	//��ת�Ƕ�
	float yAngle=0;
	//������
	ParticleForDraw fpfd;
	//������־λ
	boolean flag=true;
	
	float halfSize;
	public float[] points;//������
    public ParticleSystem(float positionx,float positionz,ParticleForDraw fpfd,int count)
    {
    	this.positionX=positionx;
    	this.positionZ=positionz;
    	this.startColor=START_COLOR[CURR_INDEX];
    	this.endColor=END_COLOR[CURR_INDEX];
    	this.srcBlend=SRC_BLEND[CURR_INDEX]; 
    	this.dstBlend=DST_BLEND[CURR_INDEX];
    	this.blendFunc=BLEND_FUNC[CURR_INDEX];
    	this.maxLifeSpan=MAX_LIFE_SPAN[CURR_INDEX];
    	this.lifeSpanStep=LIFE_SPAN_STEP[CURR_INDEX];
    	this.groupCount=GROUP_COUNT[CURR_INDEX];
    	this.sleepSpan=THREAD_SLEEP[CURR_INDEX];
    	this.sx=0;
    	this.sy=0;
    	this.xRange=X_RANGE[CURR_INDEX];
    	this.yRange=Y_RANGE[CURR_INDEX];
    	this.vx=0;
    	this.vy=VY[CURR_INDEX];
    	this.halfSize=RADIS[CURR_INDEX];
    	
    	this.fpfd=fpfd;
    	
    	this.points=initPoints(count);//��ʼ�����Ӷ�����������
    	fpfd.initVertexData(points);//���ó�ʼ����������ķ���
    	
    	new Thread()
    	{
    		public void run()
    		{
    			while(flag)
    			{
    				update();
    				try 
    				{
						Thread.sleep(sleepSpan);
					} catch (InterruptedException e) 
					{
						e.printStackTrace();
					}
    			}
    		}
    	}.start();
    }
    
    public float[] initPoints(int zcount)
    {//��ʼ�����Ӷ������ݵķ���
    	float[] points=new float[zcount*4];//��ʱ��Ŷ������ݵ�����-ÿ�����Ӷ�Ӧ1�����㣬ÿ���������4��ֵ
    	for(int i=0;i<zcount;i++)//groupCount
    	{//ѭ��������������
    		//�����ĸ��������������ӵ�λ��------**/
    		float px=(float) (sx+xRange*(Math.random()*2-1.0f));//��������λ�õ�x����
            float py=(float) (sy+yRange*(Math.random()*2-1.0f));//��������λ�õ�y����
            float vx=(sx-px)/150;//�������ӵ�x�����˶��ٶ�
            points[i*4]=px;//������λ�õ�x�������points������
            points[i*4+1]=py;//������λ�õ�y�������points������
            points[i*4+2]=vx;//������x������ٶȴ���points������
            points[i*4+3]=10.0f;//�����ӵĵ�ǰ�����ڴ���points������----Ϊ10ʱ�����Ӵ���û�б�����״̬����Ϊ10ʱ�����Ӵ��ڻ�Ծ״̬
    	}
    	for(int j=0;j<groupCount;j++)
        {//ѭ��������һ��������
    		points[4*j+3]=lifeSpanStep;//�������������ڣ���Ϊ10ʱ����ʾ���Ӵ��ڻ�Ծ״̬
        }
    	
		return points;//�����������Ӷ�����������
    }
    
//    int countt=0;//����֡���ʵ�ʱ��������--������
//	long timeStart=System.nanoTime();//��ʼʱ��	
	
    public void drawSelf(int texId)
    {
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
    	
    	//�ر���ȼ��
        GLES30.glDisable(GLES30.GL_DEPTH_TEST);
    	//�������
        GLES30.glEnable(GLES30.GL_BLEND);  
        //���û�Ϸ�ʽ
         GLES30.glBlendEquation(blendFunc);
        //���û������
        GLES30.glBlendFunc(srcBlend,dstBlend); 
        
    	MatrixState.translate(positionX, 1, positionZ);
		MatrixState.rotate(yAngle, 0, 1, 0);
		
		MatrixState.pushMatrix();//�����ֳ�
    	fpfd.drawSelf(texId,startColor,endColor,maxLifeSpan);//��������Ⱥ   	
		MatrixState.popMatrix();//�ָ��ֳ�
    	
    	//������ȼ��
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
    	//�رջ��
        GLES30.glDisable(GLES30.GL_BLEND);  
    }
    
    int count=1;//�������ӵ�λ�ü�����
    public void update()//��������״̬�ķ���
    {	
    	if(count>=(points.length/groupCount/4))//������������������λ��ʱ
    	{
    		count=0;//���¼���
    	}
    	
    	//�鿴�������Լ�������һλ��
    	for(int i=0;i<points.length/4;i++)
    	{//ѭ��������������
    		if(points[i*4+3]!=10.0f)//��ǰΪ��Ծ����ʱ
    		{
    			points[i*4+3]+=lifeSpanStep;//���㵱ǰ������
        		if(points[i*4+3]>this.maxLifeSpan)//��ǰ�����ڴ������������ʱ---�������ø����Ӳ���
        		{
        			float px=(float) (sx+xRange*(Math.random()*2-1.0f));//��������λ��x����
                    float py=(float) (sy+yRange*(Math.random()*2-1.0f));//��������λ��y����
                    float vx=(sx-px)/150;//��������x������ٶ�
                    points[i*4]=px;//������λ�õ�x�������points������
                    points[i*4+1]=py;//������λ�õ�y�������points������
                    points[i*4+2]=vx;//������x������ٶȴ���points������
                    points[i*4+3]=10.0f;//�����ӵĵ�ǰ�����ڴ���points������----Ϊ10ʱ�����Ӵ���û�б�����״̬����Ϊ10ʱ�����Ӵ��ڻ�Ծ״̬
        		}else//��ǰ������С�����������ʱ----�������ӵ���һλ������
        		{
        			 points[i*4]+=points[i*4+2];//��������λ�õ�x����
                     points[i*4+1]+=vy;//��������λ�õ�y����
        		}
    		}
    	}
    	
    	for(int i=0;i<groupCount;i++)
    	{//ѭ������һ�������������ָ��λ�õ�����
    		if(points[groupCount*count*4+4*i+3]==10.0f)//������Ӵ���δ����״̬ʱ
    		{
    			points[groupCount*count*4+4*i+3]=lifeSpanStep;//��������--�������ӵ�ǰ��������
    		}
    	}
    	
    	synchronized(lock)
    	{//����--��ֹ�ڸ��¶�����������ʱ����������������������Ⱦ����
			fpfd.updatVertexData(points);//���¶����������ݻ���ķ���
    	}
    	//�´μ������ӵ�λ��
    	count++;
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
