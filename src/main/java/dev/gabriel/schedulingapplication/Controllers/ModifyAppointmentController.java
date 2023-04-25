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

public class ModifyAppointmentController implements Initializable {
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
    private static Appointments dataPass;

    /**
     * This method is used in our appointment controller and just passes the appointment model that is selected from the table
     * to this screen so that we can pre-fill the information in the form
     * @param appointment
     */
    public static void handleDataPass(Appointments appointment) {
        dataPass = appointment;
    }

    /**
     * This method handles the save button and executes all the validation (validation functions in Utility Class) needed to modify
     * an appointment
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

        if (!validateAppointmentCollisions(start, end, customerID, dataPass.getAppointmentID())){
            return;
        }

        Appointments appointment = new Appointments(dataPass.getAppointmentID(), title, description, location, type, start, end, customerID, userID, contactID);
        DBAppointments.updateAppointment(appointment);
        switchScreen("/dev/gabriel/schedulingapplication/Views/appointment-view.fxml", saveButton);
    }

    /**
     * Handles the cancel button switches back to the appointments screen
     * @throws IOException
     */
    public void handleCancelButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/appointment-view.fxml", cancelButton);
    }

    /**
     * Initialization of this screen, pre-fills the form with the information gathered from our data pass object, also sets the customerID, UserID,
     * Contact Name, and Time choice box items
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        appointmentIDField.setText(String.valueOf(dataPass.getAppointmentID()));
        appointmentTitleField.setText(dataPass.getTitle());
        appointmentTypeField.setText(dataPass.getType());
        appointmentDescriptionField.setText(dataPass.getDescription());
        appointmentLocationField.setText(dataPass.getLocation());
        appointmentStartDateField.setValue(dataPass.getStart().toLocalDate());
        appointmentStartTimeField.setValue(dataPass.getStart().toLocalTime());
        appointmentEndDateField.setValue(dataPass.getEnd().toLocalDate());
        appointmentEndTimeField.setValue(dataPass.getEnd().toLocalTime());
        customerIDField.setValue(dataPass.getCustomerID());
        userIDField.setValue(dataPass.getUserID());
        contactIDField.setValue(DBContacts.getContactName(dataPass.getContactID()));

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

        //populate time list
        ObservableList<LocalTime> timesList = FXCollections.observableArrayList();
        LocalTime time = LocalTime.of(0,0);
        do {
            timesList.add(time);
            time = time.plusMinutes(15);
        } while (!time.equals(LocalTime.of(23,45)));

        appointmentStartTimeField.setItems(timesList);
        appointmentEndTimeField.setItems(timesList);
    }
}
