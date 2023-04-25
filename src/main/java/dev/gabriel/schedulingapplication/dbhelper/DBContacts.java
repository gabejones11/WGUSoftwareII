package dev.gabriel.schedulingapplication.dbhelper;

import dev.gabriel.schedulingapplication.Models.Contacts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBContacts {

    /**
     * This method returns a list of all the contacts that are in the database
     * @return
     */
    public static ObservableList<Contacts> getAllContacts() {
        //this is our contact list
        ObservableList<Contacts> contactList = FXCollections.observableArrayList();

        try {
            //sql query
            String sql = "SELECT * FROM contacts";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop
            while (resultSet.next()) {
                int contactID = resultSet.getInt("Contact_ID");
                String contactName = resultSet.getString("Contact_Name");
                String contactEmail = resultSet.getString("Email");
                Contacts contact = new Contacts(contactID, contactName, contactEmail);
                contactList.add(contact);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return contactList;
    }

    /**
     * This method takes a contact ID as an argument and will return the corresponding contact name
     * @param contactID
     * @return
     */
    public static String getContactName(int contactID) {
        try {
            //query
            String sql = "SELECT Contact_Name FROM contacts WHERE Contact_ID = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, contactID);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //check if there is a result first
            if (resultSet.next()) {
                String contactName = resultSet.getString("Contact_Name");
                return contactName;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method takes a contact name as and argument and will return the corresponding contact ID
     * @param contactName
     * @return
     * @throws SQLException
     */
    public static Integer getContactID(String contactName) throws SQLException {
        try {
            //query
            String sql = "SELECT Contact_ID FROM contacts WHERE Contact_Name = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, contactName);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //check if there is a result
            if (resultSet.next()) {
                int contactID = resultSet.getInt("Contact_ID");
                return contactID;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
