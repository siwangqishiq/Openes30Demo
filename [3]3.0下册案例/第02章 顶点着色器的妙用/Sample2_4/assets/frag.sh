#version 300 es
precision mediump float;
uniform sampler2D sTexture;//������������
//���մӶ�����ɫ�������Ĳ���
in vec2 vTextureCoord;
out vec4 fragColor;//�������ƬԪ��ɫ
void main()                         
{    
   //����ƬԪ��ɫֵ
   fragColor = texture(sTexture, vTextureCoord); 

}   