package org.scapesoft.game.player.actions;

import org.scapesoft.cache.loaders.ItemDefinitions;
import org.scapesoft.game.Animation;
import org.scapesoft.game.Graphics;
import org.scapesoft.game.World;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.Item;
import org.scapesoft.game.minigames.games.GamesHandler;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.game.player.actions.Firemaking.Fire;
import org.scapesoft.game.player.content.achievements.impl.LumberJackAchievement;
import org.scapesoft.game.player.content.achievements.impl.MagicsMediumAchievement;
import org.scapesoft.game.tasks.WorldTask;
import org.scapesoft.game.tasks.WorldTasksManager;
import org.scapesoft.utilities.misc.Utils;

public final class Woodcutting extends Action {

	private static final int[] BIRD_NESTS = {5070, 5071, 5072, 5073, 5074, 5075, 7413, 11966};

	public static enum TreeDefinitions {

		NORMAL(1, 25, 1511, 20, 4, 1341, 8, 0), // TODO

		DRAMEN(36, 0, 771, 20, 4, -1, 8, 0),

		EVERGREEN(1, 25, 1511, 20, 4, 57931, 8, 0),

		DEAD(1, 25, 1511, 20, 4, 12733, 8, 0),

		OAK(15, 37.5, 1521, 30, 4, 1341, 15, 15), // TODO

		WILLOW(30, 67.5, 1519, 60, 4, 1341, 51, 15), // TODO

		MAPLE(45, 100, 1517, 83, 16, 31057, 72, 10),

		YEW(60, 175, 1515, 120, 17, 1341, 94, 10), // TODO

		IVY(68, 332.5, -1, 120, 17, 46319, 58, 10),

		MAGIC(75, 250, 1513, 150, 21, 37824, 121, 10),

		CURSED_MAGIC(82, 250, 1513, 150, 21, 37822, 121, 10),

		FRUIT_TREES(1, 25, -1, 20, 4, 1341, 8, 0),

		MUTATED_VINE(83, 140, 21358, 83, 16, -1, 72, 0),

		CURLY_VINE(83, 140, null, 83, 16, 12279, 72, 0),

		CURLY_VINE_COLLECTABLE(83, 140, new int[]{21350, 21350, 21350, 21350}, 83, 16, 12283, 72, 0),

		STRAIT_VINE(83, 140, null, 83, 16, 12277, 72, 0),

		STRAIT_VINE_COLLECTABLE(83, 140, new int[]{21349, 21349, 21349, 21349}, 83, 16, 12283, 72, 0);

		private int level;
		private double xp;
		private int[] logsId;
		private int logBaseTime;
		private int logRandomTime;
		private int stumpId;
		private int respawnDelay;
		private int randomLifeProbability;

		private TreeDefinitions(int level, double xp, int[] logsId, int logBaseTime, int logRandomTime, int stumpId, int respawnDelay, int randomLifeProbability) {
			this.level = level;
			this.xp = xp;
			this.logsId = logsId;
			this.logBaseTime = logBaseTime;
			this.logRandomTime = logRandomTime;
			this.stumpId = stumpId;
			this.respawnDelay = respawnDelay;
			this.randomLifeProbability = randomLifeProbability;
		}

		private TreeDefinitions(int level, double xp, int logsId, int logBaseTime, int logRandomTime, int stumpId, int respawnDelay, int randomLifeProbability) {
			this(level, xp, new int[]{logsId}, logBaseTime, logRandomTime, stumpId, respawnDelay, randomLifeProbability);
		}

		public int getLevel() {
			return level;
		}

		public double getXp() {
			return xp;
		}

		public int[] getLogsId() {
			return logsId;
		}

		public int getLogBaseTime() {
			return logBaseTime;
		}

		public int getLogRandomTime() {
			return logRandomTime;
		}

		public int getStumpId() {
			return stumpId;
		}

		public int getRespawnDelay() {
			return respawnDelay;
		}

		public int getRandomLifeProbability() {
			return randomLifeProbability;
		}
	}

	public enum HatchetDefinitions {

		BRONZE(1351, 1, 1, 879),

		IRON(1349, 1, 2, 877),

		STEEL(1353, 5, 3, 875),

		BLACK(1361, 11, 4, 873),

		MITHRIL(1355, 21, 5, 871),

		ADAMANT(1357, 31, 7, 869),

		RUNE(1359, 41, 10, 867),

		DRAGON(6739, 61, 13, 2846),

		INFERNO(13661, 61, 13, 10251);

		private int itemId, levelRequried, axeTime, emoteId;

		private HatchetDefinitions(int itemId, int levelRequried, int axeTime, int emoteId) {
			this.itemId = itemId;
			this.levelRequried = levelRequried;
			this.axeTime = axeTime;
			this.emoteId = emoteId;
		}

		public int getItemId() {
			return itemId;
		}

		public int getLevelRequried() {
			return levelRequried;
		}

		public int getAxeTime() {
			return axeTime;
		}

		public int getEmoteId() {
			return emoteId;
		}
	}

	private WorldObject tree;
	private TreeDefinitions definitions;
	private HatchetDefinitions hatchet;

