package ec.edu.uce.services;

import ec.edu.uce.jpa.Account;
import ec.edu.uce.jpa.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UserService {
    private EntityManager em;
    public UserService(EntityManager em) {
        this.em = em;
    }

    //metodo crear
    public void createUser(User user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
    }
    //metodo leer un usuario
    public User getUserById(int id) {
        return em.find(User.class, id);
    }
    //metodo update
    public void updateUser(User user) {
        em.getTransaction().begin();
        em.merge(user);
        em.getTransaction().commit();

    }
    //metodo delete
    public void deleteUser(int id) {
        em.getTransaction().begin();
        User user = em.find(User.class, id);
        em.remove(user);
        //em.remove(em.find(User.class, id));
        em.getTransaction().commit();
    }
    public Account findAccountByUserCedula(String cedula) {
        try {
            TypedQuery<Account> query = em.createQuery(
                    "SELECT a FROM Account a WHERE a.user.cedula = :cedula", Account.class);
            query.setParameter("cedula", cedula);
            return query.getSingleResult(); // Devuelve la cuenta asociada al usuario
        } catch (NoResultException e) {
            return null; // No se encontró una cuenta asociada al usuario
        }
    }
    public User findUserByCedula(String cedula) {
        try {
            // Crear la consulta para obtener el usuario por su cédula
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM User u WHERE u.cedula = :cedula", User.class);
            query.setParameter("cedula", cedula);

            // Ejecutar la consulta y devolver el usuario encontrado
            return query.getSingleResult();
        } catch (NoResultException e) {
            // Si no se encuentra un usuario con esa cédula, retornar null
            return null;
        }
    }





}
