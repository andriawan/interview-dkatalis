package andriawan.takehome.test.utilities;

import java.util.List;
import java.util.UUID;
import andriawan.takehome.test.entities.Balance;
import andriawan.takehome.test.entities.DebtHistory;
import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.entities.User;
import andriawan.takehome.test.exception.CommandTerminateException;
import andriawan.takehome.test.exception.InvalidInputException;

public class TransferParser extends BaseParser {
    
    public TransferParser(String[] command, GlobalState state, AuthManager authManager, ATMProcessor atmProcessor) {
        this.command = command;
        this.globalState = state;
        this.atmProcessor = atmProcessor;
        this.authManager = authManager;
        this.checkValidCommand();
    }

    public void checkValidCommand() {
        if(command[0].equalsIgnoreCase("transfer")) {
            this.parse();
        }
    }

    public Long checkCreditBalance(User creditur, Long transferAmount, User debitur) {
        Long finalTransferAmount = transferAmount;
        List<DebtHistory> list = atmProcessor.getListDebt();
        DebtHistory singleDebt = list.stream().filter(debt -> {
            return debt.getCrediturName().equalsIgnoreCase(creditur.getName()) && 
                debt.getDebiturName().equalsIgnoreCase(debitur.getName());
        }).findFirst().orElse(null);

        if(singleDebt != null) {
            Long creditAmount = singleDebt.getAmount();
            if(creditAmount < transferAmount) {
                finalTransferAmount = transferAmount - creditAmount;
            }else {
                finalTransferAmount = 0L;
                singleDebt.setAmount(creditAmount - transferAmount);
                list.removeIf(data -> data.getId().equals(singleDebt.getId()));
                list.add(singleDebt);
            }
            
        }
        return finalTransferAmount;

    }
    public Long checkInsufficientBalance(User debitur, Long transferAmount, User creditur) {
        Balance currentBalance = atmProcessor.getBalance(debitur.getName());
        Long currentUserBalance = currentBalance.getAmount().longValue();
        Long owedAmount = 0L;
        Long finalTransferAmount = transferAmount;
        if(currentUserBalance < transferAmount) {
            owedAmount = transferAmount - currentUserBalance;
            finalTransferAmount = currentUserBalance;
        }
        List<DebtHistory> list = atmProcessor.getListDebt();
        DebtHistory singleDebt = list.stream().filter(debt -> {
            return debt.getCrediturName().equalsIgnoreCase(creditur.getName()) && 
                debt.getDebiturName().equalsIgnoreCase(debitur.getName());
        }).findFirst().orElse(null);
        if(owedAmount > 0 && singleDebt == null) {
            list.add(new DebtHistory(UUID.randomUUID().toString(), creditur.getName(), debitur.getName(), owedAmount));
        } else if(owedAmount > 0 && singleDebt != null) {
            singleDebt.setAmount(singleDebt.getAmount() + owedAmount);
            list.removeIf(data -> data.getId().equals(singleDebt.getId()));
            list.add(singleDebt);
        }
        return finalTransferAmount;

    }

    public void parse() {
        try {
            globalState.clear();
            if(command.length == 2) throw new InvalidInputException("missing amount to transfer");
            if(command.length == 1) throw new InvalidInputException("missing user & amount to transfer");
            if(command.length > 3) throw new InvalidInputException("Too many argument");
            if(this.globalState.getState() == CommandProcessor.LOGGED_IN) {
                User receiver = authManager.getUser(command[1]);
                if(receiver == null) {
                    throw new InvalidInputException(String.format("User %s not found", command[1]));
                }
                User user = authManager.getAuthenticatedUser();

                Long transferAmount = 0L;
                transferAmount = Long.parseLong(command[2]);
                ConsoleManager.writeMessage(String.format("Transfering %s to %s...", 
                    ConsoleManager.formatBalance(transferAmount), command[1]));
                ConsoleManager.writeMessage("checking owe form other users...");
                transferAmount = checkCreditBalance(user, transferAmount, receiver);
                transferAmount = checkInsufficientBalance(user, transferAmount, receiver);

                atmProcessor.transfer(user.getName(), command[1], transferAmount);
                ConsoleManager.writeMessage(String.format("Transfer %s to %s", 
                    ConsoleManager.formatBalance(transferAmount), command[1]));
                ConsoleManager.renderBalance(atmProcessor.getBalance(user.getName()));
                ConsoleManager.renderCrediturStatus(atmProcessor.getListDebt(), user);
                ConsoleManager.renderDebiturStatus(atmProcessor.getListDebt(), user);
                globalState.setStatus(CommandProcessor.SUCCESS);
                throw new CommandTerminateException();
            } else if(this.globalState.getState() == CommandProcessor.GUEST) {
                errorMessage = "Please Login First";
                throw new InvalidInputException("Please login first");
            };
            
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            globalState.setErrorMessage("Please input valid Amount");
            globalState.setStatus(CommandProcessor.ERROR_NOT_VALID_INPUT);
            throw new CommandTerminateException();
        } catch (InvalidInputException e) {
            globalState.setErrorMessage(e.getMessage());
            globalState.setStatus(CommandProcessor.ERROR_NOT_VALID_INPUT);
            throw new CommandTerminateException();
        }

    }
}
