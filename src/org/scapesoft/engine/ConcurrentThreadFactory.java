package org.scapesoft.engine;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentThreadFactory implements ThreadFactory {
	
	/**
	 * The thread group name
	 */
	private final String name;

	/**
	 * The <code>AtomicInteger</code> instance containing thread count
	 */
	private final AtomicInteger threadCount = new AtomicInteger();
	
	/**
	 * The constructor 
	 * @param name The name of the <code>ThreadGroup</code>.
	 */
	public ConcurrentThreadFactory(String name) {
		this.name = name;
	}
	
	/**
	 * Creates a new <code>Thread</code> with the name and thread count
	 * of the <code>ThreadGroup</code>.
	 */
	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, new StringBuilder(name).append("-").append(threadCount.getAndIncrement()).toString());
		return thread;
	}
	
}
