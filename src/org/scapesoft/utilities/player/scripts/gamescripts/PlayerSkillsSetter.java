package org.scapesoft.utilities.player.scripts.gamescripts;

import java.io.File;
import java.io.IOException;

import org.scapesoft.cache.Cache;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.utilities.misc.Config;
import org.scapesoft.utilities.player.Saving;
import org.scapesoft.utilities.player.scripts.GameScript;

/**
 * @author Lazarus <lazarus.rs.king@gmail.com>
 * @since Jul 30, 2014
 */
public class PlayerSkillsSetter extends GameScript {

	public static void main(String... args) {
		try {
			Cache.init();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Config.get().load();
		for (File acc : getAccounts()) {
			try {
				Player player = (Player) Saving.loadSerializedFile(acc);
				if (acc.getName().equalsIgnoreCase(name)) {
					set(player, Skills.ATTACK, 99);
					set(player, Skills.STRENGTH, 99);
					set(player, Skills.DEFENCE, 99);
					set(player, Skills.HITPOINTS, 99);
					set(player, Skills.MAGIC, 99);
					set(player, Skills.PRAYER, 99);
					set(player, Skills.RANGE, 99);
					set(player, Skills.SUMMONING, 99);
					set(player, Skills.AGILITY, 99);
					set(player, Skills.HERBLORE, 99);
					set(player, Skills.CRAFTING, 99);
					set(player, Skills.WOODCUTTING, 99);
					set(player, Skills.FIREMAKING, 99);
					set(player, Skills.COOKING, 99);
					set(player, Skills.FISHING, 99);
					set(player, Skills.MINING, 99);
					set(player, Skills.THIEVING, 76);
					set(player, Skills.SLAYER, 89);
					set(player, Skills.HUNTER, 73);
					set(player, Skills.FARMING, 68);
					set(player, Skills.RUNECRAFTING, 75);
					set(player, Skills.FLETCHING, 9);
					savePlayer(player, acc);
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void set(Player player, int skill, int lvl) {
		player.getSkills().setStat(skill, lvl);
	}

	private static final String name = "butthowl.p";
}
