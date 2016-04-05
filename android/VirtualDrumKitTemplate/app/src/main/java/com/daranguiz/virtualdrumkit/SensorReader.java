package com.daranguiz.virtualdrumkit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

/* http://stackoverflow.com/questions/18759849/how-to-write-a-class-to-read-the-sensor-value-in-android */
public class SensorReader implements SensorEventListener {

    // TODO: What gesture corresponds to what sound?
    private final int[] soundIndex = {
            R.raw.clap_analog,
            R.raw.crash_acoustic,
            R.raw.hihat_acoustic01,
            R.raw.kick_acoustic01,
            R.raw.openhat_acoustic01,
            R.raw.shaker_analog,
            R.raw.snare_acoustic01,
            R.raw.tom_acoustic01
    };

    private final long SEC_TO_MS = 1000;
    private final long RESAMPLE_PERIOD_IN_MS = Math.round(SEC_TO_MS * 0.04f); // TODO - Set according to your device bandwidth

    private final Context mContext;
    private final String csvFilename = "sensor_data.csv";
    private FileWriter writer;

    private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;
    private final Sensor mGyroscope;

    private GestureRecognizer mGestureRecognizer;
    private Resampler mAccelResampler;
    private Resampler mGyroResampler;
    private FirFilter[] mAccelFilter;
    private FirFilter[] mGyroFilter;

    private float[] lastAccelData = {0f, 0f, 0f};
    private float[] lastGyroData = {0f, 0f, 0f};

    public SensorReader(Context context) {
        mContext = context;

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        mGestureRecognizer = new GestureRecognizer();
        mAccelResampler = new Resampler(RESAMPLE_PERIOD_IN_MS);
        mGyroResampler = new Resampler(RESAMPLE_PERIOD_IN_MS);
        mAccelFilter = new FirFilter[3];
        mGyroFilter = new FirFilter[3];

        for (int i = 0; i < 3; i++) {
            mAccelFilter[i] = new FirFilter();
            mGyroFilter[i] = new FirFilter();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // STUB, do nothing
    }

    public boolean startCollection() {
        try {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File file = new File(path, csvFilename);
            file.createNewFile();
            writer = new FileWriter(file);

            Toast toast = Toast.makeText(mContext, "CSV file created successfully!", Toast.LENGTH_SHORT);
            toast.show();
        } catch (Exception e) {
            Log.e("ERROR", e.toString());
            Toast toast = Toast.makeText(mContext, "CSV file could not be created", Toast.LENGTH_SHORT);
            toast.show();
            return false;
        }

        register();

        return true;
    }

    public void stopCollection() {
        unregister();
    }

    public void register() {
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregister() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long timestamp = System.currentTimeMillis();

        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
        {
            lastAccelData = mAccelResampler.resample(event.values, timestamp);

            for (int i = 0; i < 3; i++) {
                lastAccelData[i] = mAccelFilter[i].filter(lastAccelData[i]);
            }
        }
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            lastGyroData = mGyroResampler.resample(event.values, timestamp);

            for (int i = 0; i < 3; i++) {
                lastGyroData[i] = mGyroFilter[i].filter(lastGyroData[i]);
            }
        }

        writeToCsv(event);

        /* Recognize */
        int gesture = mGestureRecognizer.recognize(lastAccelData, lastGyroData, System.currentTimeMillis());

        if (gesture != -1) {
            String sGesture = "Gesture recognized: " + Integer.toString(gesture);
            VirtualDrumKit.textStatus.setText(sGesture);

            Log.d("GESTURE", sGesture);

            if (gesture < soundIndex.length) {
                MediaPlayer mediaPlayer = MediaPlayer.create(mContext, soundIndex[gesture]);
                mediaPlayer.start();
            }
        }
    }

    private void writeToCsv(SensorEvent event) {
        String newLine = String.valueOf(System.currentTimeMillis()) + ", ";
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
        {
            newLine += Float.toString(event.values[0]) + ", ";
            newLine += Float.toString(event.values[1]) + ", ";
            newLine += Float.toString(event.values[2]) + ", ";

        }
        else
        {
            newLine += Float.toString(lastAccelData[0]) + ", ";
            newLine += Float.toString(lastAccelData[1]) + ", ";
            newLine += Float.toString(lastAccelData[2]) + ", ";
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            newLine += Float.toString(event.values[0]) + ", ";
            newLine += Float.toString(event.values[1]) + ", ";
            newLine += Float.toString(event.values[2]) + ", ";
        }
        else
        {
            newLine += Float.toString(lastGyroData[0]) + ", ";
            newLine += Float.toString(lastGyroData[1]) + ", ";
            newLine += Float.toString(lastGyroData[2]) + ", ";
        }
        newLine += "\n";

        try
        {
            writer.write(newLine);
            writer.flush();
        }
        catch (Exception e)
        {
            Toast toast = Toast.makeText(mContext, "Could not write to CSV file", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
