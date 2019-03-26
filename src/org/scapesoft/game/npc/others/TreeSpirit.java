package org.scapesoft.game.npc.others;

import org.scapesoft.game.Entity;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

public class TreeSpirit extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = -7774114178429486637L;
	private Player target;

	public TreeSpirit(Player target, WorldTile tile) {
		super(655, tile, -1, true, true);
		this.target = target;
		target.getTemporaryAttributtes().put("HAS_SPIRIT_TREE", true);
		setTarget(target);
		setNextForceTalk(new ForceTalk("You must defeat me before touching the tree!"));
	}

	@Override
	public void processNPC() {
		if (!target.withinDistance(this, 16)) {
			target.getTemporaryAttributtes().remove("HAS_SPIRIT_TREE");
			finish();
		}
		super.processNPC();
	}

	@Override
	public void sendDeath(Entity source) {
		target.getTemporaryAttributtes().remove("HAS_SPIRIT_TREE");
		super.sendDeath(source);

	}

}