	private boolean usingBeaver;

	public Woodcutting(WorldObject tree, TreeDefinitions definitions, boolean usingBeaver) {
		this.tree = tree;
		this.definitions = definitions;
		this.usingBeaver = usingBeaver;
	}

	public Woodcutting(WorldObject tree, TreeDefinitions definitions) {
		this(tree, definitions, false);
	}

	@Override
	public boolean start(Player player) {
		if (!checkAll(player)) {
			return false;
		}
		player.getPackets().sendGameMessage(usingBeaver ? "Your beaver uses its strong ivory teeth to chop down the tree..." : "You swing your hatchet at the " + (TreeDefinitions.IVY == definitions ? "ivy" : "tree") + "...", true);
		setActionDelay(player, getWoodcuttingDelay(player));
		return true;
	}

	private int getWoodcuttingDelay(Player player) {
		int summoningBonus = player.getFamiliar() != null ? (player.getFamiliar().getId() == 6808 || player.getFamiliar().getId() == 6807) ? 10 : 0 : 0;
		int wcTimer = definitions.getLogBaseTime() - (player.getSkills().getLevel(8) + summoningBonus) - Utils.getRandom(hatchet.axeTime);
		if (wcTimer < 1 + definitions.getLogRandomTime()) {
			wcTimer = 1 + Utils.getRandom(definitions.getLogRandomTime());
		}
		wcTimer /= player.getAuraManager().getWoodcuttingAccurayMultiplier();

		if (player.getControllerManager().getController() instanceof GamesHandler) {
			return 3;
		}
		return wcTimer;
	}

	private boolean checkAll(Player player) {
		hatchet = getHatchet(player);
		if (hatchet == null) {
			player.getPackets().sendGameMessage("You dont have the required level to use that axe or you don't have a hatchet.");
			return false;
		}
		if (!hasWoodcuttingLevel(player)) {
			return false;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return false;
		}
		return true;
	}

	public static HatchetDefinitions getHatchet(Player player) {
		HatchetDefinitions hatchet = null;
		for (HatchetDefinitions def : HatchetDefinitions.values()) {
			if ((player.getInventory().contains(def.itemId) || player.getEquipment().getWeaponId() == def.itemId) && player.getSkills().getLevelForXp(Skills.WOODCUTTING) >= def.getLevelRequried()) {
				hatchet = def;
			}
		}
		return hatchet;
	}

