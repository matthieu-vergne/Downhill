package fr.vergne.downhill.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import fr.vergne.downhill.DownhillCollider;

public class SimpleDownhillCollider<Ball> implements DownhillCollider<Ball> {

	@Override
	public Collection<Ball> rolls(Collection<Ball> balls,
			Collider<Ball> collider) {
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
					if (collider.areColliding(rollingBall, ball)) {
						rollingBall = collider.collide(rollingBall, ball);
						iterator.remove();
						collisionOccurred = true;
					} else {
						continue;
					}
				}
				merged.add(rollingBall);
			}
			remain = merged;
		} while (collisionOccurred);
		return remain;
	}

}
