package com.example.administrator.rebound;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringConfig;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;

public class MainActivity extends Activity implements View.OnTouchListener, SpringListener {
    private int tension = 40; //张力系数
    private int friction = 3; //阻力系数
    private ImageView ivScalaImage;
    private SpringSystem mSpringSystem;
    private Spring mSpring;
    private SeekBar skTension,skFriction;
    private TextView tvTension,tvFriction;
    private SBListener sbListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化控件
        initWidget();
        //创建系统用于循环执行控件弹簧效果
        mSpringSystem = SpringSystem.create();
        //给系统添加一个“弹簧”
        mSpring = mSpringSystem.createSpring();
        //添加监听器，监听“弹簧”的形变
        mSpring.addListener(this);
        //根据张力系数和阻力系数创建一组“弹簧”参数
        SpringConfig config = new SpringConfig(tension, friction);
        //配置“弹簧”
        mSpring.setSpringConfig(config);

    }
    private void initWidget() {
        ivScalaImage = findViewById(R.id.imageView);
        ivScalaImage.setOnTouchListener(this);
        skTension =  findViewById(R.id.skTension);
        skFriction =  findViewById(R.id.skFriction);
        sbListener = new SBListener();
        skTension.setMax(100);
        skFriction.setMax(30);
        skTension.setOnSeekBarChangeListener(sbListener);
        skFriction.setOnSeekBarChangeListener(sbListener);
        tvTension =  findViewById(R.id.tvTension);
        tvFriction =  findViewById(R.id.tvFriction);
        skTension.setProgress(tension);
        skFriction.setProgress(friction);
        tvTension.setText("张力系数: " + tension);
        tvFriction.setText("阻力系数: "+friction);
    }
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mSpring.setEndValue(1f);
                return true;
            case MotionEvent.ACTION_UP:
                mSpring.setEndValue(0f);
                return true;
        }
        return false;
    }
    @Override
    public void onSpringUpdate(Spring spring) {
        float value = (float) spring.getCurrentValue();
        float scale = 1f - (value * 0.5f);
        ivScalaImage.setScaleX(scale);
        ivScalaImage.setScaleY(scale);
    }
    @Override
    public void onSpringAtRest(Spring spring) {
        SpringConfig config = new SpringConfig(tension, friction);
        mSpring.setSpringConfig(config);
    }
    @Override
    public void onSpringActivate(Spring spring) {
    }
    @Override
    public void onSpringEndStateChange(Spring spring) {
    }
    private class SBListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getId() == R.id.skTension){
                tension = progress;
                skTension.setProgress(tension);
                tvTension.setText("张力系数: " + tension);
            } else if (seekBar.getId() == R.id.skFriction){
                friction = progress;
                skFriction.setProgress(friction);
                tvFriction.setText("阻力系数: "+friction);
            }
        }
        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mSpring.setAtRest();
        }
    }
}
