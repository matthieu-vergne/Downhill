package fr.vergne.downhill.impl;

import fr.vergne.downhill.DownhillCollider;
import fr.vergne.downhill.DownhillColliderTest;

public class ReuseDownhillColliderTest extends DownhillColliderTest {

	@Override
	protected <Ball> DownhillCollider<Ball> buildDownhillCollider() {
		return new ReuseDownhillCollider<Ball>(false);
	}
}
