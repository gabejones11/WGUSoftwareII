package dev.gabriel.schedulingapplication.dbhelper;

import dev.gabriel.schedulingapplication.Models.Customers;
import dev.gabriel.schedulingapplication.ReportModels.CustomerCountry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class DBCustomers {

    /**
     * This method returns a list of all the customers that are in the database
     * @return
     */
    public static ObservableList<Customers> getAllCustomers() {
        //customer list
        ObservableList<Customers> customerList = FXCollections.observableArrayList();

        try {
            //sql query
            String sql = "SELECT * FROM customers";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop
            while (resultSet.next()) {
                int customerID = resultSet.getInt("Customer_ID");
                String customerName = resultSet.getString("Customer_Name");
                String address = resultSet.getString("Address");
                String postalCode = resultSet.getString("Postal_Code");
                String phoneNumber = resultSet.getString("Phone");
                int divisionID = resultSet.getInt("Division_ID");
                Customers customer = new Customers(customerID, customerName, address, postalCode, phoneNumber, divisionID);
                customerList.add(customer);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customerList;
    }

    /**
     * This method takes a state/providence name as an argument and returns the corresponding division ID
     * @param stateProvName
     * @return
     */
    public static int getDivisionID(String stateProvName) {

        try {
            //query
            String sql = "SELECT Division_ID FROM first_level_divisions WHERE Division = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, stateProvName);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int divisionID = resultSet.getInt("Division_ID");
                return divisionID;
            }
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This method adds a Customer to the database and returns an int (generated key) that we can use to make a Customer object for the unique ID
     * I don't know if it is needed or not to make a Customer object, but I did it anyway
     * @param customerName
     * @param address
     * @param postalCode
     * @param phone
     * @param divisionID
     * @return
     */
    public static int addCustomer(String customerName, String address, String postalCode, String phone, int divisionID) {
        try {
            //query
            String sql = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                    "VALUES (?, ?, ?, ?, ?)";

            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, customerName);
            preparedStatement.setString(2, address);
            preparedStatement.setString(3, postalCode);
            preparedStatement.setString(4, phone);
            preparedStatement.setInt(5, divisionID);

            //execute query
            preparedStatement.executeUpdate();

            //return the generated id value for the customer
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();

            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve generated keys");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method takes a customer ID as an argument and removes a customer from the database
     * @param customerID
     */
    public static void deleteCustomer(int customerID) {
        try{
            //query
            String sql = "DELETE FROM customers WHERE Customer_ID = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, customerID);
            //execute query
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method takes a Customer object as an argument and updates the customer in the database
     * @param customer
     */
    public static void updateCustomer(Customers customer) {
        try {
            //query
            String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? "
                    + "WHERE Customer_ID = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.getCustomerName());
            preparedStatement.setString(2, customer.getAddress());
            preparedStatement.setString(3, customer.getPostalCode());
            preparedStatement.setString(4, customer.getPhoneNumber());
            preparedStatement.setInt(5, customer.getDivisionID());
            preparedStatement.setInt(6, customer.getCustomerID());
            //execute query
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method returns a list of customers that are grouped together by their corresponding country.
     * The query selects the country from the countries table and a count of the customer ID's from the customers table
     * it then joins the customers table and first_level_divisions table on the division ID
     * it then joins the countries table and first_level_divisions table on the country ID
     * this provides us with a table that has all the necessary information that we need. The last thing we do
     * is group the results by the country
     * @return
     */
    public static ObservableList<CustomerCountry> getCountriesByCustomer() {
        //customers by country List
        ObservableList<CustomerCountry> customerCountryList = FXCollections.observableArrayList();

        try {
            //query
            String sql = "SELECT countries.country, COUNT(customers.customer_ID) as customer_count " +
                    "FROM customers " +
                    "JOIN first_level_divisions " +
                    "ON customers.division_ID = first_level_divisions.division_ID " +
                    "JOIN countries " +
                    "ON first_level_divisions.country_ID = countries.country_ID " +
                    "GROUP BY countries.country";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop
            while (resultSet.next()) {
                String country = resultSet.getString("country");
                int customerCount = resultSet.getInt("customer_count");
                CustomerCountry customerCountry = new CustomerCountry(country, customerCount);
                customerCountryList.add(customerCountry);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return customerCountryList;
    }
}
