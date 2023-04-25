package dev.gabriel.schedulingapplication.Controllers;

import dev.gabriel.schedulingapplication.Models.Appointments;
import dev.gabriel.schedulingapplication.dbhelper.DBAppointments;
import dev.gabriel.schedulingapplication.dbhelper.DBContacts;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import static dev.gabriel.schedulingapplication.Main.switchScreen;


public class AppointmentController implements Initializable {
    @FXML
    private TableView<Appointments> appointmentTable;
    @FXML
    private TableColumn<Appointments, Integer> appointmentIDColumn;
    @FXML
    private TableColumn<Appointments, String> titleColumn;
    @FXML
    private TableColumn<Appointments, String> descriptionColumn;
    @FXML
    private TableColumn<Appointments, String> locationColumn;
    @FXML
    private TableColumn<Appointments, String> contactColumn;
    @FXML
    private TableColumn<Appointments, String> typeColumn;
    @FXML
    private TableColumn<Appointments, LocalDateTime> startDateTimeColumn;
    @FXML
    private TableColumn<Appointments, LocalDateTime> endDateTimeColumn;
    @FXML
    private TableColumn<Appointments, Integer> customerIDColumn;
    @FXML
    private TableColumn<Appointments, Integer> userIDColumn;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button modifyAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button customersButton;
    @FXML
    private Button logoutButton;
    @FXML
    private RadioButton appointmentsByWeekRadioButton;
    @FXML
    private RadioButton appointmentsByMonthRadioButton;
    @FXML
    private RadioButton allAppointmentsRadioButton;

    /**
     * Handles switching to our customers screen
     * @throws IOException
     */
    public void handleCustomersButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/customer-view.fxml", customersButton);
    }

    /**
     * Handles switching to the reports screen
     * @throws IOException
     */
    public void handleReportsButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/reports-view.fxml", reportsButton);
    }

    /**
     * Handles logging the user out of the application, with an alert for confirmation
     * @throws IOException
     */
    public void handleLogoutButton() throws IOException {
        //confirm user wants to log out
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm log out");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Click OK to log out, or Cancel to stay");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            switchScreen("/dev/gabriel/schedulingapplication/Views/log-in-view.fxml", logoutButton);
        }
    }

    /**
     * Handles switching to the add appointments screen
     * @throws IOException
     */
    public void handleAddAppointmentButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/add-appointment-view.fxml", addAppointmentButton);
    }

    /**
     * Handles the delete appointments button, first shows confirmation message to make sure a user wants to delete an appointment
     * Then shows custom alert that the specified appointment has been deleted
     */
    public void handleDeleteAppointmentButton() {
        //get the selected item from the table if it's null we do nothing
        Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            return;
        }

        //add confirmation message that they want to delete the customer
        Alert alertOne = new Alert(Alert.AlertType.CONFIRMATION);
        alertOne.setTitle("Confirm");
        alertOne.setHeaderText("Are you sure you want to cancel this appointment");
        Optional<ButtonType> result = alertOne.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            //grab the appointment ID of the selected item and delete it from our table
            int appointmentID = selectedAppointment.getAppointmentID();
            DBAppointments.deleteAppointment(appointmentID);
            //reset the items by grabbing from the DB and refreshing
            appointmentTable.setItems(DBAppointments.getAllAppointments());
            appointmentTable.refresh();
        }

        //show an alert that they deleted the appointment successfully
        Alert alertTwo = new Alert(Alert.AlertType.INFORMATION);
        alertTwo.setTitle("Success!");
        alertTwo.setHeaderText("Appointment canceled");
        alertTwo.setContentText("Appointment with ID: " + selectedAppointment.getAppointmentID() + " and type: " + selectedAppointment.getType() + " was canceled");
        alertTwo.showAndWait();
    }

    /**
     * Handles switching to the modify appointment screen when modify button is pressed
     * Uses the same data pass method used in software I
     */
    public void handleModifyAppointmentButton() {
        //this is where we do our data pass, so we can fill in the fields with the values associated with this object
        Appointments selectedAppointment = appointmentTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            return;
        }
        ModifyAppointmentController.handleDataPass(selectedAppointment);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/gabriel/schedulingapplication/Views/modify-appointment-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) modifyAppointmentButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the appointments by month radio button, filters the table to only include appointments that are in the current month
     */
    public void handleByMonthRadioButton() {
        appointmentTable.refresh();
        appointmentTable.setItems(DBAppointments.appointmentsListByMonth());
    }

    /**
     * Handles the appointments by week radio button, filters the table to only include appointments that are in the current week
     */
    public void handleByWeekRadioButton() {
        appointmentTable.refresh();
        appointmentTable.setItems(DBAppointments.appointmentsListByWeek());
    }

    /**
     * Handles the all appointments radio button, filters the table to include all of the appointments within the database
     */
    public void handleAllAppointmentsRadioButton() {
        appointmentTable.refresh();
        appointmentTable.setItems(DBAppointments.getAllAppointments());
    }

    /**
     * Initialization of our screen, loads our table view items, and uses a lambda expression to set the contact column to include
     * the contact name instead of the numerical representation.
     *
     * Lambda Description: This lambda expression works by grabbing our data model labeled as data, I then use the data model to store the contactID
     * with a getter from the appointments class. After this I use a function that I made in my DBContacts class called DBContacts.getContactName which takes
     * a contact ID as a parameter and returns the corresponding contact name. I store this in a string called contactName and set my cells to hold this value.
     * The justification for this lambda expression is that our appointment object does not have a contact name value, so the only way to set our table to
     * hold this contact name value is to convert it using a function, however our table still has to display an object of type Appointment so the only reasonable
     * way to achieve this result is with the use of a lambda expression
     *
     * Lambda Location: Lines 219-226
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initialize our table on load
        appointmentTable.refresh();
        appointmentTable.setItems(DBAppointments.getAllAppointments());

        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        userIDColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));
        //use a lambda here to grab the contact name from the db instead of displaying the numerical representation
        contactColumn.setCellValueFactory(data -> {
            //get the contact id
            int contactID = data.getValue().getContactID();
            //get the contact associated with the contact id
            String contactName = DBContacts.getContactName(contactID);
            //set the cell to the contact name
            return new SimpleStringProperty(contactName);
        });
    }
}
