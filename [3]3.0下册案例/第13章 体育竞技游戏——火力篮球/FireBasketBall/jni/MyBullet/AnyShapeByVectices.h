#ifndef _AnyShapeByVectices_H_
#define _AnyShapeByVectices_H_

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>
#include "../Bullet/LinearMath/btVector3.h"

class AnyShapeByVectices
{

	GLuint mProgram;//�Զ�����Ⱦ���߳���id
	GLuint muMVPMatrixHandle;//�ܱ任����id
	GLuint muMMatrixHandle;//λ�á���ת�任����
	GLuint muTexHandle;//�����������id
	GLuint muLightLocationHandle;//��Դλ������id
	GLuint muCameraHandle;//�����λ������id

	GLuint maPositionHandle;//����λ������id
	GLuint maTexCoorHandle;//����������������id
	GLuint maNormalHandle;//���㷨��������id

	const GLvoid* mVertexBuffer;//�����������ݻ���
	const GLvoid* mNormalBuffer;//���㷨�������ݻ���
	const GLvoid* mTextureBuffer;//�����������ݻ���


	int m_vCount;//�������� ���ظ�
	int m_vCount_noRepeat;//�������� ���ظ�
	float* m_normals;
	int m_num_normals;
	float* m_vertices;
	float* m_vertices_noRepeat;
	btVector3* m_vertices_bt;
	int* m_indices;//��������
	float m_lightFlag;// Ϊ1.0f��ʾ˳ʱ��     Ϊ-1.0f��ʾ��ʱ��  	 һ��Ϊ-1.0f

	float* m_texs ;

	bool update;//������Ķ��㲻�Ϸ����任ʱ���Ƿ���и���

public:

	AnyShapeByVectices(
			float* vertices_fl,//����*3����	���ظ�ֵ
			int numsVerticesBt,//����*3���������ظ�ֵ��
			int* indices,//��������
			int numsIndices,//��������
			float* m_texs,
			int m_nums_tex,
			float* m_normals,
			int m_num_normals,
			bool update,
			float lightFlag,// Ϊ1��ʾ˳ʱ��     Ϊ-1��ʾ��ʱ��
			GLuint mprogram
			);//������

	AnyShapeByVectices(
			float* vertices_fl,//��������	���ظ�ֵ
			int numsVerticesBt,//�������������ظ�ֵ��
			int* indices,//��������
			int numsIndices,//��������
			float* m_texs,
			int m_nums_tex,
			bool update,//��״��Ҫ�����任ʱ��Ϊtrue����״����Ҫ�����任ʱ��Ϊfalse
			float lightFlag,// Ϊ1��ʾ˳ʱ��     Ϊ-1��ʾ��ʱ��
			GLuint mprogram
			);//������

    void initShader(GLuint mprogram);
    void drawSelf(int texId);
    void initIndices();

    float* method(
    		btVector3* vertices,//��������(���ظ�ֵ��)
    		int nums_vertices,//��������ĳ���
    		int* index_vertices,//�����������������
    		int nums_indexVertices//�����������������ĳ���	ע�⣺Ӧ����nums_vertices��ͬ
    		);

	void initVNData();

	float* btVector3ToFloat(btVector3* vertex, int nums);//�������飬�������� �������鳤�� nums*3

	btVector3* floatToBtVector3(float* vertex, int nums);//�������飬��������  �������鳤�� nums/3
};

#endif
