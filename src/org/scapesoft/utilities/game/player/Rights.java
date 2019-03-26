package org.scapesoft.utilities.game.player;

import org.scapesoft.Constants;
import org.scapesoft.game.player.Player;
import org.scapesoft.utilities.game.player.ForumGroup.ForumGroups;

/**
 * The enumeration for all the player rights. The ordinal value is their rights
 * value to be sent in chat
 *
 * @author Tyluur <itstyluur@gmail.com>
 * @since Mar 21, 2014
 */
public enum Rights {

	PLAYER {
		@Override
		public boolean hasRight(Player player) {
			return true;
		}
	},
	SILVER {
		@Override
		public boolean hasRight(Player player) {
			return player.isInGroup(ForumGroups.SILVER_MEMBER);
		}
	},
	GOLD {
		@Override
		public boolean hasRight(Player player) {
			return player.isInGroup(ForumGroups.GOLD_MEMBER);
		}
	},
	DIAMOND {
		@Override
		public boolean hasRight(Player player) {
			return player.isInGroup(ForumGroups.DIAMOND_MEMBER);
		}
	},
	PLATINUM {
		@Override
		public boolean hasRight(Player player) {
			return player.isInGroup(ForumGroups.PLATINUM_MEMBER);
		}
	},
	SUPPORT {
		@Override
		public boolean hasRight(Player player) {
			return player.isInGroup(ForumGroups.SUPPORT) || player.getRights() > 0;
		}
	},
	MODERATOR {
		@Override
		public boolean hasRight(Player player) {
			return ADMINISTRATOR.hasRight(player) || player.getRights() == 1 || player.isInGroup(ForumGroups.OWNER, ForumGroups.ADMINISTRATOR, ForumGroups.SERVER_MODERATOR);
		}
	},
	ADMINISTRATOR {
		@Override
		public boolean hasRight(Player player) {
			return OWNER.hasRight(player) || player.isInGroup(ForumGroups.ADMINISTRATOR);
		}
	},
	OWNER {
		@Override
		public boolean hasRight(Player player) {
			if (Constants.DEBUG && player.getRights() == 8)
				return true;
			return player.isInGroup(ForumGroups.OWNER);
		}
	};

	public abstract boolean hasRight(Player player);

}