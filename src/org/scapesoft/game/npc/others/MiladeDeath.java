package org.scapesoft.game.npc.others;

import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.utilities.misc.Utils;

public class MiladeDeath extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = -5292929058746542076L;
	int ticks;
	private String[] MESSAGES = { "Come all, fight to your glorious deaths!", "Muhahaha, all I see are weaklings, prove your strength to me.", "Only weak fools dwell amongst this area, spend five minutes in my relm.", "It isn't proper to ignore a lady... especially me.", "There are no unfair advantages here, come all!", "Pathetic, even I can put up a better fight than you.", "Prove to me that you can fight you trash.", "Only weapons I approve are allowed here...", "Instead of pointing that thing at me you should be fighting.", "All players are welcome, well... except weaklings.", "Be prepared to lose your items... unless you're me." };

	public MiladeDeath(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setName("M'lady Death");
		setCombatLevel(1337);
	}

	@Override
	public void processNPC() {
		super.processNPC();
		ticks++;
		if (ticks % 15 == 0) {
			setNextForceTalk(new ForceTalk(MESSAGES[Utils.random(MESSAGES.length)]));
		}
	}
}
