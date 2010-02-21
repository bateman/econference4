len = [18, 36, 54, 72, 90, 108, 126, 145, 162, 182, 199, 216, 234, 253, 271, 298, 310, 362];
msa = [1780, 1892, 2534, 2609, 3023, 3531, 3945, 4082, 4227, 4920, 5575, 5077, 5481, 6458, 6284, 6591, 7122, 7560];
msg = [17921, 16325, 16222, 17848, 15674, 15933, 15981, 15780, 16024, 16035, 16639, 16707, 16104, 17044, 16684, 16635, 16901, 18101];

threads = [18, 36, 54, 72, 90, 108, 126, 145, 162, 182, 199, 216, 234, 253, 271, 298, 310, 362];
msat = [7416, 3914, 2844, 2802, 3574, 4697, 5178, 5198];
msgt = [16495, 8257, 6001, 4516, 3724, 2817, 2411, 2085];


plot(len, msa, "-*;apertium-service;", 
	len, msg, "-+;Google Translate;");

title ('Comparison in the "Sentence Length - Time" space', "fontsize", 30);
xlabel ("Sentence Length (string length)", "fontsize", 30);
ylabel ("Time (ms)", "fontsize", 30);
legend ("location", "northwest");
legend ("boxon");
grid ("on");

print lentime.png -dpng -mono -solid -F:30;

replot;

plot(threads, msat, "-*;apertium-service;",
	len, msgt, "-+;Google Translate;");

title ('Comparison in the "Concurrent Requests - Time" space', "fontsize", 30);
xlabel ("Concurrent Requests (num)", "fontsize", 30);
ylabel ("Time (ms)", "fontsize", 30);
legend ("location", "northwest");
legend ("boxon");
grid ("on");

print threadstime.png -dpng -mono -solid -F:30;

replot;
