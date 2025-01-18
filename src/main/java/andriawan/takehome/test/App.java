package andriawan.takehome.test;

import java.util.Scanner;

import andriawan.takehome.test.utilities.AuthManager;
import andriawan.takehome.test.utilities.CommandProcessor;
import andriawan.takehome.test.utilities.ConsoleManager;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        CommandProcessor commandProcessor = new CommandProcessor(new AuthManager());
        new ConsoleManager(new Scanner(System.in), commandProcessor).startApp();   
    }
}
