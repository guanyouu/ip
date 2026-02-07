package daddybot;

import org.junit.jupiter.api.Test;

import daddybot.Parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DaddyTest {

    @Test
    public void daddyCheckTest() throws Exception {
        assertTrue(Parser.checkDaddy("todo list please daddy"));
        assertFalse(Parser.checkDaddy("todo list")); 
    }

    @Test
    public void parserTest() {
        assertEquals("todo list ", Parser.daddyTask("todo list please daddy"));
    }
}
