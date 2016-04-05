package com.daranguiz.virtualdrumkit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.io.FileWriter;

/* http://stackoverflow.com/questions/18759849/how-to-write-a-class-to-read-the-sensor-value-in-android */
public class SensorReader implements SensorEventListener {

    private final Context mContext;
    private final String csvFilename = "sensor_data.csv";
    private FileWriter writer;

    private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;
    private final Sensor mGyroscope;

    private float[] lastAccelData = {0f, 0f, 0f};
    private float[] lastGyroData = {0f, 0f, 0f};

    public SensorReader(Context context) {
        mContext = context;

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // This is where the magic happens
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // STUB, do nothing
    }

    public void register() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregister() {
        mSensorManager.unregisterListener(this);
    }

    private void writeToCsv(SensorEvent event) {

    }
}
