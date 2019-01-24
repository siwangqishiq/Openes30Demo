#version 310 es
precision mediump float;//����Ĭ�ϵĸ��㾫��
uniform sampler2D sTexture;//������������
uniform float utexCoorOffset;//��������ƫ��
in vec2 vTextureCoord; //���մӶ�����ɫ����������������
in vec4 vAmbient;	//���մӶ�����ɫ�������Ļ���������ǿ��
in vec4 vDiffuse;	//���մӶ�����ɫ��������ɢ�������ǿ��
in vec4 vSpecular;	//���մӶ�����ɫ�������ľ��������ǿ��
out vec4 fragColor;//�������ƬԪ��ɫ
void main()                         
{//������           
   //�������������ƫ��      
   vec2 texCoorTemp=fract(vTextureCoord+vec2(utexCoorOffset,utexCoorOffset));
   //����ƬԪ�������в�������ɫֵ      
   vec4 finalColor=texture(sTexture, texCoorTemp); 
   //���ݹ�������ͨ����ֵ�����������ɫֵ  
   fragColor =finalColor*vAmbient + finalColor*vDiffuse + finalColor*vSpecular; 
}              