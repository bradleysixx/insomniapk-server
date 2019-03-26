package org.scapesoft.utilities.console.logging;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Dec 14, 2013
 */
public class ServerLogger extends PrintStream {

	public ServerLogger(OutputStream out) {
		super(out);
	}

	@Override
	public void print(boolean message) {
		Throwable throwable = new Throwable();
		String name = throwable.getStackTrace()[2].getFileName().replaceAll(".java", "");
		String line = String.valueOf(throwable.getStackTrace()[2].getLineNumber());
		log(name + ":" + line, "" + message);
	}

	@Override
	public void print(int message) {
		Throwable throwable = new Throwable();
		String name = throwable.getStackTrace()[2].getFileName().replaceAll(".java", "");
		String line = String.valueOf(throwable.getStackTrace()[2].getLineNumber());
		log(name + ":" + line, "" + message);
	}

	@Override
	public void print(String message) {
		Throwable throwable = new Throwable();
		String name = throwable.getStackTrace()[2].getFileName().replaceAll(".java", "");
		String line = String.valueOf(throwable.getStackTrace()[2].getLineNumber());
		log(name + ":" + line, "" + message);
	}

	private void log(String className, String text) {
		super.print("[" + className + "][" + getDate() + "]" + text);
	}

	@SuppressWarnings("deprecation")
	private String getDate() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeZone(TimeZone.getTimeZone("America/Toronto"));
		Date date = cal.getTime();
		String formatted = new String(date.getMonth() + "/" + date.getDay() + "/" + cal.get(Calendar.YEAR)); // date
		int hours = date.getHours();
		formatted += ", " + (hours > 12 ? hours - 12 : hours) + ":" + date.getMinutes() + ":" + date.getSeconds() + " " + (hours > 12 ? "PM" : "AM"); // time
		return formatted;
	}

}
