package com.daranguiz.virtualdrumkit;

import android.util.Log;

public class GestureRecognizer {

    /* Parameters */
    private final long SEC_TO_MS = 1000;
    private final float[] accelThresholds = {8f, 8f, 8f};
    private final float[] gyroThresholds = {4f, 4f, 4f};
    private final long minTimeBetweenGestures = Math.round(SEC_TO_MS * 0.4);

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

        /* First find max (by magnitude) of each sensor */
        float maxAccel = accel[0];
        int maxAccelIdx = 0;
        float maxGyro = gyro[0];
        int maxGyroIdx = 0;

        for (int i = 1; i < 3; i++) {
            if (Math.abs(accel[i]) > Math.abs(maxAccel)) {
                maxAccel = accel[i];
                maxAccelIdx = i;
            }
            if (Math.abs(gyro[i]) > Math.abs(maxGyro)) {
                maxGyro = gyro[i];
                maxGyroIdx = i;
            }
        }

        int accelOffset = 0;
        int gyroOffset = 6;

        /* Then check thresholds */
        if (Math.abs(maxAccel) > accelThresholds[maxAccelIdx]) {
            timeSinceLastGesture = 0;
            if (maxAccel < 0) {
                return accelOffset + 2 * maxAccelIdx;
            } else {
                return accelOffset + 2 * maxAccelIdx + 1;
            }
        }

        if (Math.abs(maxGyro) > gyroThresholds[maxGyroIdx]) {
            timeSinceLastGesture = 0;
            if (maxGyro < 0) {
                return gyroOffset + 2 * maxAccelIdx;
            } else {
                return gyroOffset + 2 * maxAccelIdx + 1;
            }
        }

        return -1;
    }

}
