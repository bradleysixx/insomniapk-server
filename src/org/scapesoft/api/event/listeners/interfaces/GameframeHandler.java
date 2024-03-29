package org.scapesoft.api.event.listeners.interfaces;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.impl.HelpDialogue;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 22, 2014
 */
public class GameframeHandler extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 548, 746 };
	}

	@Override
	public boolean handleButtonClick(Player player, int interfaceId, int buttonId, int packetId, int slotId, int itemId) {
		if ((interfaceId == 746 && buttonId == 51) || (interfaceId == 548 && buttonId == 103)) {
			if (player.getTemporaryAttributtes().remove("flashing_tab_flag") != null) {
				player.getPackets().sendConfig(1021, 0);
				return true;
			}
		}
		if ((buttonId == 132 && interfaceId == 548) || (buttonId == 42 && interfaceId == 746)) { // quest-tab
			player.getQuestManager().sendQuestTabInformation();
			return true;
		}
		if ((buttonId == 130 && interfaceId == 548) || (buttonId == 40 && interfaceId == 746)) { // info-tab
			player.getInterfaceManager().sendTaskTab(false);
			return true;
		}
		if ((buttonId == 184 && interfaceId == 548) || (buttonId == 175 && interfaceId == 746)) {
			player.getDialogueManager().startDialogue(HelpDialogue.class);
			return true;
		}
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
		return false;
	}

}
