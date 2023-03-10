package seals.c195task.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import seals.c195task.Helper.JDBC;
import seals.c195task.Models.Appointments;
import seals.c195task.Models.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * SHOW REPORTS
 */
public class ReportsController implements Initializable {

    //CONTACT APPOINTMENT TABLE//
    @FXML private TableColumn<Appointments, Integer> appointmentIdCol;
    @FXML private TableColumn<Appointments, String> appointmentTitleCol;
    @FXML private TableColumn<Appointments, String> appointmentTypeCol;
    @FXML private TableColumn<Appointments, String> appointmentDescriptionCol;
    @FXML private TableColumn<Appointments, String> appointmentStartCol;
    @FXML private TableColumn<Appointments, String> appointmentEndCol;
    @FXML private TableColumn<Appointments, Integer> appointmentCustomerIdCol;
    @FXML private TableView<Appointments> contactAppointmentsTableView;

    private ObservableList<Appointments> contactAppointmentsObservableList;
    @FXML
    private ComboBox<String> contacts;

    //MONTH TYPE APPOINTMENT TABLE//
    @FXML private TableColumn<Appointments, String> monthAppointmentCol;
    @FXML private TableColumn<Appointments, String> typeAppointmentCol;
    @FXML private TableColumn<Appointments, Integer> numberOfAppointmentsCol;

    @FXML private TableView<Appointments> typeAppointmentTableView;


    //DIVISION TOTAL CUSTOMERS TABLE//
    @FXML private TableColumn<Customers, String> divisionCol;
    @FXML private TableColumn<Customers, Integer> totalCustomersCol;
    @FXML private TableView<Customers> divisionTotalTableView;


    /**
     * POPULATE THE CONTACT TABLE DEPENDING ON WHICH CONTACT THE USER SELECTS
     * @param event
     * @throws SQLException
     */
    public void populateContactTable(ActionEvent event) throws SQLException {
        ObservableList<Appointments> emptyData = FXCollections.observableArrayList();
        ObservableList<Appointments> appointmentContactList = FXCollections.observableArrayList();
        if (contacts.getValue().equals("Anika Costa")) {
            String sql = "SELECT * FROM appointments WHERE Contact_ID = 1";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            contactAppointmentsTableView.setItems(emptyData);
            Label placeholder = new Label();
            placeholder.setText("Contact does not have any affiliated appointments");
            contactAppointmentsTableView.setPlaceholder(placeholder);
                while (rs.next()) {
                    int appointmentId = rs.getInt("Appointment_ID");
                    String appointmentTitle = rs.getString("Title");
                    String appointmentType = rs.getString("Type");
                    String appointmentDescription = rs.getString("Description");
                    LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                    int customerId = rs.getInt("Customer_ID");
                    appointmentContactList.add(new Appointments(appointmentId, appointmentTitle, appointmentType, appointmentDescription, start, end, customerId));
                    contactAppointmentsTableView.setItems(appointmentContactList);
                }

            } else if (contacts.getValue().equals("Daniel Garcia")) {
            //ObservableList<Appointments> appointmentContactList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM appointments WHERE Contact_ID = 2";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            contactAppointmentsTableView.setItems(emptyData);
            Label placeholder = new Label();
            placeholder.setText("Contact does not have any affiliated appointments");
            contactAppointmentsTableView.setPlaceholder(placeholder);
                while (rs.next()) {
                    int appointmentId = rs.getInt("Appointment_ID");
                    String appointmentTitle = rs.getString("Title");
                    String appointmentType = rs.getString("Type");
                    String appointmentDescription = rs.getString("Description");
                    LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                    int customerId = rs.getInt("Customer_ID");
                    appointmentContactList.add(new Appointments(appointmentId, appointmentTitle, appointmentType, appointmentDescription, start, end, customerId));
                    contactAppointmentsTableView.setItems(appointmentContactList);
                }
        } else if (contacts.getValue().equals("Li Lee")) {
            //ObservableList<Appointments> appointmentContactList = FXCollections.observableArrayList();
            String sql = "SELECT * FROM appointments WHERE Contact_ID = 3";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            contactAppointmentsTableView.setItems(emptyData);
            Label placeholder = new Label();
            placeholder.setText("Contact does not have any affiliated appointments");
            contactAppointmentsTableView.setPlaceholder(placeholder);
                while (rs.next()) {
                    int appointmentId = rs.getInt("Appointment_ID");
                    String appointmentTitle = rs.getString("Title");
                    String appointmentType = rs.getString("Type");
                    String appointmentDescription = rs.getString("Description");
                    LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                    LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                    int customerId = rs.getInt("Customer_ID");
                    appointmentContactList.add(new Appointments(appointmentId, appointmentTitle, appointmentType, appointmentDescription, start, end, customerId));
                    contactAppointmentsTableView.setItems(appointmentContactList);
                }
            }
    }

