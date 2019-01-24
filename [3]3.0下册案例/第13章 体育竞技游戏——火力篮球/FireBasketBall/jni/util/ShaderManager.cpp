#include "ShaderManager.h"
#include "util/ShaderUtil.h"
#include "util/FileUtil.h"
#include "util/AppMacros.h"

const int ShaderManager::shaderCount = 3;

const string ShaderManager::shaderName[3][2] = {
	{SHADER_PATH+"CommonTex.vert",SHADER_PATH+"CommonTex.frag"},
	{SHADER_PATH+"vertex_shadow.vert",SHADER_PATH+"frag_shadow.frag"},
	{SHADER_PATH+"CommonTexWithLight.vert",SHADER_PATH+"CommonTexWithLight.frag"}
};
GLuint* ShaderManager::program = new GLuint[shaderCount];
ShaderManager::ShaderManager(){

}
//������Ҫ�Ǳ���3D�л�ӭ�����е�shader
void ShaderManager::compileShader()
{
	for(int i=0;i<shaderCount;i++)
	{
		std::string vertFile = FileUtil::loadShaderStr(shaderName[i][0]);
		std::string fragFile = FileUtil::loadShaderStr(shaderName[i][1]);
		program[i]=ShaderUtil::createProgram(vertFile.c_str(), fragFile.c_str());
	}
}

//���ﷵ�ص�����ͨ��shader����
GLuint ShaderManager::getCommTextureShaderProgram()
{
	return program[0];
}
//���ﷵ�ص���Ӱ�ӵ�shader����
GLuint ShaderManager::getShadowshaderProgram()
{
	return program[1];
}
//���ﷵ�ص��ǹ��������shader����
GLuint ShaderManager::getLigntAndTexturehaderProgram()
{
	return program[2];
}
