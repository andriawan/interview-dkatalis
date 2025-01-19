package andriawan.takehome.test.utilities;

import java.util.Map;

import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.exception.CommandTerminateException;

public class CommandProcessor {

    public static final int ERROR_NOT_FOUND = 1;
    public static final int ERROR_NOT_VALID_INPUT = 2;
    public static final int SUCCESS = 100;
    public static final int SKIP = 101;
    public static final int EXIT_APP = 0;
    public static final String LOGGED_IN = "logged_in";
    public static final String WITHDRAW = "withdraw";
    public static final String TRANSFER = "transfer";
    public static final String DEPOSIT = "transfer";
    public static final String GUEST = "guest";

    private AuthManager authManager;
    private ATMProcessor atmProcessor;
    private GlobalState globalState;

    public CommandProcessor(AuthManager authManager, ATMProcessor atmProcessor, GlobalState state) {
        this.authManager = authManager;
        this.atmProcessor = atmProcessor;
        this.globalState = state;
    }

    public AuthManager getAuthManager() {
        return authManager;
    }

    public GlobalState getGlobalState() {
        return this.globalState;
    }

    static Map<String, String> availableCommand = Map.of(
        "login", "login [name] - Logs in as this customer and creates the customer if not exist",
        "deposit", "deposit [amount] - Deposits this amount to the logged in customer",
        "withdraw", "withdraw [amount] - Withdraws this amount from the logged in customer",
        "transfer", "transfer [target] [amount] - Transfers this amount from the logged in customer to the target customer",
        "help", "help - show list of command",
        "logout", "logout- logout from application",
        "exit", "exit - exit from application"
    );

    public CommandParser handleCommand(String input) {
        globalState.clear();
        String[] listCommand = input.trim().split("\s");
        CommandParser resultParser = new BaseParser();

        try {
            resultParser = new HelpParser(listCommand, globalState);
            resultParser = new LogoutParser(listCommand, globalState, authManager);
            resultParser = new ExitParser(listCommand, globalState);
            resultParser = new NotFoundCommand(listCommand, globalState, availableCommand);
            resultParser = new LoginParser(
                listCommand, authManager, atmProcessor, globalState);
            resultParser = new DepositParser(listCommand, globalState, 
                authManager, atmProcessor);
            resultParser = new WithdrawParser(listCommand, globalState, 
                authManager, atmProcessor);
            resultParser = new TransferParser(listCommand, globalState, 
                authManager, atmProcessor);
        } catch (CommandTerminateException e) {
            globalState.setResult("executed successfully");
        }

        return resultParser;
    }
}
