package guis;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import db_objects.User;

//perform basic bank functions (deposit, withdrawl, etc..)
public class BankingAppGui extends BaseFrame implements ActionListener {
	private JTextField currentBalanceField;
	public JTextField getCurrentBalanceField() {return currentBalanceField;}

	public BankingAppGui(User user){
		super("Banking app", user);

	}

	@Override
	protected void addGuiComponents() {
		//create welcome message
		String welcomeMessage = "<html>" + 
				"<body style='text-align:center'>" +
				"<b>Hello " + user.getUsername() + "</b><br>" +
				"What would you like to do today?</body></html>";
		JLabel welcomeMessageLabel = new JLabel(welcomeMessage);
		welcomeMessageLabel.setBounds(0, 20, getWidth() - 10, 40);
		welcomeMessageLabel.setFont(new Font("Dialog", Font.PLAIN, 16));
		welcomeMessageLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(welcomeMessageLabel);
		
		//create current balance label
		JLabel currentBalanceLabel = new JLabel("Current Balance");
		currentBalanceLabel.setBounds(0, 80, getWidth() - 10, 30);
		currentBalanceLabel.setFont(new Font("Dialog", Font.PLAIN, 22));
		currentBalanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(currentBalanceLabel);
		
		//create current balance field
		currentBalanceField = new JTextField("$" + user.getCurrentBalance());
		currentBalanceField.setBounds(15, 120, getWidth() - 50, 40);
		currentBalanceField.setFont(new Font("Dialog", Font.PLAIN, 28));
		currentBalanceField.setHorizontalAlignment(SwingConstants.RIGHT);
		currentBalanceField.setEditable(false);
		add(currentBalanceField);
		
		//deposit button
		JButton depositButton = new JButton ("Deposit");
		depositButton.setBounds(15, 180, getWidth() - 50, 50);
		depositButton.setFont(new Font("Dialog", Font.PLAIN, 22));
		depositButton.addActionListener(this);
		add(depositButton);
		
		//withdrawl button
		JButton withdrawlButton = new JButton ("Withdraw");
		withdrawlButton.setBounds(15, 250, getWidth() - 50, 50);
		withdrawlButton.setFont(new Font("Dialog", Font.PLAIN, 22));
		withdrawlButton.addActionListener(this);
		/*withdrawlButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed (ActionEvent e) {
				
			}
		});*/
		add(withdrawlButton);
		
		//past transaction button
		JButton pastButton = new JButton ("Past Transaction");
		pastButton.setBounds(15, 320, getWidth() - 50, 50);
		pastButton.setFont(new Font("Dialog", Font.PLAIN, 22));
		pastButton.addActionListener(this);
		add(pastButton);
		
		//transfer button
		JButton transferButton = new JButton ("Transfer");
		transferButton.setBounds(15, 390, getWidth() - 50, 50);
		transferButton.setFont(new Font("Dialog", Font.PLAIN, 22));
		transferButton.addActionListener(this);
		add(transferButton);
		
		//logout button
		JButton logoutButton = new JButton ("Logout");
		logoutButton.setBounds(15, 500, getWidth() - 50, 50);
		logoutButton.setFont(new Font("Dialog", Font.PLAIN, 22));
		logoutButton.addActionListener(this);
		add(logoutButton);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String buttonPressed = e.getActionCommand();
		
		if(buttonPressed.equalsIgnoreCase("Logout")) {
			//return user to login gui
			new LoginGui().setVisible(true);
			
			//dispose of gui
			this.dispose();
			
			//no need for extras 
			return;
		}
		
		//Now we introduce the banking
		BankingAppDialog bankingAppDialog = new BankingAppDialog(this, user);
		
		//set the title according to button pressed
		bankingAppDialog.setTitle(buttonPressed);
		
		if(buttonPressed.equalsIgnoreCase("Deposit") || buttonPressed.equalsIgnoreCase("Withdraw") || buttonPressed.equalsIgnoreCase("Transfer") ) {
			bankingAppDialog.addCurrentBalanceAmount();
			
			//add action button
			bankingAppDialog.addActionButton(buttonPressed);
				
			//transfer action will require more components
			if(buttonPressed.equalsIgnoreCase("Transfer")) {
				bankingAppDialog.addUserField();
			}
			
			bankingAppDialog.setVisible(true);
		}
		
	}

}
