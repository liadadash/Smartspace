package smartspace.infra;

public class Message {
	private String name;

	public Message() {
	}
	
	public Message(String name) {
		super();
		this.name = name;
	}

	public String getText() {
		return "Hello " + name + "!";
	}

	public void setName(String name) {
		this.name = name;
	}
}
