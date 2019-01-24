#include "ReadSDCardFile.h"

#include <iostream>
#include <fstream>


void ReadSDCardFile::readByLine(std::string sdCardFilePath)
{
	char buffer[256];
	//������
	fstream inFileStream;
	inFileStream.open("/sdcard/" + sdCardFilePath,ios::in);//���ļ�
	while(!inFileStream.eof())
	{
		inFileStream.getline(buffer,256,'\n');			//���л�ȡ
		string s(&buffer[0],&buffer[strlen(buffer)]);
	}
	//�ر�������
	inFileStream.close();
}
void ReadSDCardFile::readByChar(std::string sdCardFilePath)
{
	//�ļ������
	fstream inFileStream;
	//�����ȡ���ַ�
	char c;
	//���ļ�
	inFileStream.open("/sdcard/" + sdCardFilePath,ios::in);
	while(!inFileStream.eof())
	{
		inFileStream>>c;							//���ַ���ȡ
	}
	inFileStream.close();
}
