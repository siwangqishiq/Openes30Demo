#include "TextureRect.h"
#include "util/ShaderUtil.h"
#include "util/MatrixState.h"

#include <stdio.h>
#include <stdlib.h>
#include "util/FileUtil.h"

#include <string>
using namespace std;

TextureRect::TextureRect(
		float halfWidth, //���
		float halfHeight, //���
		float offset, //λ����
		float UNIT_SIZE,//��λ����
		GLuint programIn,
		float* texCoord//��������
		)
{
    initVertexDataRectangle(halfWidth, halfHeight, offset, UNIT_SIZE,texCoord);
    initShader(programIn);
}

void TextureRect::initVertexDataRectangle(float halfWidth, float halfHeight, float offset, float UNIT_SIZE,float* texcoord)
{
	//  3----2
	//  |    |
	//	|	 |
	//  0----1    ˳��Ϊ  0,1,2  0,2,3
    vCount = 6;
    float *vertices=new float[vCount*3];
	int count=0;//���������

	vertices[count++] = -halfWidth*UNIT_SIZE;
	vertices[count++] = -halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	vertices[count++] = halfWidth*UNIT_SIZE;
	vertices[count++] = -halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	vertices[count++] = halfWidth*UNIT_SIZE;
	vertices[count++] = halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	vertices[count++] = -halfWidth*UNIT_SIZE;
	vertices[count++] = -halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	vertices[count++] = halfWidth*UNIT_SIZE;
	vertices[count++] = halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	vertices[count++] = -halfWidth*UNIT_SIZE;
	vertices[count++] = halfHeight*UNIT_SIZE;
	vertices[count++] = offset;

	mVertexBuffer = &vertices[0];

    float *normal=new float[vCount*3];
	count=0;//���������
	for(int i = 0; i<vCount; i++)
	{
		normal[count++] = 0;
		normal[count++] = 0;
		normal[count++] = 1;
	}
	mNormalBuffer = &normal[0];
	if(texcoord == NULL)
	{
	    float *textures=new float[vCount*2];
		count=0;//���������
		textures[count++]=0;
		textures[count++]=1;

		textures[count++]=1;
		textures[count++]=1;

		textures[count++]=1;
		textures[count++]=0;

		textures[count++]=0;
		textures[count++]=1;

		textures[count++]=1;
		textures[count++]=0;

		textures[count++]=0;
		textures[count++]=0;

		mTextureBuffer = &textures[0];
	}else
	{
		mTextureBuffer = &texcoord[0];
	}

}


void TextureRect::initShader(GLuint programIn)
{
    mProgram = programIn;
    //��ȡ�����ж���λ����������id
    maPositionHandle = glGetAttribLocation(mProgram, "aPosition");
    //��ȡ�����ж��㾭γ����������id
    maTexCoorHandle=glGetAttribLocation(mProgram, "aTexCoor");
    //��ȡ�������ܱ任��������id
    muMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");
    //��ȡλ�á���ת�任��������id
    muMMatrixHandle = glGetUniformLocation(mProgram, "uMMatrix");
    //��ȡ�����������λ������id
    maCameraHandle=glGetUniformLocation(mProgram, "uCamera");
    //��ȡ���������
    mTexHandle=glGetUniformLocation(mProgram, "sTexture");

    //��ȡ���㷨�������Ե�����
    maNormalHandle = glGetAttribLocation(mProgram, "aNormal");
    //��ȡ�����й�Դ��λ�õ�����
    maLightLocationHandle = glGetUniformLocation(mProgram, "uLightLocation");

    mFragK = glGetUniformLocation(mProgram, "k");
}

void TextureRect::drawSelf(const GLint texId)
{
	//�ƶ�ʹ��ĳ��shader����
	glUseProgram(mProgram);
	//�����ձ任������shader����
	glUniformMatrix4fv(muMVPMatrixHandle, 1, 0, MatrixState::getFinalMatrix());
	//��λ�á���ת�任������shader����
	glUniformMatrix4fv(muMMatrixHandle, 1, 0, MatrixState::getMMatrix());
	//�������λ�ô���shader����
	glUniform3fv(maCameraHandle, 1, MatrixState::cameraFB);
	//����Դλ�ô�����ɫ������
	glUniform3fv(maLightLocationHandle, 1, MatrixState::lightPositionFBSun);

	//Ϊ����ָ������λ������
	glVertexAttribPointer
	(
		maPositionHandle,
		3,
		GL_FLOAT,
		GL_FALSE,
		3*4,
		mVertexBuffer
	);
	//Ϊ����ָ������������������
	glVertexAttribPointer
	(
		maTexCoorHandle,
		2,
		GL_FLOAT,
		GL_FALSE,
		2*4,
		mTextureBuffer
	);
	//Ϊ����ָ�����㷨��������
	glVertexAttribPointer
	(
		maNormalHandle,
		3,
		GL_FLOAT,
		GL_FALSE,
		3*4,
		mNormalBuffer
	);

	//������λ����������
	glEnableVertexAttribArray(maPositionHandle);
	glEnableVertexAttribArray(maTexCoorHandle);
	glEnableVertexAttribArray(maNormalHandle);//handle ����

	//������
	glActiveTexture(GL_TEXTURE0);
	glBindTexture(GL_TEXTURE_2D, texId);
	glUniform1i(mTexHandle, 0);
	glUniform1f(mFragK, mFlagK);//mFlagK Ϊ0ʱ��ʾ��������   Ϊ1ʱ��ʾ�к�ɫֵӰ��

	//����������
	glDrawArrays(GL_TRIANGLES, 0, vCount);

    glDisableVertexAttribArray(maPositionHandle);
    glDisableVertexAttribArray(maTexCoorHandle);
    glDisableVertexAttribArray(maNormalHandle);
}
