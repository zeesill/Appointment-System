package seals.c195task.Models;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * CUSTOMER FIELDS, CONSTRUCTORS, GETTERS AND SETTERS
 */
public class Customers {




    //CUSTOMER VARIABLES//
    private int customerId;
    private String customerName;
    private String customerAddress;
    private String customerPostalCode;
    private String customerPhone;
    private int customerDivision;

    //REPORTS TABLE//
    private String reportDivisions;

    public Customers(String reportDivisions, int reportTotalCustomers) {
        this.reportDivisions = reportDivisions;
        this.reportTotalCustomers = reportTotalCustomers;
    }

    private int reportTotalCustomers;

    public String getReportDivisions() {
        return reportDivisions;
    }

    public void setReportDivisions(String reportDivisions) {
        this.reportDivisions = reportDivisions;
    }

    public int getReportTotalCustomers() {
        return reportTotalCustomers;
    }

    public void setReportTotalCustomers(int reportTotalCustomers) {
        this.reportTotalCustomers = reportTotalCustomers;
    }

    static ObservableList<Customers> allCustomers = FXCollections.observableArrayList();



    public static void addCustomer(Customers newCustomer) {
        if(newCustomer != null) {
            allCustomers.add(newCustomer);
        }
    }



    //CUSTOMER CONSTRUCTOR//

    public Customers(int customerId, String customerName, String customerAddress, String customerPostalCode, String customerPhone, int customerDivision) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerPostalCode = customerPostalCode;
        this.customerPhone = customerPhone;
        this.customerDivision = customerDivision;
    }


    //GETTERS//


    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCustomerPostalCode() {
        return customerPostalCode;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public int getCustomerDivision() {
        return customerDivision;
    }

    //SETTERS//
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setCustomerPostalCode(String customerPostalCode) {
        this.customerPostalCode = customerPostalCode;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setCustomerDivision(int customerDivision) {
        this.customerDivision = customerDivision;
    }






}
