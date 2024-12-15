package ec.edu.uce.endpoints;

import ec.edu.uce.paymentmethods.UserSessionBean;
import ec.edu.uce.jpa.*;
import ec.edu.uce.services.AccountService;
import ec.edu.uce.services.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;

import java.net.URI;

@Path("/register")
public class UserRegister {

    private EntityManager em = Persistence.createEntityManagerFactory("PaymentUnity").createEntityManager();
    private AccountService accountService = new AccountService(em);
    private UserService userService = new UserService(em);

    @Inject
    private UserSessionBean userSessionBean; // Inyectamos el UserSessionBean

    // Endpoint para mostrar el formulario de registro
    @GET
    @Produces("text/html")
    public String showRegistrationForm() {
        return "<html>" +
                "<body>" +
                "<h1>Registro de Usuario</h1>" +
                "<form action='/payments-1.0-SNAPSHOT/api/register' method='POST'>" +
                "<label for='firstName'>Nombre:</label>" +
                "<input type='text' id='firstName' name='firstName' required/><br><br>" +
                "<label for='lastName'>Apellido:</label>" +
                "<input type='text' id='lastName' name='lastName' required/><br><br>" +
                "<label for='email'>Email:</label>" +
                "<input type='email' id='email' name='email' required/><br><br>" +
                "<label for='cedula'>Cédula:</label>" +
                "<input type='text' id='cedula' name='cedula' required/><br><br>" +
                "<input type='submit' value='Registrar Usuario'/>" +
                "</form>" +
                "</body>" +
                "</html>";
    }

    //procesar el registro de usuario
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public Response registerUser(@FormParam("firstName") String firstName,
                                 @FormParam("lastName") String lastName,
                                 @FormParam("email") String email,
                                 @FormParam("cedula") String cedula) {

        // Crear y guardar el usuario
        User user = new User(cedula, firstName, lastName, email);
        userService.createUser(user);
        Account account = new Account(100,100,100,user);
        accountService.createAccount(account);

        // Guardar la cédula del usuario en la sesión
        userSessionBean.setUserCedula(cedula);

        // Redirigir al endpoint de servicios
        return Response.status(Response.Status.FOUND)
                .location(URI.create("http://localhost:8080/payments-1.0-SNAPSHOT/api/services"))
                .build();
    }
}
