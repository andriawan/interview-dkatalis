package andriawan.takehome.test.entities;

import andriawan.takehome.test.utilities.CommandProcessor;

public class GlobalState {

    private String result;
    private int status;
    private String state;
    private String errorMessage;

    // Constructor
    public GlobalState(String result, int status, String state, String errorMessage) {
        this.result = result;
        this.status = status;
        this.state = state;
        this.errorMessage = errorMessage;
    }

    public void clear() {
        this.result = "";
        this.status = 0;
        if(this.state != CommandProcessor.LOGGED_IN) {
            this.state = CommandProcessor.GUEST;
        }
        this.errorMessage = "";
    }

    // Getter and Setter for result
    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    // Getter and Setter for status
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    // Getter and Setter for state
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Getter and Setter for errorMessage
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

