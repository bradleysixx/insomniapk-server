package org.scapesoft.game.minigames.games;

import org.scapesoft.game.Animation;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.ArmourSets;
import org.scapesoft.game.player.controlers.Controller;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.game.player.TeleportLocations;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 14, 2014
 */
public class GamesHandler extends Controller {

	@Override
	public void start() {
		for (Item item : player.getInventory().getItems().toArray()) {
			if (item == null)
				continue;
			player.getInventory().deleteItem(item);
		}
		for (Item item : player.getEquipment().getItems().toArray()) {
			if (item == null)
				continue;
			player.getEquipment().deleteItem(item.getId(), item.getAmount());
		}
		if (player.getFamiliar() != null) {
			player.getFamiliar().finish();
		}
		player.setNextWorldTile(MainGameHandler.NOVITE_GAMES_AREA);
		MainGameHandler.get().getGamePlayers().add(player);
		giveStartup();
	}

	@Override
	public boolean canTrade(Player player) {
		return false;
	}

	private void giveStartup() {
		player.getInventory().addItem(ArmourSets.Sets.BRONZE_LG.getId(), 1);
		player.getInventory().addItem(ArmourSets.Sets.GREEN_DHIDE.getId(), 1);
		player.getInventory().addItem(ArmourSets.Sets.LIGHT_MYSTIC_SET.getId(), 1);
	}
	
	@Override
	public boolean canSummonFamiliar() {
		player.sendMessage("You cannot use familiars in here.");
		return false;
	}

	@Override
	public void process() {
		sendInformationInterface();
		/*
		 * if (MainGameHandler.get().getPhase() == Phases.GAME_RUNNING &&
		 * player.getActionManager().getAction() != null) { Action action =
		 * player.getActionManager().getAction(); if (!(action instanceof
		 * PlayerCombat)) { player.getActionManager().forceStop();
		 * player.setNextAnimation(new Animation(-1));
		 * player.sendMessage("Kill the monsters incoming!"); } }
		 */
	}

	@Override
	public boolean processObjectClick1(WorldObject object) {
		if (object.getId() == 55357) {
			MainGameHandler.get().displayChest(player);
			return false;
		}
		return true;
	}

	@Override
	public boolean sendDeath() {
		player.lock(7);
		player.stopAll();
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					player.setNextAnimation(new Animation(836));
				} else if (loop == 1) {
					player.getPackets().sendGameMessage("Oh dear, you have died.");
				} else if (loop == 4) {
					player.reset();
					if (player.getControllerManager().getController() instanceof GamesHandler) {
						player.setNextWorldTile(MainGameHandler.NOVITE_GAMES_AREA);
					}
					player.setNextAnimation(new Animation(-1));
					this.stop();
				}
				loop++;
			}
		}, 0, 1);
		return false;
	}

	@Override
	public boolean logout() {
		leaveGame(true);
		return true;
	}
	
	@Override
	public boolean canDropItem(Item item) {
		player.getInventory().deleteItem(item);
		player.sendMessage("<col=" + ChatColors.BLUE + ">The item disappears to dust inside the novite games arena...");
		return false;
	}

	/**
	 * Handles the player leaving the game
	 *
	 * @param logout
	 *            Whether they left by logout or by choice
	 */
	public void leaveGame(boolean logout) {
		if (logout) {
			//player.setLocation(TeleportLocations.NOVITE_GAMES);
			MainGameHandler.get().getGamePlayers().remove(getPlayer());
		} else {
			//player.setNextWorldTile(TeleportLocations.NOVITE_GAMES);
			player.getPackets().closeInterface(player.getInterfaceManager().hasResizableScreen() ? 10 : 8);
		}
		player.reset();
		for (Item item : player.getInventory().getItems().toArray()) {
			if (item == null)
				continue;
			player.getInventory().deleteItem(item);
		}
		for (Item item : player.getEquipment().getItems().toArray()) {
			if (item == null)
				continue;
			player.getEquipment().deleteItem(item.getId(), item.getAmount());
		}
		player.getActionManager().forceStop();
		player.getAppearence().generateAppearenceData();
		forceClose();
		removeController();
	}

	/**
	 * Sends the information interface to the player
	 */
	public void sendInformationInterface() {
		int interfaceId = 532;
		int resizableId = 10;
		int normalId = 8;
		boolean shouldAdd = !player.getInterfaceManager().containsInterface(interfaceId);
		if (shouldAdd) {
			player.getInterfaceManager().sendTab(player.getInterfaceManager().hasResizableScreen() ? resizableId : normalId, interfaceId);
		}
		StringBuilder bldr = new StringBuilder();
		switch (MainGameHandler.get().getPhase()) {
		case COLLECTING_MATERIALS:
			bldr.append("Collect Time Left:" + MainGameHandler.get().getCollectTimeLeft() + "<br>");
			bldr.append("Skill Points:" + Utils.format((long) skillPoints) + "<br>");
			break;
		case GAME_RUNNING:
			bldr.append("Game Time Left:" + MainGameHandler.get().getGameTimeLeft() + "<br>");
			bldr.append("Skill Points:" + Utils.format((long) skillPoints) + "<br>");
			bldr.append("Damage Dealt:" + Utils.format(damageDealt) + "<br>");
			bldr.append("Monsters Left:" + Utils.format(MainGameHandler.get().getMonstersToHunt().size()) + "<br>");
			break;
		default:
			break;
		}
		bldr.append("Players In Game:" + MainGameHandler.get().getGamePlayers().size() + "<br>");
		player.getPackets().sendIComponentText(interfaceId, 0, "");
		player.getPackets().sendIComponentText(interfaceId, 1, bldr.toString());
	}

	@Override
	public boolean processMagicTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "You cant leave like this...");
		return false;
	}

	@Override
	public boolean processItemTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "You cant leave like this...");
		return false;
	}

	@Override
	public boolean processObjectTeleport(WorldTile toTile) {
		player.getDialogueManager().startDialogue("SimpleMessage", "You cant leave like this...");
		return false;
	}

	/**
	 * Adds the parameterized skill points to the skill points total
	 * 
	 * @param amount
	 *            The amount of skill points
	 */
	public void addSkillPoints(double amount) {
		skillPoints += amount;
	}

	public double getSkillPoints() {
		return skillPoints;
	}

	public int getDamageDealt() {
		return damageDealt;
	}

	public void setDamageDealt(int damageDealt) {
		this.damageDealt = damageDealt;
	}

	private double skillPoints = 0;
	private int damageDealt = 0;

}
