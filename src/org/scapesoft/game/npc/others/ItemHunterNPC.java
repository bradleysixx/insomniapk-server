package org.scapesoft.game.npc.others;

import java.util.List;

import org.scapesoft.game.World;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.OwnedObjectManager;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.OwnedObjectManager.ConvertEvent;
import org.scapesoft.game.player.actions.Hunter;
import org.scapesoft.game.player.actions.BoxAction.HunterNPC;
import org.scapesoft.game.player.actions.Hunter.DynamicFormula;

public class ItemHunterNPC extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = 3435469193288466870L;

	public ItemHunterNPC(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, spawned);
	}

	boolean check = false;

	@Override
	public void processNPC() {
		super.processNPC();
		List<WorldObject> objects = World.getRegion(getRegionId()).getSpawnedObjects();
		if (objects != null) {
			final HunterNPC info = HunterNPC.forId(getId());
			int objectId = info.getEquipment().getObjectId();
			for (final WorldObject object : objects) {
				if (object.getId() == objectId) {
					if (OwnedObjectManager.convertIntoObject(object, new WorldObject(info.getSuccessfulTransformObjectId(), 10, 0, this.getX(), this.getY(), this.getPlane()), new ConvertEvent() {
						@Override
						public boolean canConvert(Player player) {
							if (player == null || player.isLocked()) {
								return false;
							}
							if (Hunter.isSuccessful(player, info.getLevel(), new DynamicFormula() {
								@Override
								public int getExtraProbablity(Player player) {
									// bait here
									return 1;
								}
							})) {
								failedAttempt(object, info);
								return false;
							} else {
								setNextAnimation(info.getSuccessCatchAnim());
								return true;
							}
						}
					})) {
						setRespawnTask(); // auto finishes
					}
				}
			}
		}
	}

	private void failedAttempt(WorldObject object, HunterNPC info) {
		setNextAnimation(info.getFailCatchAnim());
		if (OwnedObjectManager.convertIntoObject(object, new WorldObject(info.getFailedTransformObjectId(), 10, 0, this.getX(), this.getY(), this.getPlane()), new ConvertEvent() {
			@Override
			public boolean canConvert(Player player) {
				// if(larupia)
				// blablabla
				return true;
			}
		})) {
		}
	}
}