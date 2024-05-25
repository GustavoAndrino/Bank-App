package guis;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import db_objects.MyJDBC;
import db_objects.Transaction;
import db_objects.User;

public class BankingAppDialog extends JDialog implements ActionListener{
	private User user;
	private BankingAppGui bankingAppGui;
	private JLabel balanceLabel, enterAmountLabel, enterUserLabel;
	private JTextField enterAmountField, enterUserField;
	private JButton actionButton;
	
	public BankingAppDialog(BankingAppGui bankingAppGui, User user) {
		//set the size
		setSize(400, 400);
		
		//add focus to the dialog (can't interact with anything else until dialog is closed)
		setModal(true);
		
		//loads in the center of our banking gui
		setLocationRelativeTo(bankingAppGui);
		
		//when user closes dialog, it releases it
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setResizable(false);
		
		setLayout(null);
		
		//Adding reference to the gui to show and update the current balance
		this.bankingAppGui = bankingAppGui;
		
		//add reference to this class user
		this.user = user;
		
	}
	
	public void addCurrentBalanceAmount() {
		//balance label
		balanceLabel = new JLabel("Balance: $" + user.getCurrentBalance());
		balanceLabel.setBounds(0, 10, getWidth() - 20, 20);
		balanceLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		balanceLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(balanceLabel);
		
		//enter amount label
				enterAmountLabel = new JLabel("Enter amount:");
				enterAmountLabel.setBounds(0, 50, getWidth() - 20, 20);
				enterAmountLabel.setFont(new Font("Dialog", Font.BOLD, 16));
				enterAmountLabel.setHorizontalAlignment(SwingConstants.CENTER);
				add(enterAmountLabel);
		
				//enter amount field
				enterAmountField = new JTextField();
				enterAmountField.setBounds(15, 80, getWidth() - 50, 40);
				enterAmountField.setFont(new Font("Dialog", Font.BOLD, 20));
				enterAmountField.setHorizontalAlignment(SwingConstants.CENTER);
				add(enterAmountField);
	}
	
	public void addActionButton(String actionButtonType) {
		actionButton = new JButton(actionButtonType);
		actionButton.setBounds(15, 300, getWidth() - 50, 40);
		actionButton.setFont(new Font("Dialog", Font.BOLD, 20));
		actionButton.addActionListener(this);
		add(actionButton);

	}
	
	public void addUserField() {
		enterUserLabel = new JLabel("Enter text: ");
		enterUserLabel.setBounds(0, 160, getWidth() - 20, 20);
		enterUserLabel.setFont(new Font("Dialog", Font.BOLD, 16));
		enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(enterUserLabel);
		
		enterUserField = new JTextField();
		enterUserField.setBounds(15, 190, getWidth() - 50, 40);
		enterUserField.setFont(new Font("Dialog", Font.BOLD, 20));
		enterUserLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(enterUserField);
	}
	
	private void handleTransaction(String transactionType, float amountVal) {
		Transaction transaction;
		if(transactionType.equalsIgnoreCase("Deposit") ) {
			user.setCurrentBalance(user.getCurrentBalance().add(new BigDecimal(amountVal)));
			
			//date null, use NOW() from sql later
			transaction = new Transaction(user.getId(), transactionType, new BigDecimal(amountVal), null);
		}else {
			user.setCurrentBalance(user.getCurrentBalance().subtract(new BigDecimal(amountVal)));
			
			transaction = new Transaction(user.getId(), transactionType, new BigDecimal(-amountVal), null);
		}
		
		//update database
		if(MyJDBC.addTransactionToDatabase(transaction) && MyJDBC.updateCurrentBalance(user)){
			JOptionPane.showMessageDialog(this, transactionType + " Succesfully!");
			
			//reset fields
			resetFieldsAndUpdateCurrentBalance();
		}else {
			JOptionPane.showMessageDialog(this, transactionType + " Failed..");
		}
		
	}
	
	private void resetFieldsAndUpdateCurrentBalance() {
		enterAmountField.setText("");
		
		//only appears when transfer is clicked
		if(enterUserField != null) {
			enterUserField.setText("");
		}
		
		//update current balance on dialog (recall method to update it )
		balanceLabel.setText("Balance: $" + user.getCurrentBalance());
		
		//update current balance on main gui
		bankingAppGui.getCurrentBalanceField().setText("$" + user.getCurrentBalance());
	}
	
	private void handleTransfer(User user, String transferredUsername, float amount) {
		//attempt to perform transfer
		if(MyJDBC.transfer(user, transferredUsername, amount)) {
			JOptionPane.showMessageDialog(this, "Transfer Successful");
			resetFieldsAndUpdateCurrentBalance();
		}else {
			JOptionPane.showMessageDialog(this, " Transfer Failed..");
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String buttonPressed = e.getActionCommand();
		
		float amountVal = Float.parseFloat(enterAmountField.getText());
		
		if(buttonPressed.equalsIgnoreCase("Deposit")) {
			//handle deposit transaction
			handleTransaction(buttonPressed, amountVal);
		}else {
			
			//validate input so that we dont have negative balances
			// -1 means the entered amount is is bigger than current balance, 0 equals, 1 is less
			int result = user.getCurrentBalance().compareTo(BigDecimal.valueOf(amountVal));
			
			if(result < 0) {
				JOptionPane.showMessageDialog(this, "Error: You are kind of broke for that");
				return;
			}
			
			if(buttonPressed.equalsIgnoreCase("Withdraw")) {
				handleTransaction(buttonPressed, amountVal);
			}else {
				//trasnfer
				String transferredUser = enterUserField.getText();
				
				//handle transfer
				handleTransfer(user, transferredUser, amountVal);
			}
		}
		
	}
}
