import java.awt.Dimension;

public interface Information extends Path {
	public final int GAME_WIDTH = 780;	// Width of the game
	public final Dimension SCREEN = new Dimension((int)(GAME_WIDTH*1.3),GAME_WIDTH);
	public final Dimension GAME = new Dimension(GAME_WIDTH,GAME_WIDTH);	// Dimension of the window
	public final int UNIT_SIZE = GAME_WIDTH/20; // Size of a case
	public final int HOR_CASES = 10;	// Number of cases horizontally
	public final int VER_CASES = 20;	// Number of cases vertically
}
