package site.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import site.facade.VideoSanitizerService;
import site.model.Speaker;

public class VideoSanitizerServiceTest {

	private VideoSanitizerService videoSanitizerService = new VideoSanitizerService();

	@Test
	public void formatStringTest() {
		String testString = "Sometext.With,Alot of;punctuation\twhichshould\nbesplitted, one.com, hh ,,, em.co.uk";
		String formatedString = videoSanitizerService.formatString(testString);

		assertTrue(formatedString.split(" ").length == 9);

		assertFalse(formatedString.contains(","));
		assertFalse(formatedString.contains("  "));
		assertFalse(formatedString.contains(";"));
		assertFalse(formatedString.contains("\n"));
		assertFalse(formatedString.contains("\t"));
	}
}