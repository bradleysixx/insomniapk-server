package org.scapesoft.game.minigames;

import static org.scapesoft.game.minigames.GoblinWars.Faction.SARADOMIN;
import static org.scapesoft.game.minigames.GoblinWars.Faction.ZAMORAK;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Equipment;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.SimpleMessage;

public final class GoblinWars {

	private static final int PLAYERS_PER_FACTION = 3;
	public static final WorldTile LOBBY = new WorldTile(2443, 3088, 0);

	private static final Map<Faction, List<Player>> factions = new HashMap<>(
			Faction.values().length);

	static {
		factions.put(SARADOMIN, new ArrayList<Player>(PLAYERS_PER_FACTION));
		factions.put(ZAMORAK, new ArrayList<Player>(PLAYERS_PER_FACTION));
	}

	public static Faction factionOf(Player player) {
		for (Faction faction : Faction.values())
			if (factions.get(faction).contains(player))
				return faction;
		return null;
	}

	public static boolean handleObjects(Player player, int id) {
		switch (id) {
		case 4387: // Saradomin portal (enter)
			enterPortal(player, SARADOMIN);
			return true;
		case 4388: // Zamorak portal (enter)
			enterPortal(player, ZAMORAK);
			return true;
		case 4408: // Guthix portal (enter)
			enterPortal(player, weakestFaction());
			return true;
		case 4389: // Saradomin portal (exit)
			exitPortal(player, SARADOMIN);
			return true;
		case 4390: // Zamorak portal (exit)
			exitPortal(player, ZAMORAK);
			return true;
		}
		return false;
	}

	public static void enterPortal(Player player, Faction faction) {
		if (player.getEquipment().wearingArmour()) {
			player.getDialogueManager().startDialogue(SimpleMessage.class,
					"You cannot equip anything when you are joining the game.");
			return;
		}
		if (player.getInventory().getFreeSlots() != 28) {
			player.getDialogueManager().startDialogue(SimpleMessage.class,
					"You cannot bring anything in your inventory.");
			return;
		}
		if (player.getFamiliar() != null) {
			player.getDialogueManager().startDialogue(SimpleMessage.class,
					"You cannot have a familiar with you.");
			return;
		}
		switch (faction) {
		case SARADOMIN:
		case ZAMORAK:
			player.lock(1);
			player.stopAll();
			if (!factions.get(faction).add(player)) {
				player.sendMessage("The lobby is already full!");
				return;
			}
			player.getControllerManager().startController("GoblinWarsLobby");
			player.setNextWorldTile(faction.getLobby());
			player.getEquipment().setEquipment(Equipment.SLOT_HAT,
					faction.getHood());
			player.getEquipment().setEquipment(Equipment.SLOT_CAPE,
					faction.getCloak());
			player.sendMessage("You have joined the lobby.");
			break;
		}
	}

	public static void exitPortal(Player player, Faction faction) {
		boolean left = false;
		List<Player> players = factions.get(faction);
		if (players != null)
			left = players.remove(player);
		player.lock(1);
		player.stopAll();
		if (left) {
			player.getEquipment().reset();
			player.getInventory().reset();
			player.getInterfaceManager().removeOverlay();
			player.getControllerManager().getController().removeController();
		}
		player.setNextWorldTile(LOBBY);
		player.sendMessage("You have left the lobby.");
	}

	public static void exit(Player player) {
		exitPortal(player, factionOf(player));
	}

	public static Faction weakestFaction() {
		int saradomin = factions.get(SARADOMIN).size();
		int zamorak = factions.get(ZAMORAK).size();
		return saradomin <= zamorak ? SARADOMIN : ZAMORAK;
	}

	public enum Faction {

		SARADOMIN(new Item(4513), new Item(4041), new WorldTile(2381, 9489, 0)), ZAMORAK(
				new Item(4515), new Item(4042), new WorldTile(2421, 9523, 0));

		private final Item hood, cloak;
		private final WorldTile lobby;

		Faction(Item hood, Item cloak, WorldTile lobby) {
			this.hood = hood;
			this.cloak = cloak;
			this.lobby = lobby;
		}

		public Item getHood() {
			return hood;
		}

		public Item getCloak() {
			return cloak;
		}

		public WorldTile getLobby() {
			return lobby;
		}

	}

	public static boolean checkTeamItems(Player player, int slotId) {
		Item item = player.getEquipment().getItem(slotId);
		for (Faction faction : Faction.values()) {
			String garmet = faction.getCloak().equals(item) ? "cloak" : faction
					.getHood().equals(item) ? "hood" : null;
			if (garmet != null) {
				player.sendMessage("You cannot unequip your team's " + garmet
						+ ".");
				return true;
			}
		}
		return false;
	}

}