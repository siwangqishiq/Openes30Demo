#include "FileUtil.h"

using namespace std;

#include <stdio.h>
#include <stdlib.h>

#include <iostream>
#include <fstream>


AAssetManager* FileUtil::aam;

int FileUtil::currCount = 0;

//����ԭʼ�����������ݵ�����
std::vector<float>* FileUtil::verticesVec = new std::vector<float>();
//����ԭʼ�����������ݵ�����
std::vector<int>* FileUtil::verticesIndexVec = new std::vector<int>();

//����ԭʼ�����������ݵ�����
std::vector<float>* FileUtil::texVec = new std::vector<float>();
//����ԭʼ���������������ݵ�����
std::vector<int>* FileUtil::texIndexVec = new std::vector<int>();

//����ԭʼ�������������ݵ�����
std::vector<float>* FileUtil::normalVec = new std::vector<float>();
//����ԭʼ�����������������ݵ�����
std::vector<int>* FileUtil::normalIndexVec = new std::vector<int>();
//������ʱ���������
std::vector<int>* FileUtil::resultVec = new std::vector<int>();

//������Ŀ
int FileUtil::numVertices = 0;
//����������Ŀ
int FileUtil::numVerticesIndex = 0;

//������Ŀ
int FileUtil::numTex = 0;
//����������Ŀ
int FileUtil::numTexIndex = 0;

//��������Ŀ
int FileUtil::numNormalIndex = 0;
//������������Ŀ
int FileUtil::numNormal = 0;

float* FileUtil::m_vertices = NULL;//��������
int* FileUtil::m_verticesIndices = NULL;//������������
float* FileUtil::m_tex = NULL;//��������
int* FileUtil::m_texIndices = NULL;//������������
float* FileUtil::m_normal = NULL;//����������
int* FileUtil::m_normalIndices = NULL;//������������

