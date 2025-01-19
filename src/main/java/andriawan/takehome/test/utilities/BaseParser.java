package andriawan.takehome.test.utilities;

import andriawan.takehome.test.entities.GlobalState;

public class BaseParser implements CommandParser {
    protected String[] command;
    protected String result;
    protected int status;
    protected String errorMessage;
    protected AuthManager authManager;
    protected ATMProcessor atmProcessor;
    protected GlobalState globalState;

    @Override
    public String getResult() {
        return this.result;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public GlobalState getState() {
        return this.globalState;
    }

    @Override
    public void parse() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'parse'");
    }
    
}
