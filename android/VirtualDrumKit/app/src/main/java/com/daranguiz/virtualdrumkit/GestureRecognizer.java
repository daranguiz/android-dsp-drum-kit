package com.daranguiz.virtualdrumkit;

public class GestureRecognizer {

    /* Parameters */
    private final float[] accelThresholds = {4f, 4f, 4f};
    private final float[] gyroThresholds = {4f, 4f, 4f};
    private final long minSamplesBetweenGestures = 10;

    /* State variables */
    private long samplesSinceLastGesture;

    public GestureRecognizer() {
        samplesSinceLastGesture = minSamplesBetweenGestures + 1;
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
    public int recognize(float[] accel, float[] gyro) {
        samplesSinceLastGesture += 1;

        if (samplesSinceLastGesture < minSamplesBetweenGestures) {
            return -1;
        }

        int accelOffset = 0;
        int gyroOffset = 6;
        int gesturesPerDof = 6;

        /* First accel */
        for (int i = 0; i < gesturesPerDof; i++ ) {
            if (accel[i] < -1 * accelThresholds[i]) {
                samplesSinceLastGesture = 0;
                return accelOffset + 2 * i;
            } else if (accel[i] > accelThresholds[i]) {
                samplesSinceLastGesture = 0;
                return accelOffset + 2 * i + 1;
            }
        }

        /* Then gyro */
        for (int i = 0; i < gesturesPerDof; i++ ) {
            if (gyro[i] < -1 * gyroThresholds[i]) {
                samplesSinceLastGesture = 0;
                return gyroOffset + 2 * i;
            } else if (gyro[i] > gyroThresholds[i]) {
                samplesSinceLastGesture = 0;
                return gyroOffset + 2 * i + 1;
            }
        }

        return -1;
    }

}
