import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public abstract class Tetrominoe implements Information {
	private ArrayList<Cube> cube = new ArrayList<Cube>(4);
	private Cube c1,c2,c3,c4;
	private boolean placed = false;
	
	// Tetrominoe is an ABSTRACT class that is a superclass from all the possible tetrominoes
	
	Tetrominoe(int x, int y){	// Constructor that works for any type of block
		// We create the cubes of the tetrominoe and we add them to our ArrayList
		c1 = new Cube(x,y);
		c2 = new Cube(x,y);
		c3 = new Cube(x,y);
		c4 = new Cube(x,y);
		cube.add(c1);
		cube.add(c2);
		cube.add(c3);
		cube.add(c4);
	}
	
	public void resetY() {
		cube.forEach(y -> y.setPos(0,-VER_CASES));
	}
	
	public ArrayList<Cube> getCubes() {
		return this.cube;
	}
	public Cube getCube(int index) {
		return cube.get(index);
	}
	public void removeCube(Cube cube) {
		this.cube.remove(cube);
	}
	
	public void rotate(int d) {
		for(Cube c : cube) {
			int dX = c.getLocalX();
			int dY = c.getLocalY();
			
			// This is a compact way to rotate the tetrominoe, see report for more informations
			if(c.getLocalX() == 0 || c.getLocalY() == 0)
				c.setLocalPos(-1*c.getLocalY()*d, c.getLocalX()*d);
			else {
				if(c.getLocalX() + c.getLocalY() != 0) {
					c.setLocalPos(-1*c.getLocalX()*d, c.getLocalY()*d);
				}
				else c.setLocalPos(c.getLocalX()*d, -1*c.getLocalY()*d);
			}
			c.setPos(c.getLocalX()-dX, c.getLocalY()-dY);
		}
	}
	
	public void moveX(int x) {
		// For each cube, if its x position is outside the game's grid, we return
		for(Cube s : cube) {
			if(s.getPosX()+x == HOR_CASES || s.getPosX()+x == -1) return;
		}
		for(Cube c : cube) {
			// Otherwise we move the tetrominoe by incrementing its x position and we add a sound
			c.addX(x);
			new Sound("se_game_move.wav");
		}
	}
	
	public void moveY() {
		// Same case for Y
		for(Cube c : cube) {
			if(c.getPosY() >= VER_CASES-1) {
				setPlaced();
				return;
			}
		}
		cube.forEach(c -> c.addY());
	}
	
	public boolean isPlaced() {
		return this.placed;
	}
	
	public void setPlaced() {
		new Sound("se_game_landing.wav");
		this.placed = true;
	}
	
	public void setTexture(BufferedImage texture) {
		cube.forEach(s -> s.setTexture(texture));
	}
	
	public void draw(Graphics g) {		// draw method that works for any type of block
		cube.forEach(s -> s.draw(g));
	}
	
	// This draw method is dedicated to the preview of the tetrominoe that is falling
	public void draw(Graphics g, float dx, float dy, int index) {
		cube.forEach(s -> s.draw(g, dx, dy, index));
	}

	// method that allows to make many different tetrominoes, depending on every local position of its cube
	// This method will be used by the subclasses as every subclass is a unique type of tetrominoe
	protected void setCubePos(int x2, int y2, int x3, int y3, int x4, int y4) {		
		c2.setLocalPos(x2, y2);
		c3.setLocalPos(x3, y3);
		c4.setLocalPos(x4, y4);
		for(Cube c : cube) {
			c.setPos(c.getLocalX(), c.getLocalY());
		}
	}
}

// Subclasses that extend Tetrominoe :

class OForm extends Tetrominoe {
	OForm(int x, int y, boolean preview) {
		super(x, y);
		// We put specific local position to the cubes for the O form, see more informations on the report.
		setCubePos(0,1,1,0,1,1);
		try {
			// If this instance of class is a preview, we change its texture
			if(!preview) super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "yellow.png")));
			else super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "yellow_d.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void rotate(int d) {}
}

class SForm extends Tetrominoe{
	// Same for every child class from Block but we change the coordinates of the Cubes
	SForm(int x, int y, boolean preview) {
		super(x, y);
		setCubePos(0,-1,1,-1,-1,0);
		try {
			if(!preview) super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "green.png")));
			else super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "green_d.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class IForm extends Tetrominoe{
	int dir = 1;
	IForm(int x, int y, boolean preview) {
		super(x, y);
		setCubePos(-1,0,1,0,2,0);
		try {
			if(!preview)super.setTexture(ImageIO.read(new File(TEXTURES_PATH,"cyan.png")));
			else super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "cyan_d.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// This one is more specific as its rotation method is different, we override and rewrite it !
	@Override
	public void rotate(int d) {
		super.getCube(1).setPos(1*dir, -1*dir);
		super.getCube(2).setPos(-1*dir, 1*dir);
		super.getCube(3).setPos(-2*dir, 2*dir);
		dir = dir*(-1);
		
	}
}

class TForm extends Tetrominoe{
	TForm(int x, int y, boolean preview) {
		super(x, y);
		setCubePos(0,-1,-1,0,1,0);
		try {
			if(!preview) super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "purple.png")));
			else super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "purple_d.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class JForm extends Tetrominoe{
	JForm(int x, int y, boolean preview) {
		super(x, y);
		setCubePos(-1,0,1,0,-1,-1);
		try {
			if(!preview) super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "blue.png")));
			else super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "blue_d.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class LForm extends Tetrominoe{
	LForm(int x, int y, boolean preview) {
		super(x, y);
		setCubePos(-1,0,1,0,1,-1);
		try {
			if(!preview) super.setTexture(ImageIO.read(new File(TEXTURES_PATH,"orange.png")));
			else super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "orange_d.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class ZForm extends Tetrominoe{
	ZForm(int x, int y, boolean preview) {
		super(x, y);
		setCubePos(-1,-1,0,-1,1,0);
		try {
			if(!preview) super.setTexture(ImageIO.read(new File(TEXTURES_PATH,"red.png")));
			else super.setTexture(ImageIO.read(new File(TEXTURES_PATH, "red_d.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
