import java.io.FileReader;
import java.util.Properties;

public abstract class Language implements Information{
	// ABSTRACT Class used to change the language of the game
	
	private static String english = "english.properties";
	private static String french = "french.properties";
	private static String german = "german.properties";
	private static FileReader input;
	private static Properties prop;
	private static float scoreFontSize;
	private static float panelFontSize;
	
	private static LanguageEnum language;
	
	public static float getFontSize() {
		return scoreFontSize;
	}
	public static float getPanelFontSize() {
		return panelFontSize;
	}
	
	// Method used to change the language of the game
	public static void setLanguage(LanguageEnum language) throws Exception {
		prop = new Properties();
		Language.language = language;
		
		// If the language selected is equal to english,
		if(language == LanguageEnum.English) {
			// We read the file english.properties
			input = new FileReader(english);
			// We load the properties file
			prop.load(input);
			// We change the size of the font as it depend on the language to avoid graphic problems
			scoreFontSize = GAME_WIDTH/19.5f;
		}
		// Same with other languages
		else if(language == LanguageEnum.French) {
			input = new FileReader(french);
			prop.load(input);
			scoreFontSize = GAME_WIDTH/23f;
		}
		else {
			input = new FileReader(german);
			prop.load(input);
			scoreFontSize = GAME_WIDTH/25f;
		}
	}
	
	public static LanguageEnum getLanguage() {
		return language;
	}
	
	// Method that returns a String in order to return a specific word depending on the language selected
	public static String setText(String key) {
		return prop.getProperty(key);
	}
	

}
