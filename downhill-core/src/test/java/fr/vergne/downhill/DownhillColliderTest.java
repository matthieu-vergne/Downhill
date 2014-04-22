package fr.vergne.downhill;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import fr.vergne.downhill.DownhillCollider.Collider;

public abstract class DownhillColliderTest {

	protected abstract <Ball> DownhillCollider<Ball> buildDownhillCollider();

	private class Range {
		private final int min;
		private final int max;

		public Range(int min, int max) {
			this.min = min;
			this.max = max;
		}

		@Override
		public boolean equals(Object obj) {
			if (obj == this) {
				return true;
			} else if (obj instanceof Range) {
				Range r = (Range) obj;
				return min == r.min && max == r.max;
			} else {
				return false;
			}
		}

		@Override
		public int hashCode() {
			return min + max;
		}

		@Override
		public String toString() {
			return Arrays.asList(min, max).toString();
		}
	}

	@Test
	public void testFullMergingOf4BallsWhateverTheOrder() {
		Range b1 = new Range(0, 10);
		Range b2 = new Range(5, 20);
		Range b3 = new Range(15, 30);
		Range b4 = new Range(25, 40);

		Collider<Range> collider = new Collider<Range>() {

			@Override
			public boolean areColliding(Range b1, Range b2) {
				if (b1.max < b2.min) {
					return false;
				} else if (b1.min > b2.max) {
					return false;
				} else {
					return true;
				}
			}

			@Override
			public Range collide(Range b1, Range b2) {
				return new Range(Math.min(b1.min, b2.min), Math.max(b1.max,
						b2.max));
			}

		};

		@SuppressWarnings("unchecked")
		Collection<List<Range>> sets = Arrays.asList(
				Arrays.asList(b1, b2, b3, b4), Arrays.asList(b1, b2, b4, b3),
				Arrays.asList(b1, b3, b2, b4), Arrays.asList(b1, b3, b4, b2),
				Arrays.asList(b1, b4, b2, b3), Arrays.asList(b1, b4, b3, b2),
				Arrays.asList(b2, b1, b3, b4), Arrays.asList(b2, b1, b4, b3),
				Arrays.asList(b2, b3, b1, b4), Arrays.asList(b2, b3, b4, b1),
				Arrays.asList(b2, b4, b1, b3), Arrays.asList(b2, b4, b3, b1),
				Arrays.asList(b3, b2, b1, b4), Arrays.asList(b3, b2, b4, b1),
				Arrays.asList(b3, b1, b2, b4), Arrays.asList(b3, b1, b4, b2),
				Arrays.asList(b3, b4, b2, b1), Arrays.asList(b3, b4, b1, b2),
				Arrays.asList(b4, b2, b3, b1), Arrays.asList(b4, b2, b1, b3),
				Arrays.asList(b4, b3, b2, b1), Arrays.asList(b4, b3, b1, b2),
				Arrays.asList(b4, b1, b2, b3), Arrays.asList(b4, b1, b3, b2));

		for (List<Range> balls : sets) {
			// check no mistake
			assertTrue(balls.contains(b1));
			assertTrue(balls.contains(b2));
			assertTrue(balls.contains(b3));
			assertTrue(balls.contains(b4));

			// check correct result
			DownhillCollider<Range> downhill = buildDownhillCollider();
			Collection<Range> merged = downhill.rolls(balls, collider);
			assertEquals("Not all merged: " + merged, 1, merged.size());
			assertEquals(balls + " > " + merged, new Range(0, 40), merged
					.iterator().next());
		}
	}

}
