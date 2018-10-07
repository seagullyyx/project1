import java.util.ArrayList;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class New_ConnectDB {
	
	private String dbAddress;
	private String dbName;
	private String dbPassword;
	private java.sql.Connection conn;
	private Statement stmt;

	public New_ConnectDB(String dbPath, String name, String pw){
		dbAddress = dbPath;
		dbName = name;
		dbPassword = pw;
		if(CreateConnection()) System.out.println("You're successfully connected");
		else System.out.println("Something is wrong");
	}
	
	private boolean CreateConnection() {
		try {
			conn = DriverManager.getConnection(
					dbAddress, dbName, dbPassword
				);
			stmt = conn.createStatement();
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private int getNextUserID() {
		String query = "select Max(userid) as last from User";
		ResultSet lastID = getResultOf(query);
		int id = 0;
		try {
			if(lastID.next()) id = lastID.getInt("last");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id+1;
	}
	
	public void createAccount(String email, String pw) throws SQLIntegrityConstraintViolationException {
		try{
			String query = "INSERT INTO User VALUE (?, '"+email+"', '"+pw+"')";
			//System.out.println("Your query is: " + newUser);
			PreparedStatement prestmt = conn.prepareStatement(query);
			prestmt.setInt(1, getNextUserID());
			prestmt.executeUpdate();
		} catch(SQLIntegrityConstraintViolationException prime) {
			throw new SQLIntegrityConstraintViolationException();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	public Account getAccount(String email, String pw) {
		String query = "select userid from User where email = '"+email+"' and passwd = '"+pw+"'";
		ResultSet userid = getResultOf(query);
		int id = 1;
		try {
			if(userid.next()) id = userid.getInt("userid");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Account user = new Account(id, email);
		return user;
	}
	
	public boolean checkUser(String email, String pw) {
		String query = "select email from User where email = '"+email+"' and passwd = '"+pw+"'";
		ResultSet checkUser = getResultOf(query);
		boolean result = false;
		try {
			result = checkUser.next();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}
	
	public Message[] getInbox(String email) {
		int len = 0;
		try {
			String query = "select count(recepient) as num from Message where recepient = '"+email+"' group by recepient";
			ResultSet num = getResultOf(query);
			if(num.next()) len = num.getInt("num");
			System.out.println(len);	
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		
		Message [] messages = new Message[len];
		
		try {
			String query = "select sender, subject, body, senttime from Message where recepient = '"+email+"' ";
			ResultSet rs = getResultOf(query);

			int i=0;
		    while(rs.next()) {
				String from = rs.getString("sender");
				String sub = rs.getString("subject");
				String body = rs.getString("body");
				
				// Get string data type of time
				SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
				Date d = rs.getTimestamp("senttime");
				String time = df.format(d);
				messages[i++] = new Message(from, "", sub, body, time);
		    }
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		return messages;
	}
	
	public Message[] getOutbox(String email) {
		int len = 0;
		try {
			String query = "select count(sender) from Message where sender = '"+email+"' group by sender";
			ResultSet num = getResultOf(query);
			if(num.next()) len = num.getInt(1);
			System.out.println(len);
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		
		Message [] messages = new Message[len];
		
		try {
			String query = "select recepient, subject, body, senttime from Message where sender = '"+email+"' ";
			ResultSet rs = getResultOf(query);

			int i=0;
		    while(rs.next()) {
				String to = rs.getString("recepient");
				String sub = rs.getString("subject");
				String body = rs.getString("body");
				
				// Get string data type of time
				SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
				Date d = rs.getTimestamp("senttime");
				String time = df.format(d);
				messages[i++] = new Message("", to, sub, body, time);
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		return messages;
	}
	
	public void showUserInfo() {
		String query = "select userid, passwd, email from User";
		ResultSet userInfo = getResultOf(query);
		try {
			while(userInfo.next()) {
				int id = userInfo.getInt("userid");
				String pw = userInfo.getString("passwd");
				String email = userInfo.getString("email");
				System.out.println(id + ", " + email + ", " + pw);
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	private ResultSet getResultOf(String sqlQuery){
		try {
			ResultSet rset = stmt.executeQuery(sqlQuery);
			//System.out.println("Your query is: " + sqlQuery);
			return rset;
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			return null;
		} 
	}
	
	public void sendMessage(String from, String to, String sub, String body) {
		try{
			String query = "INSERT INTO Message(message_id, sender, recepient, subject, body, senttime, reply_to_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement prestmt = conn.prepareStatement(query);
			int thisID = getNextMessageID();
			prestmt.setInt(1, thisID);
			prestmt.setString(2, from);
			prestmt.setString(3, to);
			prestmt.setString(4, sub);
			prestmt.setString(5, body);
			prestmt.setTimestamp(6, getCurrentTime());
			prestmt.setInt(7, thisID);
			prestmt.executeUpdate();
			//System.out.println("Your query is: " + query);
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	public void sendReplyTo(String from, String to, String sub, String body, int parentID) {
		try{
			String query = "INSERT INTO Message(message_id, sender, recepient, subject, body, senttime, reply_to_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement prestmt = conn.prepareStatement(query);
			int thisID = getNextMessageID();
			prestmt.setInt(1, thisID);
			prestmt.setString(2, from);
			prestmt.setString(3, to);
			prestmt.setString(4, sub);
			prestmt.setString(5, body);
			prestmt.setTimestamp(6, getCurrentTime());
			prestmt.setInt(7, parentID);
			prestmt.executeUpdate();
			//System.out.println("Your query is: " + query);
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	private int getNextMessageID() {
		String query = "select Max(message_id) as last from Message";
		ResultSet lastID = getResultOf(query);
		int id = 0;
		try {
			if(lastID.next()) id = lastID.getInt("last");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id+1;
	}
	
	public boolean checkEmailExists(String recepient_email) {
		String query = "select email from User where email = '"+recepient_email+"'";
		ResultSet checkEmail = getResultOf(query);
		boolean result = false;
		try {
			result = checkEmail.next();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
	}
	
	private Timestamp getCurrentTime() {
		Date date = new Date();
		return new Timestamp(date.getTime()); 
	}
	
	public Message[] getDrafts(int userid) {
		int len = 0;
		try {
			String query = "select count(savedtime) as num from Draft where userid = '"+userid+"' group by userid";
			ResultSet rs = getResultOf(query);
			if(rs.next()) len = rs.getInt("num");	
			System.out.println(len);
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		
		Message [] messages = new Message[len];
		
		try {
			String query = "select sub, body, recepient, savedtime from Draft where userid = '"+userid+"' ";
			ResultSet rs = getResultOf(query);

			int i=0;
		    while(rs.next()) {
				String to = rs.getString("recepient");
				String sub = rs.getString("sub");
				String body = rs.getString("body");
				
				// Get string data type of time
				SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");
				Date d = rs.getTimestamp("savedtime");
				String time = df.format(d);
				messages[i++] = new Message("", to, sub, body, time);
		    }
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		return messages;
	}
	
	public void createNewDraft(int uid, String sub, String body, String recepient) {
		try {
			String query = "INSERT INTO Draft(userid, sub, body, recepient, savedtime) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement prestmt = conn.prepareStatement(query);
			prestmt.setInt(1, uid);
			prestmt.setString(2, sub);
			prestmt.setString(3, body);
			prestmt.setString(4, recepient);
			prestmt.setTimestamp(5, getCurrentTime());
			prestmt.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	public void updateDraft(int uid, String sub, String body, String to, String lastTime) {
		try {
			String query = "UPDATE Draft SET sub = ?, body = ?, recepient = ?, savedtime = ? WHERE userid = '"+uid+"' and savedtime = '"+lastTime+"'";
			PreparedStatement prestmt = conn.prepareStatement(query);
			prestmt.setString(1, sub);
			prestmt.setString(2, body);
			prestmt.setString(3, to);
			prestmt.setTimestamp(4, getCurrentTime());
			prestmt.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	public Message[] getMessageThread(int messageID) {
		ArrayList<Message> threadedList = new ArrayList<Message>();
		int replyToID = 0;
		try{
			while(messageID != replyToID) {
				String query = "select message_id, sender, recepient, senttime, subject, body, reply_to_id from Message where message_id = '"+messageID+"'";
				ResultSet rs = getResultOf(query);
				Message ms;
				if(rs.next()) {
					SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm");
					Date d = rs.getTimestamp("senttime");
					String time = df.format(d);
					replyToID = rs.getInt("message_id");
					messageID = rs.getInt("reply_to_id");
					ms = new Message(rs.getString("sender"),
									   rs.getString("recepient"),
									   rs.getString("subject"),
									   rs.getString("body"),
									   time);
					threadedList.add(ms);
				}
			}   
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
				
		int len = threadedList.size();
		Message[] threadedMessages = new Message[len];
		for(int i=0;i<len;i++) 
			threadedMessages[i] = threadedList.get(i);
		return threadedMessages;
	}

}