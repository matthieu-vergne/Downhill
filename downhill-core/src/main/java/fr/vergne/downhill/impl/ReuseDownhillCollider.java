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

	private final boolean isConsumedKept;

	/**
	 * Create an instance of {@link ReuseDownhillCollider}. The argument allows
	 * to tell which {@link Ball}s to return: if set to <code>true</code>, all
	 * the {@link Ball}s should be returned, including the initial ones. If it
	 * is <code>false</code>, only the ones which have not been used to create
	 * new {@link Ball}s should be returned.<br/>
	 * <br/>
	 * Because <code>true</code> returns the full set of {@link Ball}s and
	 * <code>false</code> returns only the final {@link Ball}s, you can get the
	 * intermediaries (the ones which have been actually used to generate new
	 * {@link Ball}s) by taking the full set (<code>true</code>) and removing
	 * the final set (<code>false</code>) as well as the initial {@link Ball}s
	 * (provided at the start).
	 * 
	 * @param isConsumedKept
	 *            <code>true</code> if all the {@link Ball}s should be returned,
	 *            <code>false</code> if only the final ones should be returned
	 */
	public ReuseDownhillCollider(boolean isConsumedKept) {
		this.isConsumedKept = isConsumedKept;
	}

	public ReuseDownhillCollider() {
		this(true);
	}

	@Override
	public Collection<Ball> rolls(Collection<Ball> balls,
			Collider<Ball>... colliders) {
		Collection<Ball> oldBalls = new HashSet<Ball>(balls);
		Collection<Ball> newBalls = oldBalls;
		Collection<Ball> consumed = new HashSet<Ball>();
		do {
			Collection<Ball> merged = new LinkedList<Ball>();
			for (Ball b1 : oldBalls) {
				for (Ball b2 : newBalls) {
					for (Collider<Ball> collider : colliders) {
						if (collider.areColliding(b1, b2)) {
							Ball collided = collider.collide(b1, b2);
							if (collided.equals(b1) || collided.equals(b2)) {
								// already made, do not consider it
							} else {
								consumed.add(b1);
								consumed.add(b2);
								merged.add(collided);
							}
						} else {
							// not able to merge them
						}
					}
				}
			}
			merged.removeAll(oldBalls);

			oldBalls.addAll(newBalls);
			newBalls = merged;
		} while (!newBalls.isEmpty());

		if (isConsumedKept) {
			// keep all the balls
		} else {
			oldBalls.removeAll(consumed);
		}
		return oldBalls;
	}

}
