package com.bn.Sample10_2;//������

//���ڼ�¼���ص����ꡢ�����ƴ��ص����
public class BNPoint   
{
	//��һ�δ˴��ص��x��y����
   float oldX;
   float oldY;	
 //�Ƿ��Ѿ�������һ�δ���λ�õı�־λ
   boolean hasOld=false;
 //���ص㵱ǰx��y����
   float x;   
   float y;    
   
   public BNPoint(float x,float y){//������
	 //��ʼ��x��y����
	   this.x=x;
	   this.y=y;
   }
 //���ô��ص���λ�õķ���
   public void setLocation(float x,float y)
   {
	 //��ԭ��λ�ü�¼Ϊ��λ��
	   oldX=this.x;
	   oldY=this.y;
	 //�����Ƿ��Ѿ�������һ�δ���λ�õı�־λ
	   hasOld=true;
	 //������λ��
	   this.x=x;
	   this.y=y;
   }
   
 //�����������ص����ķ���
   public static float calDistance(BNPoint a,BNPoint b)
   {
	   float result=0;
	 //�����������ص��ֱ�߾���
	   result=(float)Math.sqrt(
	     (a.x-b.x)*(a.x-b.x)+
	     (a.y-b.y)*(a.y-b.y)
	   );	   
	   return result;
   }
}
