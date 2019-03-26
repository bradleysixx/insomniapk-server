package org.scapesoft.game.npc.others;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;

public class MutatedZygomites extends ConditionalDeath {

	/**
	 *
	 */
	private static final long serialVersionUID = 1171138458145769683L;
	boolean lvl74;

	public MutatedZygomites(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(7421, null, false, id, tile, mapAreaNameHash, true);
		this.lvl74 = id == 3344;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (!getCombat().process()) {
			resetNPC();
		}
	}

	@Override
	public void sendDeath(final Entity source) {
		super.sendDeath(source);
		if (getHitpoints() != 1) {
			resetNPC();
		}
	}

	private void resetNPC() {
		setNPC(lvl74 ? 3344 : 3345);
		transformInto(lvl74 ? 3344 : 3345);
	}

	public static void transform(final Player player, final NPC npc) {
		player.setNextAnimation(new Animation(2988));
		npc.transformInto(npc.getId() + 2);
		npc.setNPC(npc.getId() + 2);
		npc.setNextAnimation(new Animation(2982));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				npc.getCombat().setTarget(player);
				npc.setCantInteract(false);
			}
		});
	}
}
