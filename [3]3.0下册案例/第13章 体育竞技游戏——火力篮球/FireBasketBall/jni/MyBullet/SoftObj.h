/*
 * SoftObj.h
 *
 *  Created on: 2015��1��31��
 *      Author: Nr
 */

#ifndef _SoftObj_H_
#define _SoftObj_H_

#include "Bullet/LinearMath/btAlignedObjectArray.h"
#include "Bullet/btBulletDynamicsCommon.h"
#include "Bullet/LinearMath/btVector3.h"
#include "Bullet/BulletDynamics/Dynamics/btRigidBody.h"
#include "Bullet/BulletSoftBody/btSoftBody.h"
#include "Bullet/BulletSoftBody/btSoftBodyHelpers.h"
#include "Bullet/BulletSoftBody/btSoftRigidDynamicsWorld.h"
#include "Bullet/BulletSoftBody/btSoftBodySolvers.h"
#include "Bullet/BulletSoftBody/btDefaultSoftBodySolver.h"
#include "Bullet/BulletSoftBody/btSoftBodyRigidBodyCollisionConfiguration.h"

#include "MyBullet/AnyShapeByVectices.h"
#include "MyBullet/TexBody.h"
#include "MyBullet/TexCuboid.h"


using namespace std;
#include <vector>

class SoftObj {

	btSoftBody *psb;//����ָ��
	btSoftRigidDynamicsWorld *m_dynamicsWorld;//������������ָ��
	btSoftBodyWorldInfo m_softBodyWorldInfo;//��������������Ϣ
	AnyShapeByVectices* asbv;
	float* m_vertices;//��������

	int m_texId;//����id

	int m_numT;//�����θ���

	float* m_gVertices;//��������
	int m_nums_ver;//�������鳤��
	int* m_gIndices;//������������
	int m_nums_ind;//�����������鳤��

	int m_numsTriangles;//�����θ���

	float* m_texs;
	int m_numtexs;

	btVector3 m_pos;//λ������

	std::vector<btVector3 >* nodeVector;

public:
	SoftObj(
			btSoftRigidDynamicsWorld* m_dynamicsWorld,
			btSoftBodyWorldInfo& m_softBodyWorldInfo,
			btVector3 pos,
			float* m_gVertices,		//��������
			int m_nums_ver,			//�������
			int* m_gIndices,		//��������
			int m_nums_ind,			//��������
			float* m_texs,			//������������
			int m_numtexs,			//�����������
			float scale
			);//���캯��
//	virtual ~SoftObj();

	void drawSelf(GLuint texIdIn);//����������
	btSoftBody* getBody();//��ȡ����ָ�뷽��
	void initVertexTemp();
	void initVertex();//��ʼ���������ݷ���

	void initObj();//��ʼ�����巽��
	btSoftBody*	createSoftBody(const btVector3& x,const btVector3& a);//�������巽��

	btVector3* floatToBtVector3(float* vertex, int nums);//�������飬��������
	float* btVector3ToFloat(btVector3* vertex, int nums);//�������飬��������
public:
	btVector3* verticesBt;					//���ݴ���ĵ���float��������µ�btVector3��������

	static float* vertices_soft;
	static int numsVer_soft;
	static int* indices_soft;
	static int numsInd_soft;
	static float* texs_soft;
	static int numsTex_soft;
	static float* normal_soft ;
	static int numNormal_soft ;

	bool templin = true;
};

#endif /* JNI_HELLOCPP_SoftObj_H_ */

