package com.bn.Sample6_7;

import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;

import com.bn.util.Vector3f;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

public class Constant {
	
	static FloatBuffer mVertexBufferForFlag;//���㻺������
	static Vector3f ballP = new Vector3f(0, 0, 0);
	static Object lockA=new Object();
	static Object lockB=new Object();
	final static int NUMROWS = 11;//����������
	final static int NUMCOLS = 30;
	final static int NUMVERTICES = (NUMROWS+1)*(NUMCOLS+1);//������Ŀ
	final static int NUMSPTINGS = (NUMROWS*(NUMCOLS+1)+(NUMROWS+1)*NUMCOLS+2*NUMROWS*NUMCOLS);//������Ŀ
	final static float RSTER = 1.5f/NUMROWS;//���������м��
	final static float CSTER = 4.6f/NUMCOLS;//���������м��
	final static float 	KRESTITUTION=0.8f; //����ϵ��
	final static float COLLISIONTOLERANCE = 0f;//����λ��
	final static float 	FRICTIONFACTOR=0.5f; //Ħ��ϵ��
	
	final static float GRAVITY = -0.7f;//����
	final static float SPRING_TENSION_CONSTANT = 100.f;//���쵯�ɲ���
	final static float SPRING_SHEAR_CONSTANT = 100.f;//�������ɲ���
	final static float SPRING_DAMPING_CONSTANT = 2.f;//��������
	
	static boolean isC = true;//�Ƿ�����ײ���
	static float WindForce = 0.0f;//����
	final static float DRAGCOEFFICIENT = 0.01f;//��������

  	public static int initTexture(int drawableId,GLSurfaceView gsv)//textureId
	{
		//��������ID
		int[] textures = new int[1];
		GLES30.glGenTextures
		(
				1,          //����������id������
				textures,   //����id������
				0           //ƫ����
		);    
		int textureId=textures[0];    
		GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureId);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER,GLES30.GL_NEAREST);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D,GLES30.GL_TEXTURE_MAG_FILTER,GLES30.GL_LINEAR);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S,GLES30.GL_REPEAT);
		GLES30.glTexParameterf(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T,GLES30.GL_REPEAT);
   
        //ͨ������������ͼƬ===============begin===================
        InputStream is = gsv.getResources().openRawResource(drawableId);
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }
        //ͨ������������ͼƬ===============end===================== 
	   	GLUtils.texImage2D
	    (
	    		GLES30.GL_TEXTURE_2D, //��������
	     		0, 
	     		GLUtils.getInternalFormat(bitmapTmp), 
	     		bitmapTmp, //����ͼ��
	     		//GLUtils.getType(bitmapTmp), 
	     		0 //����߿�ߴ�
	     );

	    bitmapTmp.recycle(); 		  //������سɹ����ͷ�ͼƬ
        return textureId; 
	}
 
  	

}
