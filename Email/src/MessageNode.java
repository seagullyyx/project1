
public class MessageNode {
	private String to;
	private String from;
	private String subject;
	private String time;
	private String body;
	private String box;
	public MessageNode(String to, String from, String subject, String time, String body, String box) {
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
		else if(box.equals("Outbox")) {
			return  to + ",      " + subject + ",      " + time;
		}
		else{
			if(to.equals("") && subject.equals("")) {
				return "[no recepient]" + "       [no message]" + "      " + time;
			}
			else if(to.equals("")) {
				return "[no recepient]" + "       " + subject + ",      " + time;
			}
			else if(subject.equals("")) {
				return to + ",      [no message]" + "      " + time;
			}
			else return to + ",      " + subject + ",      " + time;
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
