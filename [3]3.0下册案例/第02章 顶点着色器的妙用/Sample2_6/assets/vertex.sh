#version 300 es
uniform float uFatFactor;//���յĴ�������ϵ��
uniform mat4 uMVPMatrix; //�ܱ任����
uniform mat4 uMMatrix; //�任����
uniform vec3 uLightLocation;	//��Դλ��
uniform vec3 uCamera;	//�����λ��
in vec3 aPosition;  //����λ��
in vec3 aNormal;    //���㷨����
in vec2 aTexCoor;    //������������
out vec4 ambient;//���ڴ��ݸ�ƬԪ��ɫ���ı����Ļ���������ǿ��
out vec4 diffuse;//���ڴ��ݸ�ƬԪ��ɫ���ı�����ɢ�������ǿ��
out vec4 specular;//���ڴ��ݸ�ƬԪ��ɫ���ı����ľ��������ǿ��
out vec2 vTextureCoord;//���ڴ��ݸ�ƬԪ��ɫ���ı�������������
void pointLight(					//��λ����ռ���ķ���
  in vec3 normal,				//������
  inout vec4 ambient,			//����������ǿ��
  inout vec4 diffuse,				//ɢ�������ǿ��
  inout vec4 specular,			//���������ǿ��
  in vec3 lightLocation,			//��Դλ��
  in vec4 lightAmbient,			//��Դ������ǿ��
  in vec4 lightDiffuse,			//��Դɢ���ǿ��
  in vec4 lightSpecular			//��Դ�����ǿ��
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
void main()     
{ 
	//�����ܱ任�������˴λ��ƴ˶����λ�ã��ڼ���ʱ������λ�����ŷ����������ƶ�һ���ľ���
   gl_Position = uMVPMatrix * vec4(aPosition+aNormal*uFatFactor,1); 
   vec4 ambientTemp, diffuseTemp, specularTemp;   //�����⡢ɢ��⡢���淴������ʱ����      
   pointLight(normalize(aNormal),ambientTemp,diffuseTemp,specularTemp,uLightLocation,
   vec4(0.15,0.15,0.15,1.0),vec4(0.9,0.9,0.9,1.0),vec4(0.4,0.4,0.4,1.0));
   ambient=ambientTemp;//������������ǿ�ȴ���ƬԪ��ɫ��
   diffuse=diffuseTemp;//��ɢ�������ǿ�ȴ���ƬԪ��ɫ��
   specular=specularTemp;//�����������ǿ�ȴ���ƬԪ��ɫ��
   vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
}                      