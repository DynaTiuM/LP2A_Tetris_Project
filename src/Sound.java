import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound implements Path {
	private Clip clip;
	private static boolean mute = false;
	// This class has the same aspects as the Music class, but a sound isn't static as we can have many sonuds.
	Sound(String sound) {
		File file = new File(SOUNDS_PATH,sound);
		try {
			AudioInputStream sd = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(sd);
			// If the game isn't muted, we start the music.
			if(!mute)clip.start();
		} catch (UnsupportedAudioFileException | IOException e1) {
			e1.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isMuted() {
		return mute;
	}
	
	public static void setMuted(boolean m) {
		mute = m;
	}
	
	public Clip getClip() {
		return clip;
	}
}
