#version 300 es
precision mediump float;//����Ĭ�ϸ��㾫��
uniform mat4 uMMatrix; //�任����
uniform vec3 uLightLocation;//��Դλ��
uniform vec3 uSkyColor;	//�����ɫ
uniform vec3 uGroundColor;//�����ɫ
in vec3 vNormal;//���մӶ�����ɫ�������Ķ��㷨����
in vec3 vPosition;//���մӶ�����ɫ�������Ķ���λ��

out vec4 fragColor;//�������ƬԪ��ɫ

void main()                         
{//������
	//����任��ķ�����
	vec3 normalTarget=vPosition+normalize(vNormal);
  	vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(vPosition,1)).xyz;
  	//����ӱ���㵽��Դλ�õ�����
  	vec3 position=(uMMatrix*vec4(vPosition,1)).xyz;
	vec3 lightVec=normalize(uLightLocation-position);
	float costheta=dot(newNormal,lightVec);//���㷨�����ͱ���㵽��Դλ�õ������ĵ��
	float a=costheta*0.5+0.5;//����aֵ
	vec3 vColor=mix(uGroundColor,uSkyColor,a);//�����ƬԪ�Ĺ�����ɫֵRGB-����������ɫ
	
	fragColor=vec4(vColor,1.0);//��ƬԪ��������ɫ
}