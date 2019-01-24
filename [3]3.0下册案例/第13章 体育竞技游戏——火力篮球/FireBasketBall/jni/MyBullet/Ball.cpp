#include "Ball.h"
#include "util/ShaderUtil.h"
#include "util/MatrixState.h"

#include <math.h>


#include <stdio.h>
#include <stdlib.h>
#include "util/FileUtil.h"
#include "util/Constant.h"

#include <string>
using namespace std;


Ball::Ball(
			int texIdIn,
			float r,//�뾶
			GLuint programIn
			)
{
	this->r = r;
	initVertexData();
	initShader(programIn);
}

float Ball::toRadians(float angle)
{
    return angle*3.1415926f/180;//��һ��
}
//�Զ��з����������������ķ���
void Ball::generateTexCoor(int bw,int bh,float* tex){
    //float result[bw*bh*6*2];
    float sizew=1.0f/bw;//����
    float sizeh=1.0f/bh;//����
    int c=0;
    for(int i=0;i<bh;i++){
        for(int j=0;j<bw;j++){
            //ÿ����һ�����Σ������������ι��ɣ��������㣬12����������
            float s=j*sizew;
            float t=i*sizeh;
            tex[c++]=s;
            tex[c++]=t;
            tex[c++]=s;
            tex[c++]=t+sizeh;
            tex[c++]=s+sizew;
            tex[c++]=t;
            tex[c++]=s+sizew;
            tex[c++]=t;
            tex[c++]=s;
            tex[c++]=t+sizeh;
            tex[c++]=s+sizew;
            tex[c++]=t+sizeh;
    }}
}
void Ball::initVertexData()
{
	vCount=11664/3;//���������Ϊ����ֵ������1/3����Ϊһ��������3������
    //�����������ݵĳ�ʼ��================begin============================
    const float angleSpan=10;//������е�λ�зֵĽǶ�
    float *vertices=new float[11664];//36*18*6*3
	int count=0;//���������

    for(float vAngle=90;vAngle>-90;vAngle=vAngle-angleSpan){//��ֱ����angleSpan��һ��
        for(float hAngle=360;hAngle>0;hAngle=hAngle-angleSpan){//ˮƽ����angleSpan��һ��
            //����������һ���ǶȺ�����Ӧ�Ĵ˵��������ϵ�����
            double xozLength=r*cos(toRadians(vAngle));
            float x1=(float)(xozLength*cos(toRadians(hAngle)));
            float z1=(float)(xozLength*sin(toRadians(hAngle)));
            float y1=(float)(r*sin(toRadians(vAngle)));

            xozLength=r*cos(toRadians(vAngle-angleSpan));
            float x2=(float)(xozLength*cos(toRadians(hAngle)));
            float z2=(float)(xozLength*sin(toRadians(hAngle)));
            float y2=(float)(r*sin(toRadians(vAngle-angleSpan)));

            xozLength=r*cos(toRadians(vAngle-angleSpan));
            float x3=(float)(xozLength*cos(toRadians(hAngle-angleSpan)));
            float z3=(float)(xozLength*sin(toRadians(hAngle-angleSpan)));
            float y3=(float)(r*sin(toRadians(vAngle-angleSpan)));

            xozLength=r*cos(toRadians(vAngle));
            float x4=(float)(xozLength*cos(toRadians(hAngle-angleSpan)));
            float z4=(float)(xozLength*sin(toRadians(hAngle-angleSpan)));
            float y4=(float)(r*sin(toRadians(vAngle)));

        	vertices[count++]=x1;
        	vertices[count++]=y1;
        	vertices[count++]=z1;

        	vertices[count++]=x2;
        	vertices[count++]=y2;
        	vertices[count++]=z2;

        	vertices[count++]=x4;
        	vertices[count++]=y4;
        	vertices[count++]=z4;

        	vertices[count++]=x4;
        	vertices[count++]=y4;
        	vertices[count++]=z4;

        	vertices[count++]=x2;
        	vertices[count++]=y2;
        	vertices[count++]=z2;

        	vertices[count++]=x3;
        	vertices[count++]=y3;
        	vertices[count++]=z3;

    }}
	mVertexBuffer = &vertices[0];
	mNormalBuffer = &vertices[0];//�����㷨�������ݻ��帳ֵ

    float *textures=new float[vCount*2];
    generateTexCoor(36, 18, textures);
    mTextureBuffer = &textures[0];
}

