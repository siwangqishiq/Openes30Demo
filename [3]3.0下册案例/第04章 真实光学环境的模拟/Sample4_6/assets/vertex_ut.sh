#version 300 es
uniform mat4 uMVPMatrix; 		//�ܱ任����
in vec3 aPosition;  		//����λ��
in vec2 aTexCoor;    		//������������
in vec3 aNormal;   		//������
in vec3 tNormal;   			//������
out vec2 vTextureCoord;  		//���ڴ��ݸ�ƬԪ��ɫ������������
out vec3 fNormal;    		//���ڴ��ݸ�ƬԪ��ɫ���ķ�����
out vec3 ftNormal;    		//���ڴ��ݸ�ƬԪ��ɫ����������
out vec3 vPosition;  			//���ڴ��ݸ�ƬԪ��ɫ���Ķ���λ��
void main() {     
   gl_Position = uMVPMatrix * vec4(aPosition,1); 	//�����ܱ任�������˴λ��ƴ˶����λ��
   vTextureCoord=aTexCoor;					//��������������괫��ƬԪ��ɫ��
   fNormal=aNormal;   						//������ķ���������ƬԪ��ɫ��
   ftNormal=tNormal; 						//�����������������ƬԪ��ɫ��
   vPosition=aPosition; 						//�������λ�ô���ƬԪ��ɫ��
}   
