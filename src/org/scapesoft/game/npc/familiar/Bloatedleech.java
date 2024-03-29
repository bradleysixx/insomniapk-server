package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.Graphics;
import org.scapesoft.game.Hit;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.summoning.Pouches;
import org.scapesoft.utilities.misc.Utils;

public class Bloatedleech extends Familiar {

	private static final long serialVersionUID = -5859609994157768837L;

	public Bloatedleech(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Blood Drain";
	}

	@Override
	public String getSpecialDescription() {
		return "Heals stat damage, poison, and disease but sacrifices some life points.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 5;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		final int damage = Utils.random(100) + 50;
		if (player.getHitpoints() - damage <= 0) {
			player.getPackets().sendGameMessage("You don't have enough life points to use this special.");
			return false;
		}
		if (player.getPoison().isPoisoned()) {
			player.getPoison().reset();
		}
		player.getSkills().restoreSkills();
		player.applyHit(new Hit(player, damage, HitLook.DESEASE_DAMAGE));
		setNextGraphics(new Graphics(1419));
		player.setNextGraphics(new Graphics(1420));
		return true;
	}
}