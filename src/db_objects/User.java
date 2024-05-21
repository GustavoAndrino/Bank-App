package db_objects;

import java.math.BigDecimal;
import java.math.RoundingMode;

//user entity to store user data

public class User {
	private final int id;
	private final String username, password;
	private BigDecimal currentBalance;
	
	public User(int id, String username, String password, BigDecimal currentBalance) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.currentBalance = currentBalance;
	}

	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public BigDecimal getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(BigDecimal newBalance) {
		currentBalance = newBalance.setScale(2, RoundingMode.FLOOR);
	}
	
	
	
	
	

}
