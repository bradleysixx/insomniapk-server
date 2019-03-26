package org.scapesoft.game.player.content.wild;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.TimeUnit;

import org.Server;
import org.scapesoft.Constants;
import org.scapesoft.engine.CoresManager;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.FileClassLoader;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 31, 2014
 */
public class WildernessActivityManager implements Runnable {

	/**
	 * When called, this method will register all wilderness activities into the
	 * server
	 */
	public void load() {
		FileClassLoader.getClassesInDirectory(WildernessActivityManager.class.getPackage().getName() + ".activities").forEach((clazz) -> {
			WildernessActivity activity = (WildernessActivity) clazz;
			if (activity.getActivityTime() == -1) {
				return;
			}
			wildernessActivities.add(activity);
		});
		nextActivityTime = Constants.DEBUG ? System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(5) : System.currentTimeMillis() + (ACTIVITY_INBETWEEN_DELAY / 2);
		CoresManager.slowExecutor.scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		try {
			if (Server.STARTUP_TIME == -1 || World.getPlayers().size() == 0 || CoresManager.shutdown) {
				return;
			}
			if (System.currentTimeMillis() >= nextActivityTime) {
				/**
				 * If we have an activity currently happening, we will remove it
				 */
				if (currentActivity != null) {
					currentActivity.onFinish();
					currentActivity = null;
					nextActivityTime = System.currentTimeMillis() + ACTIVITY_INBETWEEN_DELAY;
					/** Sending the server announcement */
					World.sendWorldMessage(ACTIVITY_COMPLETE_MESSAGE, false, true);
					return;
				}
				WildernessActivity randomActivity = getRandomActivity();

				randomActivity.onCreate();
				randomActivity.setActivityInitializeTime(System.currentTimeMillis());

				nextActivityTime = System.currentTimeMillis() + ACTIVITY_INBETWEEN_DELAY;
				currentActivity = randomActivity;

				/** Sending the server announcement */
				World.sendWorldMessage("<col=" + ChatColors.RED + ">Wilderness Activity</col>: " + randomActivity.getServerAnnouncement(), false, true);
			} else if (currentActivity != null) {
				long activityTime = currentActivity.getActivityTime();
				currentActivity.process();
				if (activityTime == 0) {
					return;
				}
				long timeOver = currentActivity.getActivityInitializeTime() + activityTime;
				/**
				 * Each activity has a time period it can last for but it can't
				 * be the amount of time in {@link #ACTIVITY_INBETWEEN_DELAY}.
				 * If the current activity has been happening for this amount of
				 * time, it will finish and the next activity will happen in
				 * {@link #ACTIVITY_INBETWEEN_DELAY} ms
				 */
				if (System.currentTimeMillis() >= timeOver) {
					currentActivity.onFinish();
					currentActivity = null;
					nextActivityTime = System.currentTimeMillis() + ACTIVITY_INBETWEEN_DELAY;
					/** Sending the server announcement */
					World.sendWorldMessage(ACTIVITY_COMPLETE_MESSAGE, false, true);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * If the wilderness activity is the current activity
	 * 
	 * @param activity
	 *            The activity to check for
	 * @return
	 */
	public boolean isActivityCurrent(WildernessActivity activity) {
		if (activity == null) {
			throw new RuntimeException("Checking if activity is current activity but it was null!");
		}
		if (currentActivity == null) {
			return false;
		}
		return currentActivity.getClass().getSimpleName().equals(activity.getClass().getSimpleName());
	}

	/**
	 * If the wilderness activity is the current activity
	 * 
	 * @param activity
	 *            The activity to check for
	 * @return
	 */
	public boolean isActivityCurrent(Class<? extends WildernessActivity> clazz) {
		WildernessActivity activity = getWildernessActivity(clazz);
		if (activity == null) {
			throw new RuntimeException("Checking if activity is current activity but it was null!");
		}
		if (currentActivity == null) {
			return false;
		}
		return currentActivity.getClass().getSimpleName().equals(activity.getClass().getSimpleName());
	}

	/**
	 * Gets a wilderness activity by the class
	 * 
	 * @param clazz
	 *            The class
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends WildernessActivity> T getWildernessActivity(Class<? extends WildernessActivity> clazz) {
		ListIterator<WildernessActivity> it$ = wildernessActivities.listIterator();
		while (it$.hasNext()) {
			WildernessActivity activity = it$.next();
			if (activity.getClass().getSimpleName().equals(clazz.getSimpleName())) {
				return (T) activity;
			}
		}
		return null;
	}

	/**
	 * Gives the player their bonus points for engaging in the
	 * {@link #currentActivity} wilderness activity
	 * 
	 * @param player
	 */
	public void giveBonusPoints(Player player, Object... params) {
		if (currentActivity != null) {
			if (currentActivity.receivesBonus(player, params)) {
				currentActivity.giveBonusPoints(player);
			}
		}
	}

	/**
	 * Gets a random activity from the {@link #wildernessActivities} list
	 * 
	 * @return A {@code WildernessActivity} {@code Object}
	 */
	private WildernessActivity getRandomActivity() {
		if (activitiesPerformed.size() == wildernessActivities.size()) {
			activitiesPerformed.clear();
		}
		List<WildernessActivity> activities = new ArrayList<>(wildernessActivities);
		Collections.shuffle(activities);
		WildernessActivity random = activities.get(0);
		while (activitiesPerformed.contains(random)) {
			Collections.shuffle(activities);
			random = activities.get(0);
		}
		activitiesPerformed.add(random);
		return random;
	}

	/**
	 * If we have an activity currently running, a description of the activity
	 * is necessary. This method finds that description
	 * 
	 * @return
	 */
	public String getActivityDescription() {
		if (currentActivity == null) {
			return null;
		}
		try {
			return currentActivity.getDescription();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Getting the instance of this class
	 * 
	 * @return
	 */
	public static final WildernessActivityManager getSingleton() {
		return SINGLETON;
	}

	/**
	 * The current activity that is happening
	 */
	private WildernessActivity currentActivity;

	/**
	 * The next time an activity will be executed
	 */
	private long nextActivityTime;

	/**
	 * The delay that happens inbetween wilderness activities
	 */
	public static final long ACTIVITY_INBETWEEN_DELAY = Constants.DEBUG ? TimeUnit.SECONDS.toMillis(1440) : TimeUnit.MINUTES.toMillis(30);

	/**
	 * The message players will see when the wilderness activity is over
	 */
	public static final String ACTIVITY_COMPLETE_MESSAGE = "<col=" + ChatColors.RED + ">Wilderness Activity:</col> The current wilderness activity has ended! Please wait for the next one.";

	/**
	 * The list of wilderness activities in the server
	 */
	private List<WildernessActivity> wildernessActivities = new ArrayList<>();

	/**
	 * The list of wilderness activities performed
	 */
	private List<WildernessActivity> activitiesPerformed = new ArrayList<>();

	/**
	 * The instance of this class
	 */
	private static final WildernessActivityManager SINGLETON = new WildernessActivityManager();
}
