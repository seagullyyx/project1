import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.JScrollPane;
import java.awt.Rectangle;
import javax.swing.JTree;
import javax.swing.JTextArea;

public class GUI extends JFrame implements TreeSelectionListener {

	private  JScrollPane scrollPane;
	private  JTree tree;
	private  DefaultMutableTreeNode Email =  new DefaultMutableTreeNode("Email");
	private  DefaultMutableTreeNode Inbox =  new DefaultMutableTreeNode("Inbox");
	private  DefaultMutableTreeNode Outbox =  new DefaultMutableTreeNode("Outbox");
	private  DefaultMutableTreeNode Draftbox =  new DefaultMutableTreeNode("Draftbox");
	private  JPanel contentPane;
	private  Message[] temp = new Message[1];
	private  New_ConnectDB db;
	private  Account user;
	private JTextArea abc;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try { 
					GUI frame = new GUI();
			
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		db = new New_ConnectDB("jdbc:mysql://68.183.26.151:3306/EmailServer", "root", "P@ss1234");
		user = db.getAccount("kei@yg.com", "kei1234");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 484, 250);
		contentPane.add(scrollPane);
		tree = new JTree(Email);
		Email.add(Inbox);
		Email.add(Outbox);
		Email.add(Draftbox);
		tree.expandRow(0);
		user.setMessage(db.getInbox("kei@yg.com"));
		temp = user.getMessage();
		creatTreeNode(temp,Inbox);
		
		user.setMessage(db.getOutbox("kei@yg.com"));
		temp = user.getMessage();
		creatTreeNode(temp,Outbox);
		tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(this);
		
		scrollPane.setViewportView(tree);
		
		abc = new JTextArea();
		abc.setLineWrap(true);
		abc.setEditable(false);
		abc.setBounds(27, 261, 411, 167);
		contentPane.add(abc);
	}

	
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
	    DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();
	    if(node==null) {
	    	return;
	    }
	    Object nodeInfo = node.getUserObject();
	    if(node.isLeaf()) {
	    	msg i = (msg) nodeInfo;
	    	abc.setText(i.getBody());
	    }
	    
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
		if(y.equals(Outbox) || y.equals(Draftbox)) {
			for(int x = 0; x < length; x++) {
				y.add( new DefaultMutableTreeNode(
					   new msg(i[x].getTo(),i[x].getFrom(),i[x].getSubject(),i[x].getTime()
							   ,i[x].getBody(), "other")
					));
		
			}
		}
	}
}
