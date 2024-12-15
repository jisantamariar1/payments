package ec.edu.uce.endpoints;

import ec.edu.uce.paymentmethods.UserSessionBean;
import ec.edu.uce.jpa.Service;
import ec.edu.uce.services.ServiceService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.net.URI;
import java.util.List;

@Path("/services")
public class ServiceView {
    @Inject
    private UserSessionBean userSession;

    private ServiceService serviceService;

    public ServiceView() {
        // Inicializar el servicio con el EntityManager
        EntityManager em = Persistence.createEntityManagerFactory("PaymentUnity").createEntityManager();
        this.serviceService = new ServiceService(em);

        // Crear servicios iniciales si no existen
        serviceService.createInitialServices();
    }

    @GET
    @Produces("text/html")
    public String showServicesForm() {
        // Obtener todos los servicios disponibles desde la base de datos
        List<Service> services = serviceService.getAllServices();

        // Crear el formulario HTML con los servicios y la opción de seleccionarlos
        String htmlResponse = "<html><body>" +
                "<h1>Servicios Disponibles</h1>" +
                "<form action='/payments-1.0-SNAPSHOT/api/services' method='POST'>";

        // Mostrar los servicios con botones de radio para seleccionarlos (solo uno puede ser seleccionado)
        for (Service service : services) {
            htmlResponse += "<div>" +
                    "<input type='radio' name='serviceId' value='" + service.getId() + "' required>" +
                    "<strong>" + service.getName() + "</strong>" +
                    "<p>" + service.getDescription() + "</p>" +
                    "<p>Precio: " + service.getPrice() + " USD</p>" +
                    "</div>";
        }

        // Botón para enviar la selección y ver el total
        htmlResponse += "<br><input type='submit' value='Ver Total y Proceder al Pago'>" +
                "</form>" +
                "</body></html>";

        return htmlResponse;
    }

    // Endpoint para procesar la selección de un servicio y calcular el total
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public Response processSelection(@FormParam("serviceId") Long serviceId) {
        // Guardar el serviceId en la sesión del usuario
        userSession.setSelectedServiceId(serviceId);
        // Obtener el servicio seleccionado de la base de datos
        Service service = serviceService.getServiceById(serviceId);

        // Mostrar el total y las opciones de pago
        String htmlResponse = "<html><body>" +
                "<h1>Resumen de Servicio Seleccionado</h1>" +
                "<ul>" +
                "<li>" + service.getName()  +
                "</ul>" +
                "<p>Total a pagar: " + service.getPrice() + " USD</p>" +

                // Desplegable de opciones de pago
                "<h3>Selecciona tu Método de Pago</h3>" +
                "<form action='/payments-1.0-SNAPSHOT/api/services/redirect' method='POST'>" +
                "<select name='paymentMethod' required>" +
                "<option value='card'>Tarjeta</option>" +
                "<option value='transfer'>Transferencia</option>" +
                "<option value='paypal'>PayPal</option>" +
                "</select><br><br>" +

                // Botón para proceder al pago
                "<input type='hidden' name='total' value='" + service.getPrice() + "'/>" +
                "<input type='submit' value='Proceder a Pago'>" +
                "</form>" +

                "</body></html>";

        return Response.status(Response.Status.OK)
                .entity(htmlResponse)
                .build();
    }

    // Endpoint para redirigir al método de pago seleccionado
    @POST
    @Path("/redirect")
    @Consumes("application/x-www-form-urlencoded")
    public Response redirectToPayment(@FormParam("paymentMethod") String paymentMethod,
                                      @FormParam("total") double total) {
        String redirectUrl = "";
        userSession.setAmount(total);

        // Determinar la URL del endpoint correspondiente
        switch (paymentMethod) {
            case "paypal":
                userSession.setPaymentMethod("Paypal");
                redirectUrl = "http://localhost:8080/payments-1.0-SNAPSHOT/api/paypal/";
                break;
            case "transfer":
                userSession.setPaymentMethod("Transferencia");
                redirectUrl = "http://localhost:8080/payments-1.0-SNAPSHOT/api/transfer/";
                break;
            case "card":
                userSession.setPaymentMethod("Tarjeta");
                redirectUrl = "http://localhost:8080/payments-1.0-SNAPSHOT/api/card/";
                break;
            default:
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("<html><body><h1>Error</h1><p>Método de pago inválido.</p></body></html>")
                        .build();
        }

        // Redirigir al endpoint correspondiente
        return Response.status(Response.Status.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }
}
