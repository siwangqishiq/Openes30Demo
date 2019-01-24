#version 300 es
precision mediump float;
in vec2 vTextureCoord;//�Ӷ�����ɫ�����ݹ�������������
uniform sampler2D sTexture1;//������������1(������Ƭ)
uniform sampler2D sTexture2;//������������2(��ѧ����Ƭ)
uniform float uT;//��ϱ�������
out vec4 fFragColor;//�����ƬԪ��ɫ
void main() {           
    vec4 color1 = texture(sTexture1, vTextureCoord); 	 //������1�в�������ɫֵ1 
    vec4 color2 = texture(sTexture2, vTextureCoord); 	//������2�в�������ɫֵ2
    fFragColor = color1*(1.0-uT) + color2*uT;//���������������ɫֵ
}              