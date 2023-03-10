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
import seals.c195task.Models.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * ALLOWS USER TO UPDATE CUSTOMER
 */
public class UpdateCustomerController implements Initializable {
    @FXML private TextField customerNameTextField;
    @FXML private TextField customerAddressTextField;
    @FXML private TextField customerPostalCodeTextField;
    @FXML private TextField customerPhoneTextField;
    @FXML private ComboBox customerCountryComboBox;
    @FXML private ComboBox customerStateComboBox;

    @FXML private Label stateLabel;

    @FXML private Customers selectedCustomerFromTable;


    /**
     * CHANGE LABEL BASED ON CHANGING COUNTRY
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
     * CONVERT DIVISION ID TO COUNTRY AND STATE FOR COMBO BOX
     * @param divisionId
     * @return
     * @throws SQLException
     */
    public String convertDivisionIdToState(int divisionId) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, String.valueOf(divisionId));
        ResultSet rs = ps.executeQuery();
        String divisionToState = null;
        while (rs.next()) {
            divisionToState = rs.getString("Division");

        }
        return divisionToState;
    }

    /**
     * GET SELECTED CUSTOMER
     * @param customer
     * @throws SQLException
     */
    public void getSelectedCustomer(Customers customer) throws SQLException {
        selectedCustomerFromTable = customer;
        customerNameTextField.setText(customer.getCustomerName());
        customerAddressTextField.setText(customer.getCustomerAddress());
        customerPostalCodeTextField.setText(customer.getCustomerPostalCode());
        customerPhoneTextField.setText(customer.getCustomerPhone());
        customerStateComboBox.setValue(convertDivisionIdToState(customer.getCustomerDivision()));


        /**
         * CONVERT DIVISION ID INTO LIST FOR STATE COMBO BOX
         */
        String divisionToState = convertDivisionIdToState(customer.getCustomerDivision());

        String sql = "SELECT COUNTRY_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, String.valueOf(divisionToState));
        ResultSet rs = ps.executeQuery();
        int divisionToCountry = 0;
        while (rs.next()) {
            divisionToCountry = rs.getInt("COUNTRY_ID");
        }
        if (divisionToCountry == 1) {
            String divisionToCountryName = "U.S.";
            customerCountryComboBox.setValue(divisionToCountryName);
            stateLabel.setText("State");

            ObservableList<String> stateList = FXCollections.observableArrayList( "Alabama", "Arizona",  "Arkansas",
                    "California",  "Colorado",  "Connecticut",  "Delaware", "District of Columbia",  "Florida",  "Georgia",  "Idaho",
                    "Illinois", "Indiana",  "Iowa", "Kansas",  "Kentucky", "Louisiana", "Maine",  "Maryland", "Massachusetts",  "Michigan",  "Minnesota",
                    "Mississippi", "Missouri",  "Montana",  "Nebraska",  "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York",
                    "North Carolina", "North Dakota","Ohio",  "Oklahoma", "Oregon", "Pennsylvania",  "Rhode Island", "South Carolina", "South Dakota",
                    "Tennessee",  "Texas",  "Utah",  "Vermont", "Virginia", "Washington",  "West Virginia",  "Wisconsin",
                    "Wyoming",  "Hawaii", "Alaska");
            customerStateComboBox.setItems(stateList);
        } else if (divisionToCountry == 2) {
            String divisionToCountryName = "UK";
            customerCountryComboBox.setValue(divisionToCountryName);
            stateLabel.setText("Country Within");
            ObservableList<String> countryWithinList = FXCollections.observableArrayList("England", "Wales", "Scotland", "Northern Ireland");
            customerStateComboBox.setItems(countryWithinList);
        } else if (divisionToCountry == 3) {
            String divisionToCountryName = "Canada";
            customerCountryComboBox.setValue(divisionToCountryName);
            stateLabel.setText("Province");
            ObservableList<String> provinceList = FXCollections.observableArrayList("Northwest Territories", "Alberta",  "British Columbia", "Manitoba", "New Brunswick", "Nova Scotia",
                    "Prince Edward Island",  "Ontario",  "QuÃ©bec",  "Saskatchewan",  "Nunavut", "Yukon", "Newfoundland and Labrador");
            customerStateComboBox.setItems(provinceList);
        }


    }


    /**
     * METHOD TO GET DIVISION ID
     * @param division
     * @return
     * @throws SQLException
     */
    public static int selectDivisionId(String division) throws SQLException {

        String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, division);
        ResultSet rs = ps.executeQuery();
        int Division_ID = 0;
        while (rs.next()) {
            Division_ID = rs.getInt("Division_ID");
        }
        return Division_ID;
    }

    /**
     * METHOD TO UPDATE CUSTOMER WHEN BUTTON IS PUSHED
     * @param event
     * @throws SQLException
     * @throws IOException
     */
    public void updateCustomerBtnPushed(ActionEvent event) throws SQLException, IOException {
        int updatedCustomerId = selectedCustomerFromTable.getCustomerId();
        //ERROR CONTROL FOR ADD CUSTOMER//
        if(customerNameTextField.getText().isEmpty() || customerAddressTextField.getText().isEmpty() || customerPhoneTextField.getText().isEmpty() || customerPostalCodeTextField.getText().isEmpty()) {
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
        } else if (!customerPhoneTextField.getText().matches(".*\\d.*")) {
            Alert alert = new Alert (Alert.AlertType.ERROR);
            alert.setContentText("Customer Phone Number must only be numbers");
            alert.showAndWait();
        } else {
            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? WHERE Customer_ID = ?";
            PreparedStatement ps = JDBC.connection.prepareStatement(sql);
            ps.setString(1, customerNameTextField.getText());
            ps.setString(2, customerAddressTextField.getText());
            ps.setString(3, customerPostalCodeTextField.getText());
            ps.setString(4, customerPhoneTextField.getText());
            ps.setInt(5, selectDivisionId(customerStateComboBox.getValue().toString()));
            ps.setInt(6, updatedCustomerId);
            ps.executeUpdate();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("seals/C195task/AppointmentCustomerTable-View.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }


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
     * POPULATE COUNTRY COMBO BOX
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> countryList = FXCollections.observableArrayList("U.S.", "UK", "Canada");
        customerCountryComboBox.setItems(countryList);
    }
}
