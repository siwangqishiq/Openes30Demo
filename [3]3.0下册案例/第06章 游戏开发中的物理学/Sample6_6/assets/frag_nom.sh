#version 300 es
precision mediump float;
in vec2 vTextureCoord;//���մӶ�����ɫ�������Ĳ���
uniform highp int isShadow;			//��Ӱ���Ʊ�־
in vec4 ambient;  				//�Ӷ�����ɫ�����ݹ����Ļ���������ǿ��
in vec4 diffuse;					//�Ӷ�����ɫ�����ݹ�����ɢ�������ǿ��
in vec4 specular;				//�Ӷ�����ɫ�����ݹ����ľ��������ǿ��
uniform sampler2D sTexture;//������������
out vec4 fragColor;//�����ƬԪ��ɫ
void main()                         
{  
     if(isShadow==0){						//�������屾��
	    vec4 finalColor=texture(sTexture, vTextureCoord); //���屾�����ɫ
	    //�ۺ�����ͨ���������ǿ�ȼ�ƬԪ����ɫ���������ƬԪ����ɫ�����ݸ�����
	    fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
   	}else{								//������Ӱ
	   fragColor = vec4(0.2,0.2,0.2,0.0);//ƬԪ������ɫΪ��Ӱ����ɫ
   	}
}       