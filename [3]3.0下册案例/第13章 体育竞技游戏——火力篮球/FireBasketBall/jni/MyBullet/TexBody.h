#ifndef _TexBody_H_
#define _TexBody_H_

#include "Bullet/BulletDynamics/Dynamics/btRigidBody.h"
//������
class TexBody
{
public:
	//�鷽�����̳д������ʵ��---������Java�ĳ��󷽷�
	virtual void drawSelf() = 0;
	virtual btRigidBody* getBody() = 0;
};

#endif
