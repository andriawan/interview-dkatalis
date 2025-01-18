package andriawan.takehome.test.utilities;

import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class ConsoleManager {
    
    Scanner scanner;

    static Map<String, String> availableCommand = Map.of(
        "login", "login [name] - Logs in as this customer and creates the customer if not exist",
        "deposit", "deposit [amount] - Deposits this amount to the logged in customer",
        "withdraw", "withdraw [amount] - Withdraws this amount from the logged in customer",
        "transfer", "transfer [target] [amount] - Transfers this amount from the logged in customer to the target customer",
        "help", "help - show list of command",
        "exit", "exit - exit from application"
    );


    public ConsoleManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public void startApp() {
        ConsoleManager.renderWelcomeMessage();
        watchInputFromUser(input -> handleCommand(input));
        
    }

    public static void renderInputInstruction() {
        System.out.println("");
        System.out.println("=========================================");
        System.out.println("Please input your command:");
        System.out.println("=========================================");
        System.out.println("");
    }

    public void handleCommand(String input) {
        try {
            String available = availableCommand.get(input.toLowerCase());
            if(available == null) throw new Exception("Command Not Found. Please type help for avaliable command");
            if(input.equalsIgnoreCase("help")) {
                ConsoleManager.renderHelpCommand();
            }
            if(input.equalsIgnoreCase("exit")) {
                ConsoleManager.renderExitMessage();
                System.exit(0);
            }   
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            this.watchInputFromUser(
                inputFromUser -> handleCommand(inputFromUser));
        }
    }

    public void watchInputFromUser(Consumer<String> consumer) {
        ConsoleManager.renderInputInstruction();
        String input = this.readInput();
        consumer.accept(input);
        this.watchInputFromUser(
            inputFromUser -> this.handleCommand(inputFromUser));
    }

    public static void renderWelcomeMessage() {
        System.out.println("=========================================");
        System.out.println("------ Welcome to Andriawan ATM ------");
        System.out.println("");
        System.out.println("type /help for showing available command");
        System.out.println("========================================");
        System.out.println("");
    }

    public static void renderExitMessage() {
        System.out.println("========================================");
        System.out.println("-- Thank you for using Andriawan ATM --");
        System.out.println("========================================");
        System.out.println("");
    }

    public static void renderHelpCommand() {
        System.out.println("");
        ConsoleManager.availableCommand.values().forEach(val -> {
            System.out.println("  - ".concat(val));
        });
        System.out.println("");
    }

    public Scanner getScanner() {
        if(scanner == null) {
            scanner = new Scanner(System.in);
            return scanner;
        }
        return scanner;
    }

    public String readInput() {
        return getScanner().nextLine();
    }

    public void closeScanner() {
        try {
            scanner.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
