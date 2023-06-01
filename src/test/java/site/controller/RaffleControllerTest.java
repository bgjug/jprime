package site.controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RaffleControllerTest {

    @Test
    void testMaskEmail() {
        assertEquals("abc@***.com", RaffleController.maskEmail("abc@one.com"));
    }
}