//��ʼ����ɫ����intShader����
void Ball::initShader(GLuint programIn)
{
	this->mProgram=programIn;
	//��ȡ�����ж���λ����������id
	maPositionHandle = glGetAttribLocation(mProgram, "aPosition");
	//��ȡ�����ж�������������������id
	maTexCoorHandle= glGetAttribLocation(mProgram, "aTexCoor");
	//��ȡ�������ܱ任��������id
	muMVPMatrixHandle = glGetUniformLocation(mProgram, "uMVPMatrix");
	//��ȡλ�á���ת�任��������id
	muMMatrixHandle = glGetUniformLocation(mProgram, "uMMatrix");
	//��ȡ�����ж��㷨������������id
	maNormalHandle= glGetAttribLocation(mProgram, "aNormal");
	//��ȡ�����й�Դλ������id
	maLightLocationHandle=glGetUniformLocation(mProgram, "uLightLocation");
	//��ȡ�����������λ������id
	maCameraHandle=glGetUniformLocation(mProgram, "uCamera");
	//��ȡ�������Ƿ������Ӱ��������id
	muIsShadow=glGetUniformLocation(mProgram, "uisShadow");
	muIsShadowFrag=glGetUniformLocation(mProgram, "uisShadowFrag");
	//��ȡ�Ƿ������������Ӱ����Ӧ��ID
	muIsLanBanShdow=glGetUniformLocation(mProgram, "uisLanbanFrag");
	//��ȡ�������������������id
	muCameraMatrixHandle=glGetUniformLocation(mProgram, "uMCameraMatrix");
	//��ȡ������ͶӰ��������id
	muProjMatrixHandle=glGetUniformLocation(mProgram, "uMProjMatrix");
	//��ȡ����������������id
	muBallTexHandle=glGetUniformLocation(mProgram, "usTextureBall");
	//��ȡ������ƽ�淨��������id;
	muPlaneN=glGetUniformLocation(mProgram, "uplaneN");
	//��ȡ������ƽ���ϵĵ�����õ�Id
	muPlaneV=glGetUniformLocation(mProgram, "uplaneA");
}
void Ball::drawSelf(int texIdIn,int isShadow,int planeId,int isLanbanYy)
{
	//�ƶ�ʹ��ĳ��shader����
	glUseProgram(mProgram);
    //�����ձ任������shader����
    glUniformMatrix4fv(muMVPMatrixHandle, 1, 0, MatrixState::getFinalMatrix());
    //��λ�á���ת�任������shader����
    glUniformMatrix4fv(muMMatrixHandle, 1, 0, MatrixState::getMMatrix());
    //����Դλ�ô���shader����
    glUniform3fv(maLightLocationHandle, 1,MatrixState::lightPositionFBSun);
    //�������λ�ô���shader����
    glUniform3fv(maCameraHandle, 1, MatrixState::cameraFB);
    //���Ƿ������Ӱ���Դ���shader����
    glUniform1i(muIsShadow, isShadow);
    glUniform1i(muIsShadowFrag, isShadow);
    glUniform1i(muIsLanBanShdow, isLanbanYy);
    //�������������shader����
    glUniformMatrix4fv(muCameraMatrixHandle, 1, 0, MatrixState::getCaMatrix());
    //��ͶӰ������shader����
    glUniformMatrix4fv(muProjMatrixHandle, 1, 0, MatrixState::getProjMatrix());
    //��ƽ��λ�ô������
    glUniform3fv(muPlaneV, 1, Constant::mianFXL[planeId][0]);
    //��ƽ�淨�����������
    glUniform3fv(muPlaneN, 1, Constant::mianFXL[planeId][1]);

    //�����������ж���λ����������id,һ����3�������������ͣ�GL_FALSE��һ����ռ�ֽ������������ݵ��׵�ַ
    glVertexAttribPointer(maPositionHandle,3, GL_FLOAT, GL_FALSE,3*4, mVertexBuffer);
    //�����������ж���������������id,һ����2�������������ͣ�GL_FALSE��һ����ռ�ֽ���������������׵�ַ
    glVertexAttribPointer(maTexCoorHandle,2,GL_FLOAT,GL_FALSE,2*4,mTextureBuffer);
	//Ϊ����ָ�����㷨��������
	glVertexAttribPointer(maNormalHandle,3,GL_FLOAT,GL_FALSE,3*4,mNormalBuffer);

    glEnableVertexAttribArray(maPositionHandle);//�����������ж���λ����������id
    glEnableVertexAttribArray(maTexCoorHandle); //�����������ж���������������id		��������
    glEnableVertexAttribArray(maNormalHandle);//handle ����

    //������
    glActiveTexture(GL_TEXTURE0);
    glBindTexture(GL_TEXTURE_2D, texIdIn);
    glUniform1i(muBallTexHandle, 0);

    glDrawArrays(GL_TRIANGLES, 0, vCount);//���Ʋ���GL_TRIANGLES��ʽ��0��vCount������		����

    glDisableVertexAttribArray(maPositionHandle);//�����������ж���λ����������id
    glDisableVertexAttribArray(maTexCoorHandle); //�����������ж���������������id		�رգ���
    glDisableVertexAttribArray(maNormalHandle);
}
