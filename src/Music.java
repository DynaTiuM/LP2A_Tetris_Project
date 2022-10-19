import java.io.*;
import javax.sound.sampled.*;

public class Music implements Path{
	// The music is static, there is only 1 MUSIC.
	private static Clip clip;
	Music(String name) throws Exception{
		// When creating an instance of Music, we load the music by locating it on the project
		File file = new File(SOUNDS_PATH, name);
		// We read it
		AudioInputStream music = AudioSystem.getAudioInputStream(file);
		clip = AudioSystem.getClip();
		// We open the music
		clip.open(music);
		// And we don't forget to loop it as the game can take many minutes without loosing.
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public static void stop() {
		clip.stop();
	}
	public static void start() {
		clip.start();
	}
}
