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
import java.sql.*;
import java.time.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * SHOW TABLE FOR BOTH APPOINTMENT AND CUSTOMER
 * ALLOW FOR UPDATE DELETE ADD FOR BOTH APPOINTMENTS AND CUSTOMERS
 * VIEW REPORTS
 */
public class AppointmentCustomerTableController implements Initializable {
    //RADIO BUTTONS//
    @FXML private RadioButton appointmentsByWeek;
    @FXML private RadioButton appointmentsByMonth;
    @FXML private RadioButton appointmentsByAll;


    //CUSTOMER TABLE VARIABLES//
    @FXML
    private TableView<Customers> customersTableView;
    @FXML
    private TableColumn<Customers, Integer> customerIdCol;
    @FXML
    private TableColumn<Customers, String> customerNameCol;
    @FXML
    private TableColumn<Customers, String> customerAddressCol;
    @FXML
    private TableColumn<Customers, String> customerPostalCodeCol;
    @FXML
    private TableColumn<Customers, String> customerPhoneCol;
    @FXML
    private TableColumn<Customers, Integer> customerDivisionCol;
    //LIST FOR CUSTOMERS//
    private ObservableList<Customers> customersObservableList;

    //APPOINTMENT TABLE VARIABLES//
    @FXML
    private TableView<Appointments> appointmentsTableView;
    @FXML
    private TableColumn<Appointments, Integer> appointmentIdCol;
    @FXML
    private TableColumn<Appointments, String> appointmentTitleCol;
    @FXML
    private TableColumn<Appointments, String> appointmentDescriptionCol;
    @FXML
    private TableColumn<Appointments, String> appointmentLocationCol;
    @FXML
    private TableColumn<Appointments, Integer> appointmentContactIdCol;
    @FXML
    private TableColumn<Appointments, String> appointmentTypeCol;
    @FXML
    private TableColumn<Appointments, LocalDateTime> appointmentStartDateTimeCol;
    @FXML
    private TableColumn<Appointments, LocalDateTime> appointmentEndDateTimeCol;
    @FXML
    private TableColumn<Appointments, Integer> appointmentCustomerIdCol;
    @FXML
    private TableColumn<Appointments, Integer> appointmentUserIdCol;

    private ObservableList<Appointments> appointmentsObservableList;


    //SQL//
    private PreparedStatement ps = null;
    private ResultSet rs = null;


    /**
     * SWITCH TO ADD CUSTOMER SCENE WHEN BUTTON IS PUSHED
     * @param event
     * @throws IOException
     */
    public void addCustomerBtnPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("seals/C195task/AddCustomer-View.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * SWITCH TO UPDATE CUSTOMER SCENE WHEN BUTTON IS PUSHED
     * @param event
     * @throws IOException
     * @throws SQLException
     */
    public void updateCustomerBtnPushed(ActionEvent event) throws IOException, SQLException {
        Customers selectedCustomer = customersTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Customer to Update");
            alert.showAndWait();
        } else {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("seals/C195task/UpdateCustomer-View.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            UpdateCustomerController controller = loader.getController();
            controller.getSelectedCustomer(customersTableView.getSelectionModel().getSelectedItem());
            stage.show();
        }
    }

    /**
     * DELETE CUSTOMER IF NO ERRORS
     * @param event
     * @throws SQLException
     * @throws InterruptedException
     * @throws IOException
     */
    public void deleteCustomer(ActionEvent event) throws SQLException, InterruptedException, IOException {
        Customers customer = this.customersTableView.getSelectionModel().getSelectedItem();
        if (customer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a Customer to Delete");
            alert.showAndWait();
        } else {
            int deleteCustomerID = customer.getCustomerId();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to remove this Customer?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                String sql1;
                PreparedStatement ps1;
                try {
                    sql1 = "SELECT Customer_ID FROM appointments WHERE Customer_ID = ?";
                    ps1 = JDBC.connection.prepareStatement(sql1);
                    ps1.setInt(1, deleteCustomerID);
                    ResultSet rs = ps1.executeQuery();
                    if (rs.next()) {
                        Alert alert1 = new Alert(Alert.AlertType.ERROR);
                        alert1.setContentText("Must delete appointments with customer before deleting customer");
                        alert1.showAndWait();
                        return;
                    }
                } catch (SQLException var10) {
                    System.out.println("Customer not deleted");
                }

                this.customersObservableList.remove(customer);
                sql1 = "DELETE FROM customers WHERE Customer_ID = ?";
                ps1 = JDBC.connection.prepareStatement(sql1);
                ps1.setInt(1, deleteCustomerID);
                ps1.executeUpdate();
                Alert timedAlert = new Alert(Alert.AlertType.INFORMATION);
                timedAlert.setContentText("Customer has been deleted!");
                timedAlert.showAndWait();
            }
        }

    }

