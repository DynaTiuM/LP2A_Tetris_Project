import java.awt.*;
import java.io.*;
import java.util.*;

@SuppressWarnings({ "serial", "rawtypes" })
public class WaitingRoom extends LinkedList implements Information, Colors {
	private LinkedList<Tetrominoe> waitingRoom = new LinkedList<>();
	
	// This method add new tetrominoes randomly
	public Tetrominoe randNewTetrominoe() {
		Random random = new Random();
		int rand = random.nextInt(7);
		Tetrominoe tetrominoe = null;
		Tetrominoe chosenTetrominoe = null;
		switch(rand) {
		case 0 : chosenTetrominoe = new IForm(4,-1, false);
		break;
		case 1 : chosenTetrominoe = new SForm(4,0, false);
		break;
		case 2 : chosenTetrominoe = new OForm(4,-1, false);
		break;
		case 3 : chosenTetrominoe = new TForm(4,0, false);
		break;
		case 4 : chosenTetrominoe = new JForm(4,0, false);
		break;
		case 5 : chosenTetrominoe = new LForm(4,0, false);
		break;
		case 6 : chosenTetrominoe = new ZForm(4,0, false);
		break;
		}
		// We add the random new tetrominoe to our linkedlist.
		waitingRoom.add(chosenTetrominoe);
		// If the linkedlist size is less than 4, we call again this same method recursively
		if(waitingRoom.size() < 4) 
			return randNewTetrominoe();
		// Else, we get the first tetrominoe of the linkedlist,
		else  {
			tetrominoe = waitingRoom.get(0);
			// After stocking it in a Tetrominoe variable, we remove it from the linkedlist
			waitingRoom.remove(0);
			
			// And we create its new preview by calling the setDisplay method.
			if(tetrominoe instanceof JForm) GamePanel.setDisplay(new JForm(4,0, true));
			if(tetrominoe instanceof TForm) GamePanel.setDisplay(new TForm(4,0, true));
			if(tetrominoe instanceof LForm)	GamePanel.setDisplay(new LForm(4,0, true));
			if(tetrominoe instanceof SForm) GamePanel.setDisplay(new SForm(4,0, true));
			if(tetrominoe instanceof ZForm) GamePanel.setDisplay(new ZForm(4,0, true));
			if(tetrominoe instanceof OForm) GamePanel.setDisplay(new OForm(4,0, true));
			if(tetrominoe instanceof IForm) GamePanel.setDisplay(new IForm(4,0, true));
			
		}
		// We return the new tetrominoe
		return tetrominoe;
	}
	
	// This method is dedicated to the draw of the WaitingRoom
	public void draw(Graphics g) {
		int radius = GAME_WIDTH/26;
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(LIGHT_GRAY);
		g.fillRoundRect(UNIT_SIZE*(HOR_CASES+1)+UNIT_SIZE/2, UNIT_SIZE, UNIT_SIZE*5, UNIT_SIZE*8,radius,radius);
		
		g.setColor(GRAY);
		g.fillRoundRect(UNIT_SIZE*(HOR_CASES+2)+UNIT_SIZE/2, UNIT_SIZE/2, UNIT_SIZE*5, UNIT_SIZE*9,radius,radius);
		
		waitingRoom.forEach(w -> {
			if(w instanceof OForm) w.draw(g,5,1, waitingRoom.indexOf(w));
			else if(w instanceof IForm) w.draw(g,5,1.5f, waitingRoom.indexOf(w));
			else w.draw(g,5.5f,2,waitingRoom.indexOf(w));
		});
		
		Font customFont;
		try {
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File(TEXTURES_PATH,"Urbanist-SemiBold.ttf")).deriveFont(GAME_WIDTH/19.5f);
			g.setFont(customFont);
		} catch (FontFormatException | IOException e) {
			e.printStackTrace();
		}
		g.setColor(BLUE);
		g2d.setStroke(new BasicStroke(GAME_WIDTH/130f));
		g2d.rotate(-Math.PI/2);
		if(Language.getLanguage() == LanguageEnum.French)
			g2d.drawString(Language.setText("next"), -UNIT_SIZE*7-UNIT_SIZE/3, UNIT_SIZE*12 + UNIT_SIZE/3);
		else if(Language.getLanguage() == LanguageEnum.English)
			g2d.drawString(Language.setText("next"), -UNIT_SIZE*6-UNIT_SIZE/3, UNIT_SIZE*12 + UNIT_SIZE/3);
		else
			g2d.drawString(Language.setText("next"), -UNIT_SIZE*7+UNIT_SIZE/3, UNIT_SIZE*12 + UNIT_SIZE/3);
		
		g2d.rotate(Math.PI/2);
		g.setColor(PASTEL_GOLD);
		g.drawRoundRect(UNIT_SIZE*(HOR_CASES+2)+UNIT_SIZE/2, UNIT_SIZE/2, UNIT_SIZE*5, UNIT_SIZE*3, radius, radius);
		
	}

}
