#version 300 es
#extension GL_OES_EGL_image_external : require
precision mediump float;
in vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
uniform samplerExternalOES sTexture;//������������
out vec4 fragColor;
void main()                         
{
   //����ƬԪ�������в�������ɫֵ            
   fragColor = texture(sTexture, vTextureCoord); 
}              