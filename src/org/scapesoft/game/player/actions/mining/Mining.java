package org.scapesoft.game.player.actions.mining;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.Animation;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.minigames.games.GamesHandler;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.game.player.content.achievements.impl.FaladorMediumAchievement;
import org.scapesoft.game.player.content.achievements.impl.GoldenMiningHardAchievement;
import org.scapesoft.game.player.content.achievements.impl.RuneOreHardAchievement;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

public final class Mining extends MiningBase {

	private static final int[] UNCUT_GEMS = { 1623, 1619, 1621, 1617, 1631, 6571 };
	private static final Map<Integer, RockDefinitions> DEFINITIONS = new HashMap<>();

	public static void load() {
		try {
			List<String> list = (ArrayList<String>) Files.readAllLines(new File("./data/map/rocks.txt").toPath(), Charset.defaultCharset());
			for (String line : list) {
				String[] data = line.split(":");
				String name = data[0];
				String[] values = data[1].split(",");
				RockDefinitions d = null;
				switch (name.split(" ")[0]) {
				case "Coal":
					d = RockDefinitions.Coal_Ore;
					break;
				case "Blurite":
					d = RockDefinitions.Blurite_Ore;
					break;
				case "Adamantite":
					d = RockDefinitions.Adamant_Ore;
					break;
				case "Copper":
					d = RockDefinitions.Copper_Ore;
					break;
				case "Gold":
					d = RockDefinitions.Gold_Ore;
					break;
				case "Granite":
					d = RockDefinitions.Granite_Ore;
					break;
				case "Mithril":
					d = RockDefinitions.Mithril_Ore;
					break;
				case "Silver":
					d = RockDefinitions.Silver_Ore;
					break;
				case "Sandstone":
					d = RockDefinitions.Sandstone_Ore;
					break;
				case "Clay":
					d = RockDefinitions.Clay_Ore;
					break;
				case "Runite":
					d = RockDefinitions.Runite_Ore;
					break;
				case "Tin":
					d = RockDefinitions.Tin_Ore;
					break;
				case "Iron":
					d = RockDefinitions.Iron_Ore;
					break;
				default:
					System.err.println("Unregistered mining ore name: " + name);
					break;
				}
				for (String value : values) {
					int objectId = Integer.parseInt(value);
					DEFINITIONS.put(objectId, d);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RockDefinitions getRockDefinitions(int id) {
		return DEFINITIONS.get(id);
	}

	public static enum RockDefinitions {

		Clay_Ore(1, 5, 434, 10, 1, 11552, 5, 0),
		Copper_Ore(1, 17.5, 436, 10, 1, 11552, 5, 0), 
		Tin_Ore(1, 17.5, 438, 15, 1, 11552, 5, 0),
		Blurite_Ore(10, 17.5, 668, 15, 1, 11552, 7, 0), 
		Iron_Ore(15, 35, 440, 15, 1, 11552, 10, 0), 
		Sandstone_Ore(35, 30, 6971, 30, 1, 11552, 10, 0),
		Silver_Ore(20, 40, 442, 25, 1, 11552, 20, 0),
		Coal_Ore(30, 50, 453, 50, 10, 11552, 30, 0), 
		Granite_Ore(45, 50, 6979, 50, 10, 11552, 20, 0), 
		Gold_Ore(40, 60, 444, 80, 20, 11554, 40, 0),
		Mithril_Ore(55, 80, 447, 100, 20, 11552, 60, 0), 
		Adamant_Ore(70, 95, 449, 130, 25, 11552, 180, 0),
		Runite_Ore(85, 125, 451, 150, 30, 11552, 360, 0),
		LRC_Coal_Ore(77, 50, 453, 50, 10, -1, -1, -1) {

		},
		LRC_Gold_Ore(80, 60, 444, 40, 10, -1, -1, -1);

		private int level;
		private double xp;
		private int oreId;
		private int oreBaseTime;
		private int oreRandomTime;
		private int emptySpot;
		private int respawnDelay;
		private int randomLifeProbability;

		private RockDefinitions(int level, double xp, int oreId, int oreBaseTime, int oreRandomTime, int emptySpot, int respawnDelay, int randomLifeProbability) {
			this.level = level;
			this.xp = xp;
			this.oreId = oreId;
			this.oreBaseTime = oreBaseTime;
			this.oreRandomTime = oreRandomTime;
			this.emptySpot = emptySpot;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int getOreId() {
			return oreId;
		}

		public int getOreBaseTime() {
			return oreBaseTime;
		}

		public int getOreRandomTime() {
			return oreRandomTime;
		}

		public int getEmptyId() {
			return emptySpot;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}
	}

	private WorldObject rock;
	private RockDefinitions definitions;
	private PickAxeDefinitions axeDefinitions;

	public Mining(WorldObject rock, RockDefinitions definitions) {
		this.rock = rock;
		this.definitions = definitions;
	}

	@Override
	public boolean start(Player player) {
		axeDefinitions = getPickAxeDefinitions(player);
		if (!checkAll(player))
			return false;
		player.getPackets().sendGameMessage("You swing your pickaxe at the rock.", true);
		setActionDelay(player, getMiningDelay(player));
		return true;
	}

	private int getMiningDelay(Player player) {
		int summoningBonus = 0;
		if (player.getFamiliar() != null) {
			if (player.getFamiliar().getId() == 7342 || player.getFamiliar().getId() == 7342)
				summoningBonus += 10;
			else if (player.getFamiliar().getId() == 6832 || player.getFamiliar().getId() == 6831)
				summoningBonus += 1;
		}
		int mineTimer = definitions.getOreBaseTime() - (player.getSkills().getLevel(Skills.MINING) + summoningBonus) - Utils.getRandom(axeDefinitions.getPickAxeTime());
		if (mineTimer < 1 + definitions.getOreRandomTime())
			mineTimer = 1 + Utils.getRandom(definitions.getOreRandomTime());
		mineTimer /= player.getAuraManager().getMininingAccurayMultiplier();
		return mineTimer;
	}

	private boolean checkAll(Player player) {
		if (axeDefinitions == null) {
			player.getPackets().sendGameMessage("You do not have a pickaxe or do not have the required level to use the pickaxe.");
			return false;
		}
		if (!hasMiningLevel(player))
			return false;
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	private boolean hasMiningLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(Skills.MINING)) {
			player.getPackets().sendGameMessage("You need a mining level of " + definitions.getLevel() + " to mine this rock.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		player.setNextAnimation(new Animation(axeDefinitions.getAnimationId()));
		return checkRock(player);
	}

	private boolean usedDeplateAurora;

	/**
	 * If the ore should be removed from the map
	 * 
	 * @param player
	 *            The player
	 * @return
	 */
	private boolean shouldRemoveOre(Player player) {
		if (player.getControllerManager().getController() instanceof GamesHandler)
			return false;
		if (player.getRegionId() == 10301)
			return false;
		return true;
	}

	@Override
	public int processWithDelay(Player player) {
		addOre(player);
		if (shouldRemoveOre(player)) {
			if (definitions.getEmptyId() != -1) {
				if (!usedDeplateAurora && (1 + Math.random()) < player.getAuraManager().getChanceNotDepleteMN_WC()) {
					usedDeplateAurora = true;
				} else if (Utils.getRandom(definitions.getRandomLifeProbability()) == 0) {
					World.spawnTemporaryObject(new WorldObject(definitions.getEmptyId(), rock.getType(), rock.getRotation(), rock.getX(), rock.getY(), rock.getPlane()), definitions.respawnDelay * 600);
					player.setNextAnimation(new Animation(-1));
					return -1;
				}
			}
		}
		if (!player.getInventory().hasFreeSlots() && definitions.getOreId() != -1) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return -1;
		}
		return getMiningDelay(player);
	}

	private void addOre(Player player) {
		double xpBoost = 0;
		int idSome = 0;
		if (definitions == RockDefinitions.Granite_Ore) {
			idSome = Utils.getRandom(2) * 2;
			if (idSome == 2)
				xpBoost += 10;
			else if (idSome == 4)
				xpBoost += 25;
		} else if (definitions == RockDefinitions.Sandstone_Ore) {
			idSome = Utils.getRandom(3) * 2;
			xpBoost += idSome / 2 * 10;
		}
		if (!(player.getControllerManager().getController() instanceof GamesHandler)) {
			if (definitions == RockDefinitions.Coal_Ore) {
				player.getAchievementManager().notifyUpdate(FaladorMediumAchievement.class);
			} else if (definitions == RockDefinitions.Runite_Ore) {
				player.getAchievementManager().notifyUpdate(RuneOreHardAchievement.class);
			}
			player.getAchievementManager().notifyUpdate(GoldenMiningHardAchievement.class);
		}
		double totalXp = definitions.getXp() + xpBoost;
		if (hasMiningSuit(player))
			totalXp *= 1.025;
		player.getSkills().addXp(Skills.MINING, totalXp);

		if (player.getControllerManager().getController() instanceof GamesHandler) {
			int barId = -1;
			double points = 0;
			switch(definitions) {
			case Iron_Ore:
				barId = 2351;
				points = 0.5;
				break;
			case Mithril_Ore:
				barId = 2359;
				points = 1.5;
				break;
			case Adamant_Ore:
				barId = 2361;	
				points = 2.5;
				break;
			case Runite_Ore:
				barId = 2363;
				points = 4;
				break;
			default:
				break;
			}
			if (barId != -1) {
				GamesHandler controller = (GamesHandler) player.getControllerManager().getController();
				controller.addSkillPoints(points);
				player.sendMessage("<col=" + ChatColors.MAROON + ">You mine a complete " + ItemDefinitions.getItemDefinitions(barId).name.toLowerCase() + "!");
				player.getInventory().addItem(barId, 1);
			}
		} else if (definitions.getOreId() != -1) {
			player.getInventory().addItem(definitions.getOreId() + idSome, 1);
			String oreName = ItemDefinitions.getItemDefinitions(definitions.getOreId() + idSome).getName().toLowerCase();
			player.getPackets().sendGameMessage("You mine some " + oreName + ".", true);
			if (Utils.random(150) == 0)
				player.getInventory().addItemDrop(UNCUT_GEMS[Utils.random(UNCUT_GEMS.length - 2)], 1);
			else if (Utils.random(5000) == 0)
				player.getInventory().addItemDrop(UNCUT_GEMS[Utils.random(UNCUT_GEMS.length)], 1);
		}
	}

	private boolean hasMiningSuit(Player player) {
		if (player.getEquipment().getHatId() == 20789 && player.getEquipment().getChestId() == 20791 && player.getEquipment().getLegsId() == 20790 && player.getEquipment().getBootsId() == 20788)
			return true;
		return false;
	}

	private boolean checkRock(Player player) {
		return World.containsObjectWithId(rock, rock.getId());
	}
}
