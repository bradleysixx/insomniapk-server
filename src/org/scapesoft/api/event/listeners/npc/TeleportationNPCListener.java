package org.scapesoft.api.event.listeners.npc;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.api.event.listeners.interfaces.TeleportationInterfaceListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 21, 2014
 */
public class TeleportationNPCListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 872 };
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		TeleportationInterfaceListener.display(player);
		return true;
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
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		// TODO Auto-generated method stub
		return false;
	}

}
