#version 310 es
precision mediump float;//����Ĭ�ϵĸ��㾫��
struct pos
{
   vec4 locationA; //����λ��
};

struct normal
{
   vec4 normalA; //���㷨����
};
//ʹ��һ��buffer�����������������Ķ�������
layout( std140, binding=4 ) buffer Pos  
{
   pos Positions[ ]; 
}; 
//ʹ��һ��buffer�����������������ķ���������
layout( std140, binding=5 ) writeonly buffer Nor  
{
   normal Normals[ ]; 
}; 
 
//���벼���޶�������һ��1X1X1�ı��ع�����----gl_WorkGroupSize
layout( local_size_x = 1, local_size_y = 1, local_size_z = 1 ) in;

uniform vec2 bx1;	//����1
uniform float bc1;	//����1
uniform float zf1;  //���1
uniform float qsj1; //��ʼ��1

uniform vec2 bx2;	//����2
uniform float bc2;	//����2
uniform float zf2;  //���2
uniform float qsj2; //��ʼ��2

uniform vec2 bx3;	//����3
uniform float bc3;	//����3
uniform float zf3;  //���3
uniform float qsj3; //��ʼ��3

//����һ������ָ����ĸ߶��Ŷ�
float calHdr
(
	vec2 bx,	//����
	float bc,	//����
	float zf,	//���
	float qsj,	//��ʼ��
	vec2 ddxz	//���Ŷ��Ķ���xz����
)
{
    //�����벨�ĵľ���
    float dis=distance(ddxz,bx);
    //����Ƕȿ��
    float angleSpan=dis*2.0*3.1415926/bc;
    //����˲��Դ˶��������Ŷ�
    float hrd=sin(angleSpan+qsj)*zf;
    //���ظ߶��Ŷ�ֵ
    return hrd;
}

void main()                         
{ //������           
	//������������            
   	uint indexTemp=gl_NumWorkGroups.x*gl_WorkGroupID.y+gl_WorkGroupID.x;
    //ȡ�������λ��
    vec4 positionTemp=Positions[indexTemp].locationA;
    //�����������Զ���ĸ߶��Ŷ�ֵ������
    Positions[indexTemp].locationA.y=calHdr(bx1,bc1,zf1,qsj1,positionTemp.xz)+
                                     calHdr(bx2,bc2,zf2,qsj2,positionTemp.xz)+
                                     calHdr(bx3,bc3,zf3,qsj3,positionTemp.xz);
    //�˶���ķ��������㣬�Ա���һ�ּ��㷨����
    Normals[indexTemp].normalA=vec4(0.0,0.0,0.0,0.0);
}