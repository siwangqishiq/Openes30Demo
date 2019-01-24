package com.bn.bullet;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.res.Resources;
import android.util.Log;

public class LoadUtil 
{
	//�����������Ĳ��
	public static float[] getCrossProduct(float x1,float y1,float z1,float x2,float y2,float z2)
	{		
		//�������ʸ�����ʸ����XYZ��ķ���ABC
        float A=y1*z2-y2*z1;
        float B=z1*x2-z2*x1;
        float C=x1*y2-x2*y1;
		
		return new float[]{A,B,C};
	}
	
	//�������
	public static float[] vectorNormal(float[] vector)
	{
		//��������ģ
		float module=(float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
		return new float[]{vector[0]/module,vector[1]/module,vector[2]/module};
	}
	
	public static float[] m_vertices;//��������
	public static int m_numsVer;//�������鳤��
	public static int[] m_indices;//������������
	public static int m_numsInd;//�����������鳤��
	
	public static float[] m_normals;//����������
	public static int m_numsNor;//���������鳤��

	public static float[] m_tex;//��������
	public static int m_numsTex;//�������鳤��
	
	//��obj�ļ��м���Я��������Ϣ�����壬���Զ�����ÿ�������ƽ��������
	//��ȡ�������ݣ�������������  ��������  ������������   ��������ƽ��������
    public static void loadFromFileWd(String fname, Resources r)
    {
    	//ԭʼ���������б�--ֱ�Ӵ�obj�ļ��м���
    	ArrayList<Float> alv=new ArrayList<Float>();
    	//������װ�������б�--���������Ϣ���ļ��м���
    	ArrayList<Integer> alFaceIndex=new ArrayList<Integer>();
    	//ԭʼ���������б�
    	ArrayList<Float> alt=new ArrayList<Float>(); 
    	//�������� �б�
    	ArrayList<Integer> alFaceIndex_tex=new ArrayList<Integer>();
    	
    	try
    	{
    		InputStream in=r.getAssets().open(fname);
    		InputStreamReader isr=new InputStreamReader(in);
    		BufferedReader br=new BufferedReader(isr);
    		String temps=null;
    		
    		//ɨ���ļ������������͵Ĳ�ִͬ�в�ͬ�Ĵ����߼�
		    while((temps=br.readLine())!=null) 
		    {
		    	//�ÿո�ָ����еĸ�����ɲ���
		    	String[] tempsa=temps.split("[ ]+");
		      	if(tempsa[0].trim().equals("v"))
		      	{//����Ϊ��������
		      	    //��Ϊ��������������ȡ���˶����XYZ������ӵ�ԭʼ���������б���
		      		alv.add(Float.parseFloat(tempsa[1]));
		      		alv.add(Float.parseFloat(tempsa[2]));
		      		alv.add(Float.parseFloat(tempsa[3]));
		      	}
		      	else if(tempsa[0].trim().equals("f"))
		      	{//����Ϊ��������
		      		int[] index=new int[3];//������������ֵ������
		      		//�����0�����������������ȡ�˶����XYZ��������	      		
		      		index[0]=Integer.parseInt(tempsa[1].split("/")[0])-1;
		      	    //�����1�����������������ȡ�˶����XYZ��������	  
		      		index[1]=Integer.parseInt(tempsa[2].split("/")[0])-1;
		      	    //�����2�����������������ȡ�˶����XYZ��������	
		      		index[2]=Integer.parseInt(tempsa[3].split("/")[0])-1;
		      		//��¼����Ķ�������
		      		alFaceIndex.add(index[0]);
		      		alFaceIndex.add(index[1]);
		      		alFaceIndex.add(index[2]);
		      		
		      		index[0] = Integer.parseInt(tempsa[1].split("/")[1])-1;
		      		index[1] = Integer.parseInt(tempsa[2].split("/")[1])-1;
		      		index[2] = Integer.parseInt(tempsa[3].split("/")[1])-1;
		      		alFaceIndex_tex.add(index[0]);
		      		alFaceIndex_tex.add(index[1]);
		      		alFaceIndex_tex.add(index[2]);
		      	}
		      	else if(tempsa[0].trim().equals("vt"))
		      	{
		      		//��Ϊ��������������ȡST���겢��ӽ�ԭʼ���������б���
		      		float sl=Float.parseFloat(tempsa[1]);
		      		float tl=Float.parseFloat(tempsa[2]);
		      		float t=1.0f-tl;
		      		float s=sl;
		      		alt.add(s);
		      		alt.add(t);
		      	}
		    } 
		    
		    //���ɶ�������
		    m_numsVer=alv.size();
		    m_vertices=new float[m_numsVer];
		    for(int i=0;i<m_numsVer;i++)
		    {
		    	m_vertices[i]=alv.get(i);
		    }
		    
		    //���ɶ�����������
		    m_numsInd=alFaceIndex.size();
		    m_indices=new int[m_numsInd];
		    for(int i=0;i<m_numsInd;i++)
		    {
		    	m_indices[i]=alFaceIndex.get(i);
		    }
		    
		    //��������
		    int m_numsTex_temp = alt.size();
		    float[] m_tex_temp = new float[m_numsTex_temp];
		    for(int i = 0; i < m_numsTex_temp; i++)
		    {
		    	m_tex_temp[i] = alt.get(i);
		    }
		    
		    //������������
		    int m_numsInd_tex = alFaceIndex_tex.size();
		    int[] m_indices_tex = new int[m_numsInd_tex];
		    for(int i = 0; i < m_numsInd_tex; i++)
		    {
		    	m_indices_tex[i] = alFaceIndex_tex.get(i);
		    }
		    
		    m_numsTex = m_numsInd_tex*2;
		    m_tex = new float[m_numsTex];
		    for(int i = 0; i < m_numsInd_tex; i++)
		    {
		    	int index = m_indices_tex[i];
		    	m_tex[i*2+0] = m_tex_temp[index*2];
		    	m_tex[i*2+1] = m_tex_temp[index*2+1];
		    }
    	}
    	catch(Exception e)
    	{
    		Log.d("load error", "load error");
    		e.printStackTrace();
    	}    	
    	return;
    }
	



}