package org.scapesoft.game.player.actions;

import org.scapesoft.game.player.Player;
import org.scapesoft.game.route.RouteFinder;
import org.scapesoft.game.route.strategy.EntityStrategy;
import org.scapesoft.utilities.misc.Utils;

public class PlayerFollow extends Action {

	private Player target;

	public PlayerFollow(Player target) {
		this.target = target;
	}

	@Override
	public boolean start(Player player) {
		player.setNextFaceEntity(target);
		if (checkAll(player)) {
			return true;
		}
		player.setNextFaceEntity(null);
		return false;
	}

	private boolean checkAll(Player player) {
		if (player.isDead() || player.hasFinished() || target.isDead() || target.hasFinished()) {
			return false;
		}
		int distanceX = player.getX() - target.getX();
		int distanceY = player.getY() - target.getY();
		int size = target.getSize();
		int maxDistance = 16;
		if (player.getPlane() != target.getPlane() || distanceX > size + maxDistance || distanceX < -1 - maxDistance || distanceY > size + maxDistance || distanceY < -1 - maxDistance) {
			return false;
		}
		if (player.getFreezeDelay() >= Utils.currentTimeMillis()) {
			return true;
		}
		int lastFaceEntity = target.getLastFaceEntity();
		if (lastFaceEntity == player.getClientIndex() && target.getActionManager().getAction() instanceof PlayerFollow)
			player.addWalkSteps(target.getX(), target.getY());
		else if (!player.clipedProjectile(target, true) || !Utils.isInRange(player.getX(), player.getY(), size, target.getX(), target.getY(), target.getSize(), 0)) {
			int steps = RouteFinder.findRoute(RouteFinder.WALK_ROUTEFINDER, player.getX(), player.getY(), player.getPlane(), player.getSize(), new EntityStrategy(target), true);
			if (steps == -1)
				return false;

			if (steps > 0) {
				player.resetWalkSteps();

				int[] bufferX = RouteFinder.getLastPathBufferX();
				int[] bufferY = RouteFinder.getLastPathBufferY();
				for (int step = steps - 1; step >= 0; step--) {
					if (!player.addWalkSteps(bufferX[step], bufferY[step], 25, true))
						break;
				}
			}
			return true;
		}
		return true;
	}

	@Override
	public boolean process(Player player) {
		return checkAll(player);
	}

	@Override
	public int processWithDelay(Player player) {
		return 0;
	}

	@Override
	public void stop(final Player player) {
		player.setNextFaceEntity(null);
	}

}
