package com.bn.Sample10_1;//������
/*
 * ���Ա����ص��ĳ����࣬
 * ����̳��˸�����Ա����ص�
 */
public abstract class TouchableObject {
	OBB preBox;//����OBB��Χ��
    float[] m = new float[16];//�����任����  
	float[] color=new float[]{1,1,1,1};//������ɫ
	float size = 1.5f;//��ǰ����ķŴ��ʣ����ڿ���ѡ��ʱ�Ŵ���ʾ��
	//�������OBB��Χ�еķ���
    public OBB getCurrBox(){
    	return preBox;//��ȡ�����Χ��
    
    }
    //����ѡ��״̬�ı�������ɫ���ߴ�ķ���
	public void changeOnTouch(boolean flag){
		if (flag) {//��ѡ��
			color = new float[] { 0, 1, 0, 1 };//�ı���ɫΪ��ɫ
			size = 3f;//�ı�ߴ�
		} else {//��û��ѡ��
			color = new float[] { 1, 1, 1, 1 };	//�ָ���ɫΪ��ɫ
			size = 1.5f;//�ָ��ߴ�
		}	
	}
	//���Ʊ任����
    public void copyM(){
    	for(int i=0;i<16;i++){
    		m[i]=MatrixState.getMMatrix()[i];
    	}
    }	
}
