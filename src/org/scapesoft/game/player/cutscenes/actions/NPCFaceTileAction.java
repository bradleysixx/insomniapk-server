package org.scapesoft.game.player.cutscenes.actions;

import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.cutscenes.Cutscene;

public class NPCFaceTileAction extends CutsceneAction {

	private int x, y;

	public NPCFaceTileAction(int cachedObjectIndex, int x, int y, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.x = x;
		this.y = y;
	}

	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		NPC npc = (NPC) cache[getCachedObjectIndex()];
		npc.setNextFaceWorldTile(new WorldTile(scene.getBaseX() + x, scene.getBaseY() + y, npc.getPlane()));
	}

}
