package it.uniba.di.cdg.aspects;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public aspect ThreadSafetyAspect perthis(readOperations() || writeOperations()) {

	protected pointcut writeOperations() : execution( @SetSafety * *.*(..) );

	protected pointcut readOperations() : execution( @GetSafety * *.*(..) );

	private ReadWriteLock _lock = new ReentrantReadWriteLock();

	before() : readOperations(){
		_lock.readLock().lock();
	}

	after() : readOperations() {
		_lock.readLock().unlock();
	}

	before() : writeOperations() {
		_lock.writeLock().lock();
	}

	after() : writeOperations() {
		_lock.writeLock().unlock();
	}
}
