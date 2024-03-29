package org.scapesoft.game.player.actions.prayer;

import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.Animation;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.game.player.content.achievements.impl.EasyBonesAchievement;
import org.scapesoft.game.player.content.achievements.impl.EasyDragonBonesAchievement;
import org.scapesoft.game.player.content.achievements.impl.HardBonesAchievement;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public class Burying {

	/**
	 * Burys the bone in the slot
	 * 
	 * @param player
	 *            The player burying the bone
	 * @param slotId
	 *            The slot id of the bone
	 */
	public static void bury(final Player player, int slotId) {
		final Item item = player.getInventory().getItem(slotId);
		if (item == null || Bone.forId(item.getId()) == null) {
			return;
		}
		if (player.getBoneDelay() > Utils.currentTimeMillis()) {
			return;
		}
		final Bone bone = Bone.forId(item.getId());
		final ItemDefinitions itemDef = new ItemDefinitions(item.getId());
		player.lock(3);
		player.getPackets().sendSound(2738, 0, 1);
		player.setNextAnimation(new Animation(827));
		player.getPackets().sendGameMessage("You dig a hole in the ground...");
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				player.getPackets().sendGameMessage("You bury the " + itemDef.getName().toLowerCase());
				player.getInventory().deleteItem(item.getId(), 1);
				player.getSkills().addXp(Skills.PRAYER, bone.getExperience());
				if (bone == Bone.NORMAL) {
					player.getAchievementManager().notifyUpdate(EasyBonesAchievement.class);
					player.getAchievementManager().notifyUpdate(HardBonesAchievement.class);
				} else if (bone == Bone.DRAGON) {
					player.getAchievementManager().notifyUpdate(EasyDragonBonesAchievement.class);
				}
				stop();
			}
		}, 2);
	}

}
