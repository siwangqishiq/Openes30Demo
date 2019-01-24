package com.bn.util;

/*
 * ����3D�ռ�����������
 */
public class Vector3f {
	//��������������
	public float x;
	public float y;
	public float z;
	//������
	public Vector3f(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vector3f(Vector3f v) {
		this.x=v.x;
		this.y=v.y;
		this.z=v.z;
	}
	//��������񻯵ķ���
	public void normalize(){
		float mod = module();
		//��֤ģ��Ϊ��ʱ�ٹ������
		if(mod != 0){
			x = x/mod;
			y = y/mod;
			z = z/mod;
		}
	}
	//��������ģ�ķ���
	public float module(){
		return (float) Math.sqrt(x*x + y*y + z*z);
	}
	//��������ģƽ��
	public float moduleSq(){
		return (float) x*x + y*y + z*z;
	}
	//����
	public void sub(Vector3f v){
		this.x -= v.x;
		this.y -= v.y;
		this.z -= v.z;
	}
	//�ӷ�
	public void add(Vector3f v){
		this.x += v.x;
		this.y += v.y;
		this.z += v.z;
	}
	//����
	public void scale(float s){
		this.x *= s;
		this.y *= s;
		this.z *= s;
	}
	//���
	public float dotProduct(Vector3f v){
		return x*v.x+y*v.y+z*v.z;
	}
	//���
	public Vector3f crossProduct(Vector3f v)
	{
		return new Vector3f(
				y*v.z-z*v.y,
				z*v.x-x*v.z,
				x*v.y-y*v.x
				);
	}
	//�Ը��������¸�ֵ
	public void voluation(float x,float y,float z)
	{
		this.x=x;
		this.y=y;
		this.z=z;
	}
	//�Ը��������¸�ֵ
	public void voluation(Vector3f v)
	{
		this.x=v.x;
		this.y=v.y;
		this.z=v.z;
	}
	
	


}
