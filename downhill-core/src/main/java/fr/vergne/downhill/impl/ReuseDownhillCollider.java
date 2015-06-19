package fr.vergne.downhill.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import fr.vergne.downhill.DownhillCollider;

/**
 * This {@link DownhillCollider} aims at using <b>all</b> the possible
 * {@link Ball}s to create new ones. Thus, each {@link Ball} is re-used as long
 * as it allows to generate new ones. At the end of the colliding, the original
 * {@link Ball}s are returned with the new ones, including the intermediaries.
 * 
 * @author Matthieu Vergne <matthieu.vergne@gmail.com>
 * 
 * @param <Ball>
 */
public class ReuseDownhillCollider<Ball> implements DownhillCollider<Ball> {

	@Override
	public Collection<Ball> rolls(Collection<Ball> balls,
			Collider<Ball> collider) {
		Collection<Ball> oldBalls = new HashSet<Ball>(balls);
		Collection<Ball> newBalls = oldBalls;
		do {
			Collection<Ball> merged = new LinkedList<Ball>();
			for (Ball b1 : oldBalls) {
				for (Ball b2 : newBalls) {
					if (collider.areColliding(b1, b2)) {
						Ball collided = collider.collide(b1, b2);
						merged.add(collided);
					} else {
						// not able to merge them
					}
				}
			}
			merged.removeAll(oldBalls);

			oldBalls.addAll(newBalls);
			newBalls = merged;
		} while (!newBalls.isEmpty());

		return oldBalls;
	}

}
