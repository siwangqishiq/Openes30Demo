package com.bn.Sample2_6;
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
	//��obj�ļ��м���Я��������Ϣ������
    public static LoadedObjectVertexNormalTexture loadFromFile
    (String fname, Resources r,MySurfaceView mv)
    {
    	//���غ����������
    	LoadedObjectVertexNormalTexture lo=null;
    	//ԭʼ���������б�--ֱ�Ӵ�obj�ļ��м���
    	ArrayList<Float> alv=new ArrayList<Float>();
    	//������������б�--������֯��
    	ArrayList<Float> alvResult=new ArrayList<Float>(); 
    	//ԭʼ���������б�
    	ArrayList<Float> alt=new ArrayList<Float>();  
    	//�����������б�
    	ArrayList<Float> altResult=new ArrayList<Float>();  
    	//ԭʼ�淨�����б�
    	ArrayList<Float> aln=new ArrayList<Float>();    
    	//����淨�����б�
    	ArrayList<Float> alnResult=new ArrayList<Float>();    
    	
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
		      	else if(tempsa[0].trim().equals("vt"))
		      	{//����Ϊ����������
		      		//��Ϊ��������������ȡST���겢��ӽ�ԭʼ���������б���
		      		alt.add(Float.parseFloat(tempsa[1]));
		      		alt.add(1-Float.parseFloat(tempsa[2])); 
		      	}
		      	else if(tempsa[0].trim().equals("vn"))
		      	{//����Ϊ��������
		      		//��Ϊ��������������ȡST���겢��ӽ�ԭʼ���������б���
		      		aln.add(Float.parseFloat(tempsa[1]));
		      		aln.add(Float.parseFloat(tempsa[2])); 
		      		aln.add(Float.parseFloat(tempsa[3])); 
		      	}
		      	else if(tempsa[0].trim().equals("f")) 
		      	{//����Ϊ��������	      		
		      		//�����0�����������������ȡ�˶����XYZ��������	      		
		      		int index=Integer.parseInt(tempsa[1].split("/")[0])-1;
		      		float x0=alv.get(3*index);
		      		float y0=alv.get(3*index+1);
		      		float z0=alv.get(3*index+2);
		      		alvResult.add(x0);
		      		alvResult.add(y0);
		      		alvResult.add(z0);		
		      		
		      	    //�����1�����������������ȡ�˶����XYZ��������	  
		      		index=Integer.parseInt(tempsa[2].split("/")[0])-1;
		      		float x1=alv.get(3*index);
		      		float y1=alv.get(3*index+1);
		      		float z1=alv.get(3*index+2);
		      		alvResult.add(x1);
		      		alvResult.add(y1);
		      		alvResult.add(z1);
		      		
		      	    //�����2�����������������ȡ�˶����XYZ��������	
		      		index=Integer.parseInt(tempsa[3].split("/")[0])-1;
		      		float x2=alv.get(3*index);
		      		float y2=alv.get(3*index+1);
		      		float z2=alv.get(3*index+2);
		      		alvResult.add(x2);
		      		alvResult.add(y2); 
		      		alvResult.add(z2);			      		
		      		
		      		//������������֯��������������б���
		      		//��0���������������
		      		int indexTex=Integer.parseInt(tempsa[1].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    //��1���������������
		      		indexTex=Integer.parseInt(tempsa[2].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));
		      	    //��2���������������
		      		indexTex=Integer.parseInt(tempsa[3].split("/")[1])-1;
		      		altResult.add(alt.get(indexTex*2));
		      		altResult.add(alt.get(indexTex*2+1));		      		
		      		
		      	    //=================================================
		      		//�����0�����������������ȡ�˶����XYZ��������	
		      		int indexN=Integer.parseInt(tempsa[1].split("/")[2])-1;
		      		float nx0=aln.get(3*indexN);
		      		float ny0=aln.get(3*indexN+1);
		      		float nz0=aln.get(3*indexN+2);
		      		alnResult.add(nx0);
		      		alnResult.add(ny0);
		      		alnResult.add(nz0);		
		      		
		      	    //�����1�����������������ȡ�˶����XYZ��������	  
		      		indexN=Integer.parseInt(tempsa[2].split("/")[2])-1;
		      		float nx1=aln.get(3*indexN);
		      		float ny1=aln.get(3*indexN+1);
		      		float nz1=aln.get(3*indexN+2);
		      		alnResult.add(nx1);
		      		alnResult.add(ny1);
		      		alnResult.add(nz1);
		      		
		      	    //�����2�����������������ȡ�˶����XYZ��������	
		      		indexN=Integer.parseInt(tempsa[3].split("/")[2])-1;
		      		float nx2=aln.get(3*indexN);
		      		float ny2=aln.get(3*indexN+1);
		      		float nz2=aln.get(3*indexN+2);
		      		alnResult.add(nx2);
		      		alnResult.add(ny2); 
		      		alnResult.add(nz2);	
		      	}		      		
		    } 
		    
		    //���ɶ�������
		    int size=alvResult.size();
		    float[] vXYZ=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	vXYZ[i]=alvResult.get(i);
		    } 
		    
		    //������������
		    size=altResult.size();
		    float[] tST=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	tST[i]=altResult.get(i);
		    }
		    
		    //���ɷ���������
		    size=alnResult.size();
		    float[] nXYZ=new float[size];
		    for(int i=0;i<size;i++)
		    {
		    	nXYZ[i]=alnResult.get(i);
		    }
		    
		    //����3D�������
		    lo=new LoadedObjectVertexNormalTexture(mv,vXYZ,nXYZ,tST);
    	}
    	catch(Exception e)
    	{
    		Log.d("load error", "load error");
    		e.printStackTrace();
    	}    	
    	return lo;
    }
}
