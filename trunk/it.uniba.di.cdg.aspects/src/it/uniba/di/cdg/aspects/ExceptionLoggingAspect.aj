package it.uniba.di.cdg.aspects;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.aspectj.lang.Signature;

public aspect ExceptionLoggingAspect {

	private Logger logger;
	private ThreadLocal<Throwable> lastLoggedException = new ThreadLocal<Throwable>();
	private FileHandler handler;

	pointcut exceptionTraced()
		: execution(* *.*(..)) && !within( it.uniba.di.cdg.aspects..*);

	after() throwing(Throwable ex) : exceptionTraced() {
		this.logger = Logger.getLogger(thisJoinPointStaticPart.getClass()
				.getName());
		try {
			File f = new File("logs");
			if (!f.exists()) {
				f.mkdir();
			}
			handler = new FileHandler("logs/"
					+ thisJoinPoint.getThis().getClass().getPackage().getName()
					+ ".exceptionTrace.log", true);

			handler.setFormatter(new SimpleFormatter());
			this.logger.addHandler(handler);
		} catch (IOException e) {
			System.out.println("Cannot write log file");
		}

		if (lastLoggedException.get() != ex) {
			lastLoggedException.set(ex);
			Signature sig = thisJoinPointStaticPart.getSignature();
			logger.log(Level.INFO,
					"Exception trace aspect [" + sig.toShortString() + "]", ex);
		}

	}
}
