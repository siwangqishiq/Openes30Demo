#include "view/LoadingView.h"
#include "util/MatrixState.h"
#include "util/ShaderManager.h"

#include "FireBasketBallcpp/main.h"
#include "jni.h"
#include "util/FileUtil.h"

LoadingView::LoadingView() {
	//��ת�ĽǶ�
	angle = 0;
	loadingFlag = true;
	loadTime = 0;

	isAttach = false;

	loadIndex = -1;
}
LoadingView::~LoadingView() {

}
//������
void LoadingView::onViewCreated(JNIEnv * env, jobject obj) {
	//���ñ�����ɫ
	glClearColor(0.5f, 0.5f, 0.5f, 1);
	//��ʼ���任����
	MatrixState::setInitStack();
	//�������3D�����л�ӭ��ӭ�����е�shader
	ShaderManager::compileShader();
	//��ʼ������ID
	initTextureId(env, obj);
	//��ʼ���������
	initTextureRect();
}
//���ı�
void LoadingView::onViewchanged(int w, int h) {
	//�����ӿ�
	glViewport(0, 0, w, h);
	//�������
	ratio = (float) w / h;
	//�򿪱������
	glEnable(GL_CULL_FACE);
	glDisable(GL_DEPTH_TEST);
}
//���Լ�
void LoadingView::drawSelf() //������Ҫ���ƵĲ˵�Id������id���ƶ�Ӧ�Ĳ˵�
{
	//�建��
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	//���ô˷����������͸��ͶӰ����
	MatrixState::setProjectOrtho(-ratio, ratio, -1, 1, 4.0f, 100);
	//���λ��(0,3.15,14.9)Ŀ��λ��������(0,3.15,0)
	MatrixState::setCamera(0, 0, 5, 0, 0, 0, 0, 1, 0);

	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	MatrixState::pushMatrix();
	bgTextureRect->drawSelf(bgTexId);
	MatrixState::popMatrix();

	MatrixState::pushMatrix();
	angle = angle + 20;
	MatrixState::rotate(angle, 0, 0, 1);
	XZTextureRect->drawSelf(XZTexId);
	MatrixState::popMatrix();

	if(loadingFlag == true)
	{
		intiAllAssats(main::envMain,main::objMain);
	}

	glDisable(GL_BLEND);
}
void LoadingView::threadTask() {
	while (true) {
		loadTime = loadTime + 10;
		if (loadTime >= 200) {
			loadingFlag = false;
			Constant::SWAP_VIEW_FLAG = true;
			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_MENU_INDEX;
			Constant::MENU_CURR_MENUINDEX = Constant::MENU_STARTVIEW_START;
			main::menuView->onViewchanged(Constant::w, Constant::h);
			Constant::SWAP_VIEW_FLAG = false;
			break;
		}
		std::this_thread::sleep_for(std::chrono::milliseconds(100));
	}
}
#define RIGID_KUANG_OBJ 1//����OBJ����
#define SOFT_NET_OBJ 2//����OBJ����
void LoadingView::intiAllAssats(JNIEnv* env, jobject obj) {
	loadingFlag = true;
	switch(loadIndex)
	{
	case 0:
		FileUtil::loadFromSDCardFile("/profitData/lankuang.obj");
		main::loadObjDataWd(
				(int)RIGID_KUANG_OBJ,
				main::copyFloats((float*)FileUtil::m_vertices,(int)FileUtil::numVertices),
				(int)FileUtil::numVertices,
				main::copyInts((int*)FileUtil::m_verticesIndices,(int)FileUtil::numVerticesIndex),
				(int)FileUtil::numVerticesIndex,
				main::copyFloats((float*)FileUtil::m_tex,(int)FileUtil::numTex),
				(int)FileUtil::numTex,
				main::copyFloats((float*)FileUtil::m_normal,(int)FileUtil::numNormal),
				(int)FileUtil::numNormal
			);
		break;
	case 1:
		main::menuView = new MenuView();
		break;
	case 2:
		main::menuView->onViewCreated(env, obj);
		break;
	case 3:
		main::menuView->initTextureId(env,obj);
		break;
	case 4:
		main::menuView->initGameOverMenu();
		break;
	case 5:
		main::setView = new SetView();
		break;
	case 6:
		main::setView->onViewCreated(env, obj);
		break;
	case 7:
		main::setView->initTextureId(env,obj);
		break;
	case 8:
		main::setView->initTextureRect();
		break;
	case 9:
		FileUtil::loadFromSDCardFile("/profitData/wang.obj");
		main::loadObjDataWd(
				(int)SOFT_NET_OBJ,
				main::copyFloats((float*)FileUtil::m_vertices,(int)FileUtil::numVertices),
				(int)FileUtil::numVertices,
				main::copyInts((int*)FileUtil::m_verticesIndices,(int)FileUtil::numVerticesIndex),
				(int)FileUtil::numVerticesIndex,
				main::copyFloats((float*)FileUtil::m_tex,(int)FileUtil::numTex),
				(int)FileUtil::numTex,
				main::copyFloats((float*)FileUtil::m_normal,(int)FileUtil::numNormal),
				(int)FileUtil::numNormal
				);
		break;
	case 10:
		main::recordView = new RecordView();
		break;
	case 11:
		main::recordView->onViewCreated(env, obj);
		break;
	case 12:
		main::recordView->initTextureId(env, obj);
		break;
	case 13:
		main::recordView->initTextureRect();
		break;
	case 14:
		main::gameView = new GameView();
		break;
	case 15:
		main::gameView->onViewCreated(env, obj);
		break;
	case 16:
	    //��ʼ���������
		main::gameView->initTextureRect();
	    break;
	case 17:
	    //��ʼ������Id
		main::gameView->initTextureId(env,obj);
	    break;
	case 18:
	    //��ʼ����������
		main::gameView->initPhysicalWorld();
	    break;
	case 19:
	    //��ʼ���������������
		main::gameView->initPhysicalWorldBody();
		break;
	case 20:
		main::aboutView = new AboutView();
		break;
	case 21:
		main::aboutView->onViewCreated(env, obj);
		break;
	case 22://��ʼ������ID
		main::aboutView->initTextureId(env, obj);
		break;
	case 23://��ʼ���������
		main::aboutView->initTextureRect();
		break;
	case 24:	//��ʼ���˵�
		main::aboutView->initMenu();
		break;
	case 25:
		loadingFlag = false;
		Constant::SWAP_VIEW_FLAG = true;
		Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_MENU_INDEX;
		Constant::MENU_CURR_MENUINDEX = Constant::MENU_STARTVIEW_START;
		main::menuView->onViewchanged(Constant::w, Constant::h);
		Constant::SWAP_VIEW_FLAG = false;
		break;
	}
	//������Ŀ����
	loadIndex ++ ;
}
void LoadingView::initTextureId(JNIEnv * env, jobject obj) {
	jclass cl = env->FindClass("com/bn/bullet/GL2JNIView");
	jmethodID id = env->GetStaticMethodID(cl, "initTextureRepeat",
			"(Landroid/opengl/GLSurfaceView;Ljava/lang/String;)I");
	//��������Id
	jstring name = env->NewStringUTF("pic/background.png");
	bgTexId = env->CallStaticIntMethod(cl, id, obj, name);
	//��ת����Id
	name = env->NewStringUTF("pic/dott.png");
	XZTexId = env->CallStaticIntMethod(cl, id, obj, name);
	env->DeleteLocalRef(name);
}
//��ʼ���������
void LoadingView::initTextureRect() {
	//�����������
	bgTextureRect = new TextureRect(Constant::RADIO, 1.0, 0, 1,
			ShaderManager::getCommTextureShaderProgram(), NULL); //���������ζ���
	//��ת�������
	XZTextureRect = new TextureRect(0.1, 0.1, 0, 1,
			ShaderManager::getCommTextureShaderProgram(), NULL);	//���������ζ���
}
