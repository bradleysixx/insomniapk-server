package org.scapesoft.game.npc.others;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.controlers.impl.RunespanControler;
import org.scapesoft.utilities.misc.Utils;

public class YellowWizard extends NPC {

	/**
	 *
	 */
	private static final long serialVersionUID = -5394922650288930544L;
	private RunespanControler controler;
	private long spawnTime;

	public YellowWizard(WorldTile tile, RunespanControler controler) {
		super(15430, tile, -1, true, true);
		spawnTime = Utils.currentTimeMillis();
		this.controler = controler;
	}

	@Override
	public void processNPC() {
		if (spawnTime + 300000 < Utils.currentTimeMillis()) {
			finish();
		}
	}

	@Override
	public void finish() {
		controler.removeWizard();
		super.finish();
	}

	public static void giveReward(Player player) {

	}

	@Override
	public boolean withinDistance(Player tile, int distance) {
		return tile == controler.getPlayer() && super.withinDistance(tile, distance);
	}

}
