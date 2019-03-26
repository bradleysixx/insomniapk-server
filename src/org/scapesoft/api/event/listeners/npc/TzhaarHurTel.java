package org.scapesoft.api.event.listeners.npc;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.ShopsLoader;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Aug 1, 2014
 */
public class TzhaarHurTel extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 2620 } ;
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		return false;
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
		return false;
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		((ShopsLoader) GsonHandler.getJsonLoader(ShopsLoader.class)).openShop(player, "Tokkul Store");
		return true;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		return false;
	}

}
