#version 300 es
precision mediump float;
uniform sampler2D sTexture;//������������
//���մӶ�����ɫ�������Ĳ���
in vec2 vTextureCoord;
in vec4 changeTexture;
out vec4 fragColor;

void main()
{
   //����ƬԪ�������в�������ɫֵ
   vec4 finalColor= texture(sTexture, vTextureCoord);
   
   fragColor =finalColor*changeTexture;
}   