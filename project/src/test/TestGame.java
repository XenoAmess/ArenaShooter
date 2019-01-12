package test;

import static org.junit.Assert.*;

import org.junit.Test;

import arenashooter.entities.Entity;
import arenashooter.game.Game;

public class TestGame {
	private static Game game = Game.game;
	
	@Test
	public void testConstructeur() {
		Entity player = game.getMap().children.get("Player 1");
		assertTrue(player != null);
	}
}