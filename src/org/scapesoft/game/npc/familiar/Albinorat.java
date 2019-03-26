package org.scapesoft.game.npc.familiar;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.summoning.Pouches;

public class Albinorat extends Familiar {

	/**
	 *
	 */
	private static final long serialVersionUID = 558701463128149919L;

	public Albinorat(Player owner, Pouches pouch, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(owner, pouch, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public String getSpecialName() {
		return "Cheese Feast";
	}

	@Override
	public String getSpecialDescription() {
		return "Fills your inventory with four peices of cheese.YUM.";
	}

	@Override
	public int getBOBSize() {
		return 0;
	}

	@Override
	public int getSpecialAmount() {
		return 6;
	}

	@Override
	public SpecialAttack getSpecialAttack() {
		return SpecialAttack.CLICK;
	}

	@Override
	public boolean submitSpecial(Object object) {
		Player player = (Player) object;
		player.setNextGraphics(new Graphics(1316));
		player.setNextAnimation(new Animation(7660));
		player.getInventory().addItem(new Item(1985, 4));
		return true;
	}
}
