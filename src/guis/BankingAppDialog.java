package guis;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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
	private JPanel pastTransactionPanel;
	private ArrayList<Transaction> pastTransactions;
	
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
	
	public void addPastTransactionComponents() {
		// container where we will store each transaction
		pastTransactionPanel = new JPanel();

		//make Layout 1x1 
		pastTransactionPanel.setLayout(new BoxLayout(pastTransactionPanel, BoxLayout.Y_AXIS));
		
		//make scrollable
		JScrollPane scrollPane= new JScrollPane(pastTransactionPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(0, 20, getWidth() -20, getHeight() - 60);
		
		//perform db call to retrieve all transactions from user
		pastTransactions = MyJDBC.getPastTransaction(user);
		
		//go through list and add each to it
		for(int i = 0; i < pastTransactions.size(); i++) {
			//store current transaction
			Transaction pastTransaction = pastTransactions.get(i);
			
			//create a container to store an individual transaction
			JPanel pastTransactionContainer = new JPanel();
			pastTransactionContainer.setLayout(new BorderLayout());
			
			//create transaction type label
			JLabel transactionTypeLabel = new JLabel(pastTransaction.getTransactionType());
			transactionTypeLabel.setFont(new Font("Dialog", Font.BOLD, 20));
			
			//create transaction amount label
			JLabel transactionAmountLabel = new JLabel(String.valueOf(pastTransaction.getTransactionAmount()));
			transactionAmountLabel.setFont(new Font("Dialog", Font.BOLD, 20));
			
			//create transaction date label
			JLabel transactionDateLabel = new JLabel(String.valueOf(pastTransaction.getTransactionDate()));
			transactionDateLabel.setFont(new Font("Dialog", Font.BOLD, 20));
			
			//add to container
			pastTransactionContainer.add(transactionTypeLabel, BorderLayout.WEST);
			pastTransactionContainer.add(transactionAmountLabel, BorderLayout.EAST);
			pastTransactionContainer.add(transactionDateLabel, BorderLayout.SOUTH);
			
			//background color
			pastTransactionContainer.setBackground(Color.WHITE);
			
			//add black border to past transaction pane
			pastTransactionContainer.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			
			//add to the panel
			pastTransactionPanel.add(pastTransactionContainer);
		}
		
		add(scrollPane);
		
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
