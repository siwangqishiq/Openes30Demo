#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
uniform float maxLifeSpan;//�������������
in vec4 aPosition;  //����Ⱦ���߽��յĶ���λ������
in vec2 aTexCoor;    //����Ⱦ���߽��յ���������
out vec2 vTextureCoord;  //���ڴ��ݸ�ƬԪ��ɫ������������

out vec4 vPosition;//���ڴ��ݸ�ƬԪ��ɫ���Ķ���λ������
out float sjFactor;//���ڴ��ݸ�ƬԪ��ɫ������˥������
void main()     
{ //������                           		
   gl_Position = uMVPMatrix * vec4(aPosition.x,aPosition.y,0.0,1); //�����ܱ任�������˴λ��ƴ˶���λ��
   vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
   vPosition=vec4(aPosition.x,aPosition.y,0.0,aPosition.w);//���㶥��λ�����ԣ������䴫�ݸ�ƬԪ��ɫ��
   sjFactor=(maxLifeSpan-aPosition.w)/maxLifeSpan;//������˥�����ӣ������䴫�ݸ�ƬԪ��ɫ��
}                      