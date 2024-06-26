package guis;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import db_objects.MyJDBC;
import db_objects.User;

public class LoginGui extends BaseFrame{

	public LoginGui() {
		super("Banking App Login");
		
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
		passwordLabel.setBounds(20, 260, getWidth() - 50,  24);	
		passwordLabel.setFont(new Font("Dialog", Font.BOLD, 20));
				
		add(passwordLabel);
				
		//create password field
		JPasswordField passwordField = new JPasswordField();
		passwordField.setBounds(20, 320, getWidth() - 50, 40);
		passwordField.setFont(new Font("Dialog", Font.PLAIN, 28));
		add(passwordField);
		
		//create login button
		JButton loginButton = new JButton("Login");
		loginButton.setBounds(20, 460, getWidth() - 50, 40);
		loginButton.setFont(new Font("Dialog", Font.BOLD, 20));
		loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//get username
				String username = usernameField.getText();
				
				//get password
				String password = String.valueOf(passwordField.getPassword()) ;
				
				//validate login
				User user = MyJDBC.validateLogin(username, password);
				
				if(user != null) {
				//valid login, dispose this gui and open bank app gui
					LoginGui.this.dispose();
					
				//launch bank app gui
					BankingAppGui bankingAppGui = new BankingAppGui(user);
					bankingAppGui.setVisible(true);
					
				//show success dialog
					JOptionPane.showMessageDialog(bankingAppGui, "Login Successfully!");
				}else {
					JOptionPane.showMessageDialog(LoginGui.this, "Login Failed...");
				}				
			}
		});
		add(loginButton);
		
		//create a register label
		JLabel registerLabel = new JLabel("<html><a href = \"#\">Don't have an account ? Register Here</a></html> ");
		registerLabel.setBounds(0, 510, getWidth() - 10, 30);
		registerLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		registerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		//event listener to the mouse click = mouse click to do something
		registerLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//close current window
				LoginGui.this.dispose();
				//open register window
				new RegisterGui().setVisible(true);
			}
		});
		
		add(registerLabel);
		
	}


}
