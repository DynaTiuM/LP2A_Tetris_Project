	import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import javax.swing.*;

@SuppressWarnings("serial")
public class GamePanel extends JPanel implements Information, ActionListener {
	private Grid grid = new Grid();
	private boolean running = false;
	private boolean pause;
	private int speed = 1;
	private Timer time;
	private LinkedList<Tetrominoe> tetrominoes;
	private WaitingRoom room = new WaitingRoom();
	private Tetrominoe tetrominoe;
	private static Tetrominoe display;
	private Score score;
	private int combo;
	private static int level;
	
	GamePanel() {
		// We allow our Panel to be manipulated with keys of the keyboard
		this.setFocusable(true);
		this.setPreferredSize(SCREEN);
		this.setBounds(0,0,GAME_WIDTH,GAME_WIDTH);
		this.setBackground(new Color(40,40,40));
		this.addKeyListener(new KeyListener());
		tetrominoes = new LinkedList<>();
	}
	
	// Traditional getter
	public static int getLevel() {
		return level;
	}
	
	// We destroy the instantiation of our class thanks to this method when we want to restart the game.
	public void destroy() {
		time.stop();
		score.destroy();
		score = null;
		time = null;
		room = null;
		GameOver.setGameOver(false);
		running = false;
		pause = false;
		removeTetrominoes();
		Music.stop();
	}
	
	// Traditional setter
	public static void setDisplay(Tetrominoe d) {
		display = d;
	}
	
	public void removeTetrominoes() {
		tetrominoes.clear();
	}

	public void start() throws Exception{
		time = new Timer((int)(1.0/speed*1000),this);
		running = true;
		pause = false;
		new Music("music-main-theme.wav");
		if(!Sound.isMuted()) Music.start();
		else Music.stop();
		score = new Score();
		tetrominoe = room.randNewTetrominoe();
		time.start();
	}
	
	public void setSpeed(int level) {
		GamePanel.level = level;
		// Depending on the level that we choose, we increase the speed of the falling tetrominoes.
		switch(level) {
		case 1 : speed = 1;
		break;
		case 2 : speed = 2;
		break;
		case 3 : speed = 4;
		break;
		case 4 : speed = 6;
		break;
		case 5 : speed = 9;
		break;
		}
	}
	
	public void setPause(boolean pause) {
		this.pause = pause;
		score.setPause(pause);

		// If the game isn't paused, we pause the music
		if(pause == true || !running) {
			Music.stop();
		}
		// Else if the game is not ended and the game is paused, we resume the music
		else if(!GameOver.getGameOver()) {
			// We only resume the music if the game is not muted
			if(!Sound.isMuted()) Music.start();
		}
	}
	public boolean getPause() {
		return this.pause;
	}
	
	public void setRunning(boolean running) {
		this.running = running;
	}
	public boolean getRunning() {
		return running;
	}

	public void checkRotate(int d) {
		// We begin to rotate the tetrominoe and it's preview (named display)
		tetrominoe.rotate(d);
		display.rotate(d);
		
		// First, we check the condition for every cube of the falling tetrominoe.
		for(Cube cb : tetrominoe.getCubes()) {
			// As we have already rotated the tetrominoe, we check the position of the cube
			// If the position of the cube respects one of the conditions below, we rotate the tetrominoe and its preview to its initial position, like it never rotated
			if(cb.getPosX() >= Information.HOR_CASES || cb.getPosX() < 0 || cb.getPosY() < 0 || cb.getPosY() >= Information.VER_CASES) {
				tetrominoe.rotate(-d);
				display.rotate(-d);
				// We return as we don't want to check any other condition !
				return;
			}
			// Secondly, if the cube isn't outside the grid, we check its position compared to the other tetrominoes already placed
			for(Tetrominoe b : tetrominoes) {
				for(Cube c : b.getCubes()) {
					// If the position of the cube is the same as the position of the cube of a tetrominoe already placed, we do the same process.
					if(c.getPosX() == cb.getPosX() && c.getPosY() == cb.getPosY()){
						tetrominoe.rotate(-d);
						display.rotate(-d);
						return;
					}
				}
			}
		}
		// If we arrive at this position of the function's code, it means that the tetrominoe has rotated succesfully
		// Then, we add a new sound which correspond to the one when we rotate a tetrominoe.
		new Sound("se_game_rotate.wav");
	}
	
	// This method is drawing components thanks to Graphics class.
	public void draw(Graphics g) throws Exception {
		// We draw the background of the gridd
		grid.drawBackground(g);
		// If the game is running, we draw the tetrominoe that is currently falling.
		if(running) {
			tetrominoe.draw(g);
			// Moreover, is the gamelevel isn't equal to 5, it means the player has chosen lvl1,2,3 or 4
			// If it is the case, we display the preview of the tetrominoe that is falling to help the player
			// Otherwise, we don't display it to make the game even harder.
			if(level != 5) display.draw(g);
		}
		
		// We draw every already placed tetrominoes
		tetrominoes.forEach(t -> t.draw(g));
		
		// We draw the grid
		grid.draw(g);
		
		// If the game is running or this is a game over, we draw the score and the room.
		if(running || GameOver.getGameOver()) {
			score.draw(g);
			room.draw(g);
		}

		// If this is a game over, we draw a String on the grid and we stop the music.
		if(GameOver.getGameOver()) {
			grid.draw(g, "GAME OVER", UNIT_SIZE/2, GAME_WIDTH/13f);
			Music.stop();
		}
		// If the game is paused, we draw a "PAUSE" String into uppercase on the grid.
		if(pause) grid.draw(g, Language.setText("pause").toUpperCase(), (int)(GAME_WIDTH/14.2), (int)(GAME_WIDTH/8.6));
	}
	
