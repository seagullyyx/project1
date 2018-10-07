public class Account {
	
	private int id;
	private String emailaddress;
	private Message[] messages;
	
	public void setMessage(Message[] x) {
		messages = x;
	}
	public Message[] getMessage() {
		return messages;
	}
	public Account(int i, String email) {
		id = i;
		emailaddress = email;
		messages = new Message[1];
	}
	
	public boolean isEmpty() {
		return messages == null;
	}
	
	public int getID() {
		return id;
	}
	
	public String getEmailAddress() {
		return emailaddress;
	}
	
	public void setID(int Id) {
		id = Id;
	}
	
	public void setEmailAddress(String email) {
		emailaddress = email;
	}
	
	public String toString() {
		return "I'm user: id is " + id + " and email is " + emailaddress;
	}
	
	
}