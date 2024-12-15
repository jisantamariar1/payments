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

@Path("/transfer")
public class TransferEndpoint {
    private EntityManager em = Persistence.createEntityManagerFactory("PaymentUnity").createEntityManager();
    @Inject
    private UserSessionBean userSession;
    @Inject
    @QualifierPayment("transfer")
    IPay paymentMethod;
    private PaymentProcessor paymentProcessor = new PaymentProcessor(em);


    @GET
    @Produces("text/html")
    public String showTransferForm(@QueryParam("total") double total) {
        total = userSession.getAmount();
        return "<html><body>" +
                "<h1>Pagar con Transferencia</h1>" +
                "<p>Total a pagar: " + total + " USD</p>" +
                "<form method='POST' action='/payments-1.0-SNAPSHOT/api/transfer'>" +
                "<label for='bank'>Banco:</label>" +
                "<input type='text' id='bank' name='bank' required/><br><br>" +
                "<label for='accountNumber'>Número de Cuenta:</label>" +
                "<input type='text' id='accountNumber' name='accountNumber' required/><br><br>" +
                "<input type='hidden' name='total' value='" + total + "'/>" +
                "<input type='submit' value='Pagar'>" +
                "</form>" +
                "</body></html>";
    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public Response processTransferPayment(@FormParam("bank") String bank,
                                           @FormParam("accountNumber") String accountNumber,
                                           @FormParam("total") double total) {
        //logica de validacion
        paymentProcessor.processPayment(userSession,paymentMethod);

        // Redirigir al endpoint /final
        return Response.status(Response.Status.FOUND)
                .location(URI.create("http://localhost:8080/payments-1.0-SNAPSHOT/api/final"))
                .build();
    }
}
