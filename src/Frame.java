import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class Frame extends JFrame implements Path {
	private Image icon = Toolkit.getDefaultToolkit().getImage(TEXTURES_PATH + "\\logo.png");
	
	Frame(){
		// We add the panel on the frame
		this.add(new Panel());
		// We set the window visible
		this.setVisible(true);
		// We don't allow to resize the window
		this.setResizable(false);
		// We set the title and icon
		this.setTitle("TETRIS 22");	
		this.setIconImage(icon);
		// We set the default close operation to exit on close in order to avoid hide on close
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.pack();
		// Position of the game at the center
		this.setLocationRelativeTo(null);
		this.setLayout(null);
	}
}
