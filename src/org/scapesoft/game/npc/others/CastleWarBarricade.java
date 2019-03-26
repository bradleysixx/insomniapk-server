package org.scapesoft.game.npc.others;

import org.scapesoft.game.Entity;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.minigames.CastleWars;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.utilities.misc.Utils;

public class CastleWarBarricade extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = 31049854479236711L;
	private int team;

	public CastleWarBarricade(int team, WorldTile tile) {
		super(1532, tile, -1, true, true);
		setCantFollowUnderCombat(true);
		this.team = team;
	}

	@Override
	public void processNPC() {
		if (isDead()) {
			return;
		}
		cancelFaceEntityNoCheck();
		if (getId() == 1533 && Utils.getRandom(20) == 0) {
			sendDeath(this);
		}
	}

	public void litFire() {
		transformInto(1533);
		sendDeath(this);
	}

	public void explode() {
		sendDeath(this);
	}

	@Override
	public void sendDeath(Entity killer) {
		resetWalkSteps();
		getCombat().removeTarget();
		if (this.getId() != 1533) {
			setNextAnimation(null);
			reset();
			setLocation(getRespawnTile());
			finish();
		} else {
			super.sendDeath(killer);
		}
		CastleWars.removeBarricade(team, this);
	}

}
