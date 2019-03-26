package org.scapesoft.game.player.content.wild.activities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.scapesoft.Constants;
import org.scapesoft.game.ForceTalk;
import org.scapesoft.game.Hit;
import org.scapesoft.game.Region;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.Hit.HitLook;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.mining.CrashedStarMining;
import org.scapesoft.game.player.content.wild.WildernessActivity;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 31, 2014
 */
public class MiningActivity extends WildernessActivity {

	@Override
	public String getDescription() {
		return "A Crashed star is spawned at " + star.getLocations().getInformation();
	}

	@Override
	public String getServerAnnouncement() {
		return "Crashed stars will be spawning all over the wilderness! Mine them for Wilderness Points!";
	}

	@Override
	public void onCreate() {
		spawnStar();
	}

	@Override
	public void process() {
		if (shouldSpawnStar()) {
			spawnStar();
		}
	}

	@Override
	public void onFinish() {
		destroyStar();
	}

	@Override
	public long getActivityTime() {
		return TimeUnit.MINUTES.toMillis(ACTIVITY_TIME);
	}

	@Override
	public boolean receivesBonus(Player player, Object... params) {
		return player.isDonator();
	}

	@Override
	public Integer getBonusPoints(Player player) {
		return 5;
	}

	@Override
	public Integer getPointChance(Player player) {
		return 30;
	}

	/**
	 * This method spawns a crashed star at a random {@code WildernessLocations}
	 * {@code WorldTile} and send a world message to everybody about its
	 * location
	 */
	private void spawnStar() {
		destroyStar();
		WildernessLocations location = WildernessLocations.values()[Utils.random(WildernessLocations.values().length)];
		if (location == null) {
			throw new RuntimeException("Location for star failed to set.");
		}
		WorldObject object = new WorldObject(38660, 10, 1, location.getTile());
		star = new CrashedStar(object, location);

		World.spawnObject(star.getObject());
		World.sendWorldMessage("<col=" + ChatColors.RED + ">Wilderness Activity:</col> A crashed star has spawned at " + location.getInformation(), false, true);
	}

	/**
	 * Destroys the star by removing it from the world and stops everyone from
	 * mining it.
	 */
	private void destroyStar() {
		if (star == null) {
			return;
		}
		World.players().forEach(p -> {
			if (p == null) {
				return;
			}
			if (p.getActionManager().getAction() instanceof CrashedStarMining) {
				p.getActionManager().getAction().stop(p);
			}
		});
		World.removeObject(star.getObject());
	}

	/**
	 * If the star exists and the star's life has been depleted, we should spawn
	 * a new one.
	 * 
	 * @return
	 */
	private boolean shouldSpawnStar() {
		if (star == null || star.life <= 0) {
			return true;
		}
		return false;
	}

	private CrashedStar star;
	private static final long ACTIVITY_TIME = Constants.DEBUG ? TimeUnit.SECONDS.toMillis(60) : TimeUnit.MINUTES.toMillis(5);

	private enum WildernessLocations {

		DEMONIC_RUINS(new WorldTile(3287, 3883, 0)) {
			@Override
			public String getInformation() {
				return "Demonic Ruins (Lvl 46)";
			}
		},

		KBD_ENTRANCE(new WorldTile(3011, 3846, 0)) {
			@Override
			public String getInformation() {
				return "King Black Dragons' Entrance (Lvl 41)";
			}
		},

		CHAOS_TEMPLE(new WorldTile(3222, 3633, 0)) {
			@Override
			public String getInformation() {
				return "Chaos Temple Entrance (Lvl 15)";
			}
		},

		DARK_WARRIORS_FORTRESS(new WorldTile(3029, 3631, 0)) {
			@Override
			public String getInformation() {
				return "Dark Warriors' Fortress (Lvl 14)";
			}
		};

		WildernessLocations(WorldTile tile) {
			this.tile = tile;
		}

		/**
		 * The worldtile of the location
		 */
		private final WorldTile tile;

		/**
		 * This method will give string information of the location
		 * 
		 * @return
		 */
		public abstract String getInformation();

		/**
		 * @return the tile
		 */
		public WorldTile getTile() {
			return tile;
		}
	}

	public CrashedStar getCrashedStar(int objectId) {
		if (star != null && star.getObject().getId() == objectId) {
			return star;
		}
		return null;
	}

	public static class CrashedStar extends WorldObject {
		private static final long serialVersionUID = 694626394356441469L;

		public CrashedStar(WorldObject object, WildernessLocations locations) {
			super(object);
			this.object = object;
			this.locations = locations;
			this.life = 30;
		}

		/**
		 * @return the object
		 */
		public WorldObject getObject() {
			return object;
		}

		/**
		 * @return the tile
		 */
		public WildernessLocations getLocations() {
			return locations;
		}

		/**
		 * Depletes the star's life
		 */
		public void deplete() {
			life--;
		}

		/**
		 * Getting the life of the star
		 */
		public int getLife() {
			return life;
		}

		/**
		 * The life of the star
		 */
		private int life;

		/**
		 * The {@code WorldObject} instance
		 */
		private final WorldObject object;

		/**
		 * The {@code WildernessLocations} instance
		 */
		private final WildernessLocations locations;

		/**
		 * When the star's life has ran out, it will destroy itself and all
		 * players around it will receive a reward and some damage added to
		 * them.
		 */
		public void destroy() {
			Region region = World.getRegion(getRegionId());
			if (region == null) {
				return;
			}
			List<Integer> indexes = region.getPlayerIndexes();
			List<Player> miners = new ArrayList<>();
			for (Integer index : indexes) {
				Player p = World.getPlayers().get(index);
				if (p == null) {
					continue;
				}
				if (p.getActionManager().getAction() instanceof CrashedStarMining) {
					miners.add(p);
				}
			}
			if (miners.size() == 0) {
				return;
			}
			Collections.shuffle(miners);
			Player random = miners.get(0);
			for (Player p : miners) {
				if (p == null) {
					continue;
				}
				if (random == p) {
					p.getFacade().addWildernessPoints(75);
					p.setNextForceTalk(new ForceTalk("Wow, what's this? 75 free wilderness points? Lucky me!"));
					p.applyHit(new Hit(p, random.getMaxHitpoints(), HitLook.HEALED_DAMAGE));
				} else {
					p.applyHit(new Hit(p, (int) (p.getHitpoints() * 0.65)));
				}
			}
		}
	}

}
