package org.scapesoft.game.player.content.wild.activities;

import org.scapesoft.game.World;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.npc.NPC;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.actions.Fishing;
import org.scapesoft.game.player.content.wild.WildernessActivity;
import org.scapesoft.utilities.misc.ChatColors;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Dec 31, 2014
 */
public class FishingActivity extends WildernessActivity {

	@Override
	public String getDescription() {
		return "Fishing spots prepared below east dragons";
	}

	@Override
	public String getServerAnnouncement() {
		return "Fishing locations have spawned below east dragons! (<col=" + ChatColors.RED + ">Rocktails included</col>!) <col=" + ChatColors.GREEN + ">Fish here for great fishing rewards.</col>";
	}

	@Override
	public void onCreate() {
		for (FishingSpots spot : FishingSpots.values()) {
			for (WorldTile tile : spot.spawnTiles) {
				NPC fish = new NPC(spot.npcId, tile, -1, true);
				fish.getAttributes().put("fishing_activity_npc", true);
			}
		}
	}

	@Override
	public void process() {
		
	}

	@Override
	public void onFinish() {
		for (NPC npc : World.getNPCs()) {
			for (FishingSpots spot : FishingSpots.values()) {
				for (WorldTile tile : spot.spawnTiles) {
					if (npc.getId() == spot.npcId) {
						if (npc.getStartTile().equals(tile)) {
							npc.finish();
						}
					}
				}
			}
		}
		for (Player player : World.getPlayers()) {
			if (player == null || !(player.getActionManager().getAction() instanceof Fishing)) {
				continue;
			}
			player.getActionManager().forceStop();
		}
	}

	@Override
	public long getActivityTime() {
		return 0;
	}

	private enum FishingSpots {

		SHRIMP(327, new WorldTile(3351, 3634, 0)), TROUT_SALMON(328, new WorldTile(3353, 3633, 0)), LOBSTER(312, new WorldTile(3355, 3633, 0)), SHARK(313, new WorldTile[] { new WorldTile(3370, 3637, 0), new WorldTile(3368, 3635, 0) }), ROCKTAILS(8842, new WorldTile[] { new WorldTile(3365, 3635, 0), new WorldTile(3360, 3635, 0) });

		FishingSpots(int npcId, WorldTile[] spawnTiles) {
			this.npcId = npcId;
			this.spawnTiles = spawnTiles;
		}

		FishingSpots(int npcId, WorldTile spawnTile) {
			this.npcId = npcId;
			this.spawnTiles = new WorldTile[] { spawnTile };
		}

		/**
		 * The id of the fishing npc
		 */
		private final int npcId;

		/**
		 * The spawn tiles of the npc
		 */
		private final WorldTile[] spawnTiles;
	}

	@Override
	public boolean receivesBonus(Player player, Object... params) {
		NPC fish = (NPC) params[0];
		if (fish != null && fish.getAttributes().get("fishing_activity_npc") != null && player.getRegionId() == 13368) {
			return true;
		}
		return false;
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

}
