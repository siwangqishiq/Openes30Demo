package com.bn.Sample11_3A;//声明包
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import android.opengl.GLES30;

//加载后的物体——仅携带顶点信息，颜色随机
public class GrassObject
{	
	int mProgram;//自定义渲染管线着色器程序id  
    int muMVPMatrixHandle;//总变换矩阵引用
    int maPositionHandle; //顶点位置属性引用  
    int maTexCoorHandle; //顶点纹理坐标属性引用  
    String mVertexShader;//顶点着色器代码脚本    	 
    String mFragmentShader;//片元着色器代码脚本
	
    int vCount=0;  
    
	FloatBuffer   mVertexBuffer;//顶点坐标数据缓冲  
	FloatBuffer   mTexCoorBuffer;//顶点纹理坐标数据缓冲
	
	 int uTexHandle;//小草纹理属性引用id
     int uJBTexHandle;//颜色渐变纹理引用id
     int uJBSHandle;//从颜色渐变纹理中采样的S值引用id
     int uJBTHandle;//从颜色渐变纹理中采样的T值引用id
    
    public GrassObject(MySurfaceView mv,float[] vertices,float texCoors[])
    {
    	//初始化顶点数据的方法
    	initVertexData(vertices,texCoors);
    	//初始化着色器      
    	initShader(mv);
    }
    
	//初始化顶点坐标、法向量、纹理坐标数据的方法
    public void initVertexData(float[] vertices,float texCoors[])
    {
    	//顶点坐标数据的初始化================begin============================
    	vCount=vertices.length/3;   
		
        //创建顶点坐标数据缓冲
        //vertices.length*4是因为一个整数四个字节
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length*4);
        vbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mVertexBuffer = vbb.asFloatBuffer();//转换为Float型缓冲
        mVertexBuffer.put(vertices);//向缓冲区中放入顶点坐标数据
        mVertexBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点坐标数据的初始化================end============================
        
        //顶点纹理坐标数据的初始化================begin============================  
        ByteBuffer tbb = ByteBuffer.allocateDirect(texCoors.length*4);
        tbb.order(ByteOrder.nativeOrder());//设置字节顺序
        mTexCoorBuffer = tbb.asFloatBuffer();//转换为Float型缓冲
        mTexCoorBuffer.put(texCoors);//向缓冲区中放入顶点纹理坐标数据
        mTexCoorBuffer.position(0);//设置缓冲区起始位置
        //特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
        //顶点纹理坐标数据的初始化================end============================
    }

    //初始化shader
    public void initShader(MySurfaceView mv)
    {
    	//加载顶点着色器的脚本内容
        mVertexShader=ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        //加载片元着色器的脚本内容
        mFragmentShader=ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());  
        //基于顶点着色器与片元着色器创建程序
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        //获取程序中顶点位置属性引用  
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        //对应从颜色渐变纹理中采样的S值引用id 
        uJBSHandle= GLES30.glGetUniformLocation(mProgram, "jbS");
        //对应从颜色渐变纹理中采样的T值引用id 
        uJBTHandle= GLES30.glGetUniformLocation(mProgram, "jbT");
        //获取程序中总变换矩阵引用
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");  
        //获取小草纹理引用id
        uTexHandle=GLES30.glGetUniformLocation(mProgram, "ssTexture");  
        //颜色渐变纹理引用id
        uJBTexHandle=GLES30.glGetUniformLocation(mProgram, "jbTexture");
        //获取程序中顶点纹理坐标属性引用  
        maTexCoorHandle= GLES30.glGetAttribLocation(mProgram, "aTexCoor"); 
    }
    //绘制加载物体的方法
    public void drawSelf(int texId,int jbTexId,float[] jb)
    {        
    	 //指定使用某套着色器程序
    	 GLES30.glUseProgram(mProgram);
         //将最终变换矩阵传入渲染管线
         GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(), 0); 
         // 将顶点位置数据传入渲染管线
         GLES30.glVertexAttribPointer  
         (
         		maPositionHandle,   
         		3, 
         		GLES30.GL_FLOAT, 
         		false,
                3*4,   
                mVertexBuffer
         );       
         //将顶点纹理坐标数据传入渲染管线
         GLES30.glVertexAttribPointer  
         (
        		maTexCoorHandle, 
         		2, 
         		GLES30.GL_FLOAT, 
         		false,
                2*4,   
                mTexCoorBuffer
         );
         //启用顶点位置、纹理坐标数据数组
         GLES30.glEnableVertexAttribArray(maPositionHandle);   
         GLES30.glEnableVertexAttribArray(maTexCoorHandle); //启用纹理坐标数据数组
         //绑定小草纹理
         GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, texId);
         //绑定颜色渐变纹理
         GLES30.glActiveTexture(GLES30.GL_TEXTURE1);
         GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, jbTexId);
         
         GLES30.glUniform1i(uTexHandle, 0);
         GLES30.glUniform1i(uJBTexHandle, 1);   
         GLES30.glUniform1f(uJBSHandle, jb[0]);//对应从颜色渐变纹理中采样的S值
         GLES30.glUniform1f(uJBTHandle, jb[1]);//对应从颜色渐变纹理中采样的S值
         //绘制加载的物体
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount); 
    }
}
