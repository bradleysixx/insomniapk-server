package org.scapesoft.game.minigames.games;

import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.minigames.games.MainGameHandler.Phases;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.controlers.Controller;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 14, 2014
 */
public class GamesLobby extends Controller {

	@Override
	public void start() {
		player.setNextWorldTile(new WorldTile(2660, 2639, 0));
		if (!MainGameHandler.get().getLobbyPlayers().contains(player))
			MainGameHandler.get().getLobbyPlayers().add(player);
	}

	@Override
	public void process() {
		sendTab();
	}

	@Override
	public boolean logout() {
		leaveLobby(true);
		return true;
	}

	@Override
	public boolean canTrade(Player player) {
		return false;
	}
	
	@Override
	public boolean processObjectClick1(WorldObject object) {
		return !object.getDefinitions().name.toLowerCase().contains("bank");
	}
	
	@Override
	public boolean processObjectClick2(WorldObject object) {
		return processObjectClick1(object);
	}
	
	@Override
	public boolean processObjectClick3(WorldObject object) {
		return processObjectClick1(object);
	}
	
	@Override
	public boolean processNPCClick1(NPC npc) {
		return !npc.getName().toLowerCase().contains("bank");
	}
	
	@Override
	public boolean processNPCClick2(NPC npc) {
		return processNPCClick1(npc);
	}
	
	@Override
	public boolean processNPCClick3(NPC npc) {
		return processNPCClick1(npc);
	}

	/**
	 * Sends the tab interface and clears the text on it
	 */
	public void sendTab() {
		int interfaceId = 407;
		int resizableId = 10;
		int normalId = 8;
		boolean shouldAdd = !player.getInterfaceManager().containsInterface(interfaceId);
		boolean gameRunning = MainGameHandler.get().getPhase() != null && !MainGameHandler.get().getPhase().equals(Phases.GAME_NOT_STARTED);
		player.getPackets().sendIComponentText(interfaceId, 3, "Novite Games");
		if (gameRunning) {
			player.getPackets().sendIComponentText(interfaceId, 13, "Next Departure: " + MainGameHandler.get().getSecondsTillStart());
			player.getPackets().sendIComponentText(interfaceId, 14, "A game is still running!");
			player.getPackets().sendIComponentText(interfaceId, 16, "Points: " + Utils.format(player.getFacade().getNoviteGamePoints()));
		} else {
			player.getPackets().sendIComponentText(interfaceId, 13, "Next Departure: " + MainGameHandler.get().getSecondsTillStart());
			player.getPackets().sendIComponentText(interfaceId, 14, "Players Ready: " + MainGameHandler.get().getLobbyPlayers().size());
			player.getPackets().sendIComponentText(interfaceId, 15, "(Need 3 to 25 Players)");
			player.getPackets().sendIComponentText(interfaceId, 16, "Points: " + Utils.format(player.getFacade().getNoviteGamePoints()));
			if (shouldAdd) {
				player.getInterfaceManager().sendTab(player.getInterfaceManager().hasResizableScreen() ? resizableId : normalId, interfaceId);
			}
		}
	}

	/**
	 * Leaves the lobby
	 * 
	 * @param logout
	 */
	public void leaveLobby(boolean logout) {
		MainGameHandler.get().getLobbyPlayers().remove(player);
		if (logout) {
			player.setLocation(new WorldTile(2657, 2639, 0));
		} else {
			player.reset();
			player.getPackets().closeInterface(player.getInterfaceManager().hasResizableScreen() ? 10 : 8);
			player.setNextWorldTile(new WorldTile(2657, 2639, 0));
		}
		forceClose();
		removeController();
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

}
