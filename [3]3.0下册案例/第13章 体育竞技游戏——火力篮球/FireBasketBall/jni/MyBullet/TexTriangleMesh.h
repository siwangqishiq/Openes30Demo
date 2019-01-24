#ifndef _TexTriangleMesh_H_
#define _TexTriangleMesh_H_

#include "util/MatrixState.h"
#include "../Bullet/BulletDynamics/Dynamics/btRigidBody.h"
#include "../Bullet/BulletDynamics/Dynamics/btDynamicsWorld.h"
#include "../Bullet/LinearMath/btVector3.h"

#include "MyBullet/AnyShapeByVectices.h"
#include "MyBullet/TexBody.h"

class TexTriangleMesh : public TexBody
{
	btDynamicsWorld* m_dynamicsWorld;//��������ָ��
	btRigidBody* body;//�������ָ��
	AnyShapeByVectices* ctm;//���ƶ���
	int triangleTexId;//����id
public :
	TexTriangleMesh(
		btScalar mass,//����

	float* vertices,//������������    ���ظ�ֵ
	int numsVer,//������������ĳ���
	int* indices,
	int numsInd,
	float* texs,
	int numsTex,
	float* normals,
	int num_normals,
	float scale,//��������

		btVector3 pos,//λ��
		btScalar restitutionIn,//�ָ�ϵ��
		btScalar frictionIn,//Ħ��ϵ��
		btDynamicsWorld *dynamicsWorld,//��������ָ��
		int triangleTexId//����������id
		);//���캯��
//	~TexTriangleMesh();
	void drawSelf();//��������ķ���
	btRigidBody* getBody();//��ȡ����ָ�뷽��
public:
	static float* vertices_yuanhuan;
	static int numsVer_yuanhuan;
	static int* indices_yuanhuan;
	static int numsInd_yuanhuan;
	static float* texs_yuanhuan;
	static int numsTex_yuanhuan;
	static float* normal_yuanhuan ;
	static int numNormal_yuanhuan ;
};

#endif
