package ec.edu.uce.paymentmethods;

import ec.edu.uce.jpa.Account;
import ec.edu.uce.services.AccountService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

@ApplicationScoped//(inicia y finaliza con la aplicacion)
//@SessionScoped//necesita ser serializable (elementos de inicio de sesion)
//@RequestScoped//(dura una soliciitud HTTP)
//@Stateful//(con estado)(se necesita un registro)
//@Stateless//(sin estado)
@QualifierPayment("card")
public class CardPayment implements IPay{
    private EntityManager em = Persistence.createEntityManagerFactory("PaymentUnity").createEntityManager();
    private AccountService accountService = new AccountService(em);
    @Override
    public boolean validPayment(double amount, Account account) {
        if(account.getCreditCardBalance() < amount){
            return false;
        }
        account.setCreditCardBalance(account.getCreditCardBalance() - amount);
        accountService.updateBalance(account);
        return true;
    }
}
