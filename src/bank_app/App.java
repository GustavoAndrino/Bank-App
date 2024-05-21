package bank_app;

import javax.swing.SwingUtilities;

import guis.LoginGui;
import guis.RegisterGui;

public class App {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				//new LoginGui().setVisible(true);		
				new RegisterGui().setVisible(true);
			}
			
		});

	}

}
