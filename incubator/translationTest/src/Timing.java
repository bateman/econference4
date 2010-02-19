import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.apertium.api.ApertiumXMLRPCClient;
import org.apertium.api.exceptions.ApertiumXMLRPCClientException;

class Timing extends Thread {
	
	public ApertiumXMLRPCClient client = null;
	public String text = null;
	public int cycles = 0;
	
	public boolean ws = false;
	
	public Timing(ApertiumXMLRPCClient c, String t, int cy) {
		client = c;
		text = t;
		cycles = cy;
		
		ws = false;
	}
	
	public Timing(String t, int cy) {
		text = t;
		cycles = cy;
		
		ws = true;
	}
	
	public static long benchApertium(ApertiumXMLRPCClient c, String text, int cycles, int threads) throws InterruptedException {
		Timing[] t = new Timing[threads];
		
		for (int i = 0; i < threads; ++i)
			t[i] = new Timing(c, text, cycles);
		
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < threads; ++i)
			t[i].start();
		
		for (int i = 0; i < threads; ++i)
			t[i].join();
		
		long stopTime = System.currentTimeMillis();
		
		return stopTime - startTime;
	}
	
	public static long benchApertium(String text, int cycles, int threads) throws InterruptedException {
		Timing[] t = new Timing[threads];
		
		for (int i = 0; i < threads; ++i)
			t[i] = new Timing(text, cycles);
		
		long startTime = System.currentTimeMillis();
		
		for (int i = 0; i < threads; ++i)
			t[i].start();
		
		for (int i = 0; i < threads; ++i)
			t[i].join();
		
		long stopTime = System.currentTimeMillis();
		
		return stopTime - startTime;
	}
	
	public void run() {
		if (client == null) {
			if (ws) {
				try {
					//Main.benchApertiumWS(text, cycles);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				//Main.benchApertiumApp(text, cycles);
			}
		} else {
			try {
				//Main.benchApertium(client, text, cycles);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}