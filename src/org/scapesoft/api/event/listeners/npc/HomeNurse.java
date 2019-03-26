package org.scapesoft.api.event.listeners.npc;

import org.scapesoft.api.event.EventListener;
import org.scapesoft.game.Animation;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.game.player.dialogues.SimpleNPCMessage;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Nov 29, 2014
 */
public class HomeNurse extends EventListener {

	@Override
	public int[] getEventIds() {
		return new int[] { 961 };
	}

	@Override
	public boolean handleNPCClick(Player player, NPC npc, ClickOption option) {
		/** Performing a cool npc interaction */
		npc.setNextAnimation(new Animation(12575));
		player.setNextGraphics(new Graphics(1314));
		/** Refreshing all player characteristics to optimal settings */
		player.getCombatDefinitions().setSpecialAttack(100);
		player.getPoison().reset();
		player.getPrayer().setPrayerpoints((int) ((player.getSkills().getLevelForXp(Skills.PRAYER) * 10) * 1.15));
		player.getPrayer().refreshPrayerPoints();
		player.heal(player.getMaxHitpoints(), (int) ((player.getSkills().getLevelForXp(Skills.HITPOINTS) * 10) * 0.05));
		/** Sending surgeon dialogue */
		player.getDialogueManager().startDialogue(SimpleNPCMessage.class, 961, "I have restored your character to extreme health!");
		player.sendMessage("You feel refreshed, past your normal health levels.");
		return true;
	}

}
