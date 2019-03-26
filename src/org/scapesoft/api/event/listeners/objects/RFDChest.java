package org.scapesoft.api.event.listeners.objects;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.ShopsLoader;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 31, 2014
 */
public class RFDChest extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 12309 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		return false;
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
		if (option == ClickOption.FIRST) {
			player.getBank().openBank();
			return true;
		} else if (option == ClickOption.THIRD) {
			((ShopsLoader) GsonHandler.getJsonLoader(ShopsLoader.class)).openShop(player, "Culinaromancer's Chest");
			return true;
		}
		return false;
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
