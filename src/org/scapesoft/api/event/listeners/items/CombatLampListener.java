package org.scapesoft.api.event.listeners.items;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.impl.CombatLampItem;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 22, 2014
 */
public class CombatLampListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 15390 };
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
		return false;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		if (option == ClickOption.FIRST) {
			player.getDialogueManager().startDialogue(CombatLampItem.class, "");
			return true;
		}
		return false;
	}
}
