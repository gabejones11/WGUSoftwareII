package dev.gabriel.schedulingapplication.dbhelper;

import dev.gabriel.schedulingapplication.Models.Divisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBDivisions {

    /**
     * This method returns a list of all the divisions that are in the database
     * @return
     */
    public static ObservableList<Divisions> getAllDivisions() {
        //division list
        ObservableList<Divisions> divisionList = FXCollections.observableArrayList();

        try {
            //query
            String sql = "SELECT * FROM first_level_divisions";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop
            while (resultSet.next()) {
                int divisionID = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");
                int countryID = resultSet.getInt("Country_ID");
                Divisions division = new Divisions(divisionID, divisionName, countryID);
                divisionList.add(division);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return divisionList;
    }

    /**
     * This method takes a division ID as an argument and returns the corresponding division name
     * @param divisionID
     * @return
     */
    public static String getDivisionName(int divisionID) {
        try {
            //query
            String sql = "SELECT Division FROM first_level_divisions WHERE Division_ID = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, divisionID);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();
            //check if there is a result first
            if (resultSet.next()) {
                String divisionName = resultSet.getString("Division");
                return divisionName;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method takes a division name as an argument and returns the corresponding country ID
     * @param divisionName
     * @return
     */
    public static Integer getCountryID (String divisionName) {
        try{
            //query
            String sql = "SELECT Country_ID FROM first_level_divisions WHERE Division = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            preparedStatement.setString(1, divisionName);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();
            //check if there is a result first
            if (resultSet.next()) {
                Integer countryID = resultSet.getInt("Country_ID");
                return countryID;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This method takes a country ID as an argument and returns the list of Divisions that correspond with that country ID
     * @param countryID
     * @return
     */
    public static ObservableList<Divisions> getDivisionsByCountry(int countryID) {
        //division list
        ObservableList<Divisions> divisionList = FXCollections.observableArrayList();

        try {
            //query
            String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
            //prepared statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, countryID);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop
            while (resultSet.next()) {
                int divisionID = resultSet.getInt("Division_ID");
                String divisionName = resultSet.getString("Division");

                Divisions division = new Divisions(divisionID, divisionName, countryID);
                divisionList.add(division);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return divisionList;
    }

}
