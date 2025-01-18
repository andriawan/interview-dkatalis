package andriawan.takehome.test.utilities;

import java.util.Map;

public class CommandProcessor {

    public static final int ERROR_NOT_FOUND = 1;
    public static final int ERROR_NOT_VALID_INPUT = 2;
    public static final int SUCCESS = 100;
    public static final int EXIT_APP = 0;
    public static final String LOGGED_IN = "logged_in";
    public static final String WITHDRAW = "withdraw";
    public static final String TRANSFER = "transfer";
    public static final String DEPOSIT = "transfer";
    public static final String GUEST = "guest";

    private AuthManager authManager;
    private String state = "guest";
    private String errorMessage = "";

    public CommandProcessor(AuthManager authManager) {
        this.authManager = authManager;
    }

    public String getState() {
        return state;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setState(String state) {
        this.state = state;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    static Map<String, String> availableCommand = Map.of(
        "login", "login [name] - Logs in as this customer and creates the customer if not exist",
        "deposit", "deposit [amount] - Deposits this amount to the logged in customer",
        "withdraw", "withdraw [amount] - Withdraws this amount from the logged in customer",
        "transfer", "transfer [target] [amount] - Transfers this amount from the logged in customer to the target customer",
        "help", "help - show list of command",
        "logout", "logout from application",
        "exit", "exit - exit from application"
    );

    private void clearMessage() {
        this.errorMessage = "";
    }

    public int handleCommand(String input) {
        clearMessage();
        int status = SUCCESS;
        String available = availableCommand.get(input.toLowerCase());
        String[] listCommand = input.trim().split("\s");
        boolean isLoginCommand = input.contains("login");

        if(input.equalsIgnoreCase("help")) {
            ConsoleManager.renderHelpCommand();
            return status;
        }

        if(isLoginCommand && getState() == LOGGED_IN) {
            errorMessage = "Already Login";
            return ERROR_NOT_VALID_INPUT;
        }else if(isLoginCommand && listCommand.length == 1) {
            errorMessage = "Please type name for login";
            return ERROR_NOT_VALID_INPUT;
        }else if(isLoginCommand && listCommand.length > 2) {
            errorMessage = "Too many argument";
            return ERROR_NOT_VALID_INPUT;
        }else if(isLoginCommand && listCommand.length == 2) {
            authManager.login(listCommand[1]);
            setState(CommandProcessor.LOGGED_IN);
            ConsoleManager.renderLoggedInUser(authManager.getAuthenticatedUser());
            return status;
        }

        if(input.contains("logout") && getState() == LOGGED_IN) {
            ConsoleManager.writeMessage(
                "Logged Out! Thank you for using our app ".concat(
                    authManager.getAuthenticatedUser().getName()));
            authManager.logout();
            setState(CommandProcessor.GUEST);
            return status;
        } else if(input.contains("logout") && getState() == GUEST) {
            errorMessage = "Please Login First";
            return ERROR_NOT_VALID_INPUT;
        };
        
        if(input.equalsIgnoreCase("exit")) {
            ConsoleManager.renderExitMessage();
            status = EXIT_APP;
        }

        if(available == null) {
            errorMessage = "Command Not Found. Please type help for avaliable command";
            return ERROR_NOT_FOUND;
        }

        return status;
    }
}
