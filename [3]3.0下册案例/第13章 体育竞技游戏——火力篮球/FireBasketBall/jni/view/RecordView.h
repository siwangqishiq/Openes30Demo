#ifndef _RecordView_H_
#define _RecordView_H_

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
#include "MyBullet/TextureRect.h"
#include "jni.h"

using namespace std;
#include <string>
#include <vector>

class RecordView
{
public:
	RecordView();
	~RecordView(){};
public:
	//������
	void onViewCreated(JNIEnv * env,jobject obj);
	//���ı�
	void onViewchanged(int w, int h);
	//���Լ�
	void drawSelf();
	//��ʼ���������
	void initTextureRect();
	//��ʼ������Id
	void initTextureId(JNIEnv * env,jobject obj);
	//�������ص�����
	void onTouchBegan(float touchX,float touchY);
	void onTouchMoved(float touchX,float touchY);
	void onTouchEnded(float touchX,float touchY);
	//��ѯ��
	void calTableResult();
	//����������
	void vectorToStringArray(std::vector<std::string>* result);
	//��������
	std::vector<GLuint>* analyzeStringToPNGVector(std::string strArray);
	//�ж��Ƿ񱻴���
	int judgeMenuTouched(float x, float y);
public:
	float ratio;

	TextureRect* bgTextureRect ;
	GLuint bgTexId;

	TextureRect* tgBgTextureRect ;
	GLuint tgBgTexId;

	TextureRect* digitTextureRect ;
	GLuint digitTexId[13];

	//�˵��������
	TextureRect* menuTextureRect;
	//ȷ���˵�����Id
	GLuint QDMenuTextId;
	//���š��㻹�˵�����Id
	GLuint FHMenuTextId;

	//����÷�ͼƬ���Ƶ��ַ�������
	std::vector<std::vector<GLuint>>* gradePNGVector ;
	//����ʱ��ͼƬ���Ƶ��ַ�������
	std::vector<std::vector<GLuint>>* timePNGVector ;

};

#endif
