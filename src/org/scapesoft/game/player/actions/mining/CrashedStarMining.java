package org.scapesoft.game.player.actions.mining;

import org.scapesoft.game.Animation;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.game.player.content.wild.WildernessActivityManager;
import org.scapesoft.game.player.content.wild.activities.MiningActivity.CrashedStar;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

public class CrashedStarMining extends MiningBase {

	public static enum CrashedStarDefinitions {
		CRASHED_STAR(65, 500, 15, 200, 1);
		
		private int level;
		private double xp;
		private int pointReward;
		private int oreBaseTime;
		private int oreRandomTime;

		private CrashedStarDefinitions(int level, double xp, int pointReward, int oreBaseTime, int oreRandomTime) {
			this.level = level;
			this.xp = xp;
			this.pointReward = pointReward;
			this.oreBaseTime = oreBaseTime;
			this.oreRandomTime = oreRandomTime;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getPointReward() {
			return pointReward;
		}

		public int getOreBaseTime() {
			return oreBaseTime;
		}

		public int getOreRandomTime() {
			return oreRandomTime;
		}

	}

	private CrashedStar star;
	private CrashedStarDefinitions definitions;
	private PickAxeDefinitions axeDefinitions;

	public CrashedStarMining(CrashedStar star, CrashedStarDefinitions definitions) {
		this.star = star;
		this.definitions = definitions;
	}

	@Override
	public boolean start(Player player) {
		axeDefinitions = getPickAxeDefinitions(player);
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage("You swing your pickaxe at the rock.");
		setActionDelay(player, getMiningDelay(player));
		return true;
	}
	
	private int getMiningDelay(Player player) {
		int summoningBonus = 0;
		if (player.getFamiliar() != null) {
			if (player.getFamiliar().getId() == 7342 || player.getFamiliar().getId() == 7342)
				summoningBonus += 10;
			else if (player.getFamiliar().getId() == 6832 || player.getFamiliar().getId() == 6831)
				summoningBonus += 1;
		}
		int mineTimer = definitions.getOreBaseTime() - (player.getSkills().getLevel(Skills.MINING) + summoningBonus) - Utils.getRandom(axeDefinitions.getPickAxeTime());
		if (mineTimer < 1 + definitions.getOreRandomTime())
			mineTimer = 1 + Utils.getRandom(definitions.getOreRandomTime());
		mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
		return mineTimer;
	}


	private boolean checkAll(Player player) {
		if (axeDefinitions == null) {
			player.getPackets().sendGameMessage("You do not have a pickaxe or do not have the required level to use the pickaxe.");
			return false;
		}
		if (!hasMiningLevel(player))
			return false;
		return true;
	}

	private boolean hasMiningLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage("You need a mining level of " + definitions.getLevel() + " to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(axeDefinitions.getAnimationId()));
		return checkRock(player);
	}

	@Override
	public int processWithDelay(Player player) {
		addOre(player);
		if (!player.getInventory().hasFreeSlots()) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return -1;
		}
		return getMiningDelay(player);
	}

	private void addOre(Player player) {
		if (star.getLife() > 0) {
			star.deplete();
			if (star.getLife() <= 0) {
				star.destroy();
			}
			player.getSkills().addXp(Skills.MINING, definitions.getXp());
			player.getFacade().addWildernessPoints(definitions.getPointReward());
			player.sendMessage("<col=" + ChatColors.MAROON + ">You mine " + definitions.getPointReward() + " wilderness points.");
			WildernessActivityManager.getSingleton().giveBonusPoints(player, star);
		}
	}

	private boolean checkRock(Player player) {
		return World.containsObjectWithId(star, star.getId());
	}
}
