#version 300 es
precision highp float;//����Ĭ�ϵĸ��㾫��
in vec4 vPosition;  //���մӶ�����ɫ�������Ķ���λ��
out float fragColor;//�������ƬԪ��ɫ
uniform highp vec3 uLightLocation;//��Դλ��
void main()                         
{   
   float dis=distance(vPosition.xyz,uLightLocation);//���㱻����ƬԪ����Դ�ľ���
   fragColor=dis;//����ƬԪ������ɫֵ 
}    