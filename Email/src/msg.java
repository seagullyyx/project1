
public class msg {
	private String to;
	private String from;
	private String subject;
	private String time;
	private String body;
	private String box;
	public msg(String to, String from, String subject, String time, String body, String box ) {
		this.to = to;
		this.from = from;
		this.subject = subject;
		this.time = time;
		this.body = body;
		this.box = box;
	
	}
	@Override
	public String toString() {
		if(box.equals("Inbox")) {
			return 	from + ",      " + subject + ",      " + time;
		}
		else {
			return  to + ",      " + subject + ",      " + time;
		}
	}
	public String getTo() {
		return to;
	}
	public String getFrom() {
		return from;
	}
	public String getSubject() {
		return subject;
	}
	public String getTime() {
		return time;
	}
	public String getBox() {
		return box;
	}
	public String getBody() {
		return body;
	}
	
}