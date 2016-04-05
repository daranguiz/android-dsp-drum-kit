#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import numpy as np
import matplotlib.pyplot as plt

csvfilename = '../matlab/sample_sensor_data.csv'
axis_names = ['X', 'Y', 'Z']

# Rows:
# 0 - Timestamp
# 1 - Accel X
# 2 - Accel Y
# 3 - Accel Z
# 4 - Gyro X
# 5 - Gyro Y
# 6 - Gyro Z

data = np.genfromtxt(csvfilename, delimiter=',').T

timestamps = (data[0] - data[0, 0]) / 1000  # Normalized

# Plot Accelerometer Data
fig, axarr = plt.subplots(3)
fig.tight_layout()
fig.subplots_adjust(top=0.88)
fig.suptitle('Accelerometer Data', size=16)
for i in range(3):
    axarr[i].plot(timestamps, data[1 + i])
    axarr[i].set_title('Accelerometer : {}'.format(axis_names[i]))

plt.savefig('accel_data.png')

# Plot Gyroscope Data
fig, axarr = plt.subplots(3)
fig.tight_layout()
fig.subplots_adjust(top=0.88)
fig.suptitle('Gyroscope Data', size=16)
for i in range(3):
    print(4+i)
    axarr[i].plot(timestamps, data[4 + i])
    axarr[i].set_title('Gyroscope : {}'.format(axis_names[i]))

plt.savefig('gyro_data.png')
