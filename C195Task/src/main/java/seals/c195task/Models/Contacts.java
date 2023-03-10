package seals.c195task.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.SQLException;

/**
 * CONTACTS FIELDS, CONSTRUCTORS, GETTERS AND SETTERS
 */
public class Contacts {

    int contactId;
    String contactName;
    String contactEmail;

    ObservableList<String> contacts = FXCollections.observableArrayList();

    public Contacts(int contactId, String contactName, String contactEmail) throws SQLException {
        this.contactId = contactId;
        this.contactName = contactName;
        this.contactEmail = contactEmail;
    }


    //CONVERT CONTACT NAME TO A CONTACT ID//
    public static int convertToContactId(String name) {
        if (name.equals("Anika Costa")) {
            return 1;
        } else if (name.equals("Daniel Garcia")){
            return 2;
        } else {
            return 3;
        }
    }


}
