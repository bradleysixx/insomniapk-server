package org.scapesoft.api.event.command.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.scapesoft.api.event.command.CommandHandler;
import org.scapesoft.api.event.command.CommandSkeleton;
import org.scapesoft.api.event.listeners.interfaces.Scrollable;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.Rights;
import org.scapesoft.utilities.misc.ChatColors;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 23, 2014
 */
public class CommandList extends CommandSkeleton {

	@Override
	public Rights getRightsRequired() {
		return Rights.PLAYER;
	}

	@Override
	public String[] getCommandApplicable() {
		return new String[] { "commands" };
	}

	@Override
	public void execute(Player player) {
		try {
			List<CommandSkeleton> list = CommandHandler.get().getAvailableCommands(player);
			Collections.sort(list, new Comparator<CommandSkeleton>() {

				@Override
				public int compare(CommandSkeleton x, CommandSkeleton y) {
					int startComparison = CommandList.compare(x.getRightsRequired().ordinal(), y.getRightsRequired().ordinal());
					return startComparison != 0 ? startComparison : CommandList.compare(x.getRightsRequired().ordinal(), y.getRightsRequired().ordinal());
				}

			});
			ListIterator<CommandSkeleton> it$ = list.listIterator();
			CopyOnWriteArrayList<String> messageList = new CopyOnWriteArrayList<String>();
			CopyOnWriteArrayList<String> contained = new CopyOnWriteArrayList<String>();
			while (it$.hasNext()) {
				CommandSkeleton entry = it$.next();
				if (contained.contains(entry.getClass().getName())) {
					continue;
				}
				StringBuilder params = new StringBuilder();
				int rightReq = entry.getRightsRequired().ordinal();
				String group = entry.getRightsRequired().name();
				String color = rightReq == 3 ? ChatColors.RED : rightReq == 2 ? ChatColors.BLUE : rightReq == 1 ? ChatColors.WHITE : ChatColors.MAROON;

				for (int i = 0; i < entry.getCommandApplicable().length; i++) {
					params.append(entry.getCommandApplicable()[i] + "" + (i == entry.getCommandApplicable().length - 1 ? "" : ", "));
				}
				messageList.add((entry.getClass().getSimpleName()) + "[<col=" + color + ">" + group + "</col>] ::" + params);
				contained.add(entry.getClass().getName());
			}
			Collections.reverse(messageList);
			Scrollable.sendQuestScroll(player, "Commands", messageList.toArray(new String[messageList.size()]));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static int compare(long a, long b) {
		return a < b ? -1 : a > b ? 1 : 0;
	}

}
