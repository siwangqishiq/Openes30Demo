#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
uniform float uStartAngle;//��֡��ʼ�Ƕ�(������ඥ��Ķ�Ӧ�Ƕ�)
uniform float uWidthSpan;//���򳤶��ܿ��
in vec3 aPosition;  //����λ��
in vec2 aTexCoor;    //������������
out vec2 vTextureCoord;  //���ڴ��ݸ�ƬԪ��ɫ������������
void main()     
{            
   //����X����                		
   float angleSpanH=4.0*3.14159265;//����Ƕ��ܿ�ȣ����ڽ���X������ǶȵĻ���
   float startX=-uWidthSpan/2.0;//��ʼX����(������ඥ���X����)
   //���ݺ���Ƕ��ܿ�ȡ����򳤶��ܿ�ȼ���ǰ��X�����������ǰ����X�����Ӧ�ĽǶ�
   float currAngle=uStartAngle+((aPosition.x-startX)/uWidthSpan)*angleSpanH;
   float tz=sin(currAngle)*0.1;//ͨ�����Һ��������ǰ���Z����      
   
   //�����ܱ任�������˴λ��ƴ˶����λ��
   gl_Position = uMVPMatrix * vec4(aPosition.x,aPosition.y,tz,1); 
   vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
}                      