	// This method allows to check collisions between tetrominoes
	public int collisions(int d) {
		// We reach every tetrominoe, and for every tetrominoe, we compare their cubes with the cubes of the tetrominoe that is currently falling.
		for(Tetrominoe t : tetrominoes) {
			for(Cube c : tetrominoe.getCubes()) {
				for(Cube c2 : t.getCubes()) {
					// If d != 0, it means that the player moved the tetrominoe horizontally
					// d = 1 for moving on the right
					// d = -1 for moving on the left
					// Then, we compare the next position of the cube that it will takes with the parameter d
					// If the position if the same as the position of any other cube of the game, we return 1, meaning that there is a collision on X.
					if(d != 0 && (c.getPosX() + d == c2.getPosX()) 
							&& t.isPlaced() == true 
							&& c.getPosY() == c2.getPosY()) {
						return 1;
					}
					// If d = 0, it means that we check a collision horizontally, and we do the same process of collision checking
					if(d == 0 && c.getPosX() == c2.getPosX() 
							&& t.isPlaced() == true 
							&& c.getPosY() == c2.getPosY()-1) {
						return 2;
					}
				}	
			}
		}
		// Finally, if there is no collision at the end of the verification, we return 0 meaning that there were no collision.
		return 0;
	}

	public boolean collisionsX(int d) {
		// If the collisions method return 1, it means there is a collision horizontally
		if(collisions(d) == 1) return true;
		// Else, it returns false
		return false;
	}
	public boolean collisionsY() {
		// Same process as horizontally
		if(collisions(0) == 2) {
			if(!tetrominoe.isPlaced()) tetrominoe.setPlaced();
			return true;
		}
		return false;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		// We override the paintComponent method from JPanel class
		super.paintComponent(g);
		try {
			// We draw the game by calling the draw method
			draw(g);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void checkLine() {
		// we declare a variable count that we increase every time a line is detected
		int count = 0;
		// Our cubeList LinkedList will be use to add the cubes on it and then remove them from the game if a line is done
		LinkedList<Cube> cubeList = new LinkedList<Cube>();
		// To begin with, we check every height of the grid (= every y position)
		for(int y = 0; y < Information.VER_CASES; y++) {
			// And for each tetrominoe, we check its cube and see if its position is the same as y
			for(Tetrominoe t : tetrominoes) {
				for(Cube c2: t.getCubes()) {
					if(c2.getPosY() == y) {
						// If this is the case, we add this cube in the LinkedList
						cubeList.add(c2);
					}
				}
			}
			// After traveling every cube in every tetrominoe, in the same height of the grid, if the size of the LinkedList is equal to the number of horizontal cases,
			// We add a line in the counter, we increase the count integer and we update the height of the cubes thanks to a specific method.
			if(cubeList.size() == HOR_CASES) {
				score.addLines(1);
				count++;
				updateHeight(cubeList, y);
			}
			// After that, or even if there is no need to remove a line, we clear the LinkedList
			cubeList.clear();
			// And then we do the same for the next y, while y is less than the number of vertical cases.
		}
		
		//If the count integer is equal to 4, it means that a TETRIS has been done,
		if(count == 4) {
			// We add a score of 800
			score.addScore(800);
			// We play a specific sound for a TETRIS
			new Sound("se_game_tetris.wav");
			// We increase the combo. Like so, if the next time the player makes another TETRIS consecutively, we add 400 to the score in order to add a total of 1200 to the score.
			combo++;
			if(combo >= 2) {
				score.addScore(400);
			}
		}
		// Otherwise, if the count integer isn't equal to 0, it means it is equal to 1,2 or 3
		else if(count != 0) {
			// We play a specific sound, and we add to the score 100,200 or 300 by multiplicating the count by 100
			new Sound("se_game_single.wav");
			score.addScore(count*100);
			combo = 0;
		}

		// Every time, we check if the actual score is superior to the highest score by calling the setNewHighscore method.
		score.setNewHighscore();
	}
	
	// As seen before, the updateHeight method is called when we need to update the height of tetrominoes after clearing a line.
	public void updateHeight(LinkedList<Cube> cubeList, int y) {
		// For each tetrominoe, we remove the cube that it owns the actual cube from the cubeList list.
		tetrominoes.forEach(b -> {
			for(Cube c : cubeList) {
				b.removeCube(c);
			}
		});
		// We update the height, each cube that has a higher y position than the y of the actual lined cleared, we update its height by increasing its y position. 
		tetrominoes.forEach(t -> {
			t.getCubes().forEach(c -> {
				if(c.getPosY() < y) {
					c.addY();
				}
			});
		});
	}

	// This method checks every time if the player lost
	public void checkGameOver() {
		if(!tetrominoes.isEmpty()) {
			// We only need to check the last tetrominoe placed, and we check all its cube
			tetrominoes.getLast().getCubes().forEach(c -> {
				// If the position of a cube is less than 0, it means that the cube is outside the grid, and then that the player has lost
				if(c.getPosY() < 0) {
					GameOver.setGameOver(true);
					score.destroy();
					time.restart();
					running = false;
					Panel.reset();
				}
			});
		}
	}
	
	// This method is dedicated to the preview of the falling tetrominoe
	// This is a recursiv method as it calls itself
	// In order to know where the tetrominoe will be placed at the future, it is interesting to use a recursiv method
	// Indeed, each time the y position of the display tetrominoe is incremented, and the method is called again until the preview enter in collision with a placed tetrominoe
	private void preview() {
		for(Cube c : display.getCubes()) {
			// If the y position of one cube of the preview is equal to the height of the game, we return in order to stop incrementing its y position.
			if(c.getPosY() == Information.VER_CASES-1) {
				return;
			}
			// Otherwise, we check the position of every cube of the preview with already placed tetrominoes.
			for(Tetrominoe t : tetrominoes) {
				for(Cube c2 : t.getCubes()) {
					for(Cube c3 : tetrominoe.getCubes()) {
						if(c.getPosX() == c2.getPosX()
						&& c.getPosY() == c2.getPosY()-1 
						&& c2.getPosY() >= c3.getPosY()+1) {
							// If there is a collision, we return : the preview show the position that the tetrominoe will take if we don't touch anything anymore.
							return;
						}
					}
				}	
			}
		}
		// If no condition is checked, we increment the y position of the tetrominoe.
		display.moveY();
		// We call again the preview method.
		preview();
	}
	

	// When the player touches the ENTER key, the tetrominoe is instantly placed. This is also a recursiv method.
	private void instantPlace() {
		// This is the same verification, if the position of the tetrominoe is equal to the height of the game, we set it placed and we return.
		for(Cube c : tetrominoe.getCubes()){
			if(c.getPosY() == Information.VER_CASES-1) {
				tetrominoe.setPlaced();
				return;
			}
		}
		// If there is no collision, between tetrominoes :
		if(!collisionsY()) {
			// We increment its y position
			tetrominoe.moveY();
			// We call again this method, and it makes the same process until there is a collision.
			instantPlace();
		}
	}
	

	@Override
	public void actionPerformed(ActionEvent e) {
		// Timer : if the game is running,
		if(running) {
			// If the game isn't paused and the tetrominoe is placed :
			if(!pause && tetrominoe.isPlaced()) {
			// We add the tetrominoe now placed in the tetrominoes linkedlist 
			tetrominoes.add(tetrominoe);
			// We add a new random tetrominoe in the waitingroom
			tetrominoe = room.randNewTetrominoe();
		}
		// If the game isn't paused, the tetrominoe isn't placed and there is no collision on y,
		if(!pause && !tetrominoe.isPlaced() && !collisionsY()) {
			// We move the tetrominoe horizontally.
			tetrominoe.moveY();
		}
			
		// Every clocktime, we call those methods in order to make the game working properly.
		checkLine();
		preview();
		checkGameOver();
		// We repaint the GamePanel to refresh the Panel.
		repaint();
		};
	}

	
	private class KeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			// If the game isn't paused, is running and if there is no game over, we can check the movements of the player while typing on the keyboard :
			if(!pause && running && !GameOver.getGameOver()) {
				// If there is no collision on X and the player press LEFT :
				if(e.getKeyCode() == KeyEvent.VK_LEFT && !collisionsX(-1) && !tetrominoe.isPlaced())	{
					// We call those methods
					tetrominoe.moveX(-1);
					display.moveX(-1);
					display.resetY();
					preview();
				}
				// We do the same process for every key pressed :
				if(e.getKeyCode() == KeyEvent.VK_RIGHT && !collisionsX(1) && !tetrominoe.isPlaced())	{
					tetrominoe.moveX(1);
					display.moveX(1);
					display.resetY();
					preview();
				}
				if(e.getKeyCode() == KeyEvent.VK_DOWN && !collisionsY() && !tetrominoe.isPlaced()) 	{
					tetrominoe.moveY();
				}
				if(e.getKeyCode() == KeyEvent.VK_Q && !tetrominoe.isPlaced()) {
					checkRotate(-1);
					display.resetY();
					preview();
				}
				if(e.getKeyCode() == KeyEvent.VK_D && !tetrominoe.isPlaced()) {
					checkRotate(1);
					display.resetY();
					preview();
				}
				if(e.getKeyCode() == KeyEvent.VK_ENTER && !tetrominoe.isPlaced()) {
					instantPlace();
				}

			}
			// After moving our tetrominoe, we repaint the game
			repaint();
		}
	}
}

