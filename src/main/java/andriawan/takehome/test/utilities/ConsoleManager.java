package andriawan.takehome.test.utilities;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import andriawan.takehome.test.entities.Balance;
import andriawan.takehome.test.entities.DebtHistory;
import andriawan.takehome.test.entities.GlobalState;
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
            commandProcessor.handleCommand(input);
            GlobalState state = commandProcessor.getGlobalState();
            if(state.getStatus() == CommandProcessor.EXIT_APP) {
                System.exit(0);
            }
            if(state.getErrorMessage() != "") {
                ConsoleManager.writeMessage("[ERROR]: ".concat(state.getErrorMessage()));
            }
            this.watchInputFromUser(renderResult());
        };
    }

    public static void renderLoggedInUser(User user, Balance balance, List<DebtHistory> debts) {
        System.out.println("");
        System.out.println("=========================================");
        System.out.println(String.format("Halo %s, Your balance is %s", 
            user.getName(), formatBalance(balance.getAmount().longValue())));
        renderDebiturStatus(debts, user);
        renderCrediturStatus(debts, user);
        System.out.println("=========================================");
        System.out.println("");
    }

    public static void renderBalance(Balance balance) {
        DecimalFormat df = new DecimalFormat("$###,###.##");
        String format = df.format(balance.getAmount());
        System.out.println("Your balance is ".concat(format));
    }

    public static void renderDebiturStatus(List<DebtHistory> debts, User debitur) {
        debts.stream().filter(dataFilter -> {
            return dataFilter.getDebiturName().equalsIgnoreCase(debitur.getName()) && 
                dataFilter.getAmount() > 0;
        }).collect(Collectors.toList()).forEach(debtSingle -> {
            ConsoleManager.writeMessage(String.format(
                "Owed %s to %s", ConsoleManager.formatBalance(debtSingle.getAmount()), 
                    debtSingle.getCrediturName()));
        });
    }

    public static void renderCrediturStatus(List<DebtHistory> debts, User creditur) {
        debts.stream().filter(dataFilter -> {
            return dataFilter.getCrediturName().equalsIgnoreCase(creditur.getName()) && 
                dataFilter.getAmount() > 0;
        }).collect(Collectors.toList()).forEach(debtSingle -> {
            ConsoleManager.writeMessage(String.format(
                "Owed %s from %s", ConsoleManager.formatBalance(debtSingle.getAmount()), 
                    debtSingle.getDebiturName()));
        });
    }

    public static String formatBalance(Long amount) {
        DecimalFormat df = new DecimalFormat("$###,###.##");
        return df.format(amount);
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
