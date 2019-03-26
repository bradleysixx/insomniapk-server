package org.scapesoft.game.player.actions;

import java.util.List;

import org.scapesoft.game.Animation;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.Region;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

/**
 * This class handles all wilderness obelisk actions and teleportation
 * 
 * @author Tyluur<itstyluur@gmail.com>
 * @author Jake
 * 
 * @since January 1st, 2015
 */
public class WildernessObelisks {

	/**
	 * @author Jake | Santa Hat
	 * @author Tyluur<itstyluur@gmail.com>
	 */
	private enum ObeliskLocations {

		LOCATION_1(3154, 3158, 3618, 3622, new WorldTile(3156, 3620, 0), 14829), LOCATION_2(3217, 3221, 3654, 3658, new WorldTile(3219, 3656, 0), 14830), LOCATION_3(3033, 3037, 3730, 3734, new WorldTile(3035, 3732, 0), 14827), LOCATION_4(3104, 3108, 3792, 3796, new WorldTile(3106, 3794, 0), 14828), LOCATION_5(2978, 2982, 3864, 3868, new WorldTile(2980, 3866, 0), 14826), LOCATION_6(3305, 3309, 3914, 3918, new WorldTile(3307, 3916, 0), 14831);

		private int topLeftX, bottomRightX, bottomRightY, topLeftY;
		private WorldTile targetTile;
		private int objectId;

		ObeliskLocations(int topLeftX, int bottomRightX, int bottomRightY, int topLeftY, WorldTile location, int objectId) {
			this.topLeftX = topLeftX;
			this.bottomRightX = bottomRightX;
			this.bottomRightY = bottomRightY;
			this.topLeftY = topLeftY;
			this.targetTile = location;
			this.objectId = objectId;
		}

		public int getTopLeftX() {
			return topLeftX;
		}

		public int getBottomRightX() {
			return bottomRightX;
		}

		public int getBottomRightY() {
			return bottomRightY;
		}

		public int getTopLeftY() {
			return topLeftY;
		}

		public WorldTile getTargetTile() {
			return targetTile;
		}

		public int getObjectId() {
			return objectId;
		}

		/**
		 * Gets the {@code ObeliskLocations} {@code Object} based on an object
		 * id
		 * 
		 * @param objectId
		 *            The object id
		 * @return
		 */
		public static ObeliskLocations getLocation(int objectId) {
			for (ObeliskLocations location : ObeliskLocations.values()) {
				if (location.getObjectId() == objectId) {
					return location;
				}
			}
			return null;
		}
	}

	/**
	 * Pre-Teleport - 4 Second wait until the player is teleported
	 */
	public static void initializeTeleportation(Player player, final WorldObject object) {
		if (object.getAttributes().get("obelisk_teleporting") != null) {
			return;
		}
		ObeliskLocations ours = ObeliskLocations.getLocation(object.getId());
		if (ours == null) {
			return;
		}
		object.getAttributes().put("obelisk_teleporting", true);
		switchObelisk(object, ours);
		WorldTasksManager.schedule(new WorldTask() {
			int timer;

			@Override
			public void run() {
				if (timer == 4) {
					handleObelisk(object);
					object.getAttributes().remove("obelisk_teleporting");
					stop();
				}
				timer++;
			}
		}, 0, 1);
	}

	/**
	 * Switches the obelisk type
	 * 
	 * @param object
	 *            The obelisk
	 * @param location
	 *            The location
	 * @param type
	 *            The type
	 */
	public static void switchObelisk(WorldObject object, ObeliskLocations location) {
		Region region = World.getRegion(object.getRegionId());
		if (region == null) {
			return;
		}
		List<WorldObject> objects = region.getObjects();
		if (objects == null) {
			return;
		}
		for (WorldObject o : objects) {
			if (o.getId() == location.getObjectId()) {
				World.spawnObjectTemporary(new WorldObject(14825, o.getType(), o.getRotation(), o.getX(), o.getY(), o.getPlane()), 9 * 600);
			}
		}
	}

	/**
	 * Teleports all of the players standing on the Obelisk
	 */
	private static void handleObelisk(WorldObject object) {
		final ObeliskLocations ours = ObeliskLocations.getLocation(object.getId());
		if (ours == null) {
			return;
		}
		final ObeliskLocations destination = randomLocation(ours);
		for (final Player player : World.getPlayers()) {
			if (player.isLocked()) {
				continue;
			}
			if (isOnObelisk(player, ours)) {
				player.lock();
				player.setNextGraphics(new Graphics(661));
				player.setNextAnimation(new Animation(8939));
				WorldTasksManager.schedule(new WorldTask() {
					int timer;

					@Override
					public void run() {
						if (timer == 1) {
							player.setNextWorldTile(new WorldTile(Utils.random(destination.getTargetTile().getX() - 1, destination.getTargetTile().getX() + 1), Utils.random(destination.getTargetTile().getY() - 1, destination.getTargetTile().getY() + 1), 0));
							player.setNextAnimation(new Animation(8941));
							player.unlock();
							stop();
						}
						timer++;
					}
				}, 0, 1);
			}
		}
	}

	/**
	 * Generates a random location from the Locations enum
	 */
	private static ObeliskLocations randomLocation(ObeliskLocations current) {
		ObeliskLocations random = ObeliskLocations.values()[Utils.random(ObeliskLocations.values().length)];
		while (random == current) {
			random = ObeliskLocations.values()[Utils.random(ObeliskLocations.values().length)];
		}
		return random;
	}

	/**
	 * Checks if the player is within the Obelisk
	 */
	private static boolean isOnObelisk(WorldTile tile, ObeliskLocations ob) {
		return (tile.getX() >= ob.getTopLeftX() && tile.getX() <= ob.getBottomRightX() && tile.getY() >= ob.getBottomRightY() && tile.getY() <= ob.getTopLeftY());
	}

}