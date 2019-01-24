#version 300 es
precision mediump float;//����Ĭ�ϸ��㾫��
uniform vec4 startColor;//��ʼ��ɫ
uniform vec4 endColor;//��ֹ��ɫ

uniform float bj;//������ΰ뾶
uniform sampler2D sTexture;//������������

in vec4 vPosition;//���մӶ�����ɫ����������ƬԪλ������
in float sjFactor;//���մӶ�����ɫ������������˥������
out vec4 fragColor; //�������ƬԪ��ɫ
void main()                         
{//������
	if(vPosition.w==10.0)
	{//��ƬԪ��������Ϊ10.0ʱ������δ����״̬��������
		fragColor=vec4(0.0,0.0,0.0,0.0);//������ƬԪ
	}else
	{//��ƬԪ�������ڲ�Ϊ10.0ʱ�����ڻ�Ծ״̬������
		vec2 texCoor=gl_PointCoord;   	//���ڽ�������ȡ��������
		vec4 colorTL = texture(sTexture, texCoor); //�����������
    	vec4 colorT;//��ɫ����
    	float disT=distance(vPosition.xyz,vec3(0.0,0.0,0.0));//���㵱ǰƬԪ�����ĵ�ľ���
    	float tampFactor=(1.0-disT/bj)*sjFactor;//����ƬԪ��ɫ��ֵ����
    	vec4 factor4=vec4(tampFactor,tampFactor,tampFactor,tampFactor);
    	colorT=clamp(factor4,endColor,startColor);//������ɫ��ֵ
    	colorT=colorT*colorTL.a;  //��ϲ�������͸���ȼ���������ɫ
    	fragColor=colorT;//�����������ƬԪ��ɫ������Ⱦ����
	}    
}              