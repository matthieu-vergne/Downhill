package fr.vergne.downhill;

import java.util.Collection;

/**
 * A {@link DownhillCollider} aims at merging elements together depending on
 * some similarity measures. A close description can be found with <a
 * href="https://en.wikipedia.org/wiki/Snowball_sampling">Snowball Sampling</a>,
 * yet this is not exactly what we do here. However, the metaphor is the same:
 * little <i>snowballs</i> are rolling downhill and merge together when they
 * collide (touch each other). At the end, one or several big <i>snowballs</i>
 * remain, which are completely separated and so could not collide.<br/>
 * <br/>
 * It is not a clustering algorithm, in the sense that it does not <i>group</i>
 * the elements in several sets, but <i>merge</i> them together. It can be used
 * as such if the <i>balls</i> are actually sets, which means that these sets
 * will be merged together, producing a clustering. But it is a specific
 * algorithm which can manage only certain types of clustering, not all of them.<br/>
 * 
 * @author Matthieu Vergne <vergne@fbk.eu>
 * 
 * @param <Ball>
 */
public interface DownhillCollider<Ball> {

	/**
	 * 
	 * @param balls
	 *            the balls to roll downhill
	 * @param colliders
	 *            the set of collision rules
	 * @return the set of balls remaining when no merging possibility remains
	 */
	public Collection<Ball> rolls(Collection<Ball> balls,
			Collider<Ball>... colliders);

	/**
	 * A {@link Collider} describe how collision are managed.
	 * 
	 * @author Matthieu Vergne <vergne@fbk.eu>
	 * 
	 * @param <Ball>
	 */
	public static interface Collider<Ball> {
		/**
		 * 
		 * @param b1
		 *            a ball
		 * @param b2
		 *            another ball
		 * @return <code>true</code> if the balls are colliding,
		 *         <code>false</code> otherwise
		 */
		public boolean areColliding(Ball b1, Ball b2);

		/**
		 * 
		 * @param b1
		 *            a ball
		 * @param b2
		 *            another ball
		 * @return the merging of the two balls in a single one
		 */
		public Ball collide(Ball b1, Ball b2);
	}
}
