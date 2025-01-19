package andriawan.takehome.test.utilities;

import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.exception.CommandTerminateException;
import andriawan.takehome.test.exception.InvalidInputException;

public class LogoutParser extends BaseParser {

    public LogoutParser(String[] command, GlobalState state, AuthManager authManager) {
        this.command = command;
        this.globalState = state;
        this.status = CommandProcessor.SUCCESS;
        this.authManager = authManager;
        this.parse();
    }

    public void parse() {
        try {
            globalState.clear();
            if(command[0].contains("logout") && 
                this.globalState.getState() == CommandProcessor.LOGGED_IN) {
                ConsoleManager.writeMessage(
                    "Logged Out! Thank you for using our app ".concat(
                        authManager.getAuthenticatedUser().getName()));
                authManager.logout();
                this.globalState.setStatus(CommandProcessor.SUCCESS);
                this.globalState.setState(CommandProcessor.GUEST);
                throw new CommandTerminateException();
            } else if(command[0].contains("logout") && 
                this.globalState.getState() == CommandProcessor.GUEST) {
                throw new InvalidInputException("Please Login First");
            };
        }  catch (InvalidInputException e) {
            this.globalState.setErrorMessage(e.getMessage());
            this.globalState.setStatus(CommandProcessor.ERROR_NOT_VALID_INPUT);
            throw new CommandTerminateException();
        }
    }    
    
}
