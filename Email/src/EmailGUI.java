import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.Popup;

import java.awt.Font;
import javax.swing.JScrollPane;


public class EmailGUI extends JFrame{
	public static JFrame email_mainFrame;
	private static JPanel contentPane;
	private static JTextField to_textfield;
	private static JTextField subject_textfield;
	private static JTextArea subject_label;
	private static JTextArea to_label;
	private static JTextArea greeting_label ;
	private static JTextArea Email_TextArea;
	private static JTextArea Draft_TextArea;
	private static JTextArea Inbox_TextArea;
	private static JButton send_button;
	private static JButton save_button;
	private static JButton discard_button;
	private static JButton draft_button;
	private static JButton outbox_button;
	private static JButton logout_button;
	private static JButton new_button;
	private static New_ConnectDB db;
	private static Account user;
	private JButton inbox_button;

	public EmailGUI(New_ConnectDB x) throws IOException {
		db = x;
		initialize();
	}
	private void initialize() throws IOException {
	
		getContentPane().setLayout(null);
		email_mainFrame = new JFrame();
		email_mainFrame.setResizable(true);
		email_mainFrame.setForeground(Color.GRAY);
		email_mainFrame.setBackground(Color.GRAY);
		email_mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
		email_mainFrame.getContentPane().setLayout(null);
		email_mainFrame.setBounds(100, 100, 800, 800);
		email_mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		new_button = new JButton("New");
		new_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayEmailFunction();
			}
		});
		new_button.setBackground(Color.LIGHT_GRAY);
		new_button.setBounds(140, 0, 150, 57);
		email_mainFrame.getContentPane().add(new_button);
		
		save_button = new JButton("Save");
		save_button.setBackground(Color.LIGHT_GRAY);
		save_button.setBounds(299, 0, 150, 57);
		email_mainFrame.getContentPane().add(save_button);
		save_button.setVisible(false);
		
		discard_button = new JButton("Discard");
		discard_button.setBackground(Color.LIGHT_GRAY);
		discard_button.setBounds(456, 0, 150, 57);
		email_mainFrame.getContentPane().add(discard_button);
		discard_button.setVisible(false);
		
		send_button = new JButton("Send");
		send_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(to_textfield.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "You need to put in the email address that you want to send to" ,"Error", 0);
					return;
				}
				if(!db.checkEmailExists(to_textfield.getText())) {
					JOptionPane.showMessageDialog(null, "Reciptent Email does not exist" ,"Error", 0);
					return;
				}
				if(subject_textfield.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Missing Subjects" ,"Error", 0);
					return;
				}
				if(Email_TextArea.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Empty Emails" ,"Error", 0);
					return;
				}
				db.sendMessage(email_mainFrame.getTitle(), to_textfield.getText(), subject_textfield.getText(), Email_TextArea.getText());
			}
		});
		send_button.setBackground(Color.LIGHT_GRAY);
		send_button.setBounds(613, 0, 150, 57);
		email_mainFrame.getContentPane().add(send_button);
		send_button.setVisible(false);
		
		to_label = new JTextArea();
		to_label.setFont(new Font("Arial", Font.BOLD, 18));
		to_label.setBackground(Color.LIGHT_GRAY);
		to_label.setEditable(false);
		to_label.setText("To:");
		to_label.setBounds(191, 73, 33, 30);
		email_mainFrame.getContentPane().add(to_label);
		to_label.setVisible(false);
		
		to_textfield = new JTextField();
		to_textfield.setBounds(227, 65, 536, 46);
		email_mainFrame.getContentPane().add(to_textfield);
		to_textfield.setColumns(10);
		to_textfield.setVisible(false);
		
		subject_label = new JTextArea();
		subject_label.setEditable(false);
		subject_label.setBackground(Color.LIGHT_GRAY);
		subject_label.setFont(new Font("Arial", Font.BOLD, 18));
		subject_label.setText("Subject:");
		subject_label.setBounds(150, 131, 77, 24);
		email_mainFrame.getContentPane().add(subject_label);
		subject_label.setVisible(false);
		
		subject_textfield = new JTextField();
		subject_textfield.setColumns(10);
		subject_textfield.setBounds(227, 123, 536, 46);
		email_mainFrame.getContentPane().add(subject_textfield);
		subject_textfield.setVisible(false);
		
	    greeting_label = new JTextArea();
	    greeting_label.setLineWrap(true);
	    greeting_label.setFont(new Font("Arial", Font.BOLD, 20));
	    greeting_label.setEditable(false);
	    greeting_label.setBackground(Color.LIGHT_GRAY);
	    greeting_label.setBounds(10, 16, 120, 148);
	    email_mainFrame.getContentPane().add(greeting_label);
		
		inbox_button = new JButton("Inbox");
		inbox_button.setBackground(Color.LIGHT_GRAY);
		inbox_button.setBounds(0, 175, 150, 100);
	    inbox_button.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		user = db.getAccount(email_mainFrame.getTitle(), loginGUI.getPassword() );
	    		hideEmailFunction();
	    		showInbox();
	    		if(user.isEmpty()) {
	    			user.setMessage(db.getInbox(email_mainFrame.getTitle()));
	    		}
	    		Message[] temp = user.getMessage();
	    		for(int i = 0; i < user.getMessage().length; i++) {
	    	
	    			Inbox_TextArea.append(temp[i].getFrom());
	    			Inbox_TextArea.append("\n");
	    			Inbox_TextArea.append(temp[i].getSubject());
	    			Inbox_TextArea.append("\n");
	    			Inbox_TextArea.append(temp[i].getTime());
	    			Inbox_TextArea.append("\n");
	    			Inbox_TextArea.append(temp[i].getBody());
	    			Inbox_TextArea.append("\n");
	    			Inbox_TextArea.append("\n");
	    		
	    		}
	    	}
	    });
		email_mainFrame.getContentPane().add(inbox_button);
		
		draft_button = new JButton("Draft");
		draft_button.setBackground(Color.LIGHT_GRAY);
		draft_button.setBounds(0, 370, 150, 100);
		email_mainFrame.getContentPane().add(draft_button);
		draft_button.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
				Draft_TextArea.setVisible(true);
	    	}
	    });
	    
		outbox_button = new JButton("Outbox");
		outbox_button.setBackground(Color.LIGHT_GRAY);
		outbox_button.setBounds(0, 273, 150, 100);
		email_mainFrame.getContentPane().add(outbox_button);
		
		JButton btnUndecided = new JButton("Undecided");
		btnUndecided.setBackground(Color.LIGHT_GRAY);
		btnUndecided.setBounds(0, 465, 150, 100);
		email_mainFrame.getContentPane().add(btnUndecided);
		
	    logout_button = new JButton("Log Out");
	    logout_button.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
				logout_event();
	    	}
	    });
	    logout_button.setBackground(Color.LIGHT_GRAY);
	    logout_button.setBounds(0, 562, 150, 122);
	    email_mainFrame.getContentPane().add(logout_button);
		
		Email_TextArea = new JTextArea();
		Email_TextArea.setBounds(150, 175, 624, 509);
		Email_TextArea.setVisible(false);
		email_mainFrame.getContentPane().add(Email_TextArea);

		
		Draft_TextArea = new JTextArea();
		Draft_TextArea.setBounds(150, 175, 624, 509);
		Draft_TextArea.setVisible(false);
		email_mainFrame.getContentPane().add(Draft_TextArea);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(150, 175, 624, 509);
		email_mainFrame.getContentPane().add(scrollPane);
		
		Inbox_TextArea = new JTextArea();
		scrollPane.setViewportView(Inbox_TextArea);
		Inbox_TextArea.setLineWrap(true);
		Inbox_TextArea.setWrapStyleWord(true);
		Inbox_TextArea.setVisible(false);
		

		
		// menu Bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(Color.WHITE);
		email_mainFrame.setJMenuBar(menuBar);	
		// Account menu
		JMenu account_Menu = new JMenu("Account");
		menuBar.add(account_Menu);
		// Logout MenuItem that belongs to Account menu
		JMenuItem mntmLogOut = new JMenuItem("Log Out");
		mntmLogOut.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) {
				logout_event();
			}
		});
		account_Menu.add(mntmLogOut);
	}
	private static void displayEmailFunction() {
		to_label.setVisible(true);
		to_textfield.setVisible(true);
		subject_label.setVisible(true);
		subject_textfield.setVisible(true);
		save_button.setVisible(true);
		discard_button.setVisible(true);
		send_button.setVisible(true);
		Email_TextArea.setVisible(true);
		Inbox_TextArea.setVisible(false);
	}
	private static void hideEmailFunction() {
		to_label.setVisible(false);
		to_textfield.setVisible(false);
		subject_label.setVisible(false);
		subject_textfield.setVisible(false);
		save_button.setVisible(false);
		discard_button.setVisible(false);
		send_button.setVisible(false);
		Email_TextArea.setVisible(false);
		Inbox_TextArea.setVisible(false);
	}
	private static void hideDraft() {
		Draft_TextArea.setVisible(false);
		
	}
	private static void showInbox() {
		Inbox_TextArea.setVisible(true);
	}
	private static void showOutbox() {
		
	}
	public static void displaygreeting() {
		greeting_label.setText("Welcome"
				+ "\n" +
				"user:" 
				+ "\n" +
				email_mainFrame.getTitle());
	}
	private static void logout_event() {
		JOptionPane.showMessageDialog(null, "You are logged out", "Succese", 1);
		EmailGUI.email_mainFrame.setVisible(false); // hide email panel
		loginGUI.login_mainFrame.setVisible(true);  // display login panel
		loginGUI.login_mainFrame.setTitle("Email"); 
		hideEmailFunction();
	}
}
