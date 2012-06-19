package it.uniba.di.cdg.skype.x86sdk.util;
import javax.swing.*;
import java.io.*;

import javax.sound.sampled.*;
public class SkypeSound extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final int	EXTERNAL_BUFFER_SIZE = 128000;
	private static File soundFile;
	private static AudioInputStream audioInputStream = null;
	private static AudioFormat	audioFormat;
	private static SourceDataLine	line = null;
	private static DataLine.Info info;

	public SkypeSound() {

	}

	public  SourceDataLine play(String type) {

		if (type.equals("in_call"))
			soundFile = new File(System.getProperty("user.dir")+"\\SkypeRuntime\\suoni\\ring.wav");
		if (type.equals("out_call"))
			soundFile = new File(System.getProperty("user.dir")+"\\SkypeRuntime\\suoni\\libero.wav");

		try
		{
			audioInputStream = AudioSystem.getAudioInputStream(soundFile);
		}
		catch (Exception e)
		{

			e.printStackTrace();
			System.exit(1);
		}

		audioFormat = audioInputStream.getFormat();
		info = new DataLine.Info(SourceDataLine.class,
				audioFormat);

		try

		{
			line = (SourceDataLine) AudioSystem.getLine(info);

			/*
					  The line is there, but it is not yet ready to
					  receive audio data. We have to open the line.
			 */
			line.open(audioFormat);
		}
		catch (LineUnavailableException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}


		line.start();


		int	nBytesRead = 0;
		byte[]	abData = new byte[EXTERNAL_BUFFER_SIZE];
		while (nBytesRead != -1)
		{
			try
			{
				nBytesRead = audioInputStream.read(abData, 0, abData.length);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (nBytesRead >= 0)
			{
				line.write(abData, 0, nBytesRead);
			}
		}

		return line;

	}

	public void stop(){

		line.close();
		line.flush();


	}






}





