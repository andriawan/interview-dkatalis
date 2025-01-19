package andriawan.takehome.test.utilities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import andriawan.takehome.test.entities.Balance;
import andriawan.takehome.test.entities.DebtHistory;

public class ATMProcessor {
    private HashMap<String, Balance> listUserBalance = new HashMap<>();
    private List<DebtHistory> listTrackDebt = new ArrayList<>();

    public ATMProcessor() {}

    public List<DebtHistory> getListDebt() {
        return this.listTrackDebt;
    }

    public Balance getBalance(String username) {
        Balance  balance = Optional.ofNullable(listUserBalance.get(username))
            .orElse(new Balance(BigDecimal.ZERO));
        listUserBalance.put(username, balance);
        return balance;
    }

    public void deposit(String username, Long amount) {
        Balance balance = getBalance(username);
        balance.setAmount(balance.getAmount().add(BigDecimal.valueOf(amount)));
        listUserBalance.put(username, balance);
    }

    public void withdraw(String username, Long amount) {
        Balance balance = getBalance(username);
        BigDecimal newAmount = balance.getAmount().subtract(BigDecimal.valueOf(amount));
        balance.setAmount(newAmount);
        listUserBalance.put(username, balance);
    }

    public void transfer(String sender, String receiver, Long amount) {
        Balance balanceReceiver = getBalance(receiver);
        Balance balanceSender = getBalance(sender);
        
        BigDecimal newBalanceReceiver = balanceReceiver.getAmount().add(new BigDecimal(amount));
        BigDecimal newBalanceSender = balanceSender.getAmount().subtract(BigDecimal.valueOf(amount));

        balanceReceiver.setAmount(newBalanceReceiver);
        balanceSender.setAmount(newBalanceSender);
        listUserBalance.put(sender, balanceSender);
        listUserBalance.put(receiver, balanceReceiver);
    }
}
