#include "Constant.h"
#include "math.h"

std::string SDCardPath = "";

float Constant::RADIO = 0;

//onViewChange��w,h
int Constant::w = 0;
int Constant::h = 0;
//�����л��ı�־λ
bool Constant::SWAP_VIEW_FLAG = false;
//��ֵ
const float Constant::PI = 3.1415926;
//���ӵĳ�������

const float Constant::CHANGJING_WIDTH=4.0f;
const float Constant::CHANGJING_HEIGHT=7.2f;
const float Constant::CHANGJING_LENGTH=4.0f;
//�Ǳ��
const float Constant::YBB_WIDTH = 2;
const float Constant::YBB_HEIGHT = 0.3f;

//�����Ŀ��λ��
float Constant::CAMERA_X=0.0f;
float Constant::CAMERA_Y=CHANGJING_HEIGHT/2-0.35f;
float Constant::CAMERA_Z=(CHANGJING_LENGTH+2.4f+8.5f);

const float Constant::DISTANCE=CAMERA_Z;//LENGTH;


int Constant::VIEW_CUR_GL_INDEX = 5;
//��Ϸ����
const int Constant::VIEW_GAME_INDEX = 1;
//��Ϸ��������
const int Constant::VIEW_MENU_INDEX = 2;
//��Ϸ���ڽ���
const int Constant::VIEW_ABOUT_INDEX = 3;
//���ý���
const int Constant::VIEW_SET_INDEX = 4;
//���ؽ���
const int Constant::VIEW_LOADING_INDEX = 5;
//��¼����
const int Constant::VIEW_RECORD_INDEX = 6;
//��ǰ��Ҫ��ʾ�Ĳ˵����
int Constant::MENU_CURR_MENUINDEX = 0;
//��ʼ����˵�
const int Constant::MENU_STARTVIEW_START = 0;
//��Ϸ�����˵�
const int Constant::MENU_GAMEVIEW_END = 1;

//�ж��Ƿ��ǰ�������ı�־λ
bool Constant::isnoHelpView = false;

int Constant::shipingJs = 0;//��Ƶ�̼߳�ʱ��
const float Constant::STARTBALL_V[3][3]={
		{0.90f,10.8f,-3.0f},
		{0,10.8f,-3.0f},
		{-0.90f,10.8f,-3.0f}
};

//�����ֵ�����
float Constant::shouX = 0;
float Constant::shouY = -1;

float Constant::SCREEN_WIDTH = 480;
float Constant::SCREEN_HEIGHT = 854;

float Constant::SCREEN_WIDTH_SCALE = 1;
float Constant::SCREEN_HEIGHT_SCALE = 1;

float Constant::BASKETBALL_R = 0.35;

//float Constant::BASKETBALL_ORIGIN_X[3] = {-1,0,1};
//float Constant::BASKETBALL_ORIGIN_Y[3] = {0.5,0.5,0.5};
//float Constant::BASKETBALL_ORIGIN_Z[3] = {1.6,1.6,1.6};

const float Constant::LANBAN_X=0;//�����λ��x����
const float Constant::LANBAN_Y=5;//�����λ��y����
const float Constant::LANBAN_Z=-1.9f;//�����λ��z����

const float Constant::LANKUANG_X=0;//�����λ��x����
const float Constant::LANKUANG_Y=4.5;//�����λ��y����
const float Constant::LANKUANG_Z=-1.3f;//�����λ��z����

const float Constant::LANKUANG_R = 0.65;//����뾶

const float Constant::LANWANG_H = 4.5;//�����ĸ߶�

float Constant::gFactor=1.6f;							//�������ٶ����ű���
float Constant::vFactor=(float)sqrt(Constant::gFactor);	//y�����ٶ����ű���

float Constant::sXtart = 0;
float Constant::sYtart = 0;

//�Ǳ���е������ֵĴ�С
const float Constant::SHUZI_KUANDU= 0.1f;
const float Constant::SHUZI_GAODU= 0.12f;

//���������Ӧλ������
const float Constant::STARTBALL_1[3] = {-1.0f,0.4f,1.6f};
const float Constant::STARTBALL_2[3] = {0,0.4f,1.6f};
const float Constant::STARTBALL_3[3] = {1.0f,0.4f,1.6f};

const float Constant::STARTBALL[3][3] = {
										{Constant::STARTBALL_1[0],Constant::STARTBALL_1[1],Constant::STARTBALL_1[2]},
										{Constant::STARTBALL_2[0],Constant::STARTBALL_2[1],Constant::STARTBALL_2[2]},
										{Constant::STARTBALL_3[0],Constant::STARTBALL_3[1],Constant::STARTBALL_3[2]}
									 };


