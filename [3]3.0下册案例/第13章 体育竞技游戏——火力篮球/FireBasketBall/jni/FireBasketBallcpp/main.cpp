#include <stdio.h>
#include <math.h>

#include <string.h>
#include <jni.h>

#include <vector>
#include "Bullet/LinearMath/btAlignedObjectArray.h"
#include "Bullet/btBulletDynamicsCommon.h"
#include "Bullet/BulletSoftBody/btSoftBody.h"
#include "Bullet/BulletSoftBody/btSoftBodyHelpers.h"
#include "Bullet/BulletSoftBody/btSoftRigidDynamicsWorld.h"
#include "Bullet/BulletSoftBody/btSoftBodySolvers.h"
#include "Bullet/BulletSoftBody/btDefaultSoftBodySolver.h"
#include "Bullet/BulletSoftBody/btSoftBodyRigidBodyCollisionConfiguration.h"

#include "util/Matrix.h"
#include "util/MatrixState.h"
#include "util/FileUtil.h"
#include "util/Constant.h"
#include "FireBasketBallcpp/main.h"
#include "MyBullet/TexTriangleMesh.h"
#include "MyBullet/SoftObj.h"
#include "SQLiteUtil/SQLiteUtil.h"
#include "util/FileUtil.h"
using namespace std;

GameView* main::gameView;
MenuView* main::menuView;
AboutView* main::aboutView;
SetView* main::setView;
LoadingView* main::loadingView;
RecordView* main::recordView;

JNIEnv * main::envMain;
jobject main::objMain;

//ȫ�ֱ���
JavaVM *main::g_jvm = NULL;
jobject main::g_obj = NULL;
void main::NDKCallExit(JNIEnv * env,jobject obj)
{
    //Attach���߳�
    if((*main::g_jvm).AttachCurrentThread(&env, NULL) != JNI_OK)
    {
        return ;
    }
//	//��ȡ�����ļ�����û�л�ȡ���������û�д���
	jclass cls = env->FindClass("com/bn/bullet/JNIPort");
	if(cls != NULL)
	{
		//��ȡ�����ļ���NDKCallExit��������û�л�ȡ���������û�д˷���
//			GetStaticMethodID������ز���1.java�࣬2.��������3.����,��������
		jmethodID id = env->GetStaticMethodID(cls,"NDKCallExit","()V");
		if(id != NULL)
		{
			env->CallStaticVoidMethod(cls, id);
		}
	}
	//Detach���߳�
	if((*main::g_jvm).DetachCurrentThread() != JNI_OK)
	{
		return ;
	}
}
void main::playBackmusic(JNIEnv * env,jobject obj,jint flag)
{
    //Attach���߳�
    if((*main::g_jvm).AttachCurrentThread(&env, NULL) != JNI_OK)
    {
        return ;
    }
	//��ȡ�����ļ�����û�л�ȡ���������û�д���
	jclass cls = env->FindClass("com/bn/bullet/JNIPort");
	if(cls != NULL)
	{
		//��ȡ�����ļ���NDKCallExit��������û�л�ȡ���������û�д˷���
		//GetStaticMethodID������ز���1.java�࣬2.��������3.����,��������
		jmethodID id = env->GetStaticMethodID(cls,"playBackMusic","(I)V");
		if(id != NULL)
		{
			env->CallStaticVoidMethod(cls, id,flag);
		}
	}
}
void main::sendViewIndex(JNIEnv* env,jobject obj,jint currViewIn)
{
    //Attach���߳�
    if((*main::g_jvm).AttachCurrentThread(&env, NULL) != JNI_OK)
    {
        return ;
    }
	//��ȡ�����ļ�����û�л�ȡ���������û�д���
	jclass cls = env->FindClass("com/bn/bullet/JNIPort");

	if(cls != NULL)
	{
		//��ȡ�����ļ���NDKCallExit��������û�л�ȡ���������û�д˷���
		//	GetStaticMethodID������ز���1.java�࣬2.��������3.����,��������
		jmethodID id = env->GetStaticMethodID(cls,"recordCurrViewIndex","(I)V");
		if(id != NULL)
		{
			jvalue jvaluetemp[1];
			jvaluetemp[0].i = currViewIn;
			env->CallStaticVoidMethodA(cls, id,jvaluetemp) ; // CallStaticVoidMethod(cls, id,currViewIn);
		}
	}
}

