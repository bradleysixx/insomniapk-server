package org.scapesoft.game.player.dialogues.impl;

import org.scapesoft.game.minigames.CastleWars;
import org.scapesoft.game.player.dialogues.Dialogue;

public class CastleWarsScoreboard extends Dialogue {

	@Override
	public void start() {
		CastleWars.viewScoreBoard(player);

	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();

	}

	@Override
	public void finish() {

	}

}
