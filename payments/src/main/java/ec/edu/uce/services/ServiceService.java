package ec.edu.uce.services;

import ec.edu.uce.jpa.Service;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class ServiceService {
    private EntityManager em;

    public ServiceService(EntityManager em) {
        this.em = em;
    }

    public void createService(Service service) {
        em.getTransaction().begin();
        em.persist(service);
        em.getTransaction().commit();
    }

    public List<Service> getAllServices() {
        return em.createQuery("SELECT s FROM Service s", Service.class).getResultList();
    }

    public Service getServiceById(Long serviceId) {
        return em.find(Service.class, serviceId);
    }

    public void updateService(Service service) {
        em.getTransaction().begin();
        em.merge(service);
        em.getTransaction().commit();
    }

    public void createInitialServices() {
        // Crear los servicios iniciales si no existen
        List<Service> existingServices = em.createQuery("SELECT s FROM Service s", Service.class).getResultList();

        if (existingServices.isEmpty()) {
            Service waterService = new Service("Agua", "Servicio de agua potable", 15.00, LocalDate.now().plusMonths(1));
            Service electricityService = new Service("Luz", "Servicio eléctrico", 20.00, LocalDate.now().plusMonths(1));
            Service telephoneService = new Service("Teléfono", "Servicio de telefonía fija", 10.00, LocalDate.now().plusMonths(1));

            em.getTransaction().begin();
            em.merge(waterService);
            em.merge(electricityService);
            em.merge(telephoneService);
            em.getTransaction().commit();
        }
    }
}
