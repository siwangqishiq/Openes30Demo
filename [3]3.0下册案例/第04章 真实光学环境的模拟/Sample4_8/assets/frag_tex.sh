#version 300 es
precision mediump float;
in vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������

out vec4 fragColor;
void main()                         
{           
   //����ƬԪ�������в�������ɫֵ    
   vec4 color=texture(sTexture, vTextureCoord);
   color.a=0.0;
   fragColor=color;  
}              