package ec.edu.uce.paymentmethods;

import ec.edu.uce.jpa.Account;

public interface IPay {
    public boolean validPayment(double amount, Account account);
}
