#version 300 es
precision mediump float;				//����Ĭ�ϵĸ��㾫��
uniform highp int isShadow;			//��Ӱ���Ʊ�־
uniform sampler2D sTexture;//������������
in vec4 diffuse;					//�Ӷ�����ɫ�����ݹ�����ɢ�������ǿ��
in vec4 specular;				//�Ӷ�����ɫ�����ݹ����ľ��������ǿ��
in vec2 vTextureCoord;//�Ӷ�����ɫ�����ݹ�����������������
out vec4 fragColor;//�������ƬԪ��ɫ
void main() { 
   	if(isShadow==0){						//�������屾��
	    //�����������������ƬԪ��ɫֵ
		vec4 finalColor=texture(sTexture, vTextureCoord);
   		//����ƬԪ������ɫֵ
   		fragColor =finalColor*specular+finalColor*diffuse;
   	}else{								//������Ӱ
	   fragColor = vec4(0.18,0.2,0.2,0.0);//ƬԪ������ɫΪ��Ӱ����ɫ
   	}
}
