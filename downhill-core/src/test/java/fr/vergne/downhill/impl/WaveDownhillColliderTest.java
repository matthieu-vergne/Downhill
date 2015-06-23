package fr.vergne.downhill.impl;

import fr.vergne.downhill.DownhillCollider;
import fr.vergne.downhill.DownhillColliderTest;

public class WaveDownhillColliderTest extends DownhillColliderTest {

	@Override
	protected <Ball> DownhillCollider<Ball> buildDownhillCollider() {
		return new WaveDownhillCollider<Ball>(false);
	}
}
