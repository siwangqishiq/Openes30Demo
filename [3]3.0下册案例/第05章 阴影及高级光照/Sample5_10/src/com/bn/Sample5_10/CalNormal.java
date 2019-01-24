package com.bn.Sample5_10;
import android.annotation.SuppressLint;
import java.util.HashMap;
import java.util.HashSet;

public class CalNormal {
	@SuppressLint("UseSparseArrays")
	//dataΪ������ʱ�Ķ��������������飬indicesΪ��������
	public static float[] calNormal(float[] data, int[] indices) {
		// ƽ��ǰ����������Ӧ�ĵ�ķ���������Map
		// ��HashMap��keyΪ��������� valueΪ�����ڵĸ�����ķ������ļ���
		HashMap<Integer, HashSet<Normal>> hmn = new HashMap<Integer, HashSet<Normal>>();

		float[] answer = new float[data.length];

		for (int i = 0; i < indices.length / 3; i++)
		{//����ÿ��������
			
			int[] index = new int[] { indices[i * 3 + 0], indices[i * 3 + 1],
					indices[i * 3 + 2] };// �����������飬3������������һ����

			// 3�������3������
			float x0 = data[index[0] * 3 + 0];
			float y0 = data[index[0] * 3 + 1];
			float z0 = data[index[0] * 3 + 2];

			float x1 = data[index[1] * 3 + 0];
			float y1 = data[index[1] * 3 + 1];
			float z1 = data[index[1] * 3 + 2];

			float x2 = data[index[2] * 3 + 0];
			float y2 = data[index[2] * 3 + 1];
			float z2 = data[index[2] * 3 + 2];

			// ͨ��������������������0-1��0-2�����õ�����ķ�����
			// ��0�ŵ㵽1�ŵ������
			float vxa = x1 - x0;
			float vya = y1 - y0;
			float vza = z1 - z0;
			// ��0�ŵ㵽2�ŵ������
			float vxb = x2 - x0;
			float vyb = y2 - y0;
			float vzb = z2 - z0;
			// ͨ�������������Ĳ�����㷨����
			float[] tempNormal=LoadUtil.getCrossProduct(vxa, vya, vza, vxb,vyb, vzb);
			//��񻯼���õ��ķ�����
			float[] vNormal = LoadUtil.vectorNormal(tempNormal);

			for (int tempInxex : index) 
			{// ��¼ÿ��������ķ���������������ӽ���������Ӧ�ļ���hsn��
			// ��ȡ��ǰ������Ӧ��ķ���������
				HashSet<Normal> hsn = hmn.get(tempInxex);
				if (hsn == null) {// �����ϲ������򴴽�
					hsn = new HashSet<Normal>();
				}
				// ���˵�ķ�������ӵ�������
				// ����Normal����д��equals���������ͬ���ķ����������ظ������ڴ˵�
				// ��Ӧ�ķ�����������
				hsn.add(new Normal(vNormal[0], vNormal[1], vNormal[2]));
				// �����ϷŽ�HsahMap��
				hmn.put(tempInxex, hsn);
			}
		}

		// ���ɷ���������
		int c = 0;
		for (int i = 0; i < data.length/3; i++) {
			// ���ݵ�ǰ���������Map��ȡ��һ���������ļ���
			HashSet<Normal> hsn = hmn.get(i);
			// ���ƽ��������
			float[] tn = Normal.getAverage(hsn);
			// ���������ƽ����������ŵ�������������
			answer[c++] = tn[0];
			answer[c++] = tn[1];
			answer[c++] = tn[2];
		}
		return answer;
	}
}
