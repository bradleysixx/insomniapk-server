package org.scapesoft.engine.process;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import org.scapesoft.utilities.misc.FileClassLoader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 20, 2014
 */
public class ProcessManagement {

	public ProcessManagement() {
		loadedClasses = FileClassLoader.getClassesInDirectory(ProcessManagement.class.getPackage().getName() + ".impl");
	}

	/**
	 * Registers all events to the {@link #service} thread pool
	 */
	public void registerEvents() {
		loadedClasses.forEach((c) -> startTimedProcess((TimedProcess) c));
	}

	/**
	 * Starts a new game event on the executor with the correct delay
	 *
	 * @param event
	 */
	private void startTimedProcess(final TimedProcess process) {
		service.scheduleAtFixedRate(new Runnable() {
			@Override
			public void run() {
				try {
					process.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, process.getTimer().getDelay(), process.getTimer().getTimeUnit());
	}

	/**
	 * @return the instance
	 */
	public static ProcessManagement get() {
		return INSTANCE;
	}

	/**
	 * The executor service
	 */
	private final ScheduledExecutorService service = Executors.newScheduledThreadPool(4);

	/**
	 * The instance of this class
	 */
	private static final ProcessManagement INSTANCE = new ProcessManagement();

	/**
	 * The list of loaded classes that will be processed as timed processes
	 */
	private final List<Object> loadedClasses;

}