#version 300 es
precision mediump float;
uniform sampler2D sTexture;//������������
in vec2 vTextureCoord;//���մӶ�����ɫ��������������������
out vec4 fragColor;//�����ƬԪ��ɫ
void main()                         
{    
   //�����������ɫ����ƬԪ
   vec4 finalColor=texture(sTexture, vTextureCoord);
   //����ƬԪ����ɫ
   fragColor = finalColor;

}