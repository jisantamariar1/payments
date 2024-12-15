package ec.edu.uce.services;

import ec.edu.uce.jpa.Payment;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class PaymentService {
    private EntityManager em;
    public PaymentService(EntityManager em) {
        this.em = em;
    }
    public void createPayment(Payment payment) {
        em.getTransaction().begin();
        em.persist(payment);
        em.getTransaction().commit();
    }
    // Método para obtener el último pago de un usuario
    public Payment getLastPaymentForUser(String userCedula) {
        // Consulta JPQL para obtener el último pago del usuario basado en la fecha
        String jpql = "SELECT p FROM Payment p WHERE p.user.cedula = :cedula ORDER BY p.date DESC";
        TypedQuery<Payment> query = em.createQuery(jpql, Payment.class);
        query.setParameter("cedula", userCedula);
        query.setMaxResults(1);  // Solo obtener el último pago

        // Ejecutar la consulta y devolver el resultado
        return query.getResultList().isEmpty() ? null : query.getSingleResult();
    }
}
