package com.bn.Sample10_1;//������

//�����OBB��Χ��
public class OBB
{
	Vector3f min;//OBB��Χ����x�ᡢy�ᡢz���ϵ���Сֵx��y��zֵ
	Vector3f max;//OBB��Χ����x�ᡢy�ᡢz���ϵ����ֵx��y��zֵ
	
	//����Ϊ��������Ĺ�����
	OBB(float[] vertices){
		min = new Vector3f();//X��Y��Z��Сֵ����
		max = new Vector3f();//X��Y��Z���ֵ����
		empty();//���OBB
		for(int i=0; i<vertices.length; i+=3){//ѭ���������ж���
			this.add(vertices[i], vertices[i+1], vertices[i+2]);//�����еĵ����OBB��Χ��
		}
	}
	//���OBB
	public void empty(){
		min.x = min.y = min.z = Float.POSITIVE_INFINITY;//����Сֵ��ʼ��ΪFloat���ֵ
		max.x = max.y = max.z = Float.NEGATIVE_INFINITY;//�����ֵ��ʼ��ΪFloat��Сֵ
	}

	//�����еĵ����OBB��Χ��
	public void add(float x, float y, float z){
		if (x < min.x) { min.x = x; }//����x����Сֵ
		if (x > max.x) { max.x = x; }//����x�����ֵ
		if (y < min.y) { min.y = y; }//����y����Сֵ
		if (y > max.y) { max.y = y; }//����y�����ֵ
		if (z < min.z) { min.z = z; }//����z����Сֵ
		if (z > max.z) { max.z = z; }//����z�����ֵ
	}
	/*
	 * Woo����ķ��������жϾ��α߽����ĸ�����ཻ��
	 * �ټ�����������������ƽ����ཻ�ԡ�
	 * ��������ں����У���ô��������α߽���ཻ��
	 * ���򲻴����ཻ 
	 */
	//�Ͳ������ߵ��ཻ�Բ��ԣ�������ཻ�򷵻�ֵ��һ���ǳ������(����1)
	//����ཻ�������ཻʱ��t
	//tΪ0-1֮���ֵ
	public float rayIntersect(
			Vector3f rayStart,//�������
			Vector3f rayEnd,//�����յ�
			Vector3f rayDir
			){
		//���δ�ཻ�򷵻��������
		final float kNoIntersection = Float.POSITIVE_INFINITY;
		//�����ھ��α߽��ڵ�����������㵽ÿ����ľ���
		float xt;
		
		if(rayStart.x<min.x){
			xt = min.x - rayStart.x;
			if(xt>rayDir.x){ return kNoIntersection; }
			xt /= rayDir.x;
		}
		else if(rayStart.x>max.x){
			xt = max.x - rayStart.x;
			if(xt<rayDir.x){ return kNoIntersection; }
			xt /= rayDir.x;
		}
		else{
			xt = -1.0f;
		}
		
		float yt;
		if(rayStart.y<min.y){
			yt = min.y - rayStart.y;
			if(yt>rayDir.y){ return kNoIntersection; }
			yt /= rayDir.y;
		}
		else if(rayStart.y>max.y){
			yt = max.y - rayStart.y;
			if(yt<rayDir.y){ return kNoIntersection; }
			yt /= rayDir.y;
		}
		else{
			yt = -1.0f;
		}
		
		float zt;
		if(rayStart.z<min.z){
			zt = min.z - rayStart.z;
			if(zt>rayDir.z){ return kNoIntersection; }
			zt /= rayDir.z;
		}
		else if(rayStart.z>max.z){
			zt = max.z - rayStart.z;
			if(zt<rayDir.z){ return kNoIntersection; }
			zt /= rayDir.z;
		}
		else{
			zt = -1.0f;
		}
		
		//ѡ����Զ��ƽ�桪�����������ཻ�ĵط�
		int which = 0;
		float t = xt;
		if(yt>t){
			which = 1;
			t=yt;
		}
		if(zt>t){
			which = 2;
			t=zt;
		}
		switch(which){
			case 0://��yzƽ���ཻ
			{
				float y=rayStart.y+rayDir.y*t;
				if(y<min.y||y>max.y){return kNoIntersection;}
				float z=rayStart.z+rayDir.z*t;
				if(z<min.z||z>max.z){return kNoIntersection;}
			}
			break;
			case 1://��xzƽ���ཻ
			{
				float x=rayStart.x+rayDir.x*t;
				if(x<min.x||x>max.x){return kNoIntersection;}
				float z=rayStart.z+rayDir.z*t;
				if(z<min.z||z>max.z){return kNoIntersection;}
			}
			break;
			case 2://��xyƽ���ཻ
			{
				float x=rayStart.x+rayDir.x*t;
				if(x<min.x||x>max.x){return kNoIntersection;}
				float y=rayStart.y+rayDir.y*t;
				if(y<min.y||y>max.y){return kNoIntersection;}
			}
			break;
		}
		return t;//�����ཻ�����ֵ
	}
}
