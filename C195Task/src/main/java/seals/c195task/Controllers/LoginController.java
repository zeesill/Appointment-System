package seals.c195task.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import seals.c195task.Helper.JDBC;
import seals.c195task.RunApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * LAMBDA EXPRESSION #1 RESIDES IN INITIALIZE METHOD AT BOTTOM
 * CHECK FOR CORRECT USERNAME AND PASSWORD
 * CHANGE LANGUAGE TO FRENCH IF == LOCATION USING RESOURCE BUNDLE
 * SHOW BOTH VALID AND INVALID LOGIN ATTEMPTS WITHIN login_activity.txt
 */
public class LoginController implements Initializable, TimeZones {

    /**
     * CONNECT FXML - SCENEBUILDER
     */
    @FXML private Label timeZoneLabel;

    @FXML private TextField usernameTextField;

    @FXML private PasswordField passwordTextField;

    @FXML private Label passwordLabel;
    @FXML private Label usernameLabel;
    @FXML private Button loginBtnText;
    @FXML private Label appointmentSchedulingLabel;
    @FXML private Label systemLabel;


    /**
     * GET USERNAME AND PASSWORD IF MATCHES SWITCH SCENE, ELSE ALERT
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    public void loginBtnPushed(ActionEvent event) throws SQLException, IOException {


        String user = usernameTextField.getText();
        String pass = passwordTextField.getText();
        createFile();

        String sql = "SELECT * FROM users WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, user);
        ps.setString(2, pass);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            //ADD METHOD TO SWITCH SCENE//
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("seals/C195task/AppointmentCustomerTable-View.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            validLogin();
            alertUserAppointment();


            System.out.println("Login Successful");
        } else {
            if (user.equals("") || pass.equals("")) {
                if (Locale.getDefault().getLanguage().equals("fr")) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Entrez des valeurs pour chaque champ");
                    alert.showAndWait();
                    return;
                }
                    else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Enter Values For Each Field");
                    alert.showAndWait();
                    return;
                    }
            }
            if (Locale.getDefault().getLanguage().equals("fr")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Nom d’utilisateur et mot de passe incorrects");
                alert.showAndWait();
            }
            else {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Incorrect Username or Password");
                alert.showAndWait();
                invalidLogin();
            }
        }
    }

    /**
     * CREATE FILE
     */
    private void createFile(){
        File createFile = new File("target/classes/seals/c195task/login_activity.txt");
    }

    /**
     * IF VALID WRITE TO FILE - INCLUDE USERNAME PASSWORD DATE
     */
    private void validLogin() {
        try {
            FileWriter fileWriter = new FileWriter("target/classes/seals/c195task/login_activity.txt", true);
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            fileWriter.write("Valid Login: Username: " + usernameTextField.getText() + ", Password: " + passwordTextField.getText() + " at Date/Time of: " + now + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * IF INVALID WRITE TO FILE - INCLUDE USERNAME PASSWORD DATE
     */
    private void invalidLogin() {
        try {
            FileWriter fileWriter = new FileWriter("target/classes/seals/c195task/login_activity.txt", true);
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            fileWriter.write("Invalid Login: Username: " + usernameTextField.getText() + ", Password: " + passwordTextField.getText() + " at Date/Time of: " + now + "\n");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * NOTIFY USER IF APPOINTMENT STARTS WITHIN 15 MINUTES OF SIGNING IN
     * @throws SQLException
     */
    public void alertUserAppointment() throws SQLException {
        String sql = "SELECT * FROM appointments WHERE `Start` BETWEEN NOW() AND DATE_ADD(NOW(), INTERVAL 15 minute)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            String appointmentInfo = rs.getString("Appointment_ID");
            Timestamp dateTimeOfAppointment = rs.getTimestamp("Start");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Welcome! \n Appointment with the ID of " + appointmentInfo + " will begin at " + dateTimeOfAppointment);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Welcome! There are no appointments within 15 minutes");
            alert.showAndWait();
        }
    }


    /**
     * SET TEXT BASED ON ZONE ID
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle){
        if (Locale.getDefault().getLanguage().equals("fr")) {
            ResourceBundle rb = ResourceBundle.getBundle("seals/c195task/fr", Locale.getDefault());
            passwordLabel.setText(rb.getString("Password"));
            usernameLabel.setText(rb.getString("Username"));
            appointmentSchedulingLabel.setText(rb.getString("Appointment"));
            systemLabel.setText(rb.getString("System"));
            loginBtnText.setText(rb.getString("Login"));

            timeZoneLabel.setText("Emplacement: " + ZoneId.systemDefault());
        } else {
            //TIMEZONE ON LOGIN SCREEN/
            /**
             * LAMBDA EXPRESSION #1
             * SETS ZONE ID AND SETS LOCATION INFORMATION FOR THE USER BASED ON LOCATION
             */
            ZoneId myZoneId = ZoneId.systemDefault();
            TimeZones timeZones = (zoneId) -> {
                timeZoneLabel.setText("Location: " + zoneId);
            };
            timeZones.setText(myZoneId);
        }
    }




































    @Override
    public void setText(ZoneId zoneid) {

    }
}