//�÷�
int Constant::DIGITAL_SCORE = 13;
//��ʱ��
float Constant::DIGITAL_TOTAL_TIME = 560;
//ʱ��
float Constant::DIGITAL_TIME = 0;

//��Ϸʱ�������־λ
bool Constant::GAMEOVER = false;

//ÿ���˵��ļ��
const float Constant::MENU_SPAN = 0.5;
//ÿ���˵����εĳ�
const float Constant::MENU_HEIGHT_HALF = 0.08;
//ÿ���˵����εĿ�
const float Constant::MENU_WIDTH_HALF = 0.3;


//������Ĳ˵����
int Constant::TOUCHED_CURR_MENU = 0;
//�Ƿ񱻵���ı�־λ---���鳤��Ϊ�˵�����
bool Constant::isTouched[15] = {false,false,false,false,false,false,false,false,false,false,false,false,false,false,false};
const float Constant::MENU_SACLE_X = 1.1;
const float Constant::MENU_SACLE_Y = 1.1;
const float Constant::MENU_SACLE_Z = 1.0;
//û�в˵�����
const int Constant::TOUCHED_NONE_MENU = 0;
//��ʼ��Ϸ�˵������
const int Constant::TOUCHED_KSYX_MENU = 1;
//������Ϸ�˵������
const int Constant::TOUCHED_SZ_MENU = 2;
//������Ϸ�˵������
const int Constant::TOUCHED_GY_MENU = 3;
//������Ϸ�˵������
const int Constant::TOUCHED_BZ_MENU = 4;
//��¼��Ϸ�˵������
const int Constant::TOUCHED_JL_MENU = 5;
//�˳���Ϸ�˵������
const int Constant::TOUCHED_TC_MENU = 6;
//����һ����Ϸ�˵������
const int Constant::TOUCHED_ZLYJ_MENU = 7;
//���ز˵���Ϸ�˵������
const int Constant::TOUCHED_FHCD_MENU = 8;

//ȷ���˵���Ϸ�˵������
const int Constant::TOUCHED_QD_MENU = 9;
//���ز˵���Ϸ�˵������
const int Constant::TOUCHED_FH_MENU = 10;

//ֹͣ�˵������
const int Constant::TOUCH_STOP_MENU = 11;
//��ͣ�˵������
const int Constant::TOUCH_PAUSE_MENU = 12;
//�������ֲ˵������
const int Constant::TOUCH_CJYY_KUANG_MENU = 13;
//�������ֲ˵������
const int Constant::TOUCH_BJYY_KUANG_MENU = 14;

//���ֵĿ�����ʾ��־λ
bool  Constant::isOpen_CJYY = true;
bool  Constant::isOpen_BJYY = true;

//��ͣ��ֹͣ�˵��İ����
const float Constant::MENU_STOP_PAUSE_WIDTH_HALF = 0.07;
const float Constant::MENU_STOP_PAUSE_HEIGHT_HALF = 0.07;

//ȷ�������ز˵��İ����
const float Constant::MENU_QD_FH_WIDTH_HALF = 0.2;
const float Constant::MENU_QD_FH_HEIGHT_HALF = 0.12;

//ѡ�п�˵��İ����
const float Constant::MENU_KUANG_WIDTH_HALF = 0.08;
const float Constant::MENU_KUANG_HEIGHT_HALF = 0.08;


const float* Constant::mianFXL[5][2] = {//����λ�ã�Ȼ���Ƿ�����
	{getBuffer(0,0.1f,0),getBuffer(0,1,0),},//����
	{getBuffer(-CHANGJING_WIDTH/2+0.1f,0,0),getBuffer(1,0,0)},//����
	{getBuffer(CHANGJING_WIDTH/2-0.1f,0,0),getBuffer(-1,0,0)},//����
	{getBuffer(0,0,-CHANGJING_LENGTH/2+0.1f),getBuffer(0,0,1)},//������� ����
	{getBuffer(0,0,LANBAN_Z+0.08+0.1f),getBuffer(0,0,1)}//����Ӱ��ƽ��
};
float* Constant::getBuffer(float x,float y,float z){//�����������꣬�õ�������Ļ���
	float* lightLocation=new float[3];
	lightLocation[0]=x;
	lightLocation[1]=y;
	lightLocation[2]=z;
	return lightLocation;
}
//��¼������
const int Constant::RECORD_COUNT = 10;


//��Ļx���굽�ӿ�x����
float Constant::fromScreenXToNearX(float x)
{
	return (x-Constant::SCREEN_WIDTH/2)/(Constant::SCREEN_HEIGHT/2);
}
//��Ļy���굽�ӿ�y����
float Constant::fromScreenYToNearY(float y)
{
	return -(y-Constant::SCREEN_HEIGHT/2)/(Constant::SCREEN_HEIGHT/2);
}
