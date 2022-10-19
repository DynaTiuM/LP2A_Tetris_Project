import java.awt.*;
import java.io.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Panel extends JPanel implements Information, Colors {
	private GamePanel game;
	private Font customFont;
	private static JButton pause, restart, level, start, sound, exit, language;
	private int gameLevel = 1;
	private JLabel image;
	
	Panel(){
		// game is an instance of GamePanel, there is now a reference on GamePanel.
		game = new GamePanel();
		// We add the game to our Panel
		this.add(game);
		this.setPreferredSize(SCREEN);
		this.setBackground(new Color(40,40,40));
		this.setLayout(null);
		
		// We import some images/icons
		Icon volume = new ImageIcon(new ImageIcon(TEXTURES_PATH + "\\volume.png").getImage().getScaledInstance(GAME_WIDTH/14, GAME_WIDTH/14, Image.SCALE_DEFAULT));
		Icon mute = new ImageIcon(new ImageIcon(TEXTURES_PATH + "\\mute.png").getImage().getScaledInstance(GAME_WIDTH/14, GAME_WIDTH/14, Image.SCALE_DEFAULT));
		Icon exitImage = new ImageIcon(new ImageIcon(TEXTURES_PATH + "\\exit.png").getImage().getScaledInstance(GAME_WIDTH/16, GAME_WIDTH/16, Image.SCALE_DEFAULT));
		
		Icon fr = new ImageIcon(new ImageIcon(TEXTURES_PATH + "\\fr.png").getImage().getScaledInstance(GAME_WIDTH/16, GAME_WIDTH/25, Image.SCALE_DEFAULT));
		Icon en = new ImageIcon(new ImageIcon(TEXTURES_PATH + "\\en.png").getImage().getScaledInstance(GAME_WIDTH/16, GAME_WIDTH/25, Image.SCALE_DEFAULT));
		Icon de = new ImageIcon(new ImageIcon(TEXTURES_PATH + "\\de.png").getImage().getScaledInstance(GAME_WIDTH/16, GAME_WIDTH/25, Image.SCALE_DEFAULT));
		
		// We create different buttons for the panel
		pause = new JButton();
		restart = new JButton();
		level = new JButton();
		start = new JButton();
		exit = new JButton(exitImage);
		language = new JButton(en);
		image = new JLabel(new ImageIcon(new ImageIcon(TEXTURES_PATH + "\\tetris_logo.png").getImage().getScaledInstance(GAME_WIDTH/4, (int)(GAME_WIDTH/5.65), Image.SCALE_DEFAULT)));
		
		// And we add them to our Panel also
		this.add(image);
		sound = new JButton(volume);
		this.add(pause);
		this.add(level);
		this.add(restart);
		this.add(start);
		this.add(sound);
		this.add(exit);
		this.add(language);
		
		// We call this method to place the buttons on the panel
		setBounds();
		
		// We start to set the language on English
		try {
			Language.setLanguage(LanguageEnum.English);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// We call this method that puts the text on the buttons
		refresh();
		// We disable those buttons at the beginning
		pause.setEnabled(false);
		restart.setEnabled(false);
		
		// We exit the game
		exit.addActionListener(s->System.exit(0));
		
		// When we click to sound,
		sound.addActionListener(s ->{
			// We add a new sound,
			new Sound("se_sys_select.wav");
			// If the game isn't muted, we mute it, we change the icon and we stop the music
			if(!Sound.isMuted()) {
				sound.setIcon(mute);
				Sound.setMuted(true);
				Music.stop();
			}
			// In the other case, we do the opposite.
			else if(!game.getPause() && game.getRunning()) {
				Sound.setMuted(false);
				Music.start();
				sound.setIcon(volume);
			}
			else {
				Sound.setMuted(false);
				sound.setIcon(volume);
			}
			// We don't forget to request the focus to the game after that.
			game.requestFocus();
			
		});
		
		// We pause or not the game
		pause.addActionListener(s -> {
			new Sound("se_game_pause.wav");
			if(game.getPause() == true) {
				game.setPause(false);
				game.requestFocus();
			}
			else {
				game.setPause(true);
			}
			game.repaint();
		});
		
		// This Lambda expression makes us choose the level difficulty
		level.addActionListener(s -> {
			new Sound("se_game_count.wav");
			// If the game isn't running :
			if(game.getRunning() == false) {
				if(gameLevel < 5) {
					gameLevel++;
				}
				else {
					gameLevel=1;
				}
				if(gameLevel == 5) level.setForeground(PASTEL_GOLD);
				else level.setForeground(Color.white);
				level.setText(Language.setText("level") + gameLevel);
				game.requestFocus();
				game.repaint();
			}
		});
		
		// Lambda expression to start the game
		start.addActionListener(s -> {
			if(game.getRunning() == false) {
				// We set the speed of the game in relation to the gameLevel
				game.setSpeed(gameLevel);
				new Sound("se_sys_ok.wav");
				reset(false,false,true,true);
				
				try {
					// We start the game by calling its method
					game.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				game.requestFocus();
			}
		});
		
		// When restarting, we destroy all instances and we reset the buttons.
		restart.addActionListener(s -> {
			new Sound("se_sys_select.wav");
			pause.setText(Language.setText("pause"));
			game.destroy();
			game = null;
			System.gc();
			System.runFinalization();
			game = new GamePanel();
			this.add(game);
			reset(true,true,false,false);
			repaint();
		});
		
		// Simple language modifier
		language.addActionListener(s ->{
			try {
				if(Language.getLanguage().equals(LanguageEnum.English)) {
					Language.setLanguage(LanguageEnum.French);
					language.setIcon(fr);
				}
				else if(Language.getLanguage().equals(LanguageEnum.French)) {
					Language.setLanguage(LanguageEnum.German);
					language.setIcon(de);
				}
				else if(Language.getLanguage().equals(LanguageEnum.German)) {
					Language.setLanguage(LanguageEnum.English);
					language.setIcon(en);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			refresh();
			new Sound("se_game_rotate.wav");
			game.requestFocus();
		});
	}
	
	private void refresh() {
		setButtonTexture(pause, Language.setText("pause"));
		setButtonTexture(level, Language.setText("level") + gameLevel);
		setButtonTexture(restart, Language.setText("restart"));
		setButtonTexture(start, Language.setText("start"));
		setButtonTexture(sound, "");
		setButtonTexture(exit, "");
	}
	
	private void setBounds() {
		image.setBounds(GAME_WIDTH, (int)(GAME_WIDTH/22.3), GAME_WIDTH/4,(int)(GAME_WIDTH/5.65));
		pause.setBounds(GAME_WIDTH, UNIT_SIZE*11, GAME_WIDTH/4, GAME_WIDTH/8);
		level.setBounds(GAME_WIDTH, UNIT_SIZE*5, GAME_WIDTH/4, GAME_WIDTH/8);
		restart.setBounds(GAME_WIDTH, UNIT_SIZE*14, GAME_WIDTH/4, GAME_WIDTH/8);
		start.setBounds(GAME_WIDTH, UNIT_SIZE*8, GAME_WIDTH/4, GAME_WIDTH/8);
		sound.setBounds(GAME_WIDTH, UNIT_SIZE*17, GAME_WIDTH/12, GAME_WIDTH/12);
		exit.setBounds(GAME_WIDTH+GAME_WIDTH/4-GAME_WIDTH/7, UNIT_SIZE*17, GAME_WIDTH/7, GAME_WIDTH/12);
		language.setBounds(GAME_WIDTH+GAME_WIDTH/4-GAME_WIDTH/18, (int)(UNIT_SIZE*3.3), GAME_WIDTH/18, GAME_WIDTH/25);
	}
	private void setButtonTexture(JButton button, String text) {
		button.setBorder(BorderFactory.createLineBorder(PASTEL_GOLD, GAME_WIDTH/195));
		button.setBackground(DARK_GRAY);
		if(text.equals(Language.setText("level") + gameLevel) && gameLevel == 5) {
			button.setForeground(PASTEL_GOLD);
		}
		else button.setForeground(WHITE);
			
		button.setText(text);
		
		try {
			if(!text.equals("Wieder spielen")) customFont = Font.createFont(Font.TRUETYPE_FONT, new File(TEXTURES_PATH,"Urbanist-SemiBold.ttf")).deriveFont(GAME_WIDTH/19.5f);
			else customFont = Font.createFont(Font.TRUETYPE_FONT, new File(TEXTURES_PATH,"Urbanist-SemiBold.ttf")).deriveFont(GAME_WIDTH/30f);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		
		button.setFont(customFont);
	}
	
	public static void reset() {
		level.setEnabled(false);
		pause.setEnabled(false);
		restart.setEnabled(true);
	}
	
	private void reset(boolean a, boolean b, boolean c, boolean d) {
		start.setEnabled(a);
		level.setEnabled(b);
		pause.setEnabled(c);
		restart.setEnabled(d);
	}
}
