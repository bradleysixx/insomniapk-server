package org.scapesoft.game.npc.others;

import java.util.concurrent.TimeUnit;

import org.scapesoft.game.Animation;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 5, 2014
 */
@SuppressWarnings("serial")
public class DeathNPC extends NPC {

	public static WorldTile getClosestFreeTile(WorldTile center) {
		if (World.isTileFree(center.getPlane(), center.getX() + 1, center.getY(), 1)) {
			return new WorldTile(center.getX() + 1, center.getY(), center.getPlane());
		} else if (World.isTileFree(center.getPlane(), center.getX() - 1, center.getY(), 1)) {
			return new WorldTile(center.getX() - 1, center.getY(), center.getPlane());
		} else if (World.isTileFree(center.getPlane(), center.getX(), center.getY() + 1, 1)) {
			return new WorldTile(center.getX(), center.getY() + 1, center.getPlane());
		} else if (World.isTileFree(center.getPlane(), center.getX(), center.getY() - 1, 1)) {
			return new WorldTile(center.getX(), center.getY() - 1, center.getPlane());
		}
		return center;
	}

	public DeathNPC(Player spawnedFor, WorldTile tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(2862, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		this.spawned = System.currentTimeMillis();

		setNextForceTalk(new ForceTalk("You stood no chance, " + spawnedFor.getDisplayName() + "!"));
		setNextFaceEntity(spawnedFor);

		setNextAnimation(new Animation(10530));
		setNextGraphics(new Graphics(1864));
	}

	@Override
	public void processNPC() {
		if ((TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - spawned) >= 3)) {
			finish();
			return;
		}
		super.processNPC();
	}

	private final long spawned;
}
