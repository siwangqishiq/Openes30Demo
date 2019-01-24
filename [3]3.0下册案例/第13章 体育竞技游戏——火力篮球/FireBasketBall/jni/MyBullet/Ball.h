#ifndef _Ball_H_
#define _Ball_H_

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>

class Ball
{
    GLuint mProgram;				//�Զ�����Ⱦ���߳���id

	//��ȡ�����ж���λ����������id
    GLuint maPositionHandle;
	//��ȡ�����ж�������������������id
    GLuint maTexCoorHandle;
	//��ȡ�������ܱ任��������id
    GLuint muMVPMatrixHandle;
	//��ȡλ�á���ת�任��������id
    GLuint muMMatrixHandle;
	//��ȡ�����ж��㷨������������id
    GLuint maNormalHandle;
	//��ȡ�����й�Դλ������id
    GLuint maLightLocationHandle;
	//��ȡ�����������λ������id
    GLuint maCameraHandle;
	//��ȡ�������Ƿ������Ӱ��������id
    GLuint muIsShadow;
    GLuint muIsShadowFrag;
	//��ȡ�Ƿ������������Ӱ����Ӧ��ID
    GLuint muIsLanBanShdow;
	//��ȡ�������������������id
    GLuint muCameraMatrixHandle;
	//��ȡ������ͶӰ��������id
    GLuint muProjMatrixHandle;
	//��ȡ����������������id
    GLuint muBallTexHandle;
	//��ȡ������ƽ�淨��������id;
    GLuint muPlaneN;
	//��ȡ������ƽ���ϵĵ�����õ�Id
    GLuint muPlaneV;

    int isLanbanYy;
    int isShadow;

    const GLvoid* mVertexBuffer;	//�����������ݻ���
    const GLvoid* mTextureBuffer;	//���������������ݻ���
    const GLvoid* mNormalBuffer;	//���㷨�������ݻ���

    float r;
    int texId;						//����id
    int vCount;						//�������� ���ظ�
public:
	Ball(
			int texIdIn,
			float r,//�뾶
			GLuint programIn
			);
//	~Ball();

    void initShader(GLuint programIn);
    void drawSelf(int texIdIn,int isShadow,int planeId,int isLanbanYy);

    void initVertexData();
    float toRadians(float angle);
    void generateTexCoor(int bw,int bh,float* tex);
};

#endif
