#version 300 es
precision mediump float;
uniform vec3 uColor;//������ɫ
out vec4 fragColor;//�����ƬԪ��ɫ
void main()                         
{
  //����ƬԪ��ɫֵ 
  fragColor = vec4(uColor,1.0);
}              