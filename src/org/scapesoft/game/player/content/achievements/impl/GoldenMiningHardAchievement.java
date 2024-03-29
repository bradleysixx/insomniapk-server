package org.scapesoft.game.player.content.achievements.impl;

import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.achievements.Achievement;
import org.scapesoft.game.player.content.achievements.Types;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 10, 2014
 */
public class GoldenMiningHardAchievement extends Achievement {

	public GoldenMiningHardAchievement() {
		super(Types.HARD, "Mine @TOTAL@ total ores");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6964339445517731504L;

	@Override
	public String getRewardInfo() {
		return "Golden Mining Suit & 3x Achievement Points";
	}

	@Override
	public void giveReward(Player player) {
		addItem(player, new Item(20789), new Item(20791), new Item(20790), new Item(20787), new Item(20788));
		addAchievementPoints(player, 3);
	}

	@Override
	public int getTotalAmount() {
		return 1500;
	}

	@Override
	public String getKey() {
		return "ores_mined_total";
	}

}