std::string FileUtil::SDCardPath = "";
void FileUtil::setSDCardPath(std::string SDCardPathTemp)
{
	SDCardPath = SDCardPathTemp;
}
void FileUtil::setAAssetManager(AAssetManager* aamIn)//��ʼ��AAssetManager����
{
	aam=aamIn;//��AAssetManager����ֵ
}
void FileUtil::clearAllAtrri()
{
	currCount = 0;

	//����ԭʼ�����������ݵ�����
	verticesVec->clear();
	//����ԭʼ�����������ݵ�����
	verticesIndexVec->clear();
	//����ԭʼ�����������ݵ�����
	texVec->clear();
	//����ԭʼ���������������ݵ�����
	texIndexVec->clear();
	//����ԭʼ�������������ݵ�����
	normalVec->clear();
	//����ԭʼ�����������������ݵ�����
	normalIndexVec->clear();
	//������ʱ���������
	resultVec->clear();

	//������Ŀ
	numVertices = 0;
	//����������Ŀ
	numVerticesIndex = 0;
	//������Ŀ
	numTex = 0;
	//����������Ŀ
	numTexIndex = 0;
	//��������Ŀ
	numNormalIndex = 0;
	//������������Ŀ
	numNormal = 0;

	delete m_vertices;
	m_vertices = NULL ;//��������
	delete m_verticesIndices;
	m_verticesIndices = NULL ;//������������
	delete m_tex;
	m_tex = NULL;//��������
	delete m_texIndices;
	m_texIndices = NULL;//������������
	delete m_normal;
	m_normal = NULL;//����������
	delete m_normalIndices;
	m_normalIndices = NULL;//������������
}
string FileUtil::loadShaderStr(string fname)//������ɫ���ļ�
{
	AAsset* asset =AAssetManager_open(aam,fname.c_str(),AASSET_MODE_UNKNOWN);//����AAsset����
	off_t fileSize = AAsset_getLength(asset);//��ȡAAsset����ĳ���
	unsigned char* data = (unsigned char*) malloc(fileSize + 1);//�����ڴ�
	data[fileSize] = '\0';//��ʼ��
	int size = AAsset_read(asset, (void*)data, fileSize);//��ȡ�ļ���С
	std::string resultStr((const char*)data);//��ý���ַ���
	return resultStr;
}
void FileUtil::loadFromFile(string fname)//����objģ��
{
	clearAllAtrri();
	AAsset* asset =AAssetManager_open(aam,fname.c_str(),AASSET_MODE_UNKNOWN);//����AAsset����
	off_t fileSize = AAsset_getLength(asset);//��ȡAAsset����ĳ���
	char *data = new char[fileSize + 1];//�����ڴ�
	data[fileSize] = '\0';//��ʼ��
	int size = AAsset_read(asset, (void*)data, fileSize);//��ȡ�ļ���С
	std::string resultStr((const char*)data);//��ý���ַ���
	analyze(data,size);
}
void FileUtil::loadFromSDCardFile(std::string sdCardFilePath)
{
	//�������
	clearAllAtrri();
	fstream inFileStream;
	inFileStream.open(SDCardPath + sdCardFilePath,ios::in);
	char *buffer = new char[256];
	const char *splitVTNF = " ";
	const char *splitF = "/";
	while(!inFileStream.eof())
	{
		inFileStream.getline(buffer,256,'\n');			//���л�ȡ---��ȡһ��
		string afString = &buffer[0];
		//���ո��з��ַ���
		char *tempsa = strtok((char*)afString.c_str(),splitVTNF);
		if(tempsa != NULL)
		{
			string tempString = &tempsa[0];
			if(tempString == "f")
			{
				std::string* stringArray = new std::string[3];
				int index = 0;
				for(int i=0;i<3;i++)
				{
					//���ÿո�ָ�
					tempsa = strtok(NULL,splitVTNF);
					std::string temps((const char*)tempsa);//��ý���ַ���
					stringArray[index++] = temps;
				}
				//�Ե�һ���ַ�������
				stringSplit(stringArray[0],splitF);
				saveVerticesIndexData();
				//�Եڶ����ַ�������
				stringSplit(stringArray[1],splitF);
				saveVerticesIndexData();
				//�Ե������ַ�������
				stringSplit(stringArray[2],splitF);
				saveVerticesIndexData();
			}else if(tempString == "v")
			{
				while(tempsa != NULL)
				{
					tempsa = strtok(NULL,splitVTNF);
					if(tempsa != NULL)
					{
						std::string aString = &tempsa[0];
						saveVerticesData(aString);
					}
				}
			}else if(tempString == "vt")
			{
				while(tempsa != NULL)
				{
					tempsa = strtok(NULL,splitVTNF);
					if(tempsa != NULL)
					{
						std::string aString = &tempsa[0];
						saveTextureData(aString);
					}
				}
			}else if(tempString == "vn")
			{
				while(tempsa != NULL)
				{
					tempsa = strtok(NULL,splitVTNF);
					if(tempsa != NULL)
					{
						std::string aString = &tempsa[0];
						saveNormalData(aString);
					}
				}
			}
		}
	}
	//�ر�������
	inFileStream.close();
	VertorToFloatpoint();
}
void FileUtil::spliteStrin(std::string aString,const char* split)
{
	char *p;
	p = strtok((char*)aString.c_str(),split);
	while(p != NULL)
	{
		int it = 0;
		p = strtok(NULL,split);
	}
}
void FileUtil::analyze(char *data,int size)//����objģ��
{
	FileUtil::currCount = 0;
	const char *split = "/";
	while(FileUtil::currCount < size)
	{
		std::string aString = assemblyString(data);//��ȡ�¸��ַ���
		if(aString == "f")
		{
			std::string afString = assemblyString(data);//��ȡ�¸��ַ���
			stringSplit(afString,split);
			saveVerticesIndexData();

			afString = assemblyString(data);//��ȡ�¸��ַ���
			stringSplit(afString,split);
			saveVerticesIndexData();

			afString = assemblyString(data);//��ȡ�¸��ַ���
			stringSplit(afString,split);
			saveVerticesIndexData();
		}else if(aString == "v")
		{
			for(int i=0;i<3;i++)
			{
				std::string afString = assemblyString(data);//��ȡ�¸��ַ���
				saveVerticesData(afString);
			}
		}else if(aString == "vt")
		{
			for(int i=0;i<3;i++)
			{
				std::string afString = assemblyString(data);//��ȡ�¸��ַ���
				saveTextureData(afString);
			}
		}else if(aString == "vn")
		{
			for(int i=0;i<3;i++)
			{
			std::string afString = assemblyString(data);//��ȡ�¸��ַ���
			saveNormalData(afString);
			}
		}
	}
	VertorToFloatpoint();
}
void FileUtil::VertorToFloatpoint()
{
	//��¼������Ŀ
	numVertices = verticesVec->size();
	if(m_vertices != NULL)
	{
		delete m_vertices;
		m_vertices = nullptr;
	}
	m_vertices = new float[numVertices];
	for(int i=0;i<numVertices;i++)
	{
		m_vertices[i] = verticesVec->at(i);
	}

	//��¼����������Ŀ
	numVerticesIndex = verticesIndexVec->size();
	if(m_verticesIndices != NULL)
	{
		delete m_verticesIndices;
		m_verticesIndices = nullptr;
	}
	m_verticesIndices = new int[numVerticesIndex];
	for(int i=0;i<numVerticesIndex;i++)
	{
		m_verticesIndices[i] = verticesIndexVec->at(i)-1;
	}

	//��¼������Ŀ
	numTex = texVec->size();
	float* m_tex_temp = new float[numTex];
	for(int i=0;i<numTex;i++)
	{
		m_tex_temp[i] = texVec->at(i);
	}

	//��¼����������Ŀ
	numTexIndex = texIndexVec->size();
	if(m_texIndices != NULL)
	{
		delete m_texIndices;
		m_texIndices = nullptr;
	}
	m_texIndices = new int[numTexIndex];
	for(int i=0;i<numTexIndex;i++)
	{
		m_texIndices[i] = texIndexVec->at(i);
	}

	//��װ��������
	numTex = numTexIndex*2;
	if(m_tex != NULL)
	{
		delete m_tex;
		m_tex = nullptr;
	}
	m_tex = new float[numTex];
	for(int i=0;i<numTexIndex;i++)
	{
		int index = m_texIndices[i] - 1;
    	m_tex[i*2+0] = m_tex_temp[index*3];
    	m_tex[i*2+1] = m_tex_temp[index*3+1];
	}

	//��¼��������Ŀ
	numNormal = normalVec->size();
	float *m_normal_temp = new float[numNormal];
	for(int i=0;i<numNormal;i++)
	{
		m_normal_temp[i] = normalVec->at(i);
	}
	//��¼������������Ŀ
	numNormalIndex = normalIndexVec->size();
	if(m_normalIndices != NULL)
	{
		delete m_normalIndices;
		m_normalIndices = nullptr;
	}
	m_normalIndices = new int[numNormalIndex];
	for(int i=0;i<numNormalIndex;i++)
	{
		m_normalIndices[i] = normalIndexVec->at(i);
	}

	numNormal = numNormalIndex * 3;
	if(m_normal != NULL)
	{
		delete m_normal;
		m_normal = nullptr;
	}
	m_normal = new float[numNormal];
	for(int i=0;i<numNormalIndex;i++)
	{
		int index = m_normalIndices[i] - 1;
		m_normal[i*3+0] = m_normal_temp[index*3+0];
		m_normal[i*3+1] = m_normal_temp[index*3+1];
		m_normal[i*3+2] = m_normal_temp[index*3+2];
	}
}
void FileUtil::saveVerticesIndexData()
{
	//����ԭʼ��������
	verticesIndexVec->push_back(resultVec->at(0));
	//����ԭʼ��������
	texIndexVec->push_back(resultVec->at(1));
	//����ԭʼ����������
	normalIndexVec->push_back(resultVec->at(2));
}
void FileUtil::saveVerticesData(std::string aString)
{
	//�ַ���ת����======Ϊʲô��С���
	float verticestf = atof(aString.c_str());
	verticesVec->push_back(verticestf);
}
void FileUtil::saveTextureData(std::string aString)
{
	float textf = atof(aString.c_str());
	texVec->push_back(textf);
}
void FileUtil::saveNormalData(std::string aString)
{
	float normaltf = atof(aString.c_str());
	normalVec->push_back(normaltf);
}

void FileUtil::stringSplit(std::string aString,const char* split)
{
	//�������
	resultVec->clear();
	int resultIndex = 0;
	char *p;
	p = strtok((char*)aString.c_str(),split);
	while(p != NULL)
	{
		int it = 0;
		sscanf(p,"%d",&it);		//�ַ�ת����
		resultVec->push_back(it);
		p = strtok(NULL,split);
	}
}
#define KONGGE 32
#define JINHAO 35
#define HUICHE 13
#define HUANHANG 10
std::string FileUtil::assemblyString(char *data)
{
	std::string result = "";
	bool flag = true;
	while(flag)
	{
		char c = data[FileUtil::currCount];//��ȡһ���ַ�
		int ci = (int)c;
		if(ci == KONGGE || ci == HUANHANG)
		{
			FileUtil::currCount ++;
			if(result.size() != 0)
			{
				flag = false;
				return result;
			}
		}else if(ci == JINHAO || ci == HUICHE)
		{
			FileUtil::currCount++;
		}else
		{
			result = result+c;
			FileUtil::currCount++;
		}
	}
	return result;
}
