package com.bn.Sample3_12;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.content.res.Resources;
import android.util.Log;

public class LoadUtil 
{		
	//��obj�ļ��м���Я��������Ϣ�����壬���Զ�����ÿ�������ƽ��������
    public static LoadedObjectVertexNormal loadFromFile
    (String fname, Resources r,MySurfaceView mv)
    {
    	//���غ����������
    	LoadedObjectVertexNormal lo=null;
    	//ԭʼ���������б�--ֱ�Ӵ�obj�ļ��м���
    	ArrayList<Float> alv=new ArrayList<Float>();
    	//������������б�--������֯��
    	ArrayList<Float> alvResult=new ArrayList<Float>(); 
    	//ԭʼ�������б�
    	ArrayList<Float> aln=new ArrayList<Float>();    
    	//���������б�
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
		    
		    //���ɷ���������
		    size=alnResult.size();
		    float[] nXYZ=new float[size];
		    for(int i=0;i<size;i++)  
		    {
		    	nXYZ[i]=alnResult.get(i);
		    }
		    
		    //����3D�������
		    lo=new LoadedObjectVertexNormal(mv,vXYZ,nXYZ);
    	}
    	catch(Exception e)
    	{
    		Log.d("load error", "load error");
    		e.printStackTrace(); 
    	}    	
    	return lo;
    }
}
