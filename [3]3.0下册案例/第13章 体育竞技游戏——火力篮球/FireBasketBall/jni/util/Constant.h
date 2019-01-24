#ifndef _Constant_H_
#define _Constant_H_
#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>

#include <string>
#include <jni.h>

using namespace std;

class Constant
{
public:
	static std::string SDCardPath ;

	static float RADIO ;

	//onViewChange��w,h
	static int w;
	static int h;
	//�����л��ı�־λ
	static bool SWAP_VIEW_FLAG;

	static const float PI;
	//���ӵĳ�������
	static const float CHANGJING_WIDTH;
	static const float CHANGJING_HEIGHT;
	static const float CHANGJING_LENGTH;
	//�Ǳ��
	static const float YBB_WIDTH;
	static const float YBB_HEIGHT;

	//�����Ŀ��λ��
	static float CAMERA_X;
	static float CAMERA_Y;
	static float CAMERA_Z;
	//LENGTH;
	static const float DISTANCE;

	//��������
	static int VIEW_CUR_GL_INDEX;
	//��Ϸ����
	static const int VIEW_GAME_INDEX;
	//��Ϸ�˵�����
	static const int VIEW_MENU_INDEX;
	//���ڲ˵�����
	static const int VIEW_ABOUT_INDEX;
	//���ý���
	static const int VIEW_SET_INDEX;
	//���ؽ���
	static const int VIEW_LOADING_INDEX;
	//��¼����
	static const int VIEW_RECORD_INDEX;
	//��ǰ��Ҫ��ʾ�Ĳ˵����
	static int MENU_CURR_MENUINDEX;
	//��ʼ����˵�
	static const int MENU_STARTVIEW_START ;
	//��Ϸ�����˵�
	static const int MENU_GAMEVIEW_END ;
	//�ж��Ƿ��ǰ�������ı�־λ
	static bool isnoHelpView;
	static int shipingJs;//��Ƶ�̼߳�ʱ��

	static const float STARTBALL_V[3][3];

	//�����ֵ�����
	static float shouX;
	static float shouY;
	//��Ļ�ߴ�
	static float SCREEN_WIDTH ;
	static float SCREEN_HEIGHT ;

	//��Ļ���ű�
	static float SCREEN_WIDTH_SCALE;
	static float SCREEN_HEIGHT_SCALE;

	//����뾶
	static float BASKETBALL_R;
	//�����ʼλ��
//	static float BASKETBALL_ORIGIN_X[3];
//	static float BASKETBALL_ORIGIN_Y[3];
//	static float BASKETBALL_ORIGIN_Z[3];

	static const float LANBAN_X;//�����λ��x����
	static const float LANBAN_Y;//�����λ��y����
	static const float LANBAN_Z;//�����λ��z����

	static const float LANKUANG_X;//�����λ��x����
	static const float LANKUANG_Y;//�����λ��y����
	static const float LANKUANG_Z;//�����λ��z����

	static const float LANKUANG_R;//����뾶

	static const float LANWANG_H;//�����ĸ߶�

	static float gFactor;//�������ٶ����ű���
	static float vFactor;//y�����ٶ����ű���

	//��Ļ��ʼ��X,Y
	static float sXtart;
	static float sYtart;

	//�Ǳ���е������ֵĴ�С
	static float const SHUZI_KUANDU;
	static float const SHUZI_GAODU;

	static const float STARTBALL_1[3];
	static const float STARTBALL_2[3] ;
	static const float STARTBALL_3[3] ;
	static const float STARTBALL[3][3] ;

	//�÷�
	static int DIGITAL_SCORE;
	//��ʱ��
	static float DIGITAL_TOTAL_TIME;
	//���ĵ�ʱ��
	static float DIGITAL_TIME;
	//��Ϸʱ�������־λ
	static bool GAMEOVER;


	//ÿ���˵��ļ��
	static const float MENU_SPAN ;
	//ÿ���˵����εĳ�
	static const float MENU_HEIGHT_HALF ;
	//ÿ���˵����εĿ�
	static const float MENU_WIDTH_HALF ;

	//������Ĳ˵����
	static int TOUCHED_CURR_MENU;
	//�Ƿ񱻵���ı�־λ---���鳤��Ϊ�˵�����
	static bool isTouched[15];
	static const float MENU_SACLE_X ;
	static const float MENU_SACLE_Y ;
	static const float MENU_SACLE_Z ;
	//û����Ϸ�˵������
	static const int TOUCHED_NONE_MENU;
	//��ʼ��Ϸ�˵������
	static const int TOUCHED_KSYX_MENU;
	//������Ϸ�˵������
	static const int TOUCHED_SZ_MENU;
	//������Ϸ�˵������
	static const int TOUCHED_GY_MENU;
	//������Ϸ�˵������
	static const int TOUCHED_BZ_MENU;
	//��¼��Ϸ�˵������
	static const int TOUCHED_JL_MENU;
	//�˳���Ϸ�˵������
	static const int TOUCHED_TC_MENU;
	//����һ����Ϸ�˵������
	static const int TOUCHED_ZLYJ_MENU;
	//���ز˵���Ϸ�˵������
	static const int TOUCHED_FHCD_MENU;

	//ȷ���˵���Ϸ�˵������
	static const int TOUCHED_QD_MENU;
	//���ز˵���Ϸ�˵������
	static const int TOUCHED_FH_MENU;

	//ֹͣ�˵������
	static const int TOUCH_STOP_MENU;
	//��ͣ�˵������
	static const int TOUCH_PAUSE_MENU;

	//�������ֲ˵������
	static const int TOUCH_CJYY_KUANG_MENU;
	//�������ֲ˵������
	static const int TOUCH_BJYY_KUANG_MENU;
	//���ֵĿ�����ʾ��־λ
	static bool isOpen_CJYY;
	static bool isOpen_BJYY;

	//��ͣ��ֹͣ�˵��İ����
	static const float  MENU_STOP_PAUSE_WIDTH_HALF;
	static const float  MENU_STOP_PAUSE_HEIGHT_HALF;

	//ȷ�������ز˵��İ����
	static const float  MENU_QD_FH_WIDTH_HALF;
	static const float  MENU_QD_FH_HEIGHT_HALF;

	//ѡ�п�˵��İ����
	static const float  MENU_KUANG_WIDTH_HALF;
	static const float  MENU_KUANG_HEIGHT_HALF;

	static const float* mianFXL[5][2];//����λ�ã�Ȼ���Ƿ�����
	static float* getBuffer(float x,float y,float z);//�����������꣬�õ�������Ļ���

	//��¼������
	static const int RECORD_COUNT;

	//��Ļx���굽�ӿ�x����
	static float fromScreenXToNearX(float x);
	//��Ļy���굽�ӿ�y����
	static float fromScreenYToNearY(float y);
};

#endif
