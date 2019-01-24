package com.bn.Sample9_2;
import java.util.ArrayList;
import android.opengl.Matrix;
//���岿��
public class BodyPart 
{	//������
	LoadedObjectVertexNormalTexture lovnt;	
	//��������
	int index;  
	//��Ӧ��GLSurfaceView
	MySurfaceView msv;
	//�˹����ڸ���������ϵ�е�ʵʱ�任����
	float[] mFather=new float[16];
	//�˹�������������ϵ�е�ʵʱ�任����
	float[] mWorld=new float[16];	
	//�˹����ڸ���������ϵ�еĳ�ʼ�任����
    float[] mFatherInit=new float[16];
	//�˹�����ʼ���������������ϵ�еı任����������
	float[] mWorldInitInver=new float[16];	
	//���ձ任����
	float[] finalMatrix=new float[16];
	//�˹�������������������ϵ�е�ԭʼ����
	float fx,fy,fz;
    //�˹����Լ����ӹ����б�
    ArrayList<BodyPart> childs=new ArrayList<BodyPart>();
    //ָ�򸸹���������
    BodyPart father;
    //�Ƿ�����Ϳ��Ƶ�
    boolean lowestFlag=false;
    //��Ϳ��Ƶ�����
    float[][] lowestDots;
    Robot robot;
    //����������ڲ���
    //fx,fy,fzΪ�ӹ����������ڸ�����ϵ�е�����    
    public BodyPart(float fx,float fy,float fz,LoadedObjectVertexNormalTexture lovnt,int index,MySurfaceView msv,Robot robot)
    {    	
    	this.index=index;
    	this.msv=msv;    	
    	this.lovnt=lovnt;
    	this.fx=fx;
    	this.fy=fy;
    	this.fz=fz;    	    
    	this.robot=robot;
    }
    public BodyPart(float fx,float fy,float fz,LoadedObjectVertexNormalTexture lovnt,int index,
    		boolean lowestFlag,float[][] lowestDots,MySurfaceView msv,Robot robot)
    {    	
    	this.index=index;
    	this.msv=msv;    	
    	this.lovnt=lovnt;
    	this.fx=fx;
    	this.fy=fy;
    	this.fz=fz;    	 
    	this.lowestFlag=lowestFlag;
    	this.lowestDots=lowestDots;
    	this.robot=robot;
    }       
      //����������Ϳ��Ƶ�
    public void calLowest()
    {
    	//�ȼ����Լ����Ƶ����͵�
    	if(lowestFlag)
    	{
    		for(float[] p:lowestDots)
    		{
    			//����仯�������
    			float[] pqc={p[0],p[1],p[2],1};
    			float[] resultP={0,0,0,1};
    			Matrix.multiplyMV(resultP, 0, finalMatrix, 0, pqc, 0);
    			if(resultP[1]<robot.lowest)
    			{
    				robot.lowest=resultP[1];
    			}
    		}
    	}
    	
    	//�������еĺ���
    	for(BodyPart bp:childs)
    	{
    		bp.calLowest();
    	}   
    }
     //�����վ������ݿ����������õ����վ���
    public void copyMatrixForDraw()
    {
    	for(int i=0;i<16;i++)
    	{
    		
    		robot.fianlMatrixForDrawArray[index][i]=finalMatrix[i];
    	}
    }
    
    public void drawSelf(float[][] tempMatrixArray)
    {
    	//����Լ��Ļ����߲�Ϊ�գ�������Լ�
    	if(lovnt!=null)
    	{
    		MatrixState.pushMatrix();
    		//�����¾���
        	MatrixState.setMatrix(tempMatrixArray[index]);        	
        	lovnt.drawSelf();
        	MatrixState.popMatrix();   
    	}
    	//�������еĺ���
    	for(BodyPart bp:childs)
    	{
    		bp.drawSelf(tempMatrixArray);
    	}    	
    }
     //��������ÿ���ӹ����ڸ���������ϵ�е�ԭʼ����
    public void initFatherMatrix()
    {
    	float tx=fx;
    	float ty=fy;
    	float tz=fz;    	
    	
    	if(father!=null)
    	{//����������Ϊ�գ����ӹ����ڸ���������ϵ�е�ԭʼ����
    	 //Ϊ�ӹ�������������ϵ�е�ԭʼ�����ȥ��������������
    	 //����ϵ�е�ԭʼ����
    		tx=fx-father.fx;
    		ty=fy-father.fy;
    		tz=fz-father.fz;
    	}    	
    	//���ɳ�ʼ�Ĵ˹����ڸ���������ϵ�еĳ�ʼ�任����
    	Matrix.setIdentityM(mFather, 0);
    	Matrix.translateM(mFather, 0, tx, ty, tz);   
    	for(int i=0;i<16;i++)
    	{
    		mFatherInit[i]=mFather[i];
    	}
    	
    	//Ȼ������Լ������к���
    	for(BodyPart bc:childs)
    	{
    		bc.initFatherMatrix();
    	}
    }
       //��μ��������ӹ�����ʼ���������������ϵ�еı任����������
    public void calMWorldInitInver()
    {
    	Matrix.invertM(mWorldInitInver, 0, mWorld, 0);
    	//Ȼ������Լ������к���
    	for(BodyPart bc:childs)
    	{
    		bc.calMWorldInitInver();
    	}
    }
    public void updateBone(){//��μ������¹�������ķ���
    	//���ȸ��ݸ���������Լ�����������ϵ�еı任����
    	if(father!=null){
    	 //����������Ϊ����˹�������������ϵ�еı任����
         //Ϊ����������������ϵ�еı任��������Լ��ڸ�����
         //����ϵ�еı任����
    		Matrix.multiplyMM(mWorld, 0, father.mWorld, 0, mFather, 0);
    	}
    	else{
    		//��������Ϊ�գ���˹�������������ϵ�еı任����
    		//Ϊ�Լ��ڸ���������ϵ�еı任����
    		for(int i=0;i<16;i++)
    		{
    			mWorld[i]=mFather[i];
    		}
    	}
    	calFinalMatrix();
    	
    	//Ȼ������Լ������к���
    	for(BodyPart bc:childs)
    	{
    		bc.updateBone();
    	}
    }
       //��ȡ���յİ󶨵��˹����Ķ����ܴ˹���Ӱ��Ĵ�Mesh����ϵ������������λ�õ����ձ任����
    public void calFinalMatrix()
    {
    	Matrix.multiplyMM(finalMatrix, 0, mWorld, 0, mWorldInitInver, 0);
    }
       //���״̬
    public void backToIInit()
    {
    	for(int i=0;i<16;i++)
    	{
    		mFather[i]=mFatherInit[i];
    	}
    	
    	//Ȼ������Լ������к���
    	for(BodyPart bc:childs)
    	{
    		bc.backToIInit();
    	}
    } 
        //�ڸ�����ϵ��ƽ���Լ�
    public void transtate(float x,float y,float z)//������xyz���ƶ�
    {
    	Matrix.translateM(mFather, 0, x, y, z);
    }
        //�ڸ�����ϵ����ת�Լ�
    public void rotate(float angle,float x,float y,float z)//������xyz��ת��
    {
    	Matrix.rotateM(mFather,0,angle,x,y,z);
    }    
    //����ӹ���
    public void addChild(BodyPart child)
    {
    	childs.add(child);
    	child.father=this;
    }
}