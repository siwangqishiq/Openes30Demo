#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
in vec3 aPosition;  //����λ��
//attribute vec4 aColor;    //������ɫ
in vec2 aTexCoor;    //������������

//varying  vec4 vColor;  //���ڴ��ݸ�ƬԪ��ɫ���ı���
out vec2 vTextureCoord;	//������ɫ������������

void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��
   //vColor = aColor;//�����յ���ɫ���ݸ�ƬԪ��ɫ�� 
   vTextureCoord=aTexCoor;  
}                      