#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
in vec3 aPosition;  //����λ��
in vec2 aTexCoor;    //������������
out vec2 vTextureCoord;  //���ڴ��ݸ�ƬԪ��ɫ������������
uniform float angleSpan;//��֡Ť���ܽǶ�
uniform float yStart;//Y������ʼ��
uniform float ySpan;//Y�����ܿ��
void main()     
{
   float tempAS= angleSpan*(aPosition.y-yStart)/ySpan;//���㵱ǰ����Ť��(�����ĵ�ѡ��)�ĽǶ�
   vec3 tPosition=aPosition;
  
   if(aPosition.y>yStart)
   {//������������һ��Ķ��������Ť�����X��Z����
     tPosition.x=(cos(tempAS)*aPosition.x-sin(tempAS)*aPosition.z);
     tPosition.z=(sin(tempAS)*aPosition.x+cos(tempAS)*aPosition.z);
   }
   gl_Position = uMVPMatrix * vec4(tPosition,1); //�����ܱ任�������˴λ��ƴ˶����λ��
   
   vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
}                      