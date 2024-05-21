package guis;

import javax.swing.JFrame;

import db_objects.User;

public abstract class BaseFrame extends JFrame {
	protected User user;
	
	public BaseFrame(String title) {
		initialize(title);
	}
	
	public BaseFrame(String title, User user) {
		this.user = user;
		
		initialize(title);
	}
	
	private void initialize(String title) {
		//instantiate iframe properties and add a title to the bar
		setTitle(title);
		
		//set size for the window
		setSize(420, 600);
		
		//end the program with the 'x'
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//Freely position elements across the window
		setLayout(null);
		
		setResizable(false);
		
		setLocationRelativeTo(null);
		
		//add elements to the window 
		addGuiComponents();
			
		
	};
	
	//subclasses will definie this method
	protected abstract void addGuiComponents();
}
