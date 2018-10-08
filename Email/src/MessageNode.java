
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
		String temp = "";
		if(subject.length() > 30) {
			for(int i = 0; i < 30; i++) {
				temp += Character.toString(subject.charAt(i));
			}
		}
		else temp = subject;
		if(box.equals("Inbox")) {
			return 	from + ",      " + temp + ",      " + time;
		}
		else if(box.equals("Outbox")) {
			return  to + ",      " + temp + ",      " + time;
		}
		else{
			if(to.equals("") && subject.equals("")) {
				return "[no recepient]" + "       [no message]" + "      " + time;
			}
			else if(to.equals("")) {
				return "[no recepient]" + "       " + temp + ",      " + time;
			}
			else if(subject.equals("")) {
				return to + ",      [no message]" + "      " + time;
			}
			else return to + ",      " + temp + ",      " + time;
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
