#version 300 es
uniform mat4 uMVPMatrix; 							//�ܱ任����
uniform mat4 uMMatrix; 								//�����任����
in vec3 aPosition;  									//����λ��
in vec2 aTexCoor;   								//������������
in vec3 aNormal;   									//������
out vec3 fNormal;    								//���ڴ��ݸ�ƬԪ��ɫ���ķ�����
out vec2 vTextureCoord;  							//���ڴ��ݸ�ƬԪ��ɫ������������
out vec4 vPosition;						//���ڴ��ݸ�ƬԪ��ɫ���Ļ����任��Ķ���λ��
out vec3 mvPosition;  								//���ڴ��ݸ�ƬԪ��ɫ���Ķ���λ��
out mat4 vMMatrix;						//���ڴ��ݸ�ƬԪ��ɫ���Ļ����任����
void main(){
	gl_Position = uMVPMatrix * vec4(aPosition,1); 		//�����ܱ任�������˴λ��ƴ˶���λ��
	vPosition=uMMatrix*vec4(aPosition,1);		//������任��Ķ���λ�ò����ݸ�ƬԪ��ɫ��
	vTextureCoord=aTexCoor;					//��������������괫�ݸ�ƬԪ��ɫ��
   	mvPosition=aPosition;						//���������괫�ݸ�ƬԪ��ɫ��
   	fNormal=aNormal;  							//������ķ���������ƬԪ��ɫ��
   	vMMatrix=uMMatrix;							//�������任���󴫸�ƬԪ��ɫ��
}
                 