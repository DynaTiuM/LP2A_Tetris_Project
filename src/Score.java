import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.Timer;
import java.util.*;
import javax.imageio.ImageIO;

public class Score implements Information, Colors, ActionListener {
	private int score, highscore, lines, time, seconds, minutes;
	private boolean pause;
	Timer timer;
	private FileReader input = new FileReader("highscores.properties");
	private Properties prop = new Properties();
    private int level = GamePanel.getLevel();
	
	Score() throws Exception{
		// We work with a properties file, we start to load it.
		prop.load(input);
		// We also start a timer with a refresh rate of 1000ms.
		timer = new Timer(1000,this);
		timer.start();
		
		// The highscore is the value of the level key in our properties file, we store it in an integer.
        highscore = Integer.parseInt(prop.getProperty(String.valueOf(level)));
      
	}
	
	public void setNewHighscore() {
		// If the score is higher than the highscore, it means that we need to change the highest score in the properties file !
		if(score > highscore) {
			try {
				// We change the value of the key :
				prop.setProperty(String.valueOf(level), String.valueOf(score));
				// We store it to the same properties file.
				prop.store(new FileWriter("highscores.properties"), "Highscores depending on difficulty");
				// Then the highscore takes the number of the score !
				highscore=score;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addLines(int line) {
		this.lines+=line;
	}
	
	public void addScore(int score) {
		this.score += score;
	}
	
	private void setTime() {
		if(!getPause())	time++;
	}
	

	public void destroy() {
        timer.restart();
    }
	
	
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	private boolean getPause() {
		return this.pause;
	}
	
	// Simple draw method that draws the time, the number of lines clead, the highest score, the score
	// Or also some help like the keys for moving the tetrominoes, rotating it ...
	public void draw(Graphics g) throws Exception {
		int height = GAME_WIDTH/2;
		int offset = GAME_WIDTH/13;
		int keyHeight = GAME_WIDTH/3;
		Graphics2D g2d = (Graphics2D) g;
		float keyWidth = GAME_WIDTH/19.5f;
		int spaceKey = GAME_WIDTH/78;
		int radius = GAME_WIDTH/78;
	
		Font customFont;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File(TEXTURES_PATH,"Urbanist-SemiBold.ttf")).deriveFont(Language.getFontSize());
			g.setFont(customFont);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		seconds = time%60;
		minutes = time/60;
		g.setColor(WHITE);
		
		g.drawString(Language.setText("score") + score, UNIT_SIZE*(HOR_CASES+1), (int)(height+keyWidth));
		g.drawString(Language.setText("lines") + lines, UNIT_SIZE*(HOR_CASES+1), (int)(height + keyWidth + offset));
		g.drawString(Language.setText("time") + minutes + ":" + seconds, UNIT_SIZE*(HOR_CASES+1), (int)(height + keyWidth + offset*3));
		g.setColor(GOLD);
		g.drawString(Language.setText("highscore") + highscore, UNIT_SIZE*(HOR_CASES+1), (int)(height + keyWidth + offset*2));

		g.setColor(PASTEL_GOLD);
		g.fillRoundRect(UNIT_SIZE*(HOR_CASES)+UNIT_SIZE/2, UNIT_SIZE*10, spaceKey, UNIT_SIZE*6, radius, radius);
		
		BufferedImage arrow = ImageIO.read(new File(TEXTURES_PATH, "arrow.png"));
		BufferedImage q = ImageIO.read(new File(TEXTURES_PATH, "q_key.png"));
		BufferedImage d = ImageIO.read(new File(TEXTURES_PATH, "d_key.png"));
		g.drawImage(arrow, UNIT_SIZE*(HOR_CASES+1), height+keyHeight, (int)keyWidth, (int)keyWidth, null);
		g2d.rotate(-Math.PI/2);
		g.drawImage(arrow, (int)(-height-keyHeight - keyWidth) , UNIT_SIZE*(HOR_CASES+1) + (int)keyWidth + spaceKey, (int)keyWidth, (int)keyWidth, null);
		g2d.rotate(-Math.PI/2);
		g.drawImage(arrow, (int)(- UNIT_SIZE*(HOR_CASES+1) - 2*(keyWidth+spaceKey) - keyWidth), -(int)(height+keyHeight + keyWidth), (int)keyWidth, (int)keyWidth, null);
		g2d.rotate(Math.PI);
			
		g.drawImage(q, UNIT_SIZE*(HOR_CASES+1) + (int)((spaceKey+keyWidth)/2), height+keyHeight + (int)keyWidth + spaceKey, (int)keyWidth, (int)keyWidth, null);
		g.drawImage(d, UNIT_SIZE*(HOR_CASES+1) + (int)((spaceKey+keyWidth)/2 + spaceKey + keyWidth), height + keyHeight + (int)keyWidth + spaceKey, (int)keyWidth, (int)keyWidth, null);
	
		g.setColor(WHITE);
		
		customFont = Font.createFont(Font.TRUETYPE_FONT, new File(TEXTURES_PATH,"Urbanist-SemiBold.ttf")).deriveFont(Language.getFontSize()*0.75f);
		g.setFont(customFont);
		g.drawString(Language.setText("move"), UNIT_SIZE*(HOR_CASES+5) + UNIT_SIZE/2, height + keyHeight + GAME_WIDTH/26);
		g.drawString(Language.setText("rotate"), UNIT_SIZE*(HOR_CASES+5) + UNIT_SIZE/2, height + keyHeight + (int)keyWidth + spaceKey + GAME_WIDTH/26);
		
		g.setColor(ORANGE);
		g.fillRect(UNIT_SIZE*(HOR_CASES+1) + (int)(keyWidth + offset*2), height + keyHeight, UNIT_SIZE/8, (int)keyWidth);
		g.fillRect(UNIT_SIZE*(HOR_CASES+1) + (int)(keyWidth + offset*2), (int)(height + keyHeight + spaceKey + keyWidth), UNIT_SIZE/8, (int)keyWidth);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		setTime();
	}
}
