#version 300 es
precision mediump float;
uniform sampler2D sTexture;//������������
in vec2 vTextureCoord;//���մӶ�����ɫ�������Ĳ���
out vec4 fFragColor;//�����ƬԪ��ɫ
void main()                         
{           
   //����ƬԪ�������в�������ɫֵ            
   fFragColor = texture(sTexture, vTextureCoord); 
}              