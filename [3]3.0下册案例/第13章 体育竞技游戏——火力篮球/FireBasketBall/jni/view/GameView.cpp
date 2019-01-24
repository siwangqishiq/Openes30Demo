#include "GameView.h"
#include "util/MatrixState.h"

#include "util/ShaderManager.h"
#include <math.h>

#include "util/Constant.h"

#include "MyBullet/TexCuboid.h"
#include "MyBullet/TexTriangleMesh.h"
#include <iostream>
#include <stdlib.h>
#include "util/SYSUtil.h"

#include "SQLiteUtil/SQLiteUtil.h"


using namespace std;
#include "FireBasketBallcpp/main.h"

GameView::GameView()
{

}
//��ʼ���ý��������
void GameView::initGameViewData()
{
	//����
	xAngle=0.0f;
	yAngle=0.0f;
	//�����λ��
	cx = Constant::CAMERA_X;
	cy = Constant::CAMERA_Y;
	cz = Constant::CAMERA_Z;
	//Ŀ���λ��
	tx = 0;
	ty = Constant::CAMERA_Y;
	tz = 0;
	callCamera();
	//��ʼ����ǰ���е�����Ϊ��
	curr_ball = NULL;
	threadFlag = true;
	Constant::GAMEOVER = false;
	BZWZTexRect_Y = -1.5;
	isStart=false;//�Ƿ�ʼ������Ϸ������
	//��������-��ʾ�淨
	helpThreadFlag = true;
	isnoPlay = true;
	//�Ƿ�Ϊ����������ͣ
	isHelpPause = false;
	Constant::DIGITAL_SCORE = 0;
	Constant::DIGITAL_TIME = 0;
	Constant::DIGITAL_TOTAL_TIME = 60;
	int ballIndex =0;
	std::vector<TexBall*>::iterator ballIterator ;
	for(ballIterator = tball_vector.begin();ballIterator!=tball_vector.end();ballIterator++)
	{
		btTransform btt ;
		btt.setIdentity();
		btt.setOrigin(btVector3(Constant::STARTBALL[ballIndex][0],Constant::STARTBALL[ballIndex][1],Constant::STARTBALL[ballIndex][2]));
		(*ballIterator)->getBody()->setCenterOfMassTransform(btt);
		(*ballIterator)->getBody()->activate(false);
		(*ballIterator)->getBody()->clearForces();
		ballIndex++;
	}
	{
		t = new thread(&GameView::threadTask,this);
	}
}
//������
void GameView::onViewCreated(JNIEnv * env,jobject obj)
{
	//���ñ�����ɫ
	glClearColor(0.5f, 0.5f, 0.5f, 1);
    //��ʼ����Դλ��
    MatrixState::setLightLocation(3, 7, 5);
	//��ʼ���任����
    MatrixState::setInitStack();
}

//���ı�
void GameView::onViewchanged(int w, int h)
{
	//�����ӿ�
    glViewport(0, 0, w, h);
    //�������
    ratio = (float) w/h;
    //�򿪱������
    glEnable(GL_CULL_FACE);
    glEnable(GL_DEPTH_TEST);

}
//���Լ�
void GameView::drawSelf()
{
    //�򿪱������
    glEnable(GL_CULL_FACE);
    glEnable(GL_DEPTH_TEST);
	//���ñ�����ɫ
	glClearColor(0.5f, 0.5f, 0.5f, 1);
    //��ʼ����Դλ��
    MatrixState::setLightLocation(3, Constant::CHANGJING_HEIGHT*1.7f, 5);
	//��ʼ���任����
    MatrixState::setInitStack();
	//�建��
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    //���ô˷����������͸��ͶӰ����
    MatrixState::setProjectFrustum(-ratio, ratio, -1, 1, 4.0f, 100);
    //���λ��(0,3.15,14.9)Ŀ��λ��������(0,3.15,0)
    MatrixState::setCamera(cx, cy, cz, tx, ty,tz, 0, 1, 0);
    //��������
    MatrixState::pushMatrix();
    //�������
    glEnable(GL_BLEND);
    //���û������
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    //������������
    drawPhysicalWorld();

    //�����Ǳ��
    //�رջ��
    glDisable(GL_BLEND);
    //����������
    onDrawShiping();
    drawDeshBoard();

    MatrixState::popMatrix();
    if(!isStart)
    {
    	isStart = true;
    	if(Constant::isnoHelpView)
		{
			//���ٶ�
			float vx = Constant::STARTBALL_V[1][0];
			float vy = Constant::STARTBALL_V[1][1];
			float vz = Constant::STARTBALL_V[1][2];

			tball_vector.at(1)->getBody()->activate(true);
			tball_vector.at(1)->getBody()->setLinearVelocity(btVector3(vx,vy,vz));
			tball_vector.at(1)->getBody()->setAngularVelocity(btVector3(5,0,0));
			//����һ���߳�---��ʾ�淨
			helpThred = new thread(&GameView::basketBallForHelpThread,this);
		}
    }
}
void GameView::drawPhysicalWorld()
{
	MatrixState::pushMatrix();
    for ( int i = 0; i < tca.size(); ++i )
    {
    	tca[i]->drawSelf();//�ص�����Ļ��Ʒ���
    }
    for(int i=0;i<3;i++)
    {
    	basketball_texBody[i]->drawSelf(ballTexId,0,0,0);	//����������
    }
    for(int i=0;i<3;i++)
	{
		basketball_texBody[i]->drawSelf(ballTexId,1,0,0);	//���Ƶ�����Ӱ
		basketball_texBody[i]->drawSelf(ballTexId,1,3,0);	//���Ʊ���ǽ�����Ӱ
		basketball_texBody[i]->drawSelf(ballTexId,1,4,1);	//����������Ӱ
	}
    glDisable  (GL_CULL_FACE);
    //�������
    glEnable(GL_BLEND);
    //���û������
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    MatrixState::pushMatrix();
    basketBallNet->drawSelf(this->lanwangTexId);
    MatrixState::popMatrix();
    glDisable(GL_BLEND);
    glEnable(GL_CULL_FACE);

    MatrixState::popMatrix();
}

