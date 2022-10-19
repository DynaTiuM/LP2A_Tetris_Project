import java.awt.*;
import java.io.File;

public class Grid implements Information, Colors {
	// This class is dedicated to the display of the grid
	// There are 3 methods :
	
	// The first one which draw the background of the game
	public void drawBackground(Graphics g) {
		g.setColor(GRAY);	// put gray color
		g.fillRect(0, 0, 10*UNIT_SIZE, GAME_WIDTH);	//making a gray rectangle
		g.setColor(PASTEL_GOLD);
		g.fillRect(10*UNIT_SIZE, 0, UNIT_SIZE/8, GAME_WIDTH);
	}
	
	// Second one that draw the lines of the grid
	public void draw(Graphics g) {
		g.setColor(TRANSPARENT_GOLD);
		for(int i = 0; i <= HOR_CASES; i++) {		// Display of the lines horizontally
			g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, GAME_WIDTH);
		}
		for(int i = 0; i <= VER_CASES; i++) {		// Display of the lines vertically
			g.drawLine(0, i*UNIT_SIZE, UNIT_SIZE*HOR_CASES, i*UNIT_SIZE);
		}
	}
	
	// Third one that it called when the player has lost or has set the game to pause
	public void draw(Graphics g, String s, int p, float dimension) throws Exception {
		g.setColor(TRANSPARENT_BLACK);
		g.fillRect(0, 0, HOR_CASES*UNIT_SIZE, GAME_WIDTH);

		// We create a new font
		Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File(TEXTURES_PATH,"Urbanist-SemiBold.ttf")).deriveFont(dimension);
		g.setFont(customFont);
		
		g.setColor(WHITE);
		g.drawString(s, p, GAME_WIDTH/2+UNIT_SIZE);
	}
	
}
