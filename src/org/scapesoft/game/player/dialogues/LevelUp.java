package org.scapesoft.game.player.dialogues;

import org.scapesoft.game.Graphics;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;

public final class LevelUp extends Dialogue {

	public static final int[] SKILL_LEVEL_UP_MUSIC_EFFECTS = { 37, 37, 37, 37, 37, -1, 37, -1, 39, -1, -1, -1, -1, -1, 53, -1, -1, -1, -1, -1, -1, -1, -1, 300, 417 };
	public static final int SKILL_ICON[] = { 0x5f5e100, 0x17d78400, 0xbebc200, 0x1ad27480, 0xee6b280, 0x1dcd6500, 0x11e1a300, 0x4190ab00, 0x4a817c80, 0x4d7c6d00, 0x3e95ba80, 0x47868c00, 0x2faf0800, 0x3b9aca00, 0x35a4e900, 0x26be3680, 0x23c34600, 0x29b92700, 0x53724e00, 0x566d3e80, 0x32a9f880, 0x59682f00, 0x5f5e1000, 0x62590080, 0x6553f100 };
	public static final int[] CONFIG_VALUES = { 1, 5, 2, 6, 3, 7, 4, 16, 18, 19, 15, 17, 11, 14, 13, 9, 8, 10, 20, 21, 12, 23, 22, 24, 25 };
	
	private int skill;

	@Override
	public void start() {
		skill = (Integer) parameters[0];
		int level = player.getSkills().getLevelForXp(skill);
		player.getTemporaryAttributtes().put("leveledUp", skill);
		player.getTemporaryAttributtes().put("leveledUp[" + skill + "]", Boolean.TRUE);
		player.setNextGraphics(new Graphics(199));
		player.getInterfaceManager().sendChatBoxInterface(740);
		
		String name = Skills.SKILL_NAME[skill];
		player.getPackets().sendIComponentText(740, 0, "Congratulations, you have just advanced a" + (name.startsWith("A") ? "n" : "") + " " + name + " level!");
		player.getPackets().sendIComponentText(740, 1, "You have now reached level " + level + ".");
		player.getPackets().sendGameMessage("You've just advanced a" + (name.startsWith("A") ? "n" : "") + " " + name + " level! You have reached level " + level + ".");
		
		player.getPackets().sendConfig(1179, CONFIG_VALUES[skill] << 26);
		switchFlash(player, skill, true);
		
		int musicEffect = SKILL_LEVEL_UP_MUSIC_EFFECTS[skill];
		if (musicEffect != -1) {
			player.getPackets().sendMusicEffect(musicEffect);
		}
		if (level == 99 || level == 120) {
			sendNews(player, skill, level);
		}
	}

	public static void sendNews(Player player, int skill, int level) {
		boolean reachedAll = true;
		for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
			if (player.getSkills().getLevelForXp(i) < 99) {
				reachedAll = false;
				break;
			}
		}
		String publicMessage;
		if (!reachedAll) {
			publicMessage = player.getDisplayName() + " has achieved " + level + " " + Skills.SKILL_NAME[skill] + ".";
		} else if (player.getSkills().getLevelForXp(Skills.DUNGEONEERING) == 120)
			publicMessage = "" + player.getDisplayName() + " has been awarded the Completionist Cape!";
		else
			publicMessage = "" + player.getDisplayName() + " has just achieved at least level 99 in all skills!";
		World.sendWorldMessage("<col=" + player.getAppearence().getRatesTag() + ">" + publicMessage, false, false);
	}

	public static int getIconValue(int skill) {
		if (skill == Skills.ATTACK) {
			return 1;
		}
		if (skill == Skills.STRENGTH) {
			return 2;
		}
		if (skill == Skills.RANGE) {
			return 3;
		}
		if (skill == Skills.MAGIC) {
			return 4;
		}
		if (skill == Skills.DEFENCE) {
			return 5;
		}
		if (skill == Skills.HITPOINTS) {
			return 6;
		}
		if (skill == Skills.PRAYER) {
			return 7;
		}
		if (skill == Skills.AGILITY) {
			return 8;
		}
		if (skill == Skills.HERBLORE) {
			return 9;
		}
		if (skill == Skills.THIEVING) {
			return 10;
		}
		if (skill == Skills.CRAFTING) {
			return 11;
		}
		if (skill == Skills.RUNECRAFTING) {
			return 12;
		}
		if (skill == Skills.MINING) {
			return 13;
		}
		if (skill == Skills.SMITHING) {
			return 14;
		}
		if (skill == Skills.FISHING) {
			return 15;
		}
		if (skill == Skills.COOKING) {
			return 16;
		}
		if (skill == Skills.FIREMAKING) {
			return 17;
		}
		if (skill == Skills.WOODCUTTING) {
			return 18;
		}
		if (skill == Skills.FLETCHING) {
			return 19;
		}
		if (skill == Skills.SLAYER) {
			return 20;
		}
		if (skill == Skills.FARMING) {
			return 21;
		}
		if (skill == Skills.CONSTRUCTION) {
			return 22;
		}
		if (skill == Skills.HUNTER) {
			return 23;
		}
		if (skill == Skills.SUMMONING) {
			return 24;
		}
		return 26;
	}

	public static void switchFlash(Player player, int skill, boolean on) {
		int id;
		if (skill == Skills.ATTACK) {
			id = 4732;
		} else if (skill == Skills.STRENGTH) {
			id = 4733;
		} else if (skill == Skills.DEFENCE) {
			id = 4734;
		} else if (skill == Skills.RANGE) {
			id = 4735;
		} else if (skill == Skills.PRAYER) {
			id = 4736;
		} else if (skill == Skills.MAGIC) {
			id = 4737;
		} else if (skill == Skills.HITPOINTS) {
			id = 4738;
		} else if (skill == Skills.AGILITY) {
			id = 4739;
		} else if (skill == Skills.HERBLORE) {
			id = 4740;
		} else if (skill == Skills.THIEVING) {
			id = 4741;
		} else if (skill == Skills.CRAFTING) {
			id = 4742;
		} else if (skill == Skills.FLETCHING) {
			id = 4743;
		} else if (skill == Skills.MINING) {
			id = 4744;
		} else if (skill == Skills.SMITHING) {
			id = 4745;
		} else if (skill == Skills.FISHING) {
			id = 4746;
		} else if (skill == Skills.COOKING) {
			id = 4747;
		} else if (skill == Skills.FIREMAKING) {
			id = 4748;
		} else if (skill == Skills.WOODCUTTING) {
			id = 4749;
		} else if (skill == Skills.RUNECRAFTING) {
			id = 4750;
		} else if (skill == Skills.SLAYER) {
			id = 4751;
		} else if (skill == Skills.FARMING) {
			id = 4752;
		} else if (skill == Skills.CONSTRUCTION) {
			id = 4753;
		} else if (skill == Skills.HUNTER) {
			id = 4754;
		} else if (skill == Skills.SUMMONING) {
			id = 4755;
		} else {
			id = 7756;
		}
		player.getPackets().sendConfigByFile(id, on ? 1 : 0);
	}

	@Override
	public void run(int interfaceId, int componentId) {
		end();
	}

	@Override
	public void finish() {
		//player.getPackets().sendConfig(1179, SKILL_ICON[skill]); //removes
		//random flash
	}
}
