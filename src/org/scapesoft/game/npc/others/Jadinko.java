package org.scapesoft.game.npc.others;

import org.scapesoft.game.Entity;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

public class Jadinko extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = 4434769545744287829L;

	public Jadinko(int id, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		if (source instanceof Player) {
			Player player = (Player) source;
			player.setFavorPoints((getId() == 13820 ? 3 : getId() == 13821 ? 7 : 10) + player.getFavorPoints());
		}
	}
}
