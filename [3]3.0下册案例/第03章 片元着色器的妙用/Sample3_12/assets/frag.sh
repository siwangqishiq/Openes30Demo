#version 300 es
precision mediump float;
uniform sampler3D sTexture;//3D������������
//���մӶ�����ɫ�������Ĳ���
in vec4 ambient;//���մӶ�����ɫ�������Ļ���������ǿ��
in vec4 diffuse;//���մӶ�����ɫ��������ɢ�������ǿ��
in vec4 specular;//���մӶ�����ɫ�������ľ��������ǿ��
in vec3 vPosition;//���մӶ�����ɫ��������λ������  
out vec4 fragColor;//���������ƬԪ��ɫ
void main()                         
{  
   //ǳɫľ����ɫ
   const vec4 lightWood=vec4(0.6,0.3,0.1,1.0);
   //��ɫľ����ɫ
   const vec4 darkWood=vec4(0.4,0.2,0.07,1.0);

   //����ƬԪ��λ�������3D��������--3D�������������
   vec3 texCoor=vec3(((vPosition.x/0.52)+1.0)/2.0,vPosition.y/0.4,((vPosition.z/0.52)+1.0)/2.0);
   //����3D�������
   vec4 noiseVec=texture(sTexture,texCoor); 
   
   //������3D���������������ֵӰ���λ��
   vec3 location=vPosition+noiseVec.rgb*0.05;
   //������ƽ�����ĵ�ľ���
   float dis=distance(location.xz,vec2(0,0)); 
   //���ֵ�Ƶ�� 
   dis *=2.0;//����Բ���Ķ���
   //��������ľ����ɫ�Ļ������
   float r=fract(dis+noiseVec.r+noiseVec.g*0.5+noiseVec.b*0.25+noiseVec.a*0.125)*2.0;
   if(r>1.0)
   {
      r=2.0-r;
   }
   //��������ľ����ɫ�Ļ��
   vec4 color=mix(lightWood,darkWood,r);
   
   //�ٴμ����������
   r=fract((location.y+location.x+location.z)*25.0+0.5);
   noiseVec.a*=r;//�޸�����ֵ
   //���ݵ������ӵ�����ɫ
   if(r<0.5)
   {
   	  color+=lightWood*1.0*noiseVec.a; 
   }
   else
   {
      color-=lightWood*0.02*noiseVec.a;  
   }
   //����ƬԪ��ɫֵ
   fragColor =color*ambient+color*specular+color*diffuse;
}   