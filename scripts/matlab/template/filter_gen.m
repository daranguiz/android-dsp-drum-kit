function b = filter_gen(Fs, Fc, transBand, n)

    % n =   ?;           % Number of taps 
    % Fs =  ?;           % Sampling Rate
    % Fc =  ?;           % Cutoff Frequency
    % transBand = ?;     % Width of transition band

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
    figure;
    [h,w] = freqz(b, 1);
    plot(f, a, w/pi, abs(h));
    title('Frequency Response');

    % String construction
    javaString = '\nprivate final float[] coefs = {';
    for i=1:n
        javaString = strcat(javaString, num2str(b(i)), ', ');
    end
    javaString(end-1:end) = '};';
    javaString = strcat(javaString, '\n');

    fprintf(javaString);

end