package org.scapesoft.api.event.listeners.npc;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.ArmourSetOpening;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.exchange.ExchangeManagement;
import org.scapesoft.game.player.dialogues.impl.ClerkDialogue;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jun 20, 2014
 */
public class GrandExchangeClerk extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 2241 };
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		if (option == ClickOption.FIRST) {
			player.getDialogueManager().startDialogue(ClerkDialogue.class, npc.getId());
		} else if (option == ClickOption.SECOND) {
			ExchangeManagement.sendSummary(player);
		} else if (option == ClickOption.THIRD) {
			ArmourSetOpening.openSets(player);
		}
		return true;
	}

}