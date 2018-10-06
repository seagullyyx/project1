import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.SystemColor;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;

public class loginGUI extends JFrame {
	
	private static String Account, Password, Domain = "@cq.edu";
	private static boolean login = false,  Varify_PW = false;
	public static JFrame login_mainFrame;
	private static JTextField account_text;
	private static JPasswordField password_text;
	private static Label password_label;
	private static New_ConnectDB db;


	public static boolean getLoginStatus() {
		return login;
	}
	
	public loginGUI(New_ConnectDB x) throws IOException {
		db = x;
		initialize();
	}
	
	private void initialize() throws IOException {
		db = new New_ConnectDB("jdbc:mysql://68.183.26.151:3306/EmailServer", "root", "P@ss1234");
		//Login Main Frame
		login_mainFrame = new JFrame();
		login_mainFrame.setResizable(false);
		login_mainFrame.setForeground(Color.GRAY);
		login_mainFrame.setBackground(Color.GRAY);
		login_mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
		login_mainFrame.getContentPane().setLayout(null);
		login_mainFrame.setTitle("Email"); 
		login_mainFrame.setBounds(100, 100, 600, 600);
		login_mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String elem1 = "@cq.edu";
		String elem2 = "@yg.com";
		String elem3 = "@lnb.gov";
		
		// welcome Label
		Label welcome_label = new Label("Welcome User");
		welcome_label.setBounds(90, 30, 400, 125);
		login_mainFrame.getContentPane().add(welcome_label);
		welcome_label.setFont(new Font("Times New Roman", Font.BOLD | Font.ITALIC, 40));
		welcome_label.setAlignment(Label.CENTER);
		
		// Account label
		Label account_label = new Label("Account ID:");
		account_label.setBounds(60, 170, 150, 100);
		login_mainFrame.getContentPane().add(account_label);
		account_label.setFont(new Font("Times New Roman", Font.BOLD, 25));
		
		// Account TextField and Domain ComboBox
		account_text = new JTextField();
		account_text.setBounds(225, 200, 200, 50);
		login_mainFrame.getContentPane().add(account_text);
		account_text.setToolTipText("Enter your Account");
		account_text.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		account_text.setBackground(Color.WHITE);
		account_text.setForeground(Color.BLACK);
		account_text.setColumns(10);
		JComboBox<String> domian_comboBox = new JComboBox<String>();
		domian_comboBox.setForeground(Color.BLACK);
		domian_comboBox.setBackground(Color.LIGHT_GRAY);
		domian_comboBox.setBounds(423, 200, 120, 50);
		login_mainFrame.getContentPane().add(domian_comboBox);
		domian_comboBox.addItem(elem1);
		domian_comboBox.addItem(elem2);
		domian_comboBox.addItem(elem3);
		domian_comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				if(domian_comboBox.getSelectedItem().equals(elem1) ) Domain = "@cq.edu"; 
				if(domian_comboBox.getSelectedItem().equals(elem2)) Domain = "@yg.com";
				if(domian_comboBox.getSelectedItem().equals(elem3)) Domain = "@lnb.gov";
			}
		});
		
		// Password label
		password_label = new Label("Password:");
		password_label.setBounds(60, 275, 150, 100);
		login_mainFrame.getContentPane().add(password_label);
		password_label.setFont(new Font("Times New Roman", Font.BOLD, 25));
		
		// Password TextField
		password_text = new JPasswordField(10);
		password_text.setBounds(225, 300, 200, 50);
		login_mainFrame.getContentPane().add(password_text);
		password_text.setToolTipText("Enter your Password");
		password_text.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		password_text.setForeground(Color.BLACK);
		password_text.setColumns(10);
		password_text.setBackground(Color.WHITE);
		password_text.setEchoChar('*');
		
		// button that display account and password rules
		JButton pwRule_Button = new JButton("Rules");
		pwRule_Button.setBackground(Color.LIGHT_GRAY);
		pwRule_Button.setBounds(423, 300, 125, 51);
		login_mainFrame.getContentPane().add(pwRule_Button);
		pwRule_Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,
						 "Password should be minimum 4 characters" +
				"and maximum 12 characters", "Rules", 1);
			}
		});
		pwRule_Button.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		// Login Button
		JButton login_button = new JButton("Login");
		login_button.setBounds(50, 400, 225, 100);
		login_mainFrame.getContentPane().add(login_button);
		login_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				login_event();
			}
		});
		login_button.setBackground(Color.LIGHT_GRAY);
		login_button.setFont(new Font("Tahoma", Font.BOLD, 25));
		
		// Register Button
		JButton register_button = new JButton("Register");
		register_button.setBounds(325, 400, 225, 100);
		login_mainFrame.getContentPane().add(register_button);
		register_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				register_event();
			}
		});
		register_button.setFont(new Font("Tahoma", Font.BOLD, 25));
		register_button.setBackground(Color.LIGHT_GRAY);

	}
		
	// check if password is legal or not
	public boolean checkPassword(String pw) {
		int len = pw.length();
		boolean isLength = false;
		if((4<=len) && (len<=12)) isLength = true;
		return isLength;
	}
	
	public boolean hasAt(String uname) {
		return uname.contains("@");
	}
	
	public boolean checkUserName(String name, String domain) {
		int len = name.length() + domain.length();
		boolean isLength = true;
		if(name.length()==0) isLength = false;
		if(!((0<len)&& (len<=20))) isLength = false;
		return isLength;
	}
	private void register_event() {
		Account = account_text.getText();
		Password = new String (password_text.getPassword()) ;
		
		try {
			if(Account.equals("") && Password.equals("")) return;
			if(!checkPassword(Password)) {
				JOptionPane.showMessageDialog(null, "Incorrect Password", "opps", 0);
				return;
			}
			if(hasAt(Account)) {
				JOptionPane.showMessageDialog(null, "You cannot have @ in your address. Please select domain from menu bar on the right", "opps", 0);
				return;
			}
			if(!checkUserName(Account, Domain)) {
				JOptionPane.showMessageDialog(null, "The length of address should be at least 1 and at most 20 character", "opps", 0);
				return;
			}
			db.createAccount(Account + Domain, Password);
		} catch(SQLIntegrityConstraintViolationException prime) {
			JOptionPane.showMessageDialog(null, "The address already exists", "oops", 0);
			return;
		} catch(SQLException sqle) {
			sqle.getStackTrace();
		}
		
		login = true;
		login_mainFrame.setVisible(false);  // hide the login panel
		EmailGUI.email_mainFrame.setTitle(Account + Domain);
		EmailGUI.email_mainFrame.setVisible(true);
		JOptionPane.showMessageDialog(null, "You have successful"
				+ "signed in!", "Congratulations!", 1);
		EmailGUI.displaygreeting();
	}
	private void login_event() {
		Account = account_text.getText();
		Password = new String (password_text.getPassword()) ; 
		if(Account.equals("") && Password.equals("")) return;
		
		if((db.checkUser(Account + Domain, Password))) {
			//login = true;
			login_mainFrame.setVisible(false); // display email panel
			Varify_PW = false; 
			EmailGUI.email_mainFrame.setVisible(true);
			EmailGUI.email_mainFrame.setTitle(Account + Domain);
			JOptionPane.showMessageDialog(null, "You have successful" +
				"logged in!", "Congratulations!", 1);
			return;
		}
		JOptionPane.showMessageDialog(null, "Incorrect Account or Password", "opps", 0);	
		
	}
	public static String getPassword() {
		return Password;
	}
	
}
