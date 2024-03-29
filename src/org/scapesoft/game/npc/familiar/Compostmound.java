package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.game.player.actions.summoning.Pouches;

public class Compostmound extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 5013658417553282084L;

	public Compostmound(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Generate Compost";
	}

	@Override
	public String getSpecialDescription() {
		return "Fill a nearby compost bin with compost, with a chance of creating super compost.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 12;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		int newLevel = (int) (player.getSkills().getLevel(Skills.FARMING) + 1 + (Math.round(player.getSkills().getLevelForXp(Skills.FARMING) * .02)));
		if (newLevel > player.getSkills().getLevelForXp(Skills.FARMING) + 1 + (Math.round(player.getSkills().getLevelForXp(Skills.FARMING) * .02))) {
			newLevel = (int) (player.getSkills().getLevelForXp(Skills.FARMING) + 1 + (Math.round(player.getSkills().getLevelForXp(Skills.FARMING) * .02)));
		}
		player.setNextGraphics(new Graphics(1300));
		player.setNextAnimation(new Animation(7660));
		setNextGraphics(new Graphics(1312));
		player.getSkills().set(Skills.FARMING, newLevel);
		return true;
	}

}
