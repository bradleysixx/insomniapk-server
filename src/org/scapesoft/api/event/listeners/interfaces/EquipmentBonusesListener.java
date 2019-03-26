package org.scapesoft.api.event.listeners.interfaces;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.minigames.GoblinWars;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.networking.codec.handlers.ButtonHandler;
import org.scapesoft.utilities.game.item.ItemExamines;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 27, 2014
 */
public class EquipmentBonusesListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 667 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		if (buttonId == 7) {
			if (slotId >= 14) {
				return false;
			}
			Item item = player.getEquipment().getItem(slotId);
			if (item == null) {
				return false;
			}
			if (packetId == 25) {
				player.getPackets().sendGameMessage(ItemExamines.getExamine(item));
			} else if (packetId == 61) {
				if (GoblinWars.checkTeamItems(player, slotId))
					return true;
				ButtonHandler.sendRemove(player, slotId);
				ButtonHandler.refreshEquipBonuses(player);
			}
		}
		return true;
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
		// TODO Auto-generated method stub
		return false;
	}

}
