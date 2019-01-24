#ifndef _GameView_H_
#define _GameView_H_
#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
#include <jni.h>
#include "util/Constant.h"

#include "Bullet/LinearMath/btAlignedObjectArray.h"
#include "Bullet/btBulletDynamicsCommon.h"
#include "Bullet/BulletSoftBody/btSoftBody.h"
#include "Bullet/BulletSoftBody/btSoftBodyHelpers.h"
#include "Bullet/BulletSoftBody/btSoftRigidDynamicsWorld.h"
#include "Bullet/BulletSoftBody/btSoftBodySolvers.h"
#include "Bullet/BulletSoftBody/btDefaultSoftBodySolver.h"
#include "Bullet/BulletSoftBody/btSoftBodyRigidBodyCollisionConfiguration.h"

#include "MyBullet/TexBody.h"
#include "MyBullet/TextureRect.h"
#include "MyBullet/TexBall.h"
#include "MyBullet/Digital.h"
#include "MyBullet/SoftObj.h"

#include <vector>
#include <thread>

#define BIT(x) (1<<(x))//����꣬��ʾ1�����Ʋ���
enum collisiontypes {
	COL_NOTHING = 0, //<Collide with nothing//��ʾʲô������ײ
	COL_LEFT_WALL = BIT(1),//��ǽ
	COL_RIGHT_WALL = BIT(2),//��ǽ
	COL_DOWN_WALL = BIT(3),//��ǽ
	COL_FRONT_WALL = BIT(4),//ǰǽ
	COL_LANBAN_WALL = BIT(5),//����
	COL_LANKUANG_WALL = BIT(6),//����
	COL_LANWANG = BIT(7),//����
	COL_BALL = BIT(8)//��//��ʾ������ײ
};

class GameView
{
public:
	GameView();
	~GameView(){};
	//��ʼ���ý��������
	void initGameViewData();
	//����ʱ�򱻵���
	void onViewCreated(JNIEnv * env,jobject obj);
	void onViewchanged(int w, int h);
	//���Լ�
	void drawSelf();
	//���Ǳ��
	void drawDeshBoard();
	//��ʼ���������
	void initTextureRect();
	//��ʼ����ɫ��
	void initShader();
	//��ʼ������Id
	void initTextureId(JNIEnv * env,jobject obj);
	//�������λ��
	void callCamera();
	//�Ƕ�ת����
	float degreesToRadians(float angle);
	//��ʼ����������
	void initPhysicalWorld();
	//�����������
	void cleanVector();
	//������������
	void drawPhysicalWorld();
	//��ʼ��������������
	void initPhysicalWorldBody();
    //�����ص�����
    void onTouchBegan(float touchX,float touchY);
    void onTouchMoved(float touchX,float touchY);
    void onTouchEnded(float touchX,float touchY);
    //�߳�
    void threadTask();

    void ballControlUtil();//�ж������Ƿ����

    //���ư�������
    void onDrawShiping();

    //����ʱ����߳�
    void basketBallForHelpThread();

    int judgeMenuTouched(float touchX,float touchY);

    void playSound(int sound,int loop);
public:
	float ratio ;

	TexBody* downWall;
	TexBody* frontWall;
	TexBody* leftWall;
	TexBody* rightWall;
	TexBody* lanbanWall;
	TexBall* basketball_texBody[3];
	SoftObj* basketBallNet;
	TexBody* lanKuang;
	//����������Id
	GLuint lanwangTexId;

	GLuint frontTexId;		//ǰ����Id
	GLuint downTexId;		//������Id
	GLuint leftTexId;		//������Id
	GLuint rightTexId;		//������Id
	GLuint ballTexId;		//��������Id

	TextureRect* ybb;		//�Ǳ������Id
	GLuint shijianxiansBeijingId;		//��������Id

	GLuint lanbanTexId;		//�����Id
	GLuint lankuangId;		//�����Id
	GLuint shuziId;			//���������Id

	GLuint tingZTexId;		//ֹͣ�˵�������Id
	TextureRect* tingZTexRect;//ֹͣ�˵����������
	GLuint zhanTTexId;	//��ͣ�˵�����Id
	GLuint bofangTexId;	//���Ų˵�����Id

	TextureRect* BZWZTexRect;//���������������
	GLuint BZwenziTexId;		//�������ֵ�����Id
	float BZWZTexRect_Y;

	TextureRect* shouTexRect;//���������������
	GLuint shouTexId;		//��������Id

	//����
	float xAngle;
	float yAngle;
	//�����λ��
	float cx;
	float cy;
	float cz;
	//Ŀ���λ��
	float tx;
	float ty;
	float tz;
	//�����������
	//��ײ����---�ڹ��߸����׶γ��Բ�ͬ���㷨���
	btDefaultCollisionConfiguration* m_collisionConfiguration;
	//��ײ����㷨������---����������
	btCollisionDispatcher* m_dispatcher;
	//�׶���ײ���-----�����޳�û���໥���õ������
	btBroadphaseInterface* m_broadphase;
	//����������(solver),�������������Լ�����̣��õ�������һЩ����Լ������£��������������յ�λ�ã��ٶȵȡ�
	btConstraintSolver* m_solver;
	//��������
	btDynamicsWorld* m_dynamicsWorld;
	vector<TexBody*> tca;	//�洢�����װ������vector������ �����壬���壬���棩

	//������������
	btSoftBodyWorldInfo	m_softBodyWorldInfo;
	btSoftRigidDynamicsWorld*	m_softRigidDynamicsWorld;



	vector<TexBall*> tball_vector;	//�洢��������vector

	TexBall* curr_ball;//��ʱ���������---��ʱ������������

	//��¼��ʼ����ʱ��X,Y����
	float touchXOrigin ;
	float touchYOrigin ;
	//����
	Digital* digital;

	thread* t;
	bool threadFlag;
	//��������-��ʾ�淨
	thread* helpThred;
	//�̱߳�־λ
	bool helpThreadFlag;
	//�Ƿ���ͣ��������Ĳ���
	bool isnoPlay;

	bool isStart;//�Ƿ�ʼ������Ϸ������
	//�Ƿ�Ϊ����������ͣ
	bool isHelpPause;

    JNIEnv *env;
    jclass cls;
    jmethodID mid;

};

#endif
