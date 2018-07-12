package site.controller;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import site.facade.StringSanitizer;

public class StringSanitizerTest {
	@Test
	public void formatStringTest() {
		String testString = "Sometext.With,Alot of;punctuation\twhichshould\nbesplitted, one.com, hh ,,, em.co.uk";
		String formatedString = StringSanitizer.formatString(testString);

		assertTrue(formatedString.split(" ").length == 9);

		assertFalse(formatedString.contains(","));
		assertFalse(formatedString.contains("  "));
		assertFalse(formatedString.contains(";"));
		assertFalse(formatedString.contains("\n"));
		assertFalse(formatedString.contains("\t"));
	}
}