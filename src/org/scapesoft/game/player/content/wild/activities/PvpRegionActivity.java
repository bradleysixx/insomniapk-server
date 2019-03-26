package org.scapesoft.game.player.content.wild.activities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.content.wild.WildernessActivity;
import org.scapesoft.game.player.controlers.impl.Wilderness;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 31, 2014
 */
public class PvpRegionActivity extends WildernessActivity {

	@Override
	public String getDescription() {
		return "PvP Kills in " + getLocation() + " give 2x points";
	}

	@Override
	public String getServerAnnouncement() {
		return "All PvP Kills received in " + getLocation() + " will now result in 2x Wilderness Points!";
	}

	public String getLocation() {
		return location.getAreaInformation();
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
		location = null;
	}

	@Override
	public long getActivityTime() {
		return 0;
	}

	@Override
	public boolean receivesBonus(Player player, Object... params) {
		return location.isInArea(player);
	}

	@Override
	public Integer getBonusPoints(Player player) {
		return 0;
	}

	@Override
	public Integer getPointChance(Player player) {
		return 0;
	}

	private WildernessLocation location;

	private enum WildernessLocation {

		EDGEVILLE {

			@Override
			public boolean isInArea(Player player) {
				for (int regionId : getRegionIds()) {
					if (player.getRegionId() == regionId && Wilderness.getWildLevel(player) <= 5) {
						return true;
					}
				}
				return false;
			}

			@Override
			public String getAreaInformation() {
				return "Edgeville Wild (Lvl 1-5)";
			}

			@Override
			public int[] getRegionIds() {
				return new int[] { 12343, 12087 };
			}
		},
		NORTH_HOME {

			@Override
			public String getAreaInformation() {
				return "Varrock Wilderness (North of Home)";
			}

			@Override
			public int[] getRegionIds() {
				return new int[] { 13111, 12855 };
			}
		},
		EAST_DRAGONS {

			@Override
			public String getAreaInformation() {
				return "East Dragons";
			}

			@Override
			public int[] getRegionIds() {
				return new int[] { 13369, 13368 };
			}
		},
		DESERTED_KEEP {

			@Override
			public String getAreaInformation() {
				return "Mage Bank/Deserted Keep";
			}

			@Override
			public int[] getRegionIds() {
				return new int[] { 12605, 12349, 12093 };
			}
		};

		public boolean isInArea(Player player) {
			for (int regionId : getRegionIds()) {
				if (player.getRegionId() == regionId) {
					return true;
				}
			}
			return false;
		}

		public abstract int[] getRegionIds();

		/**
		 * The information of this wilderness location
		 * 
		 * @return
		 */
		public abstract String getAreaInformation();
	}
}
