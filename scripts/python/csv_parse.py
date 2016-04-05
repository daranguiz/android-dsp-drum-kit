#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import numpy as np
import matplotlib.pyplot as plt

csvfilename = '../sample_sensor_data.csv'
axis_names = ['X', 'Y', 'Z']

# Rows:
# 0 - Timestamp
# 1 - Accel X
# 2 - Accel Y
# 3 - Accel Z
# 4 - Gyro X
# 5 - Gyro Y
# 6 - Gyro Z
# 7 - NaNs

data = np.genfromtxt(csvfilename, delimiter=',').T

timestamps = (data[0] - data[0, 0]) / 1000  # Normalized

accel_data = data[1:4]
gyro_data = data[4:-1]  # Deal with that trailing comma

# Plot Accelerometer Data
fig, axarr = plt.subplots(3)
fig.tight_layout()
fig.subplots_adjust(top=0.88)
fig.suptitle('Accelerometer Data', size=16)
for i in range(3):
    axarr[i].plot(timestamps, accel_data[i])
    axarr[i].set_title('Accelerometer : {}'.format(axis_names[i]))

plt.savefig('accel_data.png')

# Plot Gyroscope Data
fig, axarr = plt.subplots(3)
fig.tight_layout()
fig.subplots_adjust(top=0.88)
fig.suptitle('Gyroscope Data', size=16)
for i in range(3):
    axarr[i].plot(timestamps, gyro_data[i])
    axarr[i].set_title('Gyroscope : {}'.format(axis_names[i]))

plt.savefig('gyro_data.png')

# Generate filter


def filter_gen(Fs=25, Fc=12, transBand=4, numtaps=10):
    """ Generate a filter, with the given parameters. """
    import scipy.signal as ssig

    # Check out some more right here:
    # http://docs.scipy.org/doc/scipy/reference/signal.html#filter-design
    h = ssig.firwin(numtaps=numtaps, cutoff=Fc / Fs, width=transBand / Fs)
    return h


def view_filter(b):
    """ Given a filter, look at it. Like MATLAB's freqz.
        http://docs.scipy.org/doc/scipy-0.16.0/reference/generated/scipy.signal.freqz.html
    """
    import scipy.signal as ssig
    w, h = ssig.freqz(b)

    fig = plt.figure()
    plt.title('Digital filter frequency response')
    ax1 = fig.add_subplot(111)

    plt.plot(w, 20 * np.log10(abs(h)), 'b')
    plt.ylabel('Amplitude [dB]', color='b')
    plt.xlabel('Frequency [rad/sample]')

    ax2 = ax1.twinx()
    angles = np.unwrap(np.angle(h))
    plt.plot(w, angles, 'g')
    plt.ylabel('Angle (radians)', color='g')
    plt.grid()
    plt.axis('tight')
    plt.savefig('designed_filter.png')


Fs = 100        # Sampling Rate
Fc = 12         # Cutoff Frequency
transBand = 10  # Width of transition band
numtaps = 10    # Number of taps

b = filter_gen(Fs, Fc, transBand, numtaps)
view_filter(b)
