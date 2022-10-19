
public abstract class GameOver implements Information {
	
	// ABSTRACT class used when the player has lost
	
	private static boolean gameOver = false;
	
	public static void setGameOver(boolean gameOver) {
		if(gameOver) {
			// If the player has lost, we add the sound
			new Sound("gameover.wav");
		}
		GameOver.gameOver = gameOver;
	}
	
	public static boolean getGameOver() {
		return gameOver;
	}
}
