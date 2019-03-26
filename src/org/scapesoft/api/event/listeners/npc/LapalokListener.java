package org.scapesoft.api.event.listeners.npc;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.slayer.SlayerManager;
import org.scapesoft.game.player.dialogues.DialogueHandler;
import org.scapesoft.game.player.dialogues.impl.Kuradal;
import org.scapesoft.utilities.console.gson.GsonHandler;
import org.scapesoft.utilities.console.gson.impl.ShopsLoader;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 20, 2014
 */
public class LapalokListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 8467, 9085 };
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
		if (option == ClickOption.SECOND) {
			Kuradal lapalok = (Kuradal) DialogueHandler.getDialogue(Kuradal.class.getSimpleName());
			player.getDialogueManager().startDialogue(lapalok, npc.getId());
			lapalok.requestTask(player);
			return true;
		} else if (option == ClickOption.THIRD) {
			player.getDialogueManager().finishDialogue();
			player.getSlayerManager().displayRewards(SlayerManager.BUY_INTERFACE);
			return true;
		} else if (option == ClickOption.FOURTH) {
			player.getDialogueManager().finishDialogue();
			((ShopsLoader) GsonHandler.getJsonLoader(ShopsLoader.class)).openShop(player, "Slayer Rewards");
			return true;
		}
		return false;
	}

	@Override
	public boolean handleItemClick(Player player, Item item, ClickOption option) {
		return false;
	}

}
