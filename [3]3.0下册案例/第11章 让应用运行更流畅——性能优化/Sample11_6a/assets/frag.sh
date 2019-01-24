#version 310 es
precision mediump float;

uniform sampler2D sTexture;//������������
uniform float utexCoorOffset;//��������ƫ��

out vec4 fragColor;//�������ƬԪ��ɫ

in vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
in vec4 vAmbient;	//���ڴ��ݸ�ƬԪ��ɫ���Ļ���������ǿ��
in vec4 vDiffuse;	//���ڴ��ݸ�ƬԪ��ɫ����ɢ�������ǿ��
in vec4 vSpecular;	//���ڴ��ݸ�ƬԪ��ɫ���ľ��������ǿ��

void main()                         
{  
   //�������������ƫ��      
   vec2 texCoorTemp=fract(vTextureCoord+vec2(utexCoorOffset,utexCoorOffset));         
   //����ƬԪ�������в�������ɫֵ            
   vec4 finalColor=texture(sTexture, texCoorTemp);   
   fragColor =finalColor*vAmbient + finalColor*vDiffuse + finalColor*vSpecular; 
}              