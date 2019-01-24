#version 300 es
precision mediump float;					//����Ĭ�ϵĸ��㾫��
in vec2 vTextureCoord;					//���մӶ�����ɫ������������������
in vec3 vPosition;						//���մӶ�����ɫ���������Ķ���λ��
in vec4 vAmbient;						//���մӶ�����ɫ���������Ļ��������
in vec4 vDiffuse;						//���մӶ�����ɫ����������ɢ������
in vec4 vSpecular;						//���մӶ�����ɫ���������ľ��淴������
uniform sampler2D ssTexture;			//������������(ɰʯ)
uniform sampler2D lcpTexture;				//������������(�̲�Ƥ)
uniform sampler2D dlTexture;				//������������(��·)
uniform sampler2D hcpTexture;				//������������(�Ʋ�Ƥ)
uniform sampler2D rgbTexture;			//������������(��������RGB��ͼ)
uniform float repeatVaule;						//�����������repeatֵ
out vec4 fragColor;							//�����ƬԪ����ɫ
void main(){
   vec4 vColor=texture(rgbTexture,		//�ӵ�������RGB��ͼ�в�������ɫ
		vec2(vTextureCoord.x/repeatVaule,vTextureCoord.y/repeatVaule));
   float rFactor=vColor.r;								//��Ƥ�ļ�Ȩ����
   float gFactor=vColor.g;								//ɽʯ�ļ�Ȩ����
   float bFactor=vColor.b;								//��·�ļ�Ȩ����
   float aFactor=max(0.0,1.0-(rFactor+gFactor+bFactor));		//�����ļ�Ȩ����
   vec4 rColor=texture(ssTexture,vTextureCoord);		//�Ӳ�Ƥ�����в�������ɫ
   vec4 gColor=texture(lcpTexture,vTextureCoord);	//��ɽʯ�����в�������ɫ
   vec4 bColor=texture(dlTexture,vTextureCoord);		//�ӵ�·�����в�������ɫ
   vec4 aColor=texture(hcpTexture,vTextureCoord);		//�����������в�������ɫ
   //����ƬԪ��Ȩ��ɫ
   vec4 finalColor=rColor*rFactor+gColor*gFactor+bColor*bFactor+aColor*aFactor;
   //���ݸ�������ͨ����ǿ�ȼ�������ƬԪ��ɫ
   fragColor = finalColor*vAmbient + finalColor*vDiffuse + finalColor*vSpecular;
}
