#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
in vec3 aPosition;  //����λ��
in vec3 aNormal;    //���㷨����
out vec3 vNormal;//���ڴ��ݸ�ƬԪ��ɫ���Ķ��㷨����
out vec3 vPosition;//���ڴ��ݸ�ƬԪ��ɫ���Ķ���λ��

void main()     
{ 	
  	vNormal=aNormal;//�����㷨�������ݸ�ƬԪ��ɫ��
  	vPosition=aPosition;//�������������ݸ�ƬԪ��ɫ��
	gl_Position=uMVPMatrix*vec4(aPosition,1);//�����ܱ任�������˴λ��ƴ˶���λ��
}