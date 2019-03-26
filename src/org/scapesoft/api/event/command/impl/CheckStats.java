package org.scapesoft.api.event.command.impl;

import java.util.ArrayList;
import java.util.List;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.api.event.listeners.interfaces.Scrollable;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.game.player.Skills;
import org.scapesoft.utilities.game.player.Rights;

/**
 * @author Tyluur<itstyluur@gmail.com>
 * @since Jan 10, 2015
 */
public class CheckStats extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.MODERATOR;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "checkstats" };
	}

	@Override
	public void execute(Player player) {
		String name = getCompleted(cmd, 1).replaceAll(" ", "_");
		if (World.containsPlayer(name)) {
			Player target = World.getPlayer(name);
			List<String> messages = new ArrayList<String>();
			messages.add(target.getUsername() + "'s stats: ");
			for (int i = 0; i < Skills.SKILL_NAME.length; i++) {
				messages.add(Skills.SKILL_NAME[i] + ": (" + player.getSkills().getLevel(i) + "/" + player.getSkills().getLevelForXp(i) + ")");
			}
			Scrollable.sendQuestScroll(player, name + "'s stats", messages.toArray(new String[messages.size()]));
			
		} else {
			player.sendMessage(name + " is an invalid name for a player.");
		}
	}

}
