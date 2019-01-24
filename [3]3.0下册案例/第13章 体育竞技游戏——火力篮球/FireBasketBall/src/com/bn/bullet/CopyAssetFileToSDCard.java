package com.bn.bullet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import com.bn.bullet7111.R;

import android.R.string;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;



public class CopyAssetFileToSDCard {
	private Context context;
	public  CopyAssetFileToSDCard(Context context)
	{
		this.context = context;
	}
	public  void copy(String assetFilePath,String SDcardFilePath,String copyFileName) throws IOException {
		//1�����ж�sdcard�Ƿ���أ�Ȼ���ڿ��Ͻ�����Ҫ���ļ��м��ļ�
		File sdcardDir = Environment.getExternalStorageDirectory();
		File data = null;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
		{
			//�õ�һ��·����������sdcard���ļ���·��������
			String path = sdcardDir.getPath()+SDcardFilePath;//�õ�SDCard/profitData��·��
			File folder = new File(path);//�����ļ���
			data = new File(path,copyFileName);//����xml�ļ�
			if(!folder.exists())
			{
				//�������ڣ�����Ŀ¼��������Ӧ��������ʱ�򴴽�
				folder.mkdirs();
			}
			if(!data.exists())
			{
				data.createNewFile();
			}
		}
		
		//2�������ķ�������Ŀ��ȡ���ļ�		
		InputStream myInput;
		OutputStream myOutput = new FileOutputStream(data);
		myInput = context.getAssets().open(assetFilePath);
		byte[] buffer = new byte[1024];
		int length = myInput.read(buffer);
		while(length > 0)
		{
			myOutput.write(buffer,0,length);
			length = myInput.read(buffer);
		}
		myOutput.flush();
		myInput.close();
		myOutput.close();
	}
}
