#include "view/AboutView.h"
#include "util/MatrixState.h"
#include "util/ShaderManager.h"
#include <jni.h>
#include "util/Constant.h"

#include "FireBasketBallcpp/main.h"

AboutView::AboutView() {
}
AboutView::~AboutView() {
}
//������
void AboutView::onViewCreated(JNIEnv * env, jobject obj) {
	//���ñ�����ɫ
	glClearColor(0.5f, 0.5f, 0.5f, 1);
	//��ʼ���任����
	MatrixState::setInitStack();
}

//���ı�
void AboutView::onViewchanged(int w, int h) {
	//�����ӿ�
	glViewport(0, 0, w, h);
	//�������
	ratio = (float) w / h;
	//�򿪱������
	glEnable(GL_CULL_FACE);
//    glEnable(GL_DEPTH_TEST);
//    glDisable(GL_CULL_FACE);
	glDisable(GL_DEPTH_TEST);

}
//���Լ�
void AboutView::drawSelf() {
	//�建��
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	//���ô˷����������͸��ͶӰ����
//	MatrixState::setProjectFrustum(-ratio, ratio, -1, 1, 4.0f, 100);
	MatrixState::setProjectOrtho(-ratio, ratio, -1, 1, 4.0f, 100);
	//���λ��(0,3.15,14.9)Ŀ��λ��������(0,3.15,0)
	MatrixState::setCamera(0, 0, 5, 0, 0, 0, 0, 1, 0);
	//�������
	glEnable(GL_BLEND);
	//���û������
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	MatrixState::pushMatrix();
	bgTextureRect->drawSelf(bgTexId);
	MatrixState::popMatrix();

	MatrixState::pushMatrix();
	aboutTextureRect->drawSelf(aboutTexId);
	MatrixState::popMatrix();

	MatrixState::pushMatrix();
	MatrixState::translate(-0.3, -0.7, 0);
	if (Constant::isTouched[Constant::TOUCHED_QD_MENU]) {
		MatrixState::scale(Constant::MENU_SACLE_X, Constant::MENU_SACLE_Y,
				Constant::MENU_SACLE_Z);
	}
	menuTextureRect->drawSelf(QDMenuTextId);
	MatrixState::popMatrix();

	MatrixState::pushMatrix();
	MatrixState::translate(0.3, -0.7, 0);
	if (Constant::isTouched[Constant::TOUCHED_FH_MENU]) {
		MatrixState::scale(Constant::MENU_SACLE_X, Constant::MENU_SACLE_Y,
				Constant::MENU_SACLE_Z);
	}
	menuTextureRect->drawSelf(FHMenuTextId);
	MatrixState::popMatrix();

	//�������
	glDisable(GL_BLEND);
}
//��ʼ������ID
void AboutView::initTextureId(JNIEnv * env, jobject obj) {
	jclass cl = env->FindClass("com/bn/bullet/GL2JNIView");

	jmethodID id = env->GetStaticMethodID(cl,"initTextureRepeat","(Landroid/opengl/GLSurfaceView;Ljava/lang/String;)I");
	//��������Id
	jstring name = env->NewStringUTF("pic/background.png");
	bgTexId = env->CallStaticIntMethod(cl, id, main::g_obj, name);

	//������Ϣ����Id
	name = env->NewStringUTF("pic/guanyuwenzitu.png");
	aboutTexId = env->CallStaticIntMethod(cl, id, main::g_obj, name);
	//ȷ���˵�����Id
	name = env->NewStringUTF("pic/quedinganniu.png");
	QDMenuTextId = env->CallStaticIntMethod(cl, id, main::g_obj, name);
	//���ز˵�����Id
	name = env->NewStringUTF("pic/fanhuianniu.png");
	FHMenuTextId = env->CallStaticIntMethod(cl, id, main::g_obj, name);
}
//��ʼ���������
void AboutView::initTextureRect() {
	bgTextureRect = new TextureRect(Constant::RADIO,1.0, 0, 1,
			ShaderManager::getCommTextureShaderProgram(), NULL); //���������ζ���

	aboutTextureRect = new TextureRect(0.5, 0.5, 0, 1,
			ShaderManager::getCommTextureShaderProgram(), NULL); //���������ζ���

}
//��ʼ���˵�
void AboutView::initMenu() {
	menuTextureRect = new TextureRect(Constant::MENU_QD_FH_WIDTH_HALF,
			Constant::MENU_QD_FH_HEIGHT_HALF, 0, 1,
			ShaderManager::getCommTextureShaderProgram(), NULL); //���������ζ���
}
void AboutView::onTouchBegan(float touchX, float touchY) {
	//��Ļx���굽�ӿ�x����
	float tx = Constant::fromScreenXToNearX(touchX);
	//��Ļy���굽�ӿ�y����
	float ty = Constant::fromScreenYToNearY(touchY);
	//��ȡ����Ĳ˵����
	int touchedIndex = judgeMenuTouched(tx, ty);
	//��¼����˵��ı��
	Constant::TOUCHED_CURR_MENU = touchedIndex;
	//�˵��ѡ��
	Constant::isTouched[Constant::TOUCHED_CURR_MENU] = true;
}
void AboutView::onTouchMoved(float touchX, float touchY) {
}
void AboutView::onTouchEnded(float touchX, float touchY) {\
	//��Ļx���굽�ӿ�x����
	float tx = Constant::fromScreenXToNearX(touchX);
	//��Ļy���굽�ӿ�y����
	float ty = Constant::fromScreenYToNearY(touchY);
	//��ȡ����Ĳ˵����
	int touchedIndex = judgeMenuTouched(tx, ty);
	//���������ʼ�ʹ���̧����ͬһ���˵�---��ִ�иò˵��Ĺ���--�л�����
	if (Constant::TOUCHED_CURR_MENU == touchedIndex) {
		switch (Constant::TOUCHED_CURR_MENU) {
		case 9: //ȷ����ť������
		case 10:
			Constant::SWAP_VIEW_FLAG = true;
			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_MENU_INDEX;
			main::menuView->onViewchanged(Constant::w, Constant::h);
			Constant::SWAP_VIEW_FLAG = false;
			break; //���ذ�ť������
		}
		//���ó�ʼֵ������δ����κβ˵�
		Constant::TOUCHED_CURR_MENU = Constant::TOUCHED_NONE_MENU;
	}
	//���ò˵��δ��ѡ��
	for (int i = 0; i < 15; i++) {
		Constant::isTouched[i] = false;
	}
}
int AboutView::judgeMenuTouched(float x, float y) {
	if(y<(Constant::MENU_QD_FH_HEIGHT_HALF-0.7)
				&&y>(-Constant::MENU_QD_FH_HEIGHT_HALF-0.7))
		{
			for(int i=-1;i<2;i++)
			{
				if(i==0)
				{
					continue;
				}
				if(x<(Constant::MENU_QD_FH_WIDTH_HALF-0.3*(-i))
						&&x>(-Constant::MENU_QD_FH_WIDTH_HALF-0.3*(-i)))
				{
					switch(i)
					{
					case -1:
						return Constant::TOUCHED_QD_MENU;
					case 1:
						return Constant::TOUCHED_FH_MENU;
					}
				}
			}
		}
	//û��ѡ�а�ť
	return Constant::TOUCHED_NONE_MENU;
}
