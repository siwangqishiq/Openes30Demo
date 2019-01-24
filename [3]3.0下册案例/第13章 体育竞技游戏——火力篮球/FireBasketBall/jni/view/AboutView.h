#ifndef _AboutView_H_
#define _AboutView_H_

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
#include <jni.h>

#include "MyBullet/TextureRect.h"

class AboutView
{
public:
	AboutView();
	~AboutView();
public:
	//������
	void onViewCreated(JNIEnv * env,jobject obj);
	//���ı�
	void onViewchanged(int w, int h);
	//���Լ�
	void drawSelf();//������Ҫ���ƵĲ˵�Id������id���ƶ�Ӧ�Ĳ˵�
	//��ʼ������Id
	void initTextureId(JNIEnv * env,jobject obj);
    //��ʼ���������
	void initTextureRect();
	//��ʼ���˵�
	void initMenu();
	void onTouchBegan(float touchX,float touchY);
	void onTouchMoved(float touchX,float touchY);
	void onTouchEnded(float touchX,float touchY);
	int judgeMenuTouched(float x, float y);
public:
	//��������Id
	GLuint bgTexId;
	//������Ϣ����Id
	GLuint aboutTexId;
	//ȷ���˵�����Id
	GLuint QDMenuTextId;
	//���š��㻹�˵�����Id
	GLuint FHMenuTextId;

	//��������
	TextureRect* bgTextureRect;
	//������Ϣ����
	TextureRect* aboutTextureRect;
	//�˵��������
	TextureRect* menuTextureRect;
	//����
	float ratio;
};

#endif
