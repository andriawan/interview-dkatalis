package andriawan.takehome.test.utilities;

import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;

import andriawan.takehome.test.entities.User;

public class ConsoleManager {
    
    Scanner scanner;

    CommandProcessor commandProcessor;


    public ConsoleManager(Scanner scanner, CommandProcessor processor) {
        this.scanner = scanner;
        this.commandProcessor = processor;
    }

    public void startApp() {
        ConsoleManager.renderWelcomeMessage();
        watchInputFromUser(renderResult());
    }

    public Consumer<String> renderResult() {
        return (input) -> {
            int status = commandProcessor.handleCommand(input);
            if(status == CommandProcessor.EXIT_APP) {
                System.exit(0);
            }
            if(commandProcessor.getErrorMessage() != "") {
                ConsoleManager.writeMessage("[ERROR]: ".concat(commandProcessor.getErrorMessage()));
            }
            this.watchInputFromUser(renderResult());
        };
    }

    public static void renderLoggedInUser(User user) {
        System.out.println("");
        System.out.println("=========================================");
        System.out.println("Halo ".concat(user.getName()));
        System.out.println("=========================================");
        System.out.println("");
    }

    

    public void watchInputFromUser(Consumer<String> consumer) {
        String username = Optional.ofNullable(commandProcessor.getAuthManager())
            .flatMap(authManager -> Optional.ofNullable(authManager.getAuthenticatedUser()))
            .flatMap(user -> Optional.ofNullable(user.getName()))
            .map(name -> String.format("[%s] > ", name))
            .orElse("");
        System.out.print("$ ".concat(username));
        String input = this.readInput();
        consumer.accept(input);
        this.watchInputFromUser(
            inputFromUser -> commandProcessor.handleCommand(inputFromUser));
    }

    public static void writeMessage(String message) {
        System.out.println(message);
    }

    public static void renderWelcomeMessage() {
        System.out.println("=========================================");
        System.out.println("------ Welcome to Andriawan ATM ------");
        System.out.println("");
        System.out.println("type help for showing available command");
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
        CommandProcessor.availableCommand.values().forEach(val -> {
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
