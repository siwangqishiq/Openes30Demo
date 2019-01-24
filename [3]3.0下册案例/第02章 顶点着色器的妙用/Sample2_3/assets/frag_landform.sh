#version 300 es
precision mediump float;
in vec2 vTextureCoord; //���������������
in float vertexHeight;//���ܶ���ĸ߶�ֵ
uniform sampler2D sTextureSand;//������������   ----ɳ̲
uniform sampler2D sTextureGrass;//������������-----�ݵ�
out vec4 fragColor;//�������ƬԪ��ɫ
void main()                         
{       
	float height1=15.0;  
	float height2=25.0;    
   vec4 finalSand=texture(sTextureSand, vTextureCoord);//ɳ̲
   vec4 finalGrass=texture(sTextureGrass, vTextureCoord);   //�ݵ�
   if(vertexHeight<height1)//����ɳ̲
   {
  	  fragColor = finalSand;
   }
   else if(vertexHeight<height2)//���Ʋݵغ�ɳ̲��ϲ�
   {
      float ratio=(vertexHeight-height1)/(height2-height1);	
      finalSand *=(1.0-ratio); 
   	  finalGrass *=ratio;
      fragColor =finalGrass+ finalSand;
   }
   else//���Ʋݵ�
   {
      fragColor = finalGrass;
   }
}              