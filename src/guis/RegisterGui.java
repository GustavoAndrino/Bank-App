package guis;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class RegisterGui extends BaseFrame {

	public RegisterGui() {
		super("Register Gui");

	}

	@Override
	protected void addGuiComponents() {
		//create banking app label
		JLabel bankingAppLabel = new JLabel("Banking Application");
		
		bankingAppLabel.setBounds(0, 20, super.getWidth(), 40);
		
		//change the font style
		bankingAppLabel.setFont(new Font("Dialog", Font.BOLD, 32));
		
		//center text
		bankingAppLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		//add to gui
		add(bankingAppLabel);
		
		//username label
		JLabel usernameLabel = new JLabel("Username: ");
		usernameLabel.setBounds(20, 120, getWidth() - 30,  24);	
		usernameLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		
		add(usernameLabel);
		
		//create username field
		JTextField usernameField = new JTextField();
		usernameField.setBounds(20, 160, getWidth() - 50, 40);
		usernameField.setFont(new Font("Dialog", Font.PLAIN, 28));
		add(usernameField);
		
		//password label
		JLabel passwordLabel = new JLabel("Password: ");
		passwordLabel.setBounds(20, 220, getWidth() - 50,  24);	
		passwordLabel.setFont(new Font("Dialog", Font.BOLD, 20));
				
		add(passwordLabel);
				
		//create password field
		JPasswordField passwordField = new JPasswordField();
		passwordField.setBounds(20, 260, getWidth() - 50, 40);
		passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
		add(passwordField);
		
		//retype rePassword
		JLabel repasswordLabel = new JLabel ("Re-type password:");
		repasswordLabel.setBounds(20, 320, getWidth() - 50, 40);
		repasswordLabel.setFont(new Font("Dialog", Font.BOLD, 20));
		add(repasswordLabel);
		
		//create repassword field
		JPasswordField repasswordField = new JPasswordField();
		repasswordField.setBounds(20, 360, getWidth() - 50, 40);
		repasswordField.setFont(new Font("Dialog", Font.PLAIN, 28));
		add(repasswordField);
		
		//create login button
		JButton registerButton = new JButton("Register");
		registerButton.setBounds(20, 460, getWidth() - 50, 40);
		registerButton.setFont(new Font("Dialog", Font.BOLD, 20));
		add(registerButton);
		
		//create a login label
		JLabel loginLabel = new JLabel("<html><a href = \"#\">Have an account ? Sign-in Here</a></html> ");
		loginLabel.setBounds(0, 510, getWidth() - 10, 30);
		loginLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		//event listener to the mouse click = mouse click to do something
			loginLabel.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						//close current window
						RegisterGui.this.dispose();
						//open register window
						new LoginGui().setVisible(true);
					}
				});
		
		
		add(loginLabel);
		
	}

}
