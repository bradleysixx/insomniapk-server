package org.scapesoft.game.npc.godwars.zaros;

import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.minigames.ZarosGodwars;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

public class NexMinion extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = 4002213100385427294L;
	private boolean hasNoBarrier;

	public NexMinion(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, true, true);
		setCantFollowUnderCombat(true);
	}

	public void breakBarrier() {
		setCapDamage(600);
		hasNoBarrier = true;
	}

	public boolean isBarrierBroken() {
		return hasNoBarrier;
	}

	@Override
	public void handleIngoingHit(Hit hit) {
		if (!hasNoBarrier) {
			setNextGraphics(new Graphics(1549));
			if (hit.getSource() instanceof Player) {
				((Player) hit.getSource()).getPackets().sendGameMessage("The avatar is not weak enough to damage this minion.");
			}
			hit.setDamage(0);
		} else {
			super.handleIngoingHit(hit);
		}
	}

	@Override
	public void processNPC() {
		if (isDead() || !hasNoBarrier) {
			return;
		}
		if (!getCombat().process()) {
			checkAgressivity();
		}
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		ZarosGodwars.incrementStage(ZarosGodwars.nex.getCurrentPhase());
	}
}
