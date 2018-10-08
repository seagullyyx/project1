import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class ConnectDB {
	
	private String dbAddress;
	private String dbName;
	private String dbPassword;
	private java.sql.Connection conn;
	private Statement stmt;

	public ConnectDB(String dbPath, String name, String pw){
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
	
	private ResultSet getResultOf(String sqlQuery){
		try {
			ResultSet rset = stmt.executeQuery(sqlQuery);
			return rset;
		} catch(SQLException sqle) {
			sqle.printStackTrace();
			return null;
		} 
	}
	
	public void sendMessage(String from, String to, String sub, String body) {
		try{
			String query = "INSERT INTO Message(message_id, sender, recepient, subject, body, senttime) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement prestmt = conn.prepareStatement(query);
			int thisID = getNextMessageID();
			prestmt.setInt(1, thisID);
			prestmt.setString(2, from);
			prestmt.setString(3, to);
			prestmt.setString(4, sub);
			prestmt.setString(5, body);
			prestmt.setTimestamp(6, getCurrentTime());
			prestmt.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	public void sendReplyTo(String from, String to, String sub, String body) {
		try{
			String query = "INSERT INTO Message(message_id, sender, recepient, subject, body, senttime) VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement prestmt = conn.prepareStatement(query);
			int thisID = getNextMessageID();
			prestmt.setInt(1, thisID);
			prestmt.setString(2, from);
			prestmt.setString(3, to);
			prestmt.setString(4, sub);
			prestmt.setString(5, body);
			prestmt.setTimestamp(6, getCurrentTime());
			prestmt.executeUpdate();
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
				
				SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
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
			String query = "UPDATE Draft SET sub = ?, body = ?, recepient = ?, savedtime = ? WHERE userid = ? and savedtime = ?";
			
			PreparedStatement prestmt = conn.prepareStatement(query);
			prestmt.setString(1, sub);
			prestmt.setString(2, body);
			prestmt.setString(3, to);
			prestmt.setTimestamp(4, getCurrentTime());
			prestmt.setInt(5, uid);
		
		    prestmt.setTimestamp(6, convertStringToTimestamp(lastTime));
			prestmt.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	public Timestamp convertStringToTimestamp(String time) {
		Timestamp timestamp = null;
		try {
			SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy hh:mm:ss");
		    Date date = df.parse(time);
		    timestamp = new Timestamp(date.getTime());
		} catch(Exception e) { 
			e.printStackTrace();
		}
		return timestamp;
	}
	
	public void deleteSentMessage(String from, String to, String time) {
		try {
			String query = "DELETE FROM Message WHERE sender = ? and recepient = ? and senttime = ?";
			PreparedStatement prestmt = conn.prepareStatement(query);
			prestmt.setString(1, from);
			prestmt.setString(2, to);
			prestmt.setTimestamp(3, convertStringToTimestamp(time));
			prestmt.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}
	
	public void deleteDraftMessage(int userid, String time) {
		try {
			String query = "DELETE FROM Draft WHERE userid = ? and savedtime = ?";
			PreparedStatement prestmt = conn.prepareStatement(query);
			prestmt.setInt(1, userid);
			prestmt.setTimestamp(2, convertStringToTimestamp(time));
			prestmt.executeUpdate();
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
	}	
	
}