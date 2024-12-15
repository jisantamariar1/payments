package ec.edu.uce.paymentmethods;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;

@SessionScoped
@Named
public class UserSessionBean implements Serializable {
    private String userCedula; // Almacenamos la cédula del usuario
    private double amount;
    private Long selectedServiceId;  // Guardar el ID del servicio seleccionado
    private boolean status = false;
    private String paymentMethod;

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    public boolean getStatus() {
        return status;
    }

    public void setSelectedServiceId(Long selectedServiceId) {
        this.selectedServiceId = selectedServiceId;

    }
    public Long getSelectedServiceId() {
        return selectedServiceId;
    }

    public void setAmount(double amount){
        this.amount = amount;
    }
    public double getAmount(){
        return amount;
    }


    public String getUserCedula() {
        return userCedula;
    }

    public void setUserCedula(String userCedula) {
        this.userCedula = userCedula;
    }

    public void clearSession() {
        this.userCedula = null;
        this.amount = 0;
    }
}