#define RIGID_KUANG_OBJ 1//����OBJ����
#define SOFT_NET_OBJ 2//����OBJ����
void main::loadObjDataWd(
		int objId,
		float* vertices,
		int numsVer,
		int* indices,
		int numsInd,
		float* texs,
		int numsTex,
		float* normal,
		int numNormal
)
{
	if(objId == RIGID_KUANG_OBJ)
	{
		TexTriangleMesh::vertices_yuanhuan = (float*)vertices;	//��������
		TexTriangleMesh::numsVer_yuanhuan = numsVer;			//�������--------------������*3*3
		TexTriangleMesh::indices_yuanhuan = (int*)indices;		//��������
		TexTriangleMesh::numsInd_yuanhuan = numsInd;			//��������
		TexTriangleMesh::texs_yuanhuan = (float*)texs;			//��������
		TexTriangleMesh::numsTex_yuanhuan = numsTex;			//�����������

		TexTriangleMesh::normal_yuanhuan = (float*)normal;
		TexTriangleMesh::numNormal_yuanhuan = numNormal;
	}
	else if(objId == SOFT_NET_OBJ)
	{
		SoftObj::vertices_soft = (float*)vertices;
		SoftObj::numsVer_soft = numsVer;
		SoftObj::indices_soft = (int*)indices;
		SoftObj::numsInd_soft = numsInd;
		SoftObj::texs_soft = (float*)texs;
		SoftObj::numsTex_soft = numsTex;
		SoftObj::normal_soft = (float*)normal;
		SoftObj::numNormal_soft = numNormal;
	}
}
float* main::copyFloats(float* src,int count)
{
	float* dst=new float[count];
	for(int i=0;i<count;i++)
	{
		dst[i]=src[i];
	}
	return dst;
}
int* main::copyInts(int* src,int count)
{
	int* dst=new int[count];
	for(int i=0;i<count;i++)
	{
		dst[i]=src[i];
	}
	return dst;
}
//����  ����Ͷ��
extern "C" {
	bool onSurfaceChanged(int w, int h) {
		Constant::w = w;
		Constant::h = h;
		if(!Constant::SWAP_VIEW_FLAG)
		{
			//�жϵ�ǰ����Ϸ����
			if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_GAME_INDEX)		//��Ϸ������
			{
				main::gameView->onViewchanged(w,h);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_MENU_INDEX)	//�˵�����
			{
				main::menuView->onViewchanged(w,h);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_ABOUT_INDEX)	//���ڽ���
			{
				main::aboutView->onViewchanged(w,h);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_SET_INDEX)	//���ý���
			{
				main::setView->onViewchanged(w,h);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_LOADING_INDEX)
			{
				main::loadingView->onViewchanged(w,h);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_RECORD_INDEX)
			{
				main::recordView->onViewchanged(w,h);
			}
		}
		return true;
	}
	void initDatabase()
	{
		//�������
		std::string sSQL1 = "create table if not exists paihangbang(grade int(4),time char(20))";
		//����
	    SQLiteUtil::doSqlite3_exec(sSQL1);
	}
	bool onSurfaceCreated(JNIEnv * env,jobject obj) {

		main::envMain = env;
		main::objMain = env->NewGlobalRef(obj);

		env->GetJavaVM(&main::g_jvm);
		main::g_obj = env->NewGlobalRef(obj);

		//��ʼ�����ݿ�
		initDatabase();

		//������Դ����
		main::loadingView = new LoadingView();
		main::loadingView->onViewCreated(env,obj);

		return true;
	}

	void renderFrame() {
		if(!Constant::SWAP_VIEW_FLAG)
		{
			//�жϵ�ǰ����Ϸ����
			if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_GAME_INDEX)
			{
				glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
				main::gameView->drawSelf();
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_MENU_INDEX)
			{
				glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
				main::menuView->drawSelf(Constant::MENU_CURR_MENUINDEX);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_ABOUT_INDEX)
			{
				glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
				main::aboutView->drawSelf();
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_SET_INDEX)	//���ý���
			{
				glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
				main::setView->drawSelf();
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_LOADING_INDEX)
			{
				glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
				main::loadingView->drawSelf();
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_RECORD_INDEX)
			{
				glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
				main::recordView->drawSelf();
			}
		}
	}
	char*   Jstring2CStr(JNIEnv*   env,   jstring   jstr)
	{
	     char*   rtn   =   NULL;
	     jclass   clsstring   =   (*env).FindClass("java/lang/String");
	     jstring   strencode   =   (*env).NewStringUTF("GB2312");
	     jmethodID   mid   =   (*env).GetMethodID(clsstring,"getBytes",   "(Ljava/lang/String;)[B");
	     jbyteArray   barr=   (jbyteArray)(*env).CallObjectMethod(jstr,mid,strencode); // String .getByte("GB2312");
	     jsize   alen   =   (*env).GetArrayLength(barr);
	     jbyte*   ba   =   (*env).GetByteArrayElements(barr,JNI_FALSE);
	     if(alen   >   0)
	     {
	      rtn   =   (char*)malloc(alen+1);         //new   char[alen+1]; "\0"
	      memcpy(rtn,ba,alen);
	      rtn[alen]=0;
	     }
	     (*env).ReleaseByteArrayElements(barr,ba,0);  //�ͷ��ڴ�

	     return rtn;
	}
	JNIEXPORT void JNICALL Java_com_bn_bullet_JNIPort_sendSDCardPath
	  (JNIEnv *env, jclass cls, jstring SDCardPathin)
	{
		char *SDCardPathTemp = Jstring2CStr(env, SDCardPathin);
		std::string path((const char*)SDCardPathTemp);//��ý���ַ���
		FileUtil::setSDCardPath(path);
	}
	JNIEXPORT void JNICALL Java_com_bn_bullet_JNIPort_nativeSetAssetManager
	  (JNIEnv *env, jclass cls, jobject assetManager)
	{
		AAssetManager* aamIn = AAssetManager_fromJava( env, assetManager );
	    FileUtil::setAAssetManager(aamIn);
	}
	//�����ص�����
	JNIEXPORT void JNICALL Java_com_bn_bullet_JNIPort_onTouchBegan(JNIEnv *env, jclass jc, jfloat touchX,jfloat touchY)
	{
		if(!Constant::SWAP_VIEW_FLAG)
		{
			//�жϵ�ǰ����Ϸ����
			if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_GAME_INDEX)//��Ϸ����
			{
				main::gameView->onTouchBegan(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_MENU_INDEX)//��Ϸ�˵�����
			{
				main::menuView->onTouchBegan(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_ABOUT_INDEX)//��Ϸ���ڽ���
			{
				main::aboutView->onTouchBegan(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_SET_INDEX)	//���ý���
			{
				main::setView->onTouchBegan(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_RECORD_INDEX)
			{
				main::recordView->onTouchBegan(touchX,touchY);
			}
		}
	}
	JNIEXPORT void JNICALL Java_com_bn_bullet_JNIPort_onTouchMoved(JNIEnv *env, jclass jc, jfloat touchX,jfloat touchY)
	{
		if(!Constant::SWAP_VIEW_FLAG)
		{
			//�жϵ�ǰ����Ϸ����
			if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_GAME_INDEX)//��Ϸ����
			{
				main::gameView->onTouchMoved(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_MENU_INDEX)//��Ϸ�˵�����
			{
				main::menuView->onTouchMoved(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_ABOUT_INDEX)//��Ϸ���ڽ���
			{
				main::aboutView->onTouchMoved(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_SET_INDEX)	//���ý���
			{
				main::setView->onTouchMoved(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_RECORD_INDEX)
			{
				main::recordView->onTouchMoved(touchX,touchY);
			}
		}
	}
	JNIEXPORT void JNICALL Java_com_bn_bullet_JNIPort_onTouchEnded(JNIEnv *env, jclass jc, jfloat touchX,jfloat touchY)
	{
		if(!Constant::SWAP_VIEW_FLAG)
		{
			//�жϵ�ǰ����Ϸ����
			if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_GAME_INDEX)//��Ϸ����
			{
				main::gameView->onTouchEnded(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_MENU_INDEX)//��Ϸ�˵�����
			{
				main::menuView->onTouchEnded(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_ABOUT_INDEX)//��Ϸ���ڽ���
			{
				main::aboutView->onTouchEnded(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_SET_INDEX)	//���ý���
			{
				main::setView->onTouchEnded(touchX,touchY);
			}else if(Constant::VIEW_CUR_GL_INDEX == Constant::VIEW_RECORD_INDEX)
			{
				main::recordView->onTouchEnded(touchX,touchY);
			}
		}
	}
	JNIEXPORT void JNICALL Java_com_bn_bullet_JNIPort_sendWidthAndHeight(JNIEnv *env, jclass jc, jfloat width,jfloat height)
	{
		Constant::SCREEN_HEIGHT = height;
		Constant::SCREEN_WIDTH = width;

        float zoomx=Constant::SCREEN_WIDTH/1080;
		float zoomy=Constant::SCREEN_HEIGHT/1920;
		if(zoomx>zoomy){
			Constant::SCREEN_WIDTH_SCALE=Constant::SCREEN_HEIGHT_SCALE=zoomy;
		}else
		{
			Constant::SCREEN_WIDTH_SCALE=Constant::SCREEN_HEIGHT_SCALE=zoomx;
		}
//		Constant::sXtart=(Constant::SCREEN_WIDTH-Constant::SCREEN_WIDTH*Constant::SCREEN_WIDTH_SCALE)/2;
//		Constant::sYtart=(Constant::SCREEN_HEIGHT-Constant::SCREEN_HEIGHT*Constant::SCREEN_HEIGHT_SCALE)/2;

		Constant::RADIO = (float)(1080) / (float)(1920);
//		Constant::RADIO = width / height;

	}
	JNIEXPORT void JNICALL Java_com_bn_bullet_JNIPort_toMenuView(JNIEnv *env, jclass jc)
	{
		Constant::SWAP_VIEW_FLAG = true;
		Constant::VIEW_CUR_GL_INDEX = Constant::VIEW_MENU_INDEX;
		Constant::MENU_CURR_MENUINDEX = Constant::MENU_STARTVIEW_START;
		main::menuView->onViewchanged(Constant::w, Constant::h);
		Constant::SWAP_VIEW_FLAG = false;
	}
//	//��java����������JNI����
//	JNIEXPORT void Java_com_bn_bullet_JNIPort_setJNIEnv( JNIEnv* env, jobject obj)
//	 {
//	     //����ȫ��JVM�Ա������߳���ʹ��
//	     (*env).GetJavaVM(&main::g_jvm);
//	     //����ֱ�Ӹ�ֵ(g_obj = obj)
//	     main::g_obj = (*env).NewGlobalRef(obj);
//	 }

}


