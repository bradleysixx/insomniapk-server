package org.scapesoft.api.event.listeners.objects;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.minigames.games.MainGameHandler;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 14, 2014
 */
public class NoviteGamesHandler extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 14315, 14314 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		return false;
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
		if (objectId == 14315) { // the tile to enter with
			MainGameHandler.get().enterLobby(player);
		} else if (objectId == 14314) {
			MainGameHandler.get().removeLobby(player);
		}
		return true;
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		return false;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		return false;
	}

}
