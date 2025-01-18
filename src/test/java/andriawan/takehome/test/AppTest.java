package andriawan.takehome.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import andriawan.takehome.test.entities.User;
import andriawan.takehome.test.utilities.AuthManager;
import andriawan.takehome.test.utilities.CommandProcessor;
import andriawan.takehome.test.utilities.ConsoleManager;

/**
 * Unit test for simple App.
 */
public class AppTest {

    ConsoleManager consoleManager;
    CommandProcessor commandProcessor;

    @BeforeEach
    public void setUp() {
        AuthManager authManager;
        authManager = new AuthManager();
        commandProcessor = new CommandProcessor(authManager);
        consoleManager = new ConsoleManager(null, commandProcessor);
    }

    @Test
    public void shouldReturnStatusSuccessCommand() {
        assertEquals(
            commandProcessor.handleCommand("help"), 
            CommandProcessor.SUCCESS);
    }

    @Test
    public void shouldReturnStatusExitCommand() {
        assertEquals(
            commandProcessor.handleCommand("exit"), 
            CommandProcessor.EXIT_APP);
    }

    @Test
    public void shouldReturnStatusNotFoundCommand() {
        assertEquals(
            commandProcessor.handleCommand("not found command"), 
            CommandProcessor.ERROR_NOT_FOUND);
    }

    @Test
    public void shouldLoginSucessfully() {
        commandProcessor.handleCommand("login andriawan");
        User user = commandProcessor.getAuthManager().getAuthenticatedUser();
        assertEquals("andriawan", user.getName());
        assertEquals(commandProcessor.getState(), CommandProcessor.LOGGED_IN);
    }

    @Test
    public void shouldShowInvalidLoginInput() {
        int status = commandProcessor.handleCommand("login");
        assertEquals(commandProcessor.getErrorMessage(), "Please type name for login");
        assertEquals(commandProcessor.getState(), CommandProcessor.GUEST);
        assertEquals(status, CommandProcessor.ERROR_NOT_VALID_INPUT);
    }

    @Test
    public void shouldShowInvalidLoginInputTooManyArgument() {
        int status = commandProcessor.handleCommand("login abc defg");
        assertEquals(commandProcessor.getErrorMessage(), "Too many argument");
        assertEquals(commandProcessor.getState(), CommandProcessor.GUEST);
        assertEquals(status, CommandProcessor.ERROR_NOT_VALID_INPUT);
    }

    @Test
    public void shouldNotLogoutBeforeLogin() {
        int status = commandProcessor.handleCommand("logout");
        assertEquals(commandProcessor.getState(), CommandProcessor.GUEST);
        assertEquals(commandProcessor.getErrorMessage(), "Please Login First");
        assertEquals(status, CommandProcessor.ERROR_NOT_VALID_INPUT);
    }
}
