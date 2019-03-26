package org.scapesoft.game.player.content.achievements.impl;

import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.achievements.Achievement;
import org.scapesoft.game.player.content.achievements.Types;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 8, 2014
 */
public class InfernoAdzeAchievement extends Achievement {

	public InfernoAdzeAchievement() {
		super(Types.EASY, "Burn @TOTAL@ logs");
	}

	@Override
	public String getRewardInfo() {
		return "Inferno Adze";
	}

	@Override
	public void giveReward(Player player) {
		addItem(player, new Item(13661));
	}

	@Override
	public int getTotalAmount() {
		return 1000;
	}

	@Override
	public String getKey() {
		return "inferon_adze_count";
	}

	private static final long serialVersionUID = 6385963737310033982L;
}
