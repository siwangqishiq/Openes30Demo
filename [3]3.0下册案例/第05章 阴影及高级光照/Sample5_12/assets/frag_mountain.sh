#version 300 es
precision mediump float;							//����Ĭ�ϵĸ��㾫��
in vec2 vTextureCoord; 						//���մӶ�����ɫ����������������
uniform sampler2D sTextureGrass;					//������������(��Ƥ)
layout(location=0) out vec4 fragColor0;
layout(location=1) out float fragColor1;
void main(){
   vec4 gColor=texture(sTextureGrass, vTextureCoord); 	//�Ӳ�Ƥ�����в�������ɫ
   fragColor0 = gColor; //����ƬԪ������ɫֵ    
   fragColor1=gl_FragCoord.z;
}