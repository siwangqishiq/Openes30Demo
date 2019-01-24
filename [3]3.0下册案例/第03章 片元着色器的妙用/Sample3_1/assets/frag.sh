#version 300 es
precision mediump float;
in vec2 mcLongLat;//���մӶ�����ɫ�������Ĳ���
out vec4 fFragColor;//�����ƬԪ��ɫ
void main()                         
{                       
   vec3 bColor=vec3(0.678,0.231,0.129);//ש�����ɫ
   vec3 mColor=vec3(0.763,0.657,0.614);//ˮ�����ɫ
   vec3 color;//ƬԪ��������ɫ
   
   //���㵱ǰλ����������ż����
   int row=int(mod((mcLongLat.y+90.0)/12.0,2.0));
   //���㵱ǰƬԪ�Ƿ��ڴ�������1�еĸ�������
   float ny=mod(mcLongLat.y+90.0,12.0);
   //ÿ�е�ש��ƫ��ֵ��������ƫ�ư��ש��
   float oeoffset=0.0;
   //��ǰƬԪ�Ƿ��ڴ�������3�еĸ�������
   float nx;
   
   if(ny>10.0)
   {//λ�ڴ��е�����1��
     color=mColor;//����ˮ��ɫ��ɫ
   }
   else
   {//��λ�ڴ��е�����1��
     if(row==1)
     {//��Ϊ��������ƫ�ư��ש��
        oeoffset=11.0;
     }
     //���㵱ǰƬԪ�Ƿ��ڴ�������3�еĸ�������
     nx=mod(mcLongLat.x+oeoffset,22.0);
     if(nx>20.0)
     {//��λ�ڴ��е�����3��
        color=mColor;
     }
     else
     {//λ�ڴ��е�����3��
        color=bColor;//����ש��ɫ��ɫ
     }
   } 
   //��ƬԪ��������ɫ���ݽ���Ⱦ����
   fFragColor=vec4(color,0);
}     