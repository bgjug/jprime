package site.facade;


public class StringSanitizer {

	public static String formatString(String video) {
		return video.replaceAll("(\t|\n|,| |;)+", " ");
	}
}