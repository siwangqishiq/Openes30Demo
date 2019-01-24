#version 300 es
precision mediump float;
uniform highp mat4 uMVPMatrixMirror; //����������۲켰ͶӰ��Ͼ���
uniform sampler2D sTextureDY;//��Ӱ������������
uniform sampler2D sTextureWater;//ˮ���Լ�������������
uniform sampler2D sTextureNormal;		//�����������ݣ����ߣ�
uniform vec3 uCamera;				//�����λ��
uniform vec3 uLightLocation;		//��Դλ��
in mat4 vMMatrix; 				//�任����
in vec4 vPosition;
in vec2 vTextureCoord; //���մӶ�����ɫ�������Ĳ���
in vec3 fNormal;    			//���մӶ�����ɫ�����ݹ����ķ�����
in vec3 mvPosition;  				//���մӶ�����ɫ�����ݹ����Ķ���λ��
out vec4 fragColor;
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
  	vec3 normalTarget=mvPosition+normal;	//����任��ķ�����
  	vec3 newNormal=(vMMatrix*vec4(normalTarget,1)).xyz-(vMMatrix*vec4(mvPosition,1)).xyz;
  	newNormal=normalize(newNormal); 	//�Է��������
  	//����ӱ���㵽�����������
  	vec3 eye= normalize(uCamera-(vMMatrix*vec4(mvPosition,1)).xyz);  
  	//����ӱ���㵽��Դλ�õ�����vp
  	vec3 vp= normalize(lightLocation-(vMMatrix*vec4(mvPosition,1)).xyz);  
  	vp=normalize(vp);//��ʽ��vp
  	vec3 halfVector=normalize(vp+eye);	//����������ߵİ�����
  
	float nDotViewPosition=max(0.0,dot(newNormal,vp)); 	//��������vp�ĵ����0�����ֵ
	diffuse=lightDiffuse*nDotViewPosition;				//����ɢ��������ǿ��
	
	float nDotViewHalfVector=dot(newNormal,halfVector);	//������������ĵ��   
	float shininess=50.0;								//�ֲڶȣ�ԽСԽ�⻬  50
	float powerFactor=max(0.0,pow(nDotViewHalfVector,shininess)); //���淴���ǿ������  
	
	specular=lightSpecular*powerFactor;    			//���㾵��������ǿ��
}
void main()                         
{    
	//��ƬԪ��λ��ͶӰ������������Ľ�ƽ����
   vec4 gytyPosition=uMVPMatrixMirror * vec4(vPosition.xyz,1);
   gytyPosition=gytyPosition/gytyPosition.w;	//����͸�ӳ���   
   float s=(gytyPosition.s+1.0)/2.0;			//��ͶӰ������껻��Ϊ��������
   float t=(gytyPosition.t+1.0)/2.0; 
   
   vec4 ambient,diffuse,specular;	  //������������ͨ������ǿ�ȵı��� 
   pointLight(normalize(fNormal),ambient,diffuse,specular,uLightLocation,
   vec4(0.9,0.9,0.9,1.0),vec4(0.1,0.1,0.1,1.0),vec4(0.9,0.9,0.9,1.0));  
   	
   vec4 normalColor = texture(sTextureNormal, vTextureCoord); //�ӷ�������ͼ�ж���ֵ
   //��ֵ�ָ���-1��+1��Χ
   vec3 cNormal=vec3(2.0*(normalColor.r-0.5),2.0*(normalColor.g-0.5),2.0*(normalColor.b-0.5));   
   cNormal=normalize(cNormal);   //���Ŷ�����������
   
   const float mPerturbationAmt=0.02;			//�Ŷ�ϵ������Ť���̶�
   s=s+mPerturbationAmt*s*cNormal.x;	//�����Ŷ������������S
   t=t+mPerturbationAmt*t*cNormal.y;	//�����Ŷ������������T
   
   //���е�Ӱ�������
   vec4 dyColor=texture(sTextureDY, vec2(s,t));
   //����ˮ�����������
   vec4 waterColor=texture(sTextureWater,vTextureCoord);
   //��ϵ�Ӱ��ˮ����õ���ƬԪ����ɫֵ ����Ӱ����ռ70%��ˮ����������ռ30%
   vec4 dyAndWaterColor=mix(waterColor,dyColor,0.7);
   //�ۺ�3��ͨ���������ǿ���Լ���ϵõ�����ɫֵ��������յ���ɫ�����ݸ���Ⱦ����
   fragColor=dyAndWaterColor*ambient+dyAndWaterColor*specular+dyAndWaterColor*diffuse;
}   