    /**
     * DELETE APPOINTMENT IF NO ERRORS
     * @param event
     * @throws SQLException
     * @throws InterruptedException
     * @throws IOException
     */
    public void deleteAppointment(ActionEvent event) throws SQLException, InterruptedException, IOException {
        Appointments appointment = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (appointment == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an Appointment to Delete");
            alert.showAndWait();
        } else {
            int deleteAppointmentId = appointment.getAppointmentId();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure you want to remove this Appointment?");
            Optional<ButtonType> result = alert.showAndWait();
            //DELETE ONLY IF THE USER PRESSES OK//
            if (result.get() == ButtonType.OK) {

                appointmentsObservableList.remove(appointment);
                String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
                PreparedStatement ps = JDBC.connection.prepareStatement(sql);
                ps.setInt(1, deleteAppointmentId);
                ps.executeUpdate();
                Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("seals/C195task/AppointmentCustomerTable-View.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
                String appointmentType = appointment.getAppointmentType();
                int appointmentId = appointment.getAppointmentId();
                Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
                alert2.setContentText("Appointment ID: " + appointmentId + " of type '"+ appointmentType + "' has been canceled!");
                alert2.showAndWait();
            }
        }
    }


    /**
     * SWITCH TO ADD APPOINTMENT SCENE
     * @param event
     * @throws IOException
     */
    public void addAppointmentBtnPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("seals/C195task/AddAppointment-View.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * IF NOT NULL, SWITCH TO UPDATE APPOINTMENT SCENE
     * @param event
     * @throws IOException
     */
    public void updateAppointmentBtnPushed(ActionEvent event) throws IOException {
        Appointments selectedCustomer = appointmentsTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select an Appointment to Update");
            alert.showAndWait();
        } else {
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("seals/C195task/UpdateAppointment-View.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            UpdateAppointmentController controller = loader.getController();
            controller.getSelectedAppointment(appointmentsTableView.getSelectionModel().getSelectedItem());
            stage.show();
        }
    }

    /**
     * TOGGLE BUTTON METHODS
     * VIEW APPOINTMENTS BY MONTH WHEN SELECTED
     * @param event
     * @throws SQLException
     */
    @FXML
    void viewAppointmentsByMonth(ActionEvent event) throws SQLException {
        if (appointmentsByMonth.isSelected()) {
            try {
                ObservableList<Appointments> appointments = getAppointmentsMonthly();
                appointmentsTableView.setItems(appointments);
                appointmentsTableView.refresh();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }
    //SELECT INFORMATION FROM DATABASE//
    public static ObservableList<Appointments> getAppointmentsMonthly() throws SQLException {

        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE month(Start) = month(now());";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        try {
            ps.execute();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                int contactID = rs.getInt("Contact_ID");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");

                Appointments appointmentsByMonth = new Appointments(appointmentID, title, description, location, contactID, type, start, end, customerID, userID);
                appointmentList.add(appointmentsByMonth);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appointmentList;
    }

    /**
     * VIEW APPOINTMENTS BY WEEK WHEN SELECTED
     * @param event
     * @throws SQLException
     */
    @FXML
    void viewAppointmentsByWeek(ActionEvent event) throws SQLException {
        if (appointmentsByWeek.isSelected()) {
            try {
                ObservableList<Appointments> appointments = getAppointmentsWeekly();
                appointmentsTableView.setItems(appointments);
                appointmentsTableView.refresh();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    //SELECT INFORMATION FROM DATABASE//
    public static ObservableList<Appointments> getAppointmentsWeekly() throws SQLException {

        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments WHERE Start BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 7 DAY)";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        try {
            ps.execute();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                int contactID = rs.getInt("Contact_ID");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");

                Appointments appointmentsByWeek = new Appointments(appointmentID, title, description, location, contactID, type, start, end, customerID, userID);
                appointmentList.add(appointmentsByWeek);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appointmentList;
    }

    /**
     * VIEW APPOINTMENTS BY ALL
     * @throws SQLException
     */
    @FXML
    void viewAppointmentsByAll() throws SQLException {
        if (appointmentsByAll.isSelected()) {
            try {
                ObservableList<Appointments> appointments = getAppointmentsByAll();
                appointmentsTableView.setItems(appointments);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
    //GET INFORMATION FROM DATABASE//
    public static ObservableList<Appointments> getAppointmentsByAll() throws SQLException {

        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments";

        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        try {
            ps.execute();
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                int contactID = rs.getInt("Contact_ID");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                int customerID = rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");

                Appointments appointmentsByAll = new Appointments(appointmentID, title, description, location, contactID, type, start, end, customerID, userID);
                appointmentList.add(appointmentsByAll);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appointmentList;
    }


    /**
     * SWITCH TO VIEW REPORT SCENE WHEN BUTTON IS PUSHED
     * @param event
     * @throws IOException
     */
    public void viewReportsBtnPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("seals/C195task/Reports-View.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    /**
     * SET CELL VALUES FOR CUSTOMER AND APPOINTMENTS TABLE
     * TOGGLEABLE GROUP ADDED FOR RADIO BUTTONS UPON LOADING SCENE
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //TOGGLEABLE GROUP//
        ToggleGroup viewAppointmentsBy = new ToggleGroup();
        appointmentsByAll.setToggleGroup(viewAppointmentsBy);
        appointmentsByWeek.setToggleGroup(viewAppointmentsBy);
        appointmentsByMonth.setToggleGroup(viewAppointmentsBy);
        appointmentsByAll.setSelected(true);

        //SET COLUMNS FOR CUSTOMERS TABLE//
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerPostalCodeCol.setCellValueFactory(new PropertyValueFactory<>("customerPostalCode"));
        customerPhoneCol.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
        customerDivisionCol.setCellValueFactory(new PropertyValueFactory<>("customerDivision"));
        customersObservableList = FXCollections.observableArrayList();
        loadDataFromCustomers();

        //SET COLUMNS FOR APPOINTMENTS TABLE//
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentTitleCol.setCellValueFactory(new PropertyValueFactory<>("appointmentTitle"));
        appointmentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("appointmentDescription"));
        appointmentLocationCol.setCellValueFactory(new PropertyValueFactory<>("appointmentLocation"));
        appointmentContactIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentContactId"));
        appointmentTypeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentType"));
        appointmentStartDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentStartTime"));
        appointmentEndDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("appointmentEndTime"));
        appointmentCustomerIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentCustomerId"));
        appointmentUserIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentUserId"));
        appointmentsObservableList = FXCollections.observableArrayList();
        loadDataFromAppointments();



    }


    /**
     * LOAD DATA INTO CUSTOMER OBSERVABLE LIST AND INSERT INTO TABLE COLUMNS
     */
    private void loadDataFromCustomers() {
        try {
            ps = JDBC.connection.prepareStatement("SELECT * FROM customers");
            rs = ps.executeQuery();
            while(rs.next()) {
                customersObservableList.add(new Customers(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getInt(10)));
            }

        } catch(SQLException ex) {
            Logger.getLogger(AppointmentCustomerTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
        customersTableView.setItems(customersObservableList);
    }

    /**
     * LOAD DATA INTO APPOINTMENTS OBSERVABLE LIST AND INSERT INTO TABLE COLUMNS
     */
    private void loadDataFromAppointments() {
        try {
            ps = JDBC.connection.prepareStatement("SELECT * FROM appointments");
            rs = ps.executeQuery();
            while (rs.next()) {
                appointmentsObservableList.add(new Appointments(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(14), rs.getString(5), rs.getTimestamp(6).toLocalDateTime(), rs.getTimestamp(7).toLocalDateTime(), rs.getInt(12), rs.getInt(13)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentCustomerTableController.class.getName()).log(Level.SEVERE, null, ex);
        }
        appointmentsTableView.setItems(appointmentsObservableList);
    }

    }

