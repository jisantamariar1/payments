package ec.edu.uce.endpoints;

import ec.edu.uce.paymentmethods.UserSessionBean;
import ec.edu.uce.jpa.*;
import ec.edu.uce.services.PaymentService;
import ec.edu.uce.services.ServiceService;
import ec.edu.uce.services.UserService;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.time.format.DateTimeFormatter;

@Path("/final")
public class TransactionSummaryEndpoint {
    private EntityManager em = Persistence.createEntityManagerFactory("PaymentUnity").createEntityManager();
    @Inject
    private UserSessionBean userSession;

    private UserService userService = new UserService(em);
    private ServiceService serviceService = new ServiceService(em);
    private PaymentService paymentService = new PaymentService(em);

    @GET
    @Produces("text/html")
    public Response getFinalPage() {
        // Comprobar el estado de la transacción
        if (userSession.getStatus()) {
            // Si la transacción fue exitosa, mostrar el resumen
            User user = userService.findUserByCedula(userSession.getUserCedula());
            Service service = serviceService.getServiceById(userSession.getSelectedServiceId());
            Payment payment = paymentService.getLastPaymentForUser(userSession.getUserCedula());
            Account account = payment.getAccount();  // Obtener la cuenta asociada al pago

            // Formatear la fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = payment.getDate().format(formatter);

            // Obtener el método de pago desde la sesión
            String paymentMethod = userSession.getPaymentMethod();

            // Crear el HTML con la cédula del usuario y el método de pago
            String responseHtml = "<html><body>"
                    + "<h1>Resumen de la Transacción</h1>"
                    + "<p><strong>Usuario:</strong> " + user.getName() + "</p>"
                    + "<p><strong>Cédula:</strong> " + user.getCedula() + "</p>"
                    + "<p><strong>Servicio:</strong> " + service.getName() + "</p>"
                    + "<p><strong>Monto pagado:</strong> " + payment.getAmount() + " USD</p>"
                    + "<p><strong>Fecha del pago:</strong> " + formattedDate + "</p>"
                    + "<p><strong>Cuenta asociada:</strong> " + account.getId() + "</p>"
                    + "<p><strong>Método de pago:</strong> " + paymentMethod + "</p>"  // Mostrar el método de pago
                    + "</body></html>";
            return Response.status(Response.Status.OK)
                    .entity(responseHtml)
                    .build();
        } else {
            // Si el estado es false, mostrar el mensaje de fondos insuficientes
            String responseHtml = "<html><body>"
                    + "<h1>Error</h1>"
                    + "<p>Fondos insuficientes. No se pudo realizar la transacción.</p>"
                    + "</body></html>";
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(responseHtml)
                    .build();
        }
    }
}
