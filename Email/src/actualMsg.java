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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class actualMsg extends JFrame {

	private JPanel contentPane;
	private JTextArea body;
	private JTextField subject;
	private JTextField from;
	private JTextField time;
	private JTextField to;
	private msg object;
	/**
	 * Create the frame.
	 */
	
	public actualMsg(msg i) {
		object = i;
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
		
		JButton Reply = new JButton("Reply");
		Reply.setBounds(0, 5, 123, 40);
		contentPane.add(Reply);
		
		JButton Delete = new JButton("Delete");
		Delete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		time.setBounds(189, 66, 285, 26);
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
		
		JButton send = new JButton("Send");
		send.setVisible(false);
		send.setBounds(0, 5, 123, 40);
		contentPane.add(send);
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
	
}
