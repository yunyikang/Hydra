package com.example.yunyi.projecthydra;

/**
 * Created by Yun YI on 23/11/2016.
 */

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends AppCompatActivity {

    private SeekBar seekBar1;
    private TextView textView1;
    private SeekBar seekBar2;
    private TextView textView2;
    private ImageView image;

    private int imagenum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Intent intent = getIntent();

        Bundle bd = intent.getExtras();

        if (bd != null) {
            imagenum = (int) bd.get("IMAGE_I_NEED");
        }

        seekBar1 = (SeekBar) findViewById(R.id.seekBar1);
        textView1 = (TextView) findViewById(R.id.textView1);
        seekBar2 = (SeekBar) findViewById(R.id.seekBar2);
        textView2 = (TextView) findViewById(R.id.textView2);
        image = (ImageView) findViewById(R.id.settingsimage);

        image.setImageResource(imagenum);

        // Initialize the textview with '0'.
        textView1.setText(seekBar1.getProgress()+1);
        seekBar1.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress1 = 0;

            public void onProgressChanged(SeekBar seekBar, int progressValue1, boolean fromUser) {
                progress1 = progressValue1;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView1.setText(progress1 +1);
            }
        });

        textView2.setText(seekBar2.getProgress()+1);
        seekBar2.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress2 = 0;

            public void onProgressChanged(SeekBar seekBar, int progressValue2, boolean fromUser) {
                progress2 = progressValue2;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                textView2.setText(progress2 +1);
            }
        });
    }

}
