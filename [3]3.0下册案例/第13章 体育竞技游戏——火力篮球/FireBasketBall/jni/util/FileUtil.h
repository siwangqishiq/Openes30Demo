#ifndef __FileUtil_H__
#define __FileUtil_H__

#include "android/asset_manager.h"
#include "android/asset_manager_jni.h"
#include <string>
#include "jni.h"

#include <vector>
using namespace std;//ָ��ʹ�õ������ռ�
class FileUtil
{
  public:
	static AAssetManager* aam;//ָ��AAssetManager�����ָ��
	static void setAAssetManager(AAssetManager* aamIn);//��ʼ��AAssetManager����
	static string loadShaderStr(string fname);//������ɫ��
	static void loadFromFile(string fname);	//����objģ��

  private:
	static void analyze(char* data,int size);
	static std::string assemblyString(char *data);
	//�и��ַ���
	static void stringSplit(std::string aString,const char* split);
	static void saveVerticesData(std::string aString);
	static void saveTextureData(std::string aString);
	static void saveNormalData(std::string aString);
	static void saveVerticesIndexData();
	static void VertorToFloatpoint();
	static void clearAllAtrri();
  private:
	static int currCount;
	static std::vector<int>* resultVec;
	//����ԭʼ�����������ݵ�����
	static std::vector<float>* verticesVec ;
	//����ԭʼ�����������ݵ�����
	static std::vector<int>* verticesIndexVec ;
	//����ԭʼ�����������ݵ�����
	static std::vector<float>* texVec ;
	//����ԭʼ���������������ݵ�����
	static std::vector<int>* texIndexVec ;
	//����ԭʼ�������������ݵ�����
	static std::vector<float>* normalVec ;
	//����ԭʼ�����������������ݵ�����
	static std::vector<int>* normalIndexVec ;
  public:
	static float* m_vertices;		//��������
	static int* m_verticesIndices;	//������������
	static float* m_tex;			//��������
	static int* m_texIndices;		//������������
	static float* m_normal;			//����������
	static int* m_normalIndices;	//��������������
	//������Ŀ
	static int numVertices;
	//����������Ŀ
	static int numVerticesIndex;
	//������Ŀ
	static int numTex;
	//����������Ŀ
	static int numTexIndex;
	//��������Ŀ
	static int numNormal;
	//������������Ŀ
	static int numNormalIndex;


	static std::string SDCardPath;
	static void setSDCardPath(std::string SDCardPathTemp);
	static void loadFromSDCardFile(std::string sdCardFilePath);			//��SDCard�ļ�����
	static void spliteStrin(std::string aString,const char* split);
};
#endif
