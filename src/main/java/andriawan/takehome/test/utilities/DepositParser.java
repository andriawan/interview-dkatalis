package andriawan.takehome.test.utilities;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import andriawan.takehome.test.entities.Balance;
import andriawan.takehome.test.entities.DebtHistory;
import andriawan.takehome.test.entities.GlobalState;
import andriawan.takehome.test.entities.User;
import andriawan.takehome.test.exception.CommandTerminateException;
import andriawan.takehome.test.exception.InvalidInputException;

public class DepositParser extends BaseParser {
    
    public DepositParser(String[] command, GlobalState state, AuthManager authManager, ATMProcessor atmProcessor) {
        this.command = command;
        this.globalState = state;
        this.atmProcessor = atmProcessor;
        this.authManager = authManager;
        this.checkValidCommand();
    }

    public void checkValidCommand() {
        if(command[0].equalsIgnoreCase("deposit")) {
            this.parse();
        }
    }

    public List<DebtHistory> payDebt(User debitur) {
        Balance balance = atmProcessor.getBalance(debitur.getName());
        Long longBalance = balance.getAmount().longValue();
        List<DebtHistory> debt = atmProcessor.getListDebt().stream().filter(dataFilter -> {
            return dataFilter.getDebiturName().equalsIgnoreCase(debitur.getName());
        }).map(mapper -> {
            Long finalAmount = mapper.getAmount();
            Long remainingBalance = longBalance;
            if(longBalance > mapper.getAmount()) {
                ConsoleManager.writeMessage(String.format("Transfer %s to %s", 
                    mapper.getAmount(), mapper.getCrediturName()));
                atmProcessor.transfer(debitur.getName(), 
                    mapper.getCrediturName(), mapper.getAmount());
                remainingBalance = longBalance - mapper.getAmount();
                finalAmount = 0L; 
            }else {
                ConsoleManager.writeMessage(String.format("Transfer %s to %s", 
                    longBalance, mapper.getCrediturName()));
                atmProcessor.transfer(debitur.getName(), 
                    mapper.getCrediturName(), longBalance);
                finalAmount = mapper.getAmount() - longBalance;
                remainingBalance = 0L;
            }
            balance.setAmount(new BigDecimal(remainingBalance));
            mapper.setAmount(finalAmount);
            return mapper;

        }).collect(Collectors.toList());

        List<String> removedId = debt.stream().map(map -> map.getId()).collect(Collectors.toList());
        atmProcessor.getListDebt().removeIf(data -> removedId.contains(data.getId()));
        atmProcessor.getListDebt().addAll(debt);

        List<DebtHistory> newdebtData = atmProcessor.getListDebt().stream().filter(dataFilter -> {
            return dataFilter.getDebiturName().equalsIgnoreCase(debitur.getName());
        }).collect(Collectors.toList());

        return newdebtData;
    }

    public void parse() {
        try {
            globalState.clear();
            if(this.globalState.getState() == CommandProcessor.LOGGED_IN) {
                User user = authManager.getAuthenticatedUser();
                Long deposit = 0L;
                deposit = Long.parseLong(command[1]);
                atmProcessor.deposit(user.getName(), deposit);
                ConsoleManager.writeMessage("Successfully deposit ".concat(
                    ConsoleManager.formatBalance(deposit)));
                this.payDebt(user);
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
