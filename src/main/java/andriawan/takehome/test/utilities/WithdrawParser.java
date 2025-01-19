package andriawan.takehome.test.utilities;

import andriawan.takehome.test.entities.Balance;
import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.entities.User;
import andriawan.takehome.test.exception.CommandTerminateException;
import andriawan.takehome.test.exception.InvalidInputException;

public class WithdrawParser extends BaseParser {
    
    public WithdrawParser(String[] command, GlobalState state, AuthManager authManager, ATMProcessor atmProcessor) {
        this.command = command;
        this.globalState = state;
        this.atmProcessor = atmProcessor;
        this.authManager = authManager;
        this.checkValidCommand();
    }

    public void checkValidCommand() {
        if(command[0].equalsIgnoreCase("withdraw")) {
            this.parse();
        }
    }

    public void parse() {
        try {
            globalState.clear();
            if(this.globalState.getState() == CommandProcessor.LOGGED_IN) {
                User user = authManager.getAuthenticatedUser();
                Long withdraw = 0L;
                Balance currentBalance = atmProcessor.getBalance(user.getName());
                withdraw = Long.parseLong(command[1]);
                if(currentBalance.getAmount().longValue() < withdraw) {
                    throw new InvalidInputException(
                        "Unable to withdraw. you can only withdraw maximum balance ".concat(
                            currentBalance.getAmount().toString()));
                }
                atmProcessor.withdraw(user.getName(), withdraw);
                ConsoleManager.renderBalance(atmProcessor.getBalance(user.getName()));
                globalState.setStatus(CommandProcessor.SUCCESS);
                throw new CommandTerminateException();
            } else if(this.globalState.getState() == CommandProcessor.GUEST) {
                errorMessage = "Please Login First";
                throw new InvalidInputException("Please login first");
            };
            
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            globalState.setErrorMessage("Please input valid Amount");
            globalState.setStatus(CommandProcessor.ERROR_NOT_VALID_INPUT);
            throw new CommandTerminateException();
        } catch (InvalidInputException e) {
            globalState.setErrorMessage(e.getMessage());
            globalState.setStatus(CommandProcessor.ERROR_NOT_VALID_INPUT);
            throw new CommandTerminateException();
        }

    }
}
