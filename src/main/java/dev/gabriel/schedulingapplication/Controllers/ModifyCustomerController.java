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
import static dev.gabriel.schedulingapplication.Utility.Utility.validateCustomerStateProvidence;

public class ModifyCustomerController implements Initializable {
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
    private static Customers dataPass;

    /**
     * This method is used in our customers controller and just passes the customer model that is selected from the table
     * to this screen so that we can pre-fill the information in the form
     * @param customer
     */
    public static void handleDataPass(Customers customer) {
        dataPass = customer;
    }

    /**
     * This method handles the cancel button sends the user back to the customers screen
     * @throws IOException
     */
    public void handleCancelButton() throws IOException {
        switchScreen("/dev/gabriel/schedulingapplication/Views/customer-view.fxml", cancelButton);
    }

    /**
     * This method handles the save button and executes all the validation (validation functions in Utility Class) needed to modify
     * a customer
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

        if (!validateCustomerPostalCode(customerPostalCode)) {
            return;
        }

        if (!validateCustomerCountry(customerCountry)) {
            return;
        }

        if (!validateCustomerStateProvidence(customerStateProvidence)) {
            return;
        }

        //if it passes validation we will do the same operation as the modify appointment, create a new object with the same id as the data pass object and
        //perform the update action
        Customers customer = new Customers(dataPass.getCustomerID(), customerName, customerAddress, customerPostalCode, customerPhoneNumber, customerDivisionID);
        DBCustomers.updateCustomer(customer);
        switchScreen("/dev/gabriel/schedulingapplication/Views/customer-view.fxml", saveButton);
    }

    /**
     * This method handles the country selected choice box and filters the state/providence combo box based on which country the user has selected
     */
    public void handleCountrySelected() {
        //get the value from the combobox and use the get division by country id method to populate divisions with the same country code as the one selected
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
     * Initialization of our screen, to pre-fill the form with the country name we must first grab
     * the country ID, and then we use a function that I made that takes the country ID as a parameter and returns the country name.
     * We then will set this as the value that is being held in the customerCountryComboBox. We also populate the Country and state/providence
     * choice boxes with their respective items
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //some initialization to get the country name
        //get the country ID first
        int countryID = DBDivisions.getCountryID(DBDivisions.getDivisionName(dataPass.getDivisionID()));
        //convert the country ID into country name
        String countryName = DBCountries.getCountryName(countryID);

        customerIDField.setText(String.valueOf(dataPass.getCustomerID()));
        customerNameField.setText(dataPass.getCustomerName());
        customerAddressField.setText(dataPass.getAddress());
        customerPhoneNumberField.setText(dataPass.getPhoneNumber());
        customerPostalCodeField.setText(dataPass.getPostalCode());
        customerCountryComboBox.setValue(countryName);
        customerStateProvidenceComboBox.setValue(dataPass.getDivisionName());

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
