#version 300 es
precision mediump float;	//����Ĭ�Ͼ���
uniform sampler2D sTexture;	//������������
in vec4 ambient;		//���մӶ�����ɫ�������Ļ���������ǿ��
in vec4 diffuse;		//���մӶ�����ɫ��������ɢ�������ǿ��
in vec4 specular;		//���մӶ�����ɫ�������ľ��������ǿ��
in vec2 vTextureCoord;	//���մӶ�����ɫ����������������
out vec4 fragColor;

void main()                         
{    
   //�����������ɫ����ƬԪ
   vec4 finalColor=texture(sTexture, vTextureCoord);    
   //����ƬԪ��������ɫֵ
   fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;

}   