    /**
     * POPULATE MONTH, TYPE, AND NUMBER OF APPOINTMENTS TABLE
     * @throws SQLException
     */

    public void populateMonthTable() throws SQLException {
        ObservableList<Appointments> appointmentMonthList = FXCollections.observableArrayList();
        String sql = "SELECT MONTHNAME(`Start`) AS month_name, Type, count(*) as Total_Appointments FROM appointments group by month_name, Type;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            String monthName = rs.getString("month_name");
            String type = rs.getString("Type");
            int totalAmount = rs.getInt("Total_Appointments");


            appointmentMonthList.add(new Appointments(monthName, type, totalAmount));
            typeAppointmentTableView.setItems(appointmentMonthList);
        }
    }


    /**
     * POPULATE DIVISION AND TOTAL CUSTOMERS TABLE
     * @throws SQLException
     */
    public void populateDivisionTotalCustomers() throws SQLException {
        ObservableList<Customers> customerDivisionTotalList = FXCollections.observableArrayList();
        String sql = "SELECT Division_ID, count(*) AS Total_Customers FROM customers GROUP BY Division_ID;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            String divisions = rs.getString("Division_ID");
            String divisionsConverted = convertDivisionIntToString(Integer.parseInt(divisions));
            int totalCustomers = rs.getInt("Total_Customers");
            customerDivisionTotalList.add(new Customers(divisionsConverted, totalCustomers));
            divisionTotalTableView.setItems(customerDivisionTotalList);
        }
    }

    /**
     * CONVERT INTEGER TO STRING TO SHOW DIVISION NAME
     * @param divisionId
     * @return
     * @throws SQLException
     */
    public String convertDivisionIntToString(int divisionId) throws SQLException {
        String sql = "SELECT Division from first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, divisionId);
        ResultSet rs = ps.executeQuery();
        String divisionName = null;
        if (rs.next()) {
            divisionName = rs.getString("Division");
        }
        return divisionName;
    }


    /**
     * EXITS TO MAIN SCREEN OF APPLICATION
     * @param event
     * @throws IOException
     */
    public void exitBtnPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("seals/C195task/AppointmentCustomerTable-View.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * CHANGE EXISTING TABLE LABEL TO NOTIFY USER TO CLICK A CONTACT IN DROP DOWN
     * SET DROP DOWNS
     * SET CELL VALUES FOR ALL TABLES
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //CHANGE TABLE TEXT TO NOTIFY USER TO SELECT A CONTACT//
        Label placeholder = new Label();
        placeholder.setText("Select a contact to view appointments");
        contactAppointmentsTableView.setPlaceholder(placeholder);

        //SET CHOICEBOX ITEMS FOR CONTACT//
        ObservableList contactList = FXCollections.observableArrayList("Anika Costa", "Daniel Garcia", "Li Lee");
        contacts.setItems(contactList);

        //SET CELLS FOR CONTACT APPOINTMENT TABLE//
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentStartCol.setCellValueFactory(new PropertyValueFactory<>("appointmentStartTime"));
        appointmentEndCol.setCellValueFactory(new PropertyValueFactory<>("appointmentEndTime"));
        appointmentCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerId"));

        //SET CELLS FOR MONTH TYPE AND NUMBER OF APPOINTMENTS TABLE//
        monthAppointmentCol.setCellValueFactory(new PropertyValueFactory<>("appointmentMonth"));
        typeAppointmentCol.setCellValueFactory(new PropertyValueFactory<>("typeAppointment"));
        numberOfAppointmentsCol.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        try {
            populateMonthTable();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        //SET CELLS FOR DIVISION AND TOTAL CUSTOMERS TABLE//
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("reportDivisions"));
        totalCustomersCol.setCellValueFactory(new PropertyValueFactory<>("reportTotalCustomers"));
        try {
            populateDivisionTotalCustomers();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
