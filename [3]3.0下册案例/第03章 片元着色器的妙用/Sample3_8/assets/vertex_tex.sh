#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
in vec3 aPosition;  //����λ��
in vec2 aTexCoor;    //������������
out vec2 vTexPosition; //ת���󴫵ݸ�ƬԪ��ɫ������������
void main() {                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��
   vTexPosition = (aTexCoor-0.5)*5.0;//����������ת���󴫵ݸ�ƬԪ��ɫ��
}