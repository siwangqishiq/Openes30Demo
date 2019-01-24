#include "MyBullet/Digital.h"
#include "util/Constant.h"

#include "stdlib.h"
#include <string>

#include "util/MatrixState.h"

using namespace std;

#define INTTOSTR(I) #I   //����ʹ�ú궨��

Digital::Digital(GLuint mprogram)
{
	//�����������--��ָ����ɫ��
	for(int i=0;i<10;i++)
	{
		int count = 0;
		float* texCoord = new float[12];

		texCoord[count++] = 0.1f*i;
		texCoord[count++] = 0;

		texCoord[count++] = 0.1f*i;
		texCoord[count++] = 1;

		texCoord[count++] = 0.1f*(i+1);
		texCoord[count++] = 1;

		texCoord[count++] = 0.1f*i;
		texCoord[count++] = 0;

		texCoord[count++] = 0.1f*(i+1);
		texCoord[count++] = 1;

		texCoord[count++] = 0.1f*(i+1);
		texCoord[count++] = 0;

		textureRectArray[i] = new TextureRect(
				Constant::SHUZI_KUANDU/2,
				Constant::SHUZI_GAODU/2,
//				0.3,0.3,
				0,1,mprogram,texCoord
		);
	}
}
void Digital::drawSelf(int score,GLuint texId)//�������ֺ���������
{
    //�������
    glEnable(GL_BLEND);
    //���û������
    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

	//����ת�����ַ���
	std::string scoreStr ;
	//����ת������ַ�
	char index[10];
	 //����ת��Ϊ�ַ���
	sprintf(index,"%d",score);
	scoreStr = index;
	const char* charArray = scoreStr.c_str();

	for(int i=0;i<scoreStr.length();i++)
	{//���÷��е�ÿ�������ַ�����

		char c=charArray[i];

		 MatrixState::pushMatrix();
		 MatrixState::translate(i*Constant::SHUZI_KUANDU, 0, 0);
		 MatrixState::rotate(90,0,0,1);
		 textureRectArray[c-'0']->drawSelf(texId);
		 MatrixState::popMatrix();
	}
	glDisable(GL_BLEND);
}
