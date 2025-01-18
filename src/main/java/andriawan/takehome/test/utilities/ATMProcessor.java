package andriawan.takehome.test.utilities;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

import andriawan.takehome.test.entities.Balance;

public class ATMProcessor {
    private HashMap<String, Balance> listUserBalance = new HashMap<>();

    public ATMProcessor() {

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
}
