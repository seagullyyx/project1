import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class DBDemo
{	 
    public static void main(String args[]) {
        try {
        	New_ConnectDB db = new New_ConnectDB("jdbc:mysql://68.183.26.151:3306/EmailServer", "root", "P@ss1234");
			loginGUI lgframe = new loginGUI(db);
			loginGUI.login_mainFrame.setLocationRelativeTo(null);
			loginGUI.login_mainFrame.setVisible(true);
			EmailGUI emframe = new EmailGUI(db);
			emframe.email_mainFrame.setLocationRelativeTo(null);
			emframe.email_mainFrame.setVisible(false);
		


            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}