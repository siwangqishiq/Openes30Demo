#version 300 es
precision mediump float;
//varying  vec4 vColor; //���մӶ�����ɫ�������Ĳ���
in vec2 vTextureCoord;//���մӶ�����ɫ�������Ĳ���
uniform sampler2D sTexture;//������������
out vec4 fragColor;//�����ƬԪ��ɫ
void main()                         
{          
	//vec4 finalColor = vColor;      
	vec4 finalColor =  texture(sTexture, vTextureCoord);    
	fragColor = finalColor;
}