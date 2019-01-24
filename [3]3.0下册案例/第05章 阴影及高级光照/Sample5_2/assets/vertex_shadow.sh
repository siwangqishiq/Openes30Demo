#version 300 es
precision mediump float;
uniform int isShadow;					//��Ӱ���Ʊ�־
uniform mat4 uMVPMatrix; 				//�ܱ任����
uniform mat4 uMMatrix; 					//�任����
uniform mat4 uMProjCameraMatrix; 		//ͶӰ���������Ͼ���
uniform vec3 uLightLocation;				//��Դλ��
uniform vec3 uCamera;					//�����λ��
in vec3 aPosition;  						//����λ��
in vec3 aNormal;    					//���㷨����
out vec4 ambient;						//���ڴ��ݸ�ƬԪ��ɫ���Ļ���������ǿ��
out vec4 diffuse; 						//���ڴ��ݸ�ƬԪ��ɫ����ɢ�������ǿ��
out vec4 specular;	 					//���ڴ��ݸ�ƬԪ��ɫ���ľ��������ǿ��
//��λ����ռ���ķ���
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
  vec3 normalTarget=aPosition+normal;	//����任��ķ�����
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*vec4(aPosition,1)).xyz;
  newNormal=normalize(newNormal); 	//�Է��������
  //����ӱ���㵽�����������
  vec3 eye= normalize(uCamera-(uMMatrix*vec4(aPosition,1)).xyz);  
  //����ӱ���㵽��Դλ�õ�����vp
  vec3 vp= normalize(lightLocation-(uMMatrix*vec4(aPosition,1)).xyz);  
  vp=normalize(vp);//��ʽ��vp
  vec3 halfVector=normalize(vp+eye);	//����������ߵİ�����    
  float shininess=50.0;				//�ֲڶȣ�ԽСԽ�⻬
  float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//��������vp�ĵ����0�����ֵ
  diffuse=lightDiffuse*nDotViewPosition;				//����ɢ��������ǿ��
  float nDotViewHalfVector=dot(newNormal,halfVector);	//������������ĵ�� 
  float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); 	//���淴���ǿ������
  specular=lightSpecular*powerFactor;    			//���㾵��������ǿ��
}

void main(){
   if(isShadow==1){						//��־λΪ1���������Ӱ
      vec3 A=vec3(0.0,0.1,0.0);			//������Ӱƽ��������һ�������
      vec3 n=vec3(0.0,1.0,0.0);			//������Ӱƽ��ķ�����
      vec3 S=uLightLocation; 				//��Դλ��
      vec3 V=(uMMatrix*vec4(aPosition,1)).xyz;  		//����ƽ�ƺ���ת�任��ĵ������
      vec3 VL=S+(V-S)*(dot(n,(A-S))/dot(n,(V-S)));//�����ع���ͶӰ����Ҫ������Ӱ��ƽ���ϵ������
      gl_Position = uMProjCameraMatrix*vec4(VL,1); 	//������Ͼ������˴λ��ƴ˶���λ��
   }else{   							//�����ܱ任�������˴λ��ƴ˶���λ��
           gl_Position = uMVPMatrix * vec4(aPosition,1);
   }
   pointLight(normalize(aNormal),ambient,diffuse,specular,uLightLocation,
   vec4(0.4,0.4,0.4,1.0),vec4(0.7,0.7,0.7,1.0),vec4(0.3,0.3,0.3,1.0));//�������
}



                   