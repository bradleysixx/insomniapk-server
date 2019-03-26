package org.scapesoft.game.player.cutscenes.actions;

import org.scapesoft.game.player.Player;

public abstract class CutsceneAction {

	private int actionDelay; // -1 for no delay
	private int cachedObjectIndex;

	public CutsceneAction(int cachedObjectIndex, int actionDelay) {
		this.cachedObjectIndex = cachedObjectIndex;
		this.actionDelay = actionDelay;
	}

	public int getActionDelay() {
		return actionDelay;
	}

	public int getCachedObjectIndex() {
		return cachedObjectIndex;
	}

	public abstract void process(Player player, Object[] cache);

}
