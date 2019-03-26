package org.scapesoft.utilities.misc;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.lang.System.currentTimeMillis;

import java.util.concurrent.TimeUnit;

public final class Stopwatch {

	private final long time;

	private Stopwatch(long time) {
		this.time = time;
	}

	public long elapsed() {
		return currentTimeMillis() - time;
	}

	public long elapsed(TimeUnit desiredUnit) {
		return desiredUnit.convert(elapsed(), MILLISECONDS);
	}

	public void printElapsed(String format) {
		System.err.printf(format, elapsed());
	}

	public void printElapsed() {
		printElapsed("Time elapsed: %sms\n");
	}

	public static Stopwatch start() {
		return new Stopwatch(currentTimeMillis());
	}

}