#version 300 es
precision mediump float;
uniform samplerCube sTexture;//������������
in vec3 eyeVary;		//���մӶ�����ɫ����������������
in vec3 newNormalVary;	//���մӶ�����ɫ�������ı任������
out vec4 fragColor;//�������ƬԪ��ɫ
vec4 zs(					//���ݷ�����������������˹�������ɼ�������ͼ��������ķ���
  in float zsl				//����ϵ��
){  
  vec3 vTextureCoord=refract(-eyeVary,newNormalVary,zsl);//����˹�������ɼ�����������������
  vec4 finalColor=texture(sTexture, vTextureCoord);     
  return finalColor;
}
void main(){//������
   fragColor=zs(0.94); //������ϵ��0.94����zs�������ƬԪ��ɫ�ļ���
}    
