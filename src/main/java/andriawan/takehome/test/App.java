package andriawan.takehome.test;

import java.util.Scanner;

import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.utilities.ATMProcessor;
import andriawan.takehome.test.utilities.AuthManager;
import andriawan.takehome.test.utilities.CommandProcessor;
import andriawan.takehome.test.utilities.ConsoleManager;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        GlobalState state = new GlobalState("", 
            CommandProcessor.SUCCESS, CommandProcessor.GUEST, "");
        CommandProcessor commandProcessor = new CommandProcessor(
            new AuthManager(), new ATMProcessor(), state);
        new ConsoleManager(new Scanner(System.in), commandProcessor).startApp();   
    }
}
