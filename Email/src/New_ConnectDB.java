
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
		String query = "select email, passwd from User where email = '"+email+"' and passwd = '"+pw+"'";
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
			String query = "select sender, sub, body, sendtime from Message where recepient = '"+email+"' ";
			ResultSet rs = getResultOf(query);
			int i=0;
		    while(rs.next()) {
				String from = rs.getString("sender");
				String sub = rs.getString("sub");
				String body = rs.getString("body");
				
				// Get string data type of time
				SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
				Date d = rs.getTimestamp("sendtime");
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
			String query = "select recepient, sub, body, sendtime from Message where sender = '"+email+"' ";
			ResultSet rs = getResultOf(query);

			int i=0;
		    while(rs.next()) {
				String to = rs.getString("recepient");
				String sub = rs.getString("sub");
				String body = rs.getString("body");
				
				// Get string data type of time
				SimpleDateFormat df = new SimpleDateFormat("MM/dd/yy HH:mm:ss");
				Date d = rs.getTimestamp("sendtime");
				String time = df.format(d);
				messages[i++] = new Message("", to, sub, body, time);
			}
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		return messages;
	}
	
	public int getNumOfRows(String email, String type) {
		String query = "select count( '"+type+"' ) from Message where '"+type+"' = '"+email+"' group by '"+type+"'";
		ResultSet num = getResultOf(query);
		int result = -1;
		
		try {
			if(num.next()) result = num.getInt(1);
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
		return result;
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
			String query = "INSERT INTO Message(sender, recepient, sub, body, sendtime) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement prestmt = conn.prepareStatement(query);
			prestmt.setString(1, from);
			prestmt.setString(2, to);
			prestmt.setString(3, sub);
			prestmt.setString(4, body);
			prestmt.setTimestamp(5, getCurrentTime());
			prestmt.executeUpdate();
			//System.out.println("Your query is: " + query);
		} catch(SQLException sqle) {
			sqle.printStackTrace();
		}
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

}