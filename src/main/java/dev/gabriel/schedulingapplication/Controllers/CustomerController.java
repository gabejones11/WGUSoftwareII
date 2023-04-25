package dev.gabriel.schedulingapplication.Controllers;

import dev.gabriel.schedulingapplication.Models.Customers;
import dev.gabriel.schedulingapplication.dbhelper.DBAppointments;
import dev.gabriel.schedulingapplication.dbhelper.DBCustomers;
import dev.gabriel.schedulingapplication.dbhelper.DBDivisions;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static dev.gabriel.schedulingapplication.Main.switchScreen;

public class CustomerController implements Initializable {
    @FXML
    private TableView<Customers> customerTable;
    @FXML
    private TableColumn<Customers, Integer> customerIDColumn;
    @FXML
    private TableColumn<Customers, String> customerNameColumn;
    @FXML
    private TableColumn<Customers, String> customerAddressColumn;
    @FXML
    private TableColumn<Customers, String> customerPhoneColumn;
    @FXML
    private TableColumn<Customers, String> customerStateProvidenceColumn;
    @FXML
    private TableColumn<Customers, String> customerPostalCodeColumn;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button modifyCustomerButton;
    @FXML
    private Button deleteCustomerButton;
    @FXML
    private Button reportsButton;
    @FXML
    private Button appointmentsButton;
    @FXML
    private Button logoutButton;

    /**
     * Handles switching to the appointments screen when appointments button is pressed
     * @throws IOException
     */
    public void handleAppointmentsButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/appointment-view.fxml", appointmentsButton);
    }

    /**
     * Handles switching to the reports screen when the reports button is pressed
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
        alert.setTitle("Confirm Log out");
        alert.setHeaderText("Are you sure you want to log out?");
        alert.setContentText("Click OK to log out, or Cancel to stay");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            switchScreen("/dev/gabriel/schedulingapplication/Views/log-in-view.fxml", logoutButton);
        }
    }

    /**
     * Handles switching the user the add customer screen when the add button is pressed
     * @throws IOException
     */
    public void handleAddCustomerButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/add-customer-view.fxml", addCustomerButton);
    }

    /**
     * Handles switching the user to the modify customers screen when the modify button is pressed
     * Uses the same data pass method that we used in Software I
     */
    public void handleModifyCustomerButton() {
        //this is where we do the data pass so that we can fill in the fields with the values associated with this object
        Customers selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            return;
        }
        ModifyCustomerController.handleDataPass(selectedCustomer);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/dev/gabriel/schedulingapplication/Views/modify-customer-view.fxml"));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            Stage stage = (Stage) modifyCustomerButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles deleting a customer when the delete button is pressed. Validation is performed (validation function in utility class)
     * to make sure that user cannot delete a customer that has appointments scheduled. If no appointments are found for the customer
     * we add a confirmation alert that makes sure that the user wants to delete the customer
     */
    public void handleDeleteCustomerButton() {
        //get selected customer from the table if it's null we do nothing
        Customers selectedCustomer = customerTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            return;
        }

        if (DBAppointments.checkForCustomerAppointment(selectedCustomer.getCustomerID())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Customer has scheduled appointment");
            alert.setContentText("You must delete all appointments associated with this customer before deleting the customer");
            alert.showAndWait();
        } else {
            //add confirmation message that they want to delete the customer
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText("Are you sure you want to delete this customer from the application");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                //grab customerID from selected customer
                int customerID = selectedCustomer.getCustomerID();
                DBCustomers.deleteCustomer(customerID);
                //reset items by grabbing from the db and refreshing
                customerTable.setItems(DBCustomers.getAllCustomers());
                customerTable.refresh();
            }
        }
    }

    /**
     * Initialization of our screen, loads our table items, and uses a lambda expression to set the state/providence choice box to hold the
     * state/providence String representation instead of the numerical representation
     *
     * Lambda Description: This lambda expression works by grabbing our data model labeled as data, I then use the data model to store the divisionID
     * with a getter from the customer class. After this I use a function that I made in my DBDivisions class called DBDivisions.getDivisionName which takes
     * a division ID as a parameter and returns the corresponding division name. I store this in a string called divisionName and set my cells to hold this value.
     * The justification for this lambda expression is that our contact object does not have a division name value, so the only way to set our table to
     * hold this division name value is to convert it using a function, however our table still has to display an object of type Contact so the only reasonable
     * way to achieve this result is with the use of a lambda expression
     *
     * Lambda Location: Lines 176-183
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initialize our table on load
        customerTable.refresh();
        customerTable.setItems(DBCustomers.getAllCustomers());
        customerIDColumn.setCellValueFactory(new PropertyValueFactory<>("customerID"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        //lambda so that we can set our cell value with a function
        customerStateProvidenceColumn.setCellValueFactory(data -> {
            //get the division id
            int divisionID = data.getValue().getDivisionID();
            //get the division name associated with the id
            String divisionName = DBDivisions.getDivisionName(divisionID);
            //set the cell to the division name
            return new SimpleStringProperty(divisionName);
        });
        customerPostalCodeColumn.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
    }
}
