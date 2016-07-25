package com.daranguiz.virtualdrumkit;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VirtualDrumKit extends AppCompatActivity {

    public TextView textStatus;
    private Button buttonStart;
    private Button buttonStop;

    private SensorReader mSensorReader;
    private boolean sensorsOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virtual_drum_kit);

        sensorsOn = false;
        mSensorReader = new SensorReader(this);

        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStop = (Button) findViewById(R.id.buttonStop);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sensorsOn) {
                    sensorsOn = mSensorReader.startCollection();
                    if (sensorsOn) {
                        textStatus.setText("Started!");
                    }
                }
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sensorsOn) {
                    sensorsOn = false;
                    mSensorReader.stopCollection();
                    textStatus.setText("Stopped.");
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (sensorsOn) {
            mSensorReader.register();
        }
    }

    @Override
    protected  void onPause() {
        super.onPause();

        if (sensorsOn) {
            mSensorReader.unregister();
        }
    }

}
