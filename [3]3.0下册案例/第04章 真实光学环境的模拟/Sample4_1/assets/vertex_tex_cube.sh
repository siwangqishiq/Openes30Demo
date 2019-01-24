#version 300 es
uniform mat4 uMVPMatrix; 	//�ܱ任����
uniform mat4 uMMatrix; 		//�任����
uniform vec3 uCamera;		//�����λ��
in vec3 aPosition;  	//����λ��
in vec3 aNormal;    	//���㷨����
out vec3 vTextureCoord;  //���ڴ��ݸ�ƬԪ��ɫ��������ͼ��������
void main() { 
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��     
   //����任��ķ����������
  vec3 normalTarget=aPosition+aNormal;
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal);  
  //����ӹ۲�㵽�����������(��������)
  vec3 eye=- normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  vTextureCoord=reflect(eye,newNormal);     //�������������ķ������������ݸ�ƬԪ��ɫ��
}        
