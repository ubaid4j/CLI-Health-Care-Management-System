package server.impl;
import server.context.ServiceContext;
import server.domain.User;
import server.service.UserService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceDBImpl implements UserService {
    Connection connection;
    public UserServiceDBImpl(){
        connection = ServiceContext.getDatabaseConnection().getConnection();
    }

    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from user_table;");

            // Iterate through the result set and retrieve data
            while (resultSet.next()){
                users.add(fetchUserDataFromQueryResult(resultSet));
            }


        } catch (SQLException e) {
            System.out.println("Error reading users from the Database: " + e.getMessage());
        }
        return users;
    }
    public void displayUsers(List<User> usersList){
        // Print table header
        System.out.format("%-5s %-15s %-10s %-15s %-15s %-10s%n", "ID", "Name", "Role", "Username", "Password", "Account Locked");

        // Print table rows
        for (User user : usersList) {
            System.out.format("%-5d %-15s %-10s %-15s %-15s %-10s%n",
                    user.getId(), user.getName(), user.getRoll(), user.getUsername(), user.getPassword(), user.getAccountStatus());
        }
    }

    //save a list of users To users Table in database
    public void saveUsersToDb(List<User> userList){
    }

    public User validateUserLogin(String username, String password, String userRole) {
        User user = null;
        try {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM user_table WHERE username = '" + username + "' AND password = '" + password + "' AND role = '" + userRole + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                user = fetchUserDataFromQueryResult(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error reading users from the Database: " + e.getMessage());
        }
        return user;
    }


    @Override
    public void viewUsers() {
        List<User> users = getUsers();
        displayUsers(users);
    }

    @Override
    public void viewPatients() {
        List<User> patients = getPatients();
        displayUsers(patients);
    }

    @Override
    public void viewDoctors() {
        List<User> doctors = getDoctors();
        displayUsers(doctors);
    }

    public List<User> getPatients() {
        List<User> patientsList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from user_table where role = \"patient\";");

            // Iterate through the result set and retrieve data
            while (resultSet.next()){
                patientsList.add(fetchUserDataFromQueryResult(resultSet));
            }


        } catch (SQLException e) {
            System.out.println("Error reading users from the Database: " + e.getMessage());
        }
        return patientsList;
    }

    private User fetchUserDataFromQueryResult(ResultSet resultSet) throws SQLException {
            int userId = resultSet.getInt("userid");
            String name = resultSet.getString("name");
            String username = resultSet.getString("username");
            String password = resultSet.getString("password");
            String role = resultSet.getString("role");
            boolean accountLocked = resultSet.getBoolean("accountLocked");
        return new User(userId,name, role, username, password,accountLocked);
    }

    public List<User> getDoctors() {
        List<User> doctorsList = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("Select * from user_table where role = \"doctor\";");

            // Iterate through the result set and retrieve data
            while (resultSet.next()){
                doctorsList.add(fetchUserDataFromQueryResult(resultSet));
            }

        } catch (SQLException e) {
            System.out.println("Error reading users from the Database: " + e.getMessage());
        }
        return doctorsList;
    }

    public static void main(String[] args){
        UserService userService = ServiceContext.getUserServiceDB();
        userService.viewUsers();

        userService.viewPatients();
        userService.viewDoctors();

        User user = userService.validateUserLogin("admin12","password123","admin");
        System.out.println(user);

    }

}
