#version 300 es
uniform mat4 uVPMatrix;	 						//�����͸�Ӿ���
uniform mat4 uMMatrix; 							//�����任����
uniform float totalNum;							//�ܵ�С�ݿ���
uniform sampler2D sTexture;						//�Ŷ�������������
uniform sampler2D jbTexture;						//��ɫ����������������
in vec3 aPosition;  								//����λ��
in vec2 aTexCoor;    							//������������
out vec4 changeTexture;							//�ı�ݵ���ɫ����������
out vec2 vTextureCoord;  						//���ڴ��ݸ�ƬԪ��ɫ���ı���
void xz_Matrix(									//������ת�ķ���
		in float posx,							//��x���ϵķ���
		in float posy,	 						//��y���ϵķ���
		in float posz, 							//��z���ϵķ���
		in float xz_cos,							//��תһ���Ƕȵ�cosֵ
		in float xz_sin, 							//��תһ���Ƕȵ�sinֵ
		out mat4 mmtrix						//�������ת����
){
	mmtrix=mat4
	(								//������ת����
  		xz_cos+(1.0-xz_cos)*posx*posx , (1.0-xz_cos)*posy*posx - xz_sin*posz ,
			(1.0-xz_cos)*posz*posx + xz_sin*posy,0.0,
 		(1.0-xz_cos)*posx* posy + xz_sin*posz , xz_cos + (1.0-xz_cos)*posy*posy ,
			(1.0-xz_cos)*posz*posy - xz_sin*posx,0.0,
  		(1.0-xz_cos)*posx*posz - xz_sin*posy,(1.0-xz_cos)*posy* posz + xz_sin* (1.0-xz_cos)*
			posx , xz_cos +posz*posz,0.0,
		0.0,0.0,0.0,1.0
	);
}
void main(){
   int colCount=int(sqrt(totalNum));       		//ÿ�ж��ٸ�
   const float unitSize=0.3; 					//��λƽ����
   int col=gl_InstanceID%colCount;				//�����к�
   int row=gl_InstanceID/colCount; 				//�����к�
   float xtex=float(col)/float(colCount); 			//�����ÿ��С����ÿ���ж�Ӧ��λ��
   float ytex=float(row)/float(colCount); 			//�����ÿ��С����ÿ���ж�Ӧ��λ��
   	vec4 noiseVec=texture(sTexture,vec2(xtex,ytex)); //�����Ŷ��������
   	float dot_product = dot(vec4(noiseVec.rgb,gl_InstanceID), vec4(12.9898,78.233,45.164,94.673));
   	float random=fract(cos(dot_product) * 43758.5453);//���0.0~1.0��α�����
   	float size=random+1.0;						//ƽ��ʱλ�õ����ƫ����
   	mat4 pyMatrix=mat4(						//����ƽ�ƾ���
   		1,0,0,0, 
   		0,1,0,0, 
   		0,0,1,0,
   		unitSize*float(col)*size-unitSize,0,unitSize*float(row)*size-unitSize,1//ƽ�Ƶ�λ��
   	);
	mat4 xzMatrix_X;							//������x�����ת����
	mat4 xzMatrix_Y;							//������y�����ת����
   	float cx_Angle=random*360.0;					//��y��--����
	float zt_Angle=random*30.0;					//��x��--��̬�������̶ȣ�
	float xz_Cos=cos(radians(cx_Angle));			//�����y���cosֵ
   	float xz_Sin=sqrt(1.0-(xz_Cos*xz_Cos)); 			//�����y���sinֵ
   	xz_Matrix(0.0,1.0,0.0,xz_Cos,xz_Sin,xzMatrix_Y);//������y����ת����--���ó���
   	xz_Cos=cos(radians(zt_Angle));				//�����x���cosֵ
   	xz_Sin=sqrt(1.0-(xz_Cos*xz_Cos)); 				//�����x���sinֵ
	xz_Matrix(1.0,0.0,0.0,xz_Cos,xz_Sin,xzMatrix_X);//������x����ת����--������̬
   	mat4 MVPMatrix=uVPMatrix*uMMatrix*pyMatrix*xzMatrix_Y*xzMatrix_X;//�����ܱ任����
   	gl_Position = MVPMatrix * vec4(aPosition,1.0); //�����ܱ任�������˴λ��ƴ˶���λ��
   	vTextureCoord = aTexCoor;//�����յ��������괫�ݸ�ƬԪ��ɫ��
   	if(ytex<1.0/totalNum){	//��֤��ɫ�Ľ������
   		ytex=0.2;			//���ö���ɫ����������в���ʱ��tֵΪ0.2
   	}
   	vec4 jbTex=texture(jbTexture,vec2(xtex,ytex));//�����ɫ�����������������
	changeTexture=jbTex;	//���������������������ݴ��ݸ�ƬԪ��ɫ��
}