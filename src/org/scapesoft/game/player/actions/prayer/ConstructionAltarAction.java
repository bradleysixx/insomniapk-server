package org.scapesoft.game.player.actions.prayer;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.game.player.actions.Action;
import org.scapesoft.game.player.dialogues.impl.AltarBoneD;
import org.scapesoft.game.player.dialogues.impl.SimpleItemMessage;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 4, 2014
 */
public class ConstructionAltarAction extends Action {

	public ConstructionAltarAction(WorldObject object, int boneId, int ticks) {
		this.object = object;
		this.bone = Bone.forId(boneId);
		this.ticks = ticks;
		this.altar = Altars.getAltarById(object.getId());
	}

	public static boolean handleBoneOnAltar(Player player, WorldObject object, Item item) {
		final Bone bone = Bone.forId(item.getId());
		if (Altars.getAltarById(object.getId()) != null && bone != null) {
			player.getDialogueManager().startDialogue(AltarBoneD.class, item.getId(), object);
			return true;
		}
		return false;
	}

	@Override
	public boolean start(Player player) {
		if (!process(player)) {
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (ticks <= 0) {
			return false;
		}
		if (!player.getInventory().containsItem(bone.getId(), 1)) {
			return false;
		}
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		if (bone != null) {
			ticks--;
			player.getInventory().deleteItem(bone.getId(), 1);
			player.getSkills().addXp(Skills.PRAYER, bone.getExperience() * altar.boost);
			player.getDialogueManager().startDialogue(SimpleItemMessage.class, bone.getId(), "You offer the bones to the gods...", "", "They give you prayer experience in return.");
			player.getPackets().sendGraphics(OBJECT_GRAPHICS, object);
			player.setNextAnimation(ANIMATION);
		} else {
			stop(player);
		}
		return 1;
	}

	@Override
	public void stop(Player player) {
		
	}
	
	private final Bone bone;
	private final WorldObject object;
	private final Altars altar;
	
	private int ticks;
	
	private static final Graphics OBJECT_GRAPHICS = new Graphics(624);
	private static final Animation ANIMATION = new Animation(896);
	
	private enum Altars {
		
		OAK(13179, 1),
		TEAK(13182, 1.10),
		CLOTH(13185, 1.25),
		MAHOGANY(13188, 1.50),
		LIMESTONE(13191, 1.75),
		MARBLE(13194, 2),
		GILDED(13197, 2.50);
		
		Altars(int objectId, double boost) {
			this.objectId = objectId;
			this.boost = boost;
		}
		
		private final int objectId;
		private final double boost;
		
		public static final Altars getAltarById(int objectId) {
			for (Altars altar : Altars.values()) {
				if (altar.objectId == objectId) {
					return altar;
				}
			}
			return null;
		}
	}
}
