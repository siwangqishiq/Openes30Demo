#include "view/SetView.h"

#include "util/Constant.h"
#include "util/ShaderManager.h"

#include "util/MatrixState.h"

#include "FireBasketBallcpp/main.h"

SetView::SetView()
{

}
SetView::~SetView()
{

}
//������
void SetView::onViewCreated(JNIEnv * env,jobject obj)
{
	//���ñ�����ɫ
	glClearColor(0.5f, 0.5f, 0.5f, 1);
	//��ʼ���任����
    MatrixState::setInitStack();
	//�������3D�����л�ӭ��ӭ�����е�shader
//	ShaderManager::compileShader();
    //��ʼ������ID
//    initTextureId(env,obj);
    //��ʼ���������
//    initTextureRect();
}
//���ı�
void SetView::onViewchanged(int w, int h)
{
	//�����ӿ�
    glViewport(0, 0, w, h);
    //�������
    ratio = (float) w/h;
    //�򿪱������
    glEnable(GL_CULL_FACE);
    glDisable(GL_DEPTH_TEST);
}
//���Լ�---������Ҫ���ƵĲ˵�Id������id���ƶ�Ӧ�Ĳ˵�
void SetView::drawSelf()
{
    //�򿪱������
    glEnable(GL_CULL_FACE);
    glDisable(GL_DEPTH_TEST);
	//�建��
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    //���ô˷����������͸��ͶӰ����
//    MatrixState::setProjectFrustum(-ratio, ratio, -1, 1, 4.0f, 100);
    MatrixState::setProjectOrtho(-ratio, ratio, -1, 1, 4.0f, 100);
    //���λ��(0,3.15,14.9)Ŀ��λ��������(0,3.15,0)
    MatrixState::setCamera(0, 0,5, 0, 0,0, 0, 1, 0);
    glEnable(GL_BLEND);
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    MatrixState::pushMatrix();
    bgTextureRect->drawSelf(bgTexId);
    MatrixState::popMatrix();

    MatrixState::pushMatrix();
    MatrixState::translate(-Constant::MENU_HEIGHT_HALF*2,-Constant::MENU_HEIGHT_HALF*2,0);
    infoTextureRect->drawSelf(BJYY_TexId);
    MatrixState::popMatrix();

    MatrixState::pushMatrix();
    MatrixState::translate(-Constant::MENU_HEIGHT_HALF*2,Constant::MENU_HEIGHT_HALF*2,0);
    infoTextureRect->drawSelf(YXYY_TexId);
    MatrixState::popMatrix();

    MatrixState::pushMatrix();
    MatrixState::translate(-0.3,-0.7,0);
    if(Constant::isTouched[Constant::TOUCHED_QD_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }
    menuTextureRect->drawSelf(menu_QD_TexId);
    MatrixState::popMatrix();

    MatrixState::pushMatrix();
    MatrixState::translate(0.3,-0.7,0);
    if(Constant::isTouched[Constant::TOUCHED_FH_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }
    menuTextureRect->drawSelf(menu_FH_TexId);
    MatrixState::popMatrix();

    if(Constant::isOpen_BJYY)
    {
        MatrixState::pushMatrix();
        MatrixState::translate(Constant::MENU_HEIGHT_HALF*5,-Constant::MENU_HEIGHT_HALF*2,0);
        kuangTextureRect->drawSelf(KUANG_DUIGOU_TexId);
        MatrixState::popMatrix();
    }

    MatrixState::pushMatrix();
    MatrixState::translate(Constant::MENU_HEIGHT_HALF*5,-Constant::MENU_HEIGHT_HALF*2,0);
    kuangTextureRect->drawSelf(menu_KUANG_TexId);
    MatrixState::popMatrix();

    if(Constant::isOpen_CJYY)
    {
        MatrixState::pushMatrix();
        MatrixState::translate(Constant::MENU_HEIGHT_HALF*5,Constant::MENU_HEIGHT_HALF*2,0);
        kuangTextureRect->drawSelf(KUANG_DUIGOU_TexId);
        MatrixState::popMatrix();
    }

    MatrixState::pushMatrix();
    MatrixState::translate(Constant::MENU_HEIGHT_HALF*5,Constant::MENU_HEIGHT_HALF*2,0);
    kuangTextureRect->drawSelf(menu_KUANG_TexId);
    MatrixState::popMatrix();

    //�رջ��
    glDisable(GL_BLEND);

}
void SetView::initTextureId(JNIEnv * env,jobject obj)
{
	jclass cl = env->FindClass("com/bn/bullet/GL2JNIView");
	jmethodID id = env->GetStaticMethodID(cl,"initTextureRepeat","(Landroid/opengl/GLSurfaceView;Ljava/lang/String;)I");
	//��������Id
	jstring name = env->NewStringUTF("pic/background.png");
	bgTexId = env->CallStaticIntMethod(cl,id,obj,name);
	//������������Id
	name = env->NewStringUTF("pic/beijingyinyu.png");
	BJYY_TexId = env->CallStaticIntMethod(cl,id,obj,name);
	//��Ч��������Id
	name = env->NewStringUTF("pic/changjinyinyu.png");
	YXYY_TexId = env->CallStaticIntMethod(cl,id,obj,name);
	//ȷ������Id
	name = env->NewStringUTF("pic/quedinganniu.png");
	menu_QD_TexId = env->CallStaticIntMethod(cl,id,obj,name);
	//��������Id
	name = env->NewStringUTF("pic/fanhuianniu.png");
	menu_FH_TexId = env->CallStaticIntMethod(cl,id,obj,name);

	//��������Id
	name = env->NewStringUTF("pic/baisefangge.png");
	menu_KUANG_TexId = env->CallStaticIntMethod(cl,id,obj,name);

	//�Թ�����Id
	name = env->NewStringUTF("pic/honsegou.png");
	KUANG_DUIGOU_TexId = env->CallStaticIntMethod(cl,id,obj,name);

}
//��ʼ���������
void SetView::initTextureRect()
{
	//�����������
	bgTextureRect = new TextureRect(Constant::RADIO,1.0,0,1,ShaderManager::getCommTextureShaderProgram(),NULL);//���������ζ���
	//�������֡���Ч�����������
	infoTextureRect = new TextureRect(Constant::MENU_WIDTH_HALF,Constant::MENU_HEIGHT_HALF,0,1,ShaderManager::getCommTextureShaderProgram(),NULL);//���������ζ���
	//ȷ����ȡ���������
	menuTextureRect = new TextureRect(Constant::MENU_QD_FH_WIDTH_HALF,Constant::MENU_QD_FH_HEIGHT_HALF,0,1,ShaderManager::getCommTextureShaderProgram(),NULL);//���������ζ���

	kuangTextureRect = new TextureRect(Constant::MENU_KUANG_WIDTH_HALF,Constant::MENU_KUANG_HEIGHT_HALF,0,1,ShaderManager::getCommTextureShaderProgram(),NULL);//���������ζ���
}
//������ʼ���ص�
void SetView::onTouchBegan(float touchX,float touchY)
{
	//��Ļx���굽�ӿ�x����
	float tx = Constant::fromScreenXToNearX(touchX);
	//��Ļy���굽�ӿ�y����
	float ty = Constant::fromScreenYToNearY(touchY);
	//��ȡ����Ĳ˵����
	int touchedIndex = judgeMenuTouched(tx,ty);
	//��¼����˵��ı��
	Constant::TOUCHED_CURR_MENU = touchedIndex;
	//�˵��ѡ��
	Constant::isTouched[Constant::TOUCHED_CURR_MENU] = true;
}
//�����ƶ����ص�
void SetView::onTouchMoved(float touchX,float touchY)
{

}
//�����ƶ����ص�
void SetView::onTouchEnded(float touchX,float touchY)
{
	//��Ļx���굽�ӿ�x����
	float tx = Constant::fromScreenXToNearX(touchX);
	//��Ļy���굽�ӿ�y����
	float ty = Constant::fromScreenYToNearY(touchY);
	//��ȡ����Ĳ˵����
	int touchedIndex = judgeMenuTouched(tx,ty);
	if(Constant::TOUCHED_CURR_MENU == touchedIndex)
	{
		switch(Constant::TOUCHED_CURR_MENU)
		{
		case 9: //���ȷ���˵�ִ�еĴ���
		case 10: //������ز˵�ִ�еĴ���
			Constant::SWAP_VIEW_FLAG = true;
			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_MENU_INDEX;
			main::menuView->onViewchanged(Constant::w,Constant::h);
			Constant::SWAP_VIEW_FLAG = false;

			break;
		case 13: Constant::isOpen_CJYY = !Constant::isOpen_CJYY;//����������ֿ�˵�ִ�еĴ���
			break;
		case 14:
			Constant::isOpen_BJYY = !Constant::isOpen_BJYY;//����������ֿ�˵�ִ�еĴ���

			break;
		}
	}
	//���ò˵��δ��ѡ��
	for(int i=0;i<15;i++)
	{
		Constant::isTouched[i] = false;
	}
}

int SetView::judgeMenuTouched(float x,float y)
{
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
	if(x>(Constant::MENU_HEIGHT_HALF*5-Constant::MENU_KUANG_WIDTH_HALF)
			&&x<(Constant::MENU_HEIGHT_HALF*5+Constant::MENU_KUANG_WIDTH_HALF))
	{
		for(int i=-1;i<2;i++)
		{
			if(i == 0)
			{
				continue;
			}
			if(y>(Constant::MENU_HEIGHT_HALF*2*i-Constant::MENU_KUANG_HEIGHT_HALF)&&
					y<(Constant::MENU_HEIGHT_HALF*2*i+Constant::MENU_KUANG_HEIGHT_HALF)
			)
			{
				switch(i)
				{
				case -1:
					return Constant::TOUCH_BJYY_KUANG_MENU;
				case 1:
					return Constant::TOUCH_CJYY_KUANG_MENU;
				}
			}
		}
	}
	//û��ѡ�а�ť
	return Constant::TOUCHED_NONE_MENU;
}
