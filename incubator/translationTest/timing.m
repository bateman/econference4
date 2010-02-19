len = [55, 110, 165, 225, 277, 362, 519, 559, 1321];
msa = [761, 530, 698, 830, 955, 1102, 1300, 1705, 3467];
msg = [2163, 1954, 2017, 1969, 1950, 1890, 2085, 2065, 2194];

plot(len, msa, "-*;apertium-service;", 
	len, msg, "-+;Google Translate;");

title ('Comparison in the "Sentence Length - Time" space', "fontsize", 30);
xlabel ("Sentence Length (string length)", "fontsize", 30);
ylabel ("Time (ms)", "fontsize", 30);
legend ("location", "northwest");
legend ("boxon");
grid ("on");

replot;
