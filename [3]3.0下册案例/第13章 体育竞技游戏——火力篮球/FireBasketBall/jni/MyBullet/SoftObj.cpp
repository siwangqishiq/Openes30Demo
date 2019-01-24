/*
 * SoftObj.cpp
 *
 *  Created on: 2016��9��17��
 *      Author: Cy
 */

#include "SoftObj.h"
#include "util/MatrixState.h"
#include "util/ShaderManager.h"

float* SoftObj::vertices_soft = NULL;
int SoftObj::numsVer_soft = 0;
int* SoftObj::indices_soft = NULL;
int SoftObj::numsInd_soft = 0;
float* SoftObj::texs_soft = NULL;
int SoftObj::numsTex_soft = 0;
float* SoftObj::normal_soft = NULL ;
int SoftObj::numNormal_soft = 0 ;
SoftObj::SoftObj(btSoftRigidDynamicsWorld* m_dynamicsWorld,
		btSoftBodyWorldInfo& m_softBodyWorldInfo,
		btVector3 pos,
		float* m_gVertices,		//��������
		int m_nums_ver,			//�������
		int* m_gIndices,		//������������
		int m_nums_ind,			//��������
		float* m_texs,			//������������
		int m_numtexs,			//�����������
		float scale
		)
{
	// TODO Auto-generated constructor stub
	this->m_dynamicsWorld = m_dynamicsWorld;//��¼��������ָ��
	this->m_softBodyWorldInfo = m_softBodyWorldInfo;//��¼����������Ϣ
	this->m_pos = pos;//��¼λ������
	this->m_gVertices = m_gVertices;//��¼��������
	this->m_nums_ind = m_nums_ind;
	this->m_numtexs = m_numtexs;
	for(int i = 0; i<m_nums_ver; i++)//������������
	{
		this->m_gVertices[i] *= scale;//���Ķ�������
	}
	this->m_gIndices = m_gIndices;//��¼��������

	this->m_numsTriangles = m_nums_ind / 3;//���������θ���

	nodeVector = new std::vector<btVector3>();

	initObj();//��ʼ�����巽��
	this->m_numT = psb->m_faces.size();//�����θ���
	this->m_vertices=new float[m_numT*3*3];//������������

	//������float���ݣ�����µ�btVector3����
	verticesBt = floatToBtVector3(m_gVertices,m_nums_ver);

	//�������ƶ���
	this->asbv = new AnyShapeByVectices(
			this->m_vertices,//������������    ���ظ�ֵ
			m_nums_ver,//������������ĳ���
			m_gIndices,
			m_nums_ind,
			m_texs,
			m_numtexs,
			true,
			1.0f,
			ShaderManager::getCommTextureShaderProgram()
			);//����������
	this->initVertex();//��ʼ����������
}
void SoftObj::initVertex()
{
	int count=0;//���������
	//nSize = 144
	int nSize = psb->m_nodes.size();
	nodeVector->clear();
	for(int i=0;i<nSize;i++)
	{
		const btSoftBody::Node& n = psb->m_nodes.at(i);
		//���浽��������
		nodeVector->push_back((n.m_x));
	}
	for(int j=0;j<m_nums_ind;j++)
	{
		int index = m_gIndices[j];
		m_vertices[count++] = nodeVector->at(index).x();
		m_vertices[count++] = nodeVector->at(index).y();
		m_vertices[count++] = nodeVector->at(index).z();
	}
	//	m_numT = 240
//	for(int i=0;i<m_numT;++i)//��������������
//	{
//		const btSoftBody::Face	f=psb->m_faces.at(i);//��ȡ�����ϵ�һ����
//		const btVector3	x[]={f.m_n[0]->m_x,f.m_n[1]->m_x,f.m_n[2]->m_x};//��ȡ����������
//
//		m_vertices[count++]=x[0].x();//��ȡ��һ�����x����
//		m_vertices[count++]=x[0].y();//��ȡ��һ�����y����
//		m_vertices[count++]=x[0].z();//��ȡ��һ�����z����
//
//		m_vertices[count++]=x[1].x();//��ȡ�ڶ������x����
//		m_vertices[count++]=x[1].y();//��ȡ�ڶ������y����
//		m_vertices[count++]=x[1].z();//��ȡ�ڶ������z����
//
//		m_vertices[count++]=x[2].x();//��ȡ���������x����
//		m_vertices[count++]=x[2].y();//��ȡ���������y����
//		m_vertices[count++]=x[2].z();//��ȡ���������z����
//	}
}

void SoftObj::initObj()
{
	//ˮ��
	psb=createSoftBody(m_pos,btVector3(0,0,0));//���ô������巽��
	psb->generateClusters(32);
	psb->m_cfg.kDF=0;
}

btSoftBody*	SoftObj::createSoftBody(const btVector3& x,const btVector3& a)
{
	btSoftBody*	psb=btSoftBodyHelpers::CreateFromTriMesh(m_softBodyWorldInfo,m_gVertices,&m_gIndices[0],m_numsTriangles);//���������������巽��
	btSoftBody::Material*	pm=psb->appendMaterial();//��ȡ�������
	pm->m_kLST				=	1;//�������������Ϣ
	psb->generateBendingConstraints(2,pm);//��������ϵ��
	psb->m_cfg.piterations	=	2;
	psb->m_cfg.collisions	=	btSoftBody::fCollision::CL_SS+btSoftBody::fCollision::CL_RS;
	psb->setPose(true,true);

	psb->randomizeConstraints();//�����������ϵ��
	psb->rotate(btQuaternion(a[0],a[1],a[2]));//�����������ת�任
	psb->translate(x);//���������ƽ�Ʊ任
	psb->setTotalMass(0.1,true);
	psb->generateClusters(32);//��������Ⱥ����
	m_dynamicsWorld->addSoftBody(psb);//��������ӵ�����������
	return(psb);//��������ָ��
}

//SoftObj::~SoftObj() {
//	// TODO Auto-generated destructor stub
//}

void SoftObj::drawSelf(GLuint texIdIn)
{
	MatrixState::pushMatrix();//�����ֳ�

	btTransform trans;
	trans = psb->getWorldTransform();//��ȡ����ı任��Ϣ����
	trans.getOpenGLMatrix(MatrixState::currMatrix);//����ǰ�ľ������ø��任��Ϣ����

//	�����
	MatrixState::pushMatrix();//�����ֳ�
	initVertex();//���¶�������
	this->asbv->drawSelf(texIdIn);//������
	MatrixState::popMatrix();//�ָ��ֳ�

	MatrixState::popMatrix();//�ָ��ֳ�
}

btSoftBody* SoftObj::getBody()
{
	return psb;
}
btVector3* SoftObj::floatToBtVector3(float* vertex, int nums)//�������飬��������
{
	//��������ĳ���Ϊ nums/3
	btVector3* answer = new btVector3[nums / 3];
	for(int i = 0; i < nums / 3; i++)
	{
		answer[i].setValue(vertex[i*3+0],vertex[i*3+1],vertex[i*3+2]);
	}
	return answer;
}
float* SoftObj::btVector3ToFloat(btVector3* vertex, int nums)//�������飬��������
{
	//��������ĳ���Ϊ nums*3
	float* answer = new float[nums * 3];
	for(int i = 0; i < nums; i++)
	{
		answer[i*3+0] = vertex[i].x();
		answer[i*3+1] = vertex[i].y();
		answer[i*3+2] = vertex[i].z();
	}
	return answer;
}
