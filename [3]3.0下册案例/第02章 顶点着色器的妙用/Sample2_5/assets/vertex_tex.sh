#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
in vec3 aPosition;  //����λ��
in vec2 aTexCoor;    //������������
out vec2 vTextureCoord;  //���ڴ��ݸ�ƬԪ��ɫ���ı���
uniform float ratio;//��ǰ����Ť���Ƕ�����
void main()
{       
	float pi = 3.1415926; //Բ����
	float centerX=0.0;//���ĵ��X����
	float centerY=-5.0;//���ĵ��Y����
	float currX = aPosition.x;//��ǰ���x����
	float currY = aPosition.y;//��ǰ���y����
	float spanX = currX - centerX;//��ǰxƫ����
	float spanY = currY - centerY;//��ǰyƫ����
	float currRadius = sqrt(spanX * spanX + spanY * spanY);//�������
	float currRadians;//��ǰ����x��������ļн�
	if(spanX != 0.0)
	{//һ�����
		currRadians = atan(spanY , spanX);
	}
	else
	{
		currRadians = spanY > 0.0 ? pi/2.0 : 3.0*pi/2.0; 
	}
	float resultRadians = currRadians + ratio*currRadius;//�����Ť����ĽǶ�
	float resultX = centerX + currRadius * cos(resultRadians);//���������x����
	float resultY = centerY + currRadius * sin(resultRadians);//���������y����
	//�������㣬�������ܱ任�������˴λ��ƴ˶����λ��
    gl_Position = uMVPMatrix * vec4(resultX,resultY,0.0,1); 
    vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
}      
                     