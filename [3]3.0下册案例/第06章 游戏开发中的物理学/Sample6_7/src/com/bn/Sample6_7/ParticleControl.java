package com.bn.Sample6_7;

import static com.bn.Sample6_7.Constant.*;

import com.bn.util.Vector3f;

public class ParticleControl {
	Particle particles[][] = new Particle[NUMROWS+1][NUMCOLS+1];//��������
	Spring springs[] = new Spring[NUMSPTINGS];//��������
    float vertices[]=new float[NUMCOLS*NUMROWS*2*3*3];//ÿ������xyz��������
    Vector3f temp = new Vector3f(0, 0, 0);//��ʱ��������1
    Vector3f temp2 = new Vector3f(0, 0, 0);//��ʱ��������2
    BallParticle bp = new BallParticle(0.3f);
	
	public ParticleControl()
	{
		initalize();
	}
	public float[] getVerties()//��ȡһ֡����
	{
	    int count=0;//���������
        for(int r=0;r<NUMROWS;r++)
        {
        	for(int c=0;c<NUMCOLS;c++)
        	{
        		vertices[count++]=particles[r][c].pvPosition.x;
        		vertices[count++]=particles[r][c].pvPosition.y;
        		vertices[count++]=particles[r][c].pvPosition.z;
        		
        		vertices[count++]=particles[r+1][c].pvPosition.x;
        		vertices[count++]=particles[r+1][c].pvPosition.y;
        		vertices[count++]=particles[r+1][c].pvPosition.z;
        		
        		vertices[count++]=particles[r][c+1].pvPosition.x;
        		vertices[count++]=particles[r][c+1].pvPosition.y;
        		vertices[count++]=particles[r][c+1].pvPosition.z;
        		
        		vertices[count++]=particles[r][c+1].pvPosition.x;
        		vertices[count++]=particles[r][c+1].pvPosition.y;
        		vertices[count++]=particles[r][c+1].pvPosition.z;
        		
        		vertices[count++]=particles[r+1][c].pvPosition.x;
        		vertices[count++]=particles[r+1][c].pvPosition.y;
        		vertices[count++]=particles[r+1][c].pvPosition.z;
        		
        		vertices[count++]=particles[r+1][c+1].pvPosition.x;
        		vertices[count++]=particles[r+1][c+1].pvPosition.y;
        		vertices[count++]=particles[r+1][c+1].pvPosition.z;
        	}
        }
		return vertices;
	}
	public Vector3f getBall()
	{
		return bp.pvPosition;
	}
	public void initalize()//��ʼ������ϵͳ����
	{
		
		for(int r=0;r<=NUMROWS;r++)
		{
			for(int c=0;c<=NUMCOLS;c++)
			{
				particles[r][c] = new Particle();
				float f;
				f = 1;
				//��������,������������
				particles[r][c].pfMass = f;
				particles[r][c].pfInvMass = 1/particles[r][c].pfMass;
				//�����ʼ��λ��
				particles[r][c].pvPosition.x = -CSTER*NUMCOLS/2+c * CSTER;
				particles[r][c].pvPosition.y = RSTER*NUMROWS-r * RSTER;
				particles[r][c].pvPosition.z = -r*0.83f/11;
				//���ò�������
				if(r==0||c==0||r==NUMROWS||c==NUMCOLS)
				{
					particles[r][c].bLocked = true;
				}
				else
				{
					particles[r][c].bLocked = false;
				}
			}
		}
		bp.pfMass = 22f;
		bp.pfInvMass = 1/bp.pfMass;
		bp.pvPosition.voluation(0, 1, 3);
		bp.pvVelocity.voluation(1, 0, -5f);
		bp.bLocked = false;
		//��ʼ������
		int count = 0;//������
		for(int r=0;r<=NUMROWS;r++)
		{
			for(int c=0;c<=NUMCOLS;c++)
			{
				
				if(c<NUMCOLS)//��ʼ��������
				{
					springs[count] = new Spring();
					//��һ�����ӵ�
					springs[count].p1.r = r;
					springs[count].p1.c = c;
					//�ڶ������ӵ�
					springs[count].p2.r = r;
					springs[count].p2.c = c+1;
					//���㳤��
					temp.voluation(particles[r][c].pvPosition);
					temp.sub(particles[r][c+1].pvPosition);
					springs[count].L = temp.module()+0.01f;
					count++;
				}
				if(r<NUMROWS)//��ʼ���ᵯ��
				{
					springs[count] = new Spring();
					//��һ�����ӵ�
					springs[count].p1.r = r;
					springs[count].p1.c = c;
					//�ڶ������ӵ�
					springs[count].p2.r = r+1;
					springs[count].p2.c = c;
					//���㳤��
					temp.voluation(particles[r][c].pvPosition);
					temp.sub(particles[r+1][c].pvPosition);
					springs[count].L = temp.module()+0.01f;
					count++;
				}
				if(r<NUMROWS&&c<NUMCOLS)//��ʼ���������µ���
				{
					springs[count] = new Spring();
					springs[count].k = SPRING_SHEAR_CONSTANT;
					//��һ�����ӵ�
					springs[count].p1.r = r;
					springs[count].p1.c = c;
					//�ڶ������ӵ�
					springs[count].p2.r = r+1;
					springs[count].p2.c = c+1;
					//���㳤��
					temp.voluation(particles[r][c].pvPosition);
					temp.sub(particles[r+1][c+1].pvPosition);
					springs[count].L = temp.module()+0.01f;
					count++;
				}
				if(r<NUMROWS&&c>0)//��ʼ���������µ���
				{
					springs[count] = new Spring();
					springs[count].k = SPRING_SHEAR_CONSTANT;
					//��һ�����ӵ�
					springs[count].p1.r = r;
					springs[count].p1.c = c;
					//�ڶ������ӵ�
					springs[count].p2.r = r+1;
					springs[count].p2.c = c-1;
					//���㳤��
					temp.voluation(particles[r][c].pvPosition);
					temp.sub(particles[r+1][c-1].pvPosition);
					springs[count].L = temp.module()+0.01f;
					count++;
				}
			}
		}

	}
	void calcForces()
	{
		//����������������0
		for(int r=0;r<=NUMROWS;r++)
		{
			for(int c=0;c<=NUMCOLS;c++)
			{
				particles[r][c].pvForces.x = 0;
				particles[r][c].pvForces.y = 0;
				particles[r][c].pvForces.z = 0;
			}
		}
		bp.pvForces.voluation(0,0,0);
		bp.pvForces.y += GRAVITY*bp.pfMass;
		//�����������������
		for(int r=0;r<=NUMROWS;r++)
		{
			for(int c=0;c<=NUMCOLS;c++)
			{
				if(!particles[r][c].bLocked)
				{
					//����
					particles[r][c].pvForces.y += GRAVITY*particles[r][c].pfMass;
								
					//ճ������=��ǰ�����ٶȷ�����λ����*�ٶȴ�Сƽ��*�������
					temp.voluation(particles[r][c].pvVelocity);
					temp.normalize();
					temp.scale(-particles[r][c].pvVelocity.moduleSq()*DRAGCOEFFICIENT);
					particles[r][c].pvForces.add(temp);
					
					//����=�������*�������
					temp.voluation((float)(Math.random()*1), 0, (float)(Math.random()*0.4f));
					temp.scale((float)(Math.random()*WindForce));
					particles[r][c].pvForces.add(temp);
				}
			}
		}
		
		for(int i=0;i<NUMSPTINGS;i++)
		{
			int r1 = (int) springs[i].p1.r;
			int c1 = (int) springs[i].p1.c;
			int r2 = (int) springs[i].p2.r;
			int c2 = (int) springs[i].p2.c;

			temp.voluation(particles[r1][c1].pvPosition);
			temp.sub(particles[r2][c2].pvPosition);//�������Ӽ����
			float pd = temp.module();

			temp2.voluation(particles[r1][c1].pvVelocity);
			temp2.sub(particles[r2][c2].pvVelocity);//�����ٶȲ�
			
			float L = springs[i].L;
			//���ݵ��ɹ�ʽ���㵯��
			float t = -(springs[i].k*(pd-L)+springs[i].d*(temp.dotProduct(temp2)/pd))/pd;
			temp.scale(t);
			
			if(!particles[r1][c1].bLocked)
			{
				particles[r1][c1].pvForces.add(temp);
			}
			if(!particles[r2][c2].bLocked)
			{
				temp.scale(-1);
				particles[r2][c2].pvForces.add(temp);
			}
		}
		
		
		for(int r=0; r<=NUMROWS; r++){				//���ʵ������н��б���
			for(int c=0; c<=NUMCOLS; c++){			//���ʵ������н��б���
				if(!particles[r][c].bLocked){				//�ж��ʵ��Ƿ�����
					temp.voluation(particles[r][c].pvPosition);	//����ǰ�ʵ�λ�ø��ƽ���ʱ����
					temp.sub(bp.pvPosition);				//��ȥ�����ʵ�λ��
					float fd = temp.moduleSq();			//��ȡ�����ʵ�����ʵ�����ƽ��
					if(fd < bp.rQ){					//������ƽ��С������뾶ƽ��
						float u = (bp.ballR-(float)Math.sqrt(fd))/bp.ballR; //������������ֵ
						float f = u*5000;				//��������ֵ����ϵ��������Ĵ�С
						temp.normalize();				//�õ����ķ��������Ĺ�񻯰汾
						temp.scale(f);					//�������Ĵ�С����Ϊ���Ĵ�С
						particles[r][c].pvForces.add(temp);	//�����������ʵ����
						temp.scale(-1);				//�������÷�
						bp.pvForces.add(temp);			//�����������ʵ����
					} 
				}
			}
		}
	}
	public boolean checkForCollisions(){
		boolean	state = false;						//�Ƿ�����ײ��־
		if((bp.pvPosition.y <= COLLISIONTOLERANCE) 	//�������ʵ���ڵ���
				&& (bp.pvVelocity.y < 0f)){						//�������ʵ��Y���ٶ�������
			state = true;							//����ײ��־����Ϊtrue
			bp.cn.x = 0.0f;							//��¼��ײ�淨������X����
			bp.cn.y = 1.0f;							//��¼��ײ�淨������Y����
			bp.cn.z = 0.0f;							//��¼��ײ�淨������Z����
		}
		return state;								//������ײ��־ֵ
	}
	
