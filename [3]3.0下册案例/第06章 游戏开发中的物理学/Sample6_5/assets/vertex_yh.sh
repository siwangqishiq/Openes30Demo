#version 300 es
uniform mat4 uMVPMatrix; //�ܱ任����
uniform float uPointSize;//��ߴ�
uniform float uTime;//���ӵ��ۼ��˶�ʱ��
in vec3 aVelocity; //���ӳ��ٶ�
void main()     
{
   float currTime=mod(uTime,10.0);//ִ��ȡģ���㣬�൱���ۼ�ʱ�䳬��10���0
   float px=aVelocity.x*currTime;	//�������Ӵ�ʱ��X����
   float py=aVelocity.y*currTime-0.5*1.5*currTime*currTime+3.0;; //�������Ӵ�ʱ��Y����
   float pz=aVelocity.z*currTime;//�������Ӵ�ʱ��Z����
   //�����ܱ任�������˴λ��ƴ˶���λ��                         		
   gl_Position = uMVPMatrix * vec4(px,py,pz,1); 
   //�������ӳߴ�
   gl_PointSize=uPointSize;  
}