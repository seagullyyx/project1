
public class Message {
	
	int id;
	String from;
	String to;
	String subject;
	String body;
	String time;
	
	public Message(String f, String t, String s, String b, String ti) {
		from = f;
		to = t;
		subject = s;
		body = b;
		time = ti;
	}
	
	public void setIDForInboxOrOutbox(int i) {
		id = i;
	}
	
	public String getFrom() {
		return from;
	}
	
	public String getTo() {
		return to;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public String getBody() {
		return body;
	}
	
	public String getTime() {
		return time;
	}
	
	public String toString() {
		return "From: " + from + " To: " + to + " Sub: " + subject + " Time: " + time;
	}

}