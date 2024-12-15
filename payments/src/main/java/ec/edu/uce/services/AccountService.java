package ec.edu.uce.services;

import ec.edu.uce.jpa.Account;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class AccountService {

    private EntityManager em;

    public AccountService(EntityManager em) {
        this.em = em;
    }

    public void createAccount(Account account) {
        em.getTransaction().begin();
        em.persist(account);
        em.getTransaction().commit();
    }

    public void updateBalance(Account account) {
        em.getTransaction().begin();
        em.merge(account); // Cambiado a merge para actualizar correctamente la cuenta
        em.getTransaction().commit();
    }

    /**
     * Encuentra la cuenta asociada a un usuario dado su cédula.
     * @param userCedula La cédula del usuario.
     * @return La cuenta asociada al usuario, o null si no existe.
     */
    public Account findAccountByUserCedula(String userCedula) {
        try {
            TypedQuery<Account> query = em.createQuery(
                    "SELECT a FROM Account a WHERE a.user.cedula = :cedula", Account.class);
            query.setParameter("cedula", userCedula);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null; // No se encontró una cuenta asociada
        }
    }
}
