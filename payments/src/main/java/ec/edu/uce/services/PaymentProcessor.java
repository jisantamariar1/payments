package ec.edu.uce.services;

import ec.edu.uce.jpa.*;
import ec.edu.uce.paymentmethods.IPay;
import ec.edu.uce.paymentmethods.UserSessionBean;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;

public class PaymentProcessor {

    private EntityManager em;
    private UserService userService;
    private ServiceService serviceService;

    public PaymentProcessor(EntityManager em) {
        this.em = em;
        this.userService = new UserService(em);
        this.serviceService = new ServiceService(em);
    }

    public void processPayment(UserSessionBean userSession, IPay paymentMethod) {
        Account account = userService.findAccountByUserCedula(userSession.getUserCedula());
        boolean valid = paymentMethod.validPayment(userSession.getAmount(), account);

        if (valid) {
            Payment payment = new Payment();
            payment.setAmount(userSession.getAmount());
            payment.setDate(LocalDateTime.now());
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setAccount(account);
            payment.setUser(userService.findUserByCedula(userSession.getUserCedula()));
            payment.setService(serviceService.getServiceById(userSession.getSelectedServiceId()));

            PaymentService paymentService = new PaymentService(em);
            paymentService.createPayment(payment);
            userSession.setStatus(true);
        }
    }
}
