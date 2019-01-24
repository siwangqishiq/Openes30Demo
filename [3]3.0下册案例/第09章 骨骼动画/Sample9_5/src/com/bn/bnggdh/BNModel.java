package com.bn.bnggdh;

import java.io.IOException;
import java.io.InputStream;

public class BNModel {

	private float time = 0;//��ǰʱ��
	private float onceTime;// һ�ζ��������ʱ��
	private float interval = 2.0f;// һ�鶯�� �� һ�鶯�� �ļ��ʱ��
	private boolean isNormal;//�Ƿ��з�����
	private float dt;//����
	private float dtFactor;//����

	private BnggdhDraw cd;// ����������ģ����
	private BnggdhDrawNoNormal cdnn;// ������������ģ����

	/**
	 * 
	 * @param sourceName	ģ������
	 * @param picName		ͼƬ����
	 * @param isNormal		�Ƿ��й���
	 * @param dtFactor		���ʣ���Χ��0-1
	 * @param r				��Դ������
	 */
	public BNModel(String sourceName, String picName, boolean isNormal,
			float dtFactor, MySurfaceView mv) {
		String path = "bnggdh/" + sourceName;
		try {
			InputStream is = mv.getResources().getAssets().open(path);
			if (isNormal == true) {
				cd = new BnggdhDraw(is, mv, picName);
				onceTime = cd.maxKeytime;
			} else {
				cdnn = new BnggdhDrawNoNormal(is, mv, picName);
				onceTime = cdnn.maxKeytime;
			}
			this.dtFactor = dtFactor;
			this.dt = dtFactor * onceTime;
			this.isNormal = isNormal;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ���Ʒ���
	 */
	public void draw() {
		System.out.println("time = " + time);
		MatrixState.pushMatrix();
//		MatrixState.translate(0, -70, 0);
//		MatrixState.scale(0.8f, 0.8f, 0.8f);
		if (isNormal == true) {
			cd.draw(time);
		} else {
			cdnn.draw(time);
		}
		time += dt;// ����ģ�Ͷ���ʱ��
		// ����ǰ����ʱ������ܵĶ���ʱ����ʵ�ʲ���ʱ����ڵ�ǰ����ʱ���ȥ�ܵĶ���ʱ��
		if (time >= (onceTime + dt + interval)) {
			time = 0;
		}
		MatrixState.popMatrix();
	}

	/**
	 * ��ȡ����
	 * @return
	 */
	public float getDtFactor() {
		return dtFactor;
	}

	/**
	 * ��������
	 * @param dtFactor
	 */
	public void setDtFactor(float dtFactor) {
		if(dtFactor > 0 && dtFactor < 1){
			this.dtFactor = dtFactor;
			this.dt = dtFactor * onceTime;
		}
	}

	/**
	 * ���õ�ǰʱ��
	 * @param time
	 */
	public void setTime(float time){
		if(time >= 0 && time <= this.onceTime){
			this.time = time;
		}
	}
	
	/**
	 * ��ȡ��ǰʱ��
	 * @return
	 */
	public float getTime(){
		return this.time;
	}
	
	/**
	 * ��ȡ��ģ�͵Ĺ��������ܵ�ʱ��
	 * @return
	 */
	public float getOnceTime(){
		return this.onceTime;
	}
	
	public float[] getMatrix(String id){
		if (isNormal == true) {
			return cd.bnggdh.getMatrix(id);
		} else {
			return cdnn.bnggdh.getMatrix(id);
		}
	}
}
