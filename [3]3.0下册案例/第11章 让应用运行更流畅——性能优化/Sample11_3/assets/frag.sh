#version 300 es
precision mediump float;
uniform sampler2D ssTexture;//������������
in vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
in vec4 changeTexture;
out vec4 fragColor;

void main()
{
   //����ƬԪ�������в�������ɫֵ
   vec4 finalColor= texture(ssTexture, vTextureCoord);
   fragColor =finalColor*changeTexture;
}