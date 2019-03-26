package org.scapesoft.game.player.content.wild.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.scapesoft.Constants;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.wild.WildernessActivity;
import org.scapesoft.game.player.content.wild.WildernessBoss;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 31, 2014
 */
public class WildernessBossActivity extends WildernessActivity {

	/**
	 * The time players must wait after a boss has been killed for the next one
	 * to spawn
	 */
	public static final long RESPAWN_DELAY = Constants.DEBUG ? TimeUnit.SECONDS.toMillis(1) : TimeUnit.MINUTES.toMillis(2);

	@Override
	public String getDescription() {
		if (boss != null && !boss.hasFinished()) {
			return boss.getName() + " spawned in wilderness! Hunt it down!";
		} else {
			return "Wilderness bosses are spawning soon...";
		}
	}

	@Override
	public String getServerAnnouncement() {
		return "Wilderness Bosses are being spawned, pay attention for their locations!";
	}

	@Override
	public void onCreate() {
		nextBossSpawn = System.currentTimeMillis();
	}

	@Override
	public void process() {
		if (shouldSpawnBoss()) {
			List<WildernessSpawns> spawns = new ArrayList<>(Arrays.asList(WildernessSpawns.values()));
			Collections.shuffle(spawns);
			WildernessSpawns spawn = spawns.get(0);
			spawnRandomBoss(spawn);
		}
	}

	/**
	 * Spawns a random boss in the designated tile
	 * 
	 * @param spawn
	 *            The {@code WildernessSpawns} instance we should spawn the boss
	 */
	public void spawnRandomBoss(WildernessSpawns spawn) {
		int npcId = possibleBosses[(Utils.random(possibleBosses.length))];
		boss = new WildernessBoss(npcId, spawn.spawnTile);
		World.sendWorldMessage("<col=" + ChatColors.RED + ">Wilderness Activity:</col> A " + boss.getName() + " has spawned at <col=" + ChatColors.RED + ">" + spawn.getDescription() + "</col>. Hunt it down for better loot chance & wild points!", false, true);
	}

	@Override
	public void onFinish() {
		if (boss != null && !boss.hasFinished()) {
			boss.finish();
		}
	}

	@Override
	public long getActivityTime() {
		return TimeUnit.MINUTES.toMillis(15);
	}

	@Override
	public boolean receivesBonus(Player player, Object... params) {
		return true;
	}

	@Override
	public Integer getBonusPoints(Player player) {
		return 5;
	}

	@Override
	public Integer getPointChance(Player player) {
		return 80;
	}

	/**
	 * The next time a boss should be spawned
	 * 
	 * @param time
	 *            The time it should be spawned
	 */
	public void setNextSpawnTime(long time) {
		this.nextBossSpawn = time;
	}

	/**
	 * If we should spawn a boss
	 */
	private boolean shouldSpawnBoss() {
		if (boss != null && !boss.hasFinished()) {
			return false;
		}
		if (nextBossSpawn == -1 || (System.currentTimeMillis() >= nextBossSpawn))
			return true;
		return false;
	}

	/**
	 * The next boss spawn time
	 */
	private long nextBossSpawn = -1;

	/**
	 * The array of possible bosses
	 */
	private int[] possibleBosses = new int[] { 50, // king black dragon
			6260, // bandos boss
			6203, // zamorak boss
			8133, // corporeal beast
			8350, // tormented demon
			1158, // kalphite queen
			3334, // wildywyrm
	};

	private WildernessBoss boss;

	private enum WildernessSpawns {

		ROGUES_DEN(new WorldTile(3287, 3914, 0)) {
			@Override
			public String getDescription() {
				return "South of Rogues Den (Lvl 50)";
			}
		},

		RED_DRAGON_ISLE(new WorldTile(3203, 3866, 0)) {
			@Override
			public String getDescription() {
				return "Red Dragon Isle Entrance (Lvl 44)";
			}
		},

		BONEYARD(new WorldTile(3271, 3669, 0)) {
			@Override
			public String getDescription() {
				return "Boneyard (Lvl 19)";
			}
		},

		MAGE_BANK(new WorldTile(3035, 3940, 0)) {
			@Override
			public String getDescription() {
				return "West of Mage Bank (Lvl 53)";
			}
		},

		BANDIT_CAMP(new WorldTile(3035, 3685, 0)) {
			@Override
			public String getDescription() {
				return "Bandit Camp (Lvl 20)";
			}
		};

		WildernessSpawns(WorldTile spawnTile) {
			this.spawnTile = spawnTile;
		}

		/**
		 * The tile
		 */
		private final WorldTile spawnTile;

		/**
		 * The description of the location
		 * 
		 * @return
		 */
		public abstract String getDescription();
	}

}