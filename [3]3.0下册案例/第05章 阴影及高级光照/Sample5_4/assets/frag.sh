#version 300 es
precision mediump float;	//����Ĭ�Ͼ���
uniform highp int isShadow;	//�Ƿ�Ϊ��Ӱ���Ƶı�־	
uniform sampler2D sTexture;	//��Ӱ��ͼ������������
in vec4 ambient;			//���մӶ�����ɫ�����ݹ����Ļ��������
in vec4 diffuse;				//���մӶ�����ɫ�����ݹ�����ɢ������
in vec4 specular;			//���մӶ�����ɫ�����ݹ����ľ�������
in vec4 vPosition;  			//���մӶ�����ɫ�����ݹ�����ƬԪλ��
uniform highp mat4 uMVPMatrixGY; //��Դλ�ô�����������۲켰ͶӰ��Ͼ��� 
out vec4 fragColor;
void main(){
  if(isShadow==0)
  {   
   //��ƬԪ��λ��ͶӰ����Դ������������Ľ�ƽ����
   vec4 gytyPosition=uMVPMatrixGY * vec4(vPosition.xyz,1);
   gytyPosition=gytyPosition/gytyPosition.w;	//����͸�ӳ���   
   float s=gytyPosition.s+0.5;					//��ͶӰ������껻��Ϊ��������
   float t=gytyPosition.t+0.5;    
   vec4 finalColor=vec4(0.8,0.8,0.8,1.0); 		   //���屾�����ɫ
   	//������������ƬԪ��ɫ����ʱ�����⡢ɢ��⡢���������ͨ������
   vec4 colorA=finalColor*ambient+finalColor*specular+finalColor*diffuse;
   //��������Ӱ�е�ƬԪ��ɫ����ʱ���л����⡢ɢ��⣬����ɢ������Ϊԭ����30%
   vec4 colorB=finalColor*ambient+finalColor*diffuse*0.3;
   if(s>=0.0&&s<=1.0&&t>=0.0&&t<=1.0)
   {  //�����������ںϷ���Χ������ͶӰ��ͼ
	  vec4 projColor=texture(sTexture, vec2(s,t)); 					//��ͶӰ����ͼ���в���
        float a=step(0.9999,projColor.r);								//���r<0.9999����a=0������a=1
        float b=step(0.0001,projColor.r); 								//���r<0.0001����a=b������b=1
        float c=1.0-sign(a);  											//���a>0,��c=1.���a=0����c=0            	
		fragColor =a*colorA+(1.0-b)*colorB+b*c*mix(					//��������ƬԪ��ɫ
		colorB,colorA,smoothstep(0.0,1.0,projColor.r));
   } 
   else{
       //��������ƬԪ��ɫ
       fragColor= colorB; 
       } 
   }
   else{
   	   //Ӱ�ӵ���ɫ
       fragColor = vec4(0.1,0.1,0.1,0.0);//ƬԪ������ɫΪ��Ӱ����ɫ  
       }
}       