void GameView::initPhysicalWorldBody()
{
	int ballCollidesWith = COL_LEFT_WALL | COL_RIGHT_WALL | COL_DOWN_WALL | COL_FRONT_WALL | COL_LANBAN_WALL | COL_LANKUANG_WALL | COL_LANWANG ;	//����������ײ
	int lanBanCollidesWith = COL_BALL ;		//����ֻ������ײ
	int lanKuangCollidesWith = COL_BALL;	//����ֻ������ײ
	int lanWangCollidesWith = COL_LEFT_WALL | COL_RIGHT_WALL | COL_DOWN_WALL | COL_BALL;	//����ֻ������ײ

	int downWallCollidesWith = COL_BALL | COL_LANWANG;	//����ֻ������ײ
	int leftWallCollidesWith = COL_BALL | COL_LANWANG;	//����ֻ������ײ
	int rightWallCollidesWith = COL_BALL | COL_LANWANG;	//����ֻ������ײ
	int frontWallCollidesWith = COL_BALL | COL_LANWANG;	//����ֻ������ײ

	//����
	TexBody* texBody;
	cleanVector();

	{
		downWall = new TexCuboid(
				m_softRigidDynamicsWorld,//�����������
				btVector3(Constant::CHANGJING_WIDTH/2,0.01,Constant::CHANGJING_LENGTH/2),//������İ�����
				0.0f,//�����������
				btVector3(0,0,0),//�����λ��
				0.75f,//�ָ�ϵ��
				0.8f,//Ħ��ϵ��
				1,//��λ����
				downTexId,downTexId,downTexId,
				downTexId,downTexId,downTexId
		);
		m_softRigidDynamicsWorld->removeRigidBody(downWall->getBody());
		m_softRigidDynamicsWorld->addRigidBody(downWall->getBody(),COL_DOWN_WALL,downWallCollidesWith);
		//������������뵽�б���
		tca.push_back(downWall);

		frontWall = new TexCuboid(
				m_softRigidDynamicsWorld,//�����������
				btVector3(Constant::CHANGJING_WIDTH/2,(Constant::CHANGJING_HEIGHT-0.2)/2,0.01),//������İ�����
				0.0f,//�����������
				btVector3(0,(Constant::CHANGJING_HEIGHT-0.2)/2,-Constant::CHANGJING_LENGTH/2),//�����λ��
				0.2f,//�ָ�ϵ��
				0.8f,//Ħ��ϵ��
				1,//��λ����
				frontTexId,frontTexId,frontTexId,
				frontTexId,frontTexId,frontTexId
		);
		m_softRigidDynamicsWorld->removeRigidBody(frontWall->getBody());
		m_softRigidDynamicsWorld->addRigidBody(frontWall->getBody(),COL_FRONT_WALL,frontWallCollidesWith);

		//������������뵽�б���
		tca.push_back(frontWall);

		texBody = new TexCuboid(
				m_softRigidDynamicsWorld,//�����������
				btVector3(Constant::CHANGJING_WIDTH/2,3.5,0.01),//������İ�����
				0.0f,//�����������
				btVector3(0,3.5,2),//�����λ��
				0.0f,//�ָ�ϵ��
				0.0f,//Ħ��ϵ��
				1,//��λ����
				0,0,0,
				0,0,0
		);
		//������������뵽�б���
		tca.push_back(texBody);

		leftWall = new TexCuboid(
				m_softRigidDynamicsWorld,//�����������
				btVector3(0.01,(Constant::CHANGJING_HEIGHT-0.2)/2,Constant::CHANGJING_LENGTH/2),//������İ�����
				0.0f,//�����������
				btVector3(-Constant::CHANGJING_WIDTH/2,(Constant::CHANGJING_HEIGHT-0.2)/2,0),//�����λ��
				0.2f,//�ָ�ϵ��
				0.8f,//Ħ��ϵ��
				1,//��λ����
				leftTexId,leftTexId,leftTexId,
				leftTexId,leftTexId,leftTexId
		);
		m_softRigidDynamicsWorld->removeRigidBody(leftWall->getBody());
		m_softRigidDynamicsWorld->addRigidBody(leftWall->getBody(),COL_LEFT_WALL,leftWallCollidesWith);

		//������������뵽�б���
		tca.push_back(leftWall);

		rightWall = new TexCuboid(
				m_softRigidDynamicsWorld,//�����������
				btVector3(0.01,(Constant::CHANGJING_HEIGHT-0.2)/2,Constant::CHANGJING_LENGTH/2),//������İ�����
				0.0f,//�����������
				btVector3(Constant::CHANGJING_WIDTH/2,(Constant::CHANGJING_HEIGHT-0.2)/2,0),//�����λ��
				0.2f,//�ָ�ϵ��
				0.8f,//Ħ��ϵ��
				1,//��λ����
				rightTexId,rightTexId,rightTexId,
				rightTexId,rightTexId,rightTexId
		);
		m_softRigidDynamicsWorld->removeRigidBody(rightWall->getBody());
		m_softRigidDynamicsWorld->addRigidBody(rightWall->getBody(),COL_RIGHT_WALL,rightWallCollidesWith);

		//������������뵽�б���
		tca.push_back(rightWall);

		lanbanWall = new TexCuboid(
				m_softRigidDynamicsWorld,//�����������
				btVector3(1.05,0.8,0.02),//������İ�����
				0.0f,//�����������
				btVector3(0,Constant::LANBAN_Y,Constant::LANBAN_Z),//�����λ��
				0.5f,//�ָ�ϵ��
				0.8f,//Ħ��ϵ��
				1,//��λ����
				lanbanTexId,lanbanTexId,lanbanTexId,
				lanbanTexId,lanbanTexId,lanbanTexId
		);
		m_softRigidDynamicsWorld->removeRigidBody(lanbanWall->getBody());
		m_softRigidDynamicsWorld->addRigidBody(lanbanWall->getBody(),COL_LANBAN_WALL,lanBanCollidesWith);
		//������������뵽�б���
		tca.push_back(lanbanWall);

		lanKuang = new TexTriangleMesh(
			0.0f,//����
			TexTriangleMesh::vertices_yuanhuan,
			TexTriangleMesh::numsVer_yuanhuan ,//������������ĳ���
			TexTriangleMesh::indices_yuanhuan ,
			TexTriangleMesh::numsInd_yuanhuan ,
			TexTriangleMesh::texs_yuanhuan ,
			TexTriangleMesh::numsTex_yuanhuan ,
			TexTriangleMesh::normal_yuanhuan ,
			TexTriangleMesh::numNormal_yuanhuan ,
			1.0f,//��������
			btVector3(Constant::LANKUANG_X,Constant::LANKUANG_Y,Constant::LANKUANG_Z),//λ��
			0.5f,//�ָ�ϵ��
			0.5f,//Ħ��ϵ��
			m_softRigidDynamicsWorld,//��������ָ��
			lankuangId//����������id
			);
		m_softRigidDynamicsWorld->removeRigidBody(lanKuang->getBody());
		m_softRigidDynamicsWorld->addRigidBody(lanKuang->getBody(),COL_LANKUANG_WALL,lanKuangCollidesWith);

		tca.push_back(lanKuang);//������������뵽�б���
	}
	{
		basketBallNet = new SoftObj(
			m_softRigidDynamicsWorld,
			m_softBodyWorldInfo,
			btVector3(Constant::LANKUANG_X,Constant::LANWANG_H,Constant::LANKUANG_Z),
//			btVector3(0,0,0),
			SoftObj::vertices_soft,
			SoftObj::numsVer_soft,
			SoftObj::indices_soft,
			SoftObj::numsInd_soft,
			SoftObj::texs_soft,
			SoftObj::numsTex_soft,
			1.0f
			);
		m_softRigidDynamicsWorld->removeSoftBody(basketBallNet->getBody());
		m_softRigidDynamicsWorld->addSoftBody(basketBallNet->getBody(),COL_LANWANG,lanWangCollidesWith);
		//�̶�һЩ��
		for(int i=1;i<=24;i++)
		{
			basketBallNet->getBody()->appendAnchor(basketBallNet->getBody()->m_nodes.size()-i,lanKuang->getBody());
		}
	}
	{
		for(int i=0;i<3;i++)
		{
			//����
			basketball_texBody[i]=new TexBall(Constant::BASKETBALL_R,ballTexId,m_softRigidDynamicsWorld,5.0,
					btVector3(Constant::STARTBALL[i][0],Constant::STARTBALL[i][1],Constant::STARTBALL[i][2]),
					0.75, 0.5);
			m_softRigidDynamicsWorld->removeRigidBody(basketball_texBody[i]->getBody());
			m_softRigidDynamicsWorld->addRigidBody(basketball_texBody[i]->getBody(),COL_BALL,ballCollidesWith);
			//��ŵ������б���
			tball_vector.push_back(basketball_texBody[i]);
		}
	}
}
void GameView::threadTask()
{
    //Attach���߳�
    if((*main::g_jvm).AttachCurrentThread(&env, NULL) != JNI_OK)
    {
    	return ;
    }
    cls = env->GetObjectClass(main::g_obj);

    //�ٻ�����еķ���
    mid = env->GetStaticMethodID(cls,"shengyinBoFang","(II)V");

	while(threadFlag)
	{
		if(Constant::VIEW_CUR_GL_INDEX != Constant::VIEW_GAME_INDEX)
		{
			threadFlag = false;
		     //Detach���߳�
		     if((*main::g_jvm).DetachCurrentThread() != JNI_OK)
		     {
		     }
		}
	    m_softRigidDynamicsWorld->stepSimulation(1.0/60,5);//��������ģ�����
		ballControlUtil();//�ж������Ƿ����

		if(!Constant::GAMEOVER&&!Constant::isnoHelpView)
		{
			Constant::DIGITAL_TIME += 10;
		}
		if(Constant::DIGITAL_TIME>=Constant::DIGITAL_TOTAL_TIME*1000)
		{
			//�����߳�
			threadFlag = false;
			Constant::GAMEOVER = true;
			//������Ϸ��¼
			std::string sSQL1 = "INSERT INTO paihangbang VALUES('";
			//�÷�
			char str[10];
			//����ת�ַ���
			sprintf(str,"%d",Constant::DIGITAL_SCORE);
			sSQL1 = sSQL1 + str ;
		   	std::string sSQL2 = "','";

		   	//��ǰʱ��
		   	time_t tt = time(NULL);
		   	//��䷵�ص�ֻ��һ��ʱ��cuo
		   	tm* t= localtime(&tt);

		   	int yearTemp = t->tm_year + 1900;
		   	int monthTemp =t->tm_mon + 1;
		   	int dayTemp =t->tm_mday;
		   	int hourTemp = t->tm_hour;
		   	int minTemp = t->tm_min;
		   	int secTemp = t->tm_sec;
		   	//ʱ������
		   	char strTime[20];
			//����ת�ַ���
			sprintf(strTime,"%d-%02d %02d:%02d:%02d",
					monthTemp,
					dayTemp,
					hourTemp,
					minTemp,
					secTemp);
		   	sSQL2 = sSQL2 + strTime;
		   	std::string sSQL3 = "')";;
			std::string sSQL = sSQL1 + sSQL2 + sSQL3;
			//����÷ֺ�ʱ���¼
		    SQLiteUtil::doSqlite3_exec(sSQL);
			//���ò˵�����
			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_MENU_INDEX;
			//��ǰ��ʾ��Ϸ��������
			Constant::MENU_CURR_MENUINDEX = Constant::MENU_GAMEVIEW_END;

			if(!basketball_texBody[0]->getBody()->isActive()&&!basketball_texBody[1]->getBody()->isActive()&&!basketball_texBody[2]->getBody()->isActive())
			{
				threadFlag = false;//�ֳ�ֹͣ
			}
			playSound(2,0);
		     //Detach���߳�
		     if((*main::g_jvm).DetachCurrentThread() != JNI_OK)
		     {
		     }else
		     {
		     }
		 }
		std::this_thread::sleep_for(std::chrono::milliseconds(10));
	}
}
void GameView::playSound(int sound,int loop)
{
  if(mid != NULL && Constant::isOpen_CJYY)
	{
    	env->CallStaticVoidMethod(cls, mid,(jint)sound,(jint)loop);
	}
}
void GameView::ballControlUtil()//������Ҫ�����ڶ�����������沿�ֽ��в���
{
	std::vector<TexBall*>::iterator ballIterator ;
	for(ballIterator = tball_vector.begin();ballIterator!=tball_vector.end();ballIterator++)
	{
		//����������˶���,��ôÿһ�θ��ذ���ײ��ʱ�����������һ����Z��������ĳ���
		if((*ballIterator)->getBody()->isActive()&&SYSUtil::isCollided(m_softRigidDynamicsWorld,(*ballIterator)->getBody(),downWall->getBody()))
		{
			(*ballIterator)->getBody()->applyForce(btVector3(0,0,60),btVector3(0,0,0));
		}

		//��ȡ������˶����
		btTransform transform;
		(*ballIterator)->getBody()->getMotionState()->getWorldTransform(transform);//------------------------------�����д�------error

		float position_X = transform.getOrigin().x();
		float position_Y = transform.getOrigin().y();
		float position_Z = transform.getOrigin().z();
		//��ȡ������ٶȺ���ת
		btVector3 linearVelocity = (*ballIterator)->getBody()->getLinearVelocity();
		btVector3 angularVelocity = (*ballIterator)->getBody()->getAngularVelocity();
		//���������Ľ�����д���
		//�����ȡ���������λ������ֵ
		float lankuang_X = Constant::LANKUANG_X;
		float lankuang_Y = Constant::LANKUANG_Y;
		float lankuang_Z = Constant::LANKUANG_Z;

		float lankuang_Radius = Constant::LANKUANG_R;
		float ball_Radius = Constant::BASKETBALL_R;
		//�������պ�λ��������
		float temp_distance = (float)sqrt((position_X-lankuang_X)*(position_X-lankuang_X)+(position_Z-lankuang_Z)*(position_Z-lankuang_Z));
		if(linearVelocity.y()<0&&temp_distance<(lankuang_Radius-ball_Radius)&&position_Y>lankuang_Y)
		{
			(*ballIterator)->ballState = 1;
		}

		if((*ballIterator)->ballState==1
							&&position_Y<lankuang_Y
							&&position_X>lankuang_X-Constant::LANKUANG_R&&position_X<lankuang_X+Constant::LANKUANG_R&&
							position_Z>lankuang_Z-Constant::LANKUANG_R&&position_Z<lankuang_Z+Constant::LANKUANG_R
					)
		{
			Constant::DIGITAL_SCORE++;
			//��Ҫ����������
			playSound(3,0);
			(*ballIterator)->ballState = 0;
		}

		btVector3 vt = (*ballIterator)->getBody()->getLinearVelocity();
		if((vt.y()>1.0f&&vt.y()<6.5f)&&SYSUtil::isCollided(m_softRigidDynamicsWorld,(*ballIterator)->getBody(),downWall->getBody()))//�ж��Ƿ�͵���ײ
		{
			playSound(1,0);
		}else if
		(
				vt.x()>1.0f&&
				SYSUtil::isCollided(m_softRigidDynamicsWorld,(*ballIterator)->getBody(),leftWall->getBody())
		)
		{//����
			//����ײǽ����
			playSound(1,0);
		}
		else if
		(		vt.x()<-1&&
				SYSUtil::isCollided(m_softRigidDynamicsWorld,(*ballIterator)->getBody(),rightWall->getBody())
		)
		{//����
			//����ײǽ����
			playSound(1,0);
		}
		else if
		(
				vt.z()>1.0&&
				SYSUtil::isCollided(m_softRigidDynamicsWorld,(*ballIterator)->getBody(),frontWall->getBody())
		)
		{//����
			//����ײǽ����
			playSound(1,0);
		}

		if(SYSUtil::isCollided(m_softRigidDynamicsWorld,(*ballIterator)->getBody(),lanbanWall->getBody()))
		{
			if((abs(vt.x())+abs(vt.y())+abs(vt.z()))>2)
			{
				//��������
				playSound(1,0);
			}
			//������ǰ����ײ��
			(*ballIterator)->isnoLanBan=1;
		}else
		{
			(*ballIterator)->isnoLanBan=0;
		}
	}
}
void GameView::cleanVector()
{
	tca.clear();
}

