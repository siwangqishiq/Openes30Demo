#version 300 es
precision mediump float;							//����Ĭ�ϵĸ��㾫��
in vec2 vTextureCoord; 						//���մӶ�����ɫ������������������
in float currY;								//���մӶ�����ɫ����������Y����
in vec4 pLocation;			                    //���մӶ�����ɫ���������Ķ�������
uniform float slabY;            					//������Ӧ��ƽ��ĸ߶�
uniform float startAngle;            				//�Ŷ���ʼ��
uniform vec3 uCamaraLocation;   					//�����λ��
uniform sampler2D sTextureGrass;					//�����������ݣ���Ƥ��
uniform sampler2D sTextureRock;					    //�����������ݣ���ʯ��
uniform float landStartY;							//����������ʼY����
uniform float landYSpan;							//����������
out vec4 fragColor;//�������ƬԪ��ɫ

float tjFogCal(vec4 pLocation){//���������Ũ�����ӵķ���
   
   float xAngle=pLocation.x/16.0*3.1415926;//���������X����������ĽǶ�
   
   float zAngle=pLocation.z/20.0*3.1415926;//���������Z����������ĽǶ�
   
   float slabYFactor=sin(xAngle+zAngle+startAngle);//������ʼ�Ǽ�����ǶȺ͵�����ֵ
   //����������������ƬԪ�����߲�������Pc+(Pp-Pc)t����ƽ�潻���tֵ
   float t=(slabY+slabYFactor-uCamaraLocation.y)/(pLocation.y-uCamaraLocation.y);
   //��Ч��t�ķ�ΧӦ����0~1�ķ�Χ�ڣ��������ڷ�Χ�ڱ�ʾ������ƬԪ������ƽ������
   if(t>0.0&&t<1.0){//������Ч��Χ����
      //�����������ƽ��Ľ�������
	  float xJD=uCamaraLocation.x+(pLocation.x-uCamaraLocation.x)*t;
	  float zJD=uCamaraLocation.z+(pLocation.z-uCamaraLocation.z)*t;
	  vec3 locationJD=vec3(xJD,slabY,zJD);
	  
	  float L=distance(locationJD,pLocation.xyz);//������㵽������ƬԪλ�õľ���
	  float L0=10.0;
	  
	  return L0/(L+L0);//������������Ũ������
   }else{
      return 1.0;//��������ƬԪ������ƽ�����£����ƬԪ������Ӱ��
   }}
void main(){      
     
   vec4 gColor=texture(sTextureGrass, vTextureCoord);//�Ӳ�Ƥ�����в�������ɫ  
   	
   vec4 rColor=texture(sTextureRock, vTextureCoord); //����ʯ�����в�������ɫ 	
   vec4 finalColor;									//ƬԪ������ɫ
   if(currY<landStartY){	
   				
   		finalColor=gColor;	//��ƬԪY����С�ڹ���������ʼY����ʱ���ò�Ƥ����
   }else if(currY>landStartY+landYSpan){
   
	  	finalColor=rColor;//��ƬԪY������ڹ���������ʼY����ӿ��ʱ������ʯ����	
   }else{//��ƬԪY�����ڹ�������Χ��ʱ����Ƥ����ʯ���
   	   	
       	float currYRatio=(currY-landStartY)/landYSpan;//������ʯ������ռ�İٷֱ�	
       	//����ʯ����Ƥ������ɫ���������
       	finalColor= currYRatio*rColor+(1.0- currYRatio)*gColor;
   } 
   float fogFactor=tjFogCal(pLocation);//������Ũ������
   //������Ũ�����ӡ������ɫ��ƬԪ���������������ɫ�����ƬԪ��������ɫ
   fragColor=fogFactor*finalColor+ (1.0-fogFactor)*vec4(0.9765,0.7490,0.0549,0.0); //����ƬԪ������ɫֵ   
}  
