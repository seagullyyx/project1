import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class actualMsg extends JFrame {

	private JPanel contentPane;
	private JTextArea body;
	private JTextField subject;
	private JTextField from;
	private JTextField time;
	private JTextField to;

	private JButton send;
	private JButton Reply;
	private JButton Delete;
	private JButton save;
	/**
	 * Create the frame.
	 */
	
	public actualMsg(New_ConnectDB db, int uid, String box) {
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 97, 484, 364);
		contentPane.add(scrollPane);
		
		body = new JTextArea();
		body.setEditable(false);
		body.setLineWrap(true);
		body.setWrapStyleWord(true);
		scrollPane.setViewportView(body);
		
	    Reply = new JButton("Reply");
		Reply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EmailGUI.clearMSG();
				EmailGUI.ShowReply();
				EmailGUI.subject_textfield.setText("Re: " + subject.getText());
				EmailGUI.to_textfield.setText(from.getText());
				EmailGUI.Email_TextArea.setText("\n" + "\n" + "\n" + "\n" +
				"-----------------------------------------------------------------------------------------------------------------------------------------------------------" 
					+ "\n" +	
				"Reply to " + from.getText() + '\n' +  from.getText() + '\n' + to.getText() + '\n' + time.getText() + '\n' + body.getText());
				EmailGUI.parentID = db.getParentID(from.getText(), to.getText(), time.getText());
				System.out.println(from.getText() + " " + to.getText() + " " + time.getText());
				dispose();
				
			}
		});
		Reply.setBounds(0, 5, 123, 40);
		contentPane.add(Reply);
		
	    Delete = new JButton("Delete");
		Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(box.equals("Inbox")) {
					db.deleteSentMessage(from.getText(),to.getText(), time.getText());
					JOptionPane.showMessageDialog(null, "Message Deleted" ,"Congratulation!", 1);
					
				}
				if(box.equals("Draftbox")) {
					db.deleteDraftMessage(uid, time.getText());
					JOptionPane.showMessageDialog(null, "Draft Deleted" ,"Congratulation!", 1);
				}
				EmailGUI.hideTree();
				dispose();
			}
		});
		Delete.setBounds(0, 48, 123, 40);
		contentPane.add(Delete);
		
		subject = new JTextField();
		subject.setEditable(false);
		subject.setBounds(189, 2, 285, 26);
		contentPane.add(subject);
		subject.setColumns(10);
		
		from = new JTextField();
		from.setEditable(false);
		from.setColumns(10);
		from.setBounds(189, 34, 120, 26);
		contentPane.add(from);
		
		time = new JTextField();
		time.setEditable(false);
		time.setColumns(10);
		time.setBounds(189, 66, 120, 26);
		contentPane.add(time);
		
		JLabel lblNewLabel = new JLabel("From");
		lblNewLabel.setBounds(133, 34, 46, 26);
		contentPane.add(lblNewLabel);
		
		JLabel lblSubjcet = new JLabel("Subject");
		lblSubjcet.setBounds(133, 3, 46, 26);
		contentPane.add(lblSubjcet);
		
		JLabel lblTime = new JLabel("Time: ");
		lblTime.setBounds(133, 66, 46, 26);
		contentPane.add(lblTime);
		
		JLabel lblTo = new JLabel("To");
		lblTo.setBounds(319, 34, 46, 26);
		contentPane.add(lblTo);
		
		to = new JTextField();
		to.setEditable(false);
		to.setColumns(10);
		to.setBounds(354, 34, 120, 26);
		contentPane.add(to);
		
		send = new JButton("Send");
		send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(subject.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Missing Subjects" ,"Error", 0);
					return;
				}
				if(body.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Empty Emails" ,"Error", 0);
					return;
				}
				// Check every email in to_field exist
				String temp = to.getText();
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
					db.sendMessage(from.getText(), recepients[i], subject.getText(), body.getText());
					try {
						// Avoid the same time stamp
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				JOptionPane.showMessageDialog(null, "Message Sent" ,"Congratulation!", 1);
				dispose();
			}
		});
		send.setVisible(false);
		send.setBounds(0, 5, 123, 40);
		contentPane.add(send);
		
		save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				db.updateDraft(uid, subject.getText(), body.getText(), to.getText(), time.getText());
				System.out.println(uid + " " +  subject.getText() + " " +  body.getText()
				+ " " + to.getText() + " " + time.getText());
				dispose();
			}
		});
		save.setVisible(false);
		save.setBounds(329, 66, 145, 26);
		EmailGUI.hideTree();
		contentPane.add(save);
	}
	public JTextArea getBody() {
		return body;
	}
	public void setBody(String x) {
		body.setText(x);
	}
	public void setFrom(String from2) {
		if(from2 == ""){
			from.setText(EmailGUI.email_mainFrame.getTitle());
		}
		else{
			from.setText(from2);
		}
	}
	public void setTo(String to2) {
		if(to2 == ""){
			to.setText(EmailGUI.email_mainFrame.getTitle());
		}
		else{
			to.setText(to2);
		}
	}
	public void setTime(String time2) {
		time.setText(time2);
		
	}
	public void setSub(String subject2) {
		subject.setText(subject2);
		
	}
	public void editable() {
		
		body.setEditable(true);
		subject.setEditable(true);
		to.setEditable(true);
		Reply.setVisible(false);
		send.setVisible(true);
		save.setVisible(true);
		
	}
	
}
