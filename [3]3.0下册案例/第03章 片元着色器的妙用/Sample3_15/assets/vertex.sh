#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
uniform float maxLifeSpan;//�������������

uniform mediump float bj; //�������ӵİ뾶
uniform vec3 cameraPosition;//�����λ��
uniform mat4 uMMatrix;//�����任�����ܾ���

in vec4 aPosition;  //����Ⱦ���߽��յĶ���λ������
out vec4 vPosition; //���ڴ��ݸ�ƬԪ��ɫ���Ķ���λ������ 
out float sjFactor;//���ڴ��ݸ�ƬԪ��ɫ������˥������

void main()     
{//������          
   //����任�󶥵�����������ϵ�е�λ��
   vec4 currPosition=uMMatrix * vec4(aPosition.xy,0.0,1);
   //������㵽������ľ���
   float d=distance(currPosition.xyz,cameraPosition);
   //���������������S��ƽ����֮1
   float s=1.0/sqrt(0.01+0.05*d+0.001*d*d);
   
   gl_PointSize=bj*s;//���õ㾫���Ӧ��ĳߴ�
   
   gl_Position = uMVPMatrix * vec4(aPosition.xy,0.0,1); //�����ܱ任�������˴λ��ƴ˶���λ��
   vPosition=vec4(aPosition.xy,0.0,aPosition.w); //���㶥��λ�����ԣ������䴫�ݸ�ƬԪ��ɫ��        
   sjFactor=(maxLifeSpan-aPosition.w)/maxLifeSpan;//������˥�����ӣ������䴫�ݸ�ƬԪ��ɫ�� 		
}                      