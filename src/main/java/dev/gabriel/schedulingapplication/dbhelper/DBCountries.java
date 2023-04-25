package dev.gabriel.schedulingapplication.dbhelper;

import dev.gabriel.schedulingapplication.Models.Countries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBCountries {

    /**
     * This method returns a list of all the countries that are in the database
     * @return
     */
    public static ObservableList<Countries> getAllCountries() {
        //this is our country list
        ObservableList<Countries> countryList = FXCollections.observableArrayList();

        try {
            //sql query
            String sql = "SELECT * FROM countries";
            //prepared statement gets the connection and uses the prepared statement as an argument
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            //gets the result set after creating the prepared statement
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop through the result set
            while(resultSet.next()) {
                //grab your fields from the result set
                int countryId = resultSet.getInt("Country_ID");
                String countryName = resultSet.getString("Country");
                //create a new country object
                Countries country = new Countries(countryId, countryName);
                //add it to your list of countries
                countryList.add(country);
            }
        } catch (SQLException e) {
            //print any problems
            throw new RuntimeException(e);
        }

        return countryList;
    }

    /**
     * This method takes a country ID as an argument and will return the corresponding country name
     * @param countryID
     * @return
     */
    public static String getCountryName(int countryID) {
        try {
            //query
            String sql = "SELECT Country FROM countries WHERE Country_ID = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, countryID);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String countryName = resultSet.getString("Country");
                return countryName;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
