package org.scapesoft.api.event.listeners.interfaces;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.crafting.Jewellery;
import org.scapesoft.game.player.actions.crafting.Jewellery.GemCreation;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 3, 2014
 */
public class JewelleryMakingListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 675 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		player.getActionManager().setAction(new Jewellery(GemCreation.getGem(buttonId), buttonId, 1));
		return true;
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
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
