#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
in vec3 aPosition;  //����λ��
in vec2 aLongLat;   //���㾭γ��
out vec2 mcLongLat;//���ڴ��ݸ�ƬԪ��ɫ���Ķ��㾭γ��
void main()     
{                   
   //�����ܱ任�������˴λ��ƴ˶���λ��         		
   gl_Position = uMVPMatrix * vec4(aPosition,1); 
   //������ľ�γ�ȴ���ƬԪ��ɫ��
   mcLongLat=aLongLat;
}                      