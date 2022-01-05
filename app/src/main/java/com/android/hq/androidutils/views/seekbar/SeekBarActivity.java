package com.android.hq.androidutils.views.seekbar;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.widget.SeekBar;

import com.android.hq.androidutils.R;

public class SeekBarActivity extends AppCompatActivity {

    private SeekBar mSeekBar1;
    private Drawable mSeekBar1Background;
    private Drawable mSeekBar1Progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seek_bar);
        mSeekBar1 = findViewById(R.id.seek_bar_1);
        mSeekBar1.setOnSeekBarChangeListener(mOnSeekBarChangeListener1);
        LayerDrawable layerDrawable = (LayerDrawable)mSeekBar1.getProgressDrawable();
        mSeekBar1Progress = layerDrawable.findDrawableByLayerId(android.R.id.progress);
        mSeekBar1Background = layerDrawable.findDrawableByLayerId(android.R.id.background);
    }

    private SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener1 = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Rect r = mSeekBar1Progress.getBounds();
            mSeekBar1Progress.setBounds(r.left,r.top-2,r.right,r.bottom+2);

            Rect r1 = mSeekBar1Background.getBounds();
            mSeekBar1Background.setBounds(r1.left,r1.top-2,r1.right,r1.bottom+2);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Rect r = mSeekBar1Progress.getBounds();
            mSeekBar1Progress.setBounds(r.left,r.top+2,r.right,r.bottom-2);

            Rect r1 = mSeekBar1Background.getBounds();
            mSeekBar1Background.setBounds(r1.left,r1.top+2,r1.right,r1.bottom-2);
        }
    };
}