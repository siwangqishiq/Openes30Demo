#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
uniform float uStartAngle;//��֡��ʼ�Ƕ�(������ඥ��Ķ�Ӧ�Ƕ�)
uniform float uWidthSpan;//���򳤶��ܿ��
in vec3 aPosition;  //����λ��
in vec2 aTexCoor;    //������������
out vec2 vTextureCoord; //���ڴ��ݸ�ƬԪ��ɫ������������
void main()     
{                  
   //����X��Ƕ�          		
   float angleSpanH=4.0*3.14159265;//����Ƕ��ܿ�ȣ����ڽ���X������ǶȵĻ���
   float startX=-uWidthSpan/2.0;//��ʼX����(������ඥ���X����)
   //���ݺ���Ƕ��ܿ�ȡ����򳤶��ܿ�ȼ���ǰ��X�����������ǰ����X�����Ӧ�ĽǶ�
   float currAngleH=uStartAngle+((aPosition.x-startX)/uWidthSpan)*angleSpanH;
   
   //�������Y��չ��ʼ�Ƕȵ��Ŷ�ֵ
   float angleSpanZ=4.0*3.14159265;//����Ƕ��ܿ�ȣ����ڽ���Y������ǶȵĻ��� 
   float uHeightSpan=0.75*uWidthSpan;//���򳤶��ܿ��
   float startY=-uHeightSpan/2.0;//��ʼY����(�����ϲඥ���Y����)
   //��������Ƕ��ܿ�ȡ����򳤶��ܿ�ȼ���ǰ��Y�����������ǰ����Y�����Ӧ�ĽǶ�
   float currAngleZ=((aPosition.y-startY)/uHeightSpan)*angleSpanZ;
      
   //����б����
   float tzH=sin(currAngleH-currAngleZ)*0.1;//ͨ�����Һ��������ǰ���Z����   
   //�����ܱ任�������˴λ��ƴ˶����λ��
   gl_Position = uMVPMatrix * vec4(aPosition.x,aPosition.y,tzH,1); 
   vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
}                      