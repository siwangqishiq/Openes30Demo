#include "SYSUtil.h"
#include "math.h"
#include "util/Constant.h"
int SYSUtil::numManifolds;
btPersistentManifold* SYSUtil::contactManifold;
int SYSUtil::numContacts;

//����Ԫ��ת��Ϊ�Ƕȼ�ת������
float* SYSUtil::fromSYStoAXYZ(btQuaternion q4)
{
	float *result;
	double sitaHalf=acos(q4.w());
	float nx=(float) (q4.x()/sin(sitaHalf));
	float ny=(float) (q4.y()/sin(sitaHalf));
	float nz=(float) (q4.z()/sin(sitaHalf));
	float degrees = sitaHalf*2/Constant::PI*180;
	result = new float[4]{degrees,nx,ny,nz};
	return result;
}
//�÷������ڼ�������������ײ
bool SYSUtil::isCollided(btDynamicsWorld* dynamicsWorld,btCollisionObject* coA,btCollisionObject* coB)
{
	numManifolds = dynamicsWorld->getDispatcher()->getNumManifolds();
	for(int i=0;i<numManifolds;i++)
	{
		contactManifold = dynamicsWorld->getDispatcher()->getManifoldByIndexInternal(i);
		numContacts = contactManifold->getNumContacts();
		if(numContacts > 0)
		{
			btCollisionObject *obA = (btCollisionObject*)contactManifold->getBody0();
			btCollisionObject *obB = (btCollisionObject*)contactManifold->getBody1();

			if((coA==obA&&coB==obB)||(coA==obB&&coB==obA))
			{
				return true;
			}
		}
	}
	return false;
}