	private boolean hasWoodcuttingLevel(Player player) {
		if (definitions.getLevel() > player.getSkills().getLevel(Skills.WOODCUTTING)) {
			player.getPackets().sendGameMessage("You need a woodcutting level of " + definitions.getLevel() + " to chop down this tree.");
			return false;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		if (usingBeaver) {
			player.getFamiliar().setNextAnimation(new Animation(7722));
			player.getFamiliar().setNextGraphics(new Graphics(1458));
		} else {
			player.setNextAnimation(new Animation(hatchet.emoteId));
		}
		return checkTree(player);
	}

	private boolean usedDeplateAurora;

	@Override
	public int processWithDelay(Player player) {
		addLog(definitions, usingBeaver, player);
		if (!usedDeplateAurora && (1 + Math.random()) < player.getAuraManager().getChanceNotDepleteMN_WC()) {
			usedDeplateAurora = true;
		} else if (definitions.stumpId != -1 && Utils.getRandom(definitions.getRandomLifeProbability()) == 0) {
			long time = definitions.respawnDelay * 600;
			if (tree.getRegionId() != 15159) {
				World.spawnObjectTemporary(new WorldObject(definitions.getStumpId(), tree.getType(), tree.getRotation(), tree.getX(), tree.getY(), tree.getPlane()), time);
				if (tree.getPlane() < 3 && definitions != TreeDefinitions.IVY) {
					WorldObject object = World.getStandardObject(new WorldTile(tree.getX() - 1, tree.getY() - 1, tree.getPlane() + 1));
					if (object == null) {
						object = World.getStandardObject(new WorldTile(tree.getX(), tree.getY() - 1, tree.getPlane() + 1));
						if (object == null) {
							object = World.getStandardObject(new WorldTile(tree.getX() - 1, tree.getY(), tree.getPlane() + 1));
							if (object == null) {
								object = World.getStandardObject(new WorldTile(tree.getX(), tree.getY(), tree.getPlane() + 1));
							}
						}
					}
					if (object != null) {
						World.removeObjectTemporary(object, time);
					}
				}
			}
			player.setNextAnimation(new Animation(-1));
			return -1;
		}
		if (!player.getInventory().hasFreeSlots()) {
			player.setNextAnimation(new Animation(-1));
			player.getPackets().sendGameMessage("Not enough space in your inventory.");
			return -1;
		}
		return getWoodcuttingDelay(player);
	}

	public static void addLog(TreeDefinitions definitions, boolean usingBeaver, Player player) {
		if (definitions == null || definitions.getLogsId() == null || ItemDefinitions.getItemDefinitions(definitions.getLogsId()[0]) == null) {
			return;
		}
		if (!usingBeaver) {
			double xpBoost = 1.00;
			if (player.getEquipment().getChestId() == 10939) {
				xpBoost += 0.008;
			}
			if (player.getEquipment().getLegsId() == 10940) {
				xpBoost += 0.006;
			}
			if (player.getEquipment().getHatId() == 10941) {
				xpBoost += 0.004;
			}
			if (player.getEquipment().getBootsId() == 10933) {
				xpBoost += 0.002;
			}
			if (player.getEquipment().getChestId() == 10939 && player.getEquipment().getLegsId() == 10940 && player.getEquipment().getHatId() == 10941 && player.getEquipment().getBootsId() == 10933) {
				xpBoost += 0.005;
			}
			player.getSkills().addXp(8, definitions.getXp() * xpBoost);
		}
		String logName = ItemDefinitions.getItemDefinitions(definitions.getLogsId()[0]).getName().toLowerCase();
		if (definitions.getLogsId() != null) {
			boolean messaged = Boolean.FALSE;
			if (player.getActionManager().getAction() instanceof Woodcutting && ((Woodcutting) player.getActionManager().getAction()).hatchet.getItemId() == 13661 && Utils.percentageChance(30)) {
				player.sendMessage("You chop some " + logName + ". The heat of the inferno adze incinerates them.");
				Fire fire = Fire.getFireByLog(definitions.getLogsId()[0]);
				if (fire != null) {
					player.getSkills().addXp(Skills.FIREMAKING, Firemaking.increasedExperience(player, fire.getExperience()));
				}
				messaged = Boolean.TRUE;
			} else if (usingBeaver) {
				if (player.getFamiliar() != null) {
					for (int item : definitions.getLogsId()) {
						player.getInventory().addItemDrop(item, 1);
					}
				}
			} else {
				for (int item : definitions.getLogsId()) {
					player.getInventory().addItemDrop(item, 1);
				}
				if (Utils.random(50) == 0) {
					player.getInventory().addItemDrop(BIRD_NESTS[Utils.random(BIRD_NESTS.length)], 1);
					player.getPackets().sendGameMessage("A bird's nest falls out of the tree!");
				}
			}
			if (!messaged) {
				if (definitions == TreeDefinitions.IVY) {
					player.getPackets().sendGameMessage("You succesfully cut an ivy vine.", true);
				} else {
					player.getPackets().sendGameMessage("You get some " + logName + ".", true);
				}
			}
		}
		if (!(player.getControllerManager().getController() instanceof GamesHandler)) {
			if (definitions == TreeDefinitions.MAGIC)
				player.getAchievementManager().notifyUpdate(MagicsMediumAchievement.class);
			player.getAchievementManager().notifyUpdate(LumberJackAchievement.class);
		}
		if (player.getControllerManager().getController() instanceof GamesHandler) {
			GamesHandler handler = (GamesHandler) player.getControllerManager().getController();
			double points = 0;
			switch (definitions) {
				case MAGIC:
					points = 5;
					break;
				case MAPLE:
					points = 3;
					break;
				case NORMAL:
					points = 1;
					break;
				case WILLOW:
					points = 2;
					break;
				case YEW:
					points = 4;
					break;
				default:
					break;
			}
			handler.addSkillPoints(points);
		}
	}

	private boolean checkTree(Player player) {
		return World.containsObjectWithId(tree, tree.getId());
	}

	@Override
	public void stop(Player player) {
		setActionDelay(player, 3);
	}

	public static class Nest {

		public static final int[][] SEEDS = {{5312, 5283, 5284, 5313, 5285, 5286}, {5314, 5288, 5287, 5315, 5289}, {5316, 5290}, {5317}};
		private static final int[] RINGS = {1635, 1637, 1639, 1641, 1643};

		public static boolean isNest(int id) {
			return id == 5070 || id == 5071 || id == 5072 || id == 5073 || id == 5074 || id == 7413 || id == 11966;
		}

		public static void searchNest(final Player player, final int slot) {
			player.getPackets().sendGameMessage("You search the nest...and find something in it!");
			player.lock(1);
			WorldTasksManager.schedule(new WorldTask() {

				@Override
				public void run() {
					Item item = player.getInventory().getItem(slot);
					if (player != null && item != null) {
						player.getInventory().addItemDrop(getRewardForId(item.getId()), 1);
						player.getInventory().replaceItem(5075, 1, slot);
					}
				}
			});
		}

		private static int getRewardForId(int id) {
			if (id == 5070)
				return 5076;
			else if (id == 11966)
				return 11964;
			else if (id == 5071)
				return 5078;
			else if (id == 5072)
				return 5077;
			else if (id == 5074)
				return RINGS[Utils.random(RINGS.length)];
			else if (id == 7413 || id == 5073) {
				double random = Utils.random(0, 100);
				final int reward = random <= 39.69 ? 0 : random <= 56.41 ? 1 : random <= 76.95 ? 2 : random <= 96.4 ? 3 : 1;
				return SEEDS[reward][Utils.random(SEEDS[reward].length)];
			}
			return -1;
		}
	}

}
