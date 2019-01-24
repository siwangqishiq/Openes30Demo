#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
in vec3 aPosition;  //����λ��(����1�Źؼ�֡)
in vec3 bPosition;  //����λ��(����2�Źؼ�֡)
in vec3 cPosition;  //����λ��(����3�Źؼ�֡)
in vec2 aTexCoor;    //������������
uniform float uBfb;//�ںϱ���
out vec2 vTextureCoord; //���ڴ��ݸ�ƬԪ��ɫ������������ 

void main()     
{ 
	vec3 tv;   //�ںϺ�Ľ������      		
   	if(uBfb<=1.0)//���ںϱ���С�ڵ���1������Ҫִ�е���1��2�Źؼ�֡���ں�
   	{
   		tv=mix(aPosition,bPosition,uBfb);
   	}
   	else//���ںϱ�������1������Ҫִ�е���2��3�Źؼ�֡���ں�
   	{
   		tv=mix(bPosition,cPosition,uBfb-1.0);
   	}
   	gl_Position = uMVPMatrix * vec4(tv,1);;	//�����ܱ任�������˴λ��ƴ˶����λ��
   	vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
}                      