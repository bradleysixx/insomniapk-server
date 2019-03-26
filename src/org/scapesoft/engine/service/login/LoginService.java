package org.scapesoft.engine.service.login;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.scapesoft.engine.ConcurrentThreadFactory;

/**
 * This class handles the submission of all login requests to the game world.
 * They are placed in a queue and then executed in order of priority.
 * 
 * @author Harry Andreas
 * @author Tyluur<itstyluur@gmail.com>
 */
public class LoginService implements Runnable {

	/**
	 * The service thread
	 */
	private final static ExecutorService serviceThread = Executors.newSingleThreadExecutor(new ConcurrentThreadFactory("LoginService"));

	/**
	 * Login service instance
	 */
	private static LoginService loginService;

	/**
	 * A login task BlockingQueue
	 */
	private BlockingQueue<LoginServiceTask> loginQueue;

	/**
	 * A list of requests for the login server
	 */
	private final Map<String, LoginServiceTask> requests = new HashMap<String, LoginServiceTask>();

	/**
	 * Is the service running?
	 */
	private boolean serviceRunning = Boolean.FALSE;

	/**
	 * Construct the service
	 */
	private LoginService() {

	}

	/**
	 * Gets the singleton instance
	 * 
	 * @return The login service
	 */
	public static LoginService getSingleton() {
		if (loginService == null) {
			loginService = new LoginService();
		}
		return loginService;
	}

	/**
	 * Starts the services
	 */
	public void init() {
		loginQueue = new LinkedBlockingQueue<LoginServiceTask>();
		serviceRunning = Boolean.TRUE;
		getServiceThread().submit(this);
	}

	/**
	 * Offers a login task to be processed
	 * 
	 * @param task
	 */
	public void submit(LoginServiceTask task) {
		getLoginQueue().offer(task);
	}

	/**
	 * Runs the service
	 */
	@Override
	public void run() {
		System.out.println("Login service is now running!");
		while (serviceRunning) {
			try {
				LoginServiceTask task = getLoginQueue().take();
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.err.println("The login service has shutdown. All login attempts will be ignored.");
	}

	/**
	 * Gets the service thread
	 * 
	 * @return the servicethread
	 */
	private static ExecutorService getServiceThread() {
		return serviceThread;
	}

	/**
	 * Gets the login queue
	 * 
	 * @return the loginQueue
	 */
	private BlockingQueue<LoginServiceTask> getLoginQueue() {
		return loginQueue;
	}

	public Map<String, LoginServiceTask> getRequests() {
		return requests;
	}

}