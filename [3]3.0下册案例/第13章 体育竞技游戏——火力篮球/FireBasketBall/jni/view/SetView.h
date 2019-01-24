#ifndef _SetView_H_
#define _SetView_H_
#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>

#include "MyBullet/TextureRect.h"
#include "jni.h"
class SetView{
public:
	SetView();
	~SetView();
public:
	//������
	void onViewCreated(JNIEnv * env,jobject obj);
	//���ı�
	void onViewchanged(int w, int h);
	//���Լ�
	void drawSelf();//������Ҫ���ƵĲ˵�Id������id���ƶ�Ӧ�Ĳ˵�
	//��ʼ���������
	void initTextureRect();
	//��ʼ������Id
	void initTextureId(JNIEnv * env,jobject obj);
	//������ʼ���ص�
	void onTouchBegan(float touchX,float touchY);
	//�����ƶ����ص�
	void onTouchMoved(float touchX,float touchY);
	//�����ƶ����ص�
	void onTouchEnded(float touchX,float touchY);
	int judgeMenuTouched(float x,float y);
public:
	//��������Id
	GLuint bgTexId;
	//�����������
	TextureRect* bgTextureRect;
	//������������Id
	GLuint BJYY_TexId;
	//��Ч��������Id
	GLuint YXYY_TexId;
	//�������֡���Ч�����������
	TextureRect* infoTextureRect;
	//��������Id
	GLuint menu_QD_TexId;
	//��������Id
	GLuint menu_FH_TexId;
	//ȷ����ȡ���������
	TextureRect* menuTextureRect;

	//ѡ�п�����Id
	GLuint menu_KUANG_TexId;
	TextureRect* kuangTextureRect;

	//�Թ�����Id
	GLuint KUANG_DUIGOU_TexId;

	float ratio;
};

#endif
