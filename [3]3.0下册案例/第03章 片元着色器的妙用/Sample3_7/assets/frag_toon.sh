#version 300 es
precision mediump float;						//����Ĭ�ϵĸ��㾫��
uniform sampler2D uImageUnit;				//������������
uniform float uMagTol;						//�趨����ֵ���ж���ǰ���Ƿ�Ϊ��Ե��
uniform float uQuantize;						//��ֵ��������
in vec2 vST;								//���մӶ�����ɫ�������Ĳ���
out vec4 fFragColor;							//�������ƬԪ��ɫ
void main()
{
	ivec2 ires=textureSize(uImageUnit,0);		//�������ͼ�Ŀ�߶�
	float uResS=float(ires.s);					//�������ͼ��Sֵ
	float uResT=float(ires.t);					//�������ͼ��Tֵ
	vec3 rgb=texture(uImageUnit,vST).rgb;		//������������rgbֵ
	vec2 stp0=vec2(1.0/uResS,0.0);			//�������������ؼ�ľ�������
	vec2 st0p=vec2(0.0,1.0/uResT);			//�������������ؼ�ľ�������
	vec2 stpp=vec2(1.0/uResS,1.0/uResT);		//�����¡������������ؼ�ľ�������
	vec2 stpm=vec2(1.0/uResS,-1.0/uResT);		//�����ϡ������������ؼ�ľ�������
	const vec3 W=vec3(0.2125,0.7154,0.0721);	//��ɫ
	
	//��ǰ�������¡����¡����ϡ��������ڵ�ĻҶ�ֵ
	float im1m1=dot( texture( uImageUnit,vST-stpp).rgb,W );
	float ip1p1=dot( texture( uImageUnit,vST+stpp).rgb,W );
	float im1p1=dot( texture( uImageUnit,vST-stpm).rgb,W );
	float ip1m1=dot( texture( uImageUnit,vST+stpm).rgb,W );
	float im10=dot( texture( uImageUnit,vST-stp0).rgb,W );//������ڵ�ĻҶ�ֵ	
	float ip10=dot( texture( uImageUnit,vST+stp0).rgb,W );//�ұ����ڵ�ĻҶ�ֵ	
	float i0m1=dot( texture( uImageUnit,vST-st0p).rgb,W );//�ϱ����ڵ�ĻҶ�ֵ	
	float i0p1=dot( texture( uImageUnit,vST+st0p).rgb,W );//�±����ڵĵ�ĻҶ�ֵ	
	//��ͼ����ƽ�������㣬�ֱ�ó�������������Ȳ�ֽ���ֵ����sobel���ӵĺ��ݻҶ�ֵ 
	float h = -1.0 * im1p1 - 2.0 * i0p1 - 1.0 * ip1p1 + 1.0 * im1m1 + 2.0 * i0m1 + 1.0 * ip1m1;
	float v = -1.0 * im1m1 - 2.0 * im10 - 1.0 * im1p1 + 1.0 * ip1m1 + 2.0 * ip10 + 1.0 * ip1p1;
	float mag=length(vec2(h,v));//��ǰ���ص���ݶ�ֵ
	if(mag>uMagTol)
	{//����ݶ�mag������ֵ������Ϊ�õ�Ϊ��Ե�㣬������ɫΪ��ɫ
		fFragColor=vec4(0.0,0.0,0.0,1.0);
	}else
	{//���������Ե,�����������ɫֵ
		rgb.rgb *= uQuantize; //����ǰƬԪ����ɫֵ��������ֵ
		rgb.rgb += vec3(0.5,0.5,0.5);					//��ͨ���̶�
		ivec3 intrgb = ivec3(rgb.rgb);					//ת�����������͵�����
		rgb.rgb = vec3(intrgb) / uQuantize; 			//���������͵�ƬԪ��ɫֵ��������ֵ
		fFragColor=vec4(rgb,1.0);					//������¼����������ɫֵ
	}
}

	
