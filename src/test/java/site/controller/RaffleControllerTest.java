package site.controller;

import junit.framework.TestCase;

public class RaffleControllerTest extends TestCase {

    public void testMaskEmail() {
        assertEquals("abc@***.com", RaffleController.maskEmail("abc@one.com"));
    }
}