package andriawan.takehome.test.utilities;

import andriawan.takehome.test.entities.GlobalState;

public interface CommandParser {
    public void parse();
    public String getResult();
    public int getStatus();
    public GlobalState getState();
    public String getErrorMessage();
}