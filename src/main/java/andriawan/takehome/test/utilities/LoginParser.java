package andriawan.takehome.test.utilities;

import andriawan.takehome.test.entities.Balance;
import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.entities.User;
import andriawan.takehome.test.exception.CommandTerminateException;
import andriawan.takehome.test.exception.InvalidInputException;

public class LoginParser extends BaseParser {

    public LoginParser(String[] command, AuthManager authManager, ATMProcessor atmProcessor, GlobalState globalState) {
        this.command = command;
        this.status = CommandProcessor.SUCCESS;
        this.authManager = authManager;
        this.atmProcessor = atmProcessor;
        this.globalState = globalState;
        this.checkValidCommand();
    }

    public void checkValidCommand() {
        if(command[0].equalsIgnoreCase("login")) {
            this.parse();
        }
    }

    public void parse() {
        try {
            globalState.clear();
            if(this.globalState.getState() == CommandProcessor.LOGGED_IN) 
                throw new InvalidInputException("Already Login");
            if(command.length == 1) throw new InvalidInputException("Please type name for login");
            if(command.length > 2) throw new InvalidInputException("Too many argument");

            User user = authManager.login(command[1]);
            Balance balance = atmProcessor.getBalance(user.getName());
            globalState.setStatus(CommandProcessor.SUCCESS);
            globalState.setState(CommandProcessor.LOGGED_IN);
            ConsoleManager.renderLoggedInUser(authManager.getAuthenticatedUser(), 
                balance, atmProcessor.getListDebt());
            throw new CommandTerminateException();
        } catch (InvalidInputException e) {
            this.globalState.setErrorMessage(e.getMessage());
            this.globalState.setStatus(CommandProcessor.ERROR_NOT_VALID_INPUT);
        }
    }

    
    
}
