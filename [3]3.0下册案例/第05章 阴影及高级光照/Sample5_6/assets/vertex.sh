#version 300 es
//���������ջ��ƵĶ�����ɫ��
uniform mat4 uMMatrix; //�任����
uniform vec3 uLightLocation;	//��Դλ��
uniform mat4 uMProjCameraMatrix; //ͶӰ���������Ͼ���
in vec3 aPosition;  //����λ��

void main()     
{ 
      //���Ʊ�Ӱ��������Ӱ����λ��
      vec3 A=vec3(0.0,0.1,0.0);//ͶӰƽ������һ������
      vec3 n=vec3(0.0,1.0,0.0);//ͶӰƽ�淨����
      vec3 S=uLightLocation; //��Դλ��     
      vec3 V=(uMMatrix*vec4(aPosition,1)).xyz;  //����ƽ�ƺ���ת�任��ĵ������    
      vec3 VL=S+(V-S)*(dot(n,(A-S))/dot(n,(V-S)));//��õ�ͶӰ������
      gl_Position = uMProjCameraMatrix*vec4(VL,1); //�����ܱ任�������˴λ��ƴ˶���λ��   
}                      