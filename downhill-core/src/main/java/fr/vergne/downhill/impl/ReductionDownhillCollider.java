package fr.vergne.downhill.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import fr.vergne.downhill.DownhillCollider;

/**
 * The {@link ReductionDownhillCollider} aims at reducing the {@link Ball}s
 * received in a forceful way. Thus, once a {@link Ball} is created based on two
 * other {@link Ball}s, only the created one is kept, the two initial
 * {@link Ball}s begin discarded. This process is repeated iteratively until no
 * new {@link Ball} can be created. At the end of the colliding, only the
 * {@link Ball}s which have not been "consumed" to create new ones are returned.<br/>
 * <br/>
 * This implementation is particularly suited for cases where a {@link Ball}
 * <code>a</code> can be collided with a {@link Ball} <code>b</code> implies
 * that <code>a</code> can also be collided with any {@link Ball} coming from a
 * collision with <code>b</code>. On the opposite, it is totally inadapted for
 * cases where the same {@link Ball} should be reused for other collisions.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Ball>
 */
public class ReductionDownhillCollider<Ball> implements DownhillCollider<Ball> {

	@Override
	public Collection<Ball> rolls(Collection<Ball> balls,
			Collider<Ball>... colliders) {
		LinkedList<Ball> remain = new LinkedList<Ball>(balls);
		boolean collisionOccurred;
		do {
			collisionOccurred = false;
			LinkedList<Ball> merged = new LinkedList<Ball>();
			while (!remain.isEmpty()) {
				Ball rollingBall = remain.removeFirst();
				Iterator<Ball> iterator = remain.iterator();
				while (iterator.hasNext()) {
					Ball ball = iterator.next();
					for (Collider<Ball> collider : colliders) {
						if (collider.areColliding(rollingBall, ball)) {
							rollingBall = collider.collide(rollingBall, ball);
							iterator.remove();
							collisionOccurred = true;
						} else {
							continue;
						}
					}
				}
				merged.add(rollingBall);
			}
			remain = merged;
		} while (collisionOccurred);
		return remain;
	}

}
