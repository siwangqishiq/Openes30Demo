#version 300 es
precision mediump float;//����Ĭ�ϵĸ��㾫��
uniform vec4 color;		//ʵ����ɫRGBAֵ
uniform sampler2D sTexture;//������������
in vec2 vTextureCoord; //���մӶ�����ɫ����������������
out vec4 fragColor;//�����ƬԪ����ɫ
void main()                         
{ //������ 
	vec4 finalColor=texture(sTexture, vTextureCoord);//�����������
	finalColor=finalColor*color;//������ɫ��ʵ����ɫ���-ʵ������ɫ  
   	fragColor =finalColor;//ƬԪ��������ɫ
}              