package ec.edu.uce.jpa;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double creditCardBalance;
    private double paypalBalance;
    private double bankBalance;

    // Relación uno a uno con User
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    // Relación uno a muchos con Payment
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments = new ArrayList<>();

    public Account() {}

    public Account(double creditCardBalance, double paypalBalance, double bankBalance, User user) {
        this.creditCardBalance = creditCardBalance;
        this.paypalBalance = paypalBalance;
        this.bankBalance = bankBalance;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public double getCreditCardBalance() {
        return creditCardBalance;
    }

    public void setCreditCardBalance(double creditCardBalance) {
        this.creditCardBalance = creditCardBalance;
    }

    public double getPaypalBalance() {
        return paypalBalance;
    }

    public void setPaypalBalance(double paypalBalance) {
        this.paypalBalance = paypalBalance;
    }

    public double getBankBalance() {
        return bankBalance;
    }

    public void setBankBalance(double bankBalance) {
        this.bankBalance = bankBalance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void addPayment(Payment payment) {
        this.payments.add(payment);
        payment.setAccount(this); // Mantener consistencia bidireccional
    }

    public void removePayment(Payment payment) {
        this.payments.remove(payment);
        payment.setAccount(null); // Evitar referencias colgantes
    }
}
