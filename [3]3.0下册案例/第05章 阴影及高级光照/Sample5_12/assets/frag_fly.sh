#version 300 es
precision mediump float;
in vec2 vTextureCoord;//���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������
layout(location=0) out vec4 fragColor0;
layout(location=1) out float fragColor1;
void main()                         
{           
   //����ƬԪ�������в�������ɫֵ            
   fragColor0=texture(sTexture,vTextureCoord); 
   fragColor1=gl_FragCoord.z; 
}  