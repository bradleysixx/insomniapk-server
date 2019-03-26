package org.scapesoft.utilities.misc;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Tyluur <itstyluur@gmail.com>
 */
public class ScapeSoftThreadFactory implements ThreadFactory {

    private final String name;
    private final int priority;

    private final AtomicInteger threadCount = new AtomicInteger();

    public ScapeSoftThreadFactory(String name) {
        this(name, Thread.NORM_PRIORITY);
    }

    public ScapeSoftThreadFactory(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r, new StringBuilder(name).append("-").append(threadCount.getAndIncrement()).toString());
        thread.setPriority(priority);
        return thread;
    }

}
