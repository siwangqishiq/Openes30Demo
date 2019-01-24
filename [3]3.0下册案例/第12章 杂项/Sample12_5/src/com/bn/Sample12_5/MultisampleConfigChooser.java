package com.bn.Sample12_5;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGL11;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import android.opengl.GLSurfaceView;
import android.util.Log;


public class MultisampleConfigChooser implements GLSurfaceView.EGLConfigChooser 
{
    static private final String kTag = "GDC11";
    private int[] mValue;
    @Override
    public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) 
    {
        mValue = new int[1];
        //configSpec����ָ����ѡ������ʱ��Ҫ���յ����ԣ�ͨ����id,value���δ��
        int[] configSpec = {
        		EGL11.EGL_RED_SIZE, 5,//ָ����ɫ�������еĺ�ɫ������λ��
        		EGL11.EGL_GREEN_SIZE, 6,//ָ����ɫ�������е���ɫ������λ��
        		EGL11.EGL_BLUE_SIZE, 5,//ָ����ɫ�������е���ɫ������λ��
        		EGL11.EGL_DEPTH_SIZE, 16,//ָ����Ȼ�����ÿ��������ȵ�λ��
        		EGL11.EGL_RENDERABLE_TYPE, 4,//ָ��������Ⱦ��һ������Ŀͻ���API������
        		EGL11.EGL_SAMPLE_BUFFERS, 1,//ָ�����ز����������ĸ���
        		EGL11.EGL_SAMPLES, 2,//ָ��ÿ���ص���������
        		EGL11.EGL_NONE         //�����б��Ըó���Ϊ������
        };
        //���ϵͳ��֧�ֵ�������Ϣ������
        /*
         *  �ڶ�������attrib_list    ���ȶ������������
         * ����������configs  ͼ��ϵͳ�����������������������õ�������
         * ���ĸ�����config_size   configs����ĳ���
         * ���ĸ�����num_config  ͼ��ϵͳ���صĿ��õ����ø���
         */
        if (!egl.eglChooseConfig(display, configSpec, null, 0,mValue)) 
        {
            throw new IllegalArgumentException("eglChooseConfig fail");
        }
        int numConfigs = mValue[0];//��¼������Ϣ������
        if (numConfigs <= 0) 
        {//û���ҵ����ʵĶ��ز������ã�����һ�����Ƕ��ز�������
            final int EGL_COVERAGE_BUFFERS_NV = 0x30E0;
            final int EGL_COVERAGE_SAMPLES_NV = 0x30E1;
          //����������Ϣ�������б�
            configSpec = new int[]{
            		EGL11.EGL_RED_SIZE, 5,
            		EGL11.EGL_GREEN_SIZE, 6,
            		EGL11.EGL_BLUE_SIZE, 5,
            		EGL11.EGL_DEPTH_SIZE, 16,
            		EGL11.EGL_RENDERABLE_TYPE, 4,
                    EGL_COVERAGE_BUFFERS_NV, 1,
                    EGL_COVERAGE_SAMPLES_NV, 2,
                    EGL11.EGL_NONE//�����б��Ըó���Ϊ������ 
            };
            //���ϵͳ��֧�ֵ�������Ϣ������
            if (!egl.eglChooseConfig(display, configSpec, null, 0,mValue))
            {
                throw new IllegalArgumentException("2nd eglChooseConfig  fail");
            }
            numConfigs = mValue[0];

            if (numConfigs <= 0) 
            {
                configSpec = new int[]{
                		EGL11.EGL_RED_SIZE, 5,
                		EGL11.EGL_GREEN_SIZE, 6,
                		EGL11.EGL_BLUE_SIZE, 5,
                		EGL11.EGL_DEPTH_SIZE, 16,
                		EGL11.EGL_RENDERABLE_TYPE, 4 ,
                		EGL11.EGL_NONE//�����б��Ըó���Ϊ������ 
                };
                //���ϵͳ��֧�ֵ�������Ϣ������
                if (!egl.eglChooseConfig(display, configSpec, null, 0,mValue)) 
                {
                    throw new IllegalArgumentException("3rd eglChooseConfig fail");
                }
                numConfigs = mValue[0];//��ȡ���õ�����
                if (numConfigs <= 0)
                {
                    throw new IllegalArgumentException("No configs match configSpec");
                }
            }
        }

        EGLConfig[] configs = new EGLConfig[numConfigs];//������������
        //���������ϢEGLConfig����
        if (!egl.eglChooseConfig(display, configSpec, configs, numConfigs,mValue)) 
        {
        	throw new IllegalArgumentException("data eglChooseConfig failed");
        }
        int index = -1;//��������ֵ����
        for (int i = 0; i < configs.length; ++i)
        {//���Ҷ��ز���������id
            if (findConfigAttrib(egl, display, configs[i], EGL10.EGL_RED_SIZE, 0) == 5) 
            {
                index = i;//��¼���ҵ����ز������õ�����ֵ
                break;//�˳�ѭ��
            }
        }
        if (index == -1) 
        {
            Log.w(kTag, "Did not find sane config, using first");
        }
        //��ȡ���ز��������ö���
        EGLConfig config = configs.length > 0 ? configs[index] : null;
        if (config == null)
        {
            throw new IllegalArgumentException("No config chosen");
        }
        return config;
    }

    private int findConfigAttrib(EGL10 egl, EGLDisplay display,EGLConfig config, int attribute, int defaultValue) {
    	//��ѯĳ�����õ�ĳ������id
        if (egl.eglGetConfigAttrib(display, config, attribute, mValue)) 
        {
            return mValue[0];
        }
        return defaultValue;
    }

}