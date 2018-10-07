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
import javax.swing.JTree;
import javax.swing.Popup;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import java.awt.Font;
import java.awt.ScrollPane;

import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.SystemColor;


public class EmailGUI extends JFrame implements TreeSelectionListener {
	public static JFrame email_mainFrame;
	private static JPanel contentPane;
	public static JTextField to_textfield;
	public static JTextField subject_textfield;
	public static JTextArea subject_label;
	public static JTextArea to_label;
	public static JTextArea Email_TextArea;
	public static JTextArea Draft_TextArea ;
	private static JButton send_button;
	private static JButton save_button;
	private static JButton discard_button;
	private static JButton draft_button;
	private static JButton outbox_button;
	private static JButton logout_button;
	private static JButton new_button;
	private static JTextArea tip ;
	private static New_ConnectDB db;
	private static Account user;
	private static JButton reply_button;
	final private DefaultMutableTreeNode Email =  new DefaultMutableTreeNode("Email");
	final private DefaultMutableTreeNode Inbox =  new DefaultMutableTreeNode("Inbox");
	final private DefaultMutableTreeNode Outbox =  new DefaultMutableTreeNode("Outbox");
	final private DefaultMutableTreeNode Draftbox =  new DefaultMutableTreeNode("Draftbox");
	private  Message[] temp ;
	private JButton inbox_button;
	private JTree tree;
	public static JScrollPane scrollPane;
	public EmailGUI(New_ConnectDB x, String Email, String passowrd) throws IOException {
		db = x;
		user = db.getAccount(Email, passowrd);
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
		email_mainFrame.setBounds(100, 100, 800, 750);
		email_mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		new_button = new JButton("New");
		new_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				displayEmailFunction();
				clearMSG();
			}
		});

		new_button.setBackground(Color.LIGHT_GRAY);
		new_button.setBounds(0, 77, 150, 100);
		email_mainFrame.getContentPane().add(new_button);
		
		save_button = new JButton("Save");
		save_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.createNewDraft(user.getID(), subject_textfield.getText(), Email_TextArea.getText(), to_textfield.getText());
				hideEmailFunction();
			}
		});
		save_button.setBackground(Color.LIGHT_GRAY);
		save_button.setBounds(299, 0, 150, 57);
		email_mainFrame.getContentPane().add(save_button);
		save_button.setVisible(false);
		
		discard_button = new JButton("Discard");
		discard_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clearMSG();
				hideEmailFunction();
			}
		});
		discard_button.setBackground(Color.LIGHT_GRAY);
		discard_button.setBounds(456, 0, 150, 57);
		email_mainFrame.getContentPane().add(discard_button);
		discard_button.setVisible(false);
		
		send_button = new JButton("Send");
		send_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				if(subject_textfield.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Missing Subjects" ,"Error", 0);
					return;
				}
				if(Email_TextArea.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Empty Emails" ,"Error", 0);
					return;
				}
				// Check every email in to_field exist
				String temp = to_textfield.getText();
				for(int i = 0; i < temp.length(); i++) {
					if(temp.charAt(i) != ' ') {
						temp = temp.substring(i);
						break;
					}
				}
				String [] recepients = temp.split("\\s+");
				
				boolean noUserFound = false;
				int numOfEmails = recepients.length;
				for(int i=0;i<numOfEmails;i++) 
					if(!db.checkEmailExists(recepients[i]) && !noUserFound) noUserFound = true;				
				if(noUserFound) {
					JOptionPane.showMessageDialog(null, "Reciptent Email does not exist" ,"Error", 0);
					return;
				}
				// Passed all test. Send a message to each email
				for(int i=0;i<numOfEmails;i++) {
					db.sendMessage(email_mainFrame.getTitle(), recepients[i], subject_textfield.getText(), Email_TextArea.getText());
					try {
						// Avoid the same time stamp
						Thread.sleep(900);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				clearMSG();
				JOptionPane.showMessageDialog(null, "Message Sent" ,"Congratulation!", 1);
			}
		});
		send_button.setBackground(Color.LIGHT_GRAY);
		send_button.setBounds(613, 0, 150, 57);
		email_mainFrame.getContentPane().add(send_button);
		send_button.setVisible(false);
		
		
		scrollPane = new JScrollPane();
		scrollPane.setToolTipText("");
		scrollPane.setBounds(150, 175, 624, 509);
		email_mainFrame.getContentPane().add(scrollPane);
		scrollPane.setVisible(false);

		
		to_label = new JTextArea();
		to_label.setFont(new Font("Arial", Font.BOLD, 18));
		to_label.setBackground(Color.LIGHT_GRAY);
		to_label.setEditable(false);
		to_label.setText("To:");
		to_label.setBounds(191, 73, 33, 30);
		email_mainFrame.getContentPane().add(to_label);
		to_label.setVisible(false);
		
		to_textfield = new JTextField();
		to_textfield.setToolTipText("Please use space to sepearte different emails");
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
		subject_textfield.setToolTipText("");
		subject_textfield.setColumns(10);
		subject_textfield.setBounds(227, 123, 536, 46);
		email_mainFrame.getContentPane().add(subject_textfield);
		subject_textfield.setVisible(false);
		
		inbox_button = new JButton("Inbox");
		inbox_button.setBackground(Color.LIGHT_GRAY);
		inbox_button.setBounds(0, 175, 150, 100);
	    inbox_button.addActionListener(new ActionListener() {
	    
	    	public void actionPerformed(ActionEvent arg0) {
	    		createTree();
	    		hideEmailFunction();
	    		if(tree.isCollapsed(new TreePath(Inbox.getPath()))) {
	    			tree.expandPath(new TreePath(Inbox.getPath()));
	    		}
	    		else {
	    			tree.collapsePath(new TreePath(Inbox.getPath()));
	    		}
				scrollPane.setVisible(true);
				createTree();
	    	}
	    });
		email_mainFrame.getContentPane().add(inbox_button);
		
		draft_button = new JButton("Draft");
		draft_button.setBackground(Color.LIGHT_GRAY);
		draft_button.setBounds(0, 370, 150, 100);
		email_mainFrame.getContentPane().add(draft_button);
		draft_button.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		createTree();
	    		hideEmailFunction();
	    		if(tree.isCollapsed(new TreePath(Draftbox.getPath()))) {
	    			tree.expandPath(new TreePath(Draftbox.getPath()));
	    		}
	    		else {
	    			tree.collapsePath(new TreePath(Draftbox.getPath()));
	    		}
				scrollPane.setVisible(true);
				createTree();
	    	}
	    });
	    
		outbox_button = new JButton("Outbox");
		outbox_button.setBackground(Color.LIGHT_GRAY);
		outbox_button.setBounds(0, 273, 150, 100);
		email_mainFrame.getContentPane().add(outbox_button);
	    outbox_button.addActionListener(new ActionListener() {
	    	public void actionPerformed(ActionEvent arg0) {
	    		createTree();
	    		hideEmailFunction();
	    		if(tree.isCollapsed(new TreePath(Outbox.getPath()))) {
	    			tree.expandPath(new TreePath(Outbox.getPath()));
	    		}
	    		else {
	    			tree.collapsePath(new TreePath(Outbox.getPath()));
	    		}
				scrollPane.setVisible(true);
		
	    	}
	    });
		
		JButton Recieve = new JButton("Refresh");
		Recieve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				createTree();

			}
		});
		Recieve.setBackground(Color.LIGHT_GRAY);
		Recieve.setBounds(0, 465, 150, 100);
		email_mainFrame.getContentPane().add(Recieve);
		
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
		Email_TextArea.setVisible(false);
		Email_TextArea.setLineWrap(true);
		Email_TextArea.setBounds(150, 175, 624, 509);
		email_mainFrame.getContentPane().add(Email_TextArea);
		
		Draft_TextArea = new JTextArea();
		Draft_TextArea.setBounds(150, 175, 624, 509);
		Draft_TextArea.setLineWrap(true);
		Draft_TextArea.setVisible(false);
		email_mainFrame.getContentPane().add(Draft_TextArea);

		
		JLabel lblNewLabel = new JLabel("Welcome!");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblNewLabel.setBounds(10, 11, 88, 55);
		email_mainFrame.getContentPane().add(lblNewLabel);
		
		reply_button = new JButton("Reply");
		reply_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				db.sendReplyTo(email_mainFrame.getTitle(), to_textfield.getText(), subject_textfield.getText(), Email_TextArea.getText(), user.getID());
			}
		});
		reply_button.setVisible(false);
		reply_button.setBounds(613, 0, 150, 57);
		email_mainFrame.getContentPane().add(reply_button);
		
		tip = new JTextArea();
		tip.setBackground(SystemColor.activeCaptionBorder);
		tip.setLineWrap(true);
		tip.setEditable(false);
		tip.setWrapStyleWord(true);
		tip.setText("Please use space to seperate mutiple recipients");
		tip.setBounds(108, 0, 181, 66);
		tip.setVisible(false);
		email_mainFrame.getContentPane().add(tip);
		

		
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
	public static void displayEmailFunction() {
		to_label.setVisible(true);
		to_textfield.setVisible(true);
		subject_label.setVisible(true);
		subject_textfield.setVisible(true);
		save_button.setVisible(true);
		discard_button.setVisible(true);
		send_button.setVisible(true);
		Email_TextArea.setVisible(true);
		scrollPane.setVisible(false);
		reply_button.setVisible(false);
		send_button.setVisible(true);
		tip.setVisible(true);
		to_textfield.setEditable(true);
	}
	public static void ShowReply() {
		to_label.setVisible(true);
		to_textfield.setVisible(true);
		subject_label.setVisible(true);
		subject_textfield.setVisible(true);
		save_button.setVisible(true);
		discard_button.setVisible(true);
		send_button.setVisible(true);
		Email_TextArea.setVisible(true);
		scrollPane.setVisible(false);
		reply_button.setVisible(true);
		send_button.setVisible(false);
		tip.setVisible(false);
		to_textfield.setEditable(false);
	}
	public static void hideEmailFunction() {
		to_label.setVisible(false);
		to_textfield.setVisible(false);
		subject_label.setVisible(false);
		subject_textfield.setVisible(false);
		save_button.setVisible(false);
		discard_button.setVisible(false);
		send_button.setVisible(false);
		Email_TextArea.setVisible(false);
		reply_button.setVisible(false);
		tip.setVisible(false);
		to_textfield.setEditable(true);
		
	}
	private static void logout_event() {
		JOptionPane.showMessageDialog(null, "You are logged out", "Succese", 1);
		EmailGUI.email_mainFrame.setVisible(false); // hide email panel
		loginGUI.login_mainFrame.setVisible(true);  // display login panel
		loginGUI.login_mainFrame.setTitle("Email"); 
		hideEmailFunction();
	}

	public void valueChanged(TreeSelectionEvent e) {
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();
	    if(node==null || node == Inbox || node == Outbox || node == Draftbox || node == Email ) {
	    	return;
	    }
	    Object nodeInfo = node.getUserObject();
	    if(node.isLeaf() ) {
	    	msg i = (msg) nodeInfo;
	    	boolean isDraft = false;
	    	if (Draftbox.isNodeChild(node)) isDraft = true;
	        try {
	        	actualMsg x;
	        	if(isDraft) {
	        		x = new actualMsg(db,user.getID(),"Draftbox");
	        	}
	        	else x = new actualMsg(db,user.getID(),"Inbox");
		    	x.setLocationRelativeTo(null);
		    	x.setBody(i.getBody());
		    	x.setFrom(i.getFrom());
		    	x.setTo(i.getTo());
		    	x.setTime(i.getTime());
		    	x.setSub(i.getSubject());
		    	
		    	x.setVisible(true);
		    	if(isDraft) x.editable();
		    	
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }

	    	
	    }
	}
	public void createTree() {
		
		Inbox.removeAllChildren();
		Outbox.removeAllChildren();
		Draftbox.removeAllChildren();
		Email.removeAllChildren();
		tree = new JTree(Email);
		Email.add(Inbox);
		Email.add(Outbox);
		Email.add(Draftbox);
		tree.expandRow(0);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		scrollPane.setViewportView(tree);
		Email.add(Inbox);
		Email.add(Outbox);
		Email.add(Draftbox);
		user = db.getAccount(email_mainFrame.getTitle(),loginGUI.getPassword());
		user.setMessage(db.getInbox(email_mainFrame.getTitle()));
		temp = user.getMessage();
		creatTreeNode(temp,Inbox);
		user.setMessage(db.getOutbox(email_mainFrame.getTitle()));
		temp = user.getMessage();
		creatTreeNode(temp,Outbox);
		user.setMessage(db.getDrafts(user.getID()));
		temp = user.getMessage();
		creatTreeNode(temp,Draftbox);
	}
	public void creatTreeNode(Message[] i, DefaultMutableTreeNode y) {
		int length = i.length;
		if(y.equals(Inbox)) {
			for(int x = 0; x < length; x++) {
				y.add( new DefaultMutableTreeNode(
					   new msg(i[x].getTo(),i[x].getFrom(),i[x].getSubject(),i[x].getTime()
							   ,i[x].getBody(), "Inbox")
					));
			}
		}
		if(y.equals(Outbox)) {
			for(int x = 0; x < length; x++) {
				y.add( new DefaultMutableTreeNode(
					   new msg(i[x].getTo(),i[x].getFrom(),i[x].getSubject(),i[x].getTime()
							   ,i[x].getBody(), "Outbox")
					));
		
			}
		}
		if(y.equals(Draftbox)) {
			for(int x = 0; x < length; x++) {
				y.add( new DefaultMutableTreeNode(
					   new msg(i[x].getTo(),i[x].getFrom(),i[x].getSubject(),i[x].getTime()
							   ,i[x].getBody(), "Draft")
					));
		
			}
		}
	}
	public static String getEmail() {
		return email_mainFrame.getTitle();
	}
	public static String getPW() {
		return loginGUI.getPassword();
	}
	public static void clearMSG() {
		to_textfield.setText("");
		subject_textfield.setText("");
		Email_TextArea.setText("");
	}
	public static void hideTree() {
		scrollPane.setVisible(false);
	}
}
