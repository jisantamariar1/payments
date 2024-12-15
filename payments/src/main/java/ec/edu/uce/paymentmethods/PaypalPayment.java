package ec.edu.uce.paymentmethods;

import ec.edu.uce.jpa.Account;
import ec.edu.uce.services.AccountService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

@ApplicationScoped
@QualifierPayment("paypal")
public class PaypalPayment implements IPay{
    private EntityManager em = Persistence.createEntityManagerFactory("PaymentUnity").createEntityManager();
    private AccountService accountService = new AccountService(em);

    @Override
    public boolean validPayment(double amount, Account account) {
        if(account.getPaypalBalance() < amount){
            return false;
        }
        account.setPaypalBalance(account.getPaypalBalance() - amount);
        accountService.updateBalance(account);
        return true;
    }
}
