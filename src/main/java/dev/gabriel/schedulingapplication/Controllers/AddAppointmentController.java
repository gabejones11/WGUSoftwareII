package dev.gabriel.schedulingapplication.Controllers;

import dev.gabriel.schedulingapplication.Models.Appointments;
import dev.gabriel.schedulingapplication.Models.Contacts;
import dev.gabriel.schedulingapplication.Models.Customers;
import dev.gabriel.schedulingapplication.Models.Users;
import dev.gabriel.schedulingapplication.dbhelper.DBAppointments;
import dev.gabriel.schedulingapplication.dbhelper.DBContacts;
import dev.gabriel.schedulingapplication.dbhelper.DBCustomers;
import dev.gabriel.schedulingapplication.dbhelper.DBUsers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;

import static dev.gabriel.schedulingapplication.Main.switchScreen;
import static dev.gabriel.schedulingapplication.Utility.Utility.*;

public class AddAppointmentController implements Initializable {
    @FXML
    private TextField appointmentIDField;
    @FXML
    private TextField appointmentTitleField;
    @FXML
    private TextField appointmentTypeField;
    @FXML
    private TextField appointmentDescriptionField;
    @FXML
    private TextField appointmentLocationField;
    @FXML
    private DatePicker appointmentStartDateField;
    @FXML
    private DatePicker appointmentEndDateField;
    @FXML
    private ComboBox appointmentStartTimeField;
    @FXML
    private ComboBox appointmentEndTimeField;
    @FXML
    private ChoiceBox customerIDField;
    @FXML
    private ChoiceBox userIDField;
    @FXML
    private ChoiceBox contactIDField;
    @FXML
    private Button cancelButton;
    @FXML
    private Button saveButton;

    /**
     * Handles user pressing the cancel button, sends the user back to the appointments screen
     * @throws IOException
     */
    public void handleCancelButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/appointment-view.fxml", cancelButton);
    }

    /**
     * Handles all the validation (validation functions in utility class) needed to save an appointment to the database and create an appointment object
     * @throws IOException
     * @throws SQLException
     */
    public void handleSaveButton() throws IOException, SQLException {
        //grab all the input from the fields
        String title = appointmentTitleField.getText();
        String type = appointmentTypeField.getText();
        String description = appointmentDescriptionField.getText();
        String location = appointmentLocationField.getText();
        LocalDate startDate = appointmentStartDateField.getValue();
        LocalDate endDate = appointmentEndDateField.getValue();
        LocalTime startTime = (LocalTime) appointmentStartTimeField.getValue();
        LocalTime endTime = (LocalTime) appointmentEndTimeField.getValue();
        Integer customerID = (Integer) customerIDField.getValue();
        Integer userID = (Integer) userIDField.getValue();
        String contactName = (String) contactIDField.getValue();
        //get the id for saving purposes
        Integer contactID = DBContacts.getContactID(contactName);

        //validate input fields using utility class
        if (!validateAppointmentTitle(title)) {
            return;
        }

        if (!validateAppointmentType(type)) {
            return;
        }

        if (!validateAppointmentDescription(description)) {
            return;
        }

        if (!validateAppointmentLocation(location)) {
            return;
        }

        if (!validateAppointmentStartEndDateTime(startTime, endTime, startDate, endDate)) {
            return;
        }

        //after validating that these fields aren't null we can combine our fields to make a LocalDateTime object for saving purposes
        LocalDateTime start = startDate.atTime(startTime);
        LocalDateTime end = endDate.atTime(endTime);

        if (!validateAppointmentCustomerID(customerID)) {
            return;
        }

        if (!validateAppointmentUserID(userID)) {
            return;
        }

        if (!validateAppointmentContactID(contactID)) {
            return;
        }

        //here we set appointment id to -1 for our validation because we know that this will have a unique id, and we don't need to check for modification
        //of an appointment with the same id
        if (!validateAppointmentCollisions(start, end, customerID, -1)){
            return;
        }

        //I want to keep the appointment object consistent with what the database has for the id, so I decided to add the object to the database
        //first, so we can get the id and then set the object afterwards, so we can have the generated id from the database for our object
        int generatedID = DBAppointments.addAppointment(title, description, location, type, start, end, customerID, userID, contactID);
        Appointments appointment = new Appointments(generatedID, title, description, location, type, start, end, customerID, userID, contactID);
        switchScreen("/dev/gabriel/schedulingapplication/Views/appointment-view.fxml", saveButton);
    }


    /**
     * Initialization of this screen, pre-populates customerID, UserID, Contact name, and time choice boxes
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //populate customerID choice box
        ObservableList<Integer> customerIDList = FXCollections.observableArrayList();
        for (Customers customer : DBCustomers.getAllCustomers()) {
            customerIDList.add(customer.getCustomerID());
        }
        customerIDField.setItems(customerIDList);

        //populate userID choice box
        ObservableList<Integer> userIDList = FXCollections.observableArrayList();
        for (Users user : DBUsers.getAllUsers()) {
            userIDList.add(user.getId());
        }
        userIDField.setItems(userIDList);

        //populate contact choice box
        ObservableList<String> contactList = FXCollections.observableArrayList();
        for (Contacts contact : DBContacts.getAllContacts()) {
            contactList.add(contact.getContactName());
        }
        contactIDField.setItems(contactList);

        //populate time list start the time at midnight and loop until it reaches 11:45 loop will stop once it sees 11:45, so we have to manually add it
        ObservableList<LocalTime> timesList = FXCollections.observableArrayList();
        LocalTime time = LocalTime.of(0,0);

        while (!time.equals(LocalTime.of(23,45))) {
            timesList.add(time);
            time = time.plusMinutes(15);
        }
        timesList.add(LocalTime.of(23, 45));

        appointmentStartTimeField.setItems(timesList);
        appointmentEndTimeField.setItems(timesList);
    }
}
