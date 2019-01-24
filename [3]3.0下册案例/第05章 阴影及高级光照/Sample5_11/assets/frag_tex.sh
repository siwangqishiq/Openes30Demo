#version 300 es
precision mediump float;
in vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTextureOne;//������������
uniform sampler2D sTextureTwo;//������������
uniform sampler2D sTextureThree;//������������
uniform sampler2D sTextureFour;//������������
uniform sampler2D sTextureFive;//������������
out vec4 fragColor;
void main()
{
   vec4 finalColorOne= texture(sTextureOne, vTextureCoord);
   vec4 finalColorTwo= texture(sTextureTwo, vTextureCoord);
   vec4 finalColorThree= texture(sTextureThree, vTextureCoord);
   vec4 finalColorFour= texture(sTextureFour, vTextureCoord);
   vec4 finalColorFive= texture(sTextureFive, vTextureCoord);
   //����ƬԪ�������в�������ɫֵ
   fragColor = 0.6f*finalColorOne+0.1f*finalColorTwo+0.1f*finalColorThree
   +0.1f*finalColorFour+0.1f*finalColorFive;
}