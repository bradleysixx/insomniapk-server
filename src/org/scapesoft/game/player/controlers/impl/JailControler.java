package org.scapesoft.game.player.controlers.impl;

import org.scapesoft.Constants;
import org.scapesoft.game.Animation;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.controlers.Controller;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;

public class JailControler extends Controller {

	@Override
	public void start() {
		if (player.getJailed() > System.currentTimeMillis()) {
			player.sendRandomJail(player);
		}
	}

	@Override
	public void process() {
		if (player.getJailed() <= System.currentTimeMillis()) {
			player.getControllerManager().getController().removeController();
			player.getPackets().sendGameMessage("Your account has been unjailed.", true);
			player.setNextWorldTile(Constants.DEATH_TILE);
		}
	}

	public static void stopControler(Player p) {
		p.getControllerManager().getController().removeController();
	}

	@Override
	public boolean sendDeath() {
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				player.stopAll();
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 3) {
					player.setNextAnimation(new Animation(-1));
					player.reset();
					player.setCanPvp(false);
					player.sendRandomJail(player);
					player.unlock();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean login() {

		return false;
	}

	@Override
	public boolean logout() {

		return false;
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getPackets().sendGameMessage("You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getPackets().sendGameMessage("You are currently jailed for your delinquent acts.");
		return false;
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		player.getPackets().sendGameMessage("You cannot do any activities while being jailed.");
		return false;
	}

}
