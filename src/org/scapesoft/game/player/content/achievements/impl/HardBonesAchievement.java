package org.scapesoft.game.player.content.achievements.impl;

import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.achievements.Achievement;
import org.scapesoft.game.player.content.achievements.AchievementManager;
import org.scapesoft.game.player.content.achievements.Types;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 23, 2014
 */
public class HardBonesAchievement extends Achievement {

	public HardBonesAchievement() {
		super(Types.HARD, "Bury @TOTAL@ regular bones");
	}

	@Override
	public String getRewardInfo() {
		return "3x Achievement Points";
	}

	@Override
	public void giveReward(Player player) {
		addAchievementPoints(player, 3);
	}

	@Override
	public int getTotalAmount() {
		return 500;
	}

	@Override
	public String getKey() {
		return "regular_bones";
	}

	@Override
	public boolean unlocked(Player player) {
		return player.getAchievementManager().completeAchievement(AchievementManager.getAchievement(EasyBonesAchievement.class));
	}

	@Override
	public String getUnlockInfo() {
		return "Complete Achievement: " + Utils.formatPlayerNameForDisplay(AchievementManager.getAchievement(EasyBonesAchievement.class).getTitle());
	}

	private static final long serialVersionUID = 30702979048711138L;
}
