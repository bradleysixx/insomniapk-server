package org.scapesoft.game.player.content.randoms.impl;

import java.util.concurrent.TimeUnit;

import org.scapesoft.game.Animation;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.others.Follower;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.randoms.RandomEvent;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 26, 2014
 */
public class Max_the_Traveller extends RandomEvent {

	@Override
	public String getName() {
		return "Max the Traveller";
	}

	@Override
	public void initiate(Player player) {
		max = new Follower(8009, player, player.getDisplayName());
		max.setNextForceTalk(new ForceTalk("Hey " + player.getDisplayName() + ", are you there?"));
		timeSpawned = System.currentTimeMillis();
		lastTimeSpoke = System.currentTimeMillis();
		terminated = false;
		started = true;
	}

	@Override
	public void process(Player player) {
		if (!started || terminated) {
			return;
		}
		if (!terminated && TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - timeSpawned) >= 60) {
			terminate(player, EndOptions.FAIL);
			return;
		}
		if (lastTimeSpoke == -1 || TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastTimeSpoke) >= 10) {
			player.sendMessage("<col=FF0000>Warning:</col> You are ignoring your random event!");
			max.setNextForceTalk(new ForceTalk("<col=FF0000>Warning:</col> You are ignoring your random event " + player.getDisplayName() + "!"));
			lastTimeSpoke = System.currentTimeMillis();
		}
	}

	/**
	 * Handles what to do when the random event was failed
	 * 
	 * @param player
	 *            The player who failed the random event
	 */
	private void terminate(Player player, EndOptions option) {
		if (!terminated) {
			switch (option) {
			case SUCCESS:
				player.getLoyaltyManager().addPoints(POINTS_TO_GAIN);
				player.sendMessage("You gain " + POINTS_TO_GAIN + " loyalty points!");
				break;
			case FAIL:
				player.setNextWorldTile(getRandomPosition());
				player.getLoyaltyManager().forcePoints(player.getLoyaltyManager().getPoints() - POINTS_TO_LOSE);
				player.sendMessage("You loose " + POINTS_TO_LOSE + " loyalty points for ignoring your random event.");
				break;
			}
		}
		WorldTasksManager.schedule(new WorldTask() {

			int ticks = 0;

			@Override
			public void run() {
				ticks++;
				if (ticks == 1) {
					max.setNextAnimation(new Animation(option == EndOptions.SUCCESS ? 863 : 9607));
					max.setNextForceTalk(new ForceTalk(option == EndOptions.SUCCESS ? "Sorry about that; I was just checking on you!" : "How dare you ignore me!"));
				} else if (ticks >= 3) {
					dispose(player);
					stop();
				}
			}

		}, 1, 1);
		terminated = true;
	}

	@Override
	public void dispose(Player player) {
		super.dispose(player);
		max.finish();
	}

	@Override
	public boolean handleNPCInteraction(Player player, NPC npc) {
		if (npc.equals(max)) {
			if (terminated) {
				return true;
			}
			terminate(player, EndOptions.SUCCESS);
			return true;
		}
		return false;
	}

	@Override
	public boolean handleInterfaceInteraction(Player player, int interfaceId, int buttonId) {
		return false;
	}

	private enum EndOptions {
		SUCCESS, FAIL
	}

	private Follower max;
	private long lastTimeSpoke;
	private long timeSpawned;
	private boolean terminated;
	private boolean started;

	/**
	 * The amount of points you lose when you ignore the random event
	 */
	private static final int POINTS_TO_LOSE = 200;

	/**
	 * The amount of points you gain when you interact with max
	 */
	private static final int POINTS_TO_GAIN = 50;
}
