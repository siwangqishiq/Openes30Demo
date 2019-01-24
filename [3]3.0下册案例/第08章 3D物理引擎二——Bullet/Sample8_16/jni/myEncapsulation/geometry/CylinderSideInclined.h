#ifndef _CylinderSideInclined_H_
#define _CylinderSideInclined_H_

#include <GLES3/gl3.h>
#include <GLES3/gl3ext.h>

#include "../../Bullet/LinearMath/btVector3.h"

//圆柱的侧面
class CylinderSideInclined
{
    GLuint mProgram;//自定义渲染管线程序id
    GLuint muMVPMatrixHandle;//总变换矩阵引用id
    GLuint muMMatrixHandle;//位置、旋转变换矩阵
    GLuint mTexHandle;//外观纹理属性引用id

    GLuint mFragK;//外观纹理属性引用id

    GLuint maCameraHandle; //摄像机位置属性引用id
    GLuint maLightLocationHandle;//光源位置属性引用
    GLuint maPositionHandle; //顶点位置属性引用id
    GLuint maTexCoorHandle; //顶点纹理坐标属性引用id
    GLuint maNormalHandle;//顶点法向量属性引用id

    const GLvoid* mVertexBuffer;	//顶点坐标数据缓冲
    const GLvoid* mTextureBuffer;	//顶点纹理坐标数据缓冲
    const GLvoid* mNormalBuffer;	//顶点法向量数据缓冲

    int 		texId;			//纹理id
    float 		TRIANGLE_SIZE;	//三角形的单位大小
    int vCount;//顶点总数 有重复

    btVector3* pos;
    int index;
    float r;

	float* vertices;//坐标数据
	float* normals;//法向量数据
	float* textures;//顶点纹理S、T坐标值数组
	int n;

public:
    float mFlagK;//纹理属性
	CylinderSideInclined(
			int texIdIn,
			float r,//半径
			int n,//三角形总数
			btVector3* pos,
			int index
			);//轴为y轴的圆柱侧边
	CylinderSideInclined();
//	~CylinderSideInclined();

	void refresh();
    void initShader();
    void drawSelf(int texIdIn);
};

#endif
