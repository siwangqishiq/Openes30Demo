#version 310 es
uniform mat4 uMVPMatrix; 		//�ܱ任����
uniform mat4 uMMatrix; 			//�任����
uniform vec3 uLightLocation;	//��Դλ��
uniform vec3 uCamera;			//�����λ��

in vec4 aPosition;  	//����λ��
in vec2 aTexCoor;    	//������������
in vec4 aNormal;		//���㷨����

out vec2 vTextureCoord; //���ڴ��ݸ�ƬԪ��ɫ������������
out vec4 vAmbient;	//���ڴ��ݸ�ƬԪ��ɫ���Ļ���������ǿ��
out vec4 vDiffuse;	//���ڴ��ݸ�ƬԪ��ɫ����ɢ�������ǿ��
out vec4 vSpecular;	//���ڴ��ݸ�ƬԪ��ɫ���ľ��������ǿ��

void pointLight(				//��λ����ռ���ķ���
  in vec3 normal,				//������
  inout vec4 ambient,			//����������ǿ��
  inout vec4 diffuse,			//ɢ�������ǿ��
  inout vec4 specular,			//���������ǿ��
  in vec3 lightLocation,		//��Դλ��
  in vec4 lightAmbient,			//������ǿ��
  in vec4 lightDiffuse,			//ɢ���ǿ��
  in vec4 lightSpecular			//�����ǿ��
){
  ambient=lightAmbient;			//ֱ�ӵó������������ǿ��  
  vec3 normalTarget=aPosition.xyz+normal;	//����任��ķ�����
  vec3 newNormal=(uMMatrix*vec4(normalTarget,1)).xyz-(uMMatrix*aPosition).xyz;
  newNormal=normalize(newNormal); 	//�Է��������
  //����ӱ���㵽�����������
  vec3 eye= normalize(uCamera-(uMMatrix*aPosition).xyz);  
  //����ӱ���㵽��Դλ�õ�����vp
  vec3 vp= normalize(lightLocation-(uMMatrix*aPosition).xyz);  
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
   gl_Position = uMVPMatrix * aPosition; 	//�����ܱ任�������˴λ��ƴ˶���λ��
   vTextureCoord = aTexCoor;				//�����յ��������괫�ݸ�ƬԪ��ɫ��
   
   vec4 ambientTemp,diffuseTemp,specularTemp;	  //������������ͨ������ǿ�ȵı��� 
   //���ж�λ�����
   pointLight(normalize(aNormal.xyz),ambientTemp,diffuseTemp,specularTemp,uLightLocation,
   vec4(0.15,0.15,0.15,1.0),vec4(0.8,0.8,0.8,1.0),vec4(0.7,0.7,0.7,1.0));   
   vAmbient=ambientTemp; 		//������������ǿ�ȴ���ƬԪ��ɫ��
   vDiffuse=diffuseTemp; 		//��ɢ�������ǿ�ȴ���ƬԪ��ɫ��
   vSpecular=specularTemp; 		//�����������ǿ�ȴ���ƬԪ��ɫ��  
}                      