	public void	resolveCollisions()
	{	
		temp.voluation(bp.cn);//��ȡ��ײ������
		temp.scale(temp.dotProduct(bp.pvVelocity));//��÷�����������ٶȷ���
		temp2.voluation(bp.pvVelocity);//��ȡ�ٶ�
		temp2.sub(temp);//��ȥ����������������������
		temp.scale(-KRESTITUTION);//�����������ٶȳ��Է���ϵ��
		temp2.scale(FRICTIONFACTOR);//�������ٶȳ��Զ�Ħ��ϵ��
		temp.add(temp2);//������µ��ٶ�
		bp.pvVelocity.voluation(temp);

	}
	
	void stepSimulation(float dt)
	{
		calcForces();//�������
		for(int r=0;r<=NUMROWS;r++)
		{
			for(int c=0;c<=NUMCOLS;c++)
			{
				temp.voluation(particles[r][c].pvForces);
				temp.scale(particles[r][c].pfInvMass);//������ٶ�
				particles[r][c].pvAcceleration.voluation(temp);
				temp.scale(dt);//���ٶȳ˽���ʱ��
				particles[r][c].pvVelocity.add(temp);//�����µ��ٶ�
				temp.voluation(particles[r][c].pvVelocity);
				temp.scale(dt);
				particles[r][c].pvPosition.add(temp);//�����µ�λ��
			}
		}
		temp.voluation(bp.pvForces);
		temp.scale(bp.pfInvMass);//������ٶ�
		bp.pvAcceleration.voluation(temp);
		temp.scale(dt);//���ٶȳ˽���ʱ��
		bp.pvVelocity.add(temp);//�����µ��ٶ�
		temp.voluation(bp.pvVelocity);
		temp.scale(dt);
		bp.pvPosition.add(temp);//�����µ�λ��
		if(isC)
		{
			if(checkForCollisions())
			{
				resolveCollisions();
			}
		}
	}
	
}
