package andriawan.takehome.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import andriawan.takehome.test.entities.Balance;
import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.entities.User;
import andriawan.takehome.test.utilities.ATMProcessor;
import andriawan.takehome.test.utilities.AuthManager;
import andriawan.takehome.test.utilities.CommandProcessor;
import andriawan.takehome.test.utilities.ConsoleManager;

/**
 * Unit test for simple App.
 */
public class AppTest {

    ConsoleManager consoleManager;
    CommandProcessor commandProcessor;
    ATMProcessor atmProcessor;

    @BeforeEach
    public void setUp() {
        AuthManager authManager;
        authManager = new AuthManager();
        atmProcessor = new ATMProcessor();
        GlobalState state = new GlobalState("", 
            CommandProcessor.SUCCESS, CommandProcessor.GUEST, "");
        commandProcessor = new CommandProcessor(authManager, atmProcessor, state);
        consoleManager = new ConsoleManager(null, commandProcessor);
    }

    @Test
    public void shouldReturnStatusSuccessCommand() {
        commandProcessor.handleCommand("help"); 
        assertEquals(
            commandProcessor.getGlobalState().getStatus(),
            CommandProcessor.SUCCESS);
    }

    @Test
    public void shouldReturnStatusExitCommand() {
        commandProcessor.handleCommand("exit"); 
        assertEquals(
            commandProcessor.getGlobalState().getStatus(),
            CommandProcessor.EXIT_APP);
    }

    @Test
    public void shouldReturnStatusNotFoundCommand() {
        commandProcessor.handleCommand("not found command"); 
        assertEquals(
            commandProcessor.getGlobalState().getStatus(),
            CommandProcessor.ERROR_NOT_FOUND);
    }

    @Test
    public void shouldLoginSucessfully() {
        commandProcessor.handleCommand("login andriawan");
        User user = commandProcessor.getAuthManager().getAuthenticatedUser();
        assertEquals("andriawan", user.getName());
        assertEquals(commandProcessor.getGlobalState().getState(), 
            CommandProcessor.LOGGED_IN);
    }

    @Test
    public void shouldShowInvalidLoginInput() {
        commandProcessor.handleCommand("login");
        assertEquals(commandProcessor.getGlobalState().getErrorMessage(), "Please type name for login");
        assertEquals(commandProcessor.getGlobalState().getState(), CommandProcessor.GUEST);
        assertEquals(commandProcessor.getGlobalState().getStatus(), 
            CommandProcessor.ERROR_NOT_VALID_INPUT);
    }

    @Test
    public void shouldShowInvalidLoginInputTooManyArgument() {
        commandProcessor.handleCommand("login abc defg");
        assertEquals(commandProcessor.getGlobalState().getErrorMessage(), "Too many argument");
        assertEquals(commandProcessor.getGlobalState().getState(), CommandProcessor.GUEST);
        assertEquals(commandProcessor.getGlobalState().getStatus(), 
            CommandProcessor.ERROR_NOT_VALID_INPUT);
    }

    @Test
    public void shouldNotLogoutBeforeLogin() {
        commandProcessor.handleCommand("logout");
        assertEquals(commandProcessor.getGlobalState().getState(), CommandProcessor.GUEST);
        assertEquals(commandProcessor.getGlobalState().getErrorMessage(), "Please Login First");
        assertEquals(commandProcessor.getGlobalState().getStatus(), 
            CommandProcessor.ERROR_NOT_VALID_INPUT);
    }

    @Test
    public void userShouldDepositSucessfully() {
        commandProcessor.handleCommand("login andriawan");
        commandProcessor.handleCommand("deposit 100");
        User user = commandProcessor.getAuthManager().getAuthenticatedUser();
        Balance balance = atmProcessor.getBalance(user.getName());
        assertEquals(balance.getAmount(), new BigDecimal(100));
    }

    @Test
    public void userShouldTransferSucessfully() {
        commandProcessor.handleCommand("login andriawan");
        commandProcessor.handleCommand("deposit 100");
        commandProcessor.handleCommand("logout andriawan");
        commandProcessor.handleCommand("login user2");
        commandProcessor.handleCommand("logout user2");
        commandProcessor.handleCommand("login andriawan");
        commandProcessor.handleCommand("transfer user2 10");
        User user = commandProcessor.getAuthManager().getAuthenticatedUser();
        Balance balance = atmProcessor.getBalance(user.getName());
        User user2 = commandProcessor.getAuthManager().getUser("user2");
        Balance balanceUser2 = atmProcessor.getBalance(user2.getName());
        assertEquals(balance.getAmount(), new BigDecimal(90));
        assertEquals(balanceUser2.getAmount(), new BigDecimal(10));
    }

    @Test
    public void userShouldTransferSucessfullyWithOwe() {
        commandProcessor.handleCommand("login andriawan");
        commandProcessor.handleCommand("deposit 100");
        commandProcessor.handleCommand("logout");
        commandProcessor.handleCommand("login user2");
        commandProcessor.handleCommand("deposit 10");
        commandProcessor.handleCommand("logout user2");
        commandProcessor.handleCommand("login andriawan");
        commandProcessor.handleCommand("transfer user2 200");
        commandProcessor.handleCommand("logout");
        commandProcessor.handleCommand("login user2");
        commandProcessor.handleCommand("logout");
        commandProcessor.handleCommand("login andriawan");
        commandProcessor.handleCommand("deposit 40");
        commandProcessor.handleCommand("logout");
        commandProcessor.handleCommand("login user2");
        commandProcessor.handleCommand("deposit 50");
        commandProcessor.handleCommand("transfer andriawan 40");
    }

    @Test
    public void userShouldTransferSucessfullyWithOwed() {
        commandProcessor.handleCommand("login andriawan");
        commandProcessor.handleCommand("deposit 20");
        commandProcessor.handleCommand("logout");
        commandProcessor.handleCommand("login daras");
        commandProcessor.handleCommand("deposit 20");
        commandProcessor.handleCommand("transfer andriawan 10");
        commandProcessor.handleCommand("logout");
        commandProcessor.handleCommand("login andriawan");
        commandProcessor.handleCommand("transfer daras 10");
        commandProcessor.handleCommand("transfer daras 30");


    }
}