//���Ǳ��
void GameView::drawDeshBoard()
{
	MatrixState::pushMatrix();
    //�������
    glEnable(GL_BLEND);
    //���û������
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	//����Ϊ����ͶӰ
    MatrixState::setProjectOrtho(-this->ratio, this->ratio, -1, 1, 1, 10);
    MatrixState::setCamera(0, 0, 5, 0, 0,0, 0, 1, 0);

    glEnable(GL_DEPTH_TEST);

	MatrixState::pushMatrix();
    MatrixState::translate(0, 0.85, 0);
	//���ƾ���
    ybb->drawSelf(shijianxiansBeijingId);  //���Ʊ���
    MatrixState::popMatrix();

    MatrixState::pushMatrix();
    MatrixState::translate(-0.35, 0.80, 0.1);
    digital->drawSelf(Constant::DIGITAL_SCORE,shuziId);
    MatrixState::popMatrix();

    MatrixState::pushMatrix();
    MatrixState::translate(0.35, 0.80, 0.1);
    digital->drawSelf(Constant::DIGITAL_TOTAL_TIME-(Constant::DIGITAL_TIME/1000),shuziId);
    MatrixState::popMatrix();

    glDisable(GL_BLEND);
    glDisable(GL_DEPTH_TEST);
    MatrixState::popMatrix();
}
void GameView::initTextureId(JNIEnv * env,jobject obj)
{
	jclass cl = env->FindClass("com/bn/bullet/GL2JNIView");
	jmethodID id = env->GetStaticMethodID(cl,"initTextureRepeat","(Landroid/opengl/GLSurfaceView;Ljava/lang/String;)I");

	jstring name = env->NewStringUTF("pic/swall1.jpg");
	leftTexId = env->CallStaticIntMethod(cl,id,obj,name);

	name = env->NewStringUTF("pic/swall2.jpg");
	frontTexId = env->CallStaticIntMethod(cl,id,obj,name);

	name = env->NewStringUTF("pic/floor.png");
	downTexId = env->CallStaticIntMethod(cl,id,obj,name);

	name = env->NewStringUTF("pic/swall3.jpg");
	rightTexId = env->CallStaticIntMethod(cl,id,obj,name);

	name = env->NewStringUTF("pic/yibiaoban.png");
	shijianxiansBeijingId = env->CallStaticIntMethod(cl,id,obj,name);

	name = env->NewStringUTF("pic/basketball.jpg");
	ballTexId = env->CallStaticIntMethod(cl,id,obj,name);

	name = env->NewStringUTF("pic/lanban.png");
	lanbanTexId = env->CallStaticIntMethod(cl,id,obj,name);

	name = env->NewStringUTF("pic/lankuang.png");
	lankuangId = env->CallStaticIntMethod(cl,id,obj,name);

	name = env->NewStringUTF("pic/number.png");
	shuziId = env->CallStaticIntMethod(cl,id,obj,name);

	name = env->NewStringUTF("pic/wenzi.png");
	BZwenziTexId = env->CallStaticIntMethod(cl,id,obj,name);

	name = env->NewStringUTF("pic/shou.png");
	shouTexId = env->CallStaticIntMethod(cl,id,obj,name);

	//ֹͣ����Id
	name = env->NewStringUTF("pic/stop.png");
	tingZTexId = env->CallStaticIntMethod(cl,id,obj,name);

	//��ͣ����Id
	name = env->NewStringUTF("pic/pause.png");
	zhanTTexId = env->CallStaticIntMethod(cl,id,obj,name);

	//���Ų˵�����Id
	name = env->NewStringUTF("pic/play.png");
	bofangTexId = env->CallStaticIntMethod(cl,id,obj,name);

	//��������Id
	name = env->NewStringUTF("pic/basketnet.png");
	lanwangTexId = env->CallStaticIntMethod(cl,id,obj,name);
}
//��ʼ��Object
void GameView::initTextureRect()
{
	//�������3D�����л�ӭ��ӭ�����е�shader
	ShaderManager::compileShader();
	//������־��
	ybb = new TextureRect(Constant::RADIO,Constant::YBB_HEIGHT/2,0,1,ShaderManager::getCommTextureShaderProgram(),NULL);	//���������ζ���

	BZWZTexRect = new TextureRect(0.4,0.4,0,1,ShaderManager::getCommTextureShaderProgram(),NULL);	//���������ζ���

	digital = new Digital(ShaderManager::getCommTextureShaderProgram());

	shouTexRect = new TextureRect(0.1,0.1,0,1,ShaderManager::getCommTextureShaderProgram(),NULL);	//���������ζ���

	tingZTexRect = new TextureRect(Constant::MENU_STOP_PAUSE_WIDTH_HALF,Constant::MENU_STOP_PAUSE_HEIGHT_HALF,0,1,ShaderManager::getCommTextureShaderProgram(),NULL);	//���������ζ���

}
void GameView::callCamera()
{
	//�����������λ��
    cx=(float)(tx+cos(degreesToRadians(xAngle))*
    		sin(degreesToRadians(yAngle))*Constant::DISTANCE);//�����x����
    cz=(float)(tz+cos(degreesToRadians(xAngle))*
    		cos(degreesToRadians(yAngle))*Constant::DISTANCE);//�����z����
    cy=(float)(ty+sin(degreesToRadians(xAngle))*Constant::DISTANCE);//�����y����
}
float GameView::degreesToRadians(float angle)
{
	float result = angle/180*Constant::PI;
	return result;
}
//��ʼ����������
void GameView::initPhysicalWorld()
{
	//������ײ����---�ڹ��߸����׶γ��Բ�ͬ�㷨�����
	m_collisionConfiguration = new btDefaultCollisionConfiguration();
	//�������ȶ���
	m_dispatcher = new btCollisionDispatcher(m_collisionConfiguration);
	//�����׶���ײ���---�޳�û�л������õ������
	m_broadphase = new btSimpleBroadphase();
	//����������----���Լ������
	btSequentialImpulseConstraintSolver* sol = new btSequentialImpulseConstraintSolver();
	m_solver = sol;
	//������������
	m_dynamicsWorld = new btDiscreteDynamicsWorld(m_dispatcher,m_broadphase,m_solver,m_collisionConfiguration);
	//�����������ٶ�
	m_dynamicsWorld->setGravity(btVector3(0,-10,1));

	//����
	m_softBodyWorldInfo = btSoftBodyWorldInfo();

	//������ײ���������Ϣ����
	btSoftBodyRigidBodyCollisionConfiguration *collisionConfiguration = new btSoftBodyRigidBodyCollisionConfiguration();
	//������ײ����㷨�����߶����书��Ϊɨ�����е���ײ���ԣ���ȷ�����õļ����Զ�Ӧ���㷨
	btCollisionDispatcher* dispatcher = new btCollisionDispatcher(collisionConfiguration);
	m_softBodyWorldInfo.m_dispatcher = dispatcher;
	//����������������ı߽���Ϣ
	btVector3 worldAabbMin = btVector3(-10000, -10000, -10000);
	btVector3 worldAabbMax = btVector3(10000, 10000, 10000);
	int maxProxies = 1024;
	//������ײ���ֲ�׶εļ����㷨����
	btAxisSweep3 *overlappingPairCache =new btAxisSweep3(worldAabbMin, worldAabbMax, maxProxies);
	m_softBodyWorldInfo.m_broadphase = overlappingPairCache;
	//�����ƶ�Լ������߶���
	btSequentialImpulseConstraintSolver *solver = new btSequentialImpulseConstraintSolver();
	btSoftBodySolver* softBodySolver = new btDefaultSoftBodySolver();
	m_softBodyWorldInfo.m_gravity.setValue(0,-10,0);
	m_softBodyWorldInfo.m_sparsesdf.Initialize();
//		m_softBodyWorldInfo.m_sparsesdf.Reset();
	m_softBodyWorldInfo.air_density		=	(btScalar)0.5;
//		m_softBodyWorldInfo.water_density	=	0;
//		m_softBodyWorldInfo.water_offset	=	0;
//		m_softBodyWorldInfo.water_normal	=	btVector3(0,0,0);

	//���������������
	m_softRigidDynamicsWorld = new btSoftRigidDynamicsWorld(dispatcher, overlappingPairCache, solver,collisionConfiguration,softBodySolver);
	//�����������ٶ�
	btVector3 gvec = btVector3(0, -10, 0);
	m_softRigidDynamicsWorld->setGravity(gvec);

}
//���ư�������
void GameView::onDrawShiping()
{
	if(!Constant::isnoHelpView)
	{
		return ;
	}

	MatrixState::pushMatrix();
	//����Ϊ����ͶӰ
    MatrixState::setProjectOrtho(-this->ratio, this->ratio, -1, 1, 1, 100);
    MatrixState::setCamera(0, 0, 5, 0, 0,1, 0, 1, 0);
    //�������
    glEnable(GL_BLEND);
    //���û������
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    //�ر���Ȳ���
    glDisable(GL_DEPTH_TEST);

    MatrixState::pushMatrix();
    MatrixState::translate(-0.48, -0.9, 0);
    tingZTexRect->drawSelf(tingZTexId);
    MatrixState::popMatrix();

    MatrixState::pushMatrix();
    MatrixState::translate(0.48, -0.9, 0);
	if(!isHelpPause)
	{
		tingZTexRect->drawSelf(zhanTTexId);
	}else
	{
		tingZTexRect->drawSelf(bofangTexId);
	}

    MatrixState::popMatrix();

	MatrixState::pushMatrix();
	MatrixState::translate(Constant::shouX, Constant::shouY, 0.8);
	shouTexRect->drawSelf(shouTexId);
	MatrixState::popMatrix();

	MatrixState::pushMatrix();
	if(!isHelpPause)
	{
		BZWZTexRect_Y = BZWZTexRect_Y + 0.001;
	}
	MatrixState::translate(0, BZWZTexRect_Y, 0);
	BZWZTexRect->drawSelf(BZwenziTexId);
	MatrixState::popMatrix();

	//������Ȳ���
	glEnable(GL_DEPTH_TEST);
    //�رջ��
    glDisable(GL_BLEND);
    MatrixState::popMatrix();
}
//�����ص�����
void GameView::onTouchBegan(float touchX,float touchY)
{
	if(Constant::GAMEOVER)
	{
		return ;
	}
	this->touchXOrigin = touchX;
	this->touchYOrigin = touchY;

	//��Ļx���굽�ӿ�x����
	float tx = Constant::fromScreenXToNearX(this->touchXOrigin);
	//��Ļy���굽�ӿ�y����
	float ty = Constant::fromScreenYToNearY(this->touchYOrigin);

	//������תΪ3D�ռ��е�x,y
	float x3d = Constant::CHANGJING_WIDTH*((touchXOrigin)/Constant::SCREEN_WIDTH) - 0.5*Constant::CHANGJING_WIDTH;

	float y3d = Constant::CHANGJING_HEIGHT*(Constant::SCREEN_HEIGHT-(touchYOrigin))/Constant::SCREEN_HEIGHT;
	std::vector<TexBall*>::iterator ballIterator ;
	for(ballIterator = tball_vector.begin();ballIterator!=tball_vector.end();ballIterator++)
	{
		//��ȡ�����λ��
		float ball_X = (*ballIterator)->getBody()->getWorldTransform().getOrigin().getX();
		float ball_Y = (*ballIterator)->getBody()->getWorldTransform().getOrigin().getY();
		float ball_Z = (*ballIterator)->getBody()->getWorldTransform().getOrigin().getZ();

		//����뾶�Ŵ�1.5�������㴥��
		float ball_scale = 1.5f*Constant::BASKETBALL_R;

		if(x3d<ball_X+ball_scale&&x3d>ball_X-ball_scale
				&&y3d<ball_Y+ball_scale&&y3d>ball_Y-ball_scale
				&&ball_Z>1.55f
				)
		{
			//��¼�����������
			curr_ball = (*ballIterator);
			break;
		}
	}
	//���û��ѡ����
	if(curr_ball == NULL)
	{

	}
	if(Constant::isnoHelpView)
	{
		//��ȡ�ĸ�������β˵���ѡ��
		int result = judgeMenuTouched(tx,ty);
		//��¼��ǰѡ�еĲ˵�
		Constant::TOUCHED_CURR_MENU = result;
	}
}
void GameView::onTouchMoved(float touchX,float touchY){}
void GameView::onTouchEnded(float touchX,float touchY)
{
	//��Ļx���굽�ӿ�x����
	float tx = Constant::fromScreenXToNearX(touchX);
	//��Ļy���굽�ӿ�y����
	float ty = Constant::fromScreenYToNearY(touchY);

	//��ֵ
	float dx = (touchX - touchXOrigin);
	//���Ĵ���
	float max_fingerTouch = 110*0.9*Constant::SCREEN_HEIGHT_SCALE;
	float dy=(touchY-touchYOrigin)>0?0:((touchY-touchYOrigin)<-max_fingerTouch?-max_fingerTouch:(touchY-touchYOrigin));//Y�����ϵ��ƶ�����
	if(curr_ball != NULL)//������򱻴�����
	{
		float vTZ = 1.0f;
		btVector3 linearVelocity = curr_ball->getBody()->getLinearVelocity();
		btScalar btvx =	linearVelocity.x();
		btScalar btvy = linearVelocity.y();
		btScalar btvz = linearVelocity.z();
		if(linearVelocity.x()>-vTZ&&linearVelocity.x()<vTZ
				&&linearVelocity.y()<vTZ&&linearVelocity.y()>-vTZ&&
				linearVelocity.z()>-vTZ&&linearVelocity.z()<vTZ
				)
		{
	           	float vx = dx*10/Constant::SCREEN_WIDTH;
	           	float vy = -dy*76*2*Constant::vFactor/Constant::SCREEN_HEIGHT;
	           	float vz = dy*28*2/Constant::SCREEN_HEIGHT;

	           	curr_ball->getBody()->activate(true);
	           	curr_ball->getBody()->setLinearVelocity(btVector3(vx,vy,vz));
	           	curr_ball->getBody()->setAngularVelocity(btVector3(5,0,0));
	           	curr_ball= NULL;
		}
	}
	curr_ball= NULL;
	if(Constant::isnoHelpView)
	{
		//��ȡ�ĸ�������β˵���ѡ��
		int result = judgeMenuTouched(tx,ty);
		//��¼��ǰѡ�еĲ˵�
		if(Constant::TOUCHED_CURR_MENU == result)
		{
			switch(Constant::TOUCHED_CURR_MENU)
			{
			case 11://ִ��ֹͣ�˵�������Ĵ���
				helpThreadFlag = false;
				Constant::isnoHelpView = false;
				Constant::DIGITAL_SCORE = 0;
				break;
			case 12://ִ����ͣ�˵�������Ĵ���
				isHelpPause = !isHelpPause;
				break;
			}
		}
	}
}
int GameView::judgeMenuTouched(float touchX,float touchY)
{
	//��ͣ��ֹͣ�˵��İ����---170��������δ���ൽ�Ҳ�ļ��
	for(int i = -1;i<2;i++)
	{
		if(i == 0)
		{
			continue;
		}
		if(touchX<(i*0.48+Constant::MENU_STOP_PAUSE_WIDTH_HALF)
				&&touchX>(i*0.48-Constant::MENU_STOP_PAUSE_WIDTH_HALF))
		{
			if(touchY<(- 0.9 + Constant::MENU_STOP_PAUSE_HEIGHT_HALF )
					&&touchY>(- 0.9 - Constant::MENU_STOP_PAUSE_HEIGHT_HALF ))
			{
				switch(i)
				{
				case -1:return Constant::TOUCH_STOP_MENU;
				case 1:return Constant::TOUCH_PAUSE_MENU;
				}
			}
		}
	}
	return Constant::TOUCHED_NONE_MENU;
}

