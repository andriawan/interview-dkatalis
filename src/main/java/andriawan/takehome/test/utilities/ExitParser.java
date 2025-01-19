package andriawan.takehome.test.utilities;

import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.exception.CommandTerminateException;

public class ExitParser extends BaseParser {

    public ExitParser(String[] command, GlobalState state) {
        this.globalState = state;
        this.command = command;
        this.parse();
    }

    public void parse() {
        globalState.clear();
        if(command[0].equalsIgnoreCase("exit")) {
            ConsoleManager.renderExitMessage();
            this.globalState.setStatus(CommandProcessor.EXIT_APP);
            throw new CommandTerminateException();
        }

    }
    
}
