#ifndef _LoadingView_H_
#define _LoadingView_H_

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
#include "jni.h"
#include <thread>

#include "MyBullet/TextureRect.h"
using namespace std;
class LoadingView {
public:
	LoadingView();
	~LoadingView();
public:
	//������
	void onViewCreated(JNIEnv * env, jobject obj);
	//���ı�
	void onViewchanged(int w, int h);
	//���Լ�
	void drawSelf();
	//��ʼ���������
	void initTextureRect();
	//��ʼ������Id
	void initTextureId(JNIEnv * env, jobject obj);
	//תȦ�߳�
	void threadTask();
	//������Դ
	void intiAllAssats(JNIEnv * env, jobject obj);
public:
	float ratio;
	//��������Id
	GLuint bgTexId;
	//�����������
	TextureRect* bgTextureRect;
	//��ת������Id
	GLuint XZTexId;
	//��ת�������
	TextureRect* XZTextureRect;
	//��ת�ĽǶ�
	int angle;

	thread* t;
	bool loadingFlag;
	int loadTime;

	bool isAttach;
	int loadIndex;
};

#endif
