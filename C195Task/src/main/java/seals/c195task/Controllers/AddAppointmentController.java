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
import java.sql.*;
import java.time.*;
import java.util.Iterator;
import java.util.ResourceBundle;

/***
 * Add Appointment Class
 */
public class AddAppointmentController implements Initializable {

    /**
     * CONNECT @FXML - SCENEBUILDER
     */
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

    /**
     * GET TIME ZONES FOR UTC AND EST
     */
    private final ZoneId zoneUTC = ZoneId.of("UTC");
    private final ZoneId zoneEST = ZoneId.of("America/New_York");


    /**
     * EXIT TO MAIN SCREEN WHEN EXIT BTN IS PUSHED
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
     * CHECK TO MAKE SURE CUSTOMER ID EXISTS BEFORE ADDING APPOINTMENT
     * @param customerId
     * @return
     * @throws SQLException
     */
    public static boolean checkForCustomerId(int customerId) throws SQLException {
        String sql = "SELECT Customer_ID FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, customerId);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }

    /**
     * POPULATE THE COMBO BOX FOR TIMES WITH 15 MINUTE INTERVALS
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
     * CONVERT CONTACT NAME SELECTED FROM DROP DOWN BOX INTO AN INTEGER TO STORE WITHIN DATABASE
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
     * Get the appointment where Customer ID matches
     * @param customerID
     * @return
     * @throws SQLException
     */
    public static ObservableList<Appointments> getAppointmentsByCustomerID(int customerID) throws SQLException {
        ObservableList<Appointments> appointmentList = FXCollections.observableArrayList();

        try {
            String sql = "SELECT * FROM appointments WHERE Customer_ID = ?;";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                int appointmentID = rs.getInt("Appointment_ID");
                String title = rs.getString("Title");
                String description = rs.getString("Description");
                String location = rs.getString("Location");
                int contactID = rs.getInt("Contact_ID");
                String type = rs.getString("Type");
                LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
                rs.getInt("Customer_ID");
                int userID = rs.getInt("User_ID");
                Appointments appointments = new Appointments(appointmentID, title, description, location, contactID, type, start, end, customerID, userID);
                appointmentList.add(appointments);
            }
        } catch (SQLException var15) {
            var15.printStackTrace();
        }

        return appointmentList;
    }


