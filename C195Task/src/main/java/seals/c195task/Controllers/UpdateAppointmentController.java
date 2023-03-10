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
import javafx.stage.Stage;
import seals.c195task.Helper.JDBC;
import seals.c195task.Models.Appointments;
import seals.c195task.Models.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.ResourceBundle;

import static seals.c195task.Controllers.AddAppointmentController.*;

/**
 * ALLOWS USER TO UPDATE APPOINTMENTS
 */
public class UpdateAppointmentController implements Initializable {

    @FXML
    private TextField appointmentTitle;
    @FXML
    private TextField appointmentLocation;
    @FXML
    private ComboBox<String> appointmentContact;
    @FXML
    private TextField appointmentType;
    @FXML
    private DatePicker appointmentStartDate;
    @FXML
    private ComboBox<String> appointmentStartTime;
    @FXML
    private DatePicker appointmentEndDate;
    @FXML
    private ComboBox<String> appointmentEndTime;
    @FXML
    private TextField appointmentCustomerId;
    @FXML
    private ComboBox<Integer> appointmentUserId;
    @FXML
    private TextArea appointmentDescription;

    private int appointmentId;

    @FXML private Appointments selectedAppointmentFromTable;

    private final ZoneId zoneUTC = ZoneId.of("UTC");
    private final ZoneId zoneEST = ZoneId.of("America/New_York");


    /**
     * EXIT BTN PUSHED
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
     * GET SELECTED APPOINTMENT TO UPDATE FROM MAIN SCREEN AND SET THE VALUES
     * @param appointment
     */
    public void getSelectedAppointment(Appointments appointment) {
        selectedAppointmentFromTable = appointment;
        appointmentId = appointment.getAppointmentId();
        appointmentTitle.setText(selectedAppointmentFromTable.getAppointmentTitle());
        appointmentLocation.setText(selectedAppointmentFromTable.getAppointmentLocation());
        appointmentContact.setValue(convertContactIntegerToName(selectedAppointmentFromTable.getAppointmentContactId()));
        appointmentType.setText(selectedAppointmentFromTable.getAppointmentType());
        appointmentStartDate.setValue(selectedAppointmentFromTable.getAppointmentStartTime().toLocalDate());
        appointmentStartTime.setValue(String.valueOf(selectedAppointmentFromTable.getAppointmentStartTime().toLocalTime()));
        appointmentEndDate.setValue(selectedAppointmentFromTable.getAppointmentEndTime().toLocalDate());
        appointmentEndTime.setValue(String.valueOf(selectedAppointmentFromTable.getAppointmentEndTime().toLocalTime()));
        appointmentCustomerId.setText(String.valueOf(selectedAppointmentFromTable.getAppointmentCustomerId()));
        appointmentUserId.setValue(selectedAppointmentFromTable.getAppointmentUserId());
        appointmentDescription.setText(selectedAppointmentFromTable.getAppointmentDescription());
    }

    /**
     * SQL UPDATE APPOINTMENTS
     * @param title
     * @param description
     * @param location
     * @param contactId
     * @param type
     * @param start
     * @param end
     * @param customerId
     * @param userId
     * @throws SQLException
     */

    public void updateAppointments(String title, String description, String location, String contactId, String type, Timestamp start, Timestamp end, int customerId, int userId) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, appointmentTitle.getText());
        ps.setString(2, appointmentDescription.getText());
        ps.setString(3, appointmentLocation.getText());
        ps.setString(4, appointmentType.getText());
        ps.setTimestamp(5, start);
        ps.setTimestamp(6, end);
        ps.setString(7, appointmentCustomerId.getText());
        ps.setInt(8, appointmentUserId.getValue());
        ps.setInt(9, convertContactNameToInteger(appointmentContact.getValue()));
        ps.setString(10, String.valueOf(appointmentId));
        ps.executeUpdate();
    }

    /**
     * CONVERT CUSTOMER NAME TO INTEGER
     * @param contactName
     * @return
     */
    public static int convertContactNameToInteger(String contactName) {
        if (contactName == "Anika Costa") {
            return 1;
        } else {
            return contactName == "Daniel Garcia" ? 2 : 3;
        }
    }

    /**
     * CONVERT INTEGER TO CUSTOMER NAME
     * @param contactId
     * @return
     */
    public static String convertContactIntegerToName(int contactId) {
        if(contactId == 1) {
            return "Anika Costa";
        } else if (contactId == 2) {
            return "Daniel Garcia";
        } else {
            return "Li Lee";
        }
    }


    //TESTING//
    public void updateAppointmentsBtnPushed(String title, String description, String location, String contactId, String type, Timestamp start, Timestamp end, int customerId, int userId) throws SQLException {
            String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, appointmentTitle.getText());
            ps.setString(2, appointmentDescription.getText());
            ps.setString(3, appointmentLocation.getText());
            ps.setString(4, appointmentType.getText());
            ps.setTimestamp(5, start);
            ps.setTimestamp(6, end);
            ps.setString(7, appointmentCustomerId.getText());
            ps.setInt(8, appointmentUserId.getValue());
            ps.setInt(9, convertContactNameToInteger(appointmentContact.getValue()));
            ps.setString(10, String.valueOf(appointmentId));
            ps.executeUpdate();

    }



    /**
     * POPULATE TIME COMBO BOX
     */
    private void populateAppointmentTime() {
        ObservableList<String> times = FXCollections.observableArrayList();
        LocalTime startTime = LocalTime.of(7, 0);
        LocalTime endTime = LocalTime.of(23, 0);
        times.add(startTime.toString());

        while(startTime.isBefore(endTime)) {
            startTime = startTime.plusMinutes(15L);
            times.add(startTime.toString());
        }

        this.appointmentStartTime.setItems(times);
        this.appointmentEndTime.setItems(times);
    }

    /**
     * POPULATE CONTACTS AND USERS
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //SET VALUES FOR CONTACT AND USER ID DROPDOWNS//
        ObservableList<String> contacts = FXCollections.observableArrayList("Anika Costa", "Daniel Garcia", "Li Lee");
        this.appointmentContact.setItems(contacts);
        this.populateAppointmentTime();
        ObservableList<Integer> users = FXCollections.observableArrayList(1, 2);
        this.appointmentUserId.setItems(users);
    }

}
