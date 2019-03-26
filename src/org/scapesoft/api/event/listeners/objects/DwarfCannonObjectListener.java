package org.scapesoft.api.event.listeners.objects;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.OwnedObjectManager;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.cannon.CannonAlgorithms;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 22, 2014
 */
public class DwarfCannonObjectListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 6 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		return false;
	}

	@Override
	public boolean handleObjectClick(Player player, int objectId, WorldObject worldObject, WorldTile tile, ClickOption option) {
		if (option == ClickOption.FIRST) {
			CannonAlgorithms.toggleFiring(player, worldObject);
		} else if (option == ClickOption.SECOND) {
			Player owner = OwnedObjectManager.getOwner(worldObject);
			if (owner != null && owner.equals(player) && player.getDwarfCannon() != null) {
				player.getDwarfCannon().finish(false);
			} else {
				player.getDialogueManager().startDialogue("SimpleMessage", "That is not your cannon!");
			}
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
