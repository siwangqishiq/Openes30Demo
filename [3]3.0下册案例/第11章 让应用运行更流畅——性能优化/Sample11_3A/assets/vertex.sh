#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
uniform sampler2D jbTexture;//��ɫ����������������
in vec3 aPosition;  //����λ��
in vec2 aTexCoor;    //������������
out vec4 changeTexture;//�ı�С����ɫ����������
uniform float jbS;//��Ӧ����ɫ���������в�����Sֵ
uniform float jbT;//��Ӧ����ɫ���������в�����Tֵ
//���ڴ��ݸ�ƬԪ��ɫ���ı���
out vec2 vTextureCoord; 
void main()     
{ 
   gl_Position = uMVPMatrix * vec4(aPosition,1); //�����ܱ任�������˴λ��ƴ˶���λ��  
   vec4 jbTex=texture(jbTexture,vec2(jbS,jbT));//�����ɫ�����������������
   changeTexture=jbTex;//������������������1���ݴ��ݸ�ƬԪ��ɫ��
   vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
}                      