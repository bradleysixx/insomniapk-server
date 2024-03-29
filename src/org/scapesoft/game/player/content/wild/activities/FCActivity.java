package org.scapesoft.game.player.content.wild.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.wild.WildernessActivity;
import org.scapesoft.game.player.controlers.impl.Wilderness;
import org.scapesoft.utilities.misc.ChatColors;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 31, 2014
 */
public class FCActivity extends WildernessActivity {
	
	@Override
	public String getDescription() {
		return "Firemaking & Cooking have bonuses in " + location.getLocationInfo();
	}

	@Override
	public String getServerAnnouncement() {
		return "Firemaking and Cooking in the following <col=" + ChatColors.RED + ">wilderness</col> locations now have huge benefits: " + getLocationInformation();
	}

	/**
	 * The location information
	 * 
	 * @return
	 */
	public String getLocationInformation() {
		return "<col=" + ChatColors.MAROON + ">" + location.getLocationInfo() + ".";
	}

	@Override
	public void onCreate() {
		List<WildernessLocation> locations = new ArrayList<>(Arrays.asList(WildernessLocation.values()));
		Collections.shuffle(locations);
		location = locations.get(0);
		locations.clear();
		locations = null;
	}

	@Override
	public void process() {

	}

	@Override
	public void onFinish() {
	}

	@Override
	public long getActivityTime() {
		return TimeUnit.MINUTES.toMillis(10);
	}

	private WildernessLocation location;

	@Override
	public boolean receivesBonus(Player player, Object... params) {
		return location.isInArea(player);
	}

	@Override
	public Integer getBonusPoints(Player player) {
		if (player.isDonator())
			return 2;
		return 1;
	}

	@Override
	public Integer getPointChance(Player player) {
		if (player.isDonator())
			return 75;
		return 50;
	}

	private enum WildernessLocation {

		EDGEVILLE {

			@Override
			public int[] getRegionIds() {
				return new int[] { 12343, 12087 };
			}

			@Override
			public int getWildernessLevel() {
				return 3;
			}

			@Override
			public String getLocationInfo() {
				return "Edgeville Lvl 1-3";
			}
		},

		VARROCK_HOME {

			@Override
			public int[] getRegionIds() {
				return new int[] { 13111, 13367 };
			}

			@Override
			public int getWildernessLevel() {
				return 3;
			}

			@Override
			public String getLocationInfo() {
				return "North Home Wilderness (Lvl 1-3)";
			}

		};

		/**
		 * If the player is in the region
		 * 
		 * @param player
		 *            The player
		 * @return
		 */
		public boolean isInArea(Player player) {
			for (int regionId : getRegionIds()) {
				if (player.getRegionId() == regionId) {
					if (Wilderness.getWildLevel(player) <= getWildernessLevel()) {
						return true;
					}
				}
			}
			return false;
		}

		/**
		 * The region ids relevant
		 * 
		 * @return
		 */
		public abstract int[] getRegionIds();

		/**
		 * The wilderness level it goes to
		 * 
		 * @return
		 */
		public abstract int getWildernessLevel();

		/**
		 * Information about the location
		 * 
		 * @return
		 */
		public abstract String getLocationInfo();

	}

}
