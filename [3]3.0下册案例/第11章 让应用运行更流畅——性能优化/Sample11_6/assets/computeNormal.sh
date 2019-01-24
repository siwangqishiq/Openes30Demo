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
layout( std140, binding=4 ) readonly buffer Pos  
{
   pos Positions[ ]; 
}; 
//ʹ��һ��buffer�����������������ķ���������
layout( std140, binding=5 ) buffer Nor  
{
   normal Normals[ ]; 
}; 
 
//���벼���޶�������һ��1X1X1�ı��ع�����----gl_WorkGroupSize
layout( local_size_x = 1, local_size_y = 1, local_size_z = 1 ) in;

//֪������������������꣬�����������淨�����ķ���
vec3 calNormal(vec3 a,vec3 b,vec3 c)
{
   vec3 vab=b-a;//��a�㵽b�������
   vec3 vac=c-a;//��a�㵽c�������
   return normalize(cross(vab,vac));  //�������շ�����ֵ
}

void main()                         
{//������           
	//�����ǰ�������ϽǶ��������            
   	uint indexTemp=gl_NumWorkGroups.x*gl_WorkGroupID.y+gl_WorkGroupID.x;
    
    //�����α��ָ��
	//0---1
	//| / |
	//3---2
    
    //����ǰ���㲻�����һ�У��Ҳ������һ��
    if(gl_WorkGroupID.x<(gl_NumWorkGroups.x-uint(1))&&gl_WorkGroupID.y<(gl_NumWorkGroups.y-uint(1)))
    {
       //����0-3-1�����η�����
       //��ʱ��a��
       vec4 a=Positions[indexTemp].locationA;     
       //��ʱ��b��
       vec4 b=Positions[indexTemp+gl_NumWorkGroups.x].locationA;
       //��ʱ��c��
       vec4 c=Positions[indexTemp+uint(1)].locationA;
       //����������η�����
       vec3 normal=calNormal(a.xyz,b.xyz,c.xyz);
       //�����������¼������
       Normals[indexTemp].normalA.xyz+=normal;
       Normals[indexTemp+gl_NumWorkGroups.x].normalA.xyz+=normal;
       Normals[indexTemp+uint(1)].normalA.xyz+=normal;
       
       //����1-3-2�����η�����
       //��ʱ��a��
       a=Positions[indexTemp+uint(1)].locationA;     
       //��ʱ��b��
       b=Positions[indexTemp+gl_NumWorkGroups.x].locationA;
       //��ʱ��c��
       c=Positions[indexTemp+uint(1)+gl_NumWorkGroups.x].locationA;
       //����������η�����
       normal=calNormal(a.xyz,b.xyz,c.xyz);
       //�����������¼������
       Normals[indexTemp+uint(1)].normalA.xyz+=normal;
       Normals[indexTemp+gl_NumWorkGroups.x].normalA.xyz+=normal;
       Normals[indexTemp+uint(1)+gl_NumWorkGroups.x].normalA.xyz+=normal;
    }
}