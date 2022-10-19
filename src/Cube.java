import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Cube implements Information {
	// Global position of the cube
	private int x, y;
	// Local position of the cube
	private int localX, localY;
	// Texture of the cube
	private BufferedImage texture;
	
	Cube(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void setLocalPos(int x, int y) {
		this.localX = x;
		this.localY = y;
	}
	
	public void setPos(int x, int y) {
		this.x += x;
		this.y += y;
	}

	public void setTexture(BufferedImage texture) {
		this.texture = texture;
	}
	
	public int getPosX() {
		return this.x;
	}
	public int getPosY() {
		return this.y;
	}
	
	public int getLocalX() {
		return this.localX;
	}
	public int getLocalY() {
		return this.localY;
	}
	
	public void addY() {
		this.y++;
	}
	
	public void addX(int x) {
		this.x += x;
	}


	// First draw method use to draw the tetrominoe on the grid
	public void draw(Graphics g) {
		g.drawImage(texture, x*UNIT_SIZE, y*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE, null);
	}
	
	// Polymorphism : Second draw method used to display the tetrominoe in the waiting room.
	public void draw(Graphics g, float dx, float dy, int index) {
		g.drawImage(texture, (int)((dx+localX)*UNIT_SIZE+UNIT_SIZE*9), (int)(((localY+dy)+ index*3)*UNIT_SIZE), UNIT_SIZE, UNIT_SIZE, null);
	}

}
