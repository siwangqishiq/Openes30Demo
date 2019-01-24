package com.bn.Sample11_4;

import java.io.*;
import java.nio.*;
import android.content.res.Resources;
import android.opengl.GLES30;

//���ض���Shader��ƬԪShader�Ĺ�����
public class ShaderUtil 
{
   //����shader����ķ���
   public static int createProgram(String fname,Resources r) 
   { 
        //��������
        int program = GLES30.glCreateProgram();        
        //�����򴴽��ɹ���������м��붥����ɫ����ƬԪ��ɫ��
        if (program != 0) 
        {
        	ProgramObject po=loadProgramBinary("shader.bin",r);
        	ByteBuffer buf=ByteBuffer.allocate(po.binLength);
        	buf.put(po.data);
        	buf.position(0);
        	GLES30.glProgramBinary(program, po.binaryFormat, buf, po.binLength);
        }
        return program;
    }
   
   //��sh�ű��м���shader���ݵķ���
   public static ProgramObject loadProgramBinary(String fname,Resources r)
   {
	    ProgramObject po=null;
	   	try
	   	{
	   		InputStream in=r.getAssets().open(fname);
	   		ObjectInputStream oin=new ObjectInputStream(in);
			po=(ProgramObject)oin.readObject();
			oin.close();		   
	   	}
	   	catch(Exception e)
	   	{
	   		e.printStackTrace();
	   	}    
	   	return po;
   }
}
