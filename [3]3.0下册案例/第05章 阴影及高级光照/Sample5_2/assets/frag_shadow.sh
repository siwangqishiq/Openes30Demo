#version 300 es
precision mediump float;				//����Ĭ�ϵĸ��㾫��
uniform highp int isShadow;			//��Ӱ���Ʊ�־
in vec4 ambient;  					//�Ӷ�����ɫ�����ݹ����Ļ���������ǿ��
in vec4 diffuse;						//�Ӷ�����ɫ�����ݹ�����ɢ�������ǿ��
in vec4 specular;					//�Ӷ�����ɫ�����ݹ����ľ��������ǿ��
out vec4 fragColor;
void main(){
   if(isShadow==0){							//�������屾��
	    vec4 finalColor=vec4(1.0,1.0,1.0,0.0);		//���屾�����ɫ
	    //�ۺ�����ͨ���������ǿ�ȼ�ƬԪ����ɫ���������ƬԪ����ɫ�����ݸ�����
	    fragColor = finalColor*ambient+finalColor*specular+finalColor*diffuse;
   	}else{								//������Ӱ
	    fragColor = vec4(0.2,0.2,0.2,0.0);		//ƬԪ������ɫΪ��Ӱ����ɫ
    }
} 
