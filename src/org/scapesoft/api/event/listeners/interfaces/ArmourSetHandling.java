package org.scapesoft.api.event.listeners.interfaces;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.ArmourSetOpening;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.protocol.game.DefaultGameDecoder;

public class ArmourSetHandling extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 644, 645 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		if (interfaceId == 645) {
			if (buttonId == 16) {
				if (packetId == DefaultGameDecoder.ACTION_BUTTON1_PACKET)
					ArmourSetOpening.sendComponents(player, itemId);
				else if (packetId == DefaultGameDecoder.ACTION_BUTTON2_PACKET)
					ArmourSetOpening.exchangeSet(player, itemId);
				else if (packetId == DefaultGameDecoder.ACTION_BUTTON3_PACKET)
					ArmourSetOpening.examineSet(player, itemId);
			}
		} else if (interfaceId == 644) {
			if (buttonId == 0) {
				if (packetId == DefaultGameDecoder.ACTION_BUTTON1_PACKET)
					ArmourSetOpening.sendComponentsBySlot(player, slotId, itemId);
				else if (packetId == DefaultGameDecoder.ACTION_BUTTON2_PACKET)
					ArmourSetOpening.exchangeSet(player, slotId, itemId);
				else if (packetId == DefaultGameDecoder.ACTION_BUTTON3_PACKET)
					player.getInventory().sendExamine(slotId);
			}
		}
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
