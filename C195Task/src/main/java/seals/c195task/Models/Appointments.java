package seals.c195task.Models;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDateTime;

/**
 * APPOINTMENT FIELDS, CONSTRUCTORS, GETTERS AND SETTERS
 */
public class Appointments {
    //APPOINTMENT VARIABLES//
    private int appointmentId;
    private String appointmentTitle;
    private String appointmentDescription;
    private String appointmentLocation;
    private int appointmentContactId;
    private String appointmentType;
    private LocalDateTime appointmentStartTime;
    private LocalDateTime appointmentEndTime;
    private int appointmentCustomerId;
    private int appointmentUserId;

    //testing//
    private String appointmentMonth;
    private String typeAppointment;
    private int totalAmount;

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Appointments(String monthName, String type, int totalAmount) {
        this.appointmentMonth = monthName;
        this.typeAppointment = type;
        this.totalAmount = totalAmount;
    }

    public String getTypeAppointment() {
        return typeAppointment;
    }

    public void setTypeAppointment(String typeAppointment) {
        this.typeAppointment = typeAppointment;
    }

    static ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();


    //CONSTRUCTOR//
    public Appointments(int appointmentId, String appointmentTitle, String appointmentDescription, String appointmentLocation, int appointmentContactId, String appointmentType, LocalDateTime appointmentStartTime, LocalDateTime appointmentEndTime, int appointmentCustomerId, int appointmentUserId) {
        this.appointmentId = appointmentId;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentLocation = appointmentLocation;
        this.appointmentContactId = appointmentContactId;
        this.appointmentType = appointmentType;
        this.appointmentStartTime = appointmentStartTime;
        this.appointmentEndTime = appointmentEndTime;
        this.appointmentCustomerId = appointmentCustomerId;
        this.appointmentUserId = appointmentUserId;
    }

    //CONSTRUCTOR FOR CONTACT APPOINTMENTS IN REPORTS VIEW//
    public Appointments(int appointmentId, String appointmentTitle, String appointmentType, String appointmentDescription, LocalDateTime start, LocalDateTime end, int customerId) {
        this.appointmentId = appointmentId;
        this.appointmentTitle = appointmentTitle;
        this.appointmentDescription = appointmentDescription;
        this.appointmentType = appointmentType;
        this.appointmentStartTime = start;
        this.appointmentEndTime = end;
        this.appointmentCustomerId = customerId;
    }





    //GETTERS//
    public String getAppointmentMonth() {
        return appointmentMonth;
    }


    public int getAppointmentId() {
        return appointmentId;
    }

    public String getAppointmentTitle() {
        return appointmentTitle;
    }

    public String getAppointmentDescription() {
        return appointmentDescription;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public int getAppointmentContactId() {
        return appointmentContactId;
    }

    public String getAppointmentType() {
        return appointmentType;
    }


    public LocalDateTime getAppointmentStartTime() {
        return appointmentStartTime;
    }

    public LocalDateTime getAppointmentEndTime() {
        return appointmentEndTime;
    }

    public int getAppointmentCustomerId() {
        return appointmentCustomerId;
    }

    public int getAppointmentUserId() {
        return appointmentUserId;
    }


    //SETTERS//

    public void setAppointmentMonth(String appointmentMonth) {
        this.appointmentMonth = appointmentMonth;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public void setAppointmentTitle(String appointmentTitle) {
        this.appointmentTitle = appointmentTitle;
    }

    public void setAppointmentDescription(String appointmentDescription) {
        this.appointmentDescription = appointmentDescription;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public void setAppointmentContactId(int appointmentContactId) {
        this.appointmentContactId = appointmentContactId;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }


    public void setAppointmentStartTime(LocalDateTime appointmentStartTime) {
        this.appointmentStartTime = appointmentStartTime;
    }

    public void setAppointmentEndTime(LocalDateTime appointmentEndTime) {
        this.appointmentEndTime = appointmentEndTime;
    }

    public void setAppointmentCustomerId(int appointmentCustomerId) {
        this.appointmentCustomerId = appointmentCustomerId;
    }

    public void setAppointmentUserId(int appointmentUserId) {
        this.appointmentUserId = appointmentUserId;
   }


}