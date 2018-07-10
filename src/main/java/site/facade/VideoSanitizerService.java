package site.facade;

import org.springframework.stereotype.Service;

@Service
public class VideoSanitizerService {

	public String formatString(String video) {
		return video.replaceAll("(\t|\n|,| |;)+", " ");
	}
}