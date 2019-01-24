#include "view/RecordView.h"
#include "util/MatrixState.h"
#include "util/ShaderManager.h"
#include "SQLiteUtil/SQLiteUtil.h"
#include "util/Constant.h"

#include "FireBasketBallcpp/main.h"

#include <stdlib.h>
RecordView::RecordView() {

}
//������
void RecordView::onViewCreated(JNIEnv * env, jobject obj) {
	//���ñ�����ɫ
	glClearColor(0.5f, 0.5f, 0.5f, 1);
	//��ʼ���任����
	MatrixState::setInitStack();
	//�������3D�����л�ӭ��ӭ�����е�shader
	ShaderManager::compileShader();
	initTextureRect();
	initTextureId(env, obj);
	calTableResult();

}
//���ı�
void RecordView::onViewchanged(int w, int h) {
	//�����ӿ�
	glViewport(0, 0, w, h);
	//�������
	ratio = (float) w / h;
	//�򿪱������
	glEnable(GL_CULL_FACE);
	glDisable(GL_DEPTH_TEST);
}
//���Լ�
void RecordView::drawSelf() {
	//����ɫ�������Ȼ���
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	MatrixState::pushMatrix();
	//����͸��ͶӰ
//	MatrixState::setProjectFrustum(-ratio, ratio, -1, 1, 4.0f, 100);
	MatrixState::setProjectOrtho(-ratio, ratio, -1, 1, 4.0f, 100);
	//����������Ų���
	MatrixState::setCamera(0, 0, 5, 0, 0, 0, 0, 1, 0);

	//�������
	glEnable(GL_BLEND);
	glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	MatrixState::pushMatrix();
	bgTextureRect->drawSelf(bgTexId);
	MatrixState::popMatrix();

	MatrixState::pushMatrix();
	tgBgTextureRect->drawSelf(tgBgTexId);
	MatrixState::popMatrix();

	std::vector<std::vector<GLuint>>::iterator iter = gradePNGVector->begin();
	float offsetX = 0.3;
	float offsetY = 0.25;
	for (; iter != gradePNGVector->end(); iter++) {
		std::vector<GLuint>::iterator iterTwo = (*iter).begin();
		for (; iterTwo != (*iter).end(); iterTwo++) {
			MatrixState::pushMatrix();
			MatrixState::translate(offsetX, offsetY, 0);
			digitTextureRect->drawSelf(*iterTwo);
			MatrixState::popMatrix();
			offsetX += 0.05;
		}
		offsetX = 0.3;
		offsetY -= 0.09;
	}

	iter = timePNGVector->begin();
	offsetX = -0.35;
	offsetY = 0.25;
	for (; iter != timePNGVector->end(); iter++) {
		std::vector<GLuint>::iterator iterTwo = (*iter).begin();
		for (; iterTwo != (*iter).end(); iterTwo++) {
			MatrixState::pushMatrix();
			MatrixState::translate(offsetX, offsetY, 0);
			digitTextureRect->drawSelf(*iterTwo);
			MatrixState::popMatrix();
			offsetX += 0.04;
		}
		offsetX = -0.35;
		offsetY -= 0.09;
	}

	MatrixState::pushMatrix();
	MatrixState::translate(-0.3, -0.8, 0);
	if (Constant::isTouched[Constant::TOUCHED_QD_MENU]) {
		MatrixState::scale(Constant::MENU_SACLE_X, Constant::MENU_SACLE_Y,
				Constant::MENU_SACLE_Z);
	}
	menuTextureRect->drawSelf(QDMenuTextId);
	MatrixState::popMatrix();

	MatrixState::pushMatrix();
	MatrixState::translate(0.3, -0.8, 0);
	if (Constant::isTouched[Constant::TOUCHED_FH_MENU]) {
		MatrixState::scale(Constant::MENU_SACLE_X, Constant::MENU_SACLE_Y,
				Constant::MENU_SACLE_Z);
	}
	menuTextureRect->drawSelf(FHMenuTextId);
	MatrixState::popMatrix();

	//�رջ��
	glDisable(GL_BLEND);
	MatrixState::popMatrix();
}
//��ʼ���������
void RecordView::initTextureRect() {
	//�����������
	bgTextureRect = new TextureRect(Constant::RADIO, 1.0, 0, 1,
			ShaderManager::getCommTextureShaderProgram(), NULL);

	tgBgTextureRect = new TextureRect(Constant::RADIO-0.15, 0.6, 0, 1,
			ShaderManager::getCommTextureShaderProgram(), NULL);

	digitTextureRect = new TextureRect(0.02, 0.03, 0, 1,
			ShaderManager::getCommTextureShaderProgram(), NULL);

	menuTextureRect = new TextureRect(Constant::MENU_QD_FH_WIDTH_HALF,
			Constant::MENU_QD_FH_HEIGHT_HALF, 0, 1,
			ShaderManager::getCommTextureShaderProgram(), NULL); //���������ζ���
}
//��ʼ������Id
void RecordView::initTextureId(JNIEnv * env, jobject obj) {
	jclass cl = env->FindClass("com/bn/bullet/GL2JNIView");
	jmethodID id = env->GetStaticMethodID(cl, "initTextureRepeat",
			"(Landroid/opengl/GLSurfaceView;Ljava/lang/String;)I");
	//��������Id
	jstring name = env->NewStringUTF("pic/background.png");
	bgTexId = env->CallStaticIntMethod(cl, id, obj, name);

	name = env->NewStringUTF("pic/timegradebeijing.png");
	tgBgTexId = env->CallStaticIntMethod(cl, id, obj, name);

	for (int i = 0; i < 10; i++) {
		char pngPath[10];
		//����ת�ַ���
		sprintf(pngPath, "pic/d%d.png", i);
		name = env->NewStringUTF(pngPath);
		digitTexId[i] = env->CallStaticIntMethod(cl, id, obj, name);
	}
	name = env->NewStringUTF("pic/maohao.png");
	digitTexId[10] = env->CallStaticIntMethod(cl, id, obj, name);

	name = env->NewStringUTF("pic/hengxian.png");
	digitTexId[11] = env->CallStaticIntMethod(cl, id, obj, name);

	name = env->NewStringUTF("pic/d10.png");
	digitTexId[12] = env->CallStaticIntMethod(cl, id, obj, name);

	//ȷ���˵�����Id
	name = env->NewStringUTF("pic/quedinganniu.png");
	QDMenuTextId = env->CallStaticIntMethod(cl, id, obj, name);
	//���ز˵�����Id
	name = env->NewStringUTF("pic/fanhuianniu.png");
	FHMenuTextId = env->CallStaticIntMethod(cl, id, obj, name);
}
void RecordView::calTableResult() {
	//��ѯ������������
	std::vector<std::string>* result = new std::vector<std::string>();
	result->clear();
	std::string sSQL =
			"select grade,time from paihangbang  order by grade desc limit 0,30";

	result = SQLiteUtil::query(sSQL);
	vectorToStringArray(result);
}
void RecordView::vectorToStringArray(std::vector<std::string>* result) {
	//����÷ֵ��ַ�������
	std::vector<std::string>* gradeVector = new std::vector<std::string>();
	//����ʱ����ַ�������
	std::vector<std::string>* timeVector = new std::vector<std::string>();
	//��������count % 2 == 0�ǵ÷֣�count % 2 == 1��ʱ��
	int count = 0;
	//����ԭ������-���е÷ֺ�ʱ��ķ���
	std::vector<std::string>::iterator iter = result->begin();
	for (; iter != result->end(); iter++) {
		//��������Խ��--ֻ��ʾ����ǰConstant::RECORD_COUNT�ļ�¼
		if (count >= Constant::RECORD_COUNT * 2) {
			break;
		}
		if (count % 2 == 0) {
			gradeVector->push_back(*iter);
		} else {
			timeVector->push_back(*iter);
		}
		count++;
	}
	//����÷�ͼƬ���Ƶ��ַ�������
	gradePNGVector = new std::vector<std::vector<GLuint>>();
	//����ʱ��ͼƬ���Ƶ��ַ�������
	timePNGVector = new std::vector<std::vector<GLuint>>();

	//�����÷�
	std::vector<std::string>::iterator iterGrade = gradeVector->begin();
	for (; iterGrade != gradeVector->end(); iterGrade++) {
		std::vector<GLuint>* gotGradePNGVector = analyzeStringToPNGVector(
				(*iterGrade));
		gradePNGVector->push_back(*gotGradePNGVector);
	}
	//����ʱ��
	for (iterGrade = timeVector->begin(); iterGrade != timeVector->end();
			iterGrade++) {
		std::vector<GLuint>* gotTimePNGVector = analyzeStringToPNGVector(
				(*iterGrade));
		timePNGVector->push_back(*gotTimePNGVector);
	}
}
std::vector<GLuint>* RecordView::analyzeStringToPNGVector(
		std::string strArray) {
	//�������
	std::vector<GLuint>* resultVector = new std::vector<GLuint>();
	//����ַ���
	GLuint res = 0;
	for (int j = 0; j < strArray.size(); j++) {
		//���������ַ�
		char cTemp = strArray.c_str()[j];
		//�ж��ַ���Ӧ��PNG
		switch (cTemp) {
		case '0':
			res = digitTexId[0];
			break;
		case '1':
			res = digitTexId[1];
			break;
		case '2':
			res = digitTexId[2];
			break;
		case '3':
			res = digitTexId[3];
			break;
		case '4':
			res = digitTexId[4];
			break;
		case '5':
			res = digitTexId[5];
			break;
		case '6':
			res = digitTexId[6];
			break;
		case '7':
			res = digitTexId[7];
			break;
		case '8':
			res = digitTexId[8];
			break;
		case '9':
			res = digitTexId[9];
			break;
		case ':':
			res = digitTexId[10];
			break;
		case '-':
			res = digitTexId[11];
			break;
		case ' ':
			res = digitTexId[12];
			break;
		}
		resultVector->push_back(res);
	}
	return resultVector;
}

void RecordView::onTouchBegan(float touchX, float touchY) {
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
void RecordView::onTouchMoved(float touchX, float touchY) {
}
void RecordView::onTouchEnded(float touchX, float touchY) {
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
int RecordView::judgeMenuTouched(float x, float y) {
	if(y<(Constant::MENU_QD_FH_HEIGHT_HALF-0.8)
				&&y>(-Constant::MENU_QD_FH_HEIGHT_HALF-0.8))
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
