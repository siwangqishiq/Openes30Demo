#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
uniform mat4 uMMatrix; //�任����
uniform vec3 uCamera;	//�����λ��
in vec3 aPosition;  //����λ��
in vec2 aTexCoor;    //������������
out vec2 vTextureCoord;
void main()     
{                            		
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��
      //��������������괫��ƬԪ��ɫ��
   vTextureCoord=aTexCoor;  
}                 