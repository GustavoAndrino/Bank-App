package bank_app;

import java.math.BigDecimal;

import javax.swing.SwingUtilities;

import db_objects.User;
import guis.BankingAppGui;
import guis.LoginGui;

public class App {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				new LoginGui().setVisible(true);		
				//new RegisterGui().setVisible(true);
				
			}
			
		});

	}

}
