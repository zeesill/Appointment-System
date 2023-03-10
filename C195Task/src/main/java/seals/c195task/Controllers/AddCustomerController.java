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

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ALLOW USER TO ADD CUSTOMER
 */
public class AddCustomerController implements Initializable {

    /**
     * CONNECT FXML - SCENEBUILDER
     */
    @FXML private TextField customerNameTextField;
    @FXML private TextField customerAddressTextField;
    @FXML private TextField customerPostalCodeTextField;
    @FXML private TextField customerPhoneNumberTextField;
    @FXML private ComboBox customerCountryComboBox;
    @FXML private ComboBox customerStateComboBox;


    @FXML private Label stateLabel;


    /**
     * GET DIVISION ID FROM COUNTRY/STATE SELECTED
     * @param division
     * @return
     * @throws SQLException
     */
    public static String selectDivisionId(String division) throws SQLException {

        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, division);
        ResultSet rs = ps.executeQuery();
        String Division_ID = null;
        while (rs.next()) {
            Division_ID = rs.getString("Division_ID");
        }
        return Division_ID;
    }

    /**
     * CREATE A METHOD THAT INSERTS INTO DATABASE AND PUT IT INTO ADD CUSTOMER BUTTON METHOD
     * @throws SQLException
     */
    public void insertCustomer() throws SQLException {

        String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) VALUES(?, ?, ?, ?, ?)";
        PreparedStatement ps =  JDBC.connection.prepareStatement(sql);
        ps.setString(1, customerNameTextField.getText());
        ps.setString(2, customerAddressTextField.getText());
        ps.setString(3, customerPostalCodeTextField.getText());
        ps.setString(4, customerPhoneNumberTextField.getText());
        ps.setString(5, selectDivisionId(customerStateComboBox.getValue().toString()));
        ps.executeUpdate();
    }


    /**
     * ADDS CUSTOMER TO DATABASE
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    public void addCustomerBtn(ActionEvent event) throws SQLException, IOException {
        //ERROR CONTROL FOR ADD CUSTOMER//
        if(customerNameTextField.getText().isEmpty() || customerAddressTextField.getText().isEmpty() || customerPhoneNumberTextField.getText().isEmpty() || customerPostalCodeTextField.getText().isEmpty()) {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setContentText("One or more Textfields are empty");
            alert.showAndWait();
        } else if (customerCountryComboBox.getValue() == null || customerStateComboBox.getValue() == null) {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setContentText("One or more Dropdowns are empty");
            alert.showAndWait();
        } else if (customerNameTextField.getText().matches(".*\\d.*")) {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setContentText("Customer Name must only be letters");
            alert.showAndWait();
        } else if (!customerPhoneNumberTextField.getText().matches(".*\\d.*")) {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setContentText("Customer Phone Number must only be numbers");
            alert.showAndWait();
        }
        else {
            insertCustomer();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("seals/C195task/AppointmentCustomerTable-View.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * EXIT BUTTON PUSHED
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
     * POPULATE STATE AND COUNTRY LIST - CHANGE LABEL UPON WHICH COUNTRY USER SELECTS
     * @param event
     */
    public void countrySelect(ActionEvent event) {
        if(customerCountryComboBox.getValue().equals("U.S.")){
            stateLabel.setText("State");

            ObservableList<String> stateList = FXCollections.observableArrayList( "Alabama", "Arizona",  "Arkansas",
                    "California",  "Colorado",  "Connecticut",  "Delaware", "District of Columbia",  "Florida",  "Georgia",  "Idaho",
                    "Illinois", "Indiana",  "Iowa", "Kansas",  "Kentucky", "Louisiana", "Maine",  "Maryland", "Massachusetts",  "Michigan",  "Minnesota",
                    "Mississippi", "Missouri",  "Montana",  "Nebraska",  "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
                    "North Carolina", "North Dakota","Ohio",  "Oklahoma", "Oregon", "Pennsylvania",  "Rhode Island", "South Carolina", "South Dakota",
                    "Tennessee",  "Texas",  "Utah",  "Vermont", "Virginia", "Washington",  "West Virginia",  "Wisconsin",
                    "Wyoming",  "Hawaii", "Alaska");
            customerStateComboBox.setItems(stateList);

        } else if(customerCountryComboBox.getValue().equals("UK")){
            stateLabel.setText("Country Within");
            ObservableList<String> countryWithinList = FXCollections.observableArrayList("England", "Wales", "Scotland", "Northern Ireland");
            customerStateComboBox.setItems(countryWithinList);

        } else if(customerCountryComboBox.getValue().equals("Canada")){
            stateLabel.setText("Province");
            ObservableList<String> provinceList = FXCollections.observableArrayList("Northwest Territories", "Alberta",  "British Columbia", "Manitoba", "New Brunswick", "Nova Scotia",
                     "Prince Edward Island",  "Ontario",  "QuÃ©bec",  "Saskatchewan",  "Nunavut", "Yukon", "Newfoundland and Labrador");
            customerStateComboBox.setItems(provinceList);
        }
    }


    /**
     * POPULATE COUNTRY LIST UPON LOADING SCENE
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        ObservableList<String> countryList = FXCollections.observableArrayList("U.S.", "UK", "Canada");
        customerCountryComboBox.setItems(countryList);
    }
}

