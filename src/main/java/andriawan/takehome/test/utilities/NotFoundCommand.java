package andriawan.takehome.test.utilities;

import java.util.Map;

import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.exception.CommandTerminateException;

public class NotFoundCommand extends BaseParser {

    Map<String, String> availableCommand;

    public NotFoundCommand(String[] command, GlobalState state, Map<String, String> availableCommand) {
        this.globalState = state;
        this.command = command;
        this.availableCommand = availableCommand;
        this.parse();
    }

    public void parse() {
        globalState.clear();
        if(availableCommand.get(command[0]) == null) {
            globalState.setErrorMessage("Command Not Found. Please type help for avaliable command");
            globalState.setStatus(CommandProcessor.ERROR_NOT_FOUND);
            throw new CommandTerminateException();
        }

    }
    
}
