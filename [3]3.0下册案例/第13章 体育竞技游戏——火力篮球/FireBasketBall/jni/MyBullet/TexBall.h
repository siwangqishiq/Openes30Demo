#ifndef TexBall__h
#define TexBall__h

#include "Bullet/btBulletDynamicsCommon.h"
#include "Bullet/LinearMath/btVector3.h"
#include "Bullet/BulletDynamics/Dynamics/btRigidBody.h"
#include "util/MatrixState.h"
#include "MyBullet/Ball.h"

class TexBall {
    float r;//��ʾ��İ뾶
	btRigidBody *body;//��ʾ�����ָ��
	int texId;//������id
	Ball* ball;//������ָ��
public:

	TexBall(float r,
			int texId,
			btDynamicsWorld *dynamicsWorld,
			btScalar mass,
			btVector3 pos,
			btScalar restitutionIn,
			btScalar frictionIn);
    void drawSelf(int ballTexId,int isShadow,int planeId,int isLanbanYy);//�����������ķ���
	btRigidBody* getBody();//��ȡ�������ָ��ķ���
	int ballState;//1��ʾ���������ϲ��������˶�.���������Ϊ0
	//�����X�ٶ�״̬
	int isnoLanBan;//�Ƿ��������ǰһ����ײ�ˣ�0��ʾû����ײ
};

#endif
