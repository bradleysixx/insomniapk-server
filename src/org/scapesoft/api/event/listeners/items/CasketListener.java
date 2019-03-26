package org.scapesoft.api.event.listeners.items;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 29, 2014
 */
public class CasketListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 19039, 13047, 13077, 10223 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		player.getClueScrollManager().giveRewards(item);
		return true;
	}

}
