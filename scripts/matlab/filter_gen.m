%% FIR Filter Generation
% Dario Aranguiz
close all;

n = 10;            % Number of taps 
Fs = 25;           % Sampling Rate
Fc = 12;           % Cutoff Frequency
transBand = 4;     % Width of transition band

% Construct filter specification
f = [0, Fc / Fs, (Fc + transBand) / Fs, 1];
a = [1, 1, 0, 0];

% Parks-McClellan Method
b = firpm(n, f, a, [1 1]);

% Window-based Method
% b = fir1(n, Fc);

% Frequency Sampling-based Method
% b = fir2(n, f, a);

% Plot filter response over digital frequency range
[h,w] = freqz(b, 1);
plot(f, a, w/pi, abs(h));

% String construction
javaString = '\nprivate final float[] coefs = {';
for i=1:n
    javaString = strcat(javaString, num2str(b(i)), ', ');
end
javaString(end-1:end) = '};';
javaString = strcat(javaString, '\n');

fprintf(javaString);