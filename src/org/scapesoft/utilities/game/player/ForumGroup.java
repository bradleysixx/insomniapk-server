package org.scapesoft.utilities.game.player;

/**
 * The forum group class
 * 
 * @author Tyluur<itstyluur@gmail.com>
 * @since December 29, 2014
 */
public class ForumGroup {
	
	
	public static void main(String[] args) {
		for (ForumGroups g : ForumGroups.values()) {
			System.out.println(g.name() + "\t" +g.ordinal());
		}
	}

	public ForumGroup(ForumGroups group) {
		this.group = group;
	}

	/**
	 * @return the groupId
	 */
	public ForumGroups getGroup() {
		return group;
	}

	private final ForumGroups group;

	@Override
	public String toString() {
		return "ForumGroup[group=" + group + "]";
	}

	public enum ForumGroups {

		OWNER(4),

		ADMINISTRATOR(7),

		GLOBAL_MODERATOR(6),

		SERVER_MODERATOR(8),

		SUPPORT(10),

		FORUM_MODERATOR(9),

		SILVER_MEMBER(11),

		GOLD_MEMBER(12),

		DIAMOND_MEMBER(13),

		PLATINUM_MEMBER(14),

		MEMBER(3),

		BANNED(5);

		ForumGroups(int id) {
			this.id = id;
		}

		private final int id;

		/**
		 * @return The forum group user id.
		 */
		public int getId() {
			return id;
		}

		public static ForumGroups getGroup(int id) {
			for (ForumGroups group : ForumGroups.values()) {
				if (group.getId() == id) {
					return group;
				}
			}
			return null;
		}
		
		public static boolean isMembershipGroup(int id) {
			ForumGroups group = getGroup(id);
			if (group == null) {
				throw new RuntimeException("Could not find forum group by id: " + id);
			}
			switch(group) {
			case DIAMOND_MEMBER:
			case GOLD_MEMBER:
			case PLATINUM_MEMBER:
			case SILVER_MEMBER:
				return true;
			default:
				return false;
			}
		}

	}

	public enum GroupType {
		MAIN, SECONDARY
	}

}