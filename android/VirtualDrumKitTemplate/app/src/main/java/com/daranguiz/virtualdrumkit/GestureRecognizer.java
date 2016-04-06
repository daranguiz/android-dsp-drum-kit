package com.daranguiz.virtualdrumkit;

import android.util.Log;

public class GestureRecognizer {

    /* Parameters */
    private final long SEC_TO_MS = 1000;
    private final float[] accelThresholds = {8f, 8f, 8f};               // TODO
    private final float[] gyroThresholds = {4f, 4f, 4f};                // TODO
    private final long minTimeBetweenGestures = Math.round(SEC_TO_MS * 0.2);  // TODO

    /* State variables */
    private long timeSinceLastGesture;
    private long lastTimestamp;

    public GestureRecognizer() {
        timeSinceLastGesture = minTimeBetweenGestures + 1;
        lastTimestamp = 0;
    }

    /**
     * -1 = No gesture
     * 0 = Accel X neg
     * 1 = Accel X pos
     * 2 = Accel Y neg
     * 3 = Accel Y pos
     * 4 = Accel Z neg
     * 5 = Accel Z pos
     * 6 = Gyro X neg
     * 7 = Gyro X pos
     * 8 = Gyro Y neg
     * 9 = Gyro Y pos
     * 10 = Gyro Z neg
     * 11 = Gyro Z pos
     */
    public int recognize(float[] accel, float[] gyro, long timestamp) {
        long delta = timestamp - lastTimestamp;
        lastTimestamp = timestamp;
        timeSinceLastGesture += delta;

        if (timeSinceLastGesture < minTimeBetweenGestures) {
            return -1;
        }


        float maxAccel = 0;
        int maxAccelIdx = 0;
        float maxGyro = 0;
        int maxGyroIdx = 0;

        /* First, find the largest magnitude value on accelerometer and on gyroscope
         * NOTE: Math.abs() may be useful
         */
        for (int i = 1; i < 3; i++) {
            // TODO
        }

        int accelOffset = 0;
        int gyroOffset = 6;

        /* Then compare to known thresholds */
        // TODO

        return -1;
    }

}
