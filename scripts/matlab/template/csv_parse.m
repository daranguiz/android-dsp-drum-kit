%% CSV Parser
% Dario Aranguiz

csvFile = 'C:\Users\daran\Dropbox\Petronics\sensor_data_dario_house.csv';
axis_names = ['X', 'Y', 'Z'];

% Rows: 
% 1 - Timestamp
% 2 - Accel X
% 3 - Accel Y
% 4 - Accel Z
% 5 - Gyro X
% 6 - Gyro Y
% 7 - Gyro Z
% 8 - Orientation X
% 9 - Orientation Y
% 10 - Orientation Z
csv = csvread(csvFile);

accel = csv(:, 2:4);
gyro = csv(:, 5:7);
orien = mod(180 + csv(:, end-3:end-1), 360);

% Normalize timestamps
timestamps = csv(:, 1) - csv(1,1);
timestamps = timestamps / 1000;

% Plot accelerometer
figure;
for i=1:3
    subplot(3,1,i), plot(timestamps, accel(:, i));
    title(strcat('Accel ', axis_names(i)));
end
xlabel('Time in seconds');

% Plot gyroscope
figure;
title('Gyroscope Readings');
for i=1:3
    subplot(3,1,i), plot(timestamps, gyro(:, i));
    title(strcat('Gyro ', axis_names(i)));
end
xlabel('Time in seconds');

figure;
title('Orientation Readings');
for i=1:3
    subplot(3,1,i), plot(timestamps, orien(:, i));
    title(strcat('Orientation ', axis_names(i)));
end
xlabel('Time in seconds');

%% Generate filter

% TODO: Select appropriate filter parameters 

Fs = 1000;           % Sampling Rate
Fc = 500;           % Cutoff Frequency
transBand = 50;       % Width of transition band
n = 100;             % Number of taps 

b = filter_gen(Fs, Fc, transBand, n);

%% Resample

% http://www.mathworks.com/help/signal/ref/resample.html
[accelResampled, accelTime] = resample(accel, timestamps, Fs);

figure;
for i=1:3
    subplot(3,1,i), plot(accelTime, accelResampled(:, i));
    title(strcat('Accel Resampled ', axis_names(i)));
end
xlabel('Time in seconds');

% TODO: Resample gyroscope

%% Apply filter

accelFiltered = filter(b, 1, accelResampled);

figure;
for i=1:3
    subplot(3,1,i), plot(accelTime, accelFiltered(:, i));
    title(strcat('Accel Filtered ', axis_names(i)));
end
xlabel('Time in seconds');

% TODO: Filter accelerometer
