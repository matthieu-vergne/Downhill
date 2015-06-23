package fr.vergne.downhill.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import fr.vergne.downhill.DownhillCollider;

/**
 * The {@link WaveDownhillCollider} aims at creating {@link Ball}s by waves: all
 * the {@link Ball}s available at an iteration <code>t</code> are collided (when
 * possible) to create new {@link Ball}s (the ones already known are discarded)
 * for <code>t+1</code>. Then all the {@link Ball}s at <code>t+1</code> are used
 * to create the ones at <code>t+2</code>, etc. Finally, when no new
 * {@link Ball} is created, the last non-empty wave is returned.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Ball>
 */
public class WaveDownhillCollider<Ball> implements DownhillCollider<Ball> {

	private final boolean isOrderIndependent;

	public WaveDownhillCollider() {
		this(false);
	}

	/**
	 * The argument allows to optimize the process by reducing the number of
	 * evaluations: if evaluating two {@link Ball}s <code>b1</code> and
	 * <code>b2</code> gives exactly the same result independently of their
	 * order ( <code>areColliding(b1,b2) == areColliding(b2,b1)</code> and
	 * <code>collides(b1,b2) == collides(b2,b1)</code>) then you can switch the
	 * argument to <code>true</code> to evaluate only one order. If the order
	 * matter (or if you don't know), you can set it the argument to
	 * <code>false</code> to ask the evaluation of both the orders. The default
	 * case ({@link #WaveDownhillCollider()}) set the argument to
	 * <code>false</code>.
	 * 
	 * @param isOrderIndependent
	 *            if <code>true</code>, then only one couple of ball (b1, b2) is
	 *            collided, otherwise we also collide (b2, b1).
	 */
	public WaveDownhillCollider(boolean isOrderIndependent) {
		this.isOrderIndependent = isOrderIndependent;
	}

	@Override
	public Collection<Ball> rolls(Collection<Ball> balls,
			Collider<Ball>... colliders) {
		Collection<Ball> result;
		if (isOrderIndependent) {
			result = rollsIndependent(balls, colliders);
		} else {
			result = rollsDependent(balls, colliders);
		}
		return new ReductionDownhillCollider<Ball>().rolls(result, colliders);
	}

	public Collection<Ball> rollsDependent(Collection<Ball> balls,
			Collider<Ball>... colliders) {
		Collection<Ball> oldBalls = null;
		Collection<Ball> newBalls = new HashSet<Ball>(balls);
		do {
			oldBalls = newBalls;
			newBalls = new HashSet<Ball>();
			for (Ball b1 : oldBalls) {
				for (Ball b2 : oldBalls) {
					for (Collider<Ball> collider : colliders) {
						if (collider.areColliding(b1, b2)) {
							Ball collided = collider.collide(b1, b2);
							newBalls.add(collided);
						} else {
							// not able to merge them
						}
					}
				}
			}
			newBalls.removeAll(oldBalls);
		} while (!newBalls.isEmpty());
		return oldBalls;
	}

	public Collection<Ball> rollsIndependent(Collection<Ball> balls,
			Collider<Ball>... colliders) {
		Collection<Ball> oldBalls = null;
		Collection<Ball> newBalls = new HashSet<Ball>(balls);
		do {
			oldBalls = newBalls;
			newBalls = new HashSet<Ball>();

			LinkedList<Ball> remaining = new LinkedList<Ball>(oldBalls);
			while (!remaining.isEmpty()) {
				Ball b1 = remaining.removeFirst();
				for (Ball b2 : remaining) {
					for (Collider<Ball> collider : colliders) {
						if (collider.areColliding(b1, b2)) {
							Ball collided = collider.collide(b1, b2);
							newBalls.add(collided);
						} else {
							// not able to merge them
						}
					}
				}
			}
			newBalls.removeAll(oldBalls);
		} while (!newBalls.isEmpty());
		return oldBalls;
	}

}