void GameView::basketBallForHelpThread()
{
	int array_id=0;//����ڼ�����
	bool isnoFashe=false;//�Ƿ���Խ����ֵĶ���

	while(helpThreadFlag)
	{
		if(Constant::VIEW_CUR_GL_INDEX != Constant::VIEW_GAME_INDEX)
		{
			helpThreadFlag = false;
		}
		if(!isnoPlay){//�������ͣ����
			continue;
		}
		array_id = array_id%3;
		Constant::shipingJs += 100;//��¼����ʱ��
		//��������Խ����ֵĶ���������ʱ��û�дﵽ4��
		if(!isnoFashe&&(Constant::shipingJs)%5000==4000)
		{
			int ballIndex =0;
			std::vector<TexBall*>::iterator ballIterator ;
			for(ballIterator = tball_vector.begin();ballIterator!=tball_vector.end();ballIterator++)
			{
				btTransform btt ;
				btt.setIdentity();
				btt.setOrigin(btVector3(Constant::STARTBALL[ballIndex][0],Constant::STARTBALL[ballIndex][1],Constant::STARTBALL[ballIndex][2]));
				(*ballIterator)->getBody()->setCenterOfMassTransform(btt);
				(*ballIterator)->getBody()->clearForces();
				ballIndex++;
			}

			Constant::shouX=Constant::STARTBALL[array_id][0]/2;
			Constant::shouY=-0.9f;
			isnoFashe=true;
		}
		if(isnoFashe)
		{
			if(array_id==0)
			{
				Constant::shouX+=0.03f;
			}else if(array_id==2)
			{
				Constant::shouX-=0.03f;
			}
			Constant::shouY+=0.1f;
			if(Constant::shouY>1.2f)
			{
				Constant::shouY=5.0f;
			}
		}else{
			Constant::shouY=5.0f;
		}
		if((Constant::shipingJs)%5000==0)
		{
			isnoFashe=false;//������Ϻ�
			Constant::shouY=4;

			float vx=Constant::STARTBALL_V[array_id][0];//*10/SCREEN_WIDHT;
           	float vy=Constant::STARTBALL_V[array_id][1];
           	float vz=Constant::STARTBALL_V[array_id][2];

           	tball_vector.at(array_id)->getBody()->activate();
           	tball_vector.at(array_id)->getBody()->setLinearVelocity(btVector3(vx,vy,vz));//�������ٶ�
           	tball_vector.at(array_id)->getBody()->setAngularVelocity(btVector3(5,0,0));//���ý��ٶ�

			array_id ++;
		}
		if(Constant::shipingJs == 50000)
		{
			helpThreadFlag = false;
			Constant::isnoHelpView = false;
			Constant::DIGITAL_SCORE = 0;
		}
		std::this_thread::sleep_for(std::chrono::milliseconds(100));
	}
}
