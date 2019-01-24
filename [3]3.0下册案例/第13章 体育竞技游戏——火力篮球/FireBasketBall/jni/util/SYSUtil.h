#ifndef _SYSUtil_H_
#define _SYSUtil_H_

#include "../Bullet/BulletDynamics/Dynamics/btDynamicsWorld.h"
#include "../Bullet/BulletCollision/CollisionDispatch/btCollisionObject.h"
#include "BulletCollision/NarrowPhaseCollision/btPersistentManifold.h"
#include "../Bullet/LinearMath/btQuaternion.h"
#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
class SYSUtil
{
public:
	static int numManifolds;
	static btPersistentManifold* contactManifold;
	static int numContacts;
public:
	//����Ԫ��ת��Ϊ�Ƕȼ�ת������
	static float* fromSYStoAXYZ(btQuaternion q4);
	//�÷������ڼ�������������ײ
    static bool isCollided(btDynamicsWorld* dynamicsWorld,btCollisionObject* coA,btCollisionObject* coB);
};

#endif
