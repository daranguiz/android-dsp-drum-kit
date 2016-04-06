package com.daranguiz.virtualdrumkit;

import java.util.List;
import java.util.Vector;

public class FirFilter {

    /* Filter coefficients */
    private static final float[] coefs = {1f, 0f, 0f}; // TODO
    private static final int NUM_TAPS = coefs.length;

    /* State variables */
    private List<Float> pastVals;

    public FirFilter() {
        pastVals = new Vector<Float>(NUM_TAPS);

        for (int i = 0; i < NUM_TAPS; i++) {
            pastVals.add(0f);
        }
    }

    /* Streaming filtering */
    public float filter(float inputVal) {
        /* Push new value first */
        pastVals.remove(NUM_TAPS - 1);
        pastVals.add(0, inputVal);

        /* Filter */
        float filteredVal = 0;
        // TODO

        // pastVals.get(0) == get most recent value

        return filteredVal;
    }

}
