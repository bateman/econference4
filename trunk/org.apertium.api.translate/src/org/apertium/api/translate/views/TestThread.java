package org.apertium.api.translate.views;

public class TestThread extends Thread {
	
	private TranslateView tv = null;
	
	public TestThread(TranslateView tv) {
		this.tv = tv;
	}
	
	public void run() {
		for (int i = 0; i < 5; ++i) {
			tv.appendMessage("XYZ");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
