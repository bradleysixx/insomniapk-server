package org.scapesoft.game.npc.others;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Entity;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.npc.combat.NPCCombatDefinitions;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;

public class KalphiteQueen extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = 3655019070319383057L;

	public KalphiteQueen(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setForceAgressive(true);
		setIntelligentRouteFinder(true);
	}
	
	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		setNextAnimation(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					setNextAnimation(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					if (getId() == 1158) {
						setCantInteract(true);
						transformInto(1160);
						setNextGraphics(new Graphics(1055));
						setNextAnimation(new Animation(6270));
						WorldTasksManager.schedule(new WorldTask() {

							@Override
							public void run() {
								reset();
								setCantInteract(false);
							}

						}, 5);
					} else {
						drop();
						reset();
						setLocation(getRespawnTile());
						loadMapRegions();
						finish();
						if (!isSpawned()) {
							setRespawnTask();
						}
						transformInto(1158);
					}
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

}
