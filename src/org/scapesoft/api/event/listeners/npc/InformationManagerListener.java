package org.scapesoft.api.event.listeners.npc;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.dialogues.impl.InformationManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 21, 2014
 */
public class InformationManagerListener extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 747 };
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		player.getDialogueManager().startDialogue(InformationManager.class, npc.getId());
		return true;
	}

}
