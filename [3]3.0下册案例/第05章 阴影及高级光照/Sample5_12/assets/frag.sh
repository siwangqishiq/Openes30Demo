#version 300 es
precision highp float;
uniform sampler2D sTexture;//������������
//���մӶ�����ɫ�������Ĳ���
in vec4 ambient;
in vec4 diffuse;
in vec4 specular;
in vec2 vTextureCoord;
layout(location=0) out vec4 fragColor0;
layout(location=1) out float fragColor1;
void main()
{
   //�����������ɫ����ƬԪ
   vec4 finalColor=texture(sTexture, vTextureCoord);
   //����ƬԪ��ɫֵ
   fragColor0 = finalColor*ambient+finalColor*specular+finalColor*diffuse;
   fragColor1 = gl_FragCoord.z;
}