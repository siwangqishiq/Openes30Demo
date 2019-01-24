package com.bn.Sample6_6;

import static com.bn.Sample6_6.Constant.*;

import com.bn.util.Vector3f;


public class ParticleControl {
	Particle particles[][] = new Particle[NUMROWS+1][NUMCOLS+1];//��������
	Spring springs[] = new Spring[NUMSPTINGS];//��������
    float vertices[]=new float[NUMCOLS*NUMROWS*2*3*3];//ÿ������xyz��������
    Collision collisions[] = new Collision[NUMVERTICES*2];//��ײ������
    Vector3f temp = new Vector3f(0, 0, 0);//��ʱ��������1
    Vector3f temp2 = new Vector3f(0, 0, 0);//��ʱ��������2
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
	public void initalize()//��ʼ�������ʵ�ϵͳ
	{
		for(int r=0;r<=NUMROWS;r++)//���ʵ������н��б���
		{
			for(int c=0;c<=NUMCOLS;c++)//���ʵ������н��б���
			{
				particles[r][c] = new Particle();//�����ʵ����
				float f;	//�����ʵ�������ʱ����
				if(((r==0)&&(c==0))||((r==NUMROWS)&&(c==NUMCOLS))){
					f = 1;//�����������Ϻ����½ǵ��ʵ�����Ϊ1
				}
				else if(((r==NUMROWS)&&(c==0))||((r==0)&&(c==NUMCOLS))){
					f=2;//�����������º����Ͻǵ��ʵ�����Ϊ2
				}
				else if(((r==0)&&((c!=0)&&(c!=NUMCOLS)))||((r==NUMROWS)&&((c!=0)&&(c!=NUMCOLS)))){
					f=3;//�����������±�Ե���ʵ�����Ϊ3
				}
				else{
					f=6;//�����ʵ������Ϊ6
				}
				f = f/8;//ͳһ���ʵ�������С��ǰ������ֵ��1/8
				//��������,������������
				particles[r][c].pfMass = f;
				particles[r][c].pfInvMass = 1/particles[r][c].pfMass;
				
				particles[r][c].pvPosition.x = FLAGPOLERADIUS+c * CSTER;	//�����������м���
				particles[r][c].pvPosition.y = RSTER*NUMROWS/2-r * RSTER; //�ʵ��ʼλ������
				particles[r][c].pvPosition.z = 0;
				if((r==0&&c==0)||(r==NUMROWS&&c==0))
				{//��Ϊ���Ͻǻ����½ǵ��ʵ�
					particles[r][c].bLocked = true;//���ʵ�����Ϊ������
				}
				else//����Ϊ���Ͻǻ����½ǵ��ʵ�
				{
					particles[r][c].bLocked = false;//�ʵ㲻������
				}
			}
		}
		//��ʼ������
		int count = 0;								//������
		for(int r=0;r<=NUMROWS;r++){					//���ʵ������н��б���
		  for(int c=0;c<=NUMCOLS;c++){				//���ʵ������н��б���
			if(c<NUMCOLS){						//��ʼ���ᵯ��
				springs[count] = new Spring();			//�������ɶ���
				springs[count].p1.r = r;				//�������ӵĵ�1���ʵ����
				springs[count].p1.c = c;				//�������ӵĵ�1���ʵ����
				springs[count].p2.r = r;				//�������ӵĵ�2���ʵ����
				springs[count].p2.c = c+1;				//�������ӵĵ�2���ʵ����
				temp.voluation(particles[r][c].pvPosition);	//����1���ʵ��λ�����ý���ʱ����
				temp.sub(particles[r][c+1].pvPosition);	//��ȥ��2���ʵ��λ��
				springs[count].L = temp.module();		//������ɵ�ԭʼ����
				count++;							//��������1
			}
			if(r<NUMROWS){						//��ʼ��������
				springs[count] = new Spring();			//�������ɶ���
				springs[count].p1.r = r;				//�������ӵĵ�1���ʵ����
				springs[count].p1.c = c;				//�������ӵĵ�1���ʵ����
				springs[count].p2.r = r+1;				//�������ӵĵ�2���ʵ����
				springs[count].p2.c = c;				//�������ӵĵ�2���ʵ����
				temp.voluation(particles[r][c].pvPosition);	//����1���ʵ��λ�����ý���ʱ����
				temp.sub(particles[r+1][c].pvPosition);	//��ȥ��2���ʵ��λ��
				springs[count].L = temp.module();		//������ɵ�ԭʼ����
				count++;							//��������1
			}
			if(r<NUMROWS&&c<NUMCOLS){			//��ʼ�����ϡ�����б����
				springs[count] = new Spring();			//�������ɶ���
				springs[count].k = SPRING_SHEAR_CONSTANT;//�����侢��ϵ��
				springs[count].p1.r = r;				//�������ӵĵ�1���ʵ����
				springs[count].p1.c = c;				//�������ӵĵ�1���ʵ����
				springs[count].p2.r = r+1;				//�������ӵĵ�2���ʵ����
				springs[count].p2.c = c+1;				//�������ӵĵ�2���ʵ����
				temp.voluation(particles[r][c].pvPosition);	//����1���ʵ��λ�����ý���ʱ����
				temp.sub(particles[r+1][c+1].pvPosition); 	//��ȥ��2���ʵ��λ��
				springs[count].L = temp.module();		//������ɵ�ԭʼ����
				count++;							//��������1
			}
			if(r<NUMROWS&&c>0){					//��ʼ�����ϡ�����б����
				springs[count] = new Spring();			//�������ɶ���
				springs[count].k = SPRING_SHEAR_CONSTANT; //�����侢��ϵ��
				springs[count].p1.r = r;				//�������ӵĵ�1���ʵ����
				springs[count].p1.c = c;				//�������ӵĵ�1���ʵ����
				springs[count].p2.r = r+1;				//�������ӵĵ�2���ʵ����
				springs[count].p2.c = c-1;				//�������ӵĵ�2���ʵ����
				temp.voluation(particles[r][c].pvPosition);	//����1���ʵ��λ�����ý���ʱ����
				temp.sub(particles[r+1][c-1].pvPosition); 	//��ȥ��2���ʵ��λ��
				springs[count].L = temp.module();		//������ɵ�ԭʼ����
				count++;							//��������1
		}}}
		for(int i=0; i<NUMVERTICES*2; i++){			//ѭ����ʼ����ײ����
			collisions[i] = new Collision();				//������ײ��Ϣ����
		}
	}
	public void calcForces(){							//�����ʵ������ķ���
		for(int r=0;r<=NUMROWS;r++){				//���ʵ������н��б���
			for(int c=0;c<=NUMCOLS;c++){		//���ʵ������н��б���
				particles[r][c].pvForces.x = 0;		//���ʵ�������X��������Ϊ0
				particles[r][c].pvForces.y = 0;		//���ʵ�������Y��������Ϊ0
				particles[r][c].pvForces.z = 0;		//���ʵ�������Z��������Ϊ0
			}}
		for(int r=0;r<=NUMROWS;r++){				//���ʵ������н��б���
			for(int c=0;c<=NUMCOLS;c++){			//���ʵ������н��б���
				if(!particles[r][c].bLocked){				//�����ʵ�û�б�����
					particles[r][c].pvForces.y += GRAVITY*particles[r][c].pfMass;  //��������
					temp.voluation(particles[r][c].pvVelocity);//���ʵ��ٶȿ�������ʱ����
					temp.normalize();					//��ȡ�ٶȵĵ�λ����
					//��������������䷽�����ʵ��ٶȷ����෴�����С���ʵ��ٶȳ�����
					temp.scale(-particles[r][c].pvVelocity.moduleSq()*DRAGCOEFFICIENT);
					particles[r][c].pvForces.add(temp);		//��������������������
					//����һ����XOZƽ��ƽ�У��������������
					temp.voluation((float)(Math.random()*1), 0, (float)(Math.random()*0.3f));
					temp.scale((float)(Math.random()*WindForce));//���ݵ�ǰ������С������������
					particles[r][c].pvForces.add(temp);		//����������������
				}}}
		for(int i=0;i<NUMSPTINGS;i++){		//�������е��ɼ��㵯�ɵ���
			int r1 = (int) springs[i].p1.r;			//��ȡ�������ӵĵ�1���ʵ���к�
			int c1 = (int) springs[i].p1.c;			//��ȡ�������ӵĵ�1���ʵ���к�
			int r2 = (int) springs[i].p2.r;			//��ȡ�������ӵĵ�2���ʵ���к�
			int c2 = (int) springs[i].p2.c;			//��ȡ�������ӵĵ�2���ʵ���к�
			temp.voluation(particles[r1][c1].pvPosition);//����һ���ʵ�λ�ø��ƽ���ʱ����
			temp.sub(particles[r2][c2].pvPosition);	//��ȥ��2���ʵ��λ��
			float pd = temp.module();			//����������ʵ��ľ���
			temp2.voluation(particles[r1][c1].pvVelocity);//����һ���ʵ��ٶȸ��ƽ���ʱ����
			temp2.sub(particles[r2][c2].pvVelocity);	//��ȥ��2���ʵ���ٶȵõ�����������
			float L = springs[i].L;				//��ȡ��ǰ���ɾ�ֹʱ�ĳ���
			//���ݵ�������ʽ���㵯�ɵ�����������֮�͵Ĵ�С
			float t = -(springs[i].k*(pd-L)+springs[i].d*(temp.dotProduct(temp2)/pd))/pd;
			temp.scale(t);								//�õ�����������
			if(!particles[r1][c1].bLocked){					//���ʵ�1δ������
				particles[r1][c1].pvForces.add(temp);			//������������������
			}
			if(!particles[r2][c2].bLocked){					//���ʵ�2δ������
				temp.scale(-1);		   //���㵯�����ķ�������Ϊ�ʵ�2���ܵ�����
				particles[r2][c2].pvForces.add(temp);			//������������������
			}
			
		}
	}
	public boolean checkForCollisions()
	{
		int	count = 0;
		boolean	state = false;
		//�����һ����ײ��Ϣ
		for(int i=0; i<collisions.length; i++){
			collisions[i].r = -1;
		}
		//���������������ײ
		for(int r=0; r<=NUMROWS; r++)
		{
			for(int c=0; c<=NUMCOLS; c++)
			{
				if(!particles[r][c].bLocked)
				{
					if((particles[r][c].pvPosition.y <= COLLISIONTOLERANCE) && (particles[r][c].pvVelocity.y < 0f))
					{
						state = true;
						collisions[count].r = r;
						collisions[count].c = c;
						collisions[count].n.x = 0.0f;
						collisions[count].n.y = 1.0f;
						collisions[count].n.z = 0.0f;
						count++;
					}
				}
			}
		}		
		//�����������˵���ײ
		for(int r=0; r<=NUMROWS; r++)
		{
			for(int c=0; c<=NUMCOLS; c++)
			{			
				if(!particles[r][c].bLocked)
				{
					//��ô�����λ�þ�������ߵ�ͶӰ�����
					float fd = (particles[r][c].pvPosition.x)*(particles[r][c].pvPosition.x)+
								(particles[r][c].pvPosition.z)*(particles[r][c].pvPosition.z);
					temp.voluation(particles[r][c].pvPosition.x,0,particles[r][c].pvPosition.z);
					if((fd <= FLAGPOLERADIUS) && (temp.dotProduct(particles[r][c].pvVelocity)>0f))
					{
						state = true;
						collisions[count].r = r;
						collisions[count].c = c;
						collisions[count].n.voluation(temp);
						collisions[count].n.normalize();
						count++;
					} 
				}
			}
		}
		return state;
	}
	public void resolveCollisions(){						//������ײ�ķ���
		for(int i=0; i<collisions.length; i++){				//������ײ��Ϣ��������
			if(collisions[i].r != -1){						//������ײ��Ϣ������Ч
				int r = collisions[i].r;					//��ȡ��ײ��Ӧ�ʵ���к�
				int c = collisions[i].c;					//��ȡ��ײ��Ӧ�ʵ���к�
				temp.voluation(collisions[i].n);			//��ȡ��ײ�淨����
				//��÷�����������ٶȷ���
				temp.scale(temp.dotProduct(particles[r][c].pvVelocity));
				temp2.voluation(particles[r][c].pvVelocity);	//���ʵ��ٶȿ�������ʱ����2
				temp2.sub(temp);			//��ȥ�����������ٶȷ����������ٶȷ���
				temp.scale(-KRESTITUTION);			//�����������ٶȳ��Ը�����ϵ��
				temp2.scale(FRICTIONFACTOR);		//�������ٶȳ��Զ�Ħ��ϵ��
				temp.add(temp2);					//�����������ٶȼ������ٶȵõ����ٶ�
				particles[r][c].pvVelocity.voluation(temp); 	//�����ٶ����ý��ʵ��ٶ�����
			}
		}
	}
	public void stepSimulation(float dt){			//ʵ���˶����̻��ֵķ���
		calcForces();							//����calcForces������������ʵ������
		for(int r=0;r<=NUMROWS;r++){			//���ʵ������н��б���
			for(int c=0;c<=NUMCOLS;c++){			//���ʵ������н��б���
				temp.voluation(particles[r][c].pvForces);	//���ʵ��ܺ������ƽ���ʱ����
				temp.scale(particles[r][c].pfInvMass);		//�����ʵ��ܼ��ٶ�
				particles[r][c].pvAcceleration.voluation(temp);//�����ٶȼ�¼���ʵ�����Ա
				temp.scale(dt);						//���ٶȳ��Բ���ʱ��õ��ٶȱ仯��
				particles[r][c].pvVelocity.add(temp);		//ԭ�ٶȼ����ٶȱ仯���õ����ٶ�
				temp.voluation(particles[r][c].pvVelocity);	//�����ٶȸ��ƽ���ʱ����
				temp.scale(dt);						//���ٶȳ��Բ���ʱ��õ�λ��
				particles[r][c].pvPosition.add(temp);		//ԭλ�ü���λ�Ƶõ���λ��
				if(particles[r][c].pvPosition.y<COLLISIONTOLERANCE){//����λ��Y������ڵ���
					particles[r][c].pvPosition.y=COLLISIONTOLERANCE;//��λ��Y������ڵ���߶�
				}
			}
		}
		if(isC){									//����������ײ���
			if(checkForCollisions()){					//������ײ��Ϣ
				resolveCollisions();					//������ײ
			}
		}
	}
}
