package org.scapesoft.game.player.cutscenes.actions;

import org.scapesoft.game.Graphics;
import org.scapesoft.game.player.Player;

public class PlayerGraphicAction extends CutsceneAction {

	private Graphics gfx;

	public PlayerGraphicAction(Graphics gfx, int actionDelay) {
		super(-1, actionDelay);
		this.gfx = gfx;
	}

	@Override
	public void process(Player player, Object[] cache) {
		player.setNextGraphics(gfx);
	}

}
