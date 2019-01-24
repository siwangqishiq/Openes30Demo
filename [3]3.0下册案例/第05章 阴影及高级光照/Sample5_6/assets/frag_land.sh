#version 300 es
precision mediump float;//����Ĭ�ϵĸ��㾫��
uniform mat4 uMMatrix; //�����任����
uniform vec3 uLightLocation;	//��Դλ��
uniform vec3 uCamera;	//�����λ��	
uniform vec3 light;//�۹�Ƶķ�������
in vec3 VaPosition;//���մӶ�����ɫ�������Ķ���λ��
in vec3 VaNormal;//���մӶ�����ɫ�����ݹ����ķ�����
out vec4 fragColor;//�����ƬԪ��ɫ
void pointLight(					//��λ����ռ���ķ���
  in vec3 normal,				//������
  inout vec4 ambient,			//����������ǿ��
  inout vec4 diffuse,				//ɢ�������ǿ��
  inout vec4 specular,			//���������ǿ��
  in vec3 lightLocation,			//��Դλ��
  in vec4 lightAmbient,			//������ǿ��
  in vec4 lightDiffuse,			//ɢ���ǿ��
  in vec4 lightSpecular			//�����ǿ��
){
  ambient=lightAmbient;			//ֱ�ӵó������������ǿ��  
  vec3 normalTarget=VaPosition+normal;	//����任��ķ�����
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(VaPosition,1)).xyz;
  newNormal=normalize(newNormal); 	//�Է��������
  //����ӱ���㵽�����������
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(VaPosition,1)).xyz);  
  //����ӱ���㵽��Դλ�õ�����vp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(VaPosition,1)).xyz);  
  vp=normalize(vp);//��ʽ��vp
  vec3 halfVector=normalize(vp+eye);	//����������ߵİ�����    
  float shininess=50.0;				//�ֲڶȣ�ԽСԽ�⻬
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//��������vp�ĵ����0�����ֵ
  diffuse=lightDiffuse*nDotViewPosition;				//����ɢ��������ǿ��
  float nDotViewHalfVector=dot(newNormal,halfVector);	//������������ĵ�� 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//���淴���ǿ������
  specular=lightSpecular*powerFactor;    			//���㾵��������ǿ��
}

void main()
{   
   	vec4 ambient,diffuse,specular;
   	vec3 dis=VaPosition-uLightLocation;//�������Դλ�õ�����
   	float l1=length(light);//�۹�Ƶķ���������ģ
   	float l2=length(dis);//�������Դλ��֮��������ģ
   	float media=0.0;//������ȱ���ֵ
   	//��������dis������light�ļн�����ֵ
   	float cos_angle=dot(dis,light)/(l1*l2);
    pointLight(normalize(VaNormal),ambient,diffuse,specular,uLightLocation,
    	vec4(0.1,0.1,0.1,1.0),vec4(0.7,0.7,0.7,1.0),vec4(0.3,0.3,0.3,1.0)); 
    	   	 
   		vec4 finalcolor=vec4(0.8,0.8,0.8,1.0); 		   //���屾�����ɫ
   		vec4 colorA=finalcolor*ambient+finalcolor*specular+finalcolor*diffuse;//������������µ���ɫ
   		vec4 colorB=vec4(0.1,0.1,0.1,1.0);//�����ڷǹ���������ɫ
	if(cos_angle>0.98)
   	{//����ȫ������
   	  	finalcolor=colorA;
   	}
   	else if(cos_angle>0.95&&cos_angle<=0.98)
   	{//���ڹ�����
        media=(cos_angle-0.95)/0.03;
       	finalcolor=colorA*media+colorB*(1.0-media);
   	}
   	else
   	{//������Ӱ��
   		finalcolor=colorB;
   	}	
   	fragColor =finalcolor;
}     
