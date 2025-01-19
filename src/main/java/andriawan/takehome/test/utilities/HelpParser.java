package andriawan.takehome.test.utilities;

import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.exception.CommandTerminateException;

public class HelpParser extends BaseParser {

    public HelpParser(String[] command, GlobalState state) {
        this.command = command;
        this.globalState = state;
        this.parse();
    }

    public void parse() {
        globalState.clear();
        if(this.command[0].equalsIgnoreCase("help")) {
            ConsoleManager.renderHelpCommand();
            this.globalState.setStatus(CommandProcessor.SUCCESS);
            throw new CommandTerminateException();
        }
    }
    
}
