package org.scapesoft.game.npc.others;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.misc.Utils;

public class DreadNip extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = 329654207566527690L;

	public static final String[] DREADNIP_MESSAGES = { "Your dreadnip couldn't attack so it left.", "The dreadnip gave up as you were too far away.", "Your dreadnip served its purpose and fled." };

	private Player owner;
	private int ticks;

	public DreadNip(Player owner, int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		this.owner = owner;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (owner == null || owner.hasFinished()) {
			finish(-1);
			return;
		} else if (getCombat().getTarget() == null || getCombat().getTarget().isDead()) {
			finish(0);
			return;
		} else if (Utils.getDistance(owner, this) >= 10) {
			finish(1);
			return;
		} else if (ticks++ == 33) {
			finish(2);
			return;
		}
	}

	private void finish(int index) {
		if (index != -1) {
			owner.getPackets().sendGameMessage(DREADNIP_MESSAGES[index]);
			owner.getTemporaryAttributtes().remove("hasDN");
		}
		this.finish();
	}

	public Player getOwner() {
		return owner;
	}

	public int getTicks() {
		return ticks;
	}
}
