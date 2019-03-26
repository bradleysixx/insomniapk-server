package org.scapesoft.api.event.command.impl;

import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.game.World;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.console.gson.extra.Punishment.PunishmentType;
import org.scapesoft.utilities.console.gson.impl.PunishmentLoader;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.game.player.ForumGroup.ForumGroups;
import org.scapesoft.utilities.misc.ChatColors;
import org.scapesoft.utilities.misc.Utils;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Apr 3, 2014
 */
public class Yell extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "shout", "yell" };
	}

	@Override
	public void execute(Player player) {
		if (PunishmentLoader.isPunished(player.getUsername(), PunishmentType.MUTE) || PunishmentLoader.isPunished(player.getSession().getIP(), PunishmentType.IPMUTE)) {
			player.sendMessage("You are muted. Check back in 48 hours.");
			return;
		}
		if (!player.isDonator() && !player.isSupporter() && player.getRights() == 0) {
			player.sendMessage("You don't have access to the yell channel.");
			return;
		}
		String message = getCompleted(cmd, 1);
		if (message == null || message.equalsIgnoreCase("null"))
			return;

		message = Utils.fixChatMessage(message.replaceAll("<", ""));

		StringBuilder tag = new StringBuilder();
		String playerTag = hasYellFeatures(player, true) ? player.getFacade().getYellTag() : Utils.formatPlayerNameForDisplay(player.getMainGroup().name());

		tag.append("[<col=" + (hasYellFeatures(player, false) ? player.getFacade().getYellColour() : ChatColors.MAROON) + ">" + playerTag + "</col>]");
		tag.append(" <img=" + player.getChatIcon() + ">");
		tag.append(player.getDisplayName() + ": " + message);
		for (Player pl : World.getPlayers()) {
			if (pl == null || pl.getFacade().hasYellOff()) {
				continue;
			}
			pl.sendMessage(tag.toString());
		}

	}

	private boolean hasYellFeatures(Player player, boolean tag) {
		if (!player.isInGroup(ForumGroups.OWNER) && !player.isInGroup(ForumGroups.DIAMOND_MEMBER)) {
			return false;
		}
		if (tag) {
			return player.getFacade().getYellTag() != null;
		} else {
			return player.getFacade().getYellColour() != null;
		}
	}
}
