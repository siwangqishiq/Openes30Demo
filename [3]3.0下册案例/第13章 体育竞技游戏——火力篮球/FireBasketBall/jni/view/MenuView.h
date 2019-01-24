#ifndef _MenuView_H_
#define _MenuView_H_
#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
#include <jni.h>

#include "MyBullet/TextureRect.h"

class MenuView
{
public:

	MenuView();
	~MenuView();
	void initGameOverMenu();
	void initTextureId(JNIEnv * env,jobject obj);
public:
	//������
	void onViewCreated(JNIEnv * env,jobject obj);
	//���ı�
	void onViewchanged(int w, int h);
	//���Լ�
	void drawSelf(int MenuId);//������Ҫ���ƵĲ˵�Id������id���ƶ�Ӧ�Ĳ˵�
	//������Ϸ�����˵�
	void drawGameOverMenu();
	//������Ϸ��ʼ�˵�
	void drawGameStartMenu();
	//�ж��ĸ��˵������У�������0�����޲˵������
	int judgeMenuTouched(float x,float y);
	//������ʼ���ص�
	void onTouchBegan(float touchX,float touchY);
	//�����ƶ����ص�
	void onTouchMoved(float touchX,float touchY);
	//�����ƶ����ص�
	void onTouchEnded(float touchX,float touchY);
public:
	//����
	TextureRect* gameOverBgTextureRect;
	//�β˵�
	TextureRect* menuTextureRect;

	float ratio;
	//��������Id
	GLuint bgTexId;
	//����һ������Id
	GLuint ZLYCTexId;
	//�˳�����Id
	GLuint TCTextId;
	//���ز˵�����Id
	GLuint FHCDTextId;
	//��ʼ��Ϸ�˵�����id
	GLuint KSYXTexId;
	//���ò˵�����id
	GLuint SZTexId;
	//���ڲ˵�����id
	GLuint GYTexId;
	//�����˵�����id
	GLuint BZTexId;
	//��¼�˵�����id
	GLuint JLTexId;
};

#endif
