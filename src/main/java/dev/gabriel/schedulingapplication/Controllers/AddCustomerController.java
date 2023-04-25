package dev.gabriel.schedulingapplication.Controllers;

import dev.gabriel.schedulingapplication.Models.Countries;
import dev.gabriel.schedulingapplication.Models.Customers;
import dev.gabriel.schedulingapplication.Models.Divisions;
import dev.gabriel.schedulingapplication.dbhelper.DBCountries;
import dev.gabriel.schedulingapplication.dbhelper.DBCustomers;
import dev.gabriel.schedulingapplication.dbhelper.DBDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static dev.gabriel.schedulingapplication.Main.switchScreen;
import static dev.gabriel.schedulingapplication.Utility.Utility.*;

public class AddCustomerController implements Initializable {
    @FXML
    private TextField customerIDField;
    @FXML
    private TextField customerNameField;
    @FXML
    private TextField customerAddressField;
    @FXML
    private TextField customerPhoneNumberField;
    @FXML
    private TextField customerPostalCodeField;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    @FXML
    private ComboBox customerCountryComboBox;
    @FXML
    private ComboBox customerStateProvidenceComboBox;

    /**
     * Handles the cancel button, sends the user back to the customers screen
     * @throws IOException
     */
    public void handleCancelButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/customer-view.fxml", cancelButton);
    }

    /**
     * Handles all the validation (validation functions in utility class) needed to add a customer to the database and create a customer object
     * @throws IOException
     */
    public void handleSaveButton() throws IOException {
        //grab all the input fields
        String customerName = customerNameField.getText();
        String customerAddress = customerAddressField.getText();
        String customerPhoneNumber = customerPhoneNumberField.getText();
        String customerPostalCode = customerPostalCodeField.getText();
        String customerCountry = (String) customerCountryComboBox.getValue();
        String customerStateProvidence = (String) customerStateProvidenceComboBox.getValue();
        //convert the stateprov string into a division id for saving purposes
        int customerDivisionID = DBCustomers.getDivisionID(customerStateProvidence);

        if (!validateCustomerName(customerName)) {
            return;
        }

        if (!validateCustomerAddress(customerAddress)) {
            return;
        }

        if (!validateCustomerPhoneNumber(customerPhoneNumber)) {
            return;
        }

        if (!validateCustomerCountry(customerCountry)) {
            return;
        }

        if (!validateCustomerStateProvidence(customerStateProvidence)) {
            return;
        }

        if (!validateCustomerPostalCode(customerPostalCode)) {
            return;
        }

        //if it passes validation we will do the same operation as the appointment we will get the generated id when we add the object to the
        //database and then create a new object with the generated id returned from our addCustomer method
        int generatedID = DBCustomers.addCustomer(customerName, customerAddress, customerPostalCode, customerPhoneNumber, customerDivisionID);
        Customers customer = new Customers(generatedID, customerName, customerAddress, customerPostalCode, customerPhoneNumber, customerDivisionID);
        switchScreen("/dev/gabriel/schedulingapplication/Views/customer-view.fxml", saveButton);
    }

    /**
     * This function handles filtering of the state/providence combo box based on what country the user has selected
     */
    public void handleCountrySelected() {
        //this is maybe not the most efficient way to handle this, but I am assuming that in the future this code could be updated
        //to reflect if another country is added to the database
        if ("U.S".equals(customerCountryComboBox.getValue())) {
            ObservableList<String> divisionListUS = FXCollections.observableArrayList();

            for (Divisions division : DBDivisions.getDivisionsByCountry(1)) {
                divisionListUS.add(division.getDivisionName());
            }
            customerStateProvidenceComboBox.setItems(divisionListUS);

        } else if ("UK".equals(customerCountryComboBox.getValue())) {
            ObservableList<String> divisionListUK = FXCollections.observableArrayList();

            for (Divisions division : DBDivisions.getDivisionsByCountry(2)) {
                divisionListUK.add(division.getDivisionName());
            }
            customerStateProvidenceComboBox.setItems(divisionListUK);

        } else if ("Canada".equals(customerCountryComboBox.getValue())) {
            ObservableList<String> divisionListCanada = FXCollections.observableArrayList();

            for (Divisions division : DBDivisions.getDivisionsByCountry(3)) {
                divisionListCanada.add(division.getDivisionName());
            }
            customerStateProvidenceComboBox.setItems(divisionListCanada);
        }
    }

    /**
     * Initialization of this screen, pre-populates our country and state providence choice boxes
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //populate the country choice box
        ObservableList<String> countryList = FXCollections.observableArrayList();
        for (Countries country : DBCountries.getAllCountries()) {
            countryList.add(country.getName());
        }
        customerCountryComboBox.setItems(countryList);

        //populate the state/providence choice box
        ObservableList<String> stateProvList = FXCollections.observableArrayList();
        for (Divisions division : DBDivisions.getAllDivisions()) {
            stateProvList.add(division.getDivisionName());
        }
        customerStateProvidenceComboBox.setItems(stateProvList);
    }
}
