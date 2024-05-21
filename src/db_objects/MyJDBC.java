package db_objects;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//JDBC Class used to make program interact with MYSQL database
public class MyJDBC {
	private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/bankapp";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD= "Password1";
	
	//if valid return an object with the users info
	public static User validateLogin(String username, String password) {
		try {
			//establish connection to the database using configurations
			Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			
			//create sql query
			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT * FROM users WHERE username = ? AND password = ?"
					);	
			//Substitue ? with variables
			preparedStatement.setString(1, username);
			preparedStatement.setString(2, password);		
			
			//execute query and store into a result set
			ResultSet resultSet = preparedStatement.executeQuery();
			
			//next() returns true or false
			// true - query returned data
			//false - failed to retrieve data (wrong password and username)
			if(resultSet.next()) {
				//success --> get Id
				int userId = resultSet.getInt("idusers");
				
			   //get current balance
				BigDecimal currentBalance = resultSet.getBigDecimal("current_balance");
				
				//return user object
				return new User(userId, username, password, currentBalance);
				
			}

		}catch(SQLException e) {
			e.printStackTrace();
			
		}
		
		//no valid user with matching credentials
		return null;
	}
}
