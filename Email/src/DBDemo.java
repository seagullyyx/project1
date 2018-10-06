import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class DBDemo
{	 
    public static void main(String args[]) {
        try {
        	New_ConnectDB db = new New_ConnectDB("jdbc:mysql://68.183.26.151:3306/EmailServer", "root", "P@ss1234");
            //db.showUserInfo(); 
            /*db.sendMessage(
            	"kei@yg.com", 
            	"anik@yg.com", 
            	"So sleepy", 
            	"Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vitae, justo. Nullam dictum felis eu pede mollis pretium. Integer tincidunt. Cras dapibus. Vivamus elementum semper nisi. Aenean vulputate eleifend tellus. Aenean leo ligula, porttitor eu, consequat vitae, eleifend ac, enim. Aliquam lorem ante, dapibus in, viverra quis, feugiat a, tellus. Phasellus viverra nulla ut metus varius laoreet. Quisque rutrum. Aenean imperdiet. Etiam ultricies nisi vel augue. Curabitur ullamcorper ultricies nisi. Nam eget dui. Etiam rhoncus. Maecenas tempus, tellus eget condimentum rhoncus, sem quam semper libero, sit amet adipiscing sem neque sed ipsum. Nam quam nunc, blandit vel, luctus pulvinar, hendrerit id, lorem. Maecenas nec odio et ante tincidunt tempus. Donec vitae sapien ut libero venenatis faucibus. Nullam quis ante. Etiam sit amet orci eget eros faucibus tincidunt. Duis leo. Sed fringilla mauris sit amet nibh. Donec sodales sagittis magna. Sed consequat, leo eget bibendum sodales, augue velit cursus nunc, quis gravida magna mi a libero. Fusce vulputate eleifend sapien. Vestibulum purus quam, scelerisque ut, mollis sed, nonummy id, metus. Nullam accumsan lorem in dui. Cras ultricies mi eu turpis hendrerit fringilla. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; In ac dui quis mi consectetuer lacinia. Nam pretium turpis et arcu. Duis arcu tortor, suscipit eget, imperdiet nec, imperdiet iaculis, ipsum. Sed aliquam ultrices mauris. Integer ante arcu, accumsan a, consectetuer eget, posuere ut, mauris. Praesent adipiscing. Phasellus ullamcorper ipsum rutrum nunc. Nunc nonummy metus. Vestibulum volutpat pretium libero. Cras id dui. Aenean ut"
            );*/
            /*Scanner scan = new Scanner(System.in);
            String email, pw;
            System.out.println("Enter your email address: ");
            email = scan.next();
            System.out.println("Enter your pw: ");
            pw = scan.next();
            if(db.checkUser(email, pw)) {
            	System.out.println("Hello " + email);
            	Account user = db.getAccount(email, pw);
            	System.out.println(user);
            }
            else System.out.println("Bye");
            Message [] ms = db.getInbox(email);
            for(int i=0;i<ms.length;i++)
            	System.out.println(ms[i]);
            Message [] sm = db.getOutbox(email);
            for(int i=0;i<sm.length;i++)
            	System.out.println(sm[i]);*/
           
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