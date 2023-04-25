package dev.gabriel.schedulingapplication.dbhelper;

import dev.gabriel.schedulingapplication.Models.Users;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUsers {

    /**
     * This method returns a list of all the users that are in the database
     * @return
     */
    public static ObservableList<Users> getAllUsers() {
        //users list
        ObservableList<Users> userList = FXCollections.observableArrayList();

        try {
            //query
            String sql = "SELECT * FROM users";
            //prep statement
            PreparedStatement preparedStatement = JDBC.getConnection().prepareStatement(sql);
            //result set
            ResultSet resultSet = preparedStatement.executeQuery();

            //loop through the list of users
            while(resultSet.next()) {
                int userID = resultSet.getInt("User_ID");
                String username = resultSet.getString("User_Name");
                String password = resultSet.getString("Password");

                Users user = new Users(userID, username, password);
                userList.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return userList;
    }
}
