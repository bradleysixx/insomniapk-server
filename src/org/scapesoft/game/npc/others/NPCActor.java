package org.scapesoft.game.npc.others;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

public class NPCActor extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = -371817639970818404L;
	private Player target;

	public NPCActor(int id, String name, int combat, WorldTile tile, Player target) {
		super(id, tile, -1, true, true);
		this.target = target;
		setWalkType(0);
		if (name != null) {
			setName(name);
		}
		if (combat != -1) {
			setCombatLevel(combat);
		}
	}

	@Override
	public boolean withinDistance(Player tile, int distance) {
		return tile == target && super.withinDistance(tile, distance);
	}

	@Override
	public boolean checkAgressivity() {
		return false;
	}

}
