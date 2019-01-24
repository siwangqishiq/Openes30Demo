#include "view/MenuView.h"
#include "util/Constant.h"
#include "util/ShaderManager.h"
#include "util/MatrixState.h"

#include "view/GameView.h"
#include "FireBasketBallcpp/main.h"

MenuView::MenuView()
{
}
MenuView::~MenuView()
{

}

//������
void MenuView::onViewCreated(JNIEnv * env,jobject obj)
{
	//���ñ�����ɫ
	glClearColor(0.5f, 0.5f, 0.5f, 1);
	//��ʼ���任����
    MatrixState::setInitStack();
}

//���ı�
void MenuView::onViewchanged(int w, int h)
{
	//�����ӿ�
    glViewport(0, 0, w, h);
    //�������
    ratio = (float) w/h;
    //�򿪱������
    glEnable(GL_CULL_FACE);
    glDisable(GL_DEPTH_TEST);
}
//���Լ�
void MenuView::drawSelf(int MenuId)
{
    //�򿪱������
    glEnable(GL_CULL_FACE);
    glDisable(GL_DEPTH_TEST);
	//�建��
	glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
    //���ô˷����������͸��ͶӰ����
    MatrixState::setProjectOrtho(-ratio, ratio, -1, 1, 4.0f, 100);
    //���λ��(0,3.15,14.9)Ŀ��λ��������(0,3.15,0)
    MatrixState::setCamera(0, 0,5, 0, 0,0, 0, 1, 0);

	MatrixState::pushMatrix();
	gameOverBgTextureRect->drawSelf(bgTexId);
	MatrixState::popMatrix();

	switch(MenuId)
	{
	case 0://Constant::MENU_STARTVIEW_START:
		drawGameStartMenu();
	break;

	case 1://Constant::MENU_GAMEVIEW_END:
		drawGameOverMenu();
	break;
	}
}
void MenuView::initTextureId(JNIEnv * env,jobject obj)
{
	jclass cl = env->FindClass("com/bn/bullet/GL2JNIView");
	jmethodID id = env->GetStaticMethodID(cl,"initTextureRepeat","(Landroid/opengl/GLSurfaceView;Ljava/lang/String;)I");
	//��������Id
	jstring name = env->NewStringUTF("pic/background.png");
	bgTexId = env->CallStaticIntMethod(cl,id,obj,name);
	//����һ������Id
	name = env->NewStringUTF("pic/retry.png");
	ZLYCTexId = env->CallStaticIntMethod(cl,id,obj,name);
	//�˳�����Id
	name = env->NewStringUTF("pic/shut.png");
	TCTextId = env->CallStaticIntMethod(cl,id,obj,name);
	//��������Id
	name = env->NewStringUTF("pic/fanhuicaidan.png");
	FHCDTextId = env->CallStaticIntMethod(cl,id,obj,name);

	//��ʼ��Ϸ�˵�����id
	name = env->NewStringUTF("pic/begin.png");
	KSYXTexId = env->CallStaticIntMethod(cl,id,obj,name);
	//���ò˵�����id
	name = env->NewStringUTF("pic/shezhi.png");
	SZTexId = env->CallStaticIntMethod(cl,id,obj,name);
	//���ڲ˵�����id
	name = env->NewStringUTF("pic/about1.png");
	GYTexId = env->CallStaticIntMethod(cl,id,obj,name);
	//�����˵�����id
	name = env->NewStringUTF("pic/help1.png");
	BZTexId = env->CallStaticIntMethod(cl,id,obj,name);
	//��¼�˵�����id
	name = env->NewStringUTF("pic/jilu.png");
	JLTexId = env->CallStaticIntMethod(cl,id,obj,name);
	env->DeleteLocalRef(name);
}
void MenuView::initGameOverMenu()
{
	gameOverBgTextureRect = new TextureRect(Constant::RADIO,1.0,0,1,ShaderManager::getCommTextureShaderProgram(),NULL);//���������ζ���

	menuTextureRect = new TextureRect(Constant::MENU_WIDTH_HALF,Constant::MENU_HEIGHT_HALF,0,1,ShaderManager::getCommTextureShaderProgram(),NULL);//���������ζ���
}
//������Ϸ�����˵�
void MenuView::drawGameOverMenu()
{
    //�������
    glEnable(GL_BLEND);
    //���û������
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	MatrixState::pushMatrix();
	MatrixState::translate(0,Constant::MENU_SPAN,0);
    if(Constant::isTouched[Constant::TOUCHED_ZLYJ_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }
	menuTextureRect->drawSelf(ZLYCTexId);
	MatrixState::popMatrix();

	MatrixState::pushMatrix();
    if(Constant::isTouched[Constant::TOUCHED_FHCD_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }
	menuTextureRect->drawSelf(FHCDTextId);
	MatrixState::popMatrix();

	MatrixState::pushMatrix();
	MatrixState::translate(0,-Constant::MENU_SPAN,0);
    if(Constant::isTouched[Constant::TOUCHED_TC_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }
	menuTextureRect->drawSelf(TCTextId);
	MatrixState::popMatrix();
    //�رջ��
    glDisable(GL_BLEND);
}
//������Ϸ��ʼ�˵�
void MenuView::drawGameStartMenu()
{
    //�������
    glEnable(GL_BLEND);
    //���û������
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	//��ʼ��Ϸ�˵�����id
    MatrixState::pushMatrix();
    MatrixState::translate(0,Constant::MENU_SPAN/2*2,0);
    if(Constant::isTouched[Constant::TOUCHED_KSYX_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }
    menuTextureRect->drawSelf(KSYXTexId);
    MatrixState::popMatrix();

	//���ò˵�����id
    MatrixState::pushMatrix();
    MatrixState::translate(0,Constant::MENU_SPAN/2*1,0);
    if(Constant::isTouched[Constant::TOUCHED_SZ_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }

    menuTextureRect->drawSelf(SZTexId);
    MatrixState::popMatrix();

	//���ڲ˵�����id
    MatrixState::pushMatrix();
    MatrixState::translate(0,Constant::MENU_SPAN/2*0,0);
    if(Constant::isTouched[Constant::TOUCHED_GY_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }
    menuTextureRect->drawSelf(GYTexId);
    MatrixState::popMatrix();

	//�����˵�����id
	MatrixState::pushMatrix();
	MatrixState::translate(0,-Constant::MENU_SPAN/2,0);
    if(Constant::isTouched[Constant::TOUCHED_BZ_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }
	menuTextureRect->drawSelf(BZTexId);
	MatrixState::popMatrix();

	//��¼�˵�����id
    MatrixState::pushMatrix();
    MatrixState::translate(0,-Constant::MENU_SPAN/2*2,0);
    if(Constant::isTouched[Constant::TOUCHED_JL_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }
    menuTextureRect->drawSelf(JLTexId);
    MatrixState::popMatrix();


    MatrixState::pushMatrix();
    MatrixState::translate(0,-Constant::MENU_SPAN/2*3,0);
    if(Constant::isTouched[Constant::TOUCHED_TC_MENU])
    {
    	MatrixState::scale(Constant::MENU_SACLE_X,Constant::MENU_SACLE_Y,Constant::MENU_SACLE_Z);
    }
    menuTextureRect->drawSelf(TCTextId);
    MatrixState::popMatrix();

    //�رջ��
    glDisable(GL_BLEND);
}

void MenuView::onTouchBegan(float touchX,float touchY)
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
void MenuView::onTouchMoved(float touchX,float touchY){}
void MenuView::onTouchEnded(float touchX,float touchY)
{
	//��Ļx���굽�ӿ�x����
	float tx = Constant::fromScreenXToNearX(touchX);
	//��Ļy���굽�ӿ�y����
	float ty = Constant::fromScreenYToNearY(touchY);
	int touchedIndex = judgeMenuTouched(tx,ty);
	//���������ʼ�ʹ���̧����ͬһ���˵�---��ִ�иò˵��Ĺ���--�л�����
	if(Constant::TOUCHED_CURR_MENU == touchedIndex)
	{
		switch(Constant::TOUCHED_CURR_MENU)
		{
		case 1://�����ʼ��Ϸִ�еĴ���
			Constant::SWAP_VIEW_FLAG = true;
			if(Constant::isOpen_BJYY)
			{
				main::playBackmusic(main::envMain,main::objMain,(jint)1);
			}else
			{
				main::playBackmusic(main::envMain,main::objMain,(jint)0);
			}

			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_GAME_INDEX;
			main::gameView->onViewchanged(Constant::w,Constant::h);
			main::gameView->initGameViewData();
			Constant::SWAP_VIEW_FLAG = false;
			break;
		case 2://�������ִ�еĴ���
			Constant::SWAP_VIEW_FLAG = true;
			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_SET_INDEX;
			main::setView->onViewchanged(Constant::w,Constant::h);
			Constant::SWAP_VIEW_FLAG = false;
			break;
		case 3://�������ִ�еĴ���
			Constant::SWAP_VIEW_FLAG = true;
			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_ABOUT_INDEX;
			main::aboutView->onViewchanged(Constant::w,Constant::h);
			Constant::SWAP_VIEW_FLAG = false;
			break;
		case 4://�������ִ�еĴ���
			Constant::SWAP_VIEW_FLAG = true;
			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_GAME_INDEX;
			main::gameView->onViewchanged(Constant::w,Constant::h);
			main::gameView->initGameViewData();
			Constant::isnoHelpView = true;
			Constant::SWAP_VIEW_FLAG = false;
			break;
		case 5://�����¼ִ�еĴ���
			Constant::SWAP_VIEW_FLAG = true;
			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_RECORD_INDEX;
			main::recordView->onViewchanged(Constant::w,Constant::h);
			main::recordView->calTableResult();
			Constant::SWAP_VIEW_FLAG = false;
			break;
		case 6://����˳�ִ�еĴ���
			Constant::SWAP_VIEW_FLAG = true;
			main::NDKCallExit(main::envMain,main::objMain);
			Constant::SWAP_VIEW_FLAG = false;
			break;
		case 7://�������һ��ִ�еĴ���
			//���ò˵�����
			Constant::SWAP_VIEW_FLAG = true;
			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_GAME_INDEX;
			main::gameView->onViewchanged(Constant::w,Constant::h);
			main::gameView->initGameViewData();
			Constant::SWAP_VIEW_FLAG = false;
			break;
		case 8://������ز˵�ִ�еĴ���
			Constant::SWAP_VIEW_FLAG = true;
			//���ò˵�����
			Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_MENU_INDEX;
			//���˵�����
			Constant::MENU_CURR_MENUINDEX = Constant::MENU_STARTVIEW_START;
			main::menuView->onViewchanged(Constant::w,Constant::h);
			Constant::SWAP_VIEW_FLAG = false;
			break;
		}
		//���ó�ʼֵ������δ����κβ˵�
		Constant::TOUCHED_CURR_MENU = Constant::TOUCHED_NONE_MENU;
		main::sendViewIndex(main::envMain,main::objMain,Constant::VIEW_CUR_GL_INDEX);
	}

	//���ò˵��δ��ѡ��
	for(int i=0;i<15;i++)
	{
		Constant::isTouched[i] = false;
	}
}
int MenuView::judgeMenuTouched(float x,float y)
{
	if(Constant::MENU_CURR_MENUINDEX == Constant::MENU_GAMEVIEW_END)//�����ǰ��Ҫ��ʾ��Ϸ�����˵�
	{
		if(x > -Constant::MENU_WIDTH_HALF&&x<Constant::MENU_WIDTH_HALF)
		{
			for(int i=-1;i<2;i++)
			{
				//��������һ�β˵�
				if(y>(Constant::MENU_SPAN*(-i)-Constant::MENU_HEIGHT_HALF)
						&&y<(Constant::MENU_HEIGHT_HALF+(Constant::MENU_SPAN*(-i))))
				{
					//��������һ�ֲ˵���������
					switch(i)
					{
					case 1:return Constant::TOUCHED_TC_MENU;
					case 0:return Constant::TOUCHED_FHCD_MENU;
					case -1:return Constant::TOUCHED_ZLYJ_MENU;
					}
				}
			}
		}
	}else if(Constant::MENU_CURR_MENUINDEX == Constant::MENU_STARTVIEW_START)//�����ǰ��Ҫ��ʾ��Ϸ��ʼ�˵�
	{
		if(x > -Constant::MENU_WIDTH_HALF&&x<Constant::MENU_WIDTH_HALF)
		{
			for(int i=-2;i<4;i++)
			{
				//���п�ʼ��Ϸ�˵�
				if(y>(Constant::MENU_SPAN/2*(-i)-Constant::MENU_HEIGHT_HALF)
						&&y<(Constant::MENU_HEIGHT_HALF+(Constant::MENU_SPAN/2*(-i))))
				{
					//��������һ�ֲ˵���������
					switch(i)
					{
					case -2:return Constant::TOUCHED_KSYX_MENU;
					case -1:return Constant::TOUCHED_SZ_MENU;
					case 0:return Constant::TOUCHED_GY_MENU;
					case 1:return Constant::TOUCHED_BZ_MENU;
					case 2:return Constant::TOUCHED_JL_MENU;
					case 3:return Constant::TOUCHED_TC_MENU;
					}
				}
			}
		}
	}
	//δ����
	return Constant::TOUCHED_NONE_MENU;
}

