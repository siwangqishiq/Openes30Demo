#version 300 es
precision mediump float;							//����Ĭ�ϵĸ��㾫��
in vec2 vTextureCoord; 						//���մӶ�����ɫ����������������
uniform sampler2D sTextureGrass;					//�����������ݣ���Ƥ��
out vec4 fragColor;
void main(){
   vec4 gColor=texture(sTextureGrass, vTextureCoord); 	//�Ӳ�Ƥ�����в�������ɫ
   fragColor = gColor; //����ƬԪ������ɫֵ    
}
 