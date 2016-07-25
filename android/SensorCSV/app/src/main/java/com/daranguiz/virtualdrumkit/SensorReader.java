package com.daranguiz.virtualdrumkit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;

/* http://stackoverflow.com/questions/18759849/how-to-write-a-class-to-read-the-sensor-value-in-android */
public class SensorReader implements SensorEventListener {

    private final Context mContext;
    private final String csvFilenameBase = "sensor_data";
    private FileWriter writer;

    private final SensorManager mSensorManager;
    private final Sensor mAccelerometer;
    private final Sensor mGyroscope;
    private final Sensor mMag;
    private final Sensor mOrientation;

    private float[] lastAccelData = {0f, 0f, 0f};
    private float[] lastGyroData = {0f, 0f, 0f};
    private float[] lastOrientationData = {0f, 0f, 0f};
    private float[] lastMagData = {0f, 0f, 0f, 0f, 0f, 0f};

    public SensorReader(Context context) {
        mContext = context;

        mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mMag = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED);
        mOrientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION)
        {
            lastAccelData[0] = event.values[0];
            lastAccelData[1] = event.values[1];
            lastAccelData[2] = event.values[2];
        }
        else if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        {
            lastGyroData[0] = event.values[0];
            lastGyroData[1] = event.values[1];
            lastGyroData[2] = event.values[2];
        }
        else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED)
        {
            lastMagData[0] = event.values[0];
            lastMagData[1] = event.values[1];
            lastMagData[2] = event.values[2];
            lastMagData[3] = event.values[3];
            lastMagData[4] = event.values[4];
            lastMagData[5] = event.values[5];
        }
        else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            ((VirtualDrumKit) mContext).textStatus.setText(Float.toString(event.values[0]));
            lastOrientationData[0] = event.values[0];
            lastOrientationData[1] = event.values[1];
            lastOrientationData[2] = event.values[2];
        }

        writeToCsv(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // STUB, do nothing
    }

    public boolean startCollection() {
        try {
            String csvFilename = csvFilenameBase + "_" + Long.toString(System.currentTimeMillis() / 1000L) + ".csv";
            File pathBase = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            File path = new File(pathBase.toString() + "/SensorData");
            if(!path.exists())
            {
                path.mkdir();
            }

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
        mSensorManager.registerListener(this, mMag, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this, mOrientation, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregister() {
        mSensorManager.unregisterListener(this);
    }

    private void writeToCsv(SensorEvent event) {
        String newLine = String.valueOf(System.currentTimeMillis()) + ", ";
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
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

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED)
        {
            newLine += Float.toString(event.values[0]) + ", ";
            newLine += Float.toString(event.values[1]) + ", ";
            newLine += Float.toString(event.values[2]) + ", ";
            newLine += Float.toString(event.values[3]) + ", ";
            newLine += Float.toString(event.values[4]) + ", ";
            newLine += Float.toString(event.values[5]) + ", ";
        }
        else
        {
            newLine += Float.toString(lastMagData[0]) + ", ";
            newLine += Float.toString(lastMagData[1]) + ", ";
            newLine += Float.toString(lastMagData[2]) + ", ";
            newLine += Float.toString(lastMagData[3]) + ", ";
            newLine += Float.toString(lastMagData[4]) + ", ";
            newLine += Float.toString(lastMagData[5]) + ", ";
        }

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION)
        {
            newLine += Float.toString(event.values[0]) + ", ";
            newLine += Float.toString(event.values[1]) + ", ";
            newLine += Float.toString(event.values[2]) + ", ";
        }
        else
        {
            newLine += Float.toString(lastOrientationData[0]) + ", ";
            newLine += Float.toString(lastOrientationData[1]) + ", ";
            newLine += Float.toString(lastOrientationData[2]) + ", ";
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