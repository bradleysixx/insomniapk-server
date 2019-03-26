package org.scapesoft.utilities.console.logging;


/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Feb 22, 2014
 */
public class LoggerSetup {

	/**
	 * Registers the customized loggers for the server
	 */
	public static void registerServerLoggers() {
		System.setErr(new ErrorLogger(System.err));
		System.setOut(new ServerLogger(System.out));
	}

}