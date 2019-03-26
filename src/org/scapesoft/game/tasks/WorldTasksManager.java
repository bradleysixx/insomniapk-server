package org.scapesoft.game.tasks;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import org.scapesoft.utilities.misc.Stopwatch;

public final class WorldTasksManager {

	private static final List<WorldTaskInformation> tasks = Collections.synchronizedList(new LinkedList<WorldTaskInformation>());

	public static void processTasks(StringBuilder b) {
		Stream.of(tasks.toArray(new WorldTaskInformation[tasks.size()])).forEach((t) -> {
			Stopwatch s = Stopwatch.start();
			if (t.continueCount > 0) {
				t.continueCount--;
				return;
			}
			t.task.run();
			if (t.task.needRemove)
				tasks.remove(t);
			else
				t.continueCount = t.continueMaxCount;
			if (s.elapsed() > 100) {
				b.append("World task took: " + s.elapsed() + "ms " + t.task.getClass().getSimpleName());
			}
		});
	}
	public static void schedule(WorldTask task, int delayCount, int periodCount) {
		if (task == null || delayCount < 0 || periodCount < 0)
			return;
		tasks.add(new WorldTaskInformation(task, delayCount, periodCount));
	}

	public static void schedule(WorldTask task, int delayCount) {
		if (task == null || delayCount < 0)
			return;
		tasks.add(new WorldTaskInformation(task, delayCount, -1));
	}

	public static void schedule(WorldTask task) {
		if (task == null)
			return;
		tasks.add(new WorldTaskInformation(task, 0, -1));
	}

	public static int getTasksCount() {
		return tasks.size();
	}

	private WorldTasksManager() {
	}

	private static final class WorldTaskInformation {

		private WorldTask task;
		private int continueMaxCount;
		private int continueCount;

		public WorldTaskInformation(WorldTask task, int continueCount, int continueMaxCount) {
			this.task = task;
			this.continueCount = continueCount;
			this.continueMaxCount = continueMaxCount;
			if (continueMaxCount == -1) {
				task.needRemove = true;
			}
		}

	}

}
