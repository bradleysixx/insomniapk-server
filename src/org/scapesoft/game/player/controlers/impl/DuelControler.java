package org.scapesoft.game.player.controlers.impl;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.DuelRules;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.RouteEvent;
import org.scapesoft.game.player.controlers.Controller;

public class DuelControler extends Controller {

	@Override
	public void start() {
		sendInterfaces();
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendPlayerOption("Challenge", 1, false);
		moved();
	}
	
	@Override
	public boolean login() {
		start();
		return false;
	}

	@Override
	public boolean logout() {
		return false;
	}

	@Override
	public void forceClose() {
		remove();
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		return true;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		return true;
	}

	@Override
	public void magicTeleported(int type) {
		removeController();
		remove();
	}

	@Override
	public void moved() {
		if (!isAtDuelArena(player)) {
			removeController();
			remove();
		}
	}

	@Override
	public boolean canPlayerOption1(final Player target) {
		player.stopAll();
		player.setRouteEvent(new RouteEvent(target, new Runnable() {

			@Override
			public void run() {
				if (target.getInterfaceManager().containsScreenInterface() || target.isLocked()) {
					player.getPackets().sendGameMessage("The other player is busy.");
					return;
				}
				if (target.getTemporaryAttributtes().get("DuelChallenged") == player) {
					player.getControllerManager().removeControllerWithoutCheck();
					target.getControllerManager().removeControllerWithoutCheck();
					target.getTemporaryAttributtes().remove("DuelChallenged");
					player.setLastDuelRules(new DuelRules(player, target));
					target.setLastDuelRules(new DuelRules(target, player));
					player.getControllerManager().startController("DuelArena", target, target.getTemporaryAttributtes().get("DuelFriendly"));
					target.getControllerManager().startController("DuelArena", player, target.getTemporaryAttributtes().remove("DuelFriendly"));
					return;
				}
				player.getTemporaryAttributtes().put("DuelTarget", target);
				player.getInterfaceManager().sendInterface(640);
				player.getTemporaryAttributtes().put("WillDuelFriendly", true);
				player.getPackets().sendConfig(283, 67108864);
			}
		}));
		return false;
	}

	public static void challenge(Player player) {
		player.closeInterfaces();
		Boolean friendly = (Boolean) player.getTemporaryAttributtes().remove("WillDuelFriendly");
		if (friendly == null) {
			return;
		}
		Player target = (Player) player.getTemporaryAttributtes().remove("DuelTarget");
		if (target == null || target.hasFinished() || !target.withinDistance(player, 14) || !(target.getControllerManager().getController() instanceof DuelControler)) {
			player.getPackets().sendGameMessage("Unable to find " + (target == null ? "your target" : target.getDisplayName()));
			return;
		}
		player.getTemporaryAttributtes().put("DuelChallenged", target);
		player.getTemporaryAttributtes().put("DuelFriendly", friendly);
		player.getPackets().sendGameMessage("Sending " + target.getDisplayName() + " a request...");
		target.getPackets().sendDuelChallengeRequestMessage(player, friendly);
	}

	public void remove() {
		player.getInterfaceManager().removeOverlay();
		player.getAppearence().generateAppearenceData();
		player.getPackets().sendPlayerOption("null", 1, false);
	}

	@Override
	public void sendInterfaces() {
		if (isAtDuelArena(player)) {
			player.getInterfaceManager().setOverlay(638);
		}
	}

	@Override
	public boolean canDropItem(Item item) {
		player.sendMessage("You are not allowed to drop items in the duel arena.");
		return false;
	}

	public static boolean isAtDuelArena(Player player) {
		return player.inArea(3341, 3265, 3387, 3281);
	}
}