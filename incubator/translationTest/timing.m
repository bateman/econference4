len = [18, 36, 54, 72, 90, 108, 126, 145, 162, 182, 199, 216, 234, 253, 271, 298, 310, 362];
msa = [1780, 1892, 2534, 2609, 3023, 3531, 3945, 4082, 4227, 4920, 5575, 5077, 5481, 6458, 6284, 6591, 7122, 7560] ./ 256;
msg = [17921, 16325, 16222, 17848, 15674, 15933, 15981, 15780, 16024, 16035, 16639, 16707, 16104, 17044, 16684, 16635, 16901, 18101] ./ 256;

threads = [1, 2, 3, 4, 5, 6, 7, 8];
msat = [7416, 3914, 2844, 2802, 3574, 4697, 5178, 5198] ./ 256;
msgt = [16495, 8257, 6001, 4516, 3724, 2817, 2411, 2085] ./ 256;

set(0, "Defaulttextfontsize", 64);

plot(len, msa, '-*r;apertium-service;', 
	len, msg, '-+b;Google Translate;');

title ('Comparison in the "Sentence Length - Time" space', 'FontSize', 64);
xlabel ('Sentence Length (string length)', 'FontSize', 64);
ylabel ('Time (ms)', 'FontSize', 64);
legend ('location', 'northwest');
legend ('boxon');
grid ('on');

ylim([0, 100]);

print('lentime.png', '-dpng', '-F:64');

replot;

plot(threads, msat, '-*r;apertium-service;',
	threads, msgt, '-+b;Google Translate;');

title ('Comparison in the "Concurrent Requests - Time" space', 'FontSize', 64);
xlabel ('Concurrent Requests (num)', 'FontSize', 64);
ylabel ('Time (ms)', 'FontSize', 64);
legend ('location', 'northwest');
legend ('boxon');
grid ('on');

print('threadstime.png', '-dpng', '-F:64');

replot;
