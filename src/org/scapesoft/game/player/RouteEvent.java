package org.scapesoft.game.player;

import org.scapesoft.game.Entity;
import org.scapesoft.game.WorldObject;
import org.scapesoft.game.WorldTile;
import org.scapesoft.game.item.FloorItem;
import org.scapesoft.game.route.RouteFinder;
import org.scapesoft.game.route.RouteStrategy;
import org.scapesoft.game.route.strategy.EntityStrategy;
import org.scapesoft.game.route.strategy.FixedTileStrategy;
import org.scapesoft.game.route.strategy.FloorItemStrategy;
import org.scapesoft.game.route.strategy.ObjectStrategy;

public class RouteEvent {

	/**
	 * Object to which we are finding the route.
	 */
	private Object object;
	/**
	 * The event instance.
	 */
	private Runnable event;
	/**
	 * Whether we also run on alternative.
	 */
	private boolean alternative;
	/**
	 * Contains last route strategies.
	 */
	private RouteStrategy[] last;

	public RouteEvent(Object object, Runnable event) {
		this(object, event, false);
	}

	public RouteEvent(Object object, Runnable event, boolean alternative) {
		this.object = object;
		this.event = event;
		this.alternative = alternative;
	}

	public boolean processEvent(final Player player) {
		if (!simpleCheck(player)) {
			player.getPackets().sendGameMessage("You can't reach that.");
			player.getPackets().sendResetMinimapFlag();
			return true;
		}
		RouteStrategy[] strategies = generateStrategies();
		if (object instanceof Entity) {
			player.setNextFaceEntity(((Entity) object));
		}
		if (last != null && match(strategies, last) && player.hasWalkSteps()) {
			return false;
		} else if (last != null && match(strategies, last) && !player.hasWalkSteps()) {
			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1) {
					continue;
				}
				if ((!RouteFinder.lastIsAlternative() && steps <= 0) || alternative) {
					player.setNextFaceEntity(null);
					if (alternative) {
						player.getPackets().sendResetMinimapFlag();
					}
					event.run();
					return true;
				}
			}

			player.getPackets().sendGameMessage("You can't reach that.");
			player.getPackets().sendResetMinimapFlag();
			return true;
		} else {
			last = strategies;

			for (int i = 0; i < strategies.length; i++) {
				RouteStrategy strategy = strategies[i];
				int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(), player.getSize(), strategy, i == (strategies.length - 1));
				if (steps == -1) {
					continue;
				}
				if ((!RouteFinder.lastIsAlternative() && steps <= 0)) {
					if (alternative) {
						player.getPackets().sendResetMinimapFlag();
					}
					event.run();
					return true;
				}
				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();

				WorldTile last = new WorldTile(bufferX[0], bufferY[0], player.getPlane());
				player.resetWalkSteps();
				player.getPackets().sendMinimapFlag(last.getLocalX(player.getLastLoadedMapRegionTile(), player.getMapSize()), last.getLocalY(player.getLastLoadedMapRegionTile(), player.getMapSize()));
				if (player.isFrozen()) {
					return false;
				}
				for (int step = steps - 1; step >= 0; step--) {
					if (!player.addWalkSteps(bufferX[step], bufferY[step], 25, true)) {
						break;
					}
				}

				return false;
			}

			player.getPackets().sendGameMessage("You can't reach that.");
			player.getPackets().sendResetMinimapFlag();
			return true;
		}
	}

	private boolean simpleCheck(Player player) {
		if (object instanceof Entity) {
			return player.getPlane() == ((Entity) object).getPlane();
		} else if (object instanceof WorldObject) {
			return player.getPlane() == ((WorldObject) object).getPlane();
		} else if (object instanceof FloorItem) {
			return player.getPlane() == ((FloorItem) object).getTile().getPlane();
		} else if (object instanceof WorldTile) {
			return player.getPlane() == ((WorldTile) object).getPlane();
		} else {
			throw new RuntimeException(object + " is not instanceof any reachable entity.");
		}
	}

	private RouteStrategy[] generateStrategies() {
		if (object instanceof Entity) {
			return new RouteStrategy[] { new EntityStrategy((Entity) object) };
		} else if (object instanceof WorldObject) {
			return new RouteStrategy[] { new ObjectStrategy((WorldObject) object) };
		} else if (object instanceof FloorItem) {
			FloorItem item = (FloorItem) object;
			return new RouteStrategy[] { new FixedTileStrategy(item.getTile().getX(), item.getTile().getY()), new FloorItemStrategy(item) };
		} else if (object instanceof WorldTile) {
			WorldTile loc = (WorldTile) object;
			return new RouteStrategy[] { new FixedTileStrategy(loc.getX(), loc.getY()), new FixedTileStrategy(loc.getX(), loc.getY()) };
		} else {
			throw new RuntimeException(object + " is not instanceof any reachable entity.");
		}
	}

	private boolean match(RouteStrategy[] a1, RouteStrategy[] a2) {
		if (a1.length != a2.length) {
			return false;
		}
		for (int i = 0; i < a1.length; i++) {
			if (!a1[i].equals(a2[i])) {
				return false;
			}
		}
		return true;
	}

}