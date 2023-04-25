package dev.gabriel.schedulingapplication.Controllers;

import dev.gabriel.schedulingapplication.Models.Appointments;
import dev.gabriel.schedulingapplication.Models.Contacts;
import dev.gabriel.schedulingapplication.ReportModels.AppointmentsByTypeMonth;
import dev.gabriel.schedulingapplication.ReportModels.CustomerCountry;
import dev.gabriel.schedulingapplication.dbhelper.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

import static dev.gabriel.schedulingapplication.Main.switchScreen;

public class ReportsController implements Initializable {
    @FXML
    private TableView<AppointmentsByTypeMonth> appointmentsByMonthTypeTable;
    @FXML
    private TableColumn<AppointmentsByTypeMonth, String> monthColumn;
    @FXML
    private TableColumn<AppointmentsByTypeMonth, String> typeColumn;
    @FXML
    private TableColumn<AppointmentsByTypeMonth, Integer> totalColumn;
    @FXML
    private TableView<CustomerCountry> customerCountriesTable;
    @FXML
    private TableColumn<CustomerCountry, String> countryColumn;
    @FXML
    private TableColumn<CustomerCountry, Integer> totalCustomersColumn;
    @FXML
    private Button logoutButton;
    @FXML
    private ComboBox contactComboBox;
    @FXML
    private TableView<Appointments> appointmentsByContactTable;
    @FXML
    private TableColumn<Appointments, Integer> appointmentIDColumn;
    @FXML
    private TableColumn<Appointments, String> titleColumn;
    @FXML
    private TableColumn<Appointments, String> byContactTypeColumn;
    @FXML
    private TableColumn<Appointments, String> descriptionColumn;
    @FXML
    private TableColumn<Appointments, LocalDateTime> startDateTimeColumn;
    @FXML
    private TableColumn<Appointments, LocalDateTime> endDateTimeColumn;
    @FXML
    private TableColumn<Appointments, Integer> customerIDColumn;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button customersButton;

    /**
     * Handles the customers button, switches the user back to the customers screen
     * @throws IOException
     */
    public void handleCustomersButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/customer-view.fxml", customersButton);
    }

    /**
     * Handles the appointments button, switches the user back to the appointments screen
     * @throws IOException
     */
    public void handleAppointmentsButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/appointment-view.fxml", appointmentsButton);
    }

    /**
     * Handles the contact choice box, fills the data in the table with the appropriate appointments based on the contact that was selected
     * @throws SQLException
     */
    public void handleContactChoiceBox() throws SQLException {
        //first we want to grab the contact ID, so we can grab the appointments associated with this contact
        int contactID = DBContacts.getContactID((String) contactComboBox.getValue());
        //grab all the appointments associated with the contact ID and set the table
        appointmentsByContactTable.refresh();
        appointmentsByContactTable.setItems(DBAppointments.getAllAppointmentsByContactID(contactID));
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
     * Initialization of our screen, sets our table items and sets the items for our choice box
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //appointments by contact table
        appointmentIDColumn.setCellValueFactory(new PropertyValueFactory<>("appointmentID"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        byContactTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        startDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("start"));
        endDateTimeColumn.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));

        //appointments by month and type table
        appointmentsByMonthTypeTable.refresh();
        appointmentsByMonthTypeTable.setItems(DBAppointments.getAppointmentsByTypeMonth());
        monthColumn.setCellValueFactory(new PropertyValueFactory<>("month"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        //customers by country table
        customerCountriesTable.refresh();
        customerCountriesTable.setItems(DBCustomers.getCountriesByCustomer());
        countryColumn.setCellValueFactory(new PropertyValueFactory<>("country"));
        totalCustomersColumn.setCellValueFactory(new PropertyValueFactory<>("customerCount"));

        //populate contact choice box
        ObservableList<String> contactList = FXCollections.observableArrayList();
        for (Contacts contact : DBContacts.getAllContacts()) {
            contactList.add(contact.getContactName());
        }
        contactComboBox.setItems(contactList);
    }
}
