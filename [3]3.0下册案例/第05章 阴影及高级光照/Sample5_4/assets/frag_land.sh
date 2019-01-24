#version 300 es
precision mediump float;	
uniform sampler2D sTexture;		//������������
in vec4 ambient;			//���մӶ�����ɫ�����ݹ����Ļ��������
in vec4 diffuse;				//���մӶ�����ɫ�����ݹ�����ɢ������
in vec4 specular;			//���մӶ�����ɫ�����ݹ����ľ�������
in vec4 vPosition;  			//���մӶ�����ɫ�����ݹ�����ƬԪλ��
uniform highp mat4 uMVPMatrixGY; //��Դλ�ô�����������۲켰ͶӰ��Ͼ��� 
out vec4 fragColor;
void main()
{
   //��ƬԪ��λ��ͶӰ����Դ������������Ľ�ƽ����
   vec4 gytyPosition=uMVPMatrixGY * vec4(vPosition.xyz,1);
   gytyPosition=gytyPosition/gytyPosition.w;	//����͸�ӳ���   
   float s=gytyPosition.s+0.5;				//��ͶӰ������껻��Ϊ��������
   float t=gytyPosition.t+0.5;    
   vec4 finalcolor=vec4(0.8,0.8,0.8,1.0); 		   //���屾�����ɫ
   vec4 colorA=finalcolor*ambient+finalcolor*specular+finalcolor*diffuse;//��������ɫ
   vec4 colorB=vec4(0.1,0.1,0.1,0.0);//��Ӱ�µ���ɫ
   if(s>=0.0&&s<=1.0&&t>=0.0&&t<=1.0)
   {	   
        //�����������ںϷ���Χ������ͶӰ��ͼ  
		vec4 projColor=texture(sTexture, vec2(s,t)); //��ͶӰ����ͼ���в���
		float a=step(0.9999,projColor.r);								//���r<0.9999����a=0������a=1
        float b=step(0.0001,projColor.r); 								//���r<0.0001����a=b������b=1
        float c=1.0-sign(a);  											//���a>0,��c=1.���a=0����c=0            	
		fragColor =a*colorA+(1.0-b)*colorB+b*c*mix(					//��������ƬԪ��ɫ
		colorB,colorA,smoothstep(0.0,1.0,projColor.r));
   }
   else
   {
         fragColor = colorA;
   }
}     
