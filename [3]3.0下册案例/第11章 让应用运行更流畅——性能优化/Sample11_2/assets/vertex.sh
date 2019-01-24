#version 300 es
uniform mat4 uVPMatrix; //�����͸�Ӿ���
uniform mat4 uMMatrix; //�����任����
in vec3 aPosition;  //����λ��
in vec2 aTexCoor;    //������������
out vec2 vTextureCoord;  //���ڴ��ݸ�ƬԪ��ɫ���ı���
void main()     
{              
   //ÿ�ж��ٸ�
   const int colCount=3;      
   //��λƽ����
   const float unitSize=4.0;
   //�����к�
   int col=gl_InstanceID%colCount;
   //�����к�
   int row=gl_InstanceID/colCount;
   //����ƽ�ƾ���
   mat4 pyMatrix=mat4
   (
   		1,0,0,0, 
   		0,1,0,0, 
   		0,0,1,0, 
   		unitSize*float(col)-unitSize,unitSize*float(row)-unitSize,0,1
   );
   //�����ܱ任����
   mat4 MVPMatrix=uVPMatrix*uMMatrix*pyMatrix;
   //�����ܱ任�������˴λ��ƴ˶���λ��
   gl_Position = MVPMatrix * vec4(aPosition,1.0); 
   vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
}