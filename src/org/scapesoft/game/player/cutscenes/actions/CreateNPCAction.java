package org.scapesoft.game.player.cutscenes.actions;

import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.cutscenes.Cutscene;

public class CreateNPCAction extends CutsceneAction {

	private int id, x, y, plane;

	public CreateNPCAction(int cachedObjectIndex, int id, int x, int y, int plane, int actionDelay) {
		super(cachedObjectIndex, actionDelay);
		this.id = id;
		this.x = x;
		this.y = y;
		this.plane = plane;
	}

	@Override
	public void process(Player player, Object[] cache) {
		Cutscene scene = (Cutscene) cache[0];
		if (cache[getCachedObjectIndex()] != null) {
			scene.destroyCache(cache[getCachedObjectIndex()]);
		}
		NPC npc = (NPC) (cache[getCachedObjectIndex()] = World.spawnNPC(id, new WorldTile(scene.getBaseX() + x, scene.getBaseY() + y, plane), -1, true, true));
		npc.setRandomWalk(false);
	}

}
