#version 300 es
precision mediump float;
//���մӶ�����ɫ�������Ĳ���
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
uniform vec4 aColor;
out vec4 fragColor;//�������ƬԪ��ɫ
void main()                         
{    
   //�����������ɫ����ƬԪ
   //vec4 finalColor=vec4(0.9,0.9,0.9,1.0);
   fragColor = aColor*ambient+aColor*specular+aColor*diffuse;//����ƬԪ��ɫֵ
}   