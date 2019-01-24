package com.bn.Sample11_3A;


public class GrassGroup {
	GrassObject grass[];
	public int num;//�ܵ�С�ݿ���
	public float random[];
	//��λƽ����
	float unitSize=0.3f;
	int col[];
	int row[];
	float[] cx_Angle;//��y��--����
	float[] zt_Angle;//��x��--��̬�������̶ȣ�
	public float[] jbVertex;//��ɫ�����������������
	public GrassGroup(GrassObject object,int num)
	{
		this.num=num;
		//ÿ�ж��ٸ�---С������Ϊ2��n����
		int colCount=(int) Math.sqrt(num); 
		
		grass=new GrassObject[num];//GrassObject����
		col=new int[num];//�����������Ϣ�Ķ�������
		row=new int[num];//�����������Ϣ�Ķ�������
		random=new float[num];//�������������Ķ�������
		cx_Angle=new float[num];//��y��--����
		zt_Angle=new float[num];//��x��--��̬�������̶ȣ�
		jbVertex=new float[num*2];
		
		for(int i=0;i<num;i++)
		{//����С������
			grass[i]=object;
			random[i]=1+(float) Math.random();//����0~1�������
			
			//�����к�
			col[i]=i%colCount;
			//�����к�
			row[i]=i/colCount;
			
			jbVertex[2*i]=(float)col[i]/colCount;
			jbVertex[2*i+1]=(float)row[i]/colCount;
			cx_Angle[i]=(float) Math.random()*360;//��y��--����
			zt_Angle[i]=(float) Math.random()*30;//��x��--��̬�������̶ȣ�
		}
	}
	public void drawGroup(int texId,int jbTexId)
	{
		for(int i=0;i<num;i++)
		{
			MatrixState.pushMatrix();
			MatrixState.translate(unitSize*col[i]*random[i]-unitSize, 0, unitSize*row[i]*random[i]-unitSize);
			MatrixState.rotate(cx_Angle[i], 0, 1, 0);//��y��--����
			MatrixState.rotate(zt_Angle[i], 1, 0, 0);//��x��--��̬�������̶ȣ�
			//�����ص����岻Ϊ�����������
			if(grass[i]!=null)
			{
				grass[i].drawSelf(texId,jbTexId,new float[]{jbVertex[2*i],jbVertex[2*i+1]});
			}
			MatrixState.popMatrix();
		}
	}

}
