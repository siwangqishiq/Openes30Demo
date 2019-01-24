package com.bn.Sample6_6;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.ToggleButton;

public class Sample6_6Activity extends Activity {
	MySurfaceView msv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		// ����Ϊȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		// ��ʼ��GLSurfaceView
        msv=new MySurfaceView(this);
        setContentView(R.layout.activity_flag);
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);
        ll.addView(msv);
        msv.requestFocus();//��ȡ����
        msv.setFocusableInTouchMode(true);//����Ϊ�ɴ���
        SeekBar sb = (SeekBar) findViewById(R.id.seekBar1);
        sb.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener()
                {
    				@Override
    				public void onProgressChanged(SeekBar seekBar, int progress,
    						boolean fromUser) {
    					Constant.WindForce = 4.0f*progress/seekBar.getMax();//���÷���
    				}
    				@Override
    				public void onStartTrackingTouch(SeekBar seekBar) {	}
    				@Override
    				public void onStopTrackingTouch(SeekBar seekBar) { }            	
                }
            );
        Button bt1 = (Button) findViewById(R.id.button1);//ȡ���̶����Ӱ�ť
        bt1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//ȡ���̶�����
				Sample6_6Activity.this.msv.mRenderer.pc.particles[0][0].bLocked = false;
				Sample6_6Activity.this.msv.mRenderer.pc.particles[Constant.NUMROWS][0].bLocked = false;
			}
		});
        Button bt2 = (Button) findViewById(R.id.button2);//���ð�ť
        bt2.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		synchronized(Constant.lockB)
        		{
        			Sample6_6Activity.this.msv.mRenderer.pc.initalize();//��������
        		}
        	}
        });
        Button bt3 = (Button) findViewById(R.id.button3);//�л����水ť
        bt3.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Sample6_6Activity.this.msv.nowId = (Sample6_6Activity.this.msv.nowId+1)%3;//�л�id
        	}
        });
        ToggleButton tb = (ToggleButton) findViewById(R.id.toggleButton1);
        tb.setOnCheckedChangeListener(new OnCheckedChangeListener() {		
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				Constant.isC = isChecked;//�Ƿ�����ײ���
			}
		});
    }
	@Override
	protected void onPause() {
		super.onPause();
		msv.onPause();
	}
    
}
