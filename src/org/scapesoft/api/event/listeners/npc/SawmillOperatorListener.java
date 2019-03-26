package org.scapesoft.api.event.listeners.npc;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.api.event.listeners.interfaces.PlankCreationListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

/**
 * @author Tyluur<itstyluur@gmail.com
 * @since Oct 26, 2014
 */
public class SawmillOperatorListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 4250 };
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
		switch(option) {
		case SECOND:
			PlankCreationListener.display(player);
			break;
		default:
			break;
		}
		return true;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		return false;
	}

}
