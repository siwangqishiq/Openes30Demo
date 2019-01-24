#version 300 es
precision mediump float;
uniform samplerCube sTexture;//������������
in vec3 eyeVary;		//���մӶ�����ɫ����������������
in vec3 newNormalVary;	//���մӶ�����ɫ�������ı任������
out vec4 fragColor;//�������ƬԪ��ɫ
vec4 zs(					//���ݷ�����������������˹�������ɼ�������ͼ��������ķ���
  in float zsl				//������
){  
  vec3 vTextureCoord=refract(-eyeVary,newNormalVary,zsl); //����˹�������ɼ�����������������
  vec4 finalColor=texture(sTexture, vTextureCoord);  //��������ͼ�������     
  return finalColor;
}
void main(){
   vec4 finalColor=vec4(0.0,0.0,0.0,0.0);
   //������ɫɢRGB����ɫ��ͨ��������������
   finalColor.r=zs(0.97).r;  //�����ɫͨ���Ĳ������
   finalColor.g=zs(0.955).g;  //������ɫͨ���Ĳ������
   finalColor.b=zs(0.94).b;  //������ɫͨ���Ĳ������
   fragColor=finalColor; //������ƬԪ��ɫ���ݸ�����
}    
