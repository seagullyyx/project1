import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class Main
{	 
    public static void main(String args[]) {
        try {
        	ConnectDB db = new ConnectDB("jdbc:mysql://68.183.26.151:3306/EmailServer", "root", "P@ss1234");
			LoginGUI lgframe = new LoginGUI(db);
			LoginGUI.login_mainFrame.setLocationRelativeTo(null);
			LoginGUI.login_mainFrame.setVisible(true);

	
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}