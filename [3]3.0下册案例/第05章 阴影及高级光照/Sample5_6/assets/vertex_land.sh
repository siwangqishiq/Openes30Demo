#version 300 es
//���������ջ��ƵĶ�����ɫ��
uniform mat4 uMVPMatrix; //�ܱ任����
in vec3 aPosition;  //����λ��
in vec3 aNormal;    //���㷨����
out vec3 VaPosition;
out vec3 VaNormal;
void main()     
{
   gl_Position = uMVPMatrix*vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��  
   VaPosition=aPosition;
   VaNormal=aNormal;
}                      