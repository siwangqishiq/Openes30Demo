package com.bn.Sample11_4;

import java.io.*;

public class ProgramObject implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 231714197949345483L;
	//ʵ�ʶ������볤��
	int binLength;
	//���������ʽ
	int binaryFormat;
	//����������
	byte[] data;
	
	public ProgramObject(int binLength,int binaryFormat,byte[] data)
	{
		this.binLength=binLength;
		this.binaryFormat=binaryFormat;
		this.data=data;
	}
}
