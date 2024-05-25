package db_objects;

import java.math.BigDecimal;
import java.nio.channels.UnsupportedAddressTypeException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
	
	//register new user to the database
	//true = success; false = fail
	public static boolean register(String username, String password) {
		try {
			if(!checkUser(username)) {
				Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
				
				PreparedStatement preparedStatement = connection.prepareStatement(
						"INSERT INTO users(username, password, current_balance) " +
								"VALUES(?, ?, ?)"
						);
				
				preparedStatement.setString(1, username);
				preparedStatement.setString(2, password);
				preparedStatement.setBigDecimal(3, new BigDecimal(0));
				
				preparedStatement.executeUpdate();
				return true;						
			}


		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	//check if user to be registered already exists
	private static boolean checkUser(String username) {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			
			PreparedStatement preparedStatement = connection.prepareStatement(
					"SELECT * FROM users WHERE username = ?"
					);
			
			preparedStatement.setString(1, username);
			ResultSet resultSet = preparedStatement.executeQuery();
			
			if(!resultSet.next()) {
				return false;
			}

		}catch(SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public static boolean addTransactionToDatabase(Transaction transaction) {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			//value insertion statement		
			PreparedStatement insertTransaction = connection.prepareStatement(
					"INSERT transactions(user_id, transaction_type, transaction_amount, transaction_date) " +
					"VALUES(?, ?, ?, NOW())"
					);
			//switching '?' for actual values
				insertTransaction.setInt(1, transaction.getUserId());
				insertTransaction.setString(2, transaction.getTransactionType());
				insertTransaction.setBigDecimal(3, transaction.getTransactionAmount());
				
			//updating database
				insertTransaction.executeUpdate();
				
				return true;
				
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static boolean updateCurrentBalance(User user) {
		try {
			
			Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			
			PreparedStatement updateBalance = connection.prepareStatement(
					"UPDATE users SET current_balance = ? WHERE idusers = ?"
					);
			updateBalance.setBigDecimal(1, user.getCurrentBalance());
			updateBalance.setInt(2, user.getId());
			
			updateBalance.executeUpdate();
			return true;
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static boolean transfer(User user, String transferredUsername, float transferAmount) {
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			PreparedStatement queryUser = connection.prepareStatement(
					"SELECT * FROM users WHERE username = ?"		
					);
					
					queryUser.setString(1	, transferredUsername);
					ResultSet resultSet = queryUser.executeQuery();
					
					while(resultSet.next()) {
						User transferredUser = new User(
								resultSet.getInt("idusers"),
								transferredUsername,
								resultSet.getString("password"),
								resultSet.getBigDecimal("current_balance")			
								);
						
						Transaction transferTransaction = new Transaction(
								user.getId(),
								"Transfer",
								new BigDecimal(-transferAmount),
								null						
								);
						
						//this action will belong to the transferred user
						Transaction receivedTransaction = new Transaction(
								transferredUser.getId(),
								"Transfer",
								new BigDecimal(transferAmount),
								null
								);
						//update user who received the transfer
						transferredUser.setCurrentBalance(transferredUser.getCurrentBalance().add(BigDecimal.valueOf(transferAmount)));
						updateCurrentBalance(transferredUser);
						
						//update user who did the transfer
						user.setCurrentBalance(user.getCurrentBalance().subtract(BigDecimal.valueOf(transferAmount)));
						updateCurrentBalance(user);
					
						//add transactions to the data base
						addTransactionToDatabase(transferTransaction);
						addTransactionToDatabase(receivedTransaction);
						
						return true;
					
					}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static ArrayList<Transaction> getPastTransaction(User user){
		ArrayList<Transaction> pastTransactions = new ArrayList();
		
		try {
			Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			PreparedStatement grabTransactions = connection.prepareStatement(
					"SELECT * FROM transactions WHERE user_id = ?"		
					);
			grabTransactions.setInt(1	, user.getId());
			ResultSet resultSet = grabTransactions.executeQuery();

			while(resultSet.next()) {
				Transaction transaction = new Transaction(
						user.getId(),
						resultSet.getString("transaction_type"),
						resultSet.getBigDecimal("transaction_amount"),
						resultSet.getDate("transaction_date")					
				);
				pastTransactions.add(transaction);
			}
			
			return pastTransactions;
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return null;
	}
	
}