    /**
     * SET ALL COLUMN INFORMATION AND ALLOW FOR ADD APPOINTMENT
     * @param title
     * @param description
     * @param location
     * @param type
     * @param start
     * @param end
     * @param customerID
     * @param userID
     * @param contactID
     * @throws SQLException
     */
    public static void addAppointments(String title, String description, String location, String type, Timestamp start, Timestamp end, Integer customerID, Integer userID, String contactID) throws SQLException {
        try {
            String sql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Customer_ID, User_ID, Contact_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, description);
            ps.setString(3, location);
            ps.setString(4, type);
            ps.setObject(5, start);
            ps.setObject(6, end);
            ps.setInt(7, customerID);
            ps.setInt(8, userID);
            ps.setInt(9, convertContactNameToInteger(String.valueOf(contactID)));
            ps.execute();
        } catch (SQLIntegrityConstraintViolationException var11) {
            var11.printStackTrace();
        }

    }


    /**
     * ADD APPOINTMENT IF NO ERRORS
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    public void addAppointmentBtnPushed(ActionEvent event) throws SQLException, IOException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appointmentStarting = appointmentStartDate.getValue().atTime(LocalTime.from(now));
        //IF CUSTOMER ID CONTAINS LETTERS, ALERT THE USER//
        if (!appointmentCustomerId.getText().matches("[0-9]*")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Customer ID must be an Integer");
            alert.showAndWait();
        } else if (appointmentStarting.isBefore(now)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Appointment can only be scheduled on todays date or in the future");
            alert.showAndWait();
        } else {
            //ASSIGN VARIABLES TO HOLD VALUES OF EACH FIELD//
            try {
                int userId = this.appointmentUserId.getValue();
                int customerId = Integer.parseInt(this.appointmentCustomerId.getText());
                //CHECK TO SEE IF CUSTOMER ID EXISTS WITHIN THE DATABASE//
                if (!checkForCustomerId(customerId)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Customer with that ID does not exist");
                    alert.showAndWait();
                } else {
                    String title = this.appointmentTitle.getText();
                    String description = this.appointmentDescription.getText();
                    String location = this.appointmentLocation.getText();
                    String contactId = this.appointmentContact.getValue();
                    String type = this.appointmentType.getText();
                    //PARSE DATE AND TIME//
                    LocalDateTime appointmentStart = LocalDateTime.of(this.appointmentStartDate.getValue(), LocalTime.parse(this.appointmentStartTime.getSelectionModel().getSelectedItem()));
                    LocalDateTime appointmentEnd = LocalDateTime.of(this.appointmentEndDate.getValue(), LocalTime.parse(this.appointmentEndTime.getSelectionModel().getSelectedItem()));
                    //GET START AND END TIME IN UTC USING ZONE ID//
                    ZonedDateTime startToUtc = appointmentStart.atZone(this.zoneUTC).withZoneSameInstant(ZoneId.of("UTC"));
                    ZonedDateTime endToUtc = appointmentEnd.atZone(this.zoneUTC).withZoneSameInstant(ZoneId.of("UTC"));
                    //CONVERT TO TIMESTAMP//
                    Timestamp startTimestampToUtc = Timestamp.valueOf(startToUtc.toLocalDateTime());
                    Timestamp endTimestampToUtc = Timestamp.valueOf(endToUtc.toLocalDateTime());
                    Alert alert;
                    //IF START DATE AND END DATE ARE NOT THE SAME - ALERT THE USER//
                    if (!this.appointmentStartDate.getValue().equals(this.appointmentEndDate.getValue())) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Appointment start and end must be scheduled on same day");
                        alert.showAndWait();
                    //IF START TIME IS AFTER END TIME - ALERT THE USER//
                    } else if (appointmentStart.isAfter(appointmentEnd)) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setContentText("Appointment start time must be before appointment end time");
                        alert.showAndWait();
                    } else {
                        //CONVERT START AND END INTO EST TO COMPARE BUSINESS HOURS//
                        ZonedDateTime startOfDayEst = ZonedDateTime.of(appointmentStart, this.zoneEST);
                        ZonedDateTime endOfDayEst = ZonedDateTime.of(appointmentEnd, this.zoneEST);
                        //ALERT USER IF START OR END TIME IS NOT WITHIN 0800EST - 2200EST//
                        if (startOfDayEst.toLocalTime().isBefore(LocalTime.of(8, 0)) || endOfDayEst.toLocalTime().isBefore(LocalTime.of(8, 0)) || startOfDayEst.toLocalTime().isAfter(LocalTime.of(22, 0)) || endOfDayEst.toLocalTime().isAfter(LocalTime.of(22, 0))) {
                            Alert alert1 = new Alert(Alert.AlertType.ERROR);
                            alert1.setContentText("Schedule during business hours only");
                            alert1.showAndWait();
                        } else {
                            //STORE APPOINTMENT//
                            ObservableList<Appointments> appointments = getAppointmentsByCustomerID(Integer.parseInt(this.appointmentCustomerId.getText()));
                            Iterator var = appointments.iterator();

                            Timestamp startTimestamp;
                            Timestamp endTimestamp;
                            do {
                                if (!var.hasNext()) {
                                    //IF ALL TEXT FIELDS != " "//
                                    if (!this.appointmentDescription.getText().isEmpty() && !this.appointmentTitle.getText().isEmpty() && !this.appointmentDescription.getText().isEmpty() && !this.appointmentLocation.getText().isEmpty() && !this.appointmentType.getText().isEmpty()) {
                                        //ADD APPOINTMENT//
                                        addAppointments(title, description, location, type, startTimestampToUtc, endTimestampToUtc, customerId, userId, contactId);
                                        //GO BACK TO MAIN SCREEN TO SHOW APPOINTMENTS TABLE//
                                        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                                        FXMLLoader loader = new FXMLLoader(this.getClass().getClassLoader().getResource("seals/C195task/AppointmentCustomerTable-View.fxml"));
                                        Parent root = loader.load();
                                        Scene scene = new Scene(root);
                                        stage.setScene(scene);
                                        stage.show();
                                    } else {
                                        //IF FIELDS ARE EMPTY ALERT THE USER//
                                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                                        alert2.setContentText("Do not leave any empty fields");
                                        alert2.showAndWait();
                                    }
                                    return;
                                }
                                //EXISTING APPOINTMENT AND CURRENT APPOINTMENT//
                                Appointments existingAppointment = (Appointments) var.next();
                                LocalDateTime existingAppointmentStart = existingAppointment.getAppointmentStartTime();
                                LocalDateTime existingAppointmentEnd = existingAppointment.getAppointmentEndTime();
                                //PARSE TO TIMESTAMP//
                                startTimestamp = Timestamp.valueOf(existingAppointmentStart);
                                endTimestamp = Timestamp.valueOf(existingAppointmentEnd);
                                //CHECK TO MAKE SURE CURRENT APPOINTMENT IS NOT CREATED DURING EXISTING APPOINTMENT//
                            } while ((!startTimestampToUtc.after(startTimestamp) || !startTimestampToUtc.before(endTimestamp)) && (!endTimestampToUtc.after(startTimestamp) || !endTimestampToUtc.before(endTimestamp)) && (!startTimestampToUtc.before(startTimestamp) || !endTimestampToUtc.after(startTimestamp)) && (!startTimestampToUtc.equals(startTimestamp) || !endTimestampToUtc.equals(endTimestamp)) && !startTimestampToUtc.equals(startTimestamp) && !endTimestampToUtc.equals(startTimestamp));
                            //ALERT THE USER//
                            Alert alert3 = new Alert(Alert.AlertType.ERROR);
                            alert3.setContentText("Appointment cannot be scheduled during another appointment with same Customer ID");
                            alert3.showAndWait();
                        }
                    }
                }
            } catch (SQLException var25) {
                var25.printStackTrace();
            }
        }
    }


    /**
     * LOAD DROP DOWNS UPON LOADING SCENE
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


