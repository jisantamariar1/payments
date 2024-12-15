package ec.edu.uce.endpoints;

import ec.edu.uce.paymentmethods.IPay;
import ec.edu.uce.services.PaymentProcessor;
import ec.edu.uce.paymentmethods.QualifierPayment;
import ec.edu.uce.paymentmethods.UserSessionBean;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.net.URI;

@Path("/card")
public class CardEndpoint {
    private EntityManager em = Persistence.createEntityManagerFactory("PaymentUnity").createEntityManager();
    @Inject
    private UserSessionBean userSession;
    @Inject
    @QualifierPayment("card")
    IPay paymentMethod;
    private PaymentProcessor paymentProcessor = new PaymentProcessor(em);



    @GET
    @Produces("text/html")
    public String showCardForm(@QueryParam("total") double total) {
        total = userSession.getAmount();

        return "<html><body>" +
                "<h1>Pagar con Tarjeta</h1>" +
                "<p>Total a pagar: " + total + " USD</p>" +
                "<form method='POST' action='/payments-1.0-SNAPSHOT/api/card'>" +
                "<label for='cardNumber'>Número de Tarjeta:</label>" +
                "<input type='text' id='cardNumber' name='cardNumber' required/><br><br>" +
                "<label for='cardHolder'>Nombre del Titular:</label>" +
                "<input type='text' id='cardHolder' name='cardHolder' required/><br><br>" +
                "<label for='expiryDate'>Fecha de Expiración:</label>" +
                "<input type='month' id='expiryDate' name='expiryDate' required/><br><br>" +
                "<label for='cvv'>CVV:</label>" +
                "<input type='password' id='cvv' name='cvv' required/><br><br>" +
                "<input type='hidden' name='total' value='" + total + "'/>" +
                "<input type='submit' value='Pagar'>" +
                "</form>" +
                "</body></html>";
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public Response processCardPayment(@FormParam("cardNumber") String cardNumber,
                                       @FormParam("cardHolder") String cardHolder,
                                       @FormParam("expiryDate") String expiryDate,
                                       @FormParam("cvv") String cvv,
                                       @FormParam("total") double total) {
        //logica de validacion
        paymentProcessor.processPayment(userSession,paymentMethod);

        // Redirigir al endpoint /final
        return Response.status(Response.Status.FOUND)
                .location(URI.create("http://localhost:8080/payments-1.0-SNAPSHOT/api/final"))
                .build();